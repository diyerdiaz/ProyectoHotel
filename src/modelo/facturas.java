package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class facturas {
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

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }
    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }
    public Date getFechaFactura() { return fechaFactura; }
    public void setFechaFactura(Date fechaFactura) { this.fechaFactura = fechaFactura; }
    public double getTotalFactura() { return totalFactura; }
    public void setTotalFactura(double totalFactura) { this.totalFactura = totalFactura; }
    public String getEstadoFactura() { return estadoFactura; }
    public void setEstadoFactura(String estadoFactura) { this.estadoFactura = estadoFactura; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Iterator<facturas> Listar() {
        ArrayList<facturas> facturas = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM facturas");
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    facturas f = new facturas();
                    f.setIdFactura(rs.getInt("idfactura"));
                    f.setIdReserva(rs.getInt("idreserva"));
                    f.setFechaFactura(rs.getDate("fechafactura"));
                    f.setTotalFactura(rs.getDouble("totalfactura"));
                    f.setEstadoFactura(rs.getString("estadofactura"));
                    f.setMetodoPago(rs.getString("metodopago"));
                    facturas.add(f);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar: " + ex.getMessage());
        }
        if (facturas.isEmpty()) {
            facturas f = new facturas();
            f.setEstadoFactura("No hay nada registrado");
            facturas.add(f);
        }
        return facturas.iterator();
    }

    public void insertar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "INSERT INTO facturas (idreserva, fechafactura, totalfactura, estadofactura, metodopago) VALUES (?,?,?,?,?)");
            sql.setInt(1, getIdReserva());
            sql.setDate(2, new java.sql.Date(getFechaFactura().getTime()));
            sql.setDouble(3, getTotalFactura());
            sql.setString(4, getEstadoFactura());
            sql.setString(5, getMetodoPago());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al insertar: " + ex.getMessage());
        }
    }

    public void modificar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "UPDATE facturas SET idreserva=?, fechafactura=?, totalfactura=?, estadofactura=?, metodopago=? WHERE idfactura=?");
            sql.setInt(1, getIdReserva());
            sql.setDate(2, new java.sql.Date(getFechaFactura().getTime()));
            sql.setDouble(3, getTotalFactura());
            sql.setString(4, getEstadoFactura());
            sql.setString(5, getMetodoPago());
            sql.setInt(6, getIdFactura());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al modificar: " + ex.getMessage());
        }
    }

    public void eliminar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("DELETE FROM facturas WHERE idfactura=?");
            sql.setInt(1, getIdFactura());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar: " + ex.getMessage());
        }
    }

    public Iterator<facturas> buscar(String busqueda) {
        ArrayList<facturas> facturas = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM facturas WHERE CAST(idfactura AS TEXT) LIKE ? OR CAST(idreserva AS TEXT) LIKE ? OR CAST(fechafactura AS TEXT) LIKE ? OR CAST(totalfactura AS TEXT) LIKE ? OR estadofactura LIKE ? OR metodopago LIKE ?");
            String like = "%" + busqueda + "%";
            for (int i = 1; i <= 6; i++) {
                sql.setString(i, like);
            }

            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    facturas f = new facturas();
                    f.setIdFactura(rs.getInt("idfactura"));
                    f.setIdReserva(rs.getInt("idreserva"));
                    f.setFechaFactura(rs.getDate("fechafactura"));
                    f.setTotalFactura(rs.getDouble("totalfactura"));
                    f.setEstadoFactura(rs.getString("estadofactura"));
                    f.setMetodoPago(rs.getString("metodopago"));
                    facturas.add(f);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar: " + ex.getMessage());
        }
        return facturas.iterator();
    }

    public facturas buscarPorId(int elId) {
        facturas f = new facturas();
        f.setEstadoFactura("FACTURA no existe");
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM facturas WHERE idfactura=?");
            sql.setInt(1, elId);
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    f.setIdFactura(rs.getInt("idfactura"));
                    f.setIdReserva(rs.getInt("idreserva"));
                    f.setFechaFactura(rs.getDate("fechafactura"));
                    f.setTotalFactura(rs.getDouble("totalfactura"));
                    f.setEstadoFactura(rs.getString("estadofactura"));
                    f.setMetodoPago(rs.getString("metodopago"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar por ID: " + ex.getMessage());
        }
        return f;
    }

    public void cambiarEstado(int idFactura, String nuevoEstado) {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("UPDATE facturas SET estadofactura=? WHERE idfactura=?");
            sql.setString(1, nuevoEstado);
            sql.setInt(2, idFactura);
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al cambiar estado: " + ex.getMessage());
        }
    }

    public Iterator<facturas> buscarPorEstado(String estado) {
        ArrayList<facturas> facturas = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM facturas WHERE estadofactura LIKE ?");
            sql.setString(1, "%" + estado + "%");
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    facturas f = new facturas();
                    f.setIdFactura(rs.getInt("idfactura"));
                    f.setIdReserva(rs.getInt("idreserva"));
                    f.setFechaFactura(rs.getDate("fechafactura"));
                    f.setTotalFactura(rs.getDouble("totalfactura"));
                    f.setEstadoFactura(rs.getString("estadofactura"));
                    f.setMetodoPago(rs.getString("metodopago"));
                    facturas.add(f);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar por estado: " + ex.getMessage());
        }
        return facturas.iterator();
    }

    public boolean esEstadoValido(String estado) {
        return estado.equals(ESTADO_PENDIENTE)
                || estado.equals(ESTADO_PAGADA)
                || estado.equals(ESTADO_CANCELADA)
                || estado.equals(ESTADO_ANULADA)
                || estado.equals(ESTADO_PROCESADA);
    }
}
