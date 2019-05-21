/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeledWindows;

import MainPackage.IOTransactions;
import MainPackage.MainWindow;
import static MainPackage.MainWindow.MOYENNE_BASSE;
import static MainPackage.MainWindow.MOYENNE_ELEVEE;
import static MainPackage.MainWindow.MOYENNE_STABLE;
import static MainPackage.MainWindow.TAUX_TIMBRE;
import static MainPackage.MainWindow.TAUX_TVA;
import static MainPackage.MainWindow.cc;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author lennovo
 */
public class ConstantesChanger extends JFrame{
    Formulaire TVA;
    Formulaire Timbre;
    Formulaire PromoVrac;
    Formulaire PromoCondi;
    Formulaire balanceMinimale;
    Formulaire balanceMoyenne;
    Formulaire balanceMaximale;
    JButton confirmer;
    JButton annuler;
    public ConstantesChanger(){
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } 
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        this.initWindow();
        this.initComponents();
        this.initListeners();
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
    }
    private void initWindow(){
        this.setTitle("Constantes Changer");
        this.setSize(600,340);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setResizable(false);
    }
    private void initListeners(){
        this.annuler.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               cc.setInformations(MainWindow.TAUX_TVA, MainWindow.TAUX_TIMBRE, MainWindow.MOYENNE_BASSE, MainWindow.MOYENNE_STABLE, MainWindow.MOYENNE_ELEVEE,MainWindow.PROMO_VRAC,MainWindow.PROMO_CONDI);
               setVisible(false);
           } 
        });
        this.confirmer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                majConstantes();
            }
        });
    }
    private void initComponents(){        
        this.TVA = new Formulaire("Pourcentage TVA:",new Dimension(260,20),true);
        this.Timbre = new Formulaire("Taux timbre:",new Dimension(260,20),true);
        this.PromoVrac = new Formulaire("Requis pour promotion Vrac:",new Dimension(260,20),true);
        this.PromoCondi = new Formulaire("Requis pour promotion Conditionné:",new Dimension(260,20),true);
        this.balanceMinimale = new Formulaire("Rouge à partir de:",new Dimension(260,20),true);
        this.balanceMoyenne = new Formulaire("Vert à partir de:",new Dimension(260,20),true);
        this.balanceMaximale = new Formulaire("Bleu à partir de:",new Dimension(260,20),true);
        
        this.confirmer = new JButton("Confirmer");
        this.confirmer.setPreferredSize(new Dimension(270,30));
        this.confirmer.setFont(new Font("Arial",Font.BOLD,14));
        this.annuler = new JButton("Annuler");
        this.annuler.setPreferredSize(new Dimension(270,30));
        this.annuler.setFont(new Font("Arial",Font.BOLD,14));

        Panneau pBoutton = new Panneau();
        pBoutton.add(confirmer);
        pBoutton.add(annuler);
        
        Panneau contentPane = new Panneau();
        contentPane.add(this.TVA);
        contentPane.add(this.Timbre);
        contentPane.add(this.PromoVrac);
        contentPane.add(this.PromoCondi);
        contentPane.add(this.balanceMinimale);
        contentPane.add(this.balanceMoyenne);
        contentPane.add(this.balanceMaximale);
        contentPane.add(pBoutton);
        
        this.setContentPane(contentPane);
    }
    public void setInformations(double tva,double timbre,double balanceMinimale,double balanceMoyenne,double balanceMaximale,int PromoVrac,int PromoCondi){
        this.TVA.setInformation(Double.toString(tva));
        this.Timbre.setInformation(Double.toString(timbre));
        this.balanceMinimale.setInformation(Double.toString(balanceMinimale));
        this.balanceMoyenne.setInformation(Double.toString(balanceMoyenne));
        this.balanceMaximale.setInformation(Double.toString(balanceMaximale));
        this.PromoVrac.setInformation(Integer.toString(PromoVrac));
        this.PromoCondi.setInformation(Integer.toString(PromoCondi));
    }
    class ChampText extends JTextField{
        public ChampText(){
            this.setText("?");
            this.setPreferredSize(new Dimension(300,20));
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED,Color.blue,Color.red));
            this.setHorizontalAlignment(JTextField.HORIZONTAL);
        }
    }
    class Formulaire extends JPanel{
        private JLabel label;
        private JTextField champText;
        public Formulaire(String str,Dimension dim,boolean b){
            this.setBackground(Color.white);
            this.initComponent(str);
            this.label.setPreferredSize(dim);
            this.champText.setEditable(b);
            this.setOpaque(false);
            this.setBackground(new Color(255,255,255,0));
        }
        public Formulaire(Component comp,Component comp2){
            this.setBackground(Color.white);
            this.setOpaque(false);
            this.setBackground(new Color(255,255,255,0));
            this.add(comp);
            this.add(comp2);
        }
        private void initComponent(String str){
            this.label = new Label(str,false);
            this.champText = new ChampText();
            this.add(this.label);
            this.add(champText);
        }
        public String getInformation(){
            return this.champText.getText();
        }
        public void setInformation(String str){
            this.champText.setText(str);
        }
        public void clearFormulaire(){
            this.champText.setText("?");
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
            else {
                this.setOpaque(false);
                this.setBackground(new Color(255,255,255,0));
            }
        }
        public void paintComponent(Graphics g){
            if(centering){
                g.setColor(Color.white);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            g.setColor(Color.black);
            FontMetrics fm = g.getFontMetrics();
            int largeur = fm.getHeight();
            if(centering){
                int longueur = fm.stringWidth(text);
                g.drawString(text, this.getWidth()/2-longueur/2, this.getHeight()/2+largeur/4);
            }
            else {
                g.setFont(new Font("Arial",Font.BOLD,14));
                g.drawString(text, 0, this.getHeight()/2+largeur/4);
            }
        }
        public void setText(String str){
            this.text = str;
            this.repaint();
        }
        public String getText(){
            return this.text;
        }
    }
    class Panneau extends JPanel{
        public Panneau(){
            this.setBorder(MainWindow.bordure);
            this.setOpaque(false);
            this.setBackground(new Color(255,255,255,0));
        }
        public void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D)g;
            g2.setPaint(new GradientPaint(0,0,Color.white,10,10,Color.LIGHT_GRAY,true));
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }
    private void majConstantes(){
        try{
            Statement state = IOTransactions.getConnectionUtility().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = state.executeQuery("select NUM_CONST,VALEUR_CONST from CONSTANTES");
            for(int i = 1 ; i < 8 ; i++){
                double value = 0;
                switch(i){
                    case 1:
                        value = Double.parseDouble(this.TVA.getInformation());
                        MainWindow.TAUX_TVA = value;
                        break;
                    case 2:
                        value = Double.parseDouble(this.Timbre.getInformation());
                        MainWindow.TAUX_TIMBRE = value;
                        break;
                    case 3:
                        value = Double.parseDouble(this.balanceMinimale.getInformation());
                        MainWindow.MOYENNE_BASSE = value;
                        break;
                    case 4:
                        value = Double.parseDouble(this.balanceMoyenne.getInformation());
                        MainWindow.MOYENNE_STABLE = value;
                        break;
                    case 5:
                        value = Double.parseDouble(this.balanceMaximale.getInformation());
                        MainWindow.MOYENNE_ELEVEE = value;
                        break;
                    case 6:
                        value = Double.parseDouble(this.PromoVrac.getInformation());
                        MainWindow.PROMO_VRAC = (int)value;
                        break;
                    case 7:
                        value = Double.parseDouble(this.PromoCondi.getInformation());
                        MainWindow.PROMO_CONDI = (int)value;
                        break;
                }
                result.absolute(i);
                result.updateDouble("VALEUR_CONST", value);
                result.updateRow();
            }
            result.close();
            state.close();
            cc.setInformations(MainWindow.TAUX_TVA, MainWindow.TAUX_TIMBRE, MainWindow.MOYENNE_BASSE, MainWindow.MOYENNE_STABLE, MainWindow.MOYENNE_ELEVEE,MainWindow.PROMO_VRAC,MainWindow.PROMO_CONDI);
            setVisible(false);
        }
        catch(NullPointerException | NumberFormatException | ClassNotFoundException | SQLException exp){
            JOptionPane option = new JOptionPane();
            option.showMessageDialog(null, "Veuillez vérifier que tout les informations sont biens entrées !"
                    , "Error...", JOptionPane.ERROR_MESSAGE);
            exp.printStackTrace();
        }
    }
}
