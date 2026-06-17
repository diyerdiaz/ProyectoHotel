/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.empleado;
import java.util.Iterator;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alejo
 */
public class ControladorEmpleado {
    
    private empleado modeloEmpleado;
    
    public ControladorEmpleado() {
        this.modeloEmpleado = new empleado();
    }
    
    public Iterator<empleado> listarEmpleados() {
        return modeloEmpleado.Listar();
    }
    
    public void insertarEmpleado(String nombre, String apellido, int documento, 
                                   String cargo, double salario, Date fechaContratacion,
                                   String telefono, String correo, String direccion, int idUsuario) {
        empleado emp = new empleado();
        emp.setNombre(nombre);
        emp.setApellido(apellido);
        emp.setDocumento(documento);
        emp.setCargo(cargo);
        emp.setSalario(salario);
        emp.setFechaContratacion(fechaContratacion);
        emp.setTelefono(telefono);
        emp.setCorreo(correo);
        emp.setDireccion(direccion);
        emp.setIdUsuario(idUsuario);
        emp.insertar();
    }
    
    public void modificarEmpleado(int idEmpleado, String nombre, String apellido, int documento, 
                                   String cargo, double salario, Date fechaContratacion,
                                   String telefono, String correo, String direccion, int idUsuario) {
        empleado emp = new empleado();
        emp.setIdEmpleado(idEmpleado);
        emp.setNombre(nombre);
        emp.setApellido(apellido);
        emp.setDocumento(documento);
        emp.setCargo(cargo);
        emp.setSalario(salario);
        emp.setFechaContratacion(fechaContratacion);
        emp.setTelefono(telefono);
        emp.setCorreo(correo);
        emp.setDireccion(direccion);
        emp.setIdUsuario(idUsuario);
        emp.modificar();
    }
    
    public void eliminarEmpleado(int idEmpleado) {
        empleado emp = new empleado();
        emp.setIdEmpleado(idEmpleado);
        emp.eliminar();
    }
    
    public Iterator<empleado> buscarEmpleado(String busqueda) {
        return modeloEmpleado.buscar(busqueda);
    }
    
    public empleado buscarEmpleadoPorId(int idEmpleado) {
        return modeloEmpleado.buscarPorId(idEmpleado);
    }
    
    public void cargarTablaEmpleados(javax.swing.JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        
        Iterator<empleado> empleados = listarEmpleados();
        while (empleados.hasNext()) {
            empleado e = empleados.next();
            if (!e.getNombre().equals("No hay nada registrado")) {
                model.addRow(new Object[]{
                    e.getIdEmpleado(),
                    e.getNombre(),
                    e.getApellido(),
                    e.getDocumento(),
                    e.getCargo(),
                    e.getSalario(),
                    e.getFechaContratacion(),
                    e.getTelefono(),
                    e.getCorreo(),
                    e.getDireccion(),
                    e.getIdUsuario()
                });
            }
        }
    }
}
