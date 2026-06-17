/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author diyer
 */
public class Reserva {
    private  int  idReserva;
    private  String Habitacion;
    private  int personas;
    private Date fechaEntrada;
    private Date fechaSalida;
    private  String medioPago;

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public String getHabitacion() {
        return Habitacion;
    }

    public void setHabitacion(String Habitacion) {
        this.Habitacion = Habitacion;
    }

    public int getPersonas() {
        return personas;
    }

    public void setPersonas(int personas) {
        this.personas = personas;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    @Override
    public String toString() {
        return "Reservas{" + "Habitacion=" + Habitacion + '}';
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
        final Reserva other = (Reserva) obj;
        return this.idReserva == other.idReserva;
    }
    
    public Iterator<Reserva> Listar() {

    ArrayList<Reserva> lasReservas = new ArrayList<>();

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM Reserva");

        ResultSet rs = sql.executeQuery();

        Reserva unaReserva;

        while (rs.next()) {

            unaReserva = new Reserva();

            unaReserva.setIdReserva(rs.getInt("idReserva"));
            unaReserva.setHabitacion(rs.getString("Habitacion"));
            unaReserva.setPersonas(rs.getInt("personas"));
            unaReserva.setFechaEntrada(rs.getDate("fechaEntrada"));
            unaReserva.setFechaSalida(rs.getDate("fechaSalida"));
            unaReserva.setMedioPago(rs.getString("medioPago"));

            lasReservas.add(unaReserva);
        }

    } catch (SQLException ex) {
        System.err.println("Error al listar: " + ex.getMessage());
    }

    if (lasReservas.isEmpty()) {
        Reserva miReserva = new Reserva();
        miReserva.setHabitacion("No hay nada registrado");
        lasReservas.add(miReserva);
    }

    return lasReservas.iterator();
}

public void insertar() {

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "INSERT INTO Reserva VALUES(NULL,?,?,?,?,?)");

        sql.setString(1, this.getHabitacion());
        sql.setInt(2, this.getPersonas());
        sql.setDate(3, new java.sql.Date(this.getFechaEntrada().getTime()));
        sql.setDate(4, new java.sql.Date(this.getFechaSalida().getTime()));
        sql.setString(5, this.getMedioPago());

        sql.executeUpdate();

        System.out.println("Insertado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al insertar: " + ex.getMessage());
    }
}

public void modificar() {

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "UPDATE Reserva "
                + "SET Habitacion=?, personas=?, fechaEntrada=?, "
                + "fechaSalida=?, medioPago=? "
                + "WHERE idReserva=?");

        sql.setString(1, this.getHabitacion());
        sql.setInt(2, this.getPersonas());
        sql.setDate(3, new java.sql.Date(this.getFechaEntrada().getTime()));
        sql.setDate(4, new java.sql.Date(this.getFechaSalida().getTime()));
        sql.setString(5, this.getMedioPago());
        sql.setInt(6, this.getIdReserva());

        sql.executeUpdate();

        System.out.println("Modificado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al modificar: " + ex.getMessage());
    }
}

public void eliminar() {

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "DELETE FROM Reserva WHERE idReserva=?");

        sql.setInt(1, this.getIdReserva());

        sql.executeUpdate();

        System.out.println("Eliminado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al eliminar: " + ex.getMessage());
    }
}

public Iterator<Reserva> buscar(String busqueda) {

    ArrayList<Reserva> lasReservas = new ArrayList<>();

    try {

        System.out.println("Buscando: " + busqueda);

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM Reserva "
                + "WHERE idReserva LIKE ? "
                + "OR Habitacion LIKE ? "
                + "OR personas LIKE ? "
                + "OR fechaEntrada LIKE ? "
                + "OR fechaSalida LIKE ? "
                + "OR medioPago LIKE ?");

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

            Reserva unaReserva = new Reserva();

            unaReserva.setIdReserva(rs.getInt("idReserva"));
            unaReserva.setHabitacion(rs.getString("Habitacion"));
            unaReserva.setPersonas(rs.getInt("personas"));
            unaReserva.setFechaEntrada(rs.getDate("fechaEntrada"));
            unaReserva.setFechaSalida(rs.getDate("fechaSalida"));
            unaReserva.setMedioPago(rs.getString("medioPago"));

            lasReservas.add(unaReserva);

            System.out.println("Encontrado: "
                    + unaReserva.getIdReserva());
        }

        System.out.println("Total encontrados: " + contador);

    } catch (SQLException ex) {

        System.err.println("Error al buscar: " + ex.getMessage());
    }

    return lasReservas.iterator();
}

public Reserva buscarPorId(int elId) {

    Reserva unaReserva = new Reserva();
    unaReserva.setHabitacion("RESERVA no existe");

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM Reserva WHERE idReserva=?");

        sql.setInt(1, elId);

        ResultSet rs = sql.executeQuery();

        while (rs.next()) {

            unaReserva.setIdReserva(rs.getInt("idReserva"));
            unaReserva.setHabitacion(rs.getString("Habitacion"));
            unaReserva.setPersonas(rs.getInt("personas"));
            unaReserva.setFechaEntrada(rs.getDate("fechaEntrada"));
            unaReserva.setFechaSalida(rs.getDate("fechaSalida"));
            unaReserva.setMedioPago(rs.getString("medioPago"));
        }

    } catch (SQLException ex) {
        System.err.println("Error al buscar por ID: "
                + ex.getMessage());
    }

    return unaReserva;
}
    
    
}
