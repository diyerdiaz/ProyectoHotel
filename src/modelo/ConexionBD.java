/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    public static Connection conexion;

    public static void desconectar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private ConexionBD() {
        try {
         
            String driverBD = "org.postgresql.Driver";

            String urlBD = "jdbc:postgresql://164.68.98.66:5439/evaluacion";
            String usuarioBD = "postgres";
            String claveBD = "Sena2026*";

            Class.forName(driverBD);

            conexion = DriverManager.getConnection(urlBD, usuarioBD, claveBD);
            System.out.println("=========================================");
            System.out.println("¡CONEXIÓN EXITOSA A POSTGRESQL EN COOLIFY!");
            System.out.println("=========================================");
            
        } catch (ClassNotFoundException ex) {
            System.err.println("ERROR: No se encontró el Driver de Postgres. Agrega el JAR a Libraries.");
        } catch (SQLException ex) {
            System.err.println("ERROR DE CONEXIÓN: Revisa si la BD está encendida en Coolify. Detalle: " + ex.getMessage());
        }
    }

    public static ConexionBD getInstance() {
        return ConexionBDHolder.INSTANCE;
    }

    private static class ConexionBDHolder {
        private static final ConexionBD INSTANCE = new ConexionBD();
    }
    
    public static void main(String[] args) {
        ConexionBD.getInstance();
    }
}
