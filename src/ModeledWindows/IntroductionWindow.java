 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeledWindows;
import MainPackage.MainWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Painter;
import javax.swing.UIDefaults;
import javax.swing.border.EtchedBorder;
/**
 *
 * @author lennovo
 */
public class IntroductionWindow extends JFrame{
    public static JProgressBar barre;
    public IntroductionWindow(){
        this.initComponent();
        this.initWindow();
    }
    private void initComponent(){
        this.barre = new Barre();
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new Panneau(),BorderLayout.CENTER);
        contentPane.add(barre,BorderLayout.SOUTH);
        this.setContentPane(contentPane);
    }
    private void initWindow(){
        this.setTitle("Sebbane's Industry administrator (Introduction) ");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setVisible(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(false);
    }
    class Panneau extends JPanel{
        Image img;
        public Panneau(){
            this.setBorder(MainWindow.bordure);
            try{
                img = ImageIO.read(getClass().getResource(("/Images/Fond_Cafe.png")));
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        public void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
    class Barre extends JProgressBar{
        public Barre(){
            this.setOpaque(false);
            this.initUI();
            this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.red));
            this.setStringPainted(true);
            this.setFont(new Font("Arial",Font.BOLD,16));
            this.setPreferredSize(new Dimension(200,30));
            this.setMaximum(15);
            this.setMinimum(0);
        }
        private void initUI(){
            UIDefaults defaults = new UIDefaults();
            defaults.put("ProgressBar[Enabled].foregroundPainter", new MyPainter());
            defaults.put("ProgressBar[Enabled+Finished].foregroundPainter", new MyPainter());
            this.putClientProperty("Nimbus.Overrides.InheritDefaults", Boolean.TRUE);
            this.putClientProperty("Nimbus.Overrides", defaults);
        }
    }
    class MyPainter implements Painter<JProgressBar> {
        public void paint(Graphics2D gd, JProgressBar t, int width, int height) {
            gd.setPaint(new GradientPaint(0,0,Color.green,20,20,Color.yellow,true));
            gd.fillRect(1, 1, width-1, height-2);
        }
    }
}
