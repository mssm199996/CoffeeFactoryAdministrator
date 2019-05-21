/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage;

/**
 *
 * @author lennovo
 */
public class AchatsClient {
    public static int compteur = 1;
    public int numeroFacture = 1;
    public String typeAchat;
    public int Quantite;
    public float PU;
    public float totalAchat;
    public float Avance;
    public float Reste;
    public float totalPrix;
    public AchatsClient(String str,int qu,float pu ,float totalachat,float avance,float reste,float totalprix){
        this.numeroFacture = compteur;
        this.typeAchat = str;
        this.Quantite = qu;
        this.PU = pu;
        this.totalAchat = totalachat;
        this.Avance = avance;
        this.Reste = reste;
        this.totalPrix = totalprix;
        compteur ++;
    }
    
}
