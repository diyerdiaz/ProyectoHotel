package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexionBD {

    public static Connection conexion;

    private ConexionBD() {
        conectar();
    }

    public static synchronized ConexionBD getInstance() {
        if (conexion == null) {
            new ConexionBD();
        }
        return ConexionBDHolder.INSTANCE;
    }

    public static synchronized void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al desconectar: " + ex.getMessage());
        } finally {
            conexion = null;
        }
    }

    private void conectar() {
        try {
            String driverBD = "org.postgresql.Driver";
            String urlBD = System.getProperty("hotel.db.url",
                    System.getenv().getOrDefault("HOTEL_DB_URL", "jdbc:postgresql://164.68.98.66:5439/evaluacion"));
            String usuarioBD = System.getProperty("hotel.db.user",
                    System.getenv().getOrDefault("HOTEL_DB_USER", "postgres"));
            String claveBD = System.getProperty("hotel.db.password",
                    System.getenv().getOrDefault("HOTEL_DB_PASSWORD", "Sena2026*"));

            Class.forName(driverBD);
            if (conexion != null && !conexion.isClosed()) {
                return;
            }
            conexion = DriverManager.getConnection(urlBD, usuarioBD, claveBD);
            System.out.println("Conexion exitosa a PostgreSQL");
            inicializarEsquema();
        } catch (ClassNotFoundException ex) {
            System.err.println("No se encontro el driver de Postgres.");
        } catch (SQLException ex) {
            System.err.println("Error de conexion: " + ex.getMessage());
        }
    }

    private void inicializarEsquema() throws SQLException {
        try (java.sql.Statement st = conexion.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    idusuario SERIAL PRIMARY KEY,
                    nombreusuario VARCHAR(80) NOT NULL UNIQUE,
                    contrasenausuario VARCHAR(255) NOT NULL,
                    rolusuario VARCHAR(30) NOT NULL
                )
                """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS cliente (
                    idcliente SERIAL PRIMARY KEY,
                    nombre VARCHAR(80) NOT NULL,
                    apellido VARCHAR(80) NOT NULL,
                    documento BIGINT NOT NULL,
                    correo VARCHAR(120) NOT NULL,
                    telefono BIGINT NOT NULL,
                    direccion VARCHAR(150) NOT NULL,
                    idusuario INTEGER NOT NULL REFERENCES usuarios(idusuario) ON DELETE CASCADE
                )
                """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS tipohabitacion (
                    idtipohabitacion SERIAL PRIMARY KEY,
                    nombretipohabitacion VARCHAR(80) NOT NULL,
                    descripciontipohabitacion VARCHAR(255) NOT NULL
                )
                """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS habitaciones (
                    idhabitacion SERIAL PRIMARY KEY,
                    numerohabitacion INTEGER NOT NULL,
                    tipohabitacion VARCHAR(80) NOT NULL,
                    preciohabitacion DOUBLE PRECISION NOT NULL,
                    estadohabitacion VARCHAR(40) NOT NULL
                )
                """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS reserva (
                    idreserva SERIAL PRIMARY KEY,
                    habitacion VARCHAR(80) NOT NULL,
                    personas INTEGER NOT NULL,
                    fechaentrada DATE NOT NULL,
                    fechasalida DATE NOT NULL,
                    mediopago VARCHAR(80) NOT NULL
                )
                """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS empleado (
                    idempleado SERIAL PRIMARY KEY,
                    nombre VARCHAR(80) NOT NULL,
                    apellido VARCHAR(80) NOT NULL,
                    documento INTEGER NOT NULL,
                    cargo VARCHAR(80) NOT NULL,
                    salario DOUBLE PRECISION NOT NULL,
                    fechacontratacion DATE NOT NULL,
                    telefono VARCHAR(30) NOT NULL,
                    correo VARCHAR(120) NOT NULL,
                    direccion VARCHAR(150) NOT NULL,
                    idusuario INTEGER REFERENCES usuarios(idusuario) ON DELETE SET NULL
                )
                """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS facturas (
                    idfactura SERIAL PRIMARY KEY,
                    idreserva INTEGER NOT NULL REFERENCES reserva(idreserva) ON DELETE CASCADE,
                    fechafactura DATE NOT NULL DEFAULT CURRENT_DATE,
                    totalfactura DOUBLE PRECISION NOT NULL,
                    estadofactura VARCHAR(40) NOT NULL DEFAULT 'PENDIENTE',
                    metodopago VARCHAR(80) NOT NULL
                )
                """);
        }

        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT COUNT(*) FROM usuarios WHERE nombreusuario = ?")) {
            ps.setString(1, "admin");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement insert = conexion.prepareStatement(
                            "INSERT INTO usuarios (nombreusuario, contrasenausuario, rolusuario) VALUES (?, ?, ?)")) {
                        insert.setString(1, "admin");
                        insert.setString(2, "admin123");
                        insert.setString(3, "administrador");
                        insert.executeUpdate();
                    }
                }
            }
        }
    }

    private static class ConexionBDHolder {
        private static final ConexionBD INSTANCE = new ConexionBD();
    }

    public static void main(String[] args) {
        ConexionBD.getInstance();
    }
}
