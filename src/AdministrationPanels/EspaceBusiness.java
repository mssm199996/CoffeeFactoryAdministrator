/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AdministrationPanels;

import MainPackage.AchatsClient;
import MainPackage.IOTransactions;
import MainPackage.MainWindow;
import ModeledWindows.IntroductionWindow;
import ModeledWindows.MoneyTransactions;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import com.toedter.calendar.JDateChooser;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Sidi Mohamed
 */
public class EspaceBusiness extends MainPanel{
    public static ArrayList<AchatsClient> Facture = new ArrayList<>();
    protected  JDateChooser JDateChooser1;
    protected  JList listeAchats;
    protected  JList listeDepenses;
    protected  JList listeVentes;
    protected  DefaultListModel listeAchatsCollection ;
    protected  DefaultListModel listeDepensesCollection ;
    protected  DefaultListModel listeVentesCollection ;
    public static TabbedPanneau pTabbedGauche;
    private TabbedPanneau pTabbedCentre;
    public static TabbedPanneau pTabbedDroite;
    private LabelTop gestionnaireAchats;
    private LabelTop gestionnaireDepenses;
    private LabelTop gestionnaireVentes;
    public EspaceBusiness(){
        super();
        pTabbedDroite.setSelectedIndex(1);
        pTabbedCentre.setSelectedIndex(1);
        pTabbedGauche.setSelectedIndex(1);
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
    }
    void initComponent(){
//------------------------------ Initialisation ------------------------------//
        this.listeAchatsCollection = new DefaultListModel();
        this.listeDepensesCollection = new DefaultListModel();
        this.listeVentesCollection = new DefaultListModel();
        this.listeAchats = new Liste(this.listeAchatsCollection);
        this.listeAchats.setSelectionForeground(Color.black);
        this.listeDepenses = new Liste(this.listeDepensesCollection);
        this.listeDepenses.setSelectionForeground(Color.black);
        this.listeVentes = new Liste(this.listeVentesCollection);
        this.listeVentes.setSelectionForeground(Color.black);
        JPanel contentPane = new JPanel();
        JPanel pGauche = new Panneau();
        JPanel pCentre = new Panneau();
        JPanel pDroite = new Panneau();
        pTabbedGauche = new TabbedPanneau("Achats");
        pTabbedCentre = new TabbedPanneau("Depenses");
        pTabbedDroite = new TabbedPanneau("Ventes");
        gestionnaireAchats = new LabelTop(new ImageIcon(getClass().getResource("/Images/panier.png")));
        gestionnaireDepenses = new LabelTop(new ImageIcon(getClass().getResource("/Images/tax.png")));
        gestionnaireVentes = new LabelTop(new ImageIcon(getClass().getResource("/Images/ventes.jpg")));
//------------------------------ Caractérisation -----------------------------//
        contentPane.setBackground(Color.white);
        contentPane.setLayout(new BorderLayout());
        pGauche.setPreferredSize(new Dimension(MainWindow.maxWidth/3,MainWindow.maxHeight-120));
        pCentre.setPreferredSize(new Dimension(MainWindow.maxWidth/3,MainWindow.maxHeight-120));
        pDroite.setPreferredSize(new Dimension(MainWindow.maxWidth/3,MainWindow.maxHeight-120));
//-------------------------------- Adding ------------------------------------//
        pGauche.add(gestionnaireAchats,BorderLayout.NORTH);
        pGauche.add(pTabbedGauche,BorderLayout.CENTER);
        pCentre.add(gestionnaireDepenses,BorderLayout.NORTH);
        pCentre.add(pTabbedCentre,BorderLayout.CENTER);
        pDroite.add(gestionnaireVentes,BorderLayout.NORTH);
        pDroite.add(pTabbedDroite,BorderLayout.CENTER);
        contentPane.add(pGauche,BorderLayout.WEST);
        contentPane.add(pCentre,BorderLayout.CENTER);
        contentPane.add(pDroite,BorderLayout.EAST);
        this.add(contentPane,BorderLayout.CENTER);
    }
    @Override
    void initListeners() {
        this.listeAchats.addListSelectionListener(new AchatTransactionInformations());
        this.pTabbedGauche.getChoixRecherche().addItemListener(new recherchepGaucheListener());
        this.pTabbedGauche.confirmerAjout.addActionListener(new insertNewAchat());
        this.pTabbedGauche.confirmerRecherche.addActionListener(new rechercherAchat());
        //-------------------------------->
        this.listeVentes.addListSelectionListener(new VenteTransactionInformations());
        this.pTabbedDroite.getChoixRecherche().addItemListener(new recherchepDroiteListener());
        this.pTabbedDroite.confirmerAjout.addActionListener(new insertNewVente());
        this.pTabbedDroite.confirmerRecherche.addActionListener(new rechercherVente());
        //-------------------------------->
        ((ChampBox)this.pTabbedCentre.getTypeProduit()).addItemListener(new typeDepenseChanged());
        this.pTabbedCentre.getChoixRecherche().addItemListener(new recherchepCentreListener());
        this.pTabbedCentre.confirmerAjout.addActionListener(new insertNewDepense());
        this.pTabbedCentre.confirmerRecherche.addActionListener(new rechercherDepense());
        //----------------------------------- Containers for animations -----------------------------------------
        //------------------------------- pTabbedComponentListener----------------------------------------------->
        // pTabbedGauche ===>
        this.pTabbedGauche.ficheAffaire.bouttonConfirmer.addActionListener(new AchatTransactionsUpdate());
        this.pTabbedGauche.ficheAffaire.bouttonAnnuler.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                pTabbedGauche.ficheAffaire.setVisible(false);
                pTabbedGauche.ficheAffaire.nouvelleEntre.setText("");
                pTabbedGauche.ficheAffaire.nouveauReste.setText("");
                listeAchats.clearSelection();
            }
        });
        // pTabbedDroite ===> 
        this.pTabbedDroite.ficheAffaire.bouttonConfirmer.addActionListener(new VenteTransactionsUpdate());
        this.pTabbedDroite.ficheAffaire.bouttonAnnuler.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                pTabbedDroite.ficheAffaire.setVisible(false);
                pTabbedDroite.ficheAffaire.nouvelleEntre.setText("");
                pTabbedDroite.ficheAffaire.nouveauReste.setText("");
                listeVentes.clearSelection();
            }
        });
    }
    class Liste extends JList{
        public Liste(DefaultListModel collection){
            super(collection);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED,Color.cyan,Color.magenta));
        }
    }
    class ScrollPanneau extends JScrollPane {
        public ScrollPanneau(JComponent component){
            super(component);
            this.setBackground(Color.white);
        }
    }
    class ChampText extends JTextField{
        public ChampText(){
            this.setPreferredSize(new Dimension(150,20));
            this.setHorizontalAlignment(JTextField.HORIZONTAL);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
    }
    class Formulaire extends Panneau{
        private Label label ;
        public Formulaire(String str,Component comp){
            super(BorderFactory.createEmptyBorder());
            this.initComponent(str,comp);
        }
        public Formulaire(String str,Component comp,Component comp2){
            super(BorderFactory.createEmptyBorder());
            this.initComponent(str, comp,comp2);
        }
        public Formulaire(String str,Component comp,Component comp2,Dimension dim){
            super(BorderFactory.createEmptyBorder());
            this.setPreferredSize(dim);
            this.initComponent(str, comp,comp2);
        }
        public Formulaire(String str,Component comp,Dimension dim){
            super(BorderFactory.createEmptyBorder());
            this.initComponent(str, comp);
            this.label.setPreferredSize(dim);
        }
        public Formulaire(Component comp,Dimension dim){
            super(BorderFactory.createEmptyBorder());
            this.initComponent(comp);
            this.setPreferredSize(dim);
        }
        public void initComponent(Component comp){
            this.setLayout(new FlowLayout());
            this.add(comp);
        }
        private void initComponent(String str,Component comp){
            this.label = new Label(str);
            this.setLayout(new FlowLayout());
            this.add(this.label);
            this.add(comp);
        }
        private void initComponent(String str,Component comp,Component comp2){
            this.initComponent(str, comp);
            this.add(comp2);
        }
    }
    class Boutton extends JButton{
        public Boutton(String str){
            super(str);
            this.setBackground(Color.white);
            this.setForeground(Color.black);
            this.setPreferredSize(new Dimension(300,50));
        }
    }
    class Spacer extends JPanel{
        public Spacer(Color c,int width,int height){
            this.setBackground(c);
            this.setPreferredSize(new Dimension(width,height));
        }
    }
    class LabelTop extends JLabel{
        public LabelTop(ImageIcon icn){
            super(icn);
            this.setPreferredSize(new Dimension(100,100));
        }
    }
    class Panneau extends JPanel{
        public Panneau(){
            this.setBorder(MainWindow.bordure);
            this.setLayout(new BorderLayout());
            this.setBackground(Color.white);
        }
        public Panneau(Border b){
            this.setBorder(b);
            this.setBackground(Color.white);
        }
        public Panneau(LayoutManager l,Border b){
            this.setBackground(Color.white);
            this.setBorder(b);
            this.setLayout(l);
        }
    }
    public class TabbedPanneau extends JTabbedPane{
        private MoneyTransactions ficheAffaire;
        protected JButton confirmerAjout;
        protected JButton confirmerRecherche;
        private String function ;
        private JLabel InfosPlusRecherche;
        protected Label NamePersonnel;
        protected Label DataPersonnel;
        protected ChampBox DataTypeProduit;
        protected JComboBox<String> choixRecherche;
        protected ChampBox typeRecherche;
        protected JTextField DataQuantiteProduit;
        protected JTextField DataPrixUnitaireProduit;
        public Label DataTVA;
        protected JTextField DataAvanceSalaire;
        protected Label DataResteSalaire;
        protected JTextField numeroRecherche;
        public FormulaireNaissance DateNaissanceRecherche;
        public TabbedPanneau(String str){
            super(JTabbedPane.TOP);
            this.function = str;
            this.initUI();
            this.initComponent();
            this.initTootipsMessages();
        }
        public String getDataPersonnelID(){
            return this.DataPersonnel.getText();
        }
        public String getLabelName(){
            return this.DataPersonnel.getText();
        }
        private void initTootipsMessages(){
            if(this.getFunction().equals("Achats") || this.getFunction().equals("Ventes")){
                if(this.getFunction().equals("Achats")) {
                    this.DataQuantiteProduit.setToolTipText("<html>Pour Sachet 60Kg Café vert/250g Alluminium, entrez le nombre d'unités <BR>Pour les autres, entrez le nombre de kilogrammes");
                    this.DataPrixUnitaireProduit.setToolTipText("<html>Pour Sachet 60Kg Café vert/250g Alluminium, entrez le prix d'une unités<BR>Pour les autres , entrez le prix du kilogramme");
                }
                else {
                    this.DataQuantiteProduit.setToolTipText("Expression en Kg");
                    this.DataPrixUnitaireProduit.setToolTipText("Expression en DA");
                }
                this.DataAvanceSalaire.setToolTipText("Expression en DA");
            }
            else {
                this.DataQuantiteProduit.setToolTipText("Entrez le nom de la depense");
                this.DataPrixUnitaireProduit.setToolTipText("Expression en DA");
            }
        }
        public String getFunction(){
            return this.function;
        }
        public Label getPersonalData(){
            return this.DataPersonnel;
        }
         public Label getPersonalName(){
            return this.NamePersonnel;
        }
        public JLabel getCaptionRecherche(){
            return this.InfosPlusRecherche;
        }
        public Component getTypeProduit(){
            return this.DataTypeProduit;
        }
        public JTextField getQuantityTextData(){
            return this.DataQuantiteProduit;
        }
        public JTextField getPriceData(){
            return this.DataPrixUnitaireProduit;
        }
        public JTextField getAdvanceData(){
            return this.DataAvanceSalaire;
        }
         public Label getResteData(){
            return this.DataResteSalaire;
        }
        public JTextField getIDRecherche(){
            return this.numeroRecherche;
        }
        public JComponent getTypeRecherche(){
            return this.typeRecherche;
        }
        public JComboBox getChoixRecherche(){
            return this.choixRecherche;
        }
        public JPanel getDateRecherchePane(){
            return this.DateNaissanceRecherche;
        }
        private void initUI(){
            UIManager.put("TabbedPane.selected", Color.white);
            this.setUI(new BasicTabbedPaneUI(){
                @Override
                protected void installDefaults() {
                    super.installDefaults();
                    highlight = Color.blue;
                    lightHighlight = Color.blue;
                    shadow = Color.red;
                    darkShadow = Color.green;
                    focus = Color.yellow;
                }
            });
        }
        private void initSearching(JPanel pRecherche){
            this.DateNaissanceRecherche = new FormulaireNaissance();
            this.numeroRecherche = new ChampText();
            this.confirmerRecherche = new Boutton("Confirmer la recherche");
            this.InfosPlusRecherche = new Label("Infos plus:",new Dimension(60,20));
            if(this.getFunction().equals("Achats") || this.getFunction().equals("Ventes")){
                String[] tabRecherche = {"Date d'introduction",(this.getFunction().equals("Achats"))? "Numéro du fournisseur" : "Numéro du client","Type de produit"};
                this.choixRecherche = new ChampBox<>(tabRecherche,new Dimension(170,30));
                
                JPanel contentPane = new Panneau(BorderFactory.createEmptyBorder());
                contentPane.add(new Formulaire("Rechercher par:",this.choixRecherche));
                contentPane.add(this.DateNaissanceRecherche);
                
                contentPane.add(this.InfosPlusRecherche);
                contentPane.add(this.numeroRecherche);
                contentPane.add(this.typeRecherche);
                
                pRecherche.add(new Spacer(Color.white,50,50),BorderLayout.NORTH);
                pRecherche.add(new Spacer(Color.white,50,50),BorderLayout.SOUTH);
                pRecherche.add(new Spacer(Color.white,50,50),BorderLayout.WEST);
                pRecherche.add(new Spacer(Color.white,50,50),BorderLayout.EAST);
                pRecherche.add(contentPane,BorderLayout.CENTER);
            }
            else {
                String[] tabRecherche = {"Date d'introduction","Type de depenses"};
                this.choixRecherche = new ChampBox<>(tabRecherche,new Dimension(170,30));
                JPanel contentPane = new Panneau(BorderFactory.createEmptyBorder());
                contentPane.add(new Formulaire("Rechercher par:",this.choixRecherche));
                contentPane.add(this.DateNaissanceRecherche);
                contentPane.add(this.InfosPlusRecherche);
                contentPane.add(this.typeRecherche);
                pRecherche.add(new Spacer(Color.white,50,50),BorderLayout.NORTH);
                pRecherche.add(new Spacer(Color.white,50,50),BorderLayout.SOUTH);
                pRecherche.add(new Spacer(Color.white,50,50),BorderLayout.WEST);
                pRecherche.add(new Spacer(Color.white,50,50),BorderLayout.EAST);
                pRecherche.add(contentPane,BorderLayout.CENTER);
            }
            this.InfosPlusRecherche.setVisible(false);
            this.numeroRecherche.setVisible(false);
            this.typeRecherche.setVisible(false);
        }
        private void initRegistering(JPanel pEnregistrement){
            if(this.getFunction().equals("Achats") || this.getFunction().equals("Ventes")){
                String[] tabQuantite = {"Sachet 60 Kg","Autre"};
                this.confirmerAjout = new Boutton((this.getFunction().equals("Achats")) ? "Confirmer l'achat" : "Confirmer la vente");
                this.DataPersonnel = new Label("?",new Dimension(200,20));
                this.NamePersonnel=new Label("?",new Dimension(200,20));//ajouter le contenu du label
                this.DataQuantiteProduit = new ChampText();
                this.DataPrixUnitaireProduit = new ChampText();
                this.DataAvanceSalaire = new ChampText();
                if(this.function.equals("Achats")){
                    this.DataAvanceSalaire.addKeyListener(new resteAchatListener());
                    this.DataQuantiteProduit.addKeyListener(new resteAchatListener());
                    this.DataPrixUnitaireProduit.addKeyListener(new resteAchatListener());
                }
                else {
                    this.DataAvanceSalaire.addKeyListener(new resteVenteListener());
                    this.DataQuantiteProduit.addKeyListener(new resteVenteListener());
                    this.DataPrixUnitaireProduit.addKeyListener(new resteVenteListener());
                    this.DataTVA = new Label("?");
                    this.DataTVA.setForeground(Color.red);
                }
                this.DataResteSalaire = new Label("?");
                this.initComboTypeProduit();
                //Ajout nom du client et du fournisseur+N° imposition
                pEnregistrement.add(new Formulaire((this.getFunction().equals("Achats")) ? "Nom du fournisseur:" : "Nom du client:",this.NamePersonnel,new Dimension(140,10)));
                pEnregistrement.add(new Formulaire((this.getFunction().equals("Achats")) ? "Numéro du fournisseur:" : "Numéro du client:",this.DataPersonnel,new Dimension(140,10)));
                pEnregistrement.add(new Formulaire("Type du produit",this.DataTypeProduit));
                if(this.getFunction().equals("Achats"))pEnregistrement.add(new Formulaire("Quantité du produit",this.DataQuantiteProduit));
                else pEnregistrement.add(new Formulaire("Quantité du produit",this.DataQuantiteProduit));
                pEnregistrement.add(new Formulaire("Prix unitaire",this.DataPrixUnitaireProduit));
                pEnregistrement.add(new Formulaire("Versé",this.DataAvanceSalaire));
                if(this.function.equals("Ventes"))pEnregistrement.add(new Formulaire("Reste HT",this.DataResteSalaire));
                else pEnregistrement.add(new Formulaire("Reste",this.DataResteSalaire));
                if(this.function.equals("Ventes"))pEnregistrement.add(new Formulaire("Taux TVA",this.DataTVA));
            }
            else {
                this.initComboTypeProduit();
                this.DataQuantiteProduit = new ChampText();
                this.DataPrixUnitaireProduit = new ChampText();
                this.confirmerAjout = new Boutton("Confirmer les depenses");
                this.DataQuantiteProduit.setVisible(false);
                pEnregistrement.add(new Formulaire("Type de la depense:",this.DataTypeProduit,this.DataQuantiteProduit,new Dimension(400,60)));
                pEnregistrement.add(new Formulaire("coût de la depense:",this.DataPrixUnitaireProduit));
            }
        }
        private void initComboTypeProduit(){
            this.DataTypeProduit = new ChampBox<String>();
            this.typeRecherche = new ChampBox<String>();
            this.typeRecherche.setPreferredSize(new Dimension(230,30));
            this.DataTypeProduit.setPreferredSize(new Dimension(230,30));
             //this.DataQuantiteBox.setVisible(false);
            switch (this.getFunction()) {
                case "Depenses":
                                      //**GO--->>CONNECTION DEPENSES
 String prodDepense="SELECT NOM_PRODUIT FROM GESTION_PRODUITS WHERE CATEGORIE='Depenses'";
     ArrayList tabloLigne = new ArrayList();;//Declarer le tableau des Enregistrements au debut de la methode pour qu'il soit visible dans tous les endroits de cette methode
      int nbreEnregistrement=0;//on créée la variable nbreEnregistrement et on l'initialise à 0
 try {IOTransactions.getConnectionUtility();//creation d'une seule instance de connection non instanciable pour firebird
             try (Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE); ResultSet result = state.executeQuery(prodDepense)) {
                while(result.next())nbreEnregistrement++;//On obtient le nombre d'enregistrements dans cette requete
              
                   //On recueille le contenu des colonnes
                  for(int i = 1; i <=nbreEnregistrement; i++)  {result.absolute(i);  tabloLigne.add(result.getString(1));}     
     for (Object tabloLigne1 : tabloLigne) {
         ((ChampBox)this.DataTypeProduit).addItem(tabloLigne1 + "");
          ((ChampBox)this.typeRecherche).addItem(tabloLigne1 + "");
       }
           this.DataTypeProduit.addItem("Autre");
           result.close();
           state.close();}
           
        } catch (NullPointerException | ClassNotFoundException | SQLException ex) {}
           
        //*******Fin de la Procedure de Connection 
                    
                    break;
                    
                case "Achats":
                                      //**GO--->>CONNECTION ACHATS
                    (this.DataTypeProduit).addItem("Cafe vert 60Kg");
                        (this.DataTypeProduit).addItem("Cafe vert autre");
                        (this.DataTypeProduit).addItem("Cafe torrefie (grains)");
                        (this.DataTypeProduit).addItem("Cafe torrefie (moulu) [en vrac]");
                        (this.DataTypeProduit).addItem("Cafe torrefie (moulu) [conditionne]");
                        (this.DataTypeProduit).addItem("1Kg Kraft");
                        (this.DataTypeProduit).addItem("250g Papier Alluminium");
                        
                        (this.typeRecherche).addItem("Cafe vert 60Kg");
                        (this.typeRecherche).addItem("Cafe vert autre");
                        (this.typeRecherche).addItem("Cafe torrefie (grains)");
                        (this.typeRecherche).addItem("Cafe torrefie (moulu) [en vrac]");
                        (this.typeRecherche).addItem("Cafe torrefie (moulu) [conditionne]"); 
                         (this.typeRecherche).addItem("1Kg Kraft");
                         (this.typeRecherche).addItem("250g Papier Alluminium");           
        //*******Fin de la Procedure de Connection 
                    
                    break;
                    case "Ventes":
                        (this.DataTypeProduit).addItem("Cafe torrefie (grains)");
                        (this.DataTypeProduit).addItem("Cafe torrefie (moulu) [en vrac]");
                        (this.DataTypeProduit).addItem("Cafe torrefie (moulu) [conditionne]");
                        (this.typeRecherche).addItem("Cafe torrefie (grains)");
                        (this.typeRecherche).addItem("Cafe torrefie (moulu) [en vrac]");
                        (this.typeRecherche).addItem("Cafe torrefie (moulu) [conditionne]");
                break;
            }
        }
        private void initComponent(){
//----------------------------- Inisialisation -------------------------------//
            JPanel pListe = new Panneau();
            JPanel pEnregistrement = new Panneau();
            JPanel pRecherche = new Panneau();
            JPanel pEnregistrementCenter = new Panneau(new BorderLayout(),BorderFactory.createEmptyBorder());
                   pEnregistrementCenter.setPreferredSize(new Dimension(MainWindow.maxWidth/3-100,MainWindow.maxHeight/2-100));
            JPanel pEnregistrementCenter2 = new Panneau(BorderFactory.createEmptyBorder());
                   pEnregistrementCenter2.setPreferredSize(new Dimension(MainWindow.maxWidth/3-100,MainWindow.maxHeight/2-100));
            JPanel pEnregistrementSouth = new Panneau(BorderFactory.createEmptyBorder());
            JPanel pRechercheCenter = new Panneau(new BorderLayout(),BorderFactory.createEmptyBorder());
            JPanel pRechercheCenter2 = new Panneau(BorderFactory.createEmptyBorder());
            JPanel pRechercheSouth = new Panneau(BorderFactory.createEmptyBorder());
            JScrollPane pScrollListe ;
            
            pRechercheCenter2.setLayout(new BorderLayout());
            this.initRegistering(pEnregistrementCenter2);
            this.initSearching(pRechercheCenter2);
            
            if(this.function.equals("Achats")){
                this.ficheAffaire = new MoneyTransactions("fournisseur");
                pScrollListe = new ScrollPanneau(listeAchats);
            } 
            else if(this.function.equals("Depenses"))pScrollListe = new ScrollPanneau(listeDepenses);
            else {
                this.ficheAffaire = new MoneyTransactions("client");
                pScrollListe = new ScrollPanneau(listeVentes);
            }
//---------------------------- Caractérisation -------------------------------//
//----------------------------------- Adding ---------------------------------//
            pListe.add(pScrollListe,BorderLayout.CENTER);
            pRechercheCenter.add(new Spacer(Color.white,500,50),BorderLayout.NORTH);
            pRechercheCenter.add(pRechercheCenter2,BorderLayout.CENTER);
            pRechercheSouth.add(this.confirmerRecherche);
            pRecherche.add(pRechercheCenter,BorderLayout.CENTER);
            pRecherche.add(pRechercheSouth,BorderLayout.SOUTH);
            pEnregistrementCenter.add(pEnregistrementCenter2,BorderLayout.CENTER);
            JScrollPane pScrollCenter = null;
            if(!this.function.equals("Depenses")){
                pScrollCenter = new JScrollPane(pEnregistrementCenter,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                pScrollCenter.setBorder(BorderFactory.createEmptyBorder());
            }
            pEnregistrementSouth.add(this.confirmerAjout);
            if(!this.function.equals("Depenses"))pEnregistrement.add(pScrollCenter,BorderLayout.CENTER);
            else pEnregistrement.add(pEnregistrementCenter,BorderLayout.CENTER);
            if(this.function.equals("Depenses"))pEnregistrement.add(new Spacer(Color.white,500,100),BorderLayout.NORTH);
            else if(this.function.equals("Achats"))pEnregistrement.add(new Spacer(Color.white,500,50),BorderLayout.NORTH);
            else pEnregistrement.add(new Spacer(Color.white,500,25),BorderLayout.NORTH);
            pEnregistrement.add(pEnregistrementSouth,BorderLayout.SOUTH);
            this.addTab("Liste des "+function,new ImageIcon(getClass().getResource(("/Images/Liste.png"))), pListe);
            this.addTab("Enregistrement "+function,new ImageIcon(getClass().getResource(("/Images/Enregistrement.png"))), pEnregistrement);
            this.addTab("Recherche "+function,new ImageIcon(getClass().getResource(("/Images/Rechercher.png"))), pRecherche);
            for(int a = 0 ; a < this.getTabCount() ; a++){
                this.setBackgroundAt(a, Color.white);
                //Ajouter un Actionlistener au bouton confirmerAjout et aussi a confirmerRecherche
            confirmerRecherche.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt1) {
               confirmerRechercheNewBusinessActionPerformed(evt1);
            }
        });
          //Fin de ActionListener  
           
           }
        }
    
        private void confirmerRechercheNewBusinessActionPerformed(java.awt.event.ActionEvent evt1) { 
            switch (this.getFunction()) {
            }
        }

        
   
    }
    class FormulaireNaissance extends JPanel{
        protected JDateChooser JCalendrier;
        public FormulaireNaissance(){
            this.setBackground(Color.white);
            this.setPreferredSize(new Dimension(250,60));
            this.initComponent();
        }
        private void initComponent(){
            this.JCalendrier = new JDateChooser();
            this.JCalendrier.setBackground(Color.white);
            this.JCalendrier.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
            this.JCalendrier.setPreferredSize(new Dimension(180,30));
            this.JCalendrier.setDate(new java.util.Date());//la date en cours
            this.add(new Label("Infos plus:",new Dimension(65,20)));
            this.add(this.JCalendrier);
       }
          protected String getCalendarDate() {  
         String jour;String mois;
        int year=this.JCalendrier.getCalendar().get(1);
        int month=this.JCalendrier.getCalendar().get(2)+1;
        int date=this.JCalendrier.getCalendar().get(5);
        if(month<10){mois="0"+month;}else{mois=""+month;}
         if(date<10){jour="0"+date;}else{jour=""+date;}
        return jour+"."+mois+"."+year;    
    }
        public JButton getCalendarButton(){
            return this.JCalendrier.getCalendarButton();
        }
    }
    class ChampBox<String> extends JComboBox{
        public ChampBox(){
            this.setPreferredSize(new Dimension(150,30));
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
                    Color.blue, Color.red));
        }
        public ChampBox(String[] tabString){
            super(tabString);
            this.setPreferredSize(new Dimension(150,30));
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
        public ChampBox(String[] tabString , Dimension dim){
            super(tabString);
            this.setPreferredSize(dim);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
    }
    class Label extends JLabel{
        private String text;
        private Color foreColor = Color.black;
        public Label(String str){
            this.text = str;
            this.setPreferredSize(new Dimension(120,20));
        }
        @Override
        public void paintComponent(Graphics g){
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            FontMetrics fm = g.getFontMetrics();
            int longueur = fm.stringWidth(text);
            int largeur = fm.getHeight();
            g.setColor(foreColor);
            g.drawString(text, this.getWidth()/2-longueur/2, this.getHeight()/2+largeur/4);
        }
        public Label(String str,Dimension dim){
            this.text = str;
            this.setPreferredSize(dim);
        }
        public void setText(String str){
            this.text = str;
            this.repaint();
        }
        public String getText(){
            return this.text;
        }
        public void setForeground(Color c){
            this.foreColor = c;
        }
    }
    
    class typeDepenseChanged implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent e){
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(((ChampBox)pTabbedCentre.getTypeProduit()).getSelectedItem().toString().equals("Autre")) pTabbedCentre.getQuantityTextData().setVisible(true);
                else pTabbedCentre.getQuantityTextData().setVisible(false);
                revalidate();
            }
        }
    }
    class recherchepGaucheListener implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(pTabbedGauche.getChoixRecherche().getSelectedIndex() == 0){
                    pTabbedGauche.getDateRecherchePane().setVisible(true);
                    pTabbedGauche.getIDRecherche().setVisible(false);
                    pTabbedGauche.getTypeRecherche().setVisible(false);
                    pTabbedGauche.getCaptionRecherche().setVisible(false);
                }
                else if(pTabbedGauche.getChoixRecherche().getSelectedIndex() == 1){
                    pTabbedGauche.getDateRecherchePane().setVisible(false);
                    pTabbedGauche.getIDRecherche().setVisible(true);
                    pTabbedGauche.getTypeRecherche().setVisible(false);
                    pTabbedGauche.getCaptionRecherche().setVisible(true);
                }
                else {
                    pTabbedGauche.getDateRecherchePane().setVisible(false);
                    pTabbedGauche.getIDRecherche().setVisible(false);
                    pTabbedGauche.getTypeRecherche().setVisible(true);
                    pTabbedGauche.getCaptionRecherche().setVisible(true);
                }
                revalidate();
            }
        }
    }
    class recherchepDroiteListener implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(pTabbedDroite.getChoixRecherche().getSelectedIndex() == 0){
                    pTabbedDroite.getDateRecherchePane().setVisible(true);
                    pTabbedDroite.getIDRecherche().setVisible(false);
                    pTabbedDroite.getTypeRecherche().setVisible(false);
                    pTabbedDroite.getCaptionRecherche().setVisible(false);
                }
                else if(pTabbedDroite.getChoixRecherche().getSelectedIndex() == 1){
                    pTabbedDroite.getDateRecherchePane().setVisible(false);
                    pTabbedDroite.getIDRecherche().setVisible(true);
                    pTabbedDroite.getTypeRecherche().setVisible(false);
                    pTabbedDroite.getCaptionRecherche().setVisible(true);
                }
                else {
                    pTabbedDroite.getDateRecherchePane().setVisible(false);
                    pTabbedDroite.getIDRecherche().setVisible(false);
                    pTabbedDroite.getTypeRecherche().setVisible(true);
                    pTabbedDroite.getCaptionRecherche().setVisible(true);
                }
                revalidate();
            }
        }
    }
    class recherchepCentreListener implements ItemListener{
        @Override
        public void itemStateChanged(ItemEvent e){
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(pTabbedCentre.getChoixRecherche().getSelectedIndex() == 0){
                    pTabbedCentre.getDateRecherchePane().setVisible(true);
                    pTabbedCentre.getTypeRecherche().setVisible(false);
                    pTabbedCentre.getCaptionRecherche().setVisible(false);
                }
                else {
                    pTabbedCentre.getDateRecherchePane().setVisible(false);
                    pTabbedCentre.getTypeRecherche().setVisible(true);
                    pTabbedCentre.getCaptionRecherche().setVisible(true);
                }
                revalidate();
            }
        }
    }
      //***************************SECTION DE NOUVEAUX ENREGISTREMENTS***********************
    class insertNewAchat implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            //*********NOUVEL ACHAT*******************
            try{
            if(Double.parseDouble(pTabbedGauche.DataResteSalaire.getText()) >= 0.0){
                JOptionPane option = new JOptionPane();
                int reponse = option.showConfirmDialog(null, "Voulez vous vraiment confirmer l'achat ?", 
                        "Confirmation d'achat", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(reponse == JOptionPane.YES_OPTION){
                    try {
            try (Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE)) {
                ResultSet result;
                result = state.executeQuery("SELECT * FROM GESTION_ACHATS");//on selectionne la table des achats
                result.last();//on se met sur le dernier enregistrement de la table achats
                int numero = 1;//On recherche le dernier numero de malade inscrit et on l'incremente de 1
                try{
                    numero = result.getInt("NUM_ACHAT")+1;
                }
                catch(NullPointerException exp){
                    numero = 1;
                }
                state.executeUpdate("INSERT INTO GESTION_ACHATS ( DATE_ACHAT,TYPE_PRODUIT_ACHETE, QUANTITE_ACHETEE, "
                        + "PRIX_ACHAT, NFOURNISSEUR,NUM_ACHAT,NOMPRENOM_FOURNISSEUR,SOMME_VERSEE, "
                        + "SOMME_RESTANT,PRIX_UNITAIRE_ACHAT) \n" +
                "VALUES (CURRENT_DATE,'"
                +((ChampBox)pTabbedGauche.DataTypeProduit).getSelectedItem().toString()//
                +"','"+pTabbedGauche.DataQuantiteProduit.getText()
                +"','"+ Double.toString(Double.parseDouble(pTabbedGauche.DataPrixUnitaireProduit.getText())*
                        Double.parseDouble(pTabbedGauche.DataQuantiteProduit.getText()))//
                +"','"+pTabbedGauche.DataPersonnel.getText()//
                + "',"+numero
                +",'" +pTabbedGauche.NamePersonnel.getText()//               
                +"','"+pTabbedGauche.DataAvanceSalaire.getText()//
                +"','"+pTabbedGauche.DataResteSalaire.getText()
                +"','"+pTabbedGauche.DataPrixUnitaireProduit.getText() +"')");
                result = state.executeQuery("select AFFAIRES_FOURNISSEUR,NUM_SERIE from CLASSEMENT_FOURNISSEUR where NUM_FOURNISSEUR = "
                        +Integer.parseInt(pTabbedGauche.DataPersonnel.getText()));
                result.first();
                int nombrePointsMAJ = result.getInt("AFFAIRES_FOURNISSEUR")+1;
                result.updateInt("AFFAIRES_FOURNISSEUR", nombrePointsMAJ);
                result.updateRow();
                
                result = state.executeQuery("SELECT * FROM GESTION_PRODUITS");//on selectionne la table des achats
                result.last();//on se met sur le dernier enregistrement de la table achats
                numero = result.getInt("NUM_PRODUIT")+1;
                double quantity = 0;
                switch(pTabbedGauche.DataTypeProduit.getSelectedIndex()){
                    case 0:
                        quantity = Double.parseDouble(pTabbedGauche.DataQuantiteProduit.getText())*60;
                        break;
                    case 6:
                        quantity = Double.parseDouble(pTabbedGauche.DataQuantiteProduit.getText())*0.25;
                        break;
                    default: quantity = Double.parseDouble(pTabbedGauche.DataQuantiteProduit.getText());
                }
                state.executeUpdate("insert into GESTION_PRODUITS (NUM_PRODUIT,CATEGORIE,NOM_PRODUIT,QTE_REELLE_DISPONIBLE,"
                        + "DATE_MOUVEMENT_STOCK,QUANTITE_DEPLACEE,COMPTABILISE,HORAIRE,MOUVEMENT_EFFECTUE) "
                        + "values ("+numero+",'Achats','"+((ChampBox)pTabbedGauche.DataTypeProduit).getSelectedItem().toString()
                        +"',"+0+",CURRENT_DATE,"+quantity+",'N',CURRENT_TIME,"+1+")");
                result.close();
                state.close();
                ((ChampBox)pTabbedGauche.DataTypeProduit).setSelectedIndex(0);
                pTabbedGauche.DataQuantiteProduit.setText("");
                pTabbedGauche.DataPrixUnitaireProduit.setText("");
                pTabbedGauche.DataAvanceSalaire.setText("");
                pTabbedGauche.DataResteSalaire.setText("");
                EspaceFournisseur.ImportDataListeSecondaire();
            }
            MainWindow.et.Refresh();
        } 
        catch (NumberFormatException | NullPointerException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane option2 = new JOptionPane();
            option2.showMessageDialog(null,"Erreur d'introduction , veuillez verifier si tout les champs sont bien remplis","Error...",JOptionPane.ERROR_MESSAGE);
        }
                }
            }
            else {
                JOptionPane option = new JOptionPane();
                option.showMessageDialog(null, "La somme entrée est supérieur à la somme permise !", "Error...", JOptionPane.ERROR_MESSAGE);
            }
            }
            catch(NumberFormatException exp){
                JOptionPane option = new JOptionPane();
                option.showMessageDialog(null,"Erreur d'introduction , veuillez verifier si tout les champs sont bien remplis","Error...",JOptionPane.ERROR_MESSAGE);
                        }
    }
    }
    class insertNewDepense implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //*********NOUVELLE DEPENSE*******************
            JOptionPane option = new JOptionPane();
            int reponse = option.showConfirmDialog(null, "Voulez vous vraiment confirmer la dépense ?", 
                    "Confirmation de la depense", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(reponse == JOptionPane.YES_OPTION){
                try {IOTransactions.getConnectionUtility();
            try (Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE)) {
                ResultSet result;
                result=state.executeQuery("SELECT * FROM GESTION_FRAIS");//on selectionne la table des achats
                result.last();//on se met sur le dernier enregistrement de la table achats
                int numero = 1;//On recherche le dernier numero de malade inscrit et on l'incremente de 1
                try{
                    numero = result.getInt("NUM_FRAIS")+1;
                }
                catch(NullPointerException exp){
                    numero = 1;
                }//On recherche le dernier numero de malade inscrit et on l'incremente de 1
                String typeDepense = "";
                switch((pTabbedCentre.DataTypeProduit).getSelectedItem().toString()){
                    case "Autre":
                        typeDepense = pTabbedCentre.DataQuantiteProduit.getText();
                        break;
                    default:
                        typeDepense = ((pTabbedCentre.DataTypeProduit).getSelectedItem()).toString();
                }
                state.executeUpdate("INSERT INTO GESTION_FRAIS ( DATE_FRAIS,TYPE_FRAIS, VALEUR_FRAIS, NUM_FRAIS) \n" +
                "VALUES (CURRENT_DATE,'"
                +(typeDepense
                +"','"+pTabbedCentre.DataPrixUnitaireProduit.getText()//Valeur des frais
                + "'," +numero+")"));//numero des frais
                result.close();
                state.close();
                ((ChampBox)pTabbedCentre.DataTypeProduit).setSelectedIndex(0);
                pTabbedCentre.DataPrixUnitaireProduit.setText("");
            }
        } 
        catch (NullPointerException | ClassNotFoundException | SQLException ex) {ex.printStackTrace();}
        }
            }
    }
    class insertNewVente implements ActionListener {
        public void actionPerformed(ActionEvent e){
            Statement state = null;
            ResultSet resultat = null;
            JOptionPane option = new JOptionPane();
            int reponse = option.showConfirmDialog(null,"Voulez vous vraiment confirmer la vente ?",
                    "Confirmation de vente",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(reponse == JOptionPane.YES_OPTION){
                try{
                state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                // Introduction une vente
                resultat = state.executeQuery("select NUM_PRODUIT,QTE_REELLE_DISPONIBLE,NOM_PRODUIT,"
                        + "COMPTABILISE from GESTION_PRODUITS where NOM_PRODUIT = '"
                        +pTabbedDroite.DataTypeProduit.getSelectedItem().toString()+"' and COMPTABILISE = 'R'");
                resultat.first();
                if(resultat.getDouble("QTE_REELLE_DISPONIBLE") >= Double.parseDouble(pTabbedDroite.DataQuantiteProduit.getText())){
                    try{
                        resultat = state.executeQuery("select NUM_VENTE from GESTION_VENTES");
                        resultat.last();
                        int numeroVente ;
                        try{
                            numeroVente = resultat.getInt("NUM_VENTE")+1;
                        }
                        catch(NullPointerException exp){
                            numeroVente = 1;
                        }
                        int numeroClient = Integer.parseInt(pTabbedDroite.DataPersonnel.getText());
                        double quantite = Double.parseDouble(pTabbedDroite.DataQuantiteProduit.getText());
                        String typeMarchandise = pTabbedDroite.DataTypeProduit.getSelectedItem().toString();
                        double sommeVerse = Double.parseDouble(pTabbedDroite.DataAvanceSalaire.getText());
                        double prixUnitaire = Double.parseDouble(pTabbedDroite.DataPrixUnitaireProduit.getText());
                        double prixTotal = quantite*prixUnitaire;
                        double prixTotalTTC = (prixTotal+(prixTotal*MainWindow.TAUX_TVA/100));
                        double sommeRestanteHT = prixTotal - sommeVerse;
                        double sommeRestante = prixTotalTTC - sommeVerse;
                        resultat = state.executeQuery("select * from CLIENT where NUM_CLIENT = "+numeroClient);
                        resultat.first();
                        String nomClient = resultat.getString("NOM_CLIENT")+" "+resultat.getString("PRENOM_CLIENT");
                        String occupation = resultat.getString("TYPE_COMMERCE");
                        resultat.updateDouble("BALANCE_PAIEMENT",resultat.getDouble("BALANCE_PAIEMENT")+sommeRestante);
                        if(sommeRestanteHT > 0)resultat.updateDouble("RESTE_A_PAYER_HT",resultat.getDouble("RESTE_A_PAYER_HT")+sommeRestanteHT);
                        else resultat.updateDouble("RESTE_A_PAYER_HT",0);
                        if(resultat.getDouble("BALANCE_PAIEMENT") >= MainWindow.MOYENNE_BASSE)resultat.updateInt("COULEUR", -1);
                        else if(resultat.getDouble("BALANCE_PAIEMENT") < MainWindow.MOYENNE_BASSE 
                                && resultat.getDouble("BALANCE_PAIEMENT") >= MainWindow.MOYENNE_ELEVEE)resultat.updateInt("COULEUR", 0);
                        else if(resultat.getDouble("BALANCE_PAIEMENT") < MainWindow.MOYENNE_ELEVEE)resultat.updateInt("COULEUR", 1);
                        resultat.updateRow();
                        MainWindow.ec.verifierClient(numeroClient,nomClient,resultat.getInt("COULEUR"));
                        MainWindow.deb.setInformations(numeroClient,nomClient,resultat.getDouble("BALANCE_PAIEMENT"),
                                resultat.getDouble("RESTE_A_PAYER_HT"));
                        state.executeUpdate("insert into GESTION_VENTES "
                            + "("
                            + "NUM_CLIENT,"
                            + "TYPE_PRODUIT_VENDU,"
                            + "NUM_VENTE,"
                            + "SOMME_VERSEE,"
                            + "SOMME_RESTANT,"
                            + "DATE_VENTE,"
                            + "PRIX_UNITAIRE_VENTE,"
                            + "NOM_PRENOM_CLIENT,"
                            + "QUANTITE_VENDUE,"
                            + "PRIX_VENTE,"
                            + "FACTUREE,"
                            + "PRIX_VENTE_TTC"
                            + ") values ("
                            + numeroClient + ","
                            + "'" + typeMarchandise + "',"
                            + numeroVente + ","
                            + sommeVerse + ","
                            + sommeRestante + ","
                            + "CURRENT_DATE,"
                            + prixUnitaire + ","
                            + "'" + nomClient + "',"
                            + quantite + ","
                            + prixTotal 
                            + ",'N',"
                            + prixTotalTTC
                            + ")");
                        // Introduction dans stock
                        resultat = state.executeQuery("select NUM_PRODUIT from GESTION_PRODUITS");
                        resultat.last();
                        int numeroProduit ;
                        try{
                            numeroProduit = resultat.getInt("NUM_PRODUIT")+1;
                        }
                        catch(NullPointerException exp){
                            exp.printStackTrace();
                            numeroProduit = 1;
                        }
                        state.executeUpdate("insert into GESTION_PRODUITS ("
                            + "NUM_PRODUIT,"
                            + "CATEGORIE,"
                            + "NOM_PRODUIT,"
                            + "QTE_REELLE_DISPONIBLE,"
                            + "DATE_MOUVEMENT_STOCK,"
                            + "QUANTITE_DEPLACEE,"
                            + "COMPTABILISE,"
                            + "HORAIRE,"
                            + "MOUVEMENT_EFFECTUE"
                            + ") values ("
                            + numeroProduit + ","
                            + "'Ventes',"
                            + "'" + typeMarchandise +"',"
                            + 0 + ","
                            + "CURRENT_DATE,"
                            + quantite + ","
                            + "'N',"
                            + "CURRENT_TIME,"
                            + (-1)
                            + ")");
                        if(!occupation.equals("Mouturier")){
                            if(typeMarchandise.equals("Cafe torrefie (moulu) [en vrac]")){
                                resultat = state.executeQuery("select * from "
                                    + "FIDELITE_CLIENT where NUM_CLIENT = " + numeroClient);
                                resultat.first();
                                double nouveauAchat = resultat.getDouble("QUANTITE_VRAC_RESTANT") + quantite;
                                int points = resultat.getInt("POINTS_CLIENT")+(int)nouveauAchat/MainWindow.PROMO_VRAC;
                                resultat.updateDouble("QUANTITE_VRAC_RESTANT",(nouveauAchat%MainWindow.PROMO_VRAC));
                                resultat.updateDouble("POINTS_CLIENT",points);
                                resultat.updateRow();
                            }
                            else if(typeMarchandise.equals("Cafe torrefie (moulu) [conditionne]")){
                                resultat = state.executeQuery("select * from "
                                    + "FIDELITE_CLIENT where NUM_CLIENT = " + numeroClient);
                                resultat.first();
                                double nouveauAchat = resultat.getDouble("QUANTITE_CONDITIONNE_RESTANT")+quantite;
                                int points = resultat.getInt("POINTS_CLIENT")+(int)nouveauAchat/MainWindow.PROMO_CONDI;
                                resultat.updateDouble("QUANTITE_CONDITIONNE_RESTANT",(nouveauAchat%MainWindow.PROMO_CONDI));
                                resultat.updateDouble("POINTS_CLIENT",points);
                                resultat.updateRow();
                            }
                        }
                        else System.out.println("Ce client est un mouturier , donc pas de promotion !");
                        pTabbedDroite.DataAvanceSalaire.setText("");
                        pTabbedDroite.DataPrixUnitaireProduit.setText("");
                        pTabbedDroite.DataQuantiteProduit.setText("");
                        pTabbedDroite.DataResteSalaire.setText("?");
                        pTabbedDroite.DataTVA.setText("?");
                        pTabbedDroite.DataTypeProduit.setSelectedIndex(0);
                    }
                    catch(NumberFormatException exp){
                        exp.printStackTrace();
                        JOptionPane option2 = new JOptionPane();
                        option2.showMessageDialog(null,"Verifiez les champs de saisie !","Error...",JOptionPane.ERROR_MESSAGE);
                    }
                    catch(NullPointerException  exp){exp.printStackTrace();}
                }
                else {
                    JOptionPane option2 = new JOptionPane();
                    option2.showMessageDialog(null,
                            "Vous n'avez pas suffisement de marchandise en stock pour faire une telle vente !",
                            "Error...",JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(NullPointerException |NumberFormatException | ClassNotFoundException | SQLException exp){
                JOptionPane option2 = new JOptionPane();
                option2.showMessageDialog(null,"Verifiez les champs de saisie !","Error...",JOptionPane.ERROR_MESSAGE);
                exp.printStackTrace();            
            }
            finally{
                try{
                    resultat.close();
                    state.close();
                }
                catch(SQLException exp){
                    exp.printStackTrace();
                }
            }
            MainWindow.et.Refresh();
        }
            }
    }
    
    class rechercherAchat implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            //Here My code
            String requeteAchat="Achats";//Requete generée en fonction du type de recheche
            switch (pTabbedGauche.choixRecherche.getSelectedItem().toString()) {
                case "Date d'introduction":
                    String DateAchat = pTabbedGauche.DateNaissanceRecherche.getCalendarDate();
                    requeteAchat="SELECT * FROM GESTION_ACHATS WHERE DATE_ACHAT='"+DateAchat+"'";
                    break;
                case "Numéro du fournisseur":
                    requeteAchat="SELECT * FROM GESTION_ACHATS WHERE NFOURNISSEUR='"+pTabbedGauche.numeroRecherche.getText()+"'";
                    break;
                case "Type de produit":
                    String TypeProAchat=((ChampBox)pTabbedGauche.typeRecherche).getSelectedItem().toString();
                    requeteAchat = "SELECT * FROM GESTION_ACHATS WHERE TYPE_PRODUIT_ACHETE='"+TypeProAchat+"'";
                    break;
            }
           //**GO--->>CONNECTION ACHATS
		 final String[] tabloLigne;//Declarer le tableau des Enregistrements au debut de la methode pour qu'il soit visible dans tous les endroits de cette methode
      int nbreEnregistrement=0;//on créée la variable nbreEnregistrement et on l'initialise à 0
 try {IOTransactions.getConnectionUtility();//creation d'une seule instance de connection non instanciable pour firebird
             try (Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE); ResultSet result = state.executeQuery(requeteAchat)) {
                while(result.next())nbreEnregistrement++;//On obtient le nombre d'enregistrements dans cette requete
              //On determine la taille du tableau en fonction du nombre de resultats obtenus par la requete
                   tabloLigne = new String[(nbreEnregistrement+1)];
                   //On recueille le contenu des colonnes
                  for(int i = 1 ; i < tabloLigne.length ;i++){
                      result.absolute(i);
                      tabloLigne[i] = ("<HTML>*---------------------------------------"+i+"---------------------------------------*"
                              +"<BR>Numero de l'achat: "+(result.getInt(4) > 10 ? result.getInt(4) : "0" + Integer.toString(result.getInt(4)))
                              +"<BR>Numero fournisseur: "+(result.getInt(3) > 10 ? result.getInt(3) : "0" + Integer.toString(result.getInt(3)))
                              +"<BR>Nom et Prenom: "+result.getObject(8)+
                              "<BR>Date d'enregistrement: "+translateDateEnregistrement(result.getObject(1).toString())+"<BR>Type produit: "
                              +result.getObject(2)+"<BR>Quantité achetée: "+result.getObject(9));
                  }
                  
          //*************Construire la  ListModel à inclure dans la Liste des elements trouvés listeAchats *******************
          final int nombre = (nbreEnregistrement+1);
              ListModel<String> ElementsTrouves;
             //***Construction de l'ensemble ElementsTrouves
        ElementsTrouves = new AbstractListModel<String>() {
@Override
public int getSize() { return nombre; }
@Override
public String getElementAt(int index) { return tabloLigne[index]; }};
        listeAchats.setModel(ElementsTrouves);//Ici on rempli la liste de malade trouvés
//**********Fin de la construction du ListModel de malisteLigne *************
     listeAchats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//La selection doit etre sur un seul index à la fois
           listeAchats.setToolTipText("Fournisseur---DateAchat---TypeProduit---Quantité---Somme versée--->"+nbreEnregistrement+" Resultat(s)");
            //this.ScrollPatient.setViewportView(listeAchats);
           result.close();
                state.close();}
           
        } catch (NullPointerException | ClassNotFoundException | SQLException ex) {ex.printStackTrace();}
           
        //*******Fin de la Procedure de Connection 
            pTabbedGauche.setSelectedIndex(0);
        }
        
    }
    class rechercherDepense implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            //Here My code 
            String requeteFrais="Frais";//Requete generée en fonction du type de recheche
          switch (pTabbedCentre.choixRecherche.getSelectedItem().toString()) {
                case "Date d'introduction":
                   String DateFrais=pTabbedCentre.DateNaissanceRecherche.getCalendarDate();
                   requeteFrais= "SELECT * FROM GESTION_FRAIS WHERE DATE_FRAIS='"+DateFrais+"'" ;
                    break;
                case "Type de depenses":
                    String TypeFrais=((ChampBox)pTabbedGauche.typeRecherche).getSelectedItem().toString();
                   requeteFrais="SELECT *  FROM GESTION_FRAIS WHERE TYPE_FRAIS='"+TypeFrais+"'" ;
                    break;
                
            }
 //**GO--->>CONNECTION DEPENSES
     final String[] tabloLigne;//Declarer le tableau des Enregistrements au debut de la methode pour qu'il soit visible dans tous les endroits de cette methode
      int nbreEnregistrement=0;//on créée la variable nbreEnregistrement et on l'initialise à 0
 try {IOTransactions.getConnectionUtility();//creation d'une seule instance de connection non instanciable pour firebird
             try (Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE); ResultSet result = state.executeQuery(requeteFrais)) {
                while(result.next())nbreEnregistrement++;//On obtient le nombre d'enregistrements dans cette requete
              //On determine la taille du tableau en fonction du nombre de resultats obtenus par la requete
                   tabloLigne = new String[(nbreEnregistrement+1)];
                   //On recueille le contenu des colonnes
                  for(int i = 1 ; i < tabloLigne.length ; i++)  {
                      result.absolute(i);
                      tabloLigne[i] = (
                              "<HTML>*------------------------"+i+"------------------------*"
                      + "<BR>Date d'enregistrement: "+translateDateEnregistrement(result.getObject(1).toString())+"<BR>Type depense: "
                              +result.getObject(2)+"<BR>Valeur depense: "+result.getObject(3));
                  }
          //*************Construire la  ListModel à inclure dans la Liste des elements trouvés listeDepenses *******************
          final int nombre = nbreEnregistrement+1;
              ListModel<String> ElementsTrouves;
             //***Construction de l'ensemble ElementsTrouves
        ElementsTrouves = new AbstractListModel<String>() {
@Override
public int getSize() { return nombre; }
@Override
public String getElementAt(int index) { return tabloLigne[index]; }};
        listeDepenses.setModel(ElementsTrouves);//Ici on rempli la liste de malade trouvés
//**********Fin de la construction du ListModel de malisteLigne *************
     listeDepenses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//La selection doit etre sur un seul index à la fois
           listeDepenses.setToolTipText("TypeDepense---DateDepense---SommeVersée---> "+nbreEnregistrement+" Resultat(s)");
            //this.ScrollPatient.setViewportView(listeDepenses);
           result.close();
                state.close();}
           
        } catch (NullPointerException | ClassNotFoundException | SQLException ex) {ex.printStackTrace();}
           
        //*******Fin de la Procedure de Connection 
          pTabbedCentre.setSelectedIndex(0);
        }
        
    }
    class rechercherVente implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
        //Here My code 
            String requeteVente = "Ventes";//Requete generée en fonction du type de recheche
            switch (pTabbedDroite.choixRecherche.getSelectedItem().toString()) {
                case "Date d'introduction":
                     String DateVente = pTabbedDroite.DateNaissanceRecherche.getCalendarDate();
                    requeteVente= "SELECT * FROM GESTION_VENTES WHERE DATE_VENTE='"+DateVente+"'" ;
                    break;
                case "Numéro du client":
                    requeteVente="SELECT * FROM GESTION_VENTES WHERE NUM_CLIENT='"+pTabbedDroite.numeroRecherche.getText()+"'";
                    break;
                case "Type de produit":
                    String TypeProVente=(pTabbedDroite.typeRecherche).getSelectedItem().toString();
                    requeteVente="SELECT *  FROM GESTION_VENTES WHERE TYPE_PRODUIT_VENDU like '%"+TypeProVente+"%'" ;
                    break;
            }
           //**GO--->>CONNECTION VENTES
	final String[] tabloLigne;//Declarer le tableau des Enregistrements au debut de la methode pour qu'il soit visible dans tous les endroits de cette methode
                  int nbreEnregistrement=0;//on créée la variable nbreEnregistrement et on l'initialise à 0
 try {IOTransactions.getConnectionUtility();//creation d'une seule instance de connection non instanciable pour firebird
             try (Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                     ResultSet result = state.executeQuery(requeteVente)) {
                while(result.next())nbreEnregistrement++;//On obtient le nombre d'enregistrements dans cette requete
              //On determine la taille du tableau en fonction du nombre de resultats obtenus par la requete
                   tabloLigne = new String[(nbreEnregistrement+1)];
                   //On recueille le contenu des colonnes
                   for(int i = 1 ; i < tabloLigne.length ;i++){
                      result.absolute(i);
                      tabloLigne[i] = ("<HTML>*---------------------------------------"+i+"---------------------------------------*"
                              +"<BR>Nom et Prenom: "+result.getObject("NOM_PRENOM_CLIENT")+
                              "<BR>Date d'enregistrement: "+translateDateEnregistrement(result.getObject("DATE_VENTE").toString())+"<BR>Type produit: "
                              +result.getObject("TYPE_PRODUIT_VENDU")+"<BR>Quantité vendue: "+result.getObject("QUANTITE_VENDUE")+
                              "<BR>Prix Unitaire: "+result.getDouble("PRIX_UNITAIRE_VENTE")+"<BR>Avance: "+result.getDouble("SOMME_VERSEE"));
                  }
          //*************Construire la  ListModel à inclure dans la Liste des elements trouvés listeVentes *******************
          final int nombre = nbreEnregistrement+1;
              ListModel<String> ElementsTrouves;
             //***Construction de l'ensemble ElementsTrouves
        ElementsTrouves = new AbstractListModel<String>() {
@Override
public int getSize() { return nombre; }
@Override
public String getElementAt(int index) { return tabloLigne[index]; }};
        listeVentes.setModel(ElementsTrouves);//Ici on rempli la liste de malade trouvés
//**********Fin de la construction du ListModel de malisteLigne *************
     listeVentes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//La selection doit etre sur un seul index à la fois
           listeVentes.setToolTipText("NomClient---DateAchat---TypeProduit---Quantité---Somme versée--->"+nbreEnregistrement+" Resultat(s)");
            //this.ScrollPatient.setViewportView(listeVentes);
           result.close();
                state.close();}
           
        } catch (NullPointerException | ClassNotFoundException | SQLException ex) {ex.printStackTrace();}
        //*******Fin de la Procedure de Connection 
            pTabbedDroite.setSelectedIndex(0);
        }
    }
     //***************************FIN DES DIFFERENTS RECHERCHES PAR REQUETES SPECIFIQUES**********************
    class resteAchatListener implements KeyListener{
        public void keyTyped(KeyEvent e) {
        }
        public void keyPressed(KeyEvent e) {
            
        }
        public void keyReleased(KeyEvent e) {
            if(pTabbedGauche.DataAvanceSalaire.getText().equals("")
                    || pTabbedGauche.DataQuantiteProduit.equals("") || pTabbedGauche.DataPrixUnitaireProduit.equals("")
                    )pTabbedGauche.DataResteSalaire.setText("?");
            else {
                try{
                    double reste = Double.parseDouble(pTabbedGauche.DataQuantiteProduit.getText())*
                    Double.parseDouble(pTabbedGauche.DataPrixUnitaireProduit.getText()) - Double.parseDouble(pTabbedGauche.DataAvanceSalaire.getText());
                    pTabbedGauche.DataResteSalaire.setText(Double.toString(reste));
                }
                catch(NumberFormatException exp){
                    pTabbedGauche.DataResteSalaire.setText("Erreur d'initialisation...");
                }
            }
        }
    }
    class resteVenteListener implements KeyListener{
        public void keyTyped(KeyEvent e) {
        }
        public void keyPressed(KeyEvent e) {
        }
        public void keyReleased(KeyEvent e) {
            if(pTabbedDroite.DataAvanceSalaire.getText().equals("")
                    || pTabbedDroite.DataQuantiteProduit.equals("") || pTabbedDroite.DataPrixUnitaireProduit.equals("")
                    ){
                pTabbedDroite.DataResteSalaire.setText("?");
                pTabbedDroite.DataTVA.setText("?");
            }
            else {
                try{
                    double reste = Double.parseDouble(pTabbedDroite.DataQuantiteProduit.getText())*Double.parseDouble(pTabbedDroite.DataPrixUnitaireProduit.getText())- Double.parseDouble(pTabbedDroite.DataAvanceSalaire.getText());
                    double sommeTotal = Double.parseDouble(pTabbedDroite.DataQuantiteProduit.getText())*Double.parseDouble(pTabbedDroite.DataPrixUnitaireProduit.getText());
                    pTabbedDroite.DataResteSalaire.setText(Double.toString((reste)));
                    pTabbedDroite.DataTVA.setText(Double.toString(MainWindow.TAUX_TVA*sommeTotal/100));
                }
                catch(NumberFormatException exp){
                    pTabbedDroite.DataResteSalaire.setText("Erreur d'initialisation...");
                }
            }
        }
    }
    class AchatTransactionInformations implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e) {
            if(e.getValueIsAdjusting()){
                try{
                    int numeroAchat = getSelectedID(listeAchats.getSelectedValue().toString());
                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet result = state.executeQuery("select * "
                            + "from GESTION_ACHATS where NUM_ACHAT = " + numeroAchat);
                    result.first();
                    int numeroPersonnel = result.getInt(4);
                    float prixTotal = result.getFloat(7);
                    float sommeVersee = result.getFloat(5);
                    float sommeReste = result.getFloat(6);
                    pTabbedGauche.ficheAffaire.setCaptions(Integer.toString(numeroPersonnel), Float.toString(prixTotal), 
                            Float.toString(sommeVersee), Float.toString(sommeReste));
                    pTabbedGauche.ficheAffaire.setVisible(true);
                    state.close();
                    result.close();
                }
                catch(NullPointerException |ClassNotFoundException | SQLException exp){
                    exp.printStackTrace();
                }
            }
        }
    }
    class VenteTransactionInformations implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e) {
            if(e.getValueIsAdjusting()){
                
            }
        }
    }
    class AchatTransactionsUpdate implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
                int numeroAchat = getSelectedID(listeAchats.getSelectedValue().toString());
                Statement state = IOTransactions.getConnectionUtility().createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet result = state.executeQuery("select NUM_ACHAT,SOMME_VERSEE,SOMME_RESTANT,PRIX_ACHAT "
                        + "from GESTION_ACHATS where NUM_ACHAT = " + numeroAchat);
                result.first();
                if(Double.parseDouble(pTabbedGauche.ficheAffaire.nouvelleEntre.getText()) > 
                        (result.getDouble("PRIX_ACHAT")-result.getDouble("SOMME_VERSEE"))){
                    JOptionPane option = new JOptionPane();
                    option.showMessageDialog(null, "La somme introduite est superieur à la somme maximale permise", 
                            "Error...", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    double sommeRestante = Double.parseDouble(pTabbedGauche.ficheAffaire.nouveauReste.getText());
                    double sommeTotalVersee = Double.parseDouble(pTabbedGauche.ficheAffaire.nouvelleEntre.getText())+Float.parseFloat(
                        pTabbedGauche.ficheAffaire.avance.getText());
                    result.updateDouble("SOMME_RESTANT", sommeRestante);
                    result.updateDouble("SOMME_VERSEE", sommeTotalVersee);
                    result.updateRow();
                    listeAchats.clearSelection();
                    pTabbedGauche.ficheAffaire.setVisible(false);
                    pTabbedGauche.ficheAffaire.nouvelleEntre.setText("");
                    pTabbedGauche.ficheAffaire.nouveauReste.setText("");
                }
                result.close();
                state.close();
            }
            catch(NullPointerException | ClassNotFoundException | SQLException exp){
                exp.printStackTrace();
            }
            catch(NumberFormatException excep){
                JOptionPane option = new JOptionPane();
                option.showMessageDialog(null, "Veuillez verifier si toutes les informations sont valides", "Error...", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    class VenteTransactionsUpdate implements ActionListener{
        public void actionPerformed(ActionEvent e){
            
        }
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
    private int getSelectedID(String item){
        boolean startRecording = false;
        boolean recorded = false;
        String number = "";
        int time = 1;
        for(int i = 0 ; i < item.length() ; i++){
            if((item.charAt(i) == ' ') && (time == 3))startRecording = true;
            else if(item.charAt(i) == ' ' && (time == 1 || time == 2))time++;
            if(startRecording && item.charAt(i) == '<')recorded = true;
            if(startRecording && !recorded)if(item.charAt(i) != ' ')number = number+item.charAt(i);
        }
        return Integer.parseInt(number);
    }
}
