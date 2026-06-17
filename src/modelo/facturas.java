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
public class facturas {
    
    // Constantes para estados de factura
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_PAGADA = "PAGADA";
    public static final String ESTADO_CANCELADA = "CANCELADA";
    public static final String ESTADO_ANULADA = "ANULADA";
    public static final String ESTADO_PROCESADA = "PROCESADA";
    
    private int idFactura;
    private int idReserva;
    private Date fechaFactura;
    private double totalFactura;
    private String estadoFactura;
    private String metodoPago;

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public double getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(double totalFactura) {
        this.totalFactura = totalFactura;
    }

    public String getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    @Override
    public String toString() {
        return "facturas{" + "idFactura=" + idFactura + ", totalFactura=" + totalFactura + '}';
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
        final facturas other = (facturas) obj;
        return this.idFactura == other.idFactura;
    }

    public Iterator<facturas> Listar() {
        ArrayList<facturas> lasFacturas = new ArrayList<>();

        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM Facturas");

            ResultSet rs = sql.executeQuery();

            facturas unaFactura;

            while (rs.next()) {
                unaFactura = new facturas();
                unaFactura.setIdFactura(rs.getInt("idFactura"));
                unaFactura.setIdReserva(rs.getInt("idReserva"));
                unaFactura.setFechaFactura(rs.getDate("fechaFactura"));
                unaFactura.setTotalFactura(rs.getDouble("totalFactura"));
                unaFactura.setEstadoFactura(rs.getString("estadoFactura"));
                unaFactura.setMetodoPago(rs.getString("metodoPago"));

                lasFacturas.add(unaFactura);
            }

        } catch (SQLException ex) {
            System.err.println("Error al listar: " + ex.getMessage());
        }

        if (lasFacturas.isEmpty()) {
            facturas miFactura = new facturas();
            miFactura.setEstadoFactura("No hay nada registrado");
            lasFacturas.add(miFactura);
        }

        return lasFacturas.iterator();
    }

    public void insertar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "INSERT INTO Facturas VALUES(NULL,?,?,?,?,?)");

            sql.setInt(1, this.getIdReserva());
            sql.setDate(2, new java.sql.Date(this.getFechaFactura().getTime()));
            sql.setDouble(3, this.getTotalFactura());
            sql.setString(4, this.getEstadoFactura());
            sql.setString(5, this.getMetodoPago());

            sql.executeUpdate();

            System.out.println("Insertado correctamente");

        } catch (SQLException ex) {
            System.err.println("Error al insertar: " + ex.getMessage());
        }
    }

    public void modificar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "UPDATE Facturas "
                    + "SET idReserva=?, fechaFactura=?, totalFactura=?, "
                    + "estadoFactura=?, metodoPago=? "
                    + "WHERE idFactura=?");

            sql.setInt(1, this.getIdReserva());
            sql.setDate(2, new java.sql.Date(this.getFechaFactura().getTime()));
            sql.setDouble(3, this.getTotalFactura());
            sql.setString(4, this.getEstadoFactura());
            sql.setString(5, this.getMetodoPago());
            sql.setInt(6, this.getIdFactura());

            sql.executeUpdate();

            System.out.println("Modificado correctamente");

        } catch (SQLException ex) {
            System.err.println("Error al modificar: " + ex.getMessage());
        }
    }

    public void eliminar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "DELETE FROM Facturas WHERE idFactura=?");

            sql.setInt(1, this.getIdFactura());

            sql.executeUpdate();

            System.out.println("Eliminado correctamente");

        } catch (SQLException ex) {
            System.err.println("Error al eliminar: " + ex.getMessage());
        }
    }

    public Iterator<facturas> buscar(String busqueda) {
        ArrayList<facturas> lasFacturas = new ArrayList<>();

        try {
            System.out.println("Buscando: " + busqueda);

            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM Facturas "
                    + "WHERE idFactura LIKE ? "
                    + "OR idReserva LIKE ? "
                    + "OR fechaFactura LIKE ? "
                    + "OR totalFactura LIKE ? "
                    + "OR estadoFactura LIKE ? "
                    + "OR metodoPago LIKE ?");

            sql.setString(1, "%" + busqueda + "%");
            sql.setString(2, "%" + busqueda + "%");
            sql.setString(3, "%" + busqueda + "%");
            sql.setString(4, "%" + busqueda + "%");
            sql.setString(5, "%" + busqueda + "%");
            sql.setString(6, "%" + busqueda + "%");

            ResultSet rs = sql.executeQuery();

            int contador = 0;

            while (rs.next()) {
                contador++;

                facturas unaFactura = new facturas();
                unaFactura.setIdFactura(rs.getInt("idFactura"));
                unaFactura.setIdReserva(rs.getInt("idReserva"));
                unaFactura.setFechaFactura(rs.getDate("fechaFactura"));
                unaFactura.setTotalFactura(rs.getDouble("totalFactura"));
                unaFactura.setEstadoFactura(rs.getString("estadoFactura"));
                unaFactura.setMetodoPago(rs.getString("metodoPago"));

                lasFacturas.add(unaFactura);

                System.out.println("Encontrado: " + unaFactura.getIdFactura());
            }

            System.out.println("Total encontrados: " + contador);

        } catch (SQLException ex) {
            System.err.println("Error al buscar: " + ex.getMessage());
        }

        return lasFacturas.iterator();
    }

    public facturas buscarPorId(int elId) {
        facturas unaFactura = new facturas();
        unaFactura.setEstadoFactura("FACTURA no existe");

        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM Facturas WHERE idFactura=?");

            sql.setInt(1, elId);

            ResultSet rs = sql.executeQuery();

            while (rs.next()) {
                unaFactura.setIdFactura(rs.getInt("idFactura"));
                unaFactura.setIdReserva(rs.getInt("idReserva"));
                unaFactura.setFechaFactura(rs.getDate("fechaFactura"));
                unaFactura.setTotalFactura(rs.getDouble("totalFactura"));
                unaFactura.setEstadoFactura(rs.getString("estadoFactura"));
                unaFactura.setMetodoPago(rs.getString("metodoPago"));
            }

        } catch (SQLException ex) {
            System.err.println("Error al buscar por ID: " + ex.getMessage());
        }

        return unaFactura;
    }
    
    // Métodos específicos para gestión de estados
    public void cambiarEstado(int idFactura, String nuevoEstado) {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "UPDATE Facturas SET estadoFactura=? WHERE idFactura=?");
            
            sql.setString(1, nuevoEstado);
            sql.setInt(2, idFactura);
            
            sql.executeUpdate();
            System.out.println("Estado de factura actualizado a: " + nuevoEstado);
            
        } catch (SQLException ex) {
            System.err.println("Error al cambiar estado: " + ex.getMessage());
        }
    }
    
    public Iterator<facturas> buscarPorEstado(String estado) {
        ArrayList<facturas> lasFacturas = new ArrayList<>();

        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM Facturas WHERE estadoFactura LIKE ?");

            sql.setString(1, "%" + estado + "%");

            ResultSet rs = sql.executeQuery();

            while (rs.next()) {
                facturas unaFactura = new facturas();
                unaFactura.setIdFactura(rs.getInt("idFactura"));
                unaFactura.setIdReserva(rs.getInt("idReserva"));
                unaFactura.setFechaFactura(rs.getDate("fechaFactura"));
                unaFactura.setTotalFactura(rs.getDouble("totalFactura"));
                unaFactura.setEstadoFactura(rs.getString("estadoFactura"));
                unaFactura.setMetodoPago(rs.getString("metodoPago"));

                lasFacturas.add(unaFactura);
            }

        } catch (SQLException ex) {
            System.err.println("Error al buscar por estado: " + ex.getMessage());
        }

        return lasFacturas.iterator();
    }
    
    public boolean esEstadoValido(String estado) {
        return estado.equals(ESTADO_PENDIENTE) || 
               estado.equals(ESTADO_PAGADA) || 
               estado.equals(ESTADO_CANCELADA) || 
               estado.equals(ESTADO_ANULADA) || 
               estado.equals(ESTADO_PROCESADA);
    }
}
