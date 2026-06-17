/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author diyer
 */
public class TipoHabitacion {
    
    private  int  idtipoHabitacion;
    private  String nombreTipoHabitacion;
    private  String descripcionTipoHabitacion;

    public int getIdtipoHabitacion() {
        return idtipoHabitacion;
    }

    public void setIdtipoHabitacion(int idtipoHabitacion) {
        this.idtipoHabitacion = idtipoHabitacion;
    }

    public String getNombreTipoHabitacion() {
        return nombreTipoHabitacion;
    }

    public void setNombreTipoHabitacion(String nombreTipoHabitacion) {
        this.nombreTipoHabitacion = nombreTipoHabitacion;
    }

    public String getDescripcionTipoHabitacion() {
        return descripcionTipoHabitacion;
    }

    public void setDescripcionTipoHabitacion(String descripcionTipoHabitacion) {
        this.descripcionTipoHabitacion = descripcionTipoHabitacion;
    }

    @Override
    public String toString() {
        return "TiposHabitacion{" + "nombreTipoHabitacion=" + nombreTipoHabitacion + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoHabitacion other = (TipoHabitacion) obj;
        return this.idtipoHabitacion == other.idtipoHabitacion;
    }
    
    
    
    public Iterator<TipoHabitacion> Listar() {

    ArrayList<TipoHabitacion> losTiposHabitacion = new ArrayList<>();

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM TipoHabitacion");

        ResultSet rs = sql.executeQuery();

        TipoHabitacion unTipoHabitacion;

        while (rs.next()) {

            unTipoHabitacion = new TipoHabitacion();

            unTipoHabitacion.setIdtipoHabitacion(rs.getInt("idtipoHabitacion"));
            unTipoHabitacion.setNombreTipoHabitacion(rs.getString("nombreTipoHabitacion"));
            unTipoHabitacion.setDescripcionTipoHabitacion(rs.getString("descripcionTipoHabitacion"));

            losTiposHabitacion.add(unTipoHabitacion);
        }

    } catch (SQLException ex) {
        System.err.println("Error al listar: " + ex.getMessage());
    }

    if (losTiposHabitacion.isEmpty()) {
        TipoHabitacion miTipoHabitacion = new TipoHabitacion();
        miTipoHabitacion.setNombreTipoHabitacion("No hay nada registrado");
        losTiposHabitacion.add(miTipoHabitacion);
    }

    return losTiposHabitacion.iterator();
}

public void insertar() {

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "INSERT INTO TipoHabitacion VALUES(NULL,?,?)");

        sql.setString(1, this.getNombreTipoHabitacion());
        sql.setString(2, this.getDescripcionTipoHabitacion());

        sql.executeUpdate();

        System.out.println("Insertado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al insertar: " + ex.getMessage());
    }
}

public void modificar() {

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "UPDATE TipoHabitacion "
                + "SET nombreTipoHabitacion=?, "
                + "descripcionTipoHabitacion=? "
                + "WHERE idtipoHabitacion=?");

        sql.setString(1, this.getNombreTipoHabitacion());
        sql.setString(2, this.getDescripcionTipoHabitacion());
        sql.setInt(3, this.getIdtipoHabitacion());

        sql.executeUpdate();

        System.out.println("Modificado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al modificar: " + ex.getMessage());
    }
}

public void eliminar() {

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "DELETE FROM TipoHabitacion WHERE idtipoHabitacion=?");

        sql.setInt(1, this.getIdtipoHabitacion());

        sql.executeUpdate();

        System.out.println("Eliminado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al eliminar: " + ex.getMessage());
    }
}

public Iterator<TipoHabitacion> buscar(String busqueda) {

    ArrayList<TipoHabitacion> losTiposHabitacion = new ArrayList<>();

    try {

        System.out.println("Buscando: " + busqueda);

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM TipoHabitacion "
                + "WHERE idtipoHabitacion LIKE ? "
                + "OR nombreTipoHabitacion LIKE ? "
                + "OR descripcionTipoHabitacion LIKE ?");

        sql.setString(1, "%" + busqueda + "%");
        sql.setString(2, "%" + busqueda + "%");
        sql.setString(3, "%" + busqueda + "%");

        ResultSet rs = sql.executeQuery();

        int contador = 0;

        while (rs.next()) {

            contador++;

            TipoHabitacion unTipoHabitacion = new TipoHabitacion();

            unTipoHabitacion.setIdtipoHabitacion(rs.getInt("idtipoHabitacion"));
            unTipoHabitacion.setNombreTipoHabitacion(rs.getString("nombreTipoHabitacion"));
            unTipoHabitacion.setDescripcionTipoHabitacion(rs.getString("descripcionTipoHabitacion"));

            losTiposHabitacion.add(unTipoHabitacion);

            System.out.println("Encontrado: "
                    + unTipoHabitacion.getIdtipoHabitacion());
        }

        System.out.println("Total encontrados: " + contador);

    } catch (SQLException ex) {

        System.err.println("Error al buscar: " + ex.getMessage());
    }

    return losTiposHabitacion.iterator();
}

public TipoHabitacion buscarPorId(int elId) {

    TipoHabitacion unTipoHabitacion = new TipoHabitacion();
    unTipoHabitacion.setNombreTipoHabitacion("TIPO HABITACION no existe");

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM TipoHabitacion WHERE idtipoHabitacion=?");

        sql.setInt(1, elId);

        ResultSet rs = sql.executeQuery();

        while (rs.next()) {

            unTipoHabitacion.setIdtipoHabitacion(rs.getInt("idtipoHabitacion"));
            unTipoHabitacion.setNombreTipoHabitacion(rs.getString("nombreTipoHabitacion"));
            unTipoHabitacion.setDescripcionTipoHabitacion(rs.getString("descripcionTipoHabitacion"));
        }

    } catch (SQLException ex) {
        System.err.println("Error al buscar por ID: "
                + ex.getMessage());
    }

    return unTipoHabitacion;
}
    
    
    
    
}
