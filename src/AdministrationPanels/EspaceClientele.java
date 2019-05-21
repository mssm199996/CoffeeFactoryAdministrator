package AdministrationPanels;

import MainPackage.IOTransactions;
import MainPackage.MainWindow;
import ModeledWindows.FicheClient;
import ModeledWindows.IntroductionWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sidi Mohamed
 */
public class EspaceClientele extends EspacePersonel{
    Tableau tableauVersements;
    public EspaceClientele(){
        super();
        this.tableauVersements = new Tableau();
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
    }
    public void resetInformations(){
        this.labelDateEnregistrement.setText("?");
        this.labelDateNaissance.setText("?");
        this.majActivite.setSelectedIndex(0);
        this.majConfiance.setSelectedIndex(0);
        this.majSexe.setSelectedIndex(0);
        this.majSituation.setSelectedIndex(0);
        for(int i = 6 ; i < 15; i++){
            this.champRecherche[i].setText("?");
        }
    }
    class TableModel extends DefaultTableModel{
        public TableModel(){
            super(new String[]{"N°","Date du versement","Temps du versement","Valeur du versement"}, 0);
        }
    }
    class Tableau extends JTable{
        public TableModel tModel ;
        public Tableau(){
            super();
            this.initComponent();
        }
        private void initComponent(){
            this.tModel = new TableModel();
            this.setModel(tModel);
            this.setGridColor(new java.awt.Color(0, 0, 0));
            this.setRowHeight(23);
            this.setGridColor(Color.black);
            this.setShowVerticalLines(true);
            this.setShowHorizontalLines(true);
            this.setFillsViewportHeight(true);
            this.setFillsViewportHeight(true);
            this.getColumnModel().getColumn(0).setPreferredWidth(20);
            this.getColumnModel().getColumn(1).setPreferredWidth(150);
            this.getColumnModel().getColumn(2).setPreferredWidth(200);
            this.getColumnModel().getColumn(3).setPreferredWidth(400);
            this.setSelectionBackground(new java.awt.Color(255, 255, 255));
            this.setSelectionForeground(new java.awt.Color(0, 0, 0));
        }
        public void clearTable(){
            while(this.getRowCount() != 0){
                this.tModel.removeRow(0);
            }
        }
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
    void initComponent(){
        super.initComponent();
        this.ImportPersonnelsFromDataBase();
        this.ImportWinnersFromDatabase();
        this.ficheIntroduction = new FicheClient();
        this.recherchePersonnel.addKeyListener(new PersonnelsSeaching());
        this.listePersonnels.addListSelectionListener(new PersonnelsInformations());
        this.listeSecondaireClient.addListSelectionListener(new PromotionInformation());
        this.confirmerChangementPersonnel.addActionListener(new ModifierInfosPersonnel());
        this.ficheIntroduction.confirmer.addActionListener(new addNewClient());
        this.confirmerRecherche.addActionListener(new rechercheAvance());
        this.promouvoir.addActionListener(new promouvoirPersonne());
        this.effacerPersonel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                effacerClient();
            }
        });
        this.infosPromotion.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                promotionInformations();
            }
        });
        this.infosVersements.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               FenetreInterval fi = new FenetreInterval(null,"Interval de recherche",true);
           } 
        });
    }
    private void promotionInformations(){
        try{
            int nombre = getSelectePersonelNumber(this.listePersonnels);
            Statement state = IOTransactions.getConnectionUtility().createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultatRecherche = state.executeQuery("select * from FIDELITE_CLIENT where NUM_CLIENT = '"+Integer.toString(nombre)+"'");
            resultatRecherche.absolute(1);
            int points = resultatRecherche.getInt("POINTS_CLIENT");
            int recompenses = resultatRecherche.getInt("RECOMPENSE_CLIENT");
            JOptionPane option = new JOptionPane();
            option.showMessageDialog(null, "<html>Le client<BR>"+this.listePersonnels.getSelectedValue().toString()
                    +"<BR>Dispose de:<BR>Nombre de points: "+Integer.toString(points)+"<BR>Nombre de recompenses: "+
                    Integer.toString(recompenses), "Promotion Informations",JOptionPane.INFORMATION_MESSAGE);
            resultatRecherche.close();
            state.close();
        }
        catch(NullPointerException exp){
            exp.printStackTrace();
            JOptionPane op = new JOptionPane();
            op.showMessageDialog(null, "Veuillez verifier que vous avez bien selectionné un client"
                    + "", "Error...", JOptionPane.ERROR_MESSAGE);
        }
        catch(ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
    }
    private void versementsInformations(String date1 , String date2){
        try{
            int nombre = getSelectePersonelNumber(this.listePersonnels);
            Statement state = IOTransactions.getConnectionUtility().createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet result = state.executeQuery("select * from VERSEMENTS_DETTES where NUM_CLIENT = " + nombre + " and "
                    + "DATE_VERSEMENT between '"+transcriptDateEnregistrement(date1)+"' "
                    + "and '"+transcriptDateEnregistrement(date2)+ "'");
            this.tableauVersements.clearTable();
            int i = 1;
            while(result.next()){
                result.absolute(i);
                Object[] ligneDuTableau = {i,this.translateDateEnregistrement(result.getDate("DATE_VERSEMENT").toString()),result.getTime("TEMPS_VERSEMENT"),
                    result.getDouble("VALEUR_VERSEMENT")};
                this.tableauVersements.tModel.addRow(ligneDuTableau);
                i++;
            }
            JScrollPane p = new JScrollPane(this.tableauVersements,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            p.setBackground(Color.white);
            JFrame f = new JFrame();
            f.setSize(800,600);
            f.setLocationRelativeTo(null);
            f.setTitle("Resultat de la recherche des versements");
            f.setContentPane(p);
            f.setVisible(true);
        }
        catch(NullPointerException exp){
            exp.printStackTrace();
            JOptionPane op = new JOptionPane();
            op.showMessageDialog(null, "Veuillez verifier que vous avez bien selectionné un client"
                    + "", "Error...", JOptionPane.ERROR_MESSAGE);
        }
        catch(ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
    }
    private void effacerClient(){
        JOptionPane option = new JOptionPane();
        int reponse = JOptionPane.NO_OPTION;
        try{
            reponse = option.showConfirmDialog(null, "<html>Voulez vous vraiment supprimer le client<BR>"
                +this.listePersonnels.getSelectedValue().toString(), "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        }
        catch(NullPointerException exp){
            JOptionPane op = new JOptionPane();
            op.showMessageDialog(null, "Veuillez verifier que vous avez bien selectionné un client"
                    + "", "Error...", JOptionPane.ERROR_MESSAGE);
        }
        if(reponse == JOptionPane.YES_OPTION){
            this.rechercheListModel.clear();
            try{
                Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                state.executeUpdate("delete from CLIENT where NUM_CLIENT = " + getSelectePersonelNumber(this.listePersonnels));
                state.executeUpdate("delete from FIDELITE_CLIENT where NUM_CLIENT = " + getSelectePersonelNumber(this.listePersonnels));
                state.close();
            }
            catch(NullPointerException | ClassNotFoundException | SQLException exp){
                exp.printStackTrace();
            }
            ImportPersonnelsFromDataBase();
            EspaceClientele.ImportWinnersFromDatabase();
            MainWindow.efa.ImportPersonnelsFromDataBase();
            MainWindow.efa.pInfos.numClient.setText("?");
        }
    }
    public void ImportPersonnelsFromDataBase(){
        try{
            this.personelsListModel.clear();
            Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultatRecherche = state.executeQuery("select * from CLIENT");
            int i = 1 ;
            while(resultatRecherche.next()){
                resultatRecherche.absolute(i);
                personelsListModel.addElement(resultatRecherche.getInt("NUM_CLIENT")+"- "
                        +resultatRecherche.getString("NOM_CLIENT")+ " " + transformAsName(resultatRecherche.getString("PRENOM_CLIENT")));
                i++;
            }
            resultatRecherche.close();
            state.close();
        }
        catch(NullPointerException | ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
    }
    public static void ImportWinnersFromDatabase(){
        try{
            secondaireListModelClient.clear();
            int k = 0;
            Statement state = IOTransactions.getConnectionUtility().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultatRecherche = state.executeQuery("select NUM_CLIENT,POINTS_CLIENT from FIDELITE_CLIENT");
            ArrayList<Integer> numeroClients = new ArrayList<Integer>();
            int i = 1;
            while(resultatRecherche.next()){
                resultatRecherche.absolute(i);
                if(resultatRecherche.getInt("POINTS_CLIENT") >= 10)numeroClients.add(resultatRecherche.getInt("NUM_CLIENT"));
                i++;
            }
            resultatRecherche.close();
            for(int numero : numeroClients){
                resultatRecherche = state.executeQuery("select NOM_CLIENT,PRENOM_CLIENT from CLIENT where NUM_CLIENT = " + numero);
                resultatRecherche.absolute(1);
                secondaireListModelClient.addElement(Integer.toString(numero)+"- "
                        +resultatRecherche.getString("NOM_CLIENT")+" "+transformAsName(resultatRecherche.getString("PRENOM_CLIENT")));
            }
            if(!secondaireListModelClient.isEmpty()){
                String[] tabStr = new String[secondaireListModelClient.getSize()];
                for(int indice = 0 ; indice < secondaireListModelClient.getSize() ; indice++){
                    tabStr[indice] = ((String)secondaireListModelClient.get(indice));
                }   
                listeSecondaireClient.setListData(tabStr);
            }
            else listeSecondaireClient.setListData(new String[0]);
            resultatRecherche.close();
            state.close();
        }
        catch(NullPointerException | ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
    }
    class PersonnelsSeaching implements KeyListener{
        public void keyPressed(KeyEvent e) {
        }
        public void keyTyped(KeyEvent e) {
        }
        public void keyReleased(KeyEvent e) {
            try{
                personelsListModel.clear();
                Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultatRecherche = state.executeQuery("select * from CLIENT where NOM_CLIENT like '%" 
                        + recherchePersonnel.getText().toUpperCase() + "%' or PRENOM_CLIENT like '%" + recherchePersonnel.getText().toLowerCase() + "%'");
                int i = 1 ;
                while(resultatRecherche.next()){
                    resultatRecherche.absolute(i);
                    personelsListModel.addElement(resultatRecherche.getInt("NUM_CLIENT")
                            +"- "+resultatRecherche.getString("NOM_CLIENT")+ " " + transformAsName(resultatRecherche.getString("PRENOM_CLIENT")));
                    i++;
                }
                resultatRecherche.close();
                state.close();
            }
            catch(NullPointerException | ClassNotFoundException | SQLException exp){
                exp.printStackTrace();
            }
        }
    }
    class PersonnelsInformations implements ListSelectionListener{
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(!e.getValueIsAdjusting()){
                try{
                    int nombre = getSelectePersonelNumber(listePersonnels);
                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultatRecherche = state.executeQuery("select * from CLIENT where NUM_CLIENT = '"+Integer.toString(nombre)+"'");
                    resultatRecherche.absolute(1);
                    MainWindow.deb.setInformations(nombre, resultatRecherche.getString("NOM_CLIENT") + " " 
                            + transformAsName(resultatRecherche.getString("PRENOM_CLIENT")),
                            resultatRecherche.getDouble("RESTE_A_PAYER_HT"),
                            resultatRecherche.getDouble("BALANCE_PAIEMENT"));
                    String column = null;
                    for(int i = 0 ; i < 15 ; i++){
                        switch(i){
                            case 0:
                                column = "DATE_FICHE";
                                break;
                            case 1:
                                column = "DDN_CLIENT";
                                break;
                            case 2:
                                column = "TYPE_COMMERCE";
                                break;
                            case 3:
                                column = "CONFIANCE_CLIENT";
                                break;
                            case 4:
                                column = "SEXE_CLIENT";
                                break;
                            case 5:
                                column = "SITUATION_SOCIALE";
                                break;
                            case 6:
                                column = "ADRESSE_CLIENT";
                                break;
                            case 7:
                                column = "CODE_POSTAL_CLIENT";
                                break;
                            case 8:
                                column = "PAYS_CLIENT";
                                break;
                            case 9:
                                column = "WILAYA_CLIENT";
                                break;
                            case 10:
                                column = "TELEPHONE_BUREAU_CLIENT";
                                break;
                            case 11:
                                column = "TELEPHONE_PORTABLE_CLIENT";
                                break;
                            case 12:
                                column = "EMAIL_CLIENT";
                                break;
                            case 13:
                                column = "FAX_CLIENT";
                                break;
                            case 14:
                                column = "SOCIETE";
                                break;
                        }
                        if(i == 0)labelDateEnregistrement.setText(translateDateEnregistrement(resultatRecherche.getString(column)));
                        else if(i == 1)labelDateNaissance.setText(resultatRecherche.getString(column));
                        else if(i == 2){
                            String[] tabRechercheTypeCommerceClient = {"Boutique","Magasin" ,"Mouturier","Super marché","Centre commercial","Cafeteria"};
                            majActivite.removeAllItems();
                            majActivite.addItem(resultatRecherche.getString(column));
                            addTab(majActivite,tabRechercheTypeCommerceClient);
                        }
                        else if(i == 3){
                            String[] tabRechercheNiveauConfianceClient = {"S","A","B","C","D"};
                            majConfiance.removeAllItems();
                            majConfiance.addItem(resultatRecherche.getString(column));
                            addTab(majConfiance,tabRechercheNiveauConfianceClient);
                        }
                        else if(i == 4){
                            String[] tabSexe = {"Masculin","Feminin "};
                            majSexe.removeAllItems();
                            majSexe.addItem(resultatRecherche.getString(column));
                            addTab(majSexe,tabSexe);
                        }
                        else if(i == 5){
                            String[] tabSituation = {"Celibataire","Marie(e)","Divorce(e)"};
                            majSituation.removeAllItems();
                            majSituation.addItem(resultatRecherche.getString(column));
                            addTab(majSituation,tabSituation);        
                        }
                        else champRecherche[i].setText(transcriptToReading(resultatRecherche.getString(column)));
                    }
                    verifierClient(nombre,resultatRecherche.getString("NOM_CLIENT")+" "
                            +resultatRecherche.getString("PRENOM_CLIENT"),resultatRecherche.getInt("COULEUR"));
                    resultatRecherche.close();
                    state.close();
                }
                catch(NullPointerException | ClassNotFoundException | SQLException exp){
                    exp.printStackTrace();
                }
            }
        }
    }
    public void verifierClient(int nombre,String nom,int code){
        switch(code){
            case -1 :
                EspaceBusiness.pTabbedDroite.getPersonalData().setForeground(Color.red);
                EspaceBusiness.pTabbedDroite.getPersonalName().setForeground(Color.red);
                break;
            case 0:
                EspaceBusiness.pTabbedDroite.getPersonalData().setForeground(new Color(0,150,0));
                EspaceBusiness.pTabbedDroite.getPersonalName().setForeground(new Color(0,150,0));
                break;
            case 1:
                EspaceBusiness.pTabbedDroite.getPersonalData().setForeground(Color.blue);
                EspaceBusiness.pTabbedDroite.getPersonalName().setForeground(Color.blue);
                break;
        }
        EspaceBusiness.pTabbedDroite.getPersonalData().setText(nombre < 10 ? "0"+Integer.toString(nombre) : Integer.toString(nombre));
        EspaceBusiness.pTabbedDroite.getPersonalName().setText(nom);
    }
    class PromotionInformation implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e) {
            if(!e.getValueIsAdjusting()){
                try{
                    int nombre = getSelectePersonelNumber(listeSecondaireClient);
                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultatRecherche = state.executeQuery("select * from FIDELITE_CLIENT where NUM_CLIENT = '"+Integer.toString(nombre)+"'");
                    resultatRecherche.absolute(1);
                    String column = null;
                    for(int i = 15 ; i < 17 ; i++){
                        switch(i){
                            case 15:
                                column = "POINTS_CLIENT";
                                break;
                            case 16:
                                column = "RECOMPENSE_CLIENT";
                                break;
                        }
                        champRecherche[i].setText(Integer.toString(resultatRecherche.getInt(column)));
                    }
                    resultatRecherche.close();
                    state.close();
                }
                catch(NullPointerException | ClassNotFoundException | SQLException exp){
                    exp.printStackTrace();
                }
            }
        }
    }
    class ModifierInfosPersonnel implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                int numero = getSelectePersonelNumber(listePersonnels);
                Statement state = IOTransactions.getConnectionUtility().createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultat = state.executeQuery("select * from CLIENT where NUM_CLIENT = '"+Integer.toString(numero)+ "'");
                resultat.first();
                String column = null;
                String value = null;
                for(int i = 1 ; i < 15 ; i++){
                    switch(i){
                        case 1:
                            column = "DDN_CLIENT";
                            value = majNaissance.getInformations();
                            break;
                        case 2:
                            column = "TYPE_COMMERCE";
                            value = majActivite.getSelectedItem().toString();
                            break;
                        case 3:
                            column = "CONFIANCE_CLIENT";
                            value = majConfiance.getSelectedItem().toString();
                            break;
                        case 4:
                            column = "SEXE_CLIENT";
                            value = majSexe.getSelectedItem().toString();
                            break;
                        case 5:
                            column = "SITUATION_SOCIALE";
                            value = majSituation.getSelectedItem().toString();
                            break;
                        case 6:
                            column = "ADRESSE_CLIENT";
                            value = champRecherche[i].getText().toUpperCase();
                            break;
                        case 7:
                            column = "CODE_POSTAL_CLIENT";
                            value = champRecherche[i].getText();
                            break;
                        case 8:
                            column = "PAYS_CLIENT";
                            value = champRecherche[i].getText().toUpperCase();
                            break;
                        case 9:
                            column = "WILAYA_CLIENT";
                            value = champRecherche[i].getText().toUpperCase();
                            break;
                        case 10:
                            column = "TELEPHONE_BUREAU_CLIENT";
                            value = champRecherche[i].getText();
                            break;
                        case 11:
                            column = "TELEPHONE_PORTABLE_CLIENT";
                            value = champRecherche[i].getText();
                            break;
                        case 12:
                            column = "EMAIL_CLIENT";
                            value = champRecherche[i].getText();
                            break;
                        case 13:
                            column = "FAX_CLIENT";
                            value = champRecherche[i].getText();
                            break;
                        case 14:
                            column = "SOCIETE";
                            value = champRecherche[i].getText();
                            break;
                    }
                    resultat.updateString(column, value);
                }
                resultat.updateRow();
                resultat.close();
                state.close();
            }
            catch(NullPointerException | ClassNotFoundException | SQLException exp){
                exp.getStackTrace();
            }
        }
    }
    class addNewClient implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(ficheIntroduction.formulaires[0].getInformation().equals("") || ficheIntroduction.formulaires[1].getInformation().equals("")){
                JOptionPane option = new JOptionPane();
                option.showMessageDialog(null, "Vous avez oublié de mentioner un champ important", "Empty text error...",JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultat = state.executeQuery("SELECT * FROM CLIENT");
                    resultat.last();
                    int numeroNewClient ;
                    try{
                        numeroNewClient = resultat.getInt("NUM_CLIENT")+1;
                    }
                    catch(NullPointerException exp){
                        numeroNewClient = 1;
                    }
                    state.executeUpdate("INSERT INTO CLIENT (DATE_FICHE,NOM_CLIENT, PRENOM_CLIENT, DDN_CLIENT, SITUATION_SOCIALE, "
                        + "ADRESSE_CLIENT, CODE_POSTAL_CLIENT, PAYS_CLIENT, EMAIL_CLIENT, TELEPHONE_PORTABLE_CLIENT,TELEPHONE_BUREAU_CLIENT,"
                        + " FAX_CLIENT, SOCIETE,NUM_CLIENT, SEXE_CLIENT, TYPE_COMMERCE,CONFIANCE_CLIENT,WILAYA_CLIENT,NUMERO_FISCAL_CLIENT,"
                            + "RC,BALANCE_PAIEMENT,COULEUR,RESTE_A_PAYER_HT)"
                        + " VALUES ("
                        + "CURRENT_DATE, "
                        + "'" + ficheIntroduction.formulaires[0].getInformation().toUpperCase() + "',"
                        + "'" + ficheIntroduction.formulaires[1].getInformation().toLowerCase() + "',"
                        + "'" + ficheIntroduction.formulaireNaissance.getInformations() + "',"
                        + "'" + ficheIntroduction.situationSociale.getSelectedItem().toString() + "',"
                        + "'" + ficheIntroduction.formulaires[9].getInformation().toUpperCase() + "',"
                        + "'" + ficheIntroduction.formulaires[10].getInformation() + "',"
                        + "'" + ficheIntroduction.formulaires[11].getInformation().toUpperCase() + "',"
                        + "'" + ficheIntroduction.formulaires[15].getInformation() + "',"
                        + "'" + ficheIntroduction.formulaires[14].getInformation() + "',"
                        + "'" + ficheIntroduction.formulaires[13].getInformation() + "',"
                        + "'" + ficheIntroduction.formulaires[16].getInformation() + "',"
                        + "'" + ficheIntroduction.formulaires[17].getInformation() + "',"
                        + numeroNewClient + ","
                        + "'" + ficheIntroduction.getSexeInformation() + "',"
                        + "'" + (ficheIntroduction.typeActivite.getSelectedItem()).toString() + "',"
                        + "'" + (ficheIntroduction.niveauConfiance.getSelectedItem().toString()) + "',"
                        + "'" + (ficheIntroduction.formulaires[12].getInformation().toUpperCase()) + "',"
                        + "'" + (ficheIntroduction.formulaires[7].getInformation()) + "','"
                        + (ficheIntroduction.formulaires[8].getInformation()) + "',"
                            + 0 +","+0+","+0+")");
                    state.executeUpdate("insert into FIDELITE_CLIENT (NUM_SERIE,NUM_CLIENT,POINTS_CLIENT,RECOMPENSE_CLIENT,QUANTITE_VRAC_RESTANT,QUANTITE_CONDITIONNE_RESTANT) VALUES("
                        + numeroNewClient +","
                        + numeroNewClient + ","
                        + 0 + ","
                        + 0 + ","
                        + 0 + ","
                        + 0 + ")");
                    resultat.close();
                    state.close();
                    personelsListModel.clear();
                    ImportPersonnelsFromDataBase();
                    MainWindow.efa.ImportPersonnelsFromDataBase();
                }
                catch(NullPointerException | SQLException | ClassNotFoundException exp){
                    exp.printStackTrace();
                }
                for(int i = 0 ; i <= 17 ; i++)if(i < 2 || i > 6)ficheIntroduction.formulaires[i].clearFormulaire();
                ficheIntroduction.setVisible(false);
            }
        }    
    }
    class rechercheAvance implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String column = null;
            String details = null;
            rechercheListModel.clear();
            switch(rechercheOption.getSelectedIndex()){
                case 0:
                    column = "DATE_FICHE";
                    details = transcriptDateEnregistrement(rechercheDate.getInformations());
                    break;
                case 1:
                    column = "DDN_CLIENT";
                    details = rechercheDate.getInformations();
                    break;
                case 2:
                    column = "TYPE_COMMERCE";
                    details = rechercheTypeCommerce.getSelectedItem().toString();
                    break;
                case 3:
                    column = "CONFIANCE_CLIENT";
                    details = rechercheNiveauConfiance.getSelectedItem().toString();
                    break;
                case 4:
                    column = "SEXE_CLIENT";
                    details = rechercheComboSexe.getSelectedItem().toString();
                    break;
                case 5:
                    column = "SITUATION_SOCIALE";
                    details = rechercheComboSituation.getSelectedItem().toString();
                    break;
                case 6:
                    column = "ADRESSE_CLIENT";
                    details = rechercheText.getText().toUpperCase();
                    break;
                case 7:
                    column = "CODE_POSTAL_CLIENT";
                    details = rechercheText.getText();
                    break;
                case 8:
                    column = "PAYS_CLIENT";
                    details = rechercheText.getText().toUpperCase();
                    break;
                case 9:
                    column = "WILAYA_CLIENT";
                    details = rechercheText.getText().toUpperCase();
                    break;
            }
            try{
                Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultat = null;
                if(rechercheOption.getSelectedIndex() < 6){
                    resultat = state.executeQuery("select NUM_CLIENT,NOM_CLIENT,PRENOM_CLIENT from CLIENT "
                        + "where "+column+" = '"+details+"'");
                    int i = 1;
                    while(resultat.next()){
                        resultat.absolute(i);
                        rechercheListModel.addElement(resultat.getInt("NUM_CLIENT")+"- "
                            +resultat.getString("NOM_CLIENT")+" "+transformAsName(resultat.getString("PRENOM_CLIENT")));
                        i++;
                    }
                    resultat.close();
                    state.close();
                }
                else {
                    if(rechercheText.getText().equals("")){
                        JOptionPane option = new JOptionPane();
                        option.showMessageDialog(null, "Le champ d'information est vide", "Error...", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        if(rechercheOption.getSelectedIndex() == 7)resultat = state.executeQuery("select NUM_CLIENT,NOM_CLIENT,PRENOM_CLIENT from CLIENT "
                            +"where "+column + " like '%" + details +"%'");
                        else resultat = state.executeQuery("select NUM_CLIENT,NOM_CLIENT,PRENOM_CLIENT from CLIENT "
                            +"where "+column + " = '" + details +"'");
                        int i = 1;
                        while(resultat.next()){
                            resultat.absolute(i);
                            rechercheListModel.addElement(resultat.getInt("NUM_CLIENT")+"- "
                                +resultat.getString("NOM_CLIENT")+" "+transformAsName(resultat.getString("PRENOM_CLIENT")));
                            i++;
                        }
                        resultat.close();
                        state.close();
                    }
                }
                
            }
            catch(NullPointerException | ClassNotFoundException | SQLException exp){
                exp.printStackTrace();
            }
        }
    }
    class promouvoirPersonne implements ActionListener {
        public void actionPerformed(ActionEvent e){
            try{
                int numero = getSelectePersonelNumber(listeSecondaireClient);
                int points = Integer.parseInt(champRecherche[15].getText())-10;
                int cadeaux = Integer.parseInt(champRecherche[16].getText())+1;
                Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultat = state.executeQuery("select * from FIDELITE_CLIENT where NUM_CLIENT = '"+Integer.toString(numero)+"'");
                resultat.first();
                resultat.updateInt("POINTS_CLIENT",points);
                resultat.updateInt("RECOMPENSE_CLIENT",cadeaux);
                resultat.updateRow();
                if(points < 10){
                    ImportWinnersFromDatabase();
                    champRecherche[15].setText("");
                    champRecherche[16].setText("");
                }
                else {
                    champRecherche[15].setText(Integer.toString(points));
                    champRecherche[16].setText(Integer.toString(cadeaux));
                }
                resultat.close();
                state.close();
            }
            catch(NullPointerException | ClassNotFoundException | SQLException exp){
                JOptionPane option = new JOptionPane();
                option.showMessageDialog(null, "Veuillez verifier si vous avez bien selectionné un client !"
                        , "Error...", JOptionPane.ERROR_MESSAGE);
                exp.printStackTrace();
            }
        }
    }
    class PanneauInfo extends JPanel{
        public FormulaireDate dateA;
        public FormulaireDate dateB;
        public JButton confirmer;
        public JButton annuler;
        public PanneauInfo(){
            this.setLayout(new FlowLayout());
            this.setBorder(MainWindow.bordure);
            this.setPreferredSize(new Dimension(200,20));
            this.setBackground(Color.white);
            this.initComponent();
            this.initListeners();
        }
        private void initComponent(){
            this.dateA = new FormulaireDate();
            this.dateB = new FormulaireDate();
            this.confirmer = new JButton("Confirmer");
            this.annuler = new JButton("Annuler");
            this.confirmer.setPreferredSize(new Dimension(120,30));
            this.annuler.setPreferredSize(new Dimension(120,30));
            this.add(new Formulaire("Date n° 1:",this.dateA,new Dimension(60,35)));
            this.add(new Formulaire("Date n° 2:",this.dateB,new Dimension(60,35)));
            this.add(this.confirmer);
            this.add(this.annuler);
        }
        private void initListeners(){
        
        }
    }
    class FenetreInterval extends JDialog{
        public PanneauInfo pi;
        public FenetreInterval(JFrame f,String str,boolean b){
            super(f,str,b);
            this.setSize(300, 175);
            this.setLocationRelativeTo(null);
            this.initComponents();
            this.setResizable(false);
            this.setVisible(true);
        }
        private void initComponents(){
            this.pi = new PanneauInfo();
            this.pi.annuler.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent e){
                   setVisible(false);
               }
            });
            this.pi.confirmer.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent e){
                   setVisible(false);
                   versementsInformations(pi.dateA.getInformations(),pi.dateB.getInformations());
               }
            });
            this.add(this.pi);
        }
    }
}
