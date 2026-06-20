/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.TipoHabitacion;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alejo
 */
public class ControladorTipoHabitacion {
    
    private TipoHabitacion modeloTipoHabitacion;
    
    public ControladorTipoHabitacion() {
        this.modeloTipoHabitacion = new TipoHabitacion();
    }
    
    public Iterator<TipoHabitacion> listarTiposHabitacion() {
        return modeloTipoHabitacion.Listar();
    }
    
    public void insertarTipoHabitacion(String nombreTipoHabitacion, String descripcionTipoHabitacion) {
        TipoHabitacion tipoHabitacion = new TipoHabitacion();
        tipoHabitacion.setNombreTipoHabitacion(nombreTipoHabitacion);
        tipoHabitacion.setDescripcionTipoHabitacion(descripcionTipoHabitacion);
        tipoHabitacion.insertar();
    }
    
    public void modificarTipoHabitacion(int idtipoHabitacion, String nombreTipoHabitacion, 
                                         String descripcionTipoHabitacion) {
        TipoHabitacion tipoHabitacion = new TipoHabitacion();
        tipoHabitacion.setIdtipoHabitacion(idtipoHabitacion);
        tipoHabitacion.setNombreTipoHabitacion(nombreTipoHabitacion);
        tipoHabitacion.setDescripcionTipoHabitacion(descripcionTipoHabitacion);
        tipoHabitacion.modificar();
    }
    
    public void eliminarTipoHabitacion(int idtipoHabitacion) {
        TipoHabitacion tipoHabitacion = new TipoHabitacion();
        tipoHabitacion.setIdtipoHabitacion(idtipoHabitacion);
        tipoHabitacion.eliminar();
    }
    
    public Iterator<TipoHabitacion> buscarTipoHabitacion(String busqueda) {
        return modeloTipoHabitacion.buscar(busqueda);
    }
    
    public TipoHabitacion buscarTipoHabitacionPorId(int idtipoHabitacion) {
        return modeloTipoHabitacion.buscarPorId(idtipoHabitacion);
    }
    
    public void cargarTablaTiposHabitacion(javax.swing.JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);
        
        Iterator<TipoHabitacion> tipos = listarTiposHabitacion();
        while (tipos.hasNext()) {
            TipoHabitacion t = tipos.next();
            if (!t.getNombreTipoHabitacion().equals("No hay nada registrado")) {
                model.addRow(new Object[]{
                    t.getIdtipoHabitacion(),
                    t.getNombreTipoHabitacion(),
                    t.getDescripcionTipoHabitacion()
                });
            }
        }
    }
}
