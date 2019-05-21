/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AdministrationPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author Sidi Mohamed
 */
public abstract class MainPanel extends JPanel{
    public MainPanel(){
        this.initPanel();
        this.initVariables();
        this.initComponent();
        this.initListeners();
    }
    void initVariables(){}
    abstract void initListeners();
    abstract void initComponent();
    protected void initPanel(){
        this.setBackground(Color.white);
        this.setLayout(new BorderLayout());
    }
}
