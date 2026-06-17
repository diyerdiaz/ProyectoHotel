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
public class Cliente {

    private int idCliente;
    private String nombre;
    private String apellido;
    private int documento;
    private String correo;
    private int telefono;
    private String direccion;
    private int idUsuario;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Cliente{" + "nombre=" + nombre + '}';
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
        final Cliente other = (Cliente) obj;
        return this.idCliente == other.idCliente;
    }

    public Iterator<Cliente> Listar() {

        ArrayList<Cliente> losClientes = new ArrayList<>();

        try {

            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM Cliente");

            ResultSet rs = sql.executeQuery();

            Cliente unCliente;

            while (rs.next()) {

                unCliente = new Cliente();

                unCliente.setIdCliente(rs.getInt("idCliente"));
                unCliente.setNombre(rs.getString("nombre"));
                unCliente.setApellido(rs.getString("apellido"));
                unCliente.setDocumento(rs.getInt("documento"));
                unCliente.setCorreo(rs.getString("correo"));
                unCliente.setTelefono(rs.getInt("telefono"));
                unCliente.setDireccion(rs.getString("direccion"));
                unCliente.setIdUsuario(rs.getInt("idUsuario"));

                losClientes.add(unCliente);
            }

        } catch (SQLException ex) {
            System.err.println("Error al listar: " + ex.getMessage());
        }

        if (losClientes.isEmpty()) {
            Cliente miCliente = new Cliente();
            miCliente.setNombre("No hay nada registrado");
            losClientes.add(miCliente);
        }

        return losClientes.iterator();
    }

    public void insertar() {

        try {

            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "INSERT INTO Cliente VALUES(NULL,?,?,?,?,?,?,?)");

            sql.setString(1, this.getNombre());
            sql.setString(2, this.getApellido());
            sql.setInt(3, this.getDocumento());
            sql.setString(4, this.getCorreo());
            sql.setInt(5, this.getTelefono());
            sql.setString(6, this.getDireccion());
            sql.setInt(7, this.getIdUsuario());

            sql.executeUpdate();

            System.out.println("Insertado correctamente");

        } catch (SQLException ex) {
            System.err.println("Error al insertar: " + ex.getMessage());
        }
    }

    public void modificar() {

        try {

            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "UPDATE Cliente "
                    + "SET nombre=?, apellido=?, documento=?, "
                    + "correo=?, telefono=?, direccion=?, idUsuario=? "
                    + "WHERE idCliente=?");

            sql.setString(1, this.getNombre());
            sql.setString(2, this.getApellido());
            sql.setInt(3, this.getDocumento());
            sql.setString(4, this.getCorreo());
            sql.setInt(5, this.getTelefono());
            sql.setString(6, this.getDireccion());
            sql.setInt(7, this.getIdUsuario());
            sql.setInt(8, this.getIdCliente());

            sql.executeUpdate();

            System.out.println("Modificado correctamente");

        } catch (SQLException ex) {
            System.err.println("Error al modificar: " + ex.getMessage());
        }
    }

    public void eliminar() {

        try {

            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "DELETE FROM Cliente WHERE idCliente=?");

            sql.setInt(1, this.getIdCliente());

            sql.executeUpdate();

            System.out.println("Eliminado correctamente");

        } catch (SQLException ex) {
            System.err.println("Error al eliminar: " + ex.getMessage());
        }
    }

    public Iterator<Cliente> buscar(String busqueda) {

        ArrayList<Cliente> losClientes = new ArrayList<>();

        try {

            System.out.println("Buscando: " + busqueda);

            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM Cliente "
                    + "WHERE idCliente LIKE ? "
                    + "OR nombre LIKE ? "
                    + "OR apellido LIKE ? "
                    + "OR documento LIKE ? "
                    + "OR correo LIKE ? "
                    + "OR telefono LIKE ? "
                    + "OR direccion LIKE ? "
                    + "OR idUsuario LIKE ?");

            sql.setString(1, "%" + busqueda + "%");
            sql.setString(2, "%" + busqueda + "%");
            sql.setString(3, "%" + busqueda + "%");
            sql.setString(4, "%" + busqueda + "%");
            sql.setString(5, "%" + busqueda + "%");
            sql.setString(6, "%" + busqueda + "%");
            sql.setString(7, "%" + busqueda + "%");
            sql.setString(8, "%" + busqueda + "%");

            ResultSet rs = sql.executeQuery();

            int contador = 0;

            while (rs.next()) {

                contador++;

                Cliente unCliente = new Cliente();

                unCliente.setIdCliente(rs.getInt("idCliente"));
                unCliente.setNombre(rs.getString("nombre"));
                unCliente.setApellido(rs.getString("apellido"));
                unCliente.setDocumento(rs.getInt("documento"));
                unCliente.setCorreo(rs.getString("correo"));
                unCliente.setTelefono(rs.getInt("telefono"));
                unCliente.setDireccion(rs.getString("direccion"));
                unCliente.setIdUsuario(rs.getInt("idUsuario"));

                losClientes.add(unCliente);

                System.out.println("Encontrado: "
                        + unCliente.getIdCliente());
            }

            System.out.println("Total encontrados: " + contador);

        } catch (SQLException ex) {

            System.err.println("Error al buscar: " + ex.getMessage());
        }

        return losClientes.iterator();
    }

    public Cliente buscarPorId(int elId) {

        Cliente unCliente = new Cliente();
        unCliente.setNombre("CLIENTE no existe");

        try {

            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM Cliente WHERE idCliente=?");

            sql.setInt(1, elId);

            ResultSet rs = sql.executeQuery();

            while (rs.next()) {

                unCliente.setIdCliente(rs.getInt("idCliente"));
                unCliente.setNombre(rs.getString("nombre"));
                unCliente.setApellido(rs.getString("apellido"));
                unCliente.setDocumento(rs.getInt("documento"));
                unCliente.setCorreo(rs.getString("correo"));
                unCliente.setTelefono(rs.getInt("telefono"));
                unCliente.setDireccion(rs.getString("direccion"));
                unCliente.setIdUsuario(rs.getInt("idUsuario"));
            }

        } catch (SQLException ex) {
            System.err.println("Error al buscar por ID: "
                    + ex.getMessage());
        }

        return unCliente;
    }

}
