package MainPackage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sidi Mohamed
 */
import AdministrationPanels.Debiteur;
import AdministrationPanels.EspaceBusiness;
import AdministrationPanels.EspaceClientele;
import AdministrationPanels.EspaceFacturation;
import AdministrationPanels.EspaceFournisseur;
import AdministrationPanels.EspaceStatistique;
import AdministrationPanels.EspaceTorrefaction;
import ModeledWindows.ClientInformationsChanger;
import ModeledWindows.ConstantesChanger;
import ModeledWindows.IntroductionWindow;
import ModeledWindows.PassWindow;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class MainWindow extends JFrame{
    public static PassWindow fenetrePasse;
    public static IntroductionWindow launchingFrame;
    public static double TAUX_TVA = 0;
    public static double TAUX_TIMBRE = 0;
    public static double MOYENNE_STABLE = 0;
    public static double MOYENNE_BASSE = 0;
    public static double MOYENNE_ELEVEE = 0;
    public static int NUM_FACTURE = 1;
    public static int PROMO_VRAC = 60;
    public static int PROMO_CONDI = 20;
    public static int maxWidth;
    public static int maxHeight;
    private static Timer saver;
    public final static Border bordure = BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.red, 
                        Color.blue,Color.yellow,Color.green),
                BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.red,
                        Color.blue,Color.yellow,Color.green));
    private JTabbedPane panneau = new JTabbedPane();
    public static EspaceClientele ec ;
    private EspaceFournisseur efo ;
    public EspaceBusiness eb ;
    public static EspaceStatistique es ;
    public static EspaceTorrefaction et ;
    public static EspaceFacturation efa ;
    public static Debiteur deb ;
    public static ConstantesChanger cc ;
    public static ClientInformationsChanger cic ;
    
    public MainWindow(){
        this.initWindow();
        this.initOutVariables();
        this.initComponent();
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
        MainWindow.sauvegarderBase();
    }
    private void initOutVariables(){
        try{
            Statement state = IOTransactions.getConnectionUtility().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = state.executeQuery("select * from CONSTANTES");
            result.absolute(1);
            TAUX_TVA = result.getDouble("VALEUR_CONST");
            result.absolute(2);
            TAUX_TIMBRE = result.getDouble("VALEUR_CONST");
            result.absolute(3);
            MOYENNE_BASSE = result.getDouble("VALEUR_CONST");
            result.absolute(4);
            MOYENNE_STABLE = result.getDouble("VALEUR_CONST");
            result.absolute(5);
            MOYENNE_ELEVEE = result.getDouble("VALEUR_CONST");
            result.absolute(6);
            NUM_FACTURE = (int)result.getDouble("VALEUR_CONST");
            result.absolute(7);
            PROMO_VRAC = (int)result.getDouble("VALEUR_CONST");
            result.absolute(8);
            PROMO_CONDI = (int)result.getDouble("VALEUR_CONST");
            result.close();
            state.close();
        }
        catch(NullPointerException | ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
    }
    private void initComponent(){
        JButton boutton = new JButton(new ImageIcon(getClass().getResource(("/Images/debiteur.png"))));
        boutton.setToolTipText("Mise à jour des dettes du client en cours");
        boutton.setPreferredSize(new Dimension(60,60));
        JButton boutton2 = new JButton(new ImageIcon(getClass().getResource(("/Images/constante.png"))));
        boutton2.setToolTipText("Mise à jour des constantes (TVA,Timbre...)");
        boutton2.setPreferredSize(new Dimension(60,60));
        JButton boutton3 = new JButton(new ImageIcon(getClass().getResource(("/Images/NF-RC.png"))));
        boutton3.setToolTipText("Mise à jour des numero fiscal et RC du client en cours");
        boutton3.setPreferredSize(new Dimension(60,60));
        JButton bDeconnexion = new JButton(new ImageIcon(getClass().getResource("/Images/logout.png")));
        bDeconnexion.setToolTipText("Deconnexion");
        bDeconnexion.setPreferredSize(new Dimension(60,60));
        boutton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int numeroClient ;
                String nomClient = "";
                double balance = 0;
                double resteAPayer = 0;
                try{
                    numeroClient = ec.getSelectePersonelNumber(ec.listePersonnels);
                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultat = state.executeQuery("select NUM_CLIENT,BALANCE_PAIEMENT,NOM_CLIENT,PRENOM_CLIENT,RESTE_A_PAYER_HT from CLIENT where NUM_CLIENT = "+numeroClient);
                    resultat.first();
                    nomClient = resultat.getString("NOM_CLIENT")+" "+ec.transformAsName(resultat.getString("PRENOM_CLIENT"));
                    balance = resultat.getDouble("BALANCE_PAIEMENT");
                    resteAPayer = resultat.getDouble("RESTE_A_PAYER_HT");
                    resultat.close();
                    state.close();
                    deb.setInformations(numeroClient, nomClient,resteAPayer, balance);
                    deb.setVisible(true);
                }
                catch(NullPointerException exp){
                    JOptionPane option = new JOptionPane();
                    option.showMessageDialog(null, "Verifiez que vous avez bien selectionné un client !", "Error...", JOptionPane.ERROR_MESSAGE);
                }
                catch(ClassNotFoundException | SQLException exp){
                    exp.printStackTrace();
                }
            }
        });
        boutton2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                cc.setInformations(MainWindow.TAUX_TVA, MainWindow.TAUX_TIMBRE, MainWindow.MOYENNE_BASSE, 
                        MainWindow.MOYENNE_STABLE,MainWindow.MOYENNE_ELEVEE,MainWindow.PROMO_VRAC,MainWindow.PROMO_CONDI);
                cc.setVisible(true);
            }
        });
        boutton3.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               int numeroClient ;
                String nomClient = "";
                String numeroFiscal = "";
                String registreCommerce = "";
                try{
                    numeroClient = ec.getSelectePersonelNumber(ec.listePersonnels);
                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    ResultSet resultat = state.executeQuery("select NUM_CLIENT,NUMERO_FISCAL_CLIENT,RC,NOM_CLIENT,PRENOM_CLIENT from CLIENT where NUM_CLIENT = "
                            +numeroClient);
                    resultat.first();
                    nomClient = resultat.getString("NOM_CLIENT")+" "+ec.transformAsName(resultat.getString("PRENOM_CLIENT"));
                    numeroFiscal = resultat.getString("NUMERO_FISCAL_CLIENT");
                    registreCommerce = resultat.getString("RC");
                    resultat.close();
                    state.close();
                    cic.setInformations(numeroClient, nomClient, numeroFiscal, registreCommerce);
                    cic.setVisible(true);
                }
                catch(NullPointerException exp){
                    JOptionPane option = new JOptionPane();
                    option.showMessageDialog(null, "Verifiez que vous avez bien selectionné un client !", "Error...", JOptionPane.ERROR_MESSAGE);
                }
                catch(ClassNotFoundException | SQLException exp){
                    exp.printStackTrace();
                }
           } 
        });
        bDeconnexion.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               setVisible(false);
               launchingFrame.setVisible(true);
               fenetrePasse.setVisible(true);
           } 
        });
        ec = new EspaceClientele();
        efo = new EspaceFournisseur();
        es = new EspaceStatistique();
        et = new EspaceTorrefaction();
        efa = new EspaceFacturation();
        eb = new EspaceBusiness();
        deb = new Debiteur();
        cc = new ConstantesChanger();
        cic = new ClientInformationsChanger();
        panneau.setBorder(bordure);
        panneau.addTab("Clientèle",new ImageIcon(getClass().getResource(("/Images/Client.png"))),ec);
        panneau.addTab("Fournisseurs",new ImageIcon(getClass().getResource(("/Images/Fournisseur.png"))), efo);
        panneau.addTab("Achats/Ventes/Depenses", new ImageIcon(getClass().getResource(("/Images/Business.png"))), eb);
        panneau.addTab("Torrefaction",new ImageIcon(getClass().getResource(("/Images/Stocks.png"))), et);
        panneau.addTab("Facturation",new ImageIcon(getClass().getResource("/Images/feuilleImprimerie.png")),efa);
        panneau.addTab("Statistiques",new ImageIcon(getClass().getResource(("/Images/graphe.png"))),es);
        panneau.addTab("",new PanneauCafe());
        panneau.setTabComponentAt(6, boutton);
        panneau.addTab("",new PanneauCafe());
        panneau.setTabComponentAt(7, boutton2);
        panneau.addTab("",new PanneauCafe());
        panneau.setTabComponentAt(8, boutton3);
        panneau.addTab("",new PanneauCafe());
        panneau.setTabComponentAt(9, bDeconnexion);
        for(int i = 0 ; i < panneau.getTabCount() ; i++)panneau.setBackgroundAt(i, Color.white);
        this.setContentPane(panneau);
        this.panneau.setOpaque(true);
        this.panneau.setBackground(new Color(153,103,00));
    }
    public void paintComponent(Graphics g){

try {
Image img;
    img = ImageIO.read(new File("C:\\Documents and Settings\\Chakib\\Mes documents\\NetBeansProjects\\Final Sebbane's Project\\Gestionnaire de base de données Sebbane\\build\\classes\\Images/FonCAFE.jpg"));
    this.panneau.getGraphics().drawImage(img, 0, 0, this.getWidth(), this.getHeight(),this);
} catch (IOException e) {e.printStackTrace();}
}
    private void initWindow(){
        this.setTitle("Gestionnaire de Torréfaction *** X Company *** --- Mouley's Production ---");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private static Dimension getMaxDimension(){
        launchingFrame = new IntroductionWindow();
        int largeur = launchingFrame.getWidth();
        int hauteur = launchingFrame.getHeight();
        return new Dimension(largeur,hauteur);
    }
    public static void main(String[] args){
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("TabbedPane.selected", Color.white);
        } 
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        Dimension dimension = getMaxDimension();
        maxWidth  = (int)dimension.getWidth();
        maxHeight = (int)dimension.getHeight();
        fenetrePasse = new PassWindow();
        /*maxWidth  = 1360;
        maxHeight = 768;
        final MainWindow mw = new MainWindow();
        mw.setVisible(true);
        mw.setResizable(false);*/
    }
    public static void sauvegarderBase(){
        saver = new Timer(5*1000,new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                try{
                    final PanneauDialogue p = new PanneauDialogue();
                    JFrame dialogue = new JFrame();
                    dialogue.setSize(600,150);
                    dialogue.setLocationRelativeTo(null);
                    dialogue.setContentPane(p);
                    dialogue.setAlwaysOnTop(true);
                    Timer repainter = new Timer(500,new ActionListener(){
                        public void actionPerformed(ActionEvent evt){
                            p.repaint();
                        }
                    });
                    repainter.start();
                    dialogue.setVisible(true);
                    Files.copy(Paths.get("C:\\GESTION_USINE_CAFE.FDB"), Paths.get("D:\\Safe Database.FDB"),
                            StandardCopyOption.REPLACE_EXISTING);
                    dialogue.setVisible(false);
                    repainter.stop();
                }
                catch(NullPointerException | IOException exp){exp.printStackTrace();}
            }
        });
    }
    public static class PanneauDialogue extends JPanel{
        public int b = 0;
        public void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D)g;
            g2.setPaint(new GradientPaint(0,0,new Color(255,126,126),10,10,new Color(126,126,255),true));
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            g2.setFont(new Font("Calibri",Font.BOLD,25));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(2));
            g2.drawString("Sauvergarde de la base de donnée en cours d'éxecution",
                    (this.getWidth()-fm.stringWidth("Sauvergarde de la base de donnée en cours d'éxecution"))/2,
                    (this.getHeight()-fm.getHeight())/2);
            String str = "";
            if(b == 0){
                str = ("Veuillez patienter S.V.P .");
                b = 1;
            }
            else if(b == 1){
                str = ("Veuillez patienter S.V.P ..");
                b = 2;
            }
            else if(b == 2){
                str = ("Veuillez patienter S.V.P ...");
                b = 3;
            }
            else {
                str = ("Veuillez patienter S.V.P ....");
                b = 0;
            }
            g2.drawString(str, (this.getWidth()-fm.stringWidth("Veuillez patienter S.V.P ."))/2, 3*fm.getHeight());
        }
    }
    class PanneauCafe extends JPanel{
        public void paintComponent(Graphics g){
            try{
                Image img = ImageIO.read(getClass().getResource(("/Images/panneauCafe.jpg")));
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
            catch(NullPointerException | IOException exp){
                exp.printStackTrace();
            }
        }
    }
}




