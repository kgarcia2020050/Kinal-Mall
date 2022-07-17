    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kennethgarcia.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 26/05/2021
 * @time 09:32:09
 */
public class Conexion {
    
    
    private Connection conexion;
    private final String URL;
    private static Conexion instancia;
    
    
private Conexion(){
    
    //URL="jdbc:mysql://localhost:3306/dbkinalmallkennethgarcia?user=root&password=rekkadaizanto&serverTimeZone=UTC&useSSL=false";
    
    
    
    URL = "jdbc:mysql://localhost:3306/IN5BM_KinalMall?allowPublicKeyRetrieval=true&serverTimezone=UTC&useSSL=false";
    
    
    try{
    //Registrar el driver
    
    
    //Opcion 1
    
    
Class.forName("com.mysql.jdbc.Driver").newInstance();

 

//Opcion 2
//Class.forName("com.mysql.jdbc.Driver");
    
//Opcion 3
//Class.forName("com.mysql.cj.jdbc.Driver");

//Opcion 4
//A partir de jdk 6 los driver jdbc 4 se registra automaticamente


//la conexion se hace con getConnection
//conexion=DriverManager.getConnection(URL);


conexion = DriverManager.getConnection(URL, "root", "admin");
    }catch (ClassNotFoundException e){
        System.out.println("Se produjo un error.");
        e.printStackTrace();
    }catch (InstantiationException  e){
        System.out.println("no se puede instanciar");
        e.printStackTrace();
    }catch(IllegalAccessException e){
        System.out.println("Ilegal");
    e.printStackTrace();
    }catch(SQLException e){
        System.out.println("Problema de sql");
    e.printStackTrace();
    }catch(Exception e){
        System.out.println("Excepcion");
    e.printStackTrace();
    }
}

public static Conexion getInstance(){   
    if(instancia==null){
    instancia=new Conexion();
    }
    return instancia;
}

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }



}
