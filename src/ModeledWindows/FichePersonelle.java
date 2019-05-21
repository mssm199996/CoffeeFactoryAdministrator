/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeledWindows;

import MainPackage.MainWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import com.toedter.calendar.JDateChooser;

/**
 *
 * @author Sidi Mohamed
 */
public abstract class FichePersonelle extends JFrame {
    protected String title ;
    protected JCheckBox masculin,feminin;
    public JButton confirmer ;
    protected JButton annuler ;
    public Formulaire[] formulaires;
    public FormulaireDate formulaireNaissance;
    public JComboBox situationSociale;
    public JComboBox typeActivite;
    public JComboBox niveauConfiance ;
    protected Color couleurFond;
    protected Color couleurEcriture;
    protected Dimension dim ;
    public FichePersonelle(Color c1,Color c2,Dimension dim){
        this.couleurFond = c1;
        this.couleurEcriture = c2;
        this.initVariables();
        this.initComponent();
        this.initListeners();
        this.initWindow(dim);
    }
    public void initComponent(){
        String[] tabSociale = {"Celibataire","Marie(e)","Divorce(e)"};
        String[] tabTypeActiviteClient = {"Boutique","Magasin" ,"Mouturier","Super marché","Centre commercial","Cafeteria"};
        String[] tabTypeActiviteFournisseur = {"Grossiste","Importateur"};
        String[] tabConfianceClient = {"S","A","B","C","D"};
        String[] tabConfianceFournisseur = {"Cafe","Emballage"};
        this.situationSociale = new ChampBox(tabSociale);
        if(this.getClass().getSimpleName().equals("FicheClient")){
            this.typeActivite = new ChampBox(tabTypeActiviteClient);
            this.niveauConfiance = new ChampBox(tabConfianceClient);
        }
        else {
            this.typeActivite = new ChampBox(tabTypeActiviteFournisseur);
            this.niveauConfiance = new ChampBox(tabConfianceFournisseur);
        }
        this.masculin = new BouttonChoix("Masculin",true);
        this.feminin = new BouttonChoix("Feminin");
        this.confirmer = new Boutton("Confirmer");
        this.annuler = new Boutton("Annuler");
        this.formulaires = new Formulaire[18];
        this.situationSociale.setPreferredSize(new Dimension(180,30));
        this.typeActivite.setPreferredSize(new Dimension(180,30));
        this.niveauConfiance.setPreferredSize(new Dimension(180,30));
//--------------------------------- Conteneurs ---------------------------------
        ButtonGroup groupe = new ButtonGroup();
        groupe.add(this.masculin);
        groupe.add(this.feminin);
        
        JPanel panneau = new JPanel();
        JPanel pSexe = new JPanel();
        panneau.setBorder(MainWindow.bordure);
        panneau.setBackground(this.couleurFond);
        pSexe.setBackground(couleurFond);
        pSexe.setPreferredSize(new Dimension(180,25));        
        JPanel pButton = new JPanel();
        pButton.setBackground(this.couleurFond);
        
        pSexe.add(this.masculin);
        pSexe.add(this.feminin);
        pButton.add(this.confirmer);
        pButton.add(this.annuler);
        String caption = null;
        for(int a = 0; a < 18 ; a++){
            switch(a){
                case 0:
                    caption = "Nom*:";
                    break;
                case 1:
                    caption = "Prénom*:";
                    break;
                case 2:
                    this.formulaireNaissance = new FormulaireDate();
                    panneau.add(new Formulaire("Date de naissance:",this.formulaireNaissance));
                    break;
                case 3:
                    panneau.add(new Formulaire("Type d'activité:",this.typeActivite));
                    break;
                case 4:
                    panneau.add(new Formulaire(this.getClass().getSimpleName().equals("FicheClient")? "Niveau de confiance:" : "Fournisseur de:",this.niveauConfiance));
                    break;
                case 5:
                    panneau.add(new Formulaire("Sexe:",pSexe));
                    break;
                case 6:
                    panneau.add(new Formulaire("Situation sociale :",this.situationSociale));
                    break;
                case 7:
                    if(this.getClass().getSimpleName().equals("FicheClient"))caption = "Numero Fiscal:";
                    break;
                case 8: if(this.getClass().getSimpleName().equals("FicheClient"))caption = "Registre commerce:";
                    break;
                case 9:
                    caption = "Adresse:";
                    break;
                case 10:
                    caption = "Code postal:";
                    break;
                case 11:
                    caption = "Pays";
                    break;
                case 12:
                    caption = "Wilaya";
                    break;
                case 13:
                    caption = "Telephone bureau";
                    break;
                case 14:
                    caption = "Telephone portable";
                    break;
                case 15:
                    caption = "E-mail";
                    break;
                case 16:
                    caption = "Fax";
                    break;
                case 17:
                    caption = "Société";
                    break;
            }
            if(a < 2 ){
                this.formulaires[a] = new Formulaire(caption);
                panneau.add(this.formulaires[a]);
            }
            else if(a == 7 || a == 8){
                if(this.getClass().getSimpleName().equals("FicheClient")){
                    this.formulaires[a] = new Formulaire(caption);
                    panneau.add(this.formulaires[a]);
                }
            }
            else if(a > 8){
                this.formulaires[a] = new Formulaire(caption);
                panneau.add(this.formulaires[a]);
            }
        }
//------------------------------------------------------------------------------
        panneau.add(pButton,BorderLayout.SOUTH);
        this.setContentPane(panneau);
    }
    private void initListeners(){
        annuler.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setVisible(false);
            }
        });
    }
    private void initVariables(){
        if(this.getClass().getSimpleName().equals("FicheClient"))title = "Nouveau client";
        else title = "Nouveau fournisseur";
    }
    private void initWindow(Dimension dim){
        this.setTitle(title);
        this.setSize(dim);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setResizable(false);
    }
    class Label extends JLabel{
        private String text;
        public Label(String str){
            this.text = str;
            this.setPreferredSize(new Dimension(140,20));
        }
        public Label(String str,Dimension dim){
            this.text = str;
            this.setPreferredSize(dim);
        }
        public void paintComponent(Graphics g){
            g.setColor(couleurFond);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            FontMetrics fm = g.getFontMetrics();
            int largeur = fm.getHeight();
            g.setColor(couleurEcriture);
            g.drawString(text, 0, this.getHeight()/2+largeur/4);
        }
    }
    class Boutton extends JButton{
        public Boutton(String str){
            super(str);
            this.setPreferredSize(new Dimension(150,30));
        }
    }
    class BouttonChoix extends JCheckBox{
        public BouttonChoix(String str,boolean boo){
            super(str,boo);
            this.setBackground(couleurFond);
            this.setForeground(couleurEcriture);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, couleurFond, couleurFond));
        }
        public BouttonChoix(String str){
            super(str);
            this.setBackground(couleurFond);
            this.setForeground(couleurEcriture);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, couleurFond, couleurFond));
        }
    }
    class ChampText extends JTextField{
        public ChampText(){
            this.setPreferredSize(new Dimension(180,20));
            this.setHorizontalAlignment(JTextField.HORIZONTAL);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
    }
    class ChampBox extends JComboBox{
        public ChampBox(String[] tabString){
            super(tabString);
            this.setPreferredSize(new Dimension(45,20));
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
    }
    public class Formulaire extends JPanel{
        private JLabel label;
        private JTextField champText;
        public Formulaire(String str){
            this.setBackground(couleurFond);
            this.initComponent(str);
        }
        public Formulaire(String str,Component comp){
            this.setBackground(couleurFond);
            this.initComponentManually(str,comp);
        }
        private void initComponent(String str){
            this.label = new Label(str);
            this.champText = new ChampText();
            this.add(this.label);
            this.add(this.champText);
        }
        private void initComponentManually(String str,Component comp){
            this.label = new Label(str);
            this.add(this.label);
            this.add(comp);
        }
        public String getInformation(){
            return this.champText.getText();
        }
        public void clearFormulaire(){
            this.champText.setText("");
        }
    }
    public class FormulaireDate extends JDateChooser{
        public FormulaireDate(){
            this.setBackground(couleurFond);
            this.setPreferredSize(new Dimension(180,30));
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
        public String getInformations(){
            return (this.jcalendar.getCalendar().get(5) < 10? "0"+Integer.toString(this.jcalendar.getCalendar().get(5)) : Integer.toString(this.jcalendar.getCalendar().get(5)))
                    +(this.jcalendar.getCalendar().get(2)+1 < 10? "-0"+Integer.toString(this.jcalendar.getCalendar().get(2)+1) : "-"+Integer.toString(this.jcalendar.getCalendar().get(2)+1))
                    +"-"+Integer.toString(this.jcalendar.getCalendar().get(1));
        }
    }
    public String getSexeInformation(){
        if(this.masculin.isSelected())return "Masculin";
        else return "Feminin";
    }
}
