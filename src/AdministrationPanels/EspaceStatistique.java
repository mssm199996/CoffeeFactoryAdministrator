/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdministrationPanels;

import MainPackage.IOTransactions;
import MainPackage.MainWindow;
import ModeledWindows.IntroductionWindow;
import com.toedter.calendar.JDateChooser;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lennovo
 */
public class EspaceStatistique extends MainPanel{
   private Tableau tableau ;
   private PanneauInfo pInfos;
   private PanneauTorrefaction pTorrefaction;
   private PanneauAffaire pAffaire;
   private PanneauVente pVente;
   private PanneauAchat pAchat;
   private int ventes = 0 ;
   private int achats = 0 ;
   private int depenses = 0 ;
   private JPanel panoChoix;
   private JCheckBox CheckVente;
   private JCheckBox CheckAchat;
   private JCheckBox CheckDepense;
   private String KVente="1";
   private String KAchat="1";
   private String KDepense="1";
public EspaceStatistique(){
        super();
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
    }
   @Override
void initListeners() {
    }
   @Override
void initComponent() {
        this.tableau = new Tableau();
        this.pInfos = new PanneauInfo();
        this.pTorrefaction = new PanneauTorrefaction();
        this.pAffaire = new PanneauAffaire();
        this.pVente = new PanneauVente();
        this.pAchat = new PanneauAchat();
        this.panoChoix=new JPanel();
        this.panoChoix.setBackground(Color.white);//Panneau contenant les 3 checkBox 
        this.CheckVente=new JCheckBox();//Les CheckBox
        this.CheckAchat=new JCheckBox();//--
        this.CheckDepense=new JCheckBox();//--
//Inscription des Evenements des CheckBox 
        CheckVente.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckVenteActionPerformed(evt);
            }
        });
        CheckAchat.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckAchatActionPerformed(evt);
            }
        });
        CheckDepense.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckDepenseActionPerformed(evt);
            }
        });
//FIN de l'Inscription des evenements
        CheckVente.setSize(100,200) ;CheckVente.setVisible(true);
        CheckVente.setBackground(Color.white);
        CheckVente.setOpaque(true);
        CheckVente.setText("Ventes                 ");
        CheckVente.setSelected(true);
        CheckAchat.setSize(100,200);
        CheckAchat.setVisible(true);
        CheckAchat.setBackground(Color.white);
        CheckAchat.setOpaque(true);
        CheckAchat.setText("Achats                 ");
        CheckAchat.setSelected(true);
        CheckDepense.setSize(100,200);
        CheckDepense.setVisible(true);
        CheckDepense.setBackground(Color.white);
        CheckDepense.setOpaque(true);
        CheckDepense.setText("Depenses  ");
        CheckDepense.setSelected(true);
        CheckVente.setForeground(new Color(0,102,0));
        CheckAchat.setForeground(new Color(255,0,0));
        CheckDepense.setForeground(new Color(128,128,255));
        CheckAchat.setFont(new Font("Arial",Font.BOLD,16));
        CheckDepense.setFont(new Font("Arial",Font.BOLD,16));
        CheckVente.setFont(new Font("Arial",Font.BOLD,16));
        JScrollPane pScrollAchat = new JScrollPane(this.pAchat,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                ,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pScrollAchat.setBorder(BorderFactory.createEmptyBorder());
        JTabbedPane pNorthEast = new JTabbedPane();
        pNorthEast.setFont(new Font("Arial",Font.BOLD,16));
        pNorthEast.addTab("Panneau Inventaire",this.pTorrefaction);
        pNorthEast.addTab("Panneau des ventes", this.pVente);
        pNorthEast.addTab("Panneau d'achats", pScrollAchat);
        Panneau pSouth = new Panneau((int)(3*MainWindow.maxHeight/4-120),
                "Liste des ventes",new BorderLayout());
        Panneau pNorth = new Panneau((int)(MainWindow.maxHeight/4-30),new BorderLayout());
        JScrollPane pScrollTableau = new JScrollPane(
                this.tableau,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //**************************
        //this.panoChoix.setSize(pSouth.getHeight(), 100);
        panoChoix.add(this.CheckVente,BorderLayout.CENTER);
        panoChoix.add(this.CheckAchat,BorderLayout.CENTER);
        panoChoix.add(this.CheckDepense,BorderLayout.CENTER);
        pSouth.add(panoChoix,BorderLayout.NORTH);
        pScrollTableau.setViewportView(this.tableau);
        pScrollTableau.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pScrollTableau.setBackground(Color.white);
        pNorthEast.addTab("Panneau Inventaire",this.pTorrefaction);
        pNorthEast.addTab("Panneau de Gains/Pertes",this.pAffaire);
        pNorthEast.addTab("Panneau prix unitaire",new PanneauPU());
        pSouth.add(pScrollTableau,BorderLayout.CENTER);
        pNorth.add(pNorthEast,BorderLayout.CENTER);
        pNorth.add(this.pInfos,BorderLayout.WEST);
        this.add(pSouth,BorderLayout.SOUTH);
        this.add(pNorth,BorderLayout.NORTH);
    }
class PanneauPU extends Panneau{
        public PanneauPU(){
            super(400,new BorderLayout());
        }
        @Override
        public void paintComponent(Graphics g){
         Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(5, 0, 5, this.getHeight()-5);
            g2.drawLine(5, this.getHeight()-5, this.getWidth()-5, this.getHeight()-5);
            g2.setColor(Color.red);
            for(int i = 0 ; i < tableau.getRowCount() ; i++){
                if(tableau.getModel().getValueAt(i, 2).equals("Vente")){
                    int prixUnitaire = (int)((double)tableau.getModel().getValueAt(i, 8));
                    g2.drawLine(5*i + 5, this.getHeight()-prixUnitaire, 5*i + 5, this.getHeight()-prixUnitaire);
                }
            }
        }
    }
private void CheckVenteActionPerformed(java.awt.event.ActionEvent evt) { 
        if(this.CheckVente.isSelected()){this.KVente="1";}
        else{this.KVente="0";}
        //Relancer automatiquement la recherche
        SearchingInOut();
        SearchingCoffeeTransactions();
    } 
private void CheckAchatActionPerformed(java.awt.event.ActionEvent evt) {                                           
        if(this.CheckAchat.isSelected()){this.KAchat="1";}
        else{this.KAchat="0";}
        //Relancer automatiquement la recherche
        SearchingInOut();
        SearchingCoffeeTransactions();
    } 
private void CheckDepenseActionPerformed(java.awt.event.ActionEvent evt) {                                           
        if(this.CheckDepense.isSelected()){this.KDepense="1";}
        else{this.KDepense="0";}
        //Relancer automatiquement la recherche
        SearchingInOut();
        SearchingCoffeeTransactions();
    } 
        
class PanneauInfo extends Panneau{
        public FormulaireDate dateA;
        public FormulaireDate dateB;
        public JButton bouttonBilan;
        public PanneauInfo(){
            super(0,"Recherche ventes:",new FlowLayout());
            this.setPreferredSize(new Dimension((int)MainWindow.maxWidth/4,(int)(MainWindow.maxHeight/4)-50));
            this.initComponent();
            bouttonBilan.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    SearchingInOut();
                    SearchingCoffeeTransactions();
                }
            });
        }
        private void initComponent(){
            this.dateA = new FormulaireDate();
            this.dateB = new FormulaireDate();
            this.bouttonBilan = new JButton("Faire un bilan");
            this.bouttonBilan.setPreferredSize(new Dimension(200,35));
            this.add(new Formulaire("Date n° 1:",this.dateA));
            this.add(new Formulaire("Date n° 2:",this.dateB));
            this.add(this.bouttonBilan);
        }        
    }
class PanneauTorrefaction extends Panneau{
        public Label qVertTorrifiee;
        public Label qGrainMoulue;
        public Label qConditionnee;
        public Label qAluminium;
        public Label qKraft;
        public PanneauTorrefaction(){
            super(0,"Bilan de torrefaction:",new FlowLayout());
            this.setPreferredSize(new Dimension((int)3*MainWindow.maxWidth/8-25,(int)(MainWindow.maxHeight/4)-25));
            this.initComponents();
        }
        public void initComponents(){
            this.qVertTorrifiee = new Label("?",true);
            this.qGrainMoulue = new Label("?",true);
            this.qConditionnee = new Label("?",true);
            this.qAluminium = new Label("?",true);
            this.qKraft = new Label("?",true);
            this.add(new Formulaire("Quantite de C.Vert ayant été torrefiée: ",this.qVertTorrifiee,new Dimension(250,20)));
            this.add(new Formulaire("Quantite de C.Torrefié ayant été moulue: ",this.qGrainMoulue,new Dimension(220,20)));
            this.add(new Formulaire("Quantite de Café conditionné: ",this.qConditionnee,new Dimension(250,20)));
            this.add(new Formulaire("Quantite d'Aluminium ayant été utilisé: ",this.qAluminium,new Dimension(220,20)));
            this.add(new Formulaire("Quantite de Kraft ayant été utilisé: ",this.qKraft,new Dimension(185,20)));
        }
        public void setInformations(double qVertTorrifiee ,double qGrainMoulue,double qConditionnee,double qAluminium,double qKraft){
            this.qVertTorrifiee.setText(Double.toString(qVertTorrifiee));
            this.qGrainMoulue.setText(Double.toString(qGrainMoulue));
            this.qConditionnee.setText(Double.toString(qConditionnee));
            this.qAluminium.setText(Double.toString(qAluminium));
            this.qKraft.setText(Double.toString(qKraft));
        }
    }
class PanneauVente extends Panneau{
        public Label qVenduGrain;
        public Label qVenduVrac;
        public Label qVenduConditionne;
        public PanneauVente(){
            super(0,"Bilan des ventes:",new FlowLayout());
            this.setPreferredSize(new Dimension((int)3*MainWindow.maxWidth/8-10,(int)(MainWindow.maxHeight/4)-50));
            this.initComponents();
        }
        public void initComponents(){
            this.qVenduGrain = new Label("?",true);
            this.qVenduVrac = new Label("?",true);
            this.qVenduConditionne = new Label("?",true);
            this.add(new Formulaire("Quantite de C.Torrefié (Grains) vendue:",this.qVenduGrain,new Dimension(220,20)));
            this.add(new Formulaire("Quantite de C.Torrefié (Grains) [En vrac] vendue:",this.qVenduVrac,new Dimension(270,20)));
            this.add(new Formulaire("Quantite de C.Torrefié (Grains) [Conditionné] vendue:",this.qVenduConditionne,new Dimension(290,20)));
        }
        public void setInformations(double qGrainVendu ,double qVracVendu,double qConditionneVendu){
            this.qVenduGrain.setText(Double.toString(qGrainVendu));
            this.qVenduVrac.setText(Double.toString(qVracVendu));
            this.qVenduConditionne.setText(Double.toString(qConditionneVendu));
        }
    }
class PanneauAffaire extends Panneau{
        public Label gainAvance;
        public Label gainDette;
        public Label gainTotal;
        public Label perteActuel;
        public CircleGrapher pCenter;
        public PanneauAffaire(){
            super(0,"Bilan des Gains/Pertes",new BorderLayout());
            this.setPreferredSize(new Dimension((int)3*MainWindow.maxWidth/8-10,(int)(MainWindow.maxHeight/4)-50));
            this.initComponents();
        }
        private void initComponents(){
            this.perteActuel = new Label("?",true,new Color(255,0,0));
            this.gainAvance = new Label("?",true,new Color(0,0,255));
            this.gainDette = new Label("?",true,new Color(0,0,255));
            this.gainTotal = new Label("?",true,new Color(0,0,255));
            this.pCenter = new CircleGrapher(); 
            JPanel pWest = new JPanel();
            JPanel pEast = new JPanel();
            pWest.setPreferredSize(new Dimension(260,40));
            pEast.setPreferredSize(new Dimension(260,40));
            pWest.setBackground(Color.white);
            pEast.setBackground(Color.white);
            pWest.add(new Formulaire("Avances:",this.gainAvance,new Dimension(100,20),new Color(0,0,255)));
            pWest.add(new Formulaire("Dettes payées:",this.gainDette,new Dimension(100,20),new Color(0,0,255)));
            pWest.add(new Formulaire("Gain total:",this.gainTotal,new Dimension(100,20),new Color(0,0,255)));
            pEast.add(new Formulaire("Total des Pertes:",this.perteActuel,new Dimension(100,20),new Color(255,0,0)));
            this.add(pWest,BorderLayout.WEST);
            this.add(pEast,BorderLayout.EAST);
            this.add(pCenter,BorderLayout.CENTER);
        }
        public void setInformation(double gainActuel,double gainDette,double perteActuel){
            this.gainAvance.setText(Double.toString(gainActuel));
            this.gainDette.setText(Double.toString(gainDette));
            this.gainTotal.setText(Double.toString(gainActuel+gainDette));
            this.perteActuel.setText(Double.toString(perteActuel));
            this.pCenter.repaint();
        }
        class CircleGrapher extends JPanel{
            int degres = 0;
            public CircleGrapher(){
                this.setPreferredSize(new Dimension(150,150));
                this.setLayout(new BorderLayout());
            }
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D)g;
                g2.setColor(Color.white);
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                g2.setColor(Color.black);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(this.getWidth()/2-50,this.getHeight()/2-50,100,100);
                try{
                    fillGrapher(g2);
                }
                catch(NumberFormatException exp){
                    exp.printStackTrace();
                }
            }
            private void fillGrapher(Graphics2D g2){
                Color c = null;
                int debut = 0;
                double sommeTotal = Double.parseDouble(gainTotal.getText())
                        +Double.parseDouble(perteActuel.getText());
                for(int i = 0 ; i < 2 ; i++){
                    switch(i){
                        case 0:
                            c = new Color(0,128,0);//Blue
                            degres = Math.round((float)((Double.parseDouble(gainTotal.getText())*360)/sommeTotal));
                            break;
                        case 1:
                            c = new Color(255,0,0);//Red
                            degres = Math.round((float)((Double.parseDouble(perteActuel.getText())*360)/sommeTotal));
                            break;
                    }
                    g2.setColor(c);
                    g2.fillArc(this.getWidth()/2-50,this.getHeight()/2-50,100,100, debut, degres);
                    debut = debut+degres;
                }
            }
        }
    }
class PanneauAchat extends Panneau{
        public Label qAchatGrain;
        public Label qAchatVrac;
        public Label qAchatConditionne;
        public Label qAchatVert;
        public Label qAluminiumDispo;
        public Label qKraftDispo;
        public PanneauAchat(){
            super(0,"Bilan des achats",new FlowLayout());
            this.setPreferredSize(new Dimension((int)3*MainWindow.maxWidth/8-10,(int)(MainWindow.maxHeight/4)-65));
            this.initComponents();
        }
        public void initComponents(){
            this.qAchatGrain = new Label("?",true);
            this.qAchatVrac = new Label("?",true);
            this.qAchatConditionne = new Label("?",true);
            this.qAchatVert = new Label("?",true);
            this.qAluminiumDispo = new Label("?",true);
            this.qKraftDispo = new Label("?",true);
            this.add(new Formulaire("Quantite de C.Vert achetée:",this.qAchatVert,new Dimension(300,20)));
            this.add(new Formulaire("Quantite de C.Torrefié (Grains) achetée:",this.qAchatGrain,new Dimension(300,20)));
            this.add(new Formulaire("Quantite de C.Torrefié (Grains) [En Vrac] achetée:",this.qAchatVrac,new Dimension(300,20)));
            this.add(new Formulaire("Quantite de C.Torrefié (Grains) [Conditionné] achetée:",this.qAchatConditionne,new Dimension(300,20)));
            this.add(new Formulaire("Quantite d'Aluminium achetée:",this.qAluminiumDispo,new Dimension(300,20)));
            this.add(new Formulaire("Quantite de Kraft achetée:",this.qKraftDispo,new Dimension(300,20)));
        }
        public void setInformations(double qVertAchatee,double qGrainAchetee ,double qVracAchetee,double qConditionneAchetee,double qAluminiumDispo ,double qKraftDispo){
            this.qAchatVert.setText(Double.toString(qVertAchatee));
            this.qAchatGrain.setText(Double.toString(qGrainAchetee));
            this.qAchatVrac.setText(Double.toString(qVracAchetee));
            this.qAchatConditionne.setText(Double.toString(qConditionneAchetee));
            this.qAluminiumDispo.setText(Double.toString(qAluminiumDispo));
            this.qKraftDispo.setText(Double.toString(qKraftDispo));
        }
    }
class TableModel extends DefaultTableModel{
        public TableModel(){
            super(new String[]{"N°","Date d'enregistrement","Type entre/sortie","Type produit","Quantite","Gain/Perte actuel (DA)",
                "Gain/Perte total (DA)","NUM_STAT","PRIX_UNITAIRE"}, 0);
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
            this.setColumnSelectionAllowed(false);
            this.setRowSelectionAllowed(false);
            this.setGridColor(Color.black);
            this.setShowVerticalLines(true);
            this.setShowHorizontalLines(true);
            this.setFillsViewportHeight(true);
            this.setFillsViewportHeight(true);
            this.getColumnModel().getColumn(0).setPreferredWidth(10);
            this.getColumnModel().getColumn(1).setPreferredWidth(40);
            this.getColumnModel().getColumn(2).setPreferredWidth(30);
            this.getColumnModel().getColumn(3).setPreferredWidth(150);
            this.getColumnModel().getColumn(4).setPreferredWidth(100);
            this.getColumnModel().getColumn(5).setPreferredWidth(150);
            this.getColumnModel().getColumn(6).setPreferredWidth(150);
            this.getColumnModel().getColumn(7).setPreferredWidth(150);
            this.setSelectionBackground(new java.awt.Color(255, 255, 255));
            this.setSelectionForeground(new java.awt.Color(0, 0, 0));
            this.removeColumn(this.getColumnModel().getColumn(7));
            this.removeColumn(this.getColumnModel().getColumn(7));
            this.setDefaultRenderer(Object.class, new MyRenderer());
        }
        public void clearTable(){
            while(this.getRowCount() != 0){
                this.tModel.removeRow(0);
            }
        }
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
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
public class FormulaireDate extends JDateChooser{
        public FormulaireDate(){
            this.setBackground(Color.white);
            this.setPreferredSize(new Dimension(150,30));
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
            this.jcalendar.setDate(new Date(0,0,0));
        }
        public String getInformations(){
            return (this.jcalendar.getCalendar().get(5) < 10? "0"+Integer.toString(this.jcalendar.getCalendar().get(5)) : Integer.toString(this.jcalendar.getCalendar().get(5)))
                    +(this.jcalendar.getCalendar().get(2)+1 < 10? "-0"+Integer.toString(this.jcalendar.getCalendar().get(2)+1) : "-"+Integer.toString(this.jcalendar.getCalendar().get(2)+1))
                    +"-"+Integer.toString(this.jcalendar.getCalendar().get(1));
        }
    }
class Label extends JLabel{
        private String text;
        private boolean centering ;
        private Color foreGround; 
        public Label(String str,boolean b){
            this.text = str;
            this.centering = b;
            this.setPreferredSize(new Dimension(150,20));
            if(centering)this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
            this.foreGround = Color.black;
        }
        public Label(String str,boolean b,Color c){
            this.text = str;
            this.centering = b;
            this.setPreferredSize(new Dimension(150,20));
            if(centering)this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
            this.foreGround = c;
        }
        @Override
        public void paintComponent(Graphics g){
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(this.foreGround);
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
        public Formulaire(String str,Component comp,Dimension dim){
            this.setBackground(Color.white);
            this.initComponent(str,comp);
            this.label.setPreferredSize(dim);
        }
        public Formulaire(String str,Component comp,Dimension dim,Color c){
            this.setBackground(Color.white);
            this.initComponent(str,comp,c);
            this.label.setPreferredSize(dim);
        }
        private void initComponent(String str,Component comp){
            this.label = new Label(str,false);
            this.add(this.label);
            this.add(comp);
        }
        private void initComponent(String str,Component comp,Color c){
            this.label = new Label(str,false,c);
            this.add(this.label);
            this.add(comp);
        }
        public String getInformation(){
            return this.champText.getText();
        }
        public void clearFormulaire(){
            this.champText.setText("?");
        }
    }
public void SearchingInOut(){
        tableau.clearTable();
        ventes = 0;
        achats = 0;
        depenses = 0;
        double sGainHO = 0;
        double sGainO = 0;
        double sPerteHO = 0;
        double sPerteO = 0;
        double qVert = 0;
        double qGrain = 0;
        double qVrac = 0;
        double qConditionne = 0;
        double qAluminiumDispo = 0;
        double qKraftDispo = 0;
        try{
            try (Statement state = IOTransactions.getConnectionUtility().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE)) {
                ResultSet result;
                int i;
                //*********************Affichage VENTES
                if("1".equals(KVente)){
                    result = state.executeQuery("select * from GESTION_VENTES where "
                            +"DATE_VENTE between '"+transcriptDateEnregistrement(pInfos.dateA.getInformations())+"' "
                            + "and '"+transcriptDateEnregistrement(pInfos.dateB.getInformations())+ "'");
                    i = 1;
                    while(result.next()){
                        ventes++;
                        result.absolute(i);
                        Object[] rowData = {i,translateDateEnregistrement(result.getDate("DATE_VENTE").toString()),"Vente",
                            result.getString("TYPE_PRODUIT_VENDU"),result.getDouble("QUANTITE_VENDUE"),
                            result.getDouble("SOMME_VERSEE"),result.getDouble("PRIX_VENTE"),
                            result.getInt("NUM_VENTE"),result.getDouble("PRIX_UNITAIRE_VENTE")};
                        tableau.tModel.addRow(rowData);
                        switch(result.getString("TYPE_PRODUIT_VENDU")){
                            case "Cafe torrefie (grains)":
                                qGrain += result.getDouble("QUANTITE_VENDUE");
                                break;
                            case "Cafe torrefie (moulu) [en vrac]":
                                qVrac += result.getDouble("QUANTITE_VENDUE");
                                break;
                            case "Cafe torrefie (moulu) [conditionne]":
                                qConditionne += result.getDouble("QUANTITE_VENDUE");
                                break;
                        }
                        sGainHO += result.getDouble("SOMME_VERSEE");//No problemo
                        i++;
                    }
                    pVente.setInformations(qGrain, qVrac, qConditionne);}
                //***************FIN VENTES**************
                qGrain = 0;
                qVrac = 0;
                qConditionne = 0;
                //*****************AFFICHAGE ACHATS*****
                if("1".equals(KAchat)){
                    result = state.executeQuery("select * from GESTION_ACHATS where DATE_ACHAT between '"+transcriptDateEnregistrement(pInfos.dateA.getInformations())+"' "
                            + "and '"+transcriptDateEnregistrement(pInfos.dateB.getInformations())+ "'");
                    i  = 1;
                    achats = ventes;
                    while(result.next()){
                        achats++;
                        result.absolute(i);
                        Object[] rowData = {i,translateDateEnregistrement(result.getDate("DATE_ACHAT").toString()),
                            "Achat",
                            result.getString("TYPE_PRODUIT_ACHETE"),
                            result.getDouble("QUANTITE_ACHETEE"),
                            result.getDouble("SOMME_VERSEE"),
                            result.getDouble("SOMME_VERSEE")+result.getDouble("SOMME_RESTANT"),
                            result.getInt("NUM_ACHAT")};
                        tableau.tModel.addRow(rowData);
                        switch(result.getString("TYPE_PRODUIT_ACHETE")){
                            case "Cafe vert 60Kg":
                                qVert += result.getDouble("QUANTITE_ACHETEE")*60;
                                break;
                            case "Cafe vert autre":
                                qVert += result.getDouble("QUANTITE_ACHETEE");
                                break;
                            case "Cafe torrefie (grains)":
                                qGrain += result.getDouble("QUANTITE_ACHETEE");
                                break;
                            case "Cafe torrefie (moulu) [en vrac]":
                                qVrac += result.getDouble("QUANTITE_ACHETEE");
                                break;
                            case "Cafe torrefie (moulu) [conditionne]":
                                qConditionne += result.getDouble("QUANTITE_ACHETEE");
                                break;
                            case "250g Papier Alluminium":
                                qAluminiumDispo += (result.getDouble("QUANTITE_ACHETEE")*0.25);
                                break;
                            case "1Kg Kraft":
                                qKraftDispo += result.getDouble("QUANTITE_ACHETEE");
                                break;
                        }
                        sPerteHO += result.getDouble("SOMME_VERSEE");
                        sPerteO += result.getDouble("SOMME_RESTANT");
                        i++;
                    }
                    pAchat.setInformations(qVert, qGrain, qVrac, qConditionne,qAluminiumDispo,qKraftDispo);}
                //*************************FIN ACHATS*****************
                //***************************AFFICHAGE DEPENSES*****
                if("1".equals(KDepense)){
                    result = state.executeQuery("select * from GESTION_FRAIS where DATE_FRAIS between '"+transcriptDateEnregistrement(pInfos.dateA.getInformations())+"' "
                            + "and '"+transcriptDateEnregistrement(pInfos.dateB.getInformations())+ "'");
                    i = 1;
                    while(result.next()){
                        depenses++;
                        result.absolute(i);
                        Object[] rowData = {i,translateDateEnregistrement(result.getDate("DATE_FRAIS").toString()),"Depense",
                            result.getString("TYPE_FRAIS"),"R.A.S","R.A.S",result.getDouble("VALEUR_FRAIS"),result.getDouble("VALEUR_FRAIS")
                                ,result.getInt("NUM_FRAIS")};
                        tableau.tModel.addRow(rowData);
                        sPerteHO += result.getDouble("VALEUR_FRAIS");
                        i++;}
                }//*******************FIN des DEPENSES************
                
                //***********************AFFICHAGE DETTES*******
                result = state.executeQuery("select * from VERSEMENTS_DETTES where DATE_VERSEMENT between '"+transcriptDateEnregistrement(pInfos.dateA.getInformations())+"' "
                        + "and '"+transcriptDateEnregistrement(pInfos.dateB.getInformations())+ "'");
                i = 1;
                while(result.next()){
                    result.absolute(i);
                    sGainO += result.getDouble("VERSEMENT_HT");
                    i++;
                }
                sPerteHO += sPerteO;
                //**********FIN DETTES***************
                result.close();
            }
        }
        catch(NullPointerException | ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
        this.pAffaire.setInformation(sGainHO,sGainO,sPerteHO);
        
    }
 public void SearchingCoffeeTransactions(){
        double qVertTorrefie = 0;
        double qGrainMoulu = 0;
        double qConditionne = 0;
        double qAluminium = 0;
        double qKraft = 0;
        try{
            try (Statement state = IOTransactions.getConnectionUtility().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE)) {
                ResultSet result = null;
                String query = "";
                for(int i = 0 ; i < 5 ; i++){
                    switch(i){
                        case 0:
                            query = ("select * from GESTION_TORREFACTION where NOM_CAFE = 'Cafe vert' and DATE_OPERATION between '"+transcriptDateEnregistrement(pInfos.dateA.getInformations())
                                    + "' and '"+transcriptDateEnregistrement(pInfos.dateB.getInformations())+ "'");
                            break;
                        case 1:
                            query = ("select * from GESTION_TORREFACTION where NOM_CAFE = 'Cafe torrefie (grains)' and DATE_OPERATION between '"+transcriptDateEnregistrement(pInfos.dateA.getInformations())
                                    + "' and '"+transcriptDateEnregistrement(pInfos.dateB.getInformations())+ "'");
                            break;
                        case 2:
                            query = ("select * from GESTION_TORREFACTION where NOM_CAFE = 'Cafe moulu (en vrac)' and DATE_OPERATION between '"+transcriptDateEnregistrement(pInfos.dateA.getInformations())
                                    + "' and '"+transcriptDateEnregistrement(pInfos.dateB.getInformations())+ "'");
                            break;
                        case 3:
                            query = ("select * from GESTION_TORREFACTION where NOM_CAFE = 'Aluminium' and DATE_OPERATION between '"+transcriptDateEnregistrement(pInfos.dateA.getInformations())
                                    + "' and '"+transcriptDateEnregistrement(pInfos.dateB.getInformations())+ "'");
                            break;
                        case 4:
                            query = ("select * from GESTION_TORREFACTION where NOM_CAFE = 'Kraft' and DATE_OPERATION between '"+transcriptDateEnregistrement(pInfos.dateA.getInformations())
                                    + "' and '"+transcriptDateEnregistrement(pInfos.dateB.getInformations())+ "'");
                            break;
                    }
                    result = state.executeQuery(query);
                    int indice = 1;
                    while(result.next()){
                        result.absolute(indice);
                        if(i == 0)qVertTorrefie += result.getDouble("QUANTITE_PERDUE");
                        else if(i == 1)qGrainMoulu += result.getDouble("QUANTITE_PERDUE");
                        else if(i == 2)qConditionne += result.getDouble("QUANTITE_PERDUE");
                        else if(i == 3)qAluminium += (result.getDouble("QUANTITE_PERDUE"));
                        else if(i == 4) qKraft += result.getDouble("QUANTITE_PERDUE");
                        indice++;
                    }
                }
                result.close();
                state.close();
            }
        }
        catch(NullPointerException | ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
        pTorrefaction.setInformations(qVertTorrefie, qGrainMoulu, qConditionne,qAluminium,qKraft);
        
    }
public String transcriptDateEnregistrement(String str){
        String date = "";
        for(int i = 0 ; i < str.length() ; i++){
            if(str.charAt(i) == '-')date = date + '.';
            else date = date + str.charAt(i);
        }
        return date;
    }
public class MyRenderer extends DefaultTableCellRenderer{ 
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){ 
            Component c = super.getTableCellRendererComponent(table, value, false, hasFocus, row, column); 
            if(row >= 0 && row < ventes)c.setBackground(new Color(128, 255, 128));
            else if(row < achats)c.setBackground(new Color(255,128,128));
            else c.setBackground(new Color(128,128,255));
            return c; 
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
    
}
