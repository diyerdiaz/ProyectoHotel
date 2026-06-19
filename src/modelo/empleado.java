package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public int getDocumento() { return documento; }
    public void setDocumento(int documento) { this.documento = documento; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }
    public Date getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(Date fechaContratacion) { this.fechaContratacion = fechaContratacion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public Iterator<empleado> Listar() {
        ArrayList<empleado> empleados = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM empleado");
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    empleado e = new empleado();
                    e.setIdEmpleado(rs.getInt("idempleado"));
                    e.setNombre(rs.getString("nombre"));
                    e.setApellido(rs.getString("apellido"));
                    e.setDocumento(rs.getInt("documento"));
                    e.setCargo(rs.getString("cargo"));
                    e.setSalario(rs.getDouble("salario"));
                    e.setFechaContratacion(rs.getDate("fechacontratacion"));
                    e.setTelefono(rs.getString("telefono"));
                    e.setCorreo(rs.getString("correo"));
                    e.setDireccion(rs.getString("direccion"));
                    e.setIdUsuario(rs.getInt("idusuario"));
                    empleados.add(e);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar: " + ex.getMessage());
        }
        if (empleados.isEmpty()) {
            empleado e = new empleado();
            e.setNombre("No hay nada registrado");
            empleados.add(e);
        }
        return empleados.iterator();
    }

    public void insertar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "INSERT INTO empleado (nombre, apellido, documento, cargo, salario, fechacontratacion, telefono, correo, direccion, idusuario) VALUES (?,?,?,?,?,?,?,?,?,?)");
            sql.setString(1, getNombre());
            sql.setString(2, getApellido());
            sql.setInt(3, getDocumento());
            sql.setString(4, getCargo());
            sql.setDouble(5, getSalario());
            sql.setDate(6, new java.sql.Date(getFechaContratacion().getTime()));
            sql.setString(7, getTelefono());
            sql.setString(8, getCorreo());
            sql.setString(9, getDireccion());
            sql.setInt(10, getIdUsuario());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al insertar: " + ex.getMessage());
        }
    }

    public void modificar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "UPDATE empleado SET nombre=?, apellido=?, documento=?, cargo=?, salario=?, fechacontratacion=?, telefono=?, correo=?, direccion=?, idusuario=? WHERE idempleado=?");
            sql.setString(1, getNombre());
            sql.setString(2, getApellido());
            sql.setInt(3, getDocumento());
            sql.setString(4, getCargo());
            sql.setDouble(5, getSalario());
            sql.setDate(6, new java.sql.Date(getFechaContratacion().getTime()));
            sql.setString(7, getTelefono());
            sql.setString(8, getCorreo());
            sql.setString(9, getDireccion());
            sql.setInt(10, getIdUsuario());
            sql.setInt(11, getIdEmpleado());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al modificar: " + ex.getMessage());
        }
    }

    public void eliminar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("DELETE FROM empleado WHERE idempleado=?");
            sql.setInt(1, getIdEmpleado());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar: " + ex.getMessage());
        }
    }

    public Iterator<empleado> buscar(String busqueda) {
        ArrayList<empleado> empleados = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM empleado WHERE CAST(idempleado AS TEXT) LIKE ? OR nombre LIKE ? OR apellido LIKE ? OR CAST(documento AS TEXT) LIKE ? OR cargo LIKE ? OR CAST(salario AS TEXT) LIKE ? OR CAST(fechacontratacion AS TEXT) LIKE ? OR telefono LIKE ? OR correo LIKE ? OR direccion LIKE ? OR CAST(idusuario AS TEXT) LIKE ?");
            String like = "%" + busqueda + "%";
            for (int i = 1; i <= 11; i++) {
                sql.setString(i, like);
            }

            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    empleado e = new empleado();
                    e.setIdEmpleado(rs.getInt("idempleado"));
                    e.setNombre(rs.getString("nombre"));
                    e.setApellido(rs.getString("apellido"));
                    e.setDocumento(rs.getInt("documento"));
                    e.setCargo(rs.getString("cargo"));
                    e.setSalario(rs.getDouble("salario"));
                    e.setFechaContratacion(rs.getDate("fechacontratacion"));
                    e.setTelefono(rs.getString("telefono"));
                    e.setCorreo(rs.getString("correo"));
                    e.setDireccion(rs.getString("direccion"));
                    e.setIdUsuario(rs.getInt("idusuario"));
                    empleados.add(e);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar: " + ex.getMessage());
        }
        return empleados.iterator();
    }

    public empleado buscarPorId(int elId) {
        empleado e = new empleado();
        e.setNombre("EMPLEADO no existe");
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM empleado WHERE idempleado=?");
            sql.setInt(1, elId);
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    e.setIdEmpleado(rs.getInt("idempleado"));
                    e.setNombre(rs.getString("nombre"));
                    e.setApellido(rs.getString("apellido"));
                    e.setDocumento(rs.getInt("documento"));
                    e.setCargo(rs.getString("cargo"));
                    e.setSalario(rs.getDouble("salario"));
                    e.setFechaContratacion(rs.getDate("fechacontratacion"));
                    e.setTelefono(rs.getString("telefono"));
                    e.setCorreo(rs.getString("correo"));
                    e.setDireccion(rs.getString("direccion"));
                    e.setIdUsuario(rs.getInt("idusuario"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar por ID: " + ex.getMessage());
        }
        return e;
    }
}
