package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class TipoHabitacion {
    private int idtipoHabitacion;
    private String nombreTipoHabitacion;
    private String descripcionTipoHabitacion;

    public int getIdtipoHabitacion() { return idtipoHabitacion; }
    public void setIdtipoHabitacion(int idtipoHabitacion) { this.idtipoHabitacion = idtipoHabitacion; }
    public String getNombreTipoHabitacion() { return nombreTipoHabitacion; }
    public void setNombreTipoHabitacion(String nombreTipoHabitacion) { this.nombreTipoHabitacion = nombreTipoHabitacion; }
    public String getDescripcionTipoHabitacion() { return descripcionTipoHabitacion; }
    public void setDescripcionTipoHabitacion(String descripcionTipoHabitacion) { this.descripcionTipoHabitacion = descripcionTipoHabitacion; }

    public Iterator<TipoHabitacion> Listar() {
        ArrayList<TipoHabitacion> tipos = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM tipohabitacion");
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    TipoHabitacion t = new TipoHabitacion();
                    t.setIdtipoHabitacion(rs.getInt("idtipohabitacion"));
                    t.setNombreTipoHabitacion(rs.getString("nombretipohabitacion"));
                    t.setDescripcionTipoHabitacion(rs.getString("descripciontipohabitacion"));
                    tipos.add(t);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar: " + ex.getMessage());
        }
        if (tipos.isEmpty()) {
            TipoHabitacion t = new TipoHabitacion();
            t.setNombreTipoHabitacion("No hay nada registrado");
            tipos.add(t);
        }
        return tipos.iterator();
    }

    public void insertar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "INSERT INTO tipohabitacion (nombretipohabitacion, descripciontipohabitacion) VALUES (?,?)");
            sql.setString(1, getNombreTipoHabitacion());
            sql.setString(2, getDescripcionTipoHabitacion());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al insertar: " + ex.getMessage());
        }
    }

    public void modificar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "UPDATE tipohabitacion SET nombretipohabitacion=?, descripciontipohabitacion=? WHERE idtipohabitacion=?");
            sql.setString(1, getNombreTipoHabitacion());
            sql.setString(2, getDescripcionTipoHabitacion());
            sql.setInt(3, getIdtipoHabitacion());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al modificar: " + ex.getMessage());
        }
    }

    public void eliminar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("DELETE FROM tipohabitacion WHERE idtipohabitacion=?");
            sql.setInt(1, getIdtipoHabitacion());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar: " + ex.getMessage());
        }
    }

    public Iterator<TipoHabitacion> buscar(String busqueda) {
        ArrayList<TipoHabitacion> tipos = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM tipohabitacion WHERE CAST(idtipohabitacion AS TEXT) LIKE ? OR nombretipohabitacion LIKE ? OR descripciontipohabitacion LIKE ?");
            String like = "%" + busqueda + "%";
            sql.setString(1, like);
            sql.setString(2, like);
            sql.setString(3, like);
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    TipoHabitacion t = new TipoHabitacion();
                    t.setIdtipoHabitacion(rs.getInt("idtipohabitacion"));
                    t.setNombreTipoHabitacion(rs.getString("nombretipohabitacion"));
                    t.setDescripcionTipoHabitacion(rs.getString("descripciontipohabitacion"));
                    tipos.add(t);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar: " + ex.getMessage());
        }
        return tipos.iterator();
    }

    public TipoHabitacion buscarPorId(int elId) {
        TipoHabitacion t = new TipoHabitacion();
        t.setNombreTipoHabitacion("TIPO HABITACION no existe");
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM tipohabitacion WHERE idtipohabitacion=?");
            sql.setInt(1, elId);
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    t.setIdtipoHabitacion(rs.getInt("idtipohabitacion"));
                    t.setNombreTipoHabitacion(rs.getString("nombretipohabitacion"));
                    t.setDescripcionTipoHabitacion(rs.getString("descripciontipohabitacion"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar por ID: " + ex.getMessage());
        }
        return t;
    }
}
