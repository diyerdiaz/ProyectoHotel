package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Reserva {
    private int idReserva;
    private int idCliente;
    private int idHabitacion;
    private String Habitacion; // Para mantener compatibilidad con vistas si guarda el numero
    private int personas;
    private Date fechaEntrada;
    private Date fechaSalida;
    private String medioPago;

    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
    public String getHabitacion() { return Habitacion; }
    public void setHabitacion(String Habitacion) { this.Habitacion = Habitacion; }
    public int getPersonas() { return personas; }
    public void setPersonas(int personas) { this.personas = personas; }
    public Date getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(Date fechaEntrada) { this.fechaEntrada = fechaEntrada; }
    public Date getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(Date fechaSalida) { this.fechaSalida = fechaSalida; }
    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }

    public Iterator<Reserva> Listar() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM reserva");
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    Reserva r = new Reserva();
                    r.setIdReserva(rs.getInt("idreserva"));
                    r.setIdCliente(rs.getInt("idcliente"));
                    r.setIdHabitacion(rs.getInt("idhabitacion"));
                    r.setHabitacion(rs.getString("habitacion"));
                    r.setPersonas(rs.getInt("personas"));
                    r.setFechaEntrada(rs.getDate("fechaentrada"));
                    r.setFechaSalida(rs.getDate("fechasalida"));
                    r.setMedioPago(rs.getString("mediopago"));
                    reservas.add(r);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar: " + ex.getMessage());
        }
        if (reservas.isEmpty()) {
            Reserva miReserva = new Reserva();
            miReserva.setHabitacion("No hay nada registrado");
            reservas.add(miReserva);
        }
        return reservas.iterator();
    }

    public void insertar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "INSERT INTO reserva (idcliente, idhabitacion, habitacion, personas, fechaentrada, fechasalida, mediopago) VALUES (?,?,?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            sql.setInt(1, getIdCliente());
            sql.setInt(2, getIdHabitacion());
            sql.setString(3, getHabitacion());
            sql.setInt(4, getPersonas());
            sql.setDate(5, new java.sql.Date(getFechaEntrada().getTime()));
            sql.setDate(6, new java.sql.Date(getFechaSalida().getTime()));
            sql.setString(7, getMedioPago());
            sql.executeUpdate();
            
            try (ResultSet generatedKeys = sql.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    setIdReserva(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al insertar: " + ex.getMessage());
        }
    }

    public void modificar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "UPDATE reserva SET idcliente=?, idhabitacion=?, habitacion=?, personas=?, fechaentrada=?, fechasalida=?, mediopago=? WHERE idreserva=?");
            sql.setInt(1, getIdCliente());
            sql.setInt(2, getIdHabitacion());
            sql.setString(3, getHabitacion());
            sql.setInt(4, getPersonas());
            sql.setDate(5, new java.sql.Date(getFechaEntrada().getTime()));
            sql.setDate(6, new java.sql.Date(getFechaSalida().getTime()));
            sql.setString(7, getMedioPago());
            sql.setInt(8, getIdReserva());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al modificar: " + ex.getMessage());
        }
    }

    public void eliminar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("DELETE FROM reserva WHERE idreserva=?");
            sql.setInt(1, getIdReserva());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar: " + ex.getMessage());
        }
    }

    public Iterator<Reserva> buscar(String busqueda) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM reserva WHERE CAST(idreserva AS TEXT) LIKE ? OR habitacion LIKE ? OR CAST(personas AS TEXT) LIKE ? OR CAST(fechaentrada AS TEXT) LIKE ? OR CAST(fechasalida AS TEXT) LIKE ? OR mediopago LIKE ?");
            String like = "%" + busqueda + "%";
            for (int i = 1; i <= 6; i++) {
                sql.setString(i, like);
            }

            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    Reserva r = new Reserva();
                    r.setIdReserva(rs.getInt("idreserva"));
                    r.setIdCliente(rs.getInt("idcliente"));
                    r.setIdHabitacion(rs.getInt("idhabitacion"));
                    r.setHabitacion(rs.getString("habitacion"));
                    r.setPersonas(rs.getInt("personas"));
                    r.setFechaEntrada(rs.getDate("fechaentrada"));
                    r.setFechaSalida(rs.getDate("fechasalida"));
                    r.setMedioPago(rs.getString("mediopago"));
                    reservas.add(r);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar: " + ex.getMessage());
        }
        return reservas.iterator();
    }

    public Reserva buscarPorId(int elId) {
        Reserva r = new Reserva();
        r.setHabitacion("RESERVA no existe");
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM reserva WHERE idreserva=?");
            sql.setInt(1, elId);
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    r.setIdReserva(rs.getInt("idreserva"));
                    r.setIdCliente(rs.getInt("idcliente"));
                    r.setIdHabitacion(rs.getInt("idhabitacion"));
                    r.setHabitacion(rs.getString("habitacion"));
                    r.setPersonas(rs.getInt("personas"));
                    r.setFechaEntrada(rs.getDate("fechaentrada"));
                    r.setFechaSalida(rs.getDate("fechasalida"));
                    r.setMedioPago(rs.getString("mediopago"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar por ID: " + ex.getMessage());
        }
        return r;
    }

    public boolean esHabitacionDisponible(int idHabitacion, Date fechaEntrada, Date fechaSalida, int idReservaExcluir) {
        boolean disponible = true;
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT COUNT(*) FROM reserva WHERE idhabitacion = ? AND idreserva != ? AND " +
                    "((fechaentrada <= ? AND fechasalida >= ?) OR (fechaentrada <= ? AND fechasalida >= ?) OR " +
                    "(fechaentrada >= ? AND fechasalida <= ?))");
            sql.setInt(1, idHabitacion);
            sql.setInt(2, idReservaExcluir); // Usar -1 si es nueva
            
            java.sql.Date sqlEntrada = new java.sql.Date(fechaEntrada.getTime());
            java.sql.Date sqlSalida = new java.sql.Date(fechaSalida.getTime());
            
            sql.setDate(3, sqlEntrada);
            sql.setDate(4, sqlEntrada);
            sql.setDate(5, sqlSalida);
            sql.setDate(6, sqlSalida);
            sql.setDate(7, sqlEntrada);
            sql.setDate(8, sqlSalida);
            
            try (ResultSet rs = sql.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) disponible = false;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al verificar disponibilidad: " + ex.getMessage());
        }
        return disponible;
    }
}
