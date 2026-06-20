package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Habitaciones {
    private int idHabitacion;
    private int numeroHabitacion;
    private String tipoHabitacion;
    private double precioHabitacion;
    private String estadoHbitacion;

    public int getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
    public int getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(int numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }
    public String getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(String tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }
    public double getPrecioHabitacion() { return precioHabitacion; }
    public void setPrecioHabitacion(double precioHabitacion) { this.precioHabitacion = precioHabitacion; }
    public String getEstadoHbitacion() { return estadoHbitacion; }
    public void setEstadoHbitacion(String estadoHbitacion) { this.estadoHbitacion = estadoHbitacion; }

    public Iterator<Habitaciones> Listar() {
        ArrayList<Habitaciones> habitaciones = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM habitaciones");
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    Habitaciones h = new Habitaciones();
                    h.setIdHabitacion(rs.getInt("idhabitacion"));
                    h.setNumeroHabitacion(rs.getInt("numerohabitacion"));
                    h.setTipoHabitacion(rs.getString("tipohabitacion"));
                    h.setPrecioHabitacion(rs.getDouble("preciohabitacion"));
                    h.setEstadoHbitacion(rs.getString("estadohabitacion"));
                    habitaciones.add(h);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar: " + ex.getMessage());
        }

        if (habitaciones.isEmpty()) {
            Habitaciones miHabitacion = new Habitaciones();
            miHabitacion.setTipoHabitacion("No hay nada registrado");
            habitaciones.add(miHabitacion);
        }
        return habitaciones.iterator();
    }

    public void insertar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "INSERT INTO habitaciones (numerohabitacion, tipohabitacion, preciohabitacion, estadohabitacion) VALUES (?,?,?,?)");
            sql.setInt(1, getNumeroHabitacion());
            sql.setString(2, getTipoHabitacion());
            sql.setDouble(3, getPrecioHabitacion());
            sql.setString(4, getEstadoHbitacion());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al insertar: " + ex.getMessage());
        }
    }

    public void modificar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "UPDATE habitaciones SET numerohabitacion=?, tipohabitacion=?, preciohabitacion=?, estadohabitacion=? WHERE idhabitacion=?");
            sql.setInt(1, getNumeroHabitacion());
            sql.setString(2, getTipoHabitacion());
            sql.setDouble(3, getPrecioHabitacion());
            sql.setString(4, getEstadoHbitacion());
            sql.setInt(5, getIdHabitacion());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al modificar: " + ex.getMessage());
        }
    }

    public void eliminar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("DELETE FROM habitaciones WHERE idhabitacion=?");
            sql.setInt(1, getIdHabitacion());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar: " + ex.getMessage());
        }
    }

    public Iterator<Habitaciones> buscar(String busqueda) {
        ArrayList<Habitaciones> habitaciones = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM habitaciones WHERE CAST(idhabitacion AS TEXT) LIKE ? OR CAST(numerohabitacion AS TEXT) LIKE ? OR tipohabitacion LIKE ? OR CAST(preciohabitacion AS TEXT) LIKE ? OR estadohabitacion LIKE ?");
            String like = "%" + busqueda + "%";
            for (int i = 1; i <= 5; i++) {
                sql.setString(i, like);
            }

            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    Habitaciones h = new Habitaciones();
                    h.setIdHabitacion(rs.getInt("idhabitacion"));
                    h.setNumeroHabitacion(rs.getInt("numerohabitacion"));
                    h.setTipoHabitacion(rs.getString("tipohabitacion"));
                    h.setPrecioHabitacion(rs.getDouble("preciohabitacion"));
                    h.setEstadoHbitacion(rs.getString("estadohabitacion"));
                    habitaciones.add(h);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar: " + ex.getMessage());
        }
        return habitaciones.iterator();
    }

    public Habitaciones buscarPorId(int elId) {
        Habitaciones h = new Habitaciones();
        h.setTipoHabitacion("HABITACION no existe");

        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM habitaciones WHERE idhabitacion=?");
            sql.setInt(1, elId);
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    h.setIdHabitacion(rs.getInt("idhabitacion"));
                    h.setNumeroHabitacion(rs.getInt("numerohabitacion"));
                    h.setTipoHabitacion(rs.getString("tipohabitacion"));
                    h.setPrecioHabitacion(rs.getDouble("preciohabitacion"));
                    h.setEstadoHbitacion(rs.getString("estadohabitacion"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar por ID: " + ex.getMessage());
        }
        return h;
    }
}
