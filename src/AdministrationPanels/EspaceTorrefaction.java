/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdministrationPanels;


/**
 *
 * @author lennovo
 */
import MainPackage.IOTransactions;
import MainPackage.MainWindow;
import ModeledWindows.IntroductionWindow;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
public class EspaceTorrefaction extends MainPanel{
    private Formulaire cafeVert;
    private Formulaire cafeTorrefie;
    private Formulaire cafeVrac;
    private Formulaire cafeConditionne;
    private Formulaire Kraft;
    private Formulaire Aluminium;
    public JButton refreshButton ;
    public EspaceTorrefaction(){
        super();
        this.setQuantities();
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
    }
    @Override
    void initListeners() {
        this.refreshButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Refresh();
            }
        });
    }
    @Override
    void initComponent() {
        final Color c = new Color(160,80,0);
        this.setPreferredSize(new Dimension(MainWindow.maxWidth,MainWindow.maxHeight));
        this.setLayout(new FlowLayout());
        this.setBackground(c);
        this.cafeVert = new Formulaire("Café vert","Torréfier",c,Color.white);
        this.cafeTorrefie = new Formulaire("Café torréfié (grains)","Moudre",c,Color.white);
        this.cafeVrac = new Formulaire("Café moulu (en vrac)","Conditioner",c,Color.white);
        this.cafeConditionne = new Formulaire("Café moulu (conditionné)","",c,Color.white);
        this.Kraft = new Formulaire("Kraft","Utiliser",c,Color.white);
        this.Aluminium = new Formulaire("Aluminium","Utiliser",c,Color.white);
        this.refreshButton = new JButton(new ImageIcon("refresh.png"));
        
        this.add(new Label("GESTIONNAIRE DE CAFE",new Font("Arial",Font.BOLD,24),c,Color.BLACK));
        this.add(this.cafeVert);
        this.add(this.cafeTorrefie);
        this.add(this.cafeVrac);
        this.add(this.cafeConditionne);
        this.add(new Label("GESTIONNAIRE D'EMBALLAGE",new Font("Arial",Font.BOLD,24),c,Color.BLACK));
        this.add(this.Aluminium);
        this.add(this.Kraft);
    }
    class ChampText extends JTextField{
        public ChampText(){
            this.setPreferredSize(new Dimension(220,30));
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED,Color.blue,Color.red));
            this.setHorizontalAlignment(JTextField.HORIZONTAL);
        }
    }
    class Label extends JLabel{
        private Font font;
        private boolean centering;
        private String text ;
        private Color couleurFond;
        private Color couleurFont;
        public Label(String str,boolean b,Color c){
            this.setPreferredSize(new Dimension(200,30));
            this.text = str;
            this.centering = b;
            this.font = new Font("Arial",Font.BOLD,14);
            this.couleurFond = c;
            this.couleurFont = Color.black;
        }
        public Label(String str,Font f,Color c,Color c2){
            this.font = f;
            this.text = str;
            this.centering = true;
            this.couleurFond = c;
            this.couleurFont = c2;
            this.setPreferredSize(new Dimension(400,60));///////
            this.setBorder(MainWindow.bordure);
        }
        @Override
        public void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(couleurFond);
            g2.fillRect(0,0,this.getWidth(),this.getHeight());
            g2.setFont(font);
            g2.setColor(couleurFont);
            FontMetrics fm = g2.getFontMetrics();
            int hauteur = fm.getHeight();
            if(centering){
                int longueur = fm.stringWidth(text);
                g2.drawString(text,this.getWidth()/2-longueur/2,this.getHeight()/2+hauteur/4);
            }
            else g2.drawString(text,0,this.getHeight()/2+hauteur/4);
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
    class Formulaire extends JPanel{
        private String title ;
        Label marchandise;
        Label stock;
        Boutton elargir;
        ChampText soustraction;
        Boutton actioner;
        Label Kg2 ;
        private Color couleurFond;
        private Color couleurFont;
        public Formulaire(String marchandise,String fonctionBoutton,Color c,Color c2){
            this.couleurFond = c;
            this.couleurFont = c2;
            this.title = marchandise;
            this.setPreferredSize(new Dimension(MainWindow.maxWidth-10,75));
            this.setBorder(BorderFactory.createTitledBorder(MainWindow.bordure,title,TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.TOP,new Font("Arial",Font.BOLD,14),couleurFont));
            this.setBackground(c);
            this.initComponents(fonctionBoutton);
            this.initListeners();
        }
        private void initComponents(String fonctionBoutton){
            this.marchandise = new Label("Quantitée totale:",false,couleurFond);
            this.stock = new Label("?",true,couleurFond);
            if(!this.title.equals("Café moulu (conditionné)")){
                this.elargir = new Boutton(fonctionBoutton+"->>>");
                this.soustraction = new ChampText();
                this.soustraction.setText("0");
                this.Kg2 = new Label("Kg",true,couleurFond);
                this.actioner = new Boutton(fonctionBoutton);
            }
            this.add(this.marchandise);
            this.add(this.stock);
            this.add(new Label("Kg",true,couleurFond));
            if(!this.title.equals("Café moulu (conditionné)")){
                this.soustraction.setVisible(false);
                this.actioner.setVisible(false);
                this.Kg2.setVisible(false);
                this.add(this.elargir);
                this.add(this.soustraction);
                this.add(this.Kg2);
                this.add(this.actioner);
            }
        }
        private void initListeners(){
            if(!this.title.equals("Café moulu (conditionné)")){
                this.elargir.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e){
                        if(!actioner.isVisible()){
                            elargir.setVisible(false);
                            soustraction.setVisible(true);
                            actioner.setVisible(true);
                            Kg2.setVisible(true);
                            revalidate();
                        }
                    }
                });
                switch(title){
                    case "Café vert" :
                        this.actioner.addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                JOptionPane option = new JOptionPane();
                                int reponse = JOptionPane.showConfirmDialog(null,"Voulez vraiment effectuer cette action ?",
                                        "Torréfaction",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                                if(reponse == JOptionPane.YES_OPTION){
                                    try{
                                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    ResultSet result = state.executeQuery("select NUM_PRODUIT,QTE_REELLE_DISPONIBLE "
                                            + "from GESTION_PRODUITS where NUM_PRODUIT = "+1);
                                    result.first();
                                    if(result.getDouble("QTE_REELLE_DISPONIBLE") >= Double.parseDouble(soustraction.getText())){
                                        double updatedQuantity = result.getDouble("QTE_REELLE_DISPONIBLE") - Double.parseDouble(soustraction.getText());
                                        result.updateDouble("QTE_REELLE_DISPONIBLE", updatedQuantity);
                                        result.updateRow();
                                        result = state.executeQuery("select NUM_PRODUIT,QTE_REELLE_DISPONIBLE "
                                            + "from GESTION_PRODUITS where NUM_PRODUIT = "+2);
                                        result.first();
                                        updatedQuantity = result.getDouble("QTE_REELLE_DISPONIBLE") + ((Double.parseDouble(soustraction.getText())*5)/6);
                                        result.updateDouble("QTE_REELLE_DISPONIBLE", updatedQuantity);
                                        result.updateRow();
                                        //-------- insertion sur gestionnaire torrefaction
                                        result = state.executeQuery("select NUM_OPERATION from GESTION_TORREFACTION");
                                        result.last();
                                        int num = 0;
                                        try{
                                            num = result.getInt("NUM_OPERATION")+1;
                                        }
                                        catch(NullPointerException exp){
                                            num = 1;
                                        }
                                        state.executeUpdate("insert into GESTION_TORREFACTION ("
                                                + "NUM_OPERATION,"
                                                + "NOM_CAFE,"
                                                + "QUANTITE_PERDUE,"
                                                + "DATE_OPERATION"
                                                + ") values ("
                                                + num + ","
                                                + "'Cafe vert',"
                                                + Double.parseDouble(soustraction.getText())+","
                                                + "CURRENT_DATE)");
                                        result.close();
                                        state.close();
                                        setQuantities();
                                        elargir.setVisible(true);
                                        soustraction.setVisible(false);
                                        actioner.setVisible(false);
                                        Kg2.setVisible(false);
                                        revalidate();
                                    }
                                    else {
                                        JOptionPane option2 = new JOptionPane();
                                        JOptionPane.showMessageDialog(null,"La quantité introduite est superieur au stock disponible","Error...",JOptionPane.ERROR_MESSAGE);
                                        result.close();
                                        state.close();
                                    }
                                }
                                catch(NumberFormatException | NullPointerException | ClassNotFoundException | SQLException exp){
                                    exp.printStackTrace();
                                }
                                }
                            }
                        });
                        break;
                    case "Café torréfié (grains)" :
                        this.actioner.addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                JOptionPane option = new JOptionPane();
                                int reponse = JOptionPane.showConfirmDialog(null,"Voulez vraiment effectuer cette action ?",
                                        "Mouture",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                                if(reponse == JOptionPane.YES_OPTION){
                                    try{
                                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    ResultSet result = state.executeQuery("select NUM_PRODUIT,QTE_REELLE_DISPONIBLE "
                                            + "from GESTION_PRODUITS where NUM_PRODUIT = "+2);
                                    result.first();
                                    if(result.getDouble("QTE_REELLE_DISPONIBLE") >= Double.parseDouble(soustraction.getText())){
                                        double updatedQuantity = result.getDouble("QTE_REELLE_DISPONIBLE") - Double.parseDouble(soustraction.getText());
                                        result.updateDouble("QTE_REELLE_DISPONIBLE", updatedQuantity);
                                        result.updateRow();
                                    
                                        result = state.executeQuery("select NUM_PRODUIT,QTE_REELLE_DISPONIBLE "
                                            + "from GESTION_PRODUITS where NUM_PRODUIT = "+3);
                                        result.first();
                                        updatedQuantity = result.getDouble("QTE_REELLE_DISPONIBLE") + Double.parseDouble(soustraction.getText());
                                        result.updateDouble("QTE_REELLE_DISPONIBLE", updatedQuantity);
                                        result.updateRow();
                                        //-------- insertion sur gestionnaire torrefaction
                                        result = state.executeQuery("select NUM_OPERATION from GESTION_TORREFACTION");
                                        result.last();
                                        int num = 0;
                                        try{
                                            num = result.getInt("NUM_OPERATION")+1;
                                        }
                                        catch(NullPointerException exp){
                                            num = 1;
                                        }
                                        state.executeUpdate("insert into GESTION_TORREFACTION ("
                                                + "NUM_OPERATION,"
                                                + "NOM_CAFE,"
                                                + "QUANTITE_PERDUE,"
                                                + "DATE_OPERATION"
                                                + ") values ("
                                                + num + ","
                                                + "'Cafe torrefie (grains)',"
                                                + Double.parseDouble(soustraction.getText())+","
                                                + "CURRENT_DATE)");
                                        result.close();
                                        state.close();
                                        setQuantities();
                                        elargir.setVisible(true);
                                        soustraction.setVisible(false);
                                        actioner.setVisible(false);
                                        Kg2.setVisible(false);
                                        revalidate();
                                    }
                                    else {
                                        JOptionPane option2 = new JOptionPane();
                                        JOptionPane.showMessageDialog(null,"La quantité introduite est superieur au stock disponible","Error...",JOptionPane.ERROR_MESSAGE);
                                        result.close();
                                        state.close();
                                    }
                                }
                                catch(NumberFormatException | NullPointerException | ClassNotFoundException | SQLException exp){
                                    JOptionPane option2 = new JOptionPane();
                                    JOptionPane.showMessageDialog(null, 
                                            "Veuillez vérifier que vous avez correctement entré la donnée !", 
                                            "Error...", JOptionPane.ERROR_MESSAGE);
                                    exp.printStackTrace();
                                }
                                }
                            }
                        });
                        break;
                    case "Café moulu (en vrac)" :
                        this.actioner.addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                JOptionPane option = new JOptionPane();
                                int reponse = JOptionPane.showConfirmDialog(null,"Voulez vraiment effectuer cette action ?",
                                        "Conditionnement",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                                if(reponse == JOptionPane.YES_OPTION){
                                    try{
                                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    ResultSet result = state.executeQuery("select NUM_PRODUIT,QTE_REELLE_DISPONIBLE "
                                            + "from GESTION_PRODUITS where NUM_PRODUIT = "+3);
                                    result.first();
                                    if(result.getDouble("QTE_REELLE_DISPONIBLE") >= Double.parseDouble(soustraction.getText())){
                                        double updatedQuantity = result.getDouble("QTE_REELLE_DISPONIBLE") - Double.parseDouble(soustraction.getText());
                                        result.updateDouble("QTE_REELLE_DISPONIBLE", updatedQuantity);
                                        result.updateRow();
                                    
                                        result = state.executeQuery("select NUM_PRODUIT,QTE_REELLE_DISPONIBLE "
                                            + "from GESTION_PRODUITS where NUM_PRODUIT = "+4);
                                        result.first();
                                        updatedQuantity = result.getDouble("QTE_REELLE_DISPONIBLE") + Double.parseDouble(soustraction.getText());
                                        result.updateDouble("QTE_REELLE_DISPONIBLE", updatedQuantity);
                                        result.updateRow();
                                        //-------- insertion sur gestionnaire torrefaction
                                        result = state.executeQuery("select NUM_OPERATION from GESTION_TORREFACTION");
                                        result.last();
                                        int num = 0;
                                        try{
                                            num = result.getInt("NUM_OPERATION")+1;
                                        }
                                        catch(NullPointerException exp){
                                            num = 1;
                                        }
                                        state.executeUpdate("insert into GESTION_TORREFACTION ("
                                                + "NUM_OPERATION,"
                                                + "NOM_CAFE,"
                                                + "QUANTITE_PERDUE,"
                                                + "DATE_OPERATION"
                                                + ") values ("
                                                + num + ","
                                                + "'Cafe moulu (en vrac)',"
                                                + Double.parseDouble(soustraction.getText())+","
                                                + "CURRENT_DATE)");
                                        result.close();
                                        state.close();
                                        setQuantities();
                                        elargir.setVisible(true);
                                        soustraction.setVisible(false);
                                        actioner.setVisible(false);
                                        Kg2.setVisible(false);
                                        revalidate();
                                    }
                                    else {
                                        JOptionPane option2 = new JOptionPane();
                                        option2.showMessageDialog(null,"La quantite introduite est superieur au stock disponible","Error...",JOptionPane.ERROR_MESSAGE);
                                        result.close();
                                        state.close();
                                    }
                                }
                                catch(NumberFormatException | NullPointerException | ClassNotFoundException | SQLException exp){
                                    exp.printStackTrace();
                                }
                                }
                            }
                        });
                        break;
                    case "Aluminium" : 
                        this.actioner.addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e){
                                JOptionPane option = new JOptionPane();
                                int reponse = option.showConfirmDialog(null,"Voulez vraiment effectuer cette action ?",
                                        "Utilisation d'aluminium",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                                if(reponse == JOptionPane.YES_OPTION){
                                    try{
                                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    ResultSet result = state.executeQuery("select NUM_PRODUIT,QTE_REELLE_DISPONIBLE "
                                            + "from GESTION_PRODUITS where NUM_PRODUIT = "+5);
                                    result.first();
                                    if(result.getDouble("QTE_REELLE_DISPONIBLE") >= Double.parseDouble(soustraction.getText())){
                                        double updatedQuantity = result.getDouble("QTE_REELLE_DISPONIBLE") - Double.parseDouble(soustraction.getText());
                                        result.updateDouble("QTE_REELLE_DISPONIBLE", updatedQuantity);
                                        result.updateRow();
                                        //-------- insertion sur gestionnaire torrefaction
                                        result = state.executeQuery("select NUM_OPERATION from GESTION_TORREFACTION");
                                        result.last();
                                        int num = 0;
                                        try{
                                            num = result.getInt("NUM_OPERATION")+1;
                                        }
                                        catch(NullPointerException exp){
                                            num = 1;
                                        }
                                        state.executeUpdate("insert into GESTION_TORREFACTION ("
                                                + "NUM_OPERATION,"
                                                + "NOM_CAFE,"
                                                + "QUANTITE_PERDUE,"
                                                + "DATE_OPERATION"
                                                + ") values ("
                                                + num + ","
                                                + "'Aluminium',"
                                                + Double.parseDouble(soustraction.getText())+","
                                                + "CURRENT_DATE)");
                                        result.close();
                                        state.close();
                                        stock.setText(Double.toString(updatedQuantity));
                                        elargir.setVisible(true);
                                        soustraction.setVisible(false);
                                        actioner.setVisible(false);
                                        Kg2.setVisible(false);
                                        revalidate();
                                    }
                                    else {
                                        JOptionPane option2 = new JOptionPane();
                                        option2.showMessageDialog(null,"La quantite introduite est superieur au stock disponible","Error...",JOptionPane.ERROR_MESSAGE);
                                        result.close();
                                        state.close();
                                    }
                                }
                                catch(NumberFormatException | NullPointerException | ClassNotFoundException | SQLException exp){
                                    exp.printStackTrace();
                                }
                                }
                            }
                        });
                        break;
                    case "Kraft" :
                        this.actioner.addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e){
                                JOptionPane option = new JOptionPane();
                                int reponse = option.showConfirmDialog(null,"Voulez vraiment effectuer cette action ?",
                                        "Utilisation de kraft",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                                if(reponse == JOptionPane.YES_OPTION){
                                    try{
                                    Statement state = IOTransactions.getConnectionUtility().createStatement(
                                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    ResultSet result = state.executeQuery("select NUM_PRODUIT,QTE_REELLE_DISPONIBLE "
                                            + "from GESTION_PRODUITS where NUM_PRODUIT = "+6);
                                    result.first();
                                    if(result.getDouble("QTE_REELLE_DISPONIBLE") >= Double.parseDouble(soustraction.getText())){
                                        double updatedQuantity = result.getDouble("QTE_REELLE_DISPONIBLE") - Double.parseDouble(soustraction.getText());
                                        result.updateDouble("QTE_REELLE_DISPONIBLE", updatedQuantity);
                                        result.updateRow();
                                        result = state.executeQuery("select NUM_OPERATION from GESTION_TORREFACTION");
                                        result.last();
                                        int num = 0;
                                        try{
                                            num = result.getInt("NUM_OPERATION")+1;
                                        }
                                        catch(NullPointerException exp){
                                            num = 1;
                                        }
                                        state.executeUpdate("insert into GESTION_TORREFACTION ("
                                                + "NUM_OPERATION,"
                                                + "NOM_CAFE,"
                                                + "QUANTITE_PERDUE,"
                                                + "DATE_OPERATION"
                                                + ") values ("
                                                + num + ","
                                                + "'Kraft',"
                                                + Double.parseDouble(soustraction.getText())+","
                                                + "CURRENT_DATE)");
                                        result.close();
                                        state.close();
                                        stock.setText(Double.toString(updatedQuantity));
                                        elargir.setVisible(true);
                                        soustraction.setVisible(false);
                                        actioner.setVisible(false);
                                        Kg2.setVisible(false);
                                        revalidate();
                                    }
                                    else {
                                        JOptionPane option2 = new JOptionPane();
                                        option2.showMessageDialog(null,"La quantité introduite est superieur au stock disponible","Error...",JOptionPane.ERROR_MESSAGE);
                                        result.close();
                                        state.close();
                                    }
                                }
                                catch(NumberFormatException | NullPointerException | ClassNotFoundException | SQLException exp){
                                    exp.printStackTrace();
                                }
                                }
                            }
                        });
                        break;
                }
                soustraction.setText("0");
            }
        }
    }
    class Boutton extends JButton{
        public Boutton(String cap){
            super(cap);
            this.setPreferredSize(new Dimension(150,30));
        }
    }
    private void setQuantities(){
        try{
            Statement state = IOTransactions.getConnectionUtility().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = state.executeQuery("select * from GESTION_PRODUITS where CATEGORIE = 'Achats'");
            String columnQuantity = "QTE_REELLE_DISPONIBLE";
            // ------------- Café vert ----------------
            result.absolute(1);
            double CafeVert = result.getDouble(columnQuantity);
            this.cafeVert.stock.setText(Double.toString(CafeVert));
            // ------------- Café Cafe torrefie (Grains) --------------
            result.absolute(2);
            double CafeTorrefieGrains = result.getDouble(columnQuantity);
            this.cafeTorrefie.stock.setText(Double.toString(CafeTorrefieGrains));
            // ------------- Café en vrac ------------
            result.absolute(3);
            double CafeEnVrac = result.getDouble(columnQuantity);
            this.cafeVrac.stock.setText(Double.toString(CafeEnVrac));
            // ------------- Café conditionne ------------
            result.absolute(4);
            double CafeConditionne = result.getDouble(columnQuantity);
            this.cafeConditionne.stock.setText(Double.toString(CafeConditionne));
            // ------------- Café en vrac ------------
            result.absolute(5);
            double Aluminium = result.getDouble(columnQuantity);
            this.Aluminium.stock.setText(Double.toString(Aluminium));
            // ------------- Café en vrac ------------
            result.absolute(6);
            double Kraft = result.getDouble(columnQuantity);
            this.Kraft.stock.setText(Double.toString(Kraft));
            
            result.close();
            state.close();
        }
        catch(NullPointerException | ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
    }
    public void Refresh(){
        // Refreshing Buys
        try{
            Statement state = IOTransactions.getConnectionUtility().createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = state.executeQuery("select * from GESTION_PRODUITS where CATEGORIE = 'Achats' and COMPTABILISE = "
                    + "'N' OR CATEGORIE = 'Achats' and COMPTABILISE = 'R' OR CATEGORIE = 'Ventes' and COMPTABILISE = 'N'");
            int nombreEnregistrement = 0;
            while(result.next())nombreEnregistrement++;
            for(int i = 7 ; i <= nombreEnregistrement ; i++){
                int line = -1;
                result.absolute(i);
                switch(result.getString("NOM_PRODUIT")){
                    case "Cafe vert 60Kg": line = 1;
                        break;
                    case "Cafe vert autre": line = 1;
                        break;
                    case "Cafe torrefie (grains)": line = 2;
                        break;
                    case "Cafe torrefie (moulu) [en vrac]": line = 3;
                        break;
                    case "Cafe torrefie (moulu) [conditionne]": line = 4;
                        break;
                    case "250g Papier Alluminium": line = 5;
                        break;
                    case "1Kg Kraft": line = 6;
                        break;
                }
                double additionalQuantity = result.getDouble("QUANTITE_DEPLACEE")*result.getDouble("MOUVEMENT_EFFECTUE");
                result.updateString("COMPTABILISE", "O");
                result.updateRow();
                result.absolute(line);
                result.updateDouble("QTE_REELLE_DISPONIBLE", additionalQuantity+result.getDouble("QTE_REELLE_DISPONIBLE"));
                result.updateRow();
            }
            result.close();
            state.close();
            EspaceClientele.ImportWinnersFromDatabase();
        }
        catch(NullPointerException | ClassNotFoundException | SQLException exp){
            exp.printStackTrace();
        }
        this.setQuantities();
    }
}
