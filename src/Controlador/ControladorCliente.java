/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.Cliente;
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
        
        Iterator<Cliente> clientes = listarClientes();
        while (clientes.hasNext()) {
            Cliente c = clientes.next();
            if (c.getNombre() != null && !c.getNombre().equals("No hay nada registrado")) {
                model.addRow(new Object[]{
                    c.getIdCliente(),
                    c.getNombre(),
                    c.getApellido(),
                    c.getDocumento(),
                    c.getCorreo(),
                    c.getTelefono(),
                    c.getDireccion(),
                    c.getIdUsuario()
                });
            }
        }
    }
}
