/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AdministrationPanels;

import MainPackage.MainWindow;
import ModeledWindows.FichePersonelle;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Sidi Mohamed
 */
public abstract class EspacePersonel extends MainPanel{
    private JLabel label;
    private JButton ajoutProfile;
    protected JButton confirmerRecherche;
    protected JButton confirmerChangementPersonnel;
    protected JButton promouvoir;
    protected JComboBox<String> rechercheOption;
    protected JComboBox<String> rechercheComboSexe;
    protected JComboBox<String> rechercheComboSituation;
    protected JComboBox<String> rechercheTypeCommerce;
    protected JComboBox<String> rechercheNiveauConfiance;
    protected JTextField rechercheText;
    protected JTextField recherchePersonnel ;
    protected FormulaireDate rechercheDate;
    private String liste2X;
    private String ajoutX;
    private String listeX;
    public JList listePersonnels ;
    public static JList listeSecondaireFournisseur;
    protected static JList listeSecondaireClient;
    protected JList listeRechercheAvance;
    protected JLabel labelDateEnregistrement;
    protected JLabel labelDateNaissance;
    protected ChampText[] champRecherche;
    protected DefaultListModel personelsListModel ;
    public static  DefaultListModel secondaireListModelFournisseur ;
    public static DefaultListModel secondaireListModelClient;
    protected DefaultListModel rechercheListModel;
    public FichePersonelle ficheIntroduction ;
    protected FormulaireDate majNaissance;
    protected JComboBox majActivite;
    protected JComboBox majConfiance;
    protected JComboBox majSexe;
    protected JComboBox majSituation;
    protected JPopupMenu menuContextuel;
    protected JMenuItem effacerPersonel;
    protected JMenuItem infosPromotion;
    protected JMenuItem infosVersements;
    public EspacePersonel(){
        super();
    }
    private void initMenu(){
        this.menuContextuel = new JPopupMenu();
        if(this.getClass().getSimpleName().equals("EspaceClientele")){
            this.effacerPersonel = new JMenuItem("Effacer le client");
            this.infosPromotion = new JMenuItem("Inspection avec systeme de point");
            this.infosVersements = new JMenuItem("Afficher l'historique des versements");
            this.menuContextuel.add(this.infosPromotion);
            this.menuContextuel.add(this.infosVersements);
        }
        else this.effacerPersonel = new JMenuItem("Effacer le fournisseur");
        this.menuContextuel.add(this.effacerPersonel);
        this.listePersonnels.addMouseListener(new MouseAdapter(){
            public void mouseReleased(MouseEvent e){
                if(e.isPopupTrigger())menuContextuel.show(listePersonnels, e.getX(), e.getY());
            }
        });
    }
    void initVariables(){
        if(this.getClass().getSimpleName().equals("EspaceClientele")){
            ajoutX = "Ajouter Client";
            listeX = "Liste des clients";
            liste2X = "Liste des clients non promus";
        }
        else {
            ajoutX = "Ajout Fournisseur";
            listeX = "Liste des fournisseurs";
            liste2X = "Classement des fournisseurs par nombre d'affaires conclues";
        }
        this.secondaireListModelClient = new DefaultListModel();
        this.personelsListModel = new DefaultListModel();
        this.secondaireListModelFournisseur = new DefaultListModel();
        this.rechercheListModel = new DefaultListModel();
        this.listePersonnels = new Liste(this.personelsListModel);
        if(this.getClass().getSimpleName().equals("EspaceClientele"))this.listeSecondaireClient = new Liste(this.secondaireListModelClient);
        else this.listeSecondaireFournisseur = new Liste(this.secondaireListModelFournisseur);
        this.listeRechercheAvance = new Liste(this.rechercheListModel);
    }
    private void initSearchingResult(JPanel Container){
        Container.setLayout(new BorderLayout());
        JPanel p = new Panneau(BorderFactory.createEmptyBorder(),new FlowLayout());
        p.setPreferredSize(new Dimension(300,560));
        JScrollPane pScroll = new JScrollPane(p,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pScroll.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.magenta));
        pScroll.setPreferredSize(new Dimension((int)Container.getPreferredSize().getWidth()-20,(int)Container.getPreferredSize().getHeight()));
        this.labelDateEnregistrement = new Label("?",new Dimension(150,25),true);
        this.labelDateNaissance = new Label("?",new Dimension(150,25),true);
        this.majNaissance  = new FormulaireDate();
        this.majActivite = new ChampBox("?",new Dimension(140,25));
        this.majConfiance = new ChampBox("?",new Dimension(140,25));
        this.majSexe = new ChampBox("?",new Dimension(140,25));
        this.majSituation = new ChampBox("?",new Dimension(140,25));
        this.confirmerChangementPersonnel = new Boutton("Confirmer le changement");
        this.confirmerChangementPersonnel.setPreferredSize(new Dimension(MainWindow.maxWidth/4-100,40));
        this.champRecherche = new ChampText[17];
        for(int i = 0 ; i < 15 ; i++){
            String str = null;
            switch(i){
                case 0:
                    str = "Date d'enregistrement:";
                    break;
                case 1:
                    str = "Date de naissance:";
                    break;
                case 2:
                    str = "Type d'activité:";
                    break;
                case 3:
                    if(this.getClass().getSimpleName().equals("EspaceClientele"))str = "Niveau confiance:";
                    else str = "Type marchandise:";;
                    break;
                case 4:
                    str = "Sexe";
                    break;
                case 5:
                    str = "Situation Sociale:";
                    break;
                case 6:
                    str = "Adresse:";
                    break;
                case 7:
                    str = "Code postal:";
                    break;
                case 8:
                    str = "Pays:";
                    break;
                case 9:
                    str = "Wilaya:";
                    break;
                case 10:
                    str = "Numero telephone:";
                    break;
                case 11:
                    str = "Numero bureau:";
                    break;
                case 12:
                    str = "E-mail:";
                    break;
                case 13:
                    str = "Fax";
                    break;
                case 14:
                    str = "Industrie:";
                    break;
            }
            if(i == 0)p.add(new Formulaire(new Dimension(300,25),str,this.labelDateEnregistrement,new Dimension(130,18)));
            else if( i == 1){
                p.add(new Formulaire(new Dimension(300,30),str,this.labelDateNaissance,new Dimension(130,18)));
                p.add(this.majNaissance);
            }
            else if(i == 2)p.add(new Formulaire(new Dimension(300,30),str,this.majActivite,new Dimension(110,18)));
            else if(i == 3)p.add(new Formulaire(new Dimension(300,30),str,this.majConfiance,new Dimension(110,18)));
            else if(i == 4)p.add(new Formulaire(new Dimension(300,30),str,this.majSexe,new Dimension(110,18)));
            else if(i == 5)p.add(new Formulaire(new Dimension(300,30),str,this.majSituation,new Dimension(110,18)));
            else {
                this.champRecherche[i] = new ChampText("?");
                this.champRecherche[i].setPreferredSize(new Dimension(130,20));
                p.add(new Formulaire(new Dimension(300,25),str,this.champRecherche[i],new Dimension(110,18)));
            }
        }
        p.add(this.confirmerChangementPersonnel);
        Container.add(pScroll,BorderLayout.CENTER);
    }
    private void initPromotionResult(JPanel p){
        this.promouvoir = new Boutton("Promouvoir",new Dimension(200,40));
        JPanel pNorth = new Panneau(BorderFactory.createEmptyBorder(),new FlowLayout());
        JPanel pSouth = new Panneau(BorderFactory.createEmptyBorder(),new FlowLayout());
        pNorth.setPreferredSize(new Dimension(300,200));
        pSouth.setPreferredSize(new Dimension(300,50));
        for(int i = 15 ; i < this.champRecherche.length;i++){
            String str = null;
            switch(i){
                case 15:
                    if(this.getClass().getSimpleName().equals("EspaceClientele"))str = "Nombre des points:";
                    else str = "Debut des transactions:";
                    break;
                case 16:
                    if(this.getClass().getSimpleName().equals("EspaceClientele"))str = "Nombre des promotions:";
                    else str = "Nombre d'affaires:";
                    break;
            }
            this.champRecherche[i] = new ChampText();
            this.champRecherche[i].setPreferredSize(new Dimension(40,20));
            pNorth.add(new Formulaire(new Dimension(210,25),str,this.champRecherche[i],new Dimension(140,20)));
        }
        pSouth.add(this.promouvoir);
        p.add(pNorth,BorderLayout.NORTH);
        p.add(pSouth,BorderLayout.SOUTH);
    }
    private void initRechercheAvance(Panneau p){
//--------------------------- Initialisation -----------------------------------
        String[] tabRechercheSexe = {"Masculin","Feminin"};
        String[] tabRechercheSituation = {"Celibataire","Marie(e)","Divorce(e)"};
        String[] tabRechercheOptionClient = {"Date d'enregistrement","Date de naissance","Type Activitée","Niveau confiance","Sexe","Situation Sociale","Adresse","Code postale","Pays","Wilaya"};
        String[] tabRechercheOptionFournisseur = {"Date d'enregistrement","Date de naissance","Type Activitée","Type marchandise","Sexe","Situation Sociale","Adresse","Code postale","Pays","Wilaya"};
        String[] tabRechercheTypeCommerceClient = {"Boutique","Magasin" ,"Mouturier","Super marché","Centre commercial","Cafeteria"};
        String[] tabRechercheTypeCommerceFournisseur = {"Grossiste","Importateur"};
        String[] tabRechercheNiveauConfianceClient = {"S","A","B","C","D"};
        String[] tabRechercheNiveauConfianceFournisseur = {"Cafe","Emballage"};
        this.label = new Label("Infos plus:",new Dimension(65,20));
        this.rechercheText = new ChampText();
        this.rechercheComboSexe = new ChampBox(tabRechercheSexe,new Dimension(150,25));
        this.rechercheComboSituation = new ChampBox(tabRechercheSituation,new Dimension(170,25));
        if(this.getClass().getSimpleName().equals("EspaceClientele")){
            this.rechercheOption = new ChampBox(tabRechercheOptionClient,new Dimension(170,30));
            this.rechercheTypeCommerce =  new ChampBox(tabRechercheTypeCommerceClient,new Dimension(170,25));
            this.rechercheNiveauConfiance = new ChampBox(tabRechercheNiveauConfianceClient,new Dimension(145,25));
        }
        else {
            this.rechercheOption = new ChampBox(tabRechercheOptionFournisseur,new Dimension(170,30));
            this.rechercheTypeCommerce=  new ChampBox(tabRechercheTypeCommerceFournisseur,new Dimension(145,25));
            this.rechercheNiveauConfiance = new ChampBox(tabRechercheNiveauConfianceFournisseur,new Dimension(145,25));
        }
        this.rechercheDate = new FormulaireDate();
        this.confirmerRecherche = new Boutton("Confirmer");
        this.listeRechercheAvance = new Liste(this.rechercheListModel);
        JPanel pFormulaires = new Panneau(BorderFactory.createEmptyBorder());
        JPanel pBoutton = new Panneau(BorderFactory.createEmptyBorder(),new FlowLayout());
        JPanel pWest = new Panneau(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.green, 2, true), 
                "Infos plus", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.CENTER, new Font("Arial",Font.BOLD,12),
                Color.green),new FlowLayout());
        JPanel pCenter = new Panneau(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.cyan, 2, true), 
                "Promotion informations", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.CENTER, new Font("Arial",Font.BOLD,12),
                Color.cyan));
        JPanel pEast = new Panneau(BorderFactory.createEmptyBorder());
        JPanel pEastWest = new Panneau(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.red, 2, true), 
                "Faire une recherche", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.CENTER, new Font("Arial",Font.BOLD,12),
                Color.red));
        JPanel pEasttEast = new Panneau(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.blue, 2, true), 
                "Result", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.CENTER, new Font("Arial",Font.BOLD,12),
                Color.blue));
        JPanel pEastWestNorth = new Panneau(BorderFactory.createEmptyBorder(),new FlowLayout());
        JPanel pEastEastNorth = new Panneau(BorderFactory.createEmptyBorder(),new FlowLayout());
        JPanel pEastEastContainer = new Panneau(BorderFactory.createLineBorder(Color.white, 2));
        JLabel labelRechercheAvance = new Label("Resultat de la recherche:",new Dimension(140,20));
        JScrollPane pScrollRecherche = new JScrollPane(this.listeRechercheAvance);
//--------------------------- Caractérisation ----------------------------------
        this.rechercheText.setVisible(false);
        this.rechercheComboSexe.setVisible(false);
        this.rechercheComboSituation.setVisible(false);
        this.rechercheTypeCommerce.setVisible(false);
        this.rechercheNiveauConfiance.setVisible(false);
        pEastWest.setPreferredSize(new Dimension((MainWindow.maxWidth-MainWindow.maxWidth/6)/4,MainWindow.maxHeight));
        pEasttEast.setPreferredSize(new Dimension((MainWindow.maxWidth-MainWindow.maxWidth/6)/4,MainWindow.maxHeight));
        pWest.setPreferredSize(new Dimension(MainWindow.maxWidth/5+MainWindow.maxWidth/36,MainWindow.maxHeight));
//---------------------------------------Adding ------------------------------\\
        this.initSearchingResult(pWest);
        this.initPromotionResult(pCenter);
        pBoutton.add(this.confirmerRecherche);
        pEastWestNorth.add(new Formulaire("Trier par:",this.rechercheOption,new Dimension(60,50)));
        pEastWestNorth.add(this.label);
        pEastWestNorth.add(this.rechercheDate);
        pEastWestNorth.add(this.rechercheText);
        pEastWestNorth.add(this.rechercheComboSexe);
        pEastWestNorth.add(this.rechercheComboSituation);
        pEastWestNorth.add(this.rechercheTypeCommerce);
        pEastWestNorth.add(this.rechercheNiveauConfiance);
        pEastEastNorth.add(labelRechercheAvance);
        pEastWest.add(pEastWestNorth,BorderLayout.CENTER);
        pEastWest.add(pBoutton,BorderLayout.SOUTH);
        pEastEastContainer.add(pScrollRecherche,BorderLayout.CENTER);
        pEastEastContainer.add(pEastEastNorth,BorderLayout.NORTH);
        pEasttEast.add(pEastEastContainer,BorderLayout.CENTER);
        pEast.add(pEastWest,BorderLayout.WEST);
        pEast.add(new Spacer(new Dimension(5,20)),BorderLayout.CENTER);
        pEast.add(pEasttEast,BorderLayout.EAST);
        pFormulaires.add(pWest,BorderLayout.WEST);
        pFormulaires.add(pEast,BorderLayout.EAST);
        pFormulaires.add(pCenter,BorderLayout.CENTER);
        p.add(pFormulaires,BorderLayout.CENTER);
    }
    void initComponent() {
        this.initMenu();
        this.ajoutProfile = new JButton(new ImageIcon(getClass().getResource("/Images/NAjouterPersonne.png")));
        this.recherchePersonnel = new ChampText();
//------------------------ Initilisation Conteneurs ----------------------------               
        JPanel pRecherche = new JPanel();
        JPanel centerPane = new Panneau(BorderFactory.createEmptyBorder());
        JPanel pUser = new Panneau(BorderFactory.createEmptyBorder());
        JPanel pNewUser = new Panneau(this.ajoutX);
        JPanel pListe = new Panneau(this.listeX);
        JPanel pSpecialUserSearching = new Panneau(this.liste2X);
        Panneau pRechercheAvance = new Panneau("Details");
        JLabel label = new JLabel("Nom:");
        JScrollPane pScrollListe1 = new JScrollPane(this.listePersonnels,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane pScrollListe2;
        if(this.getClass().getSimpleName().equals("EspaceClientele"))pScrollListe2 = new JScrollPane(
                this.listeSecondaireClient,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        else pScrollListe2 = new JScrollPane(
        this.listeSecondaireFournisseur,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//----------------------------- Specifications ---------------------------------
        pListe.setPreferredSize(new Dimension(MainWindow.maxWidth/6,MainWindow.maxHeight));
        pNewUser.setPreferredSize(new Dimension(200,200));
        pRecherche.setBackground(Color.white);
//--------------------------------- Adding -------------------------------------
        this.initRechercheAvance(pRechercheAvance);
        pRecherche.add(label);
        pRecherche.add(this.recherchePersonnel);
        pListe.add(pScrollListe1,BorderLayout.CENTER);
        pListe.add(pRecherche,BorderLayout.NORTH);
        pNewUser.add(this.ajoutProfile,BorderLayout.CENTER);
        pSpecialUserSearching.add(pScrollListe2,BorderLayout.CENTER);
        pUser.add(pNewUser,BorderLayout.WEST);
        pUser.add(pSpecialUserSearching,BorderLayout.CENTER);
        centerPane.add(pUser,BorderLayout.NORTH);
        centerPane.add(pRechercheAvance,BorderLayout.CENTER);
        this.add(centerPane,BorderLayout.CENTER);
        this.add(pListe,BorderLayout.WEST);
    }
    void initListeners(){
        this.ajoutProfile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ficheIntroduction.setVisible(true);
            }
        });
        this.rechercheOption.addItemListener(new SearchingOptions());
    }
    class Liste extends JList{
        public Liste(DefaultListModel collection){
            super(collection);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED,Color.cyan,Color.magenta));
        }
    }
    class Panneau extends JPanel{
        public Panneau(String title){
            this.setBackground(Color.white);
            this.setBorder(BorderFactory.createTitledBorder(MainWindow.bordure, 
                    title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, 
                    new Font("Arial",Font.BOLD,16), Color.black));
            this.setLayout(new BorderLayout());
        }
        public Panneau(Border bordure){
            this.setBackground(Color.white);
            this.setBorder(bordure);
            this.setLayout(new BorderLayout());
        }
        public Panneau(Border bordure , LayoutManager b){
            this.setBackground(Color.white);
            this.setBorder(bordure);
            this.setLayout(b);
        }
    }
    class Label extends JLabel{
        private String text;
        private boolean centered = false;
        public Label(String str){
            this.text = str;
            this.setPreferredSize(new Dimension(120,20));
        }
        public void paintComponent(Graphics g){
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            FontMetrics fm = g.getFontMetrics();
            int largeur = fm.getHeight();
            g.setColor(Color.black);
            if(!centered)g.drawString(text, 0 , this.getHeight()/2+largeur/4);
            else {
                int longueur = fm.stringWidth(text);
                g.drawString(text, this.getWidth()/2-longueur/2 , this.getHeight()/2+largeur/4);
            }
        }
        public Label(String str,Dimension dim){
            this.text = str;
            this.setPreferredSize(dim);
        }
        public Label(String str,Dimension dim,boolean bool){
            this.text = str;
            this.setPreferredSize(dim);
            this.centered = bool;
        }
        public void setText(String str){
            this.text = str;
            this.repaint();
        }
        public String getText(){
            return this.text;
        }
    }
    class Formulaire extends JPanel{
        private JLabel label ;
        public Formulaire(String str,Component comp){
            this.setBackground(Color.white);
            this.initComponent(str,comp);
        }
        public Formulaire(String str,Component comp,Dimension dim){
            this.setBackground(Color.white);
            this.initComponent(str, comp);
            this.label.setPreferredSize(dim);
        }
        public Formulaire(Dimension dim,String str,Component comp){
            this.setBackground(Color.white);
            this.initComponent(str, comp);
            this.setPreferredSize(dim);
        }
        public Formulaire(Dimension dim,String str,Component comp,Dimension dim2){
            this.setBackground(Color.white);
            this.initComponent(str, comp);
            this.label.setPreferredSize(dim2);
            this.setPreferredSize(dim);
        }
        public Formulaire(Component comp){
            this.setBackground(Color.white);
            this.initComponent(comp);
        }
        private void initComponent(Component comp){
            this.add(comp);
        }
        private void initComponent(String str,Component comp){
            this.label = new Label(str);
            this.add(this.label);
            this.add(comp);
        }
    }
    class Boutton extends JButton{
        public Boutton(String str){
            super(str);
            this.setForeground(Color.black);
            this.setPreferredSize(new Dimension(250,50));
        }
        public Boutton(String str,Dimension dim){
            super(str);
            this.setForeground(Color.black);
            this.setPreferredSize(dim);
        }
    }
    class ChampText extends JTextField{
        public ChampText(){
            this.setPreferredSize(new Dimension(150,20));
            this.setHorizontalAlignment(JTextField.HORIZONTAL);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
        public ChampText(String str){
            super(str);
            this.setPreferredSize(new Dimension(150,20));
            this.setHorizontalAlignment(JTextField.HORIZONTAL);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
    }
    class ChampBox extends JComboBox{
        public ChampBox(String[] tabString){
            super(tabString);
            this.setPreferredSize(new Dimension(150,30));
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
        public ChampBox(String str,Dimension dim){
            this.addItem(str);
            this.setPreferredSize(dim);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
        public ChampBox(String[] tabString , Dimension dim){
            super(tabString);
            this.setPreferredSize(dim);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
    }
    class Spacer extends JPanel{
        public Spacer(Dimension dim){
            this.setPreferredSize(dim);
            this.setBackground(Color.white);
        }
    }
    class SearchingOptions implements ItemListener{
        public void itemStateChanged(ItemEvent e){
            if(e.getStateChange() == ItemEvent.SELECTED){
                if(rechercheOption.getSelectedIndex() == 0 || rechercheOption.getSelectedIndex() == 1){
                    rechercheText.setVisible(false);
                    rechercheComboSexe.setVisible(false);
                    rechercheComboSituation.setVisible(false);
                    rechercheTypeCommerce.setVisible(false);
                    rechercheDate.setVisible(true);
                    rechercheNiveauConfiance.setVisible(false);
                }
                if(rechercheOption.getSelectedIndex() == 2){
                    rechercheText.setVisible(false);
                    rechercheComboSexe.setVisible(false);
                    rechercheComboSituation.setVisible(false);
                    rechercheDate.setVisible(false);
                    rechercheNiveauConfiance.setVisible(false);
                    rechercheTypeCommerce.setVisible(true);
                }
                else if(rechercheOption.getSelectedIndex() == 5){
                    rechercheText.setVisible(false);
                    rechercheComboSexe.setVisible(false);
                    rechercheComboSituation.setVisible(true);
                    rechercheDate.setVisible(false);
                    rechercheNiveauConfiance.setVisible(false);
                    rechercheTypeCommerce.setVisible(false);
                }
                else if(rechercheOption.getSelectedIndex() == 4){
                    rechercheText.setVisible(false);
                    rechercheComboSexe.setVisible(true);
                    rechercheComboSituation.setVisible(false);
                    rechercheDate.setVisible(false);
                    rechercheNiveauConfiance.setVisible(false);
                    rechercheTypeCommerce.setVisible(false);
                }
                else if(rechercheOption.getSelectedIndex() == 3) {
                    rechercheText.setVisible(false);
                    rechercheComboSexe.setVisible(false);
                    rechercheComboSituation.setVisible(false);
                    rechercheDate.setVisible(false);
                    rechercheTypeCommerce.setVisible(false);
                    rechercheNiveauConfiance.setVisible(true);
                }
                else if (rechercheOption.getSelectedIndex() >= 6){
                    rechercheText.setVisible(true);
                    rechercheComboSexe.setVisible(false);
                    rechercheComboSituation.setVisible(false);
                    rechercheDate.setVisible(false);
                    rechercheTypeCommerce.setVisible(false);
                }
            }
        }
    }    
    class FormulaireDate extends JDateChooser{
        public FormulaireDate(){
            this.setBackground(Color.white);
            this.setPreferredSize(new Dimension(180,30));
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
        public String getInformations(){
            return (this.jcalendar.getCalendar().get(5) < 10? "0"+Integer.toString(this.jcalendar.getCalendar().get(5)) : Integer.toString(this.jcalendar.getCalendar().get(5)))
                    +(this.jcalendar.getCalendar().get(2)+1 < 10? "-0"+Integer.toString(this.jcalendar.getCalendar().get(2)+1) : "-"+Integer.toString(this.jcalendar.getCalendar().get(2)+1))
                    +"-"+Integer.toString(this.jcalendar.getCalendar().get(1));
        }
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
    private String tournerStr(String str){
        String str2 = "";
        for(int i = 1 ; i <= str.length(); i++)str2 = str2 + str.charAt(str.length()-i);
        return str2;
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
    public String transcriptDateEnregistrement(String str){
        String date = "";
        for(int i = 0 ; i < str.length() ; i++){
            if(str.charAt(i) == '-')date = date + '.';
            else date = date + str.charAt(i);
        }
        return date;
    }
    public boolean isAvailable(JComboBox<String> combo,String item){
        boolean b = false;
        try{
            for(int i = 0 ; i < combo.getItemCount() ; i++){
                if(combo.getItemAt(i).toString().equals(item))b = true;
            }
        }
        catch(NullPointerException exp){
        }
        return b;
    }
    public void addTab(JComboBox<String> combo,String[] tabString){
        for(int i = 0 ; i < tabString.length ; i++){
            if(!isAvailable(combo,tabString[i]))combo.addItem(tabString[i]);
        }
    }
    public String transcriptToReading(String str){
        String caption = "";
        try{
            boolean debut = true;
            String strLower = str.toLowerCase();
            boolean ifUpper = true;
            for(int i = 0 ; i < str.length() ; i++){
                if(str.charAt(i) == ' ')ifUpper = true;
                if(ifUpper){
                    if(debut){
                        caption = caption + str.charAt(0);
                        debut = false;
                        ifUpper = false;
                    }
                    else {
                        caption = caption + " " + str.charAt(i+1);
                        i++;
                        debut = false;
                        ifUpper = false;
                    }
                }
                else caption = caption + strLower.charAt(i);
            }
        }
        catch(NullPointerException exp){
        }
        return caption;
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
}

    
