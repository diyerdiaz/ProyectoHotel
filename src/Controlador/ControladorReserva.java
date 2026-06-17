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
    
    public void insertarReserva(String habitacion, int personas, 
                                  Date fechaEntrada, Date fechaSalida, String medioPago) {
        Reserva reserva = new Reserva();
        reserva.setHabitacion(habitacion);
        reserva.setPersonas(personas);
        reserva.setFechaEntrada(fechaEntrada);
        reserva.setFechaSalida(fechaSalida);
        reserva.setMedioPago(medioPago);
        reserva.insertar();
    }
    
    public void modificarReserva(int idReserva, String habitacion, int personas, 
                                  Date fechaEntrada, Date fechaSalida, String medioPago) {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(idReserva);
        reserva.setHabitacion(habitacion);
        reserva.setPersonas(personas);
        reserva.setFechaEntrada(fechaEntrada);
        reserva.setFechaSalida(fechaSalida);
        reserva.setMedioPago(medioPago);
        reserva.modificar();
    }
    
    public void eliminarReserva(int idReserva) {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(idReserva);
        reserva.eliminar();
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
                    r.getMedioPago()
                });
            }
        }
    }
}
