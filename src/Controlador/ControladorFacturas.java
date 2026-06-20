/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.facturas;
import modelo.Reserva;
import modelo.Habitaciones;
import java.util.Iterator;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alejo
 */
public class ControladorFacturas {
    
    private facturas modeloFacturas;
    
    public ControladorFacturas() {
        this.modeloFacturas = new facturas();
    }
    
    public Iterator<facturas> listarFacturas() {
        return modeloFacturas.Listar();
    }
    
    public void insertarFactura(int idReserva, Date fechaFactura, 
                                  double totalFactura, String estadoFactura, String metodoPago) {
        facturas factura = new facturas();
        factura.setIdReserva(idReserva);
        factura.setFechaFactura(fechaFactura);
        factura.setTotalFactura(totalFactura);
        factura.setEstadoFactura(estadoFactura);
        factura.setMetodoPago(metodoPago);
        factura.insertar();
    }
    
    public void modificarFactura(int idFactura, int idReserva, Date fechaFactura, 
                                  double totalFactura, String estadoFactura, String metodoPago) {
        facturas factura = new facturas();
        factura.setIdFactura(idFactura);
        factura.setIdReserva(idReserva);
        factura.setFechaFactura(fechaFactura);
        factura.setTotalFactura(totalFactura);
        factura.setEstadoFactura(estadoFactura);
        factura.setMetodoPago(metodoPago);
        factura.modificar();
    }
    
    public void eliminarFactura(int idFactura) {
        facturas factura = new facturas();
        factura.setIdFactura(idFactura);
        factura.eliminar();
    }
    
    public Iterator<facturas> buscarFactura(String busqueda) {
        return modeloFacturas.buscar(busqueda);
    }
    
    public facturas buscarFacturaPorId(int idFactura) {
        return modeloFacturas.buscarPorId(idFactura);
    }
    
    public void cargarTablaFacturas(javax.swing.JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        
        Iterator<facturas> facturas = listarFacturas();
        while (facturas.hasNext()) {
            facturas f = facturas.next();
            if (f.getEstadoFactura() != null && !f.getEstadoFactura().equals("No hay nada registrado")) {
                model.addRow(new Object[]{
                    f.getIdFactura(),
                    f.getIdReserva(),
                    f.getFechaFactura(),
                    f.getTotalFactura(),
                    f.getEstadoFactura(),
                    f.getMetodoPago()
                });
            }
        }
    }
    
    // Métodos específicos para gestión de estados
    public void cambiarEstadoFactura(int idFactura, String nuevoEstado) {
        if (modeloFacturas.esEstadoValido(nuevoEstado)) {
            modeloFacturas.cambiarEstado(idFactura, nuevoEstado);
        } else {
            System.err.println("Estado no válido: " + nuevoEstado);
        }
    }
    
    public Iterator<facturas> buscarFacturasPorEstado(String estado) {
        return modeloFacturas.buscarPorEstado(estado);
    }
    
    public boolean validarEstado(String estado) {
        return modeloFacturas.esEstadoValido(estado);
    }
    
    public String[] obtenerEstadosDisponibles() {
        return new String[]{
            facturas.ESTADO_PENDIENTE,
            facturas.ESTADO_PAGADA,
            facturas.ESTADO_CANCELADA,
            facturas.ESTADO_ANULADA,
            facturas.ESTADO_PROCESADA
        };
    }
    
    public String generarFacturaDesdeReserva(int idReserva) {
        ControladorReserva cr = new ControladorReserva();
        Reserva r = cr.buscarReservaPorId(idReserva);
        if (r == null || r.getHabitacion().contains("no existe")) {
            return "Reserva no encontrada.";
        }
        
        ControladorHabitaciones ch = new ControladorHabitaciones();
        Habitaciones h = ch.buscarHabitacionPorId(r.getIdHabitacion());
        if (h == null || h.getTipoHabitacion().contains("no existe")) {
            return "Habitación no encontrada.";
        }
        
        long diffInMillies = Math.abs(r.getFechaSalida().getTime() - r.getFechaEntrada().getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff == 0) diff = 1; // Mínimo 1 día
        
        double total = diff * h.getPrecioHabitacion();
        
        insertarFactura(idReserva, new Date(), total, facturas.ESTADO_PENDIENTE, r.getMedioPago());
        return "Factura generada con éxito por un total de " + total;
    }
}
