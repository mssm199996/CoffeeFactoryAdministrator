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
public class FicheClient extends FichePersonelle{
    public FicheClient(){
        super(new Color(200,200,255),new Color(255,50,50),new Dimension(400,760));
        IntroductionWindow.barre.setValue(IntroductionWindow.barre.getValue()+1);
    }
}
