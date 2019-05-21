/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage;

import javax.swing.JPanel;

/**
 *
 * @author Sidi Mohamed
 */
public abstract class PanneauImpression extends JPanel{
    public abstract void setFactureInformations(String numFacture,String dateFacture,String numeroClient,
        String nomClient,String adresse,String rc,String Fiscale);
    public abstract void setNewRow(int compteur,String typeAchat,double quantite,double PU,double prixTotal,double avance,
            double reste);
    public abstract void setFactureStatistique(String prixHT,String prixAT,double verse,double reste);
}
