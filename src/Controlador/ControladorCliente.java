/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.Cliente;
import modelo.ConexionBD;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alejo
 */
public class ControladorCliente {
    
    private Cliente modeloCliente;
    
    public ControladorCliente() {
        this.modeloCliente = new Cliente();
    }
    
    public Iterator<Cliente> listarClientes() {
        return modeloCliente.Listar();
    }
    
    public void insertarCliente(String nombre, String apellido, long documento,
                                  String correo, long telefono, String direccion, int idUsuario) {
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setDocumento(documento);
        cliente.setCorreo(correo);
        cliente.setTelefono(telefono);
        cliente.setDireccion(direccion);
        cliente.setIdUsuario(idUsuario);
        cliente.insertar();
    }
    
    public void modificarCliente(int idCliente, String nombre, String apellido, long documento,
                                  String correo, long telefono, String direccion, int idUsuario) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(idCliente);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setDocumento(documento);
        cliente.setCorreo(correo);
        cliente.setTelefono(telefono);
        cliente.setDireccion(direccion);
        cliente.setIdUsuario(idUsuario);
        cliente.modificar();
    }
    
    public void eliminarCliente(int idCliente) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(idCliente);
        cliente.eliminar();
    }
    
    public Iterator<Cliente> buscarCliente(String busqueda) {
        return modeloCliente.buscar(busqueda);
    }
    
    public Cliente buscarClientePorId(int idCliente) {
        return modeloCliente.buscarPorId(idCliente);
    }
    
    public void cargarTablaClientes(javax.swing.JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        try {
            Statement st = ConexionBD.conexion.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT c.*, u.nombreusuario FROM cliente c LEFT JOIN usuarios u ON c.idusuario = u.idusuario");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("idcliente"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getLong("documento"),
                    rs.getString("correo"),
                    rs.getLong("telefono"),
                    rs.getString("direccion"),
                    rs.getString("nombreusuario") != null ? rs.getString("nombreusuario") : "-"
                });
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.err.println("Error cargando clientes: " + e.getMessage());
        }
    }
}
