/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.Reserva;
import java.util.Iterator;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alejo
 */
public class ControladorReserva {
    
    private Reserva modeloReserva;
    
    public ControladorReserva() {
        this.modeloReserva = new Reserva();
    }
    
    public Iterator<Reserva> listarReservas() {
        return modeloReserva.Listar();
    }
    
    public String insertarReserva(int idCliente, int idHabitacion, String habitacion, int personas, 
                                  Date fechaEntrada, Date fechaSalida, String medioPago) {
        if (!modeloReserva.esHabitacionDisponible(idHabitacion, fechaEntrada, fechaSalida, -1)) {
            return "La habitación no está disponible en las fechas seleccionadas.";
        }
        Reserva reserva = new Reserva();
        reserva.setIdCliente(idCliente);
        reserva.setIdHabitacion(idHabitacion);
        reserva.setHabitacion(habitacion);
        reserva.setPersonas(personas);
        reserva.setFechaEntrada(fechaEntrada);
        reserva.setFechaSalida(fechaSalida);
        reserva.setMedioPago(medioPago);
        reserva.insertar();
        new ControladorHabitaciones().cambiarEstadoHabitacion(idHabitacion, "OCUPADA");
        
        if (reserva.getIdReserva() > 0) {
            new ControladorFacturas().generarFacturaDesdeReserva(reserva.getIdReserva());
        }
        
        return "Reserva insertada con éxito.";
    }
    
    public String modificarReserva(int idReserva, int idCliente, int idHabitacion, String habitacion, int personas, 
                                  Date fechaEntrada, Date fechaSalida, String medioPago) {
        if (!modeloReserva.esHabitacionDisponible(idHabitacion, fechaEntrada, fechaSalida, idReserva)) {
            return "La habitación no está disponible en las fechas seleccionadas.";
        }
        Reserva reserva = new Reserva();
        reserva.setIdReserva(idReserva);
        reserva.setIdCliente(idCliente);
        reserva.setIdHabitacion(idHabitacion);
        reserva.setHabitacion(habitacion);
        reserva.setPersonas(personas);
        reserva.setFechaEntrada(fechaEntrada);
        reserva.setFechaSalida(fechaSalida);
        reserva.setMedioPago(medioPago);
        reserva.modificar();
        return "Reserva modificada con éxito.";
    }
    
    public void eliminarReserva(int idReserva) {
        Reserva reserva = buscarReservaPorId(idReserva);
        if (reserva != null && reserva.getIdHabitacion() > 0) {
            new ControladorHabitaciones().cambiarEstadoHabitacion(reserva.getIdHabitacion(), "DISPONIBLE");
        }
        Reserva resDel = new Reserva();
        resDel.setIdReserva(idReserva);
        resDel.eliminar();
    }
    
    public Iterator<Reserva> buscarReserva(String busqueda) {
        return modeloReserva.buscar(busqueda);
    }
    
    public Reserva buscarReservaPorId(int idReserva) {
        return modeloReserva.buscarPorId(idReserva);
    }
    
    public void cargarTablaReservas(javax.swing.JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        
        Iterator<Reserva> reservas = listarReservas();
        while (reservas.hasNext()) {
            Reserva r = reservas.next();
            if (!r.getHabitacion().equals("No hay nada registrado")) {
                model.addRow(new Object[]{
                    r.getIdReserva(),
                    r.getHabitacion(),
                    r.getPersonas(),
                    r.getFechaEntrada(),
                    r.getFechaSalida(),
                    r.getMedioPago(),
                    r.getIdCliente()
                });
            }
        }
    }
}
