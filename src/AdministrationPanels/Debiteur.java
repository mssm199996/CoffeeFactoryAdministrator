/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdministrationPanels;

import MainPackage.IOTransactions;
import MainPackage.MPanelPrinter;
import MainPackage.MainWindow;
import MainPackage.PanneauApresImpression;
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
import java.awt.print.PageFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
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
public class Debiteur extends JFrame{
    Formulaire ID;
    Formulaire Nom;
    Formulaire Balance;
    Formulaire resteAPayer;
    Formulaire nEntree;
    JButton confirmer;
    JButton annuler;
    public Debiteur(){
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
        this.setTitle("Debiteur");
        this.setSize(520,270);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setResizable(false);
    }
    private void initListeners(){
        this.annuler.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               nEntree.champText.setText("0.0");
               setVisible(false);
           } 
        });
        this.confirmer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                majBalance();
            }
        });
    }
    private void initComponents(){        
        this.ID = new Formulaire("Numero du client:",new Dimension(180,20),false);
        this.Nom = new Formulaire("Nom et prénom du client:",new Dimension(180,20),false);
        this.Balance = new Formulaire("Balance du client:",new Dimension(180,20),false);
        this.resteAPayer = new Formulaire("Dette HT:",new Dimension(180,20),false);
        this.nEntree = new Formulaire("Nouvelle entrée:",new Dimension(180,20),true);
        
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
        contentPane.add(this.Nom);
        contentPane.add(this.resteAPayer);
        contentPane.add(this.Balance);
        contentPane.add(this.nEntree);
        contentPane.add(pBoutton);
        
        this.setContentPane(contentPane);
    }
    public void setInformations(int id,String name,double resteAPayer,double balance){
        this.ID.setInformation(Integer.toString(id));
        this.Nom.setInformation(name);
        this.resteAPayer.setInformation(Double.toString(resteAPayer));
        this.Balance.setInformation(Double.toString(balance));
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
    private void majBalance(){
        if(!(Double.parseDouble(this.nEntree.getInformation()) > Double.parseDouble(this.Balance.getInformation()))){
            try{
            Statement state = IOTransactions.getConnectionUtility().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = state.executeQuery("select * from CLIENT where NUM_CLIENT = " + 
                    Integer.parseInt(ID.getInformation()));
            result.first();
            String name = result.getString("NOM_CLIENT")
                        +"  "+result.getString("PRENOM_CLIENT");
            double versementHT = 0;
            double balanceAncienne = result.getDouble("BALANCE_PAIEMENT");
            result.updateDouble("BALANCE_PAIEMENT", result.getDouble("BALANCE_PAIEMENT")-Double.parseDouble(nEntree.getInformation()));
            if(result.getDouble("RESTE_A_PAYER_HT")-Double.parseDouble(nEntree.getInformation()) > 0){
                result.updateDouble("RESTE_A_PAYER_HT",
                        result.getDouble("RESTE_A_PAYER_HT")-Double.parseDouble(nEntree.getInformation()));
                versementHT = Double.parseDouble(nEntree.getInformation());
            }
            else {
                versementHT = result.getDouble("RESTE_A_PAYER_HT");
                result.updateDouble("RESTE_A_PAYER_HT",0);
            }
            result.updateRow();
            // Inscription dans VERSEMENT_DETTES
            int numVersement = 1;
            result = state.executeQuery("select * from VERSEMENTS_DETTES");
            result.last();
            try{
                numVersement = result.getInt("NUM_VERSEMENT") + 1;
            }
            catch(NullPointerException exp){
                numVersement = 1;
                exp.printStackTrace();
            }
            state.executeUpdate("insert into VERSEMENTS_DETTES ("
                    + "NUM_VERSEMENT,"
                    + "DATE_VERSEMENT,"
                    + "TEMPS_VERSEMENT,"
                    + "VALEUR_VERSEMENT,"
                    + "VERSEMENT_HT,"
                    + "NUM_CLIENT) values "
                    + "("
                    + numVersement+ ","
                    + "CURRENT_DATE,"
                    + "CURRENT_TIME,"
                    + Double.parseDouble(nEntree.getInformation())+ ","
                    + versementHT + ","
                    + Integer.parseInt(ID.getInformation())
                    + ")");
            // ---------------------------------
            JOptionPane option = new JOptionPane();
            int i = option.showConfirmDialog(null, "Voulez vous imprimer un bon ?", "*** Sebbane's Industry ***", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            JFrame f = null;
            if(i == JOptionPane.YES_OPTION){
                PanneauApresImpression pai = new PanneauApresImpression();
                Calendar cal = Calendar.getInstance();
                String date = (cal.get(Calendar.DAY_OF_MONTH) < 10? "0"+Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) : Integer.toString(cal.get(Calendar.DAY_OF_MONTH)))
                    +(cal.get(Calendar.MONTH) < 10? "-0"+Integer.toString(cal.get(Calendar.MONTH)) : "-"+Integer.toString(cal.get(Calendar.MONTH)))
                    +"-"+Integer.toString(cal.get(Calendar.YEAR));
                pai.setInformations(date, Integer.parseInt(ID.getInformation()),name, balanceAncienne,
                        Double.parseDouble(nEntree.getInformation()));
                f = new JFrame();
                f.setSize(750,290);
                f.setLocationRelativeTo(null);
                f.setContentPane(pai);
                f.setResizable(false);
                f.setTitle("Imprimerie de bon");
                f.setVisible(true);
                MPanelPrinter mpp = new MPanelPrinter(pai);
                mpp.setLRMargins(50);
                mpp.setTBMargins(50);
                mpp.setOrientation(PageFormat.PORTRAIT);
                mpp.setLRMargins(50);
                mpp.setTBMargins(50);
                mpp.setFitIntoPage(true);
                mpp.setLRMargins(50);
                mpp.setTBMargins(50);
                mpp.print();
                f.setVisible(false);
            }
            setVisible(false);
            result.close();
            state.close();
        }
        catch(NullPointerException | NumberFormatException | ClassNotFoundException | SQLException exp){
            JOptionPane option = new JOptionPane();
            option.showMessageDialog(null, "Veuillez vérifier que tout les informations sont biens entrées !"
                    , "Error...", JOptionPane.ERROR_MESSAGE);
            exp.printStackTrace();
        }
        try{
            Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = state.executeQuery("select * from CLIENT where NUM_CLIENT = "+ Integer.parseInt(ID.getInformation()));
            result.first();
            double balance = result.getDouble("BALANCE_PAIEMENT");
            if(balance >= MainWindow.MOYENNE_BASSE)result.updateInt("COULEUR", -1);
            else if(balance < MainWindow.MOYENNE_BASSE 
                    && balance >= MainWindow.MOYENNE_ELEVEE)result.updateInt("COULEUR", 0);
            else if(balance < MainWindow.MOYENNE_ELEVEE)result.updateInt("COULEUR", 1);
            result.updateRow();
            verifierClient(Integer.parseInt(ID.getInformation()),result.getString("NOM_CLIENT")
                        +"  "+result.getString("PRENOM_CLIENT"),result.getInt("COULEUR"));
        }
        catch(NullPointerException | ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
        this.nEntree.clearFormulaire();
        }
        else {
            JOptionPane p = new JOptionPane();
            p.showMessageDialog(null, "La somme introduite est supérieur à la somme maximale possible", "Error ...", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void verifierClient(int nombre,String nom,int code){
        switch(code){
            case -1 :
                EspaceBusiness.pTabbedDroite.getPersonalData().setForeground(Color.red);
                EspaceBusiness.pTabbedDroite.getPersonalName().setForeground(Color.red);
                break;
            case 0:
                EspaceBusiness.pTabbedDroite.getPersonalData().setForeground(new Color(0,150,0));
                EspaceBusiness.pTabbedDroite.getPersonalName().setForeground(new Color(0,150,0));
                break;
            case 1:
                EspaceBusiness.pTabbedDroite.getPersonalData().setForeground(Color.blue);
                EspaceBusiness.pTabbedDroite.getPersonalName().setForeground(Color.blue);
                break;
        }
        EspaceBusiness.pTabbedDroite.getPersonalData().setText(nombre < 10 ? "0"+Integer.toString(nombre) : Integer.toString(nombre));
        EspaceBusiness.pTabbedDroite.getPersonalName().setText(nom);
    }
}
