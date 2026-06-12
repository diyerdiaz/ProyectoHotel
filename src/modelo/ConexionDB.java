/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author diyer
 */
public class ConexionDB {
    public static Connection conexion;

    

    private ConexionDB() {

        try {
            String driverBD = "com.mysql.cj.jdbc.Driver";
            String urlBD = "jdbc:mysql://localhost/Autos";
            String usuarioBD = "root";
            String claveBD = "";
            Class.forName(driverBD);
            conexion = DriverManager.getConnection(urlBD, usuarioBD, claveBD);
        } catch (ClassNotFoundException ex) {
            System.err.println("no encuentro el driver:" + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("Error al conectarme:" + ex.getMessage());
        }
        } 
    
    
    

    public static void desconectar(){
        try {
            conexion.close();
        } catch (SQLException ex) {
            System.err.println("Error al desconectar:" + ex.getMessage());
        }
    }
    
    
    
    public static ConexionDB getInstance() {
        return ConexionBdHolder.INSTANCE;
    }
    
    private static class ConexionBdHolder {

        private static final ConexionDB INSTANCE = new ConexionDB();
    }
    
}
