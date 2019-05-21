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
public class ClientInformationsChanger extends JFrame{
    Formulaire ID;
    Formulaire nomClient;
    Formulaire numeroFiscal;
    Formulaire registreCommerce;
    JButton confirmer;
    JButton annuler;
    public ClientInformationsChanger(){
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } 
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        this.initWindow();
        this.initComponents();
        this.initListeners();
    }
    private void initWindow(){
        this.setTitle("NF/RC Changer");
        this.setSize(520,240);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setResizable(false);
    }
    private void initListeners(){
        this.annuler.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
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
        this.ID = new Formulaire("Numero du client:",new Dimension(180,20),false);
        this.nomClient = new Formulaire("Numero du client:",new Dimension(180,20),false);
        this.numeroFiscal = new Formulaire("Numero fiscal:",new Dimension(180,20),true);
        this.registreCommerce = new Formulaire("Registre de commerce:",new Dimension(180,20),true);

        
        this.confirmer = new JButton("Confirmer");
        this.confirmer.setPreferredSize(new Dimension(230,30));
        this.confirmer.setFont(new Font("Arial",Font.BOLD,14));
        this.annuler = new JButton("Annuler");
        this.annuler.setPreferredSize(new Dimension(230,30));
        this.annuler.setFont(new Font("Arial",Font.BOLD,14));

        Panneau pBoutton = new Panneau();
        pBoutton.add(confirmer);
        pBoutton.add(annuler);
        
        Panneau contentPane = new Panneau();
        contentPane.add(this.ID);
        contentPane.add(this.nomClient);
        contentPane.add(this.numeroFiscal);
        contentPane.add(this.registreCommerce);
        contentPane.add(pBoutton);
        
        this.setContentPane(contentPane);
    }
    public void setInformations(int numeroClient,String nom,String numeroFiscal,String rc){
        this.ID.setInformation(Integer.toString(numeroClient));
        this.nomClient.setInformation(nom);
        this.numeroFiscal.setInformation(numeroFiscal);
        this.registreCommerce.setInformation(rc);
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
            ResultSet result = state.executeQuery("select NUM_CLIENT,NUMERO_FISCAL_CLIENT,RC from CLIENT where NUM_CLIENT = "
                    +Integer.parseInt(this.ID.getInformation()));
            result.first();
            for(int i = 1 ; i < 3 ; i++){
                String column = "";
                String value = "";
                switch(i){
                    case 1:
                        column = "NUMERO_FISCAL_CLIENT";
                        value = this.numeroFiscal.getInformation();
                        break;
                    case 2:
                        column = "RC";
                        value = this.registreCommerce.getInformation();
                        break;
                }
                result.updateString(column, value);
            }
            result.updateRow();
            result.close();
            state.close();
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
