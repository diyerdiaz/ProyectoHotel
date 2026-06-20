/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.Habitaciones;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alejo
 */
public class ControladorHabitaciones {
    
    private Habitaciones modeloHabitaciones;
    
    public ControladorHabitaciones() {
        this.modeloHabitaciones = new Habitaciones();
    }
    
    public Iterator<Habitaciones> listarHabitaciones() {
        return modeloHabitaciones.Listar();
    }
    
    public void insertarHabitacion(int numeroHabitacion, String tipoHabitacion, 
                                     double precioHabitacion, String estadoHabitacion) {
        Habitaciones habitacion = new Habitaciones();
        habitacion.setNumeroHabitacion(numeroHabitacion);
        habitacion.setTipoHabitacion(tipoHabitacion);
        habitacion.setPrecioHabitacion(precioHabitacion);
        habitacion.setEstadoHbitacion(estadoHabitacion);
        habitacion.insertar();
    }
    
    public void modificarHabitacion(int idHabitacion, int numeroHabitacion, String tipoHabitacion, 
                                     double precioHabitacion, String estadoHabitacion) {
        Habitaciones habitacion = new Habitaciones();
        habitacion.setIdHabitacion(idHabitacion);
        habitacion.setNumeroHabitacion(numeroHabitacion);
        habitacion.setTipoHabitacion(tipoHabitacion);
        habitacion.setPrecioHabitacion(precioHabitacion);
        habitacion.setEstadoHbitacion(estadoHabitacion);
        habitacion.modificar();
    }
    
    public void eliminarHabitacion(int idHabitacion) {
        Habitaciones habitacion = new Habitaciones();
        habitacion.setIdHabitacion(idHabitacion);
        habitacion.eliminar();
    }
    
    public Iterator<Habitaciones> buscarHabitacion(String busqueda) {
        return modeloHabitaciones.buscar(busqueda);
    }
    
    public Habitaciones buscarHabitacionPorId(int idHabitacion) {
        return modeloHabitaciones.buscarPorId(idHabitacion);
    }
    
    public void cargarTablaHabitaciones(javax.swing.JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        
        Iterator<Habitaciones> habitaciones = listarHabitaciones();
        while (habitaciones.hasNext()) {
            Habitaciones h = habitaciones.next();
            if (!h.getTipoHabitacion().equals("No hay nada registrado")) {
                model.addRow(new Object[]{
                    h.getIdHabitacion(),
                    h.getNumeroHabitacion(),
                    h.getTipoHabitacion(),
                    h.getPrecioHabitacion(),
                    h.getEstadoHbitacion()
                });
            }
        }
    }
}
