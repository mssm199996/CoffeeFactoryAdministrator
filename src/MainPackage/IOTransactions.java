package MainPackage;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lennovo
 */
public class IOTransactions {
    private static final String urlfb ="jdbc:firebirdsql://LOCALHOST:3050/C:/GESTION_USINE_CAFE.FDB";
    private static final String userfb = "SYSDBA";
    private static final String passwdfb = "masterkey";
    private static  Connection connect;
    public static List<String> Load(Path p){
        List liste = null ;
        try{
            liste = Files.readAllLines(p,Charset.forName("UTF-8"));
        }
        catch(IOException e){e.printStackTrace();}
        return liste;
    }
    public static void Save(Path p,List<String> liste){
        List total = IOTransactions.Load(p);
        for(String str: liste)total.add(str);
        try{
            Files.write(p, total, Charset.forName("UTF-8"));
        }
        catch(IOException e){e.printStackTrace();}
    }
    public static Connection getConnectionUtility() throws ClassNotFoundException{
        if(connect == null){
            try {
                Class.forName("org.firebirdsql.jdbc.FBDriver");
                connect = DriverManager.getConnection(urlfb, userfb,passwdfb);
            } 
            catch (SQLException e) {}}
        return connect;
    }
}
