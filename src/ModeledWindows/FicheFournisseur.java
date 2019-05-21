/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeledWindows;

import java.awt.Color;
import java.awt.Dimension;


/**
 *
 * @author Sidi Mohamed
 */
public class FicheFournisseur extends FichePersonelle{
    public FicheFournisseur(){
        super(new Color(255,200,200),new Color(50,50,255),new Dimension(400,700));
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
    }
}
