/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeledWindows;

import static AdministrationPanels.EspaceBusiness.pTabbedDroite;
import MainPackage.IOTransactions;
import MainPackage.MainWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author lennovo
 */
public class MoneyTransactions extends JFrame{
    public JButton bouttonConfirmer ;
    public JButton bouttonAnnuler;
    public Label numPersonnel;
    public Label prixTotal;
    public Label avance;
    public Label resteAnterieur;
    public JTextField nouvelleEntre;
    public Label nouveauReste;
    public MoneyTransactions(String str){
        this.initComponent(str);
        this.initListeners();
        this.initWindow();
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
    }
    private void initWindow(){
        this.setTitle("Money Transactions");
        this.setSize(600, 335);
        this.setLocationRelativeTo(null);
    }
    private void initListeners(){
        this.nouvelleEntre.addKeyListener(new newResteListener());
    }
    private void initComponent(String str){
        this.bouttonConfirmer = new JButton("Confirmer");
        this.bouttonAnnuler = new JButton("Annuler");
        this.avance = new Label("",true);
        this.numPersonnel = new Label("",true);
        this.nouvelleEntre = new ChampText();
        this.prixTotal = new Label("",true);
        this.resteAnterieur = new Label("",true);
        this.nouveauReste = new Label("",true);
        this.bouttonAnnuler.setPreferredSize(new Dimension(100,25));
        this.bouttonConfirmer.setPreferredSize(new Dimension(100,25));
        JPanel pCenter = new Panneau(MainWindow.bordure);
        JPanel pSouth = new Panneau(MainWindow.bordure);
        pCenter.add(new Formulaire("Numero du "+str+":",this.numPersonnel));
        pCenter.add(new Formulaire("Prix total:",this.prixTotal));
        pCenter.add(new Formulaire("L'avance:",this.avance));
        pCenter.add(new Formulaire("Reste antérieur:",this.resteAnterieur));
        pCenter.add(new Formulaire("Nouvelle entrée:",this.nouvelleEntre));
        pCenter.add(new Formulaire("Nouveau reste:",this.nouveauReste));
        pSouth.add(this.bouttonConfirmer);
        pSouth.add(this.bouttonAnnuler);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(pCenter,BorderLayout.CENTER);
        this.getContentPane().add(pSouth,BorderLayout.SOUTH);
    }
    public void setCaptions(String numeroPersonnel,String sommeTotal,String avance,String reste){
        this.numPersonnel.setText(numeroPersonnel);
        this.prixTotal.setText(sommeTotal);
        this.avance.setText(avance);
        this.resteAnterieur.setText(reste);
    }
    class Formulaire extends Panneau{
        JLabel label;
        JComponent comp;
        public Formulaire(String str,JComponent comp){
            super(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.red));
            this.setPreferredSize(new Dimension(500,35));
            this.initComponent(str,comp);
        }
        private void initComponent(String str,JComponent comp){
            this.label = new Label(str,false);
            this.comp = comp;
            this.add(this.label);
            this.add(this.comp);
        }
    }
    public class Label extends JLabel{
        private String text;
        private boolean centering ;
        public Label(String str,boolean b){
            this.text = str;
            this.centering = b;
            this.setPreferredSize(new Dimension(200,20));
        }
        public void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(new Color(200,250,255));
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            g2.setColor(Color.black);
            g2.setFont(new Font("Arial",Font.BOLD,14));
            FontMetrics fm = g2.getFontMetrics();
            int largeur = fm.getHeight();
            if(centering){
                int longueur = fm.stringWidth(text);
                g2.drawString(text, this.getWidth()/2-longueur/2, this.getHeight()/2+largeur/4);
            }
            else g2.drawString(text, 0, this.getHeight()/2+largeur/4);
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
        public Panneau(Border b){
            this.setBackground(new Color(200,250,255));
            this.setBorder(b);
        }
    }
    class ChampText extends JTextField{
        public ChampText(){
            this.setPreferredSize(new Dimension(200,20));
            this.setHorizontalAlignment(JTextField.HORIZONTAL);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
    }
    class newResteListener implements KeyListener{
        public void keyTyped(KeyEvent e) {
        }
        public void keyPressed(KeyEvent e) {
        }
        public void keyReleased(KeyEvent e) {
            if(nouvelleEntre.getText().equals(""))nouveauReste.setText("");
            else nouveauReste.setText(Float.toString(Float.parseFloat(resteAnterieur.getText())-Float.parseFloat(nouvelleEntre.getText())));
        }
    }
}
