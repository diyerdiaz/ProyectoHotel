package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Cliente {

    private int idCliente;
    private String nombre;
    private String apellido;
    private long documento;
    private String correo;
    private long telefono;
    private String direccion;
    private int idUsuario;

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public long getDocumento() { return documento; }
    public void setDocumento(long documento) { this.documento = documento; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public long getTelefono() { return telefono; }
    public void setTelefono(long telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public Iterator<Cliente> Listar() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM cliente");
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getInt("idcliente"));
                    c.setNombre(rs.getString("nombre"));
                    c.setApellido(rs.getString("apellido"));
                    c.setDocumento(rs.getLong("documento"));
                    c.setCorreo(rs.getString("correo"));
                    c.setTelefono(rs.getLong("telefono"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setIdUsuario(rs.getInt("idusuario"));
                    clientes.add(c);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar: " + ex.getMessage());
        }

        if (clientes.isEmpty()) {
            Cliente c = new Cliente();
            c.setNombre("No hay nada registrado");
            clientes.add(c);
        }

        return clientes.iterator();
    }

    public void insertar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "INSERT INTO cliente (nombre, apellido, documento, correo, telefono, direccion, idusuario) VALUES (?,?,?,?,?,?,?)");
            sql.setString(1, getNombre());
            sql.setString(2, getApellido());
            sql.setLong(3, getDocumento());
            sql.setString(4, getCorreo());
            sql.setLong(5, getTelefono());
            sql.setString(6, getDireccion());
            sql.setInt(7, getIdUsuario());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al insertar: " + ex.getMessage());
        }
    }

    public void modificar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "UPDATE cliente SET nombre=?, apellido=?, documento=?, correo=?, telefono=?, direccion=?, idusuario=? WHERE idcliente=?");
            sql.setString(1, getNombre());
            sql.setString(2, getApellido());
            sql.setLong(3, getDocumento());
            sql.setString(4, getCorreo());
            sql.setLong(5, getTelefono());
            sql.setString(6, getDireccion());
            sql.setInt(7, getIdUsuario());
            sql.setInt(8, getIdCliente());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al modificar: " + ex.getMessage());
        }
    }

    public void eliminar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("DELETE FROM cliente WHERE idcliente=?");
            sql.setInt(1, getIdCliente());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar: " + ex.getMessage());
        }
    }

    public Iterator<Cliente> buscar(String busqueda) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM cliente WHERE CAST(idcliente AS TEXT) LIKE ? OR nombre LIKE ? OR apellido LIKE ? OR CAST(documento AS TEXT) LIKE ? OR correo LIKE ? OR CAST(telefono AS TEXT) LIKE ? OR direccion LIKE ? OR CAST(idusuario AS TEXT) LIKE ?");
            String like = "%" + busqueda + "%";
            for (int i = 1; i <= 8; i++) {
                sql.setString(i, like);
            }

            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getInt("idcliente"));
                    c.setNombre(rs.getString("nombre"));
                    c.setApellido(rs.getString("apellido"));
                    c.setDocumento(rs.getLong("documento"));
                    c.setCorreo(rs.getString("correo"));
                    c.setTelefono(rs.getLong("telefono"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setIdUsuario(rs.getInt("idusuario"));
                    clientes.add(c);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar: " + ex.getMessage());
        }
        return clientes.iterator();
    }

    public Cliente buscarPorId(int elId) {
        Cliente c = new Cliente();
        c.setNombre("CLIENTE no existe");

        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM cliente WHERE idcliente=?");
            sql.setInt(1, elId);
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    c.setIdCliente(rs.getInt("idcliente"));
                    c.setNombre(rs.getString("nombre"));
                    c.setApellido(rs.getString("apellido"));
                    c.setDocumento(rs.getLong("documento"));
                    c.setCorreo(rs.getString("correo"));
                    c.setTelefono(rs.getLong("telefono"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setIdUsuario(rs.getInt("idusuario"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar por ID: " + ex.getMessage());
        }

        return c;
    }
}
