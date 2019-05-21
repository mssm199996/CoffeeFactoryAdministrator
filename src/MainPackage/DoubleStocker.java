/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lennovo
 */
public class DoubleStocker<T,S>{
    static List<Integer> liste1 = new ArrayList<Integer>();
    static List<Integer> liste2 = new ArrayList<Integer>();
    public void add(int n1,int n2){
        this.liste1.add(n1);
        this.liste2.add(n2);
    }
    public int getDataListe1(int index){
        return this.liste1.get(index);
    }
    public int getDataListe2(int index){
        return this.liste2.get(index);
    }
    public void clear(){
        this.liste1.clear();
        this.liste2.clear();
    }
    public void remove(int n1,int n2){
        boolean b;
        b = this.liste1.remove((Integer)n1);
        b = this.liste2.remove((Integer)n2);
    }
    public boolean isEmpty(){
        if(liste1.isEmpty())return true;
        else return false;
    }
    public int size(){
        return this.liste1.size();
    }
}
