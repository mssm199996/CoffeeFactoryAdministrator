/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeledWindows;

import MainPackage.IOTransactions;
import MainPackage.MainWindow;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
/**
 *
 * @author lennovo
 */
public class PassWindow extends JFrame{
    private final Color c1 = Color.white;
    private final Color c2 = Color.LIGHT_GRAY;
    private IDPWChanger fc = new IDPWChanger();
    private MainWindow fenetrePrincipale ;
    private JTextField champUser;
    private JPasswordField ChampPass;
    private JButton Confirmer,Annuler;
    private JCheckBox souvenir ;
    public PassWindow(){
        this.initComponent();
        this.initListeners();
        this.initWindow();
        this.addWindowListener(new WindowAdapter(){ 
            public void windowActivated( WindowEvent e){
                checkConfidentiality();
            } 
        });
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
        this.setVisible(true);
    }
    private void checkConfidentiality(){
        try{
            Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = state.executeQuery("select NUM_CONST,VALEUR_CONST,VALEUR_CONST_S from CONSTANTES");
            result.absolute(9);
            if(result.getDouble("VALEUR_CONST") == 1.00){
                this.champUser.setText(result.getString("VALEUR_CONST_S"));
                this.souvenir.setSelected(true);
                this.ChampPass.requestFocus();
            }
            else {
                this.champUser.setText("");
                this.ChampPass.setText("");
                this.champUser.requestFocus();
            }
            result.close();
            state.close();
        }
        catch(NullPointerException | ClassNotFoundException | SQLException e){e.printStackTrace();}
    }
    private void initWindow(){
        this.setTitle("Gestionaire de torréfacton (Pass step)");
        this.setSize(400,175);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void initListeners(){
        this.champUser.addKeyListener(new testWithKey());
        this.ChampPass.addKeyListener(new testWithKey());
        this.Annuler.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.Confirmer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                testInformations();
            }
        });
    }
    private void initComponent(){
        this.fenetrePrincipale = new MainWindow();
        JPanel contentPane = new Panneau(MainWindow.bordure,true);
        this.fc = new IDPWChanger();
        this.champUser = new ChampText("");
        this.ChampPass = new ChampPassword("");
        this.souvenir = new JCheckBox("Se souvenir du nom d'utilisateur");
        this.souvenir.setFont(new Font("Arial",Font.BOLD,14));
        this.souvenir.setForeground(Color.black);
        this.Confirmer = new Boutton("Confirmer");
        this.Annuler = new Boutton("Annuler");
        JButton IDChanger = new JButton(new ImageIcon(getClass().getResource("/Images/modify.png")));
        IDChanger.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                fc.setLabelsInformations(0);
                fc.setVisible(true);
            }
        });
        JButton PassChanger = new JButton(new ImageIcon(getClass().getResource("/Images/modify.png")));
        PassChanger.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                fc.setLabelsInformations(1);
                fc.setVisible(true);
            }
        });
        contentPane.add(new Formulaire("Nom d'utilisateur:",this.champUser,IDChanger));
        contentPane.add(new Formulaire("Mot de passe:",this.ChampPass,PassChanger));
        contentPane.add(this.souvenir);
        contentPane.add(new Formulaire(this.Confirmer,this.Annuler));
        this.setContentPane(contentPane);
    }
    class Boutton extends JButton{
        public Boutton(String str){
            super(str);
            this.setPreferredSize(new Dimension(180,30));
        }
    }
    class Formulaire extends Panneau{
        private Label label ;
        public Formulaire(String str,Component comp,JButton b){
            super(BorderFactory.createEmptyBorder(),false);
            this.initComponent(str,comp);
            this.add(b);
        }
        public Formulaire(Component comp1,Component comp2){
            super(BorderFactory.createEmptyBorder(),false);
            this.setOpaque(false);
            this.setBackground(new Color(255,255,255,0));
            this.setLayout(new FlowLayout());
            this.add(comp1);
            this.add(comp2);
        }
        private void initComponent(String str,Component comp){
            this.label = new Label(str);
            this.setLayout(new FlowLayout());
            this.add(this.label);
            this.add(comp);
        }
    }
    class Panneau extends JPanel{
        private boolean drawDegrading ;
        public Panneau(Border border,Boolean boo){
            this.drawDegrading = boo;
            this.setBorder(border);
            this.setBackground(Color.white);
        }
        public void paintComponent(Graphics g){
            if(drawDegrading){
                Graphics2D g2 = (Graphics2D)g;
                g2.setPaint(new GradientPaint(0,0,c1,20,20,c2,true));
                g2.fillRect(0,0,this.getWidth(),this.getHeight());
            }
        }
    }
    class Label extends JLabel{
        private String text;
        public Label(String str){
            this.text = str;
            this.setPreferredSize(new Dimension(150,20));
            this.setOpaque(false);
            this.setBackground(new Color(255,255,255,0));
        }
        public void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D)g;
            g2.setFont(new Font("Arial",Font.BOLD,16));
            FontMetrics fm = g.getFontMetrics();
            int longueur = fm.stringWidth(text);
            int largeur = fm.getHeight();
            g2.setColor(Color.black);
            g2.drawString(text, this.getWidth()/2-longueur/2, this.getHeight()/2+largeur/4);
        }
        public Label(String str,Dimension dim){
            this.text = str;
            this.setPreferredSize(dim);
        }
    }
    class ChampText extends JTextField{
        public ChampText(String str){
            super(str);
            this.setPreferredSize(new Dimension(150,20));
            this.setHorizontalAlignment(JTextField.HORIZONTAL);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
    }
    class ChampPassword extends JPasswordField{
        public ChampPassword(String str){
            super(str);
            this.setPreferredSize(new Dimension(150,20));
            this.setHorizontalAlignment(JTextField.HORIZONTAL);
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green));
        }
    }
    private void testInformations(){
        String ID = new String();
        String PW = new String();
        try{
            Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = state.executeQuery("select * from CONSTANTES");
            result.absolute(9);
            ID = result.getString("VALEUR_CONST_S");
            if(this.souvenir.isSelected())result.updateDouble("VALEUR_CONST", 1.00);
            else result.updateDouble("VALEUR_CONST", 0.00);
            result.updateRow();
            result.absolute(10);
            PW = result.getString("VALEUR_CONST_S");
            result.close();
            state.close();
        }
        catch(NullPointerException | ClassNotFoundException | SQLException e){e.printStackTrace();}
        if(champUser.getText().equals(ID) && ChampPass.getText().equals(PW)){
           setVisible(false);
           MainWindow.launchingFrame.setVisible(false);
           fenetrePrincipale.setVisible(true);
           fenetrePrincipale.setResizable(false);
           ChampPass.setText("");
        }
        else {
           JOptionPane optionPane = new JOptionPane();
           optionPane.showMessageDialog(null, "Nom d'utilisateur ou mot de passe incorrect", "Error...",JOptionPane.ERROR_MESSAGE);
           ChampPass.setText("");
        }
    }
    class testWithKey implements KeyListener{
        public void keyTyped(KeyEvent e) {
            if((int)e.getKeyChar() == 10)testInformations();
        }
        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }
    }
}
