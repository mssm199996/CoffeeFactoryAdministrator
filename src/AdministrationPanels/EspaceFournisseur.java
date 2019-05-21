/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AdministrationPanels;

import MainPackage.IOTransactions;
import ModeledWindows.FicheFournisseur;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import MainPackage.DoubleStocker;
import ModeledWindows.IntroductionWindow;

/**
 *
 * @author Sidi Mohamed
 */
public class EspaceFournisseur extends EspacePersonel{
    public static DoubleStocker<Integer,Integer> listeProvisoire = new DoubleStocker<Integer,Integer>();
    public EspaceFournisseur(){
        super();
        this.promouvoir.setVisible(false);
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
    }
    void initComponent(){
        super.initComponent();
        this.ImportPersonnelsFromDataBase();
        this.ImportDataListeSecondaire();
        this.ficheIntroduction = new FicheFournisseur();
        this.recherchePersonnel.addKeyListener(new PersonnelsSeaching());
        this.listePersonnels.addListSelectionListener(new PersonnelsInformations());
        this.ficheIntroduction.confirmer.addActionListener(new addNewFournisseur());
        this.confirmerRecherche.addActionListener(new rechercheAvance());
        this.listeSecondaireFournisseur.addListSelectionListener(new secondListListener());
        this.confirmerChangementPersonnel.addActionListener(new ModifierInfosPersonnel());
        this.effacerPersonel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                effacerFournisseur();
                ImportPersonnelsFromDataBase();
                EspaceFournisseur.ImportDataListeSecondaire();
            }
        });
    }
    public void effacerFournisseur(){
        JOptionPane option = new JOptionPane();
        int reponse = JOptionPane.NO_OPTION;
        try{
            reponse = option.showConfirmDialog(null, "<html>Voulez vous vraiment supprimer le client<BR>"
                +this.listePersonnels.getSelectedValue().toString(), "Confirmation de supression", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        }
        catch(NullPointerException exp){
            JOptionPane op = new JOptionPane();
            op.showMessageDialog(null, "Veuillez verifier que vous avez bien selectionné un fournisseur"
                    + "", "Error...", JOptionPane.ERROR_MESSAGE);
        }
        if(reponse == JOptionPane.YES_OPTION){
            try{
                Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                state.executeUpdate("delete from FOURNISSEUR where NUM_FOURNISSEUR = " 
                        + getSelectePersonelNumber(this.listePersonnels));
                state.executeQuery("delete from CLASSEMENT_FOURNISSEUR where NUM_FOURNISSEUR = "
                        + getSelectePersonelNumber(this.listePersonnels));
                state.close();
            }
            catch(NullPointerException | ClassNotFoundException | SQLException exp){
            }
        }
    }
    private void ImportPersonnelsFromDataBase(){
        try{
            this.personelsListModel.clear();
            Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultatRecherche = state.executeQuery("select * from FOURNISSEUR");
            int i = 1 ;
            while(resultatRecherche.next()){
                resultatRecherche.absolute(i);
                personelsListModel.addElement(resultatRecherche.getInt("NUM_FOURNISSEUR")+"- "
                        +resultatRecherche.getString("NOM_FOURNISSEUR")+ " " + transformAsName(resultatRecherche.getString("PRENOM_FOURNISSEUR")));
                i++;
            }
            resultatRecherche.close();
            state.close();
            }
        catch(NullPointerException | ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
    }
    class addNewFournisseur implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(ficheIntroduction.formulaires[0].getInformation().equals("") || ficheIntroduction.formulaires[1].getInformation().equals("")){
                JOptionPane option = new JOptionPane();
                option.showMessageDialog(null, "Vous avez oublié de mentioner un champ important", "Empty text error...",JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultat = state.executeQuery("SELECT * FROM FOURNISSEUR");
                    resultat.last();
                    int numeroNewFournisseur ;
                    try{
                        numeroNewFournisseur = resultat.getInt("NUM_FOURNISSEUR")+1;
                    }
                    catch(NullPointerException exp){
                        numeroNewFournisseur = 1;
                    }
                    state.executeUpdate("INSERT INTO FOURNISSEUR (DATE_FICHE,NOM_FOURNISSEUR, PRENOM_FOURNISSEUR, DDN_FOURNISSEUR, SITUATION_SOCIALE, "
                        + "ADRESSE_FOURNISSEUR, CODE_POSTAL_FOURNISSEUR, PAYS_FOURNISSEUR, EMAIL_FOURNISSEUR, TELEPHONE_PORTABLE_FOURNISSEUR,TELEPHONE_BUREAU_FOURNISSEUR,"
                        + " FAX_FOURNISSEUR, SOCIETE_FOURNISSEUR,NUM_FOURNISSEUR,SEXE_FOURNISSEUR,TYPE_COMMERCE_FOURNISSEUR,TYPE_MARCHANDISE,WILAYA_FOURNISSEUR)"
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
                        + numeroNewFournisseur + ","
                        + "'" + ficheIntroduction.getSexeInformation() + "',"
                        + "'" + (ficheIntroduction.typeActivite.getSelectedItem()).toString() + "',"
                        + "'" + (ficheIntroduction.niveauConfiance.getSelectedItem().toString()) + "',"
                        + "'" + (ficheIntroduction.formulaires[12].getInformation().toUpperCase())
                        + "')");
                    state.executeUpdate("insert into CLASSEMENT_FOURNISSEUR (NUM_SERIE,NUM_FOURNISSEUR,NOM_PRENOM_FOURNISSEUR,AFFAIRES_FOURNISSEUR,DATE_ENREGISTREMENT) VALUES("
                        + numeroNewFournisseur +","
                        + numeroNewFournisseur + ",'"
                        + ficheIntroduction.formulaires[0].getInformation().toUpperCase()+ " " + ficheIntroduction.formulaires[1].getInformation().toLowerCase() + "',"
                        + 0+",CURRENT_DATE)");
                    resultat.close();
                    state.close();
                    personelsListModel.clear();
                    ImportPersonnelsFromDataBase();
                    ImportDataListeSecondaire();
                }
                catch(NullPointerException | SQLException | ClassNotFoundException exp){
                    exp.printStackTrace();
                }
                for(int i = 0 ; i < 17 ; i++)if(i < 2 || i > 6 && (i != 7) && (i != 8))ficheIntroduction.formulaires[i].clearFormulaire();
                ficheIntroduction.setVisible(false);
            }
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
                ResultSet resultatRecherche = state.executeQuery("select * from FOURNISSEUR where NOM_FOURNISSEUR like '%" 
                        + recherchePersonnel.getText().toUpperCase() + "%' or PRENOM_FOURNISSEUR like '%" + recherchePersonnel.getText().toLowerCase() + "%'");
                int i = 1 ;
                while(resultatRecherche.next()){
                    resultatRecherche.absolute(i);
                    personelsListModel.addElement(resultatRecherche.getInt("NUM_FOURNISSEUR")
                            +"- "+resultatRecherche.getString("NOM_FOURNISSEUR")+ " " + transformAsName(resultatRecherche.getString("PRENOM_FOURNISSEUR")));
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
        public void valueChanged(ListSelectionEvent e) {
            if(!e.getValueIsAdjusting()){
                try{
                    int nombre = getSelectePersonelNumber(listePersonnels);
                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultatRecherche = state.executeQuery("select * from FOURNISSEUR where NUM_FOURNISSEUR = '"+Integer.toString(nombre)+"'");
                    resultatRecherche.absolute(1);
                    String column = null;
                    for(int i = 0 ; i < 15 ; i++){
                        switch(i){
                            case 0:
                                column = "DATE_FICHE";
                                break;
                            case 1:
                                column = "DDN_FOURNISSEUR";
                                break;
                            case 2:
                                column = "TYPE_COMMERCE_FOURNISSEUR";
                                break;
                            case 3:
                                column = "TYPE_MARCHANDISE";
                                break;
                            case 4:
                                column = "SEXE_FOURNISSEUR";
                                break;
                            case 5:
                                column = "SITUATION_SOCIALE";
                                break;
                            case 6:
                                column = "ADRESSE_FOURNISSEUR";
                                break;
                            case 7:
                                column = "CODE_POSTAL_FOURNISSEUR";
                                break;
                            case 8:
                                column = "PAYS_FOURNISSEUR";
                                break;
                            case 9:
                                column = "WILAYA_FOURNISSEUR";
                                break;
                            case 10:
                                column = "TELEPHONE_BUREAU_FOURNISSEUR";
                                break;
                            case 11:
                                column = "TELEPHONE_PORTABLE_FOURNISSEUR";
                                break;
                            case 12:
                                column = "EMAIL_FOURNISSEUR";
                                break;
                            case 13:
                                column = "FAX_FOURNISSEUR";
                                break;
                            case 14:
                                column = "SOCIETE_FOURNISSEUR";
                                break;
                        }
                        if(i == 0)labelDateEnregistrement.setText(translateDateEnregistrement(resultatRecherche.getString(column)));
                        else if(i == 1)labelDateNaissance.setText(resultatRecherche.getString(column));
                        else if(i == 2){
                            String[] tabTypeActiviteFournisseur = {"Grossiste","Importateur"};
                            majActivite.removeAllItems();
                            majActivite.addItem(resultatRecherche.getString(column));
                            addTab(majActivite,tabTypeActiviteFournisseur);
                        }
                        else if(i == 3){
                            String[] tabRechercheNiveauConfianceFournisseur = {"Cafe","Emballage"};
                            majConfiance.removeAllItems();
                            majConfiance.addItem(resultatRecherche.getString(column));
                            addTab(majConfiance,tabRechercheNiveauConfianceFournisseur);
                        }
                        else if(i == 4){
                            String[] tabSexe = {"Masculin","Feminin"};
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
                    EspaceBusiness.pTabbedGauche.getPersonalData().setText(nombre < 10 ? "0"+Integer.toString(nombre) : Integer.toString(nombre));
                    EspaceBusiness.pTabbedGauche.getPersonalName().setText(resultatRecherche.getString("NOM_FOURNISSEUR")+" "+resultatRecherche.getString("PRENOM_FOURNISSEUR"));
                    resultatRecherche.close();
                    state.close();
                }
                catch(NullPointerException | ClassNotFoundException | SQLException exp){
                    exp.printStackTrace();
                }
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
                    column = "DDN_FOURNISSEUR";
                    details = rechercheDate.getInformations();
                    break;
                case 2:
                    column = "TYPE_COMMERCE_FOURNISSEUR";
                    details = rechercheTypeCommerce.getSelectedItem().toString();
                    break;
                case 3:
                    column = "TYPE_MARCHANDISE";
                    details = rechercheNiveauConfiance.getSelectedItem().toString();
                    break;
                case 4:
                    column = "SEXE_FOURNISSEUR";
                    details = rechercheComboSexe.getSelectedItem().toString();
                    break;
                case 5:
                    column = "SITUATION_SOCIALE";
                    details = rechercheComboSituation.getSelectedItem().toString();
                    break;
                case 6:
                    column = "ADRESSE_FOURNISSEUR";
                    details = rechercheText.getText().toUpperCase();
                    break;
                case 7:
                    column = "CODE_POSTAL_FOURNISSEUR";
                    details = rechercheText.getText();
                    break;
                case 8:
                    column = "PAYS_FOURNISSEUR";
                    details = rechercheText.getText().toUpperCase();
                    break;
                case 9:
                    column = "WILAYA_FOURNISSEUR";
                    details = rechercheText.getText().toUpperCase();
                    break;
            }
            try{
                Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultat = null;
                if(rechercheOption.getSelectedIndex() < 6){
                    resultat = state.executeQuery("select NUM_FOURNISSEUR,NOM_FOURNISSEUR,PRENOM_FOURNISSEUR from FOURNISSEUR "
                        + "where "+column+" = '"+details+"'");
                    int i = 1;
                    while(resultat.next()){
                    resultat.absolute(i);
                    rechercheListModel.addElement(resultat.getInt("NUM_FOURNISSEUR")+"- "
                        +resultat.getString("NOM_FOURNISSEUR")+" "+transformAsName(resultat.getString("PRENOM_FOURNISSEUR")));
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
                        if(rechercheOption.getSelectedIndex() == 7)resultat = state.executeQuery("select NUM_FOURNISSEUR,NOM_FOURNISSEUR,PRENOM_FOURNISSEUR from FOURNISSEUR "
                            +"where "+column + " = '" + details + "'");
                        else resultat = state.executeQuery("select NUM_FOURNISSEUR,NOM_FOURNISSEUR,PRENOM_FOURNISSEUR from FOURNISSEUR "
                            +"where "+column + " like '%" + details + "%'");
                        int i = 1;
                        while(resultat.next()){
                        resultat.absolute(i);
                        rechercheListModel.addElement(resultat.getInt("NUM_FOURNISSEUR")+"- "
                            +resultat.getString("NOM_FOURNISSEUR")+" "+transformAsName(resultat.getString("PRENOM_FOURNISSEUR")));
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
    public static void ImportDataListeSecondaire(){
        listeProvisoire.clear();
        secondaireListModelFournisseur.clear();
        try{
            Statement state = IOTransactions.getConnectionUtility().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultat = state.executeQuery("select NUM_FOURNISSEUR,AFFAIRES_FOURNISSEUR from CLASSEMENT_FOURNISSEUR");
            int i = 1;
            // Recuperation des données
            while(resultat.next()){
                resultat.absolute(i);
                listeProvisoire.add(resultat.getInt("NUM_FOURNISSEUR"),resultat.getInt("AFFAIRES_FOURNISSEUR"));
                i++;
            }
            // Classement des personnes
            while(!listeProvisoire.isEmpty()){
                int fournisseur = listeProvisoire.getDataListe1(0);
                int nombreAffaire = listeProvisoire.getDataListe2(0);
                for(int a = 1 ; a < listeProvisoire.size() ; a++){
                    if(nombreAffaire < listeProvisoire.getDataListe2(a)){
                        fournisseur = listeProvisoire.getDataListe1(a);
                        nombreAffaire = listeProvisoire.getDataListe2(a);
                    }
                }
                resultat = state.executeQuery("select NUM_FOURNISSEUR,NOM_PRENOM_FOURNISSEUR"
                        + " from CLASSEMENT_FOURNISSEUR where NUM_FOURNISSEUR = " + fournisseur);
                resultat.first();
                secondaireListModelFournisseur.addElement(fournisseur + "- "+resultat.getString("NOM_PRENOM_FOURNISSEUR"));
                listeProvisoire.remove(fournisseur, nombreAffaire);
            }
            resultat.close();
            state.close();
            Object[] objs = new Object[secondaireListModelFournisseur.getSize()];
            for(int k = 0 ; k < objs.length ; k++)objs[k] = secondaireListModelFournisseur.getElementAt(k);
            listeSecondaireFournisseur.setListData(objs);
        }
        catch(NullPointerException | ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
    }
    class secondListListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e) {
            if(!e.getValueIsAdjusting()){
                try{
                    int nombre = getSelectePersonelNumber(listeSecondaireFournisseur);
                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultatRecherche = state.executeQuery("select NUM_FOURNISSEUR,DATE_ENREGISTREMENT,AFFAIRES_FOURNISSEUR from CLASSEMENT_FOURNISSEUR where NUM_FOURNISSEUR = '"+nombre+"'");
                    resultatRecherche.absolute(1);
                    String info = null;
                    for(int i = 15 ; i < 17 ; i++){
                        switch(i){
                            case 15:
                                info = getYearFromDate(resultatRecherche.getString("DATE_ENREGISTREMENT"));
                                break;
                            case 16:
                                info = Integer.toString(resultatRecherche.getInt("AFFAIRES_FOURNISSEUR"));
                                break;
                        }
                        champRecherche[i].setText(info);
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
    private String getYearFromDate(String date){
        String translatedDate = translateDateEnregistrement(date);
        String year = "";
        for(int i = 0 ; i < translatedDate.length() ;i++){
            if(i > 5)year = year + translatedDate.charAt(i);
        }
        return year;
    }
    class ModifierInfosPersonnel implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                int numero = getSelectePersonelNumber(listePersonnels);
                Statement state = IOTransactions.getConnectionUtility().createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultat = state.executeQuery("select * from FOURNISSEUR where NUM_FOURNISSEUR = '"+Integer.toString(numero)+ "'");
                resultat.first();
                String column = null;
                String value = null;
                for(int i = 1 ; i < 15 ; i++){
                    switch(i){
                        case 1:
                            column = "DDN_FOURNISSEUR";
                            value = majNaissance.getInformations();
                            break;
                        case 2:
                            column = "TYPE_COMMERCE_FOURNISSEUR";
                            value = majActivite.getSelectedItem().toString();
                            break;
                        case 3:
                            column = "TYPE_MARCHANDISE";
                            value = majConfiance.getSelectedItem().toString();
                            break;
                        case 4:
                            column = "SEXE_FOURNISSEUR";
                            value = majSexe.getSelectedItem().toString();
                            break;
                        case 5:
                            column = "SITUATION_SOCIALE";
                            value = majSituation.getSelectedItem().toString();
                            break;
                        case 6:
                            column = "ADRESSE_FOURNISSEUR";
                            value = champRecherche[i].getText().toUpperCase();
                            break;
                        case 7:
                            column = "CODE_POSTAL_FOURNISSEUR";
                            value = champRecherche[i].getText();
                            break;
                        case 8:
                            column = "PAYS_FOURNISSEUR";
                            value = champRecherche[i].getText().toUpperCase();
                            break;
                        case 9:
                            column = "WILAYA_FOURNISSEUR";
                            value = champRecherche[i].getText().toUpperCase();
                            break;
                        case 10:
                            column = "TELEPHONE_BUREAU_FOURNISSEUR";
                            value = champRecherche[i].getText();
                            break;
                        case 11:
                            column = "TELEPHONE_PORTABLE_FOURNISSEUR";
                            value = champRecherche[i].getText();
                            break;
                        case 12:
                            column = "EMAIL_FOURNISSEUR";
                            value = champRecherche[i].getText();
                            break;
                        case 13:
                            column = "FAX_FOURNISSEUR";
                            value = champRecherche[i].getText();
                            break;
                        case 14:
                            column = "SOCIETE_FOURNISSEUR";
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
}
