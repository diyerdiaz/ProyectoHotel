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
public class Habitaciones {
    private  int  idHabitacion;
    private  int numeroHabitacion;
    private  String tipoHabitacion;
    private  double precioHabitacion;
    private  String estadoHbitacion; 

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public int getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(int numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public double getPrecioHabitacion() {
        return precioHabitacion;
    }

    public void setPrecioHabitacion(double precioHabitacion) {
        this.precioHabitacion = precioHabitacion;
    }

    public String getEstadoHbitacion() {
        return estadoHbitacion;
    }

    public void setEstadoHbitacion(String estadoHbitacion) {
        this.estadoHbitacion = estadoHbitacion;
    }

    @Override
    public String toString() {
        return "Habitaciones{" + "numeroHabitacion=" + numeroHabitacion + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Habitaciones other = (Habitaciones) obj;
        return this.idHabitacion == other.idHabitacion;
    }
    
    public Iterator<Habitaciones> Listar() {

    ArrayList<Habitaciones> lasHabitaciones = new ArrayList<>();

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM Habitaciones");

        ResultSet rs = sql.executeQuery();

        Habitaciones unaHabitacion;

        while (rs.next()) {

            unaHabitacion = new Habitaciones();

            unaHabitacion.setIdHabitacion(rs.getInt("idHabitacion"));
            unaHabitacion.setNumeroHabitacion(rs.getInt("numeroHabitacion"));
            unaHabitacion.setTipoHabitacion(rs.getString("tipoHabitacion"));
            unaHabitacion.setPrecioHabitacion(rs.getDouble("precioHabitacion"));
            unaHabitacion.setEstadoHbitacion(rs.getString("estadoHabitacion"));

            lasHabitaciones.add(unaHabitacion);
        }

    } catch (SQLException ex) {
        System.err.println("Error al listar: " + ex.getMessage());
    }

    if (lasHabitaciones.isEmpty()) {
        Habitaciones miHabitacion = new Habitaciones();
        miHabitacion.setTipoHabitacion("No hay nada registrado");
        lasHabitaciones.add(miHabitacion);
    }

    return lasHabitaciones.iterator();
}

public void insertar() {

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "INSERT INTO Habitaciones VALUES(NULL,?,?,?,?)");

        sql.setInt(1, this.getNumeroHabitacion());
        sql.setString(2, this.getTipoHabitacion());
        sql.setDouble(3, this.getPrecioHabitacion());
        sql.setString(4, this.getEstadoHbitacion());

        sql.executeUpdate();

        System.out.println("Insertado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al insertar: " + ex.getMessage());
    }
}

public void modificar() {

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "UPDATE Habitaciones "
                + "SET numeroHabitacion=?, tipoHabitacion=?, "
                + "precioHabitacion=?, estadoHabitacion=? "
                + "WHERE idHabitacion=?");

        sql.setInt(1, this.getNumeroHabitacion());
        sql.setString(2, this.getTipoHabitacion());
        sql.setDouble(3, this.getPrecioHabitacion());
        sql.setString(4, this.getEstadoHbitacion());
        sql.setInt(5, this.getIdHabitacion());

        sql.executeUpdate();

        System.out.println("Modificado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al modificar: " + ex.getMessage());
    }
}

public void eliminar() {

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "DELETE FROM Habitaciones WHERE idHabitacion=?");

        sql.setInt(1, this.getIdHabitacion());

        sql.executeUpdate();

        System.out.println("Eliminado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al eliminar: " + ex.getMessage());
    }
}

public Iterator<Habitaciones> buscar(String busqueda) {

    ArrayList<Habitaciones> lasHabitaciones = new ArrayList<>();

    try {

        System.out.println("Buscando: " + busqueda);

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM Habitaciones "
                + "WHERE idHabitacion LIKE ? "
                + "OR numeroHabitacion LIKE ? "
                + "OR tipoHabitacion LIKE ? "
                + "OR precioHabitacion LIKE ? "
                + "OR estadoHabitacion LIKE ?");

        sql.setString(1, "%" + busqueda + "%");
        sql.setString(2, "%" + busqueda + "%");
        sql.setString(3, "%" + busqueda + "%");
        sql.setString(4, "%" + busqueda + "%");
        sql.setString(5, "%" + busqueda + "%");

        ResultSet rs = sql.executeQuery();

        int contador = 0;

        while (rs.next()) {

            contador++;

            Habitaciones unaHabitacion = new Habitaciones();

            unaHabitacion.setIdHabitacion(rs.getInt("idHabitacion"));
            unaHabitacion.setNumeroHabitacion(rs.getInt("numeroHabitacion"));
            unaHabitacion.setTipoHabitacion(rs.getString("tipoHabitacion"));
            unaHabitacion.setPrecioHabitacion(rs.getDouble("precioHabitacion"));
            unaHabitacion.setEstadoHbitacion(rs.getString("estadoHabitacion"));

            lasHabitaciones.add(unaHabitacion);

            System.out.println("Encontrado: "
                    + unaHabitacion.getIdHabitacion());
        }

        System.out.println("Total encontrados: " + contador);

    } catch (SQLException ex) {

        System.err.println("Error al buscar: " + ex.getMessage());
    }

    return lasHabitaciones.iterator();
}

public Habitaciones buscarPorId(int elId) {

    Habitaciones unaHabitacion = new Habitaciones();
    unaHabitacion.setTipoHabitacion("HABITACION no existe");

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM Habitaciones WHERE idHabitacion=?");

        sql.setInt(1, elId);

        ResultSet rs = sql.executeQuery();

        while (rs.next()) {

            unaHabitacion.setIdHabitacion(rs.getInt("idHabitacion"));
            unaHabitacion.setNumeroHabitacion(rs.getInt("numeroHabitacion"));
            unaHabitacion.setTipoHabitacion(rs.getString("tipoHabitacion"));
            unaHabitacion.setPrecioHabitacion(rs.getDouble("precioHabitacion"));
            unaHabitacion.setEstadoHbitacion(rs.getString("estadoHabitacion"));
        }

    } catch (SQLException ex) {
        System.err.println("Error al buscar por ID: "
                + ex.getMessage());
    }

    return unaHabitacion;
}
    
    
    
}
