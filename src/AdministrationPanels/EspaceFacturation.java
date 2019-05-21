/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdministrationPanels;

/**
 *
 * @author lennovo
 */
import MainPackage.IOTransactions;
import MainPackage.MPanelPrinter;
import MainPackage.MainWindow;
import MainPackage.PanneauBonPour;
import MainPackage.PanneauImpression;
import MainPackage.PanneauImpressionPrincipale;
import MainPackage.PanneauImpressionSecondaire;
import ModeledWindows.IntroductionWindow;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
public class EspaceFacturation extends MainPanel{
    private Tableau tableau;
    private ChampText textRecherche;
    public PanneauInfo pInfos;
    private final int k1 = 17;
    public static final int k2 = 34;
    public Liste listeClients;
    public DefaultListModel lModel;
    public MenuContextuel mc ;
    public EspaceFacturation(){
        super();
        ImportPersonnelsFromDataBase();
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
    }
    void initListeners() {
        this.textRecherche.addKeyListener(new EspaceFacturation.PersonnelsSeaching());
        this.listeClients.addListSelectionListener(new EspaceFacturation.listeListener());
        this.pInfos.controleurs.bouttonRecherche.addActionListener(new EspaceFacturation.rechercheVentes());
        this.pInfos.controleurs.bouttonImprimer.addActionListener(new EspaceFacturation.Imprimer());
        this.listeClients.addMouseListener(new MouseAdapter(){
            public void mouseReleased(MouseEvent e){
                if(e.isPopupTrigger())mc.show(listeClients, e.getX(), e.getY());
            }
        });
    }
    void initComponent() {
        this.lModel = new DefaultListModel();
        this.listeClients = new Liste(this.lModel);
        this.textRecherche = new ChampText();
        this.tableau = new Tableau();
        this.pInfos = new PanneauInfo();
        this.mc = new MenuContextuel();
        Panneau pNorth = new EspaceFacturation.Panneau((int)(3*MainWindow.maxHeight/8-100),
                new BorderLayout());
        pNorth.setPreferredSize(new Dimension(MainWindow.maxWidth,(int)(3*MainWindow.maxHeight/8-90)));
        EspaceFacturation.Panneau pNorthEast = new EspaceFacturation.Panneau(0,"Liste des clients",new BorderLayout());
        JScrollPane pScrollTableau = new JScrollPane(
                this.tableau,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane pScrollListe = new JScrollPane(
                this.listeClients,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pScrollTableau.setViewportView(this.tableau);
        pScrollTableau.setBackground(Color.white);
        pScrollTableau.setPreferredSize(new Dimension(MainWindow.maxWidth,2*MainWindow.maxHeight/3-100));
        pScrollTableau.setBorder(BorderFactory.createTitledBorder(MainWindow.bordure,"Liste des ventes:",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.TOP));
        JScrollPane pScrollInfos = new JScrollPane(this.pInfos,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    pScrollInfos.setBorder(BorderFactory.createTitledBorder(MainWindow.bordure,"Recherche:",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.TOP));
        pNorthEast.add(new EspaceFacturation.Formulaire("Rechercher:",this.textRecherche,new Dimension(100,20)),
                BorderLayout.NORTH);
        pNorthEast.add(pScrollListe,BorderLayout.CENTER);       
        pNorth.add(pNorthEast,BorderLayout.CENTER);
        pNorth.add(pScrollInfos,BorderLayout.WEST);
        this.add(pScrollTableau,BorderLayout.SOUTH);
        this.add(pNorth,BorderLayout.NORTH);
    }
    class Tableau extends JTable{
        public TableModel tModel ;
        public Tableau(){
            super();
            this.initComponent();
            this.setRowSelectionAllowed(false);
        }
        private void initComponent(){
            this.tModel = new TableModel();
            this.setModel(tModel);
            this.setGridColor(new java.awt.Color(0, 0, 0));
            this.setRowHeight(23);
            this.setColumnSelectionAllowed(false);
            this.setGridColor(Color.black);
            this.setShowVerticalLines(true);
            this.setShowHorizontalLines(true);
            this.setFillsViewportHeight(true);
            this.setFillsViewportHeight(true);
            this.getColumnModel().getColumn(0).setPreferredWidth(20);
            this.getColumnModel().getColumn(1).setPreferredWidth(100);
            this.getColumnModel().getColumn(2).setPreferredWidth(200);
            this.getColumnModel().getColumn(3).setPreferredWidth(150);
            this.getColumnModel().getColumn(4).setPreferredWidth(50);
            this.getColumnModel().getColumn(5).setPreferredWidth(175);
            this.getColumnModel().getColumn(6).setPreferredWidth(175);
            this.getColumnModel().getColumn(7).setPreferredWidth(0);
            this.getColumnModel().getColumn(8).setPreferredWidth(50);
            this.getColumnModel().getColumn(9).setPreferredWidth(50);
            this.setSelectionBackground(new java.awt.Color(255, 255, 255));
            this.setSelectionForeground(new java.awt.Color(0, 0, 0));
            this.removeColumn(this.getColumnModel().getColumn(6));
            this.removeColumn(this.getColumnModel().getColumn(6));
            this.removeColumn(this.getColumnModel().getColumn(8));
        }
        public void clearTable(){
            while(this.getRowCount() != 0){
                this.tModel.removeRow(0);
            }
        }
    }
    class TableModel extends DefaultTableModel{
        public TableModel(){
            super(new String[]{"N°","Date d'enregistrement","Type produit","Quantite (Kg)","P.U (DA)","Prix Total HT (DA)"
                    ,"Avance (DA)","Reste (DA)","Facturée","Facturer","NUM_VENTE"}, 0);
        }
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class clazz = String.class;
            switch (columnIndex) {
                case 0:
                    clazz = Integer.class;
                    break;
                case 9:
                    clazz = Boolean.class;
                    break;
            }
            return clazz;
        }
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 9;
        }
        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (aValue instanceof Boolean && column == 9) {
                Vector rowData = (Vector)getDataVector().get(row);
                rowData.set(9, (boolean)aValue);
                fireTableCellUpdated(row, column);
            }
        }
    }
    class Panneau extends JPanel{
        public Panneau(int height,String str,LayoutManager lm){
            this.setLayout(lm);
            this.setPreferredSize(new Dimension(MainWindow.maxWidth,height));
            this.setBorder(BorderFactory.createTitledBorder(
                    MainWindow.bordure,str,TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.TOP));
            this.setBackground(Color.white);
        }
        public Panneau(int height,LayoutManager lm){
            this.setLayout(lm);
            this.setPreferredSize(new Dimension(MainWindow.maxWidth,height));
            this.setBackground(Color.white);
            this.setBorder(BorderFactory.createEmptyBorder());
        }
    }
    class PanneauInfo extends Panneau{
        public FormulaireDate dateVente;
        public Label numClient;
        public ChampBox facture;
        public ChampText IDFacture;
        public Controllers controleurs;
        public PanneauInfo(){
            super(0,"Recherche:",new FlowLayout());
            this.setBorder(BorderFactory.createEmptyBorder());
            this.setPreferredSize(new Dimension((int)MainWindow.maxWidth/3,(int)(MainWindow.maxHeight/4-50)));
            this.initComponent();
        }
        private void initComponent(){
            String[] tabOption = {"Ventes non facturé","Factures","Bon pour"};
            this.controleurs = new Controllers();
            this.dateVente = new FormulaireDate();
            this.facture = new ChampBox(tabOption);
            this.IDFacture = new ChampText();
            this.IDFacture.setText("?");
            this.IDFacture.setVisible(false);
            this.IDFacture.setPreferredSize(new Dimension(95,20));
            this.numClient = new Label("?",true);
            this.add(new Formulaire("Organiser par:",this.facture,this.IDFacture));
            this.add(new Formulaire("Numero du client:",this.numClient,new Dimension(150,30)));
            this.add(new Formulaire("Date de la vente:",this.dateVente,new Dimension(150,30)));
            this.add(controleurs);
            this.facture.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try{
                        if(facture.getSelectedIndex() == 1) {
                            tableau.clearTable();
                            pInfos.controleurs.bouttonImprimer.setEnabled(false);
                            IDFacture.setVisible(true);
                            numClient.setText("?");
                            dateVente.setEnabled(false);
                        }
                        else {
                            tableau.clearTable();
                            pInfos.controleurs.bouttonImprimer.setEnabled(false);
                            IDFacture.setText("?");
                            IDFacture.setVisible(false);
                            dateVente.setEnabled(true);
                            numClient.setText(Integer.toString(getSelectePersonelNumber(listeClients)));
                        }
                        revalidate();
                    }
                    catch(NullPointerException exp){
                        exp.printStackTrace();
                    }
                }
            });
        }        
    }
    public class FormulaireDate extends JDateChooser{
        public FormulaireDate(){
            this.setBackground(Color.white);
            this.setPreferredSize(new Dimension(150,30));
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
            this.jcalendar.setDate(new Date(0,0,0));
        }
        public String getInformations(){
            return (this.jcalendar.getCalendar().get(5) < 10? "0"+Integer.toString(this.jcalendar.getCalendar().get(5)) : 
                    Integer.toString(this.jcalendar.getCalendar().get(5)))
                    +(this.jcalendar.getCalendar().get(2)+1 < 10? "-0"+Integer.toString(this.jcalendar.getCalendar().get(2)+1) : "-"+Integer.toString(this.jcalendar.getCalendar().get(2)+1))
                    +"-"+Integer.toString(this.jcalendar.getCalendar().get(1));
        }
    }
    class Label extends JLabel{
        private String text;
        private boolean centering ;
        public Label(String str,boolean b){
            this.text = str;
            this.centering = b;
            this.setPreferredSize(new Dimension(150,20));
            if(centering)this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
        @Override
        public void paintComponent(Graphics g){
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(Color.black);
            FontMetrics fm = g.getFontMetrics();
            int largeur = fm.getHeight();
            if(centering){
                int longueur = fm.stringWidth(text);
                g.drawString(text, this.getWidth()/2-longueur/2, this.getHeight()/2+largeur/4);
            }
            else g.drawString(text, 0, this.getHeight()/2+largeur/4);
        }
        @Override
        public void setText(String str){
            this.text = str;
            this.repaint();
        }
        @Override
        public String getText(){
            return this.text;
        }
    }
    public class Formulaire extends JPanel{
        private JLabel label;
        private JTextField champText;
        public Formulaire(String str,Component comp){
            this.setBackground(Color.white);
            this.initComponent(str,comp);
        }
        public Formulaire(String str,Component comp1,Component comp2){
            this.setBackground(Color.white);
            this.initComponent(str, comp1, comp2);
        }
        public Formulaire(String str,Component comp,Dimension dim){
            this.setBackground(Color.white);
            this.initComponent(str,comp);
            this.label.setPreferredSize(dim);
        }
        private void initComponent(String str,Component comp){
            this.label = new EspaceFacturation.Label(str,false);
            this.add(this.label);
            this.add(comp);
        }
        private void initComponent(String str,Component comp1,Component comp2){
            this.label = new EspaceFacturation.Label(str,false);
            this.add(this.label);
            this.add(comp1);
            this.add(comp2);
        }
        public String getInformation(){
            return this.champText.getText();
        }
        public void clearFormulaire(){
            this.champText.setText("?");
        }
    }
    class Liste extends JList{
        public Liste(DefaultListModel collection){
            super(collection);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED,Color.cyan,Color.magenta));
        }
    }
    class ChampBox extends JComboBox{
        public ChampBox(String[] tabString){
            super(tabString);
            this.setPreferredSize(new Dimension(150,25));
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
    }
    class ChampText extends JTextField{
        public ChampText(){
            this.setPreferredSize(new Dimension(180,20));
            this.setHorizontalAlignment(JTextField.HORIZONTAL);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
    }
    public void ImportPersonnelsFromDataBase(){
        try{
            lModel.clear();
            try (Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE)) {
                ResultSet resultatRecherche = state.executeQuery("select NUM_CLIENT,NOM_CLIENT,PRENOM_CLIENT from CLIENT");
                int i = 1 ;
                while(resultatRecherche.next()){
                    resultatRecherche.absolute(i);
                    lModel.addElement(resultatRecherche.getInt("NUM_CLIENT")+"- "
                            +resultatRecherche.getString("NOM_CLIENT")+ " " + 
                            transformAsName(resultatRecherche.getString("PRENOM_CLIENT")));
                    i++;
                }
                resultatRecherche.close();
            }
        }
        catch(NullPointerException | ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
    }
    class PersonnelsSeaching implements KeyListener{
        @Override
        public void keyPressed(KeyEvent e) {
        }
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyReleased(KeyEvent e) {
            try{
                lModel.clear();
                try (Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE); ResultSet resultatRecherche 
                                = state.executeQuery("select * from CLIENT where NOM_CLIENT like '%"
                                + textRecherche.getText().toUpperCase() + "%' or PRENOM_CLIENT like '%" 
                                + textRecherche.getText().toLowerCase() + "%'")) {
                    int i = 1 ;
                    while(resultatRecherche.next()){
                        resultatRecherche.absolute(i);
                        lModel.addElement(resultatRecherche.getInt("NUM_CLIENT")
                                +"- "+resultatRecherche.getString("NOM_CLIENT")+ " " + 
                                transformAsName(resultatRecherche.getString("PRENOM_CLIENT")));
                        i++;
                    }
                }
            }
            catch(ClassNotFoundException | SQLException exp){
                exp.printStackTrace();
            }
        }
    }
    class Controllers extends JPanel{
        public JButton bouttonRecherche;
        public JButton bouttonImprimer;
        public Controllers(){
            this.setBackground(Color.white);
            this.setPreferredSize(new Dimension(410,35));
            this.initComponents();
        }
        private void initComponents(){
            this.bouttonImprimer = new JButton("Imprimer");
            this.bouttonRecherche = new JButton("Rechercher");
            this.bouttonRecherche.setPreferredSize(new Dimension(200,30));
            this.bouttonImprimer.setPreferredSize(new Dimension(200,30));
            this.bouttonImprimer.setEnabled(false);
            this.add(this.bouttonRecherche);
            this.add(this.bouttonImprimer);
        }
    }
    class MenuContextuel extends JPopupMenu{
        JMenuItem listeFacture ;
        public MenuContextuel(){
            this.listeFacture = new JMenuItem("Lister les factures");
            this.add(this.listeFacture);
            this.listeFacture.addActionListener(new listerFactures());
        }
    }
    class listeListener implements ListSelectionListener{
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(pInfos.facture.getSelectedIndex() != 1){
                tableau.clearTable();
                pInfos.controleurs.bouttonImprimer.setEnabled(false);
            }
            try{
                if(!e.getValueIsAdjusting())
                    if(pInfos.facture.getSelectedIndex() != 1)
                        pInfos.numClient.setText(Integer.toString(getSelectePersonelNumber(listeClients)));
            }
            catch(NullPointerException exp){
                exp.printStackTrace();
            }
        }
    }
    class rechercheVentes implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if((pInfos.dateVente.getInformations().equals("31-12-1899") || pInfos.numClient.getText().equals("?")) 
                    && pInfos.facture.getSelectedIndex() == 0){
                JOptionPane option = new JOptionPane();
                option.showMessageDialog(null, "Verifez que tout les champs sont biens remplis", "Error...",
                        JOptionPane.ERROR_MESSAGE);
            }
            else {
                tableau.clearTable();
                String query = "";
                try{
                    switch(pInfos.facture.getSelectedIndex()){
                        case 1:
                            query = "select * from GESTION_VENTES where NUMERO_FACTURE = " + Integer.parseInt(
                                pInfos.IDFacture.getText());
                            break;
                        case 0:
                            query = "select * from GESTION_VENTES where NUM_CLIENT = '" + Integer.parseInt(
                                pInfos.numClient.getText())
                                + "' and DATE_VENTE = '" + transcriptDateEnregistrement(
                                pInfos.dateVente.getInformations()) 
                                + "' and FACTUREE = 'N'";
                            break;
                        case 2:
                            query = "select * from GESTION_VENTES where NUM_CLIENT = '" + Integer.parseInt(
                                pInfos.numClient.getText())
                                + "' and DATE_VENTE = '" + transcriptDateEnregistrement(
                                pInfos.dateVente.getInformations()) 
                                + "' and FACTUREE = 'N'";
                            break;
                    }                
                    try (Statement state = IOTransactions.getConnectionUtility().createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                            ResultSet result = state.executeQuery(query)) {
                        int i = 1;
                        while(result.next()){
                            result.absolute(i);
                            Object[] objets = {
                                i,
                                pInfos.dateVente.getInformations(),
                                result.getString("TYPE_PRODUIT_VENDU"),
                                result.getDouble("QUANTITE_VENDUE"),
                                result.getDouble("PRIX_UNITAIRE_VENTE"),
                                result.getDouble("PRIX_VENTE"),
                                result.getDouble("SOMME_VERSEE"),
                                result.getDouble("SOMME_RESTANT"),
                                result.getString("FACTUREE"),
                                true,
                                result.getInt("NUM_VENTE")
                            };
                            tableau.tModel.addRow(objets);
                            i++;
                        }
                    }
                    if(tableau.getRowCount() != 0)pInfos.controleurs.bouttonImprimer.setEnabled(true);
                }
                catch(NullPointerException | NumberFormatException | ClassNotFoundException | SQLException exp){
                    JOptionPane option = new JOptionPane();
                        option.showMessageDialog(null, "Verifez que tout les champs sont biens remplis", "Error...",
                        JOptionPane.ERROR_MESSAGE);
                    exp.printStackTrace();
                }
            }
        }
    }
    class Imprimer implements ActionListener{//Evenement Bouton Imprimer
        public void actionPerformed(ActionEvent e){
            JOptionPane pOption = new JOptionPane();
            int reponse = pOption.showConfirmDialog(null,"Voulez vous vraiment imprimer ?","Confirmation d'impresssion",
                    JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(reponse == JOptionPane.YES_OPTION){
                double HTValue=0;
            double TTValue=0;
            double VerseValue=0;
            double ResteValue=0;
            PanneauImpression pi;
            if(pInfos.facture.getSelectedIndex() == 0 || pInfos.facture.getSelectedIndex() == 1)pi = new PanneauImpressionPrincipale();//DECLARATION DU PANNEAU PRINCIPAL
            else pi = new PanneauBonPour();
            Calendar cal = Calendar.getInstance();
            String date = (cal.get(Calendar.DAY_OF_MONTH) < 10? "0"+Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) 
                    : Integer.toString(cal.get(Calendar.DAY_OF_MONTH)))
                    +(cal.get(Calendar.MONTH) < 10? "-0"+Integer.toString(cal.get(Calendar.MONTH)) 
                    : "-"+Integer.toString(cal.get(Calendar.MONTH)))
                    +"-"+Integer.toString(cal.get(Calendar.YEAR));
            int numeroClient = -1;
            if(pInfos.facture.getSelectedIndex() == 0 || pInfos.facture.getSelectedIndex() == 2)numeroClient = Integer.parseInt(pInfos.numClient.getText());
            else {
                try{
                    Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    ResultSet r = state.executeQuery("select NUM_VENTE,NUM_CLIENT,NUMERO_FACTURE from GESTION_VENTES where NUMERO_FACTURE = "
                            +Integer.parseInt(pInfos.IDFacture.getText()));
                    r.first();
                    numeroClient = r.getInt("NUM_CLIENT");
                    r.close();
                    state.close();
                }
                catch(NullPointerException | ClassNotFoundException | SQLException exp){
                    exp.printStackTrace();
                }
            }
            String nomClient = "";
            String adresseClient = "";
            String rcClient = "";
            String fiscalClient = "";
            double taxTimbre = 0;
            int numeroFacture = MainWindow.NUM_FACTURE;
            try{
                        Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                        ResultSet result = state.executeQuery("select NUM_CLIENT,NOM_CLIENT,PRENOM_CLIENT,"
                                + "ADRESSE_CLIENT,RC,NUMERO_FISCAL_CLIENT,BALANCE_PAIEMENT "
                                + "from CLIENT where NUM_CLIENT = " + numeroClient);
                    result.first();
                    nomClient = result.getString("NOM_CLIENT") + "     " + EspacePersonel.transformAsName(result.getString("PRENOM_CLIENT"));
                    adresseClient = transformAdress(result.getString("ADRESSE_CLIENT"));
                    rcClient = result.getString("RC");
                    fiscalClient = result.getString("NUMERO_FISCAL_CLIENT");
                    if(pInfos.facture.getSelectedIndex() == 0){
                        result.updateDouble("BALANCE_PAIEMENT", result.getDouble("BALANCE_PAIEMENT")+MainWindow.TAUX_TIMBRE);
                        taxTimbre = MainWindow.TAUX_TIMBRE;
                        result.updateRow();
                    }
                    else if(pInfos.facture.getSelectedIndex() == 1){
                        result = state.executeQuery("select NUM_FACTURE,VALEUR_TIMBRE from FACTURES where NUM_FACTURE = "+
                                Integer.parseInt(pInfos.IDFacture.getText()));
                        result.first();
                        taxTimbre = result.getDouble("VALEUR_TIMBRE");
                    }
                    result.close();
                    state.close();
            }
            catch(NullPointerException | ClassNotFoundException | SQLException exp){
            }            
            if(pInfos.facture.getSelectedIndex() == 0)pi.setFactureInformations(Integer.toString(numeroFacture),date,Integer.toString(numeroClient),nomClient,adresseClient,rcClient,
                    fiscalClient);
            else if(pInfos.facture.getSelectedIndex() == 1)pi.setFactureInformations(pInfos.IDFacture.getText(),date,Integer.toString(numeroClient),nomClient,adresseClient,rcClient,
                    fiscalClient);
            else pi.setFactureInformations("", date,Integer.toString(numeroClient), nomClient, adresseClient, rcClient, fiscalClient);
            int n = 1;
            ArrayList<Integer> venteAFacturer = new ArrayList<>();
            PanneauImpressionSecondaire[] pis = new PanneauImpressionSecondaire[getNombreTableauSecondaire()];
            for(int m = 0 ; m < pis.length ; m++)pis[m] = new PanneauImpressionSecondaire();
            for(int i = 0 ; i < tableau.getRowCount() ; i++){
                if((boolean)tableau.tModel.getValueAt(i, 9) == true){
                    if(n <= k1){pi.setNewRow(n,
                        (String)tableau.tModel.getValueAt(i,2),//type de produit
                            (double)tableau.tModel.getValueAt(i,3),//Quantité vendue
                                (double)tableau.tModel.getValueAt(i,4),//Prix Unitaire
                                    (double)tableau.tModel.getValueAt(i,5),//Prix de vente
                                        (double)tableau.tModel.getValueAt(i,6),//Somme versee
                                            (double)tableau.tModel.getValueAt(i,7));//Somme restant
                    HTValue=HTValue+(double)tableau.tModel.getValueAt(i, 5);//VALEUR TOTALE Hors TAXE de la FACTURE
                    VerseValue=VerseValue+(double)tableau.tModel.getValueAt(i,6);
                    //ResteValue=(double)tableau.getValueAt(i,7);
                    }
                    else{
                        int tpec = 0;
                        for(int indice = 0 ; indice < pis.length ; indice++){
                            if(pis[indice].isFull())tpec++;
                        }
                        pis[tpec].setNewRow(n,
                        (String)tableau.tModel.getValueAt(i,2),//type de produit
                            (double)tableau.tModel.getValueAt(i,3),//Quantité vendue
                                (double)tableau.tModel.getValueAt(i,4),//Prix Unitaire
                                    (double)tableau.tModel.getValueAt(i,5),//Prix de vente
                                        (double)tableau.tModel.getValueAt(i,6),//Somme versee
                                            (double)tableau.tModel.getValueAt(i,7));//Somme restant
                    HTValue=HTValue+(double)tableau.tModel.getValueAt(i, 5);//VALEUR TOTALE Hors TAXE de la FACTURE
                    VerseValue=VerseValue+(double)tableau.tModel.getValueAt(i,6);
                    //ResteValue=(double)tableau.getValueAt(i,7);
                    }
                    venteAFacturer.add((int)tableau.tModel.getValueAt(i, 10));
                    n++;
                }
                TTValue=HTValue+(HTValue*MainWindow.TAUX_TVA/100)+taxTimbre;//La valeur TTC=ValeurHT+Timbre
                if(pInfos.facture.getSelectedIndex() != 2)ResteValue=TTValue-VerseValue;//Calcule du reste a partir de la valeur TTC - Somme versée
                else ResteValue = HTValue - VerseValue;
                pi.setFactureStatistique(Double.toString(HTValue),Double.toString(TTValue),VerseValue,ResteValue);//AFFICHAGE Valeur Hors TAXE et VALEUR TT
            }
            //DEBUT Mise a jour de la Table GESTION_VENTES
            if(pInfos.facture.getSelectedIndex() == 0){
                for(int k : venteAFacturer){
                    try{
                        try (Statement state = IOTransactions.getConnectionUtility().createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE); 
                                ResultSet result = state.executeQuery("select * from GESTION_VENTES where NUM_VENTE = "+k)) {
                            result.first();
                            result.updateString("FACTUREE", "O");
                            result.updateInt("NUMERO_FACTURE",MainWindow.NUM_FACTURE);
                            result.updateRow();
                        }
                    }
                    catch(NullPointerException | ClassNotFoundException | SQLException exp){
                        exp.printStackTrace();
                    }
                }
            }
            JFrame[] fs = new JFrame[pis.length+1];
            for(int i = 0 ; i < fs.length ; i++){
                JPanel p;
                if(i == 0) p = pi;
                else p = pis[i-1];
                fs[i] =  new JFrame("Fenetre d'impression n° = "+(i+1));
                if(i != 0)fs[i].setSize(new Dimension(582,807));
                else fs[i].setSize(new Dimension(582,815));
                fs[i].setUndecorated(true);
                fs[i].setBackground(new Color(255,255,255,0));
                fs[i].setContentPane(p);
                fs[i].setLocationRelativeTo(null);
                fs[i].setVisible(true);
                MPanelPrinter mpp = new MPanelPrinter(p);
                mpp.setLRMargins(50);
                mpp.setLRMargins(50);
                mpp.setLRMargins(30);
                mpp.setTBMargins(50);
                mpp.setTBMargins(50);
                mpp.setTBMargins(30);
                mpp.print();
                fs[i].setVisible(false);
            }
            //FIN DE L'IMPRESSION DU PANNEAU
            tableau.clearTable();
            pInfos.controleurs.bouttonImprimer.setEnabled(false);
            if(pInfos.facture.getSelectedIndex() == 0){
                try{
                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    state.executeUpdate("insert into FACTURES ("
                                    + "NUM_FACTURE,"
                            + "DATE_FACTURE,"
                            + "NUM_CLIENT,"
                            + "VALEUR_TIMBRE) values ("
                            + MainWindow.NUM_FACTURE + ","
                            + "CURRENT_DATE,"
                            + numeroClient + ","
                            + MainWindow.TAUX_TIMBRE
                            + ")");
                    MainWindow.NUM_FACTURE++;
                    ResultSet result = state.executeQuery("select NUM_CONST,VALEUR_CONST from CONSTANTES where NUM_CONST = "+6);
                    result.first();
                    result.updateDouble("VALEUR_CONST",MainWindow.NUM_FACTURE);
                    result.updateRow();
                    result.close();
                    state.close();
                }
                catch(NullPointerException | ClassNotFoundException | SQLException exp){
                    exp.printStackTrace();
                }
            }
            }
        }
    }
    class listerFactures implements ActionListener{
        class TableModel extends DefaultTableModel{
            public TableModel(){
                super(new String[]{"N°","Date d'enregistrement"}, 0);
            }
            public boolean isCellEditable(int row,int column){
                return false;
            }
        }
        class Table extends JTable{
            TableModel tModel ;
            public Table(){
                super();
                this.initComponent();
                this.setRowSelectionAllowed(false);
            }
            private void initComponent(){
                this.tModel = new TableModel();
                this.setModel(tModel);
                this.setGridColor(new java.awt.Color(0, 0, 0));
                this.setRowHeight(23);
                this.setColumnSelectionAllowed(false);
                this.setGridColor(Color.black);
                this.setShowVerticalLines(true);
                this.setShowHorizontalLines(true);
                this.setFillsViewportHeight(true);
                this.setFillsViewportHeight(true);
                this.getColumnModel().getColumn(0).setPreferredWidth(20);
                this.setSelectionBackground(new java.awt.Color(255, 255, 255));
                this.setSelectionForeground(new java.awt.Color(0, 0, 0));
            }
        }
        public void actionPerformed(ActionEvent evt){
            try{
               Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
                       ResultSet.CONCUR_UPDATABLE);
               ResultSet result = state.executeQuery("select * from FACTURES where NUM_CLIENT = "+
                       getSelectePersonelNumber(listeClients));
               int i = 1;
               Table table = new Table();
               while(result.next()){
                   result.absolute(i);
                   Object[] row = {result.getInt("NUM_FACTURE"),translateDateEnregistrement(result.getDate("DATE_FACTURE").toString())};
                   table.tModel.addRow(row);
                   i++;
               }
               JFrame f = new JFrame();
               f.setSize(300,400);
               f.setAlwaysOnTop(true);
               JScrollPane p = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
               f.setTitle("Recherche de factures");
               f.setLocationRelativeTo(null);
               f.getContentPane().add(p,BorderLayout.CENTER);
               f.setResizable(false);
               f.setVisible(true);
            }
            catch(NullPointerException | ClassNotFoundException | SQLException exp){
                JOptionPane option = new JOptionPane();
                    option.showMessageDialog(null, "Verifez que vous avez bien selectioné un client ! ","Error...",
                            JOptionPane.ERROR_MESSAGE);
                exp.printStackTrace();
            }
        }
    }
    public static String transformAsName(String name){
        name = name.toUpperCase();
        String transformedName = name.charAt(0) + "";
        try{
            for(int i = 1 ; i < name.length() ; i++){
                if(name.charAt(i) == ' '){
                    transformedName = transformedName + " ";
                    name = name.toUpperCase();
                    transformedName = transformedName + name.charAt(i+1);
                    i = i+1;
                }
                else {
                    name = name.toLowerCase();
                    transformedName = transformedName + name.charAt(i);
                }
            }
        }
        catch(NullPointerException exp){
            exp.printStackTrace();
        }
        return transformedName;
    }
    public int getSelectePersonelNumber(JList liste) throws NullPointerException{
        String value = liste.getSelectedValue().toString();
        String str = "";
        boolean stop = false;
        int i = 0;
        while(!stop){
            if(value.charAt(i) != '-'){
                str = str + value.charAt(i);
                i++;
            }
            else stop = true;
        }
        return Integer.parseInt(str);
    }
    public String transcriptDateEnregistrement(String str){
        String date = "";
        for(int i = 0 ; i < str.length() ; i++){
            if(str.charAt(i) == '-')date = date + '.';
            else date = date + str.charAt(i);
        }
        return date;
    }
    private int getNombreTableauSecondaire(){
        int n = 0;
        int nT = 0;
        for(int i = k1 ; i < tableau.getRowCount()-1 ; i++)
            if((boolean)tableau.tModel.getValueAt(i, 9) == true){
                n++;
                if(n%k2 == 0){
                    nT++;
                    n = 0;
                }
            }
        if(n != 0)nT++;
        return nT;
    }
    public String translateDateEnregistrement(String str){
        String date = "";
        String strPartiel = "";
        for(int i = 1 ; i <= str.length() ; i++){
            if(((str.charAt(str.length()-i)) == '-')){
                date = date + tournerStr(strPartiel) + "-";
                strPartiel = "";
            }
            else if(i == str.length())date = date + "2" + tournerStr(strPartiel) ;
            else strPartiel = strPartiel + str.charAt(str.length()-i);
        }
        return date;
    }
    private String tournerStr(String str){
        String str2 = "";
        for(int i = 1 ; i <= str.length(); i++)str2 = str2 + str.charAt(str.length()-i);
        return str2;
    }
    private String transformAdress(String adresse){
        String nAdresse = "";
        for(int i = 0 ; i < adresse.length() ; i++){
            if(adresse.charAt(i) == ' ')nAdresse = nAdresse + "  ";
            else nAdresse = nAdresse + adresse.charAt(i);
        }
        return nAdresse;
    }
}
