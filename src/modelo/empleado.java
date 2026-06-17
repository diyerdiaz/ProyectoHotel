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
import java.util.Date;

/**
 *
 * @author diyer
 */
public class empleado {
    
    private int idEmpleado;
    private String nombre;
    private String apellido;
    private int documento;
    private String cargo;
    private double salario;
    private Date fechaContratacion;
    private String telefono;
    private String correo;
    private String direccion;
    private int idUsuario;

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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
        return "empleado{" + "nombre=" + nombre + ", cargo=" + cargo + '}';
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
        final empleado other = (empleado) obj;
        return this.idEmpleado == other.idEmpleado;
    }

    public Iterator<empleado> Listar() {
        ArrayList<empleado> losEmpleados = new ArrayList<>();

        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM Empleado");

            ResultSet rs = sql.executeQuery();

            empleado unEmpleado;

            while (rs.next()) {
                unEmpleado = new empleado();
                unEmpleado.setIdEmpleado(rs.getInt("idEmpleado"));
                unEmpleado.setNombre(rs.getString("nombre"));
                unEmpleado.setApellido(rs.getString("apellido"));
                unEmpleado.setDocumento(rs.getInt("documento"));
                unEmpleado.setCargo(rs.getString("cargo"));
                unEmpleado.setSalario(rs.getDouble("salario"));
                unEmpleado.setFechaContratacion(rs.getDate("fechaContratacion"));
                unEmpleado.setTelefono(rs.getString("telefono"));
                unEmpleado.setCorreo(rs.getString("correo"));
                unEmpleado.setDireccion(rs.getString("direccion"));
                unEmpleado.setIdUsuario(rs.getInt("idUsuario"));

                losEmpleados.add(unEmpleado);
            }

        } catch (SQLException ex) {
            System.err.println("Error al listar: " + ex.getMessage());
        }

        if (losEmpleados.isEmpty()) {
            empleado miEmpleado = new empleado();
            miEmpleado.setNombre("No hay nada registrado");
            losEmpleados.add(miEmpleado);
        }

        return losEmpleados.iterator();
    }

    public void insertar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "INSERT INTO Empleado VALUES(NULL,?,?,?,?,?,?,?,?,?,?)");

            sql.setString(1, this.getNombre());
            sql.setString(2, this.getApellido());
            sql.setInt(3, this.getDocumento());
            sql.setString(4, this.getCargo());
            sql.setDouble(5, this.getSalario());
            sql.setDate(6, new java.sql.Date(this.getFechaContratacion().getTime()));
            sql.setString(7, this.getTelefono());
            sql.setString(8, this.getCorreo());
            sql.setString(9, this.getDireccion());
            sql.setInt(10, this.getIdUsuario());

            sql.executeUpdate();

            System.out.println("Insertado correctamente");

        } catch (SQLException ex) {
            System.err.println("Error al insertar: " + ex.getMessage());
        }
    }

    public void modificar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "UPDATE Empleado "
                    + "SET nombre=?, apellido=?, documento=?, cargo=?, salario=?, "
                    + "fechaContratacion=?, telefono=?, correo=?, direccion=?, idUsuario=? "
                    + "WHERE idEmpleado=?");

            sql.setString(1, this.getNombre());
            sql.setString(2, this.getApellido());
            sql.setInt(3, this.getDocumento());
            sql.setString(4, this.getCargo());
            sql.setDouble(5, this.getSalario());
            sql.setDate(6, new java.sql.Date(this.getFechaContratacion().getTime()));
            sql.setString(7, this.getTelefono());
            sql.setString(8, this.getCorreo());
            sql.setString(9, this.getDireccion());
            sql.setInt(10, this.getIdUsuario());
            sql.setInt(11, this.getIdEmpleado());

            sql.executeUpdate();

            System.out.println("Modificado correctamente");

        } catch (SQLException ex) {
            System.err.println("Error al modificar: " + ex.getMessage());
        }
    }

    public void eliminar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "DELETE FROM Empleado WHERE idEmpleado=?");

            sql.setInt(1, this.getIdEmpleado());

            sql.executeUpdate();

            System.out.println("Eliminado correctamente");

        } catch (SQLException ex) {
            System.err.println("Error al eliminar: " + ex.getMessage());
        }
    }

    public Iterator<empleado> buscar(String busqueda) {
        ArrayList<empleado> losEmpleados = new ArrayList<>();

        try {
            System.out.println("Buscando: " + busqueda);

            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM Empleado "
                    + "WHERE idEmpleado LIKE ? "
                    + "OR nombre LIKE ? "
                    + "OR apellido LIKE ? "
                    + "OR documento LIKE ? "
                    + "OR cargo LIKE ? "
                    + "OR salario LIKE ? "
                    + "OR fechaContratacion LIKE ? "
                    + "OR telefono LIKE ? "
                    + "OR correo LIKE ? "
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
            sql.setString(9, "%" + busqueda + "%");
            sql.setString(10, "%" + busqueda + "%");
            sql.setString(11, "%" + busqueda + "%");

            ResultSet rs = sql.executeQuery();

            int contador = 0;

            while (rs.next()) {
                contador++;

                empleado unEmpleado = new empleado();
                unEmpleado.setIdEmpleado(rs.getInt("idEmpleado"));
                unEmpleado.setNombre(rs.getString("nombre"));
                unEmpleado.setApellido(rs.getString("apellido"));
                unEmpleado.setDocumento(rs.getInt("documento"));
                unEmpleado.setCargo(rs.getString("cargo"));
                unEmpleado.setSalario(rs.getDouble("salario"));
                unEmpleado.setFechaContratacion(rs.getDate("fechaContratacion"));
                unEmpleado.setTelefono(rs.getString("telefono"));
                unEmpleado.setCorreo(rs.getString("correo"));
                unEmpleado.setDireccion(rs.getString("direccion"));
                unEmpleado.setIdUsuario(rs.getInt("idUsuario"));

                losEmpleados.add(unEmpleado);

                System.out.println("Encontrado: " + unEmpleado.getIdEmpleado());
            }

            System.out.println("Total encontrados: " + contador);

        } catch (SQLException ex) {
            System.err.println("Error al buscar: " + ex.getMessage());
        }

        return losEmpleados.iterator();
    }

    public empleado buscarPorId(int elId) {
        empleado unEmpleado = new empleado();
        unEmpleado.setNombre("EMPLEADO no existe");

        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM Empleado WHERE idEmpleado=?");

            sql.setInt(1, elId);

            ResultSet rs = sql.executeQuery();

            while (rs.next()) {
                unEmpleado.setIdEmpleado(rs.getInt("idEmpleado"));
                unEmpleado.setNombre(rs.getString("nombre"));
                unEmpleado.setApellido(rs.getString("apellido"));
                unEmpleado.setDocumento(rs.getInt("documento"));
                unEmpleado.setCargo(rs.getString("cargo"));
                unEmpleado.setSalario(rs.getDouble("salario"));
                unEmpleado.setFechaContratacion(rs.getDate("fechaContratacion"));
                unEmpleado.setTelefono(rs.getString("telefono"));
                unEmpleado.setCorreo(rs.getString("correo"));
                unEmpleado.setDireccion(rs.getString("direccion"));
                unEmpleado.setIdUsuario(rs.getInt("idUsuario"));
            }

        } catch (SQLException ex) {
            System.err.println("Error al buscar por ID: " + ex.getMessage());
        }

        return unEmpleado;
    }
}
