package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Login {

    private int idUsuario;
    private String nombreUsuario;
    private String contrasenaUsuario;
    private String rolUsuario;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenaUsuario() {
        return contrasenaUsuario;
    }

    public void setContrasenaUsuario(String contrasenaUsuario) {
        this.contrasenaUsuario = contrasenaUsuario;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public Iterator<Login> Listar() {
        ArrayList<Login> usuarios = new ArrayList<>();

        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement("SELECT * FROM usuarios");
            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    Login u = new Login();
                    u.setIdUsuario(rs.getInt("idusuario"));
                    u.setNombreUsuario(rs.getString("nombreusuario"));
                    u.setContrasenaUsuario(rs.getString("contrasenausuario"));
                    u.setRolUsuario(rs.getString("rolusuario"));
                    usuarios.add(u);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar usuario: " + ex.getMessage());
        }

        if (usuarios.isEmpty()) {
            Login miUsuario = new Login();
            miUsuario.setNombreUsuario("No hay nada registrado");
            usuarios.add(miUsuario);
        }

        return usuarios.iterator();
    }

    public void insertar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "INSERT INTO usuarios (nombreusuario, contrasenausuario, rolusuario) VALUES (?,?,?)");
            sql.setString(1, this.getNombreUsuario());
            sql.setString(2, this.getContrasenaUsuario());
            sql.setString(3, this.getRolUsuario());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al insertar: " + ex.getMessage());
        }
    }

    public void modificar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "UPDATE usuarios SET nombreusuario=?, contrasenausuario=?, rolusuario=? WHERE idusuario=?");
            sql.setString(1, this.getNombreUsuario());
            sql.setString(2, this.getContrasenaUsuario());
            sql.setString(3, this.getRolUsuario());
            sql.setInt(4, this.getIdUsuario());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al modificar: " + ex.getMessage());
        }
    }

    public void eliminar() {
        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "DELETE FROM usuarios WHERE idusuario=?");
            sql.setInt(1, this.getIdUsuario());
            sql.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar: " + ex.getMessage());
        }
    }

    public Iterator<Login> buscar(String busqueda) {
        ArrayList<Login> usuarios = new ArrayList<>();

        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM usuarios WHERE CAST(idusuario AS TEXT) LIKE ? OR nombreusuario LIKE ? OR contrasenausuario LIKE ? OR rolusuario LIKE ?");
            String like = "%" + busqueda + "%";
            sql.setString(1, like);
            sql.setString(2, like);
            sql.setString(3, like);
            sql.setString(4, like);

            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    Login u = new Login();
                    u.setIdUsuario(rs.getInt("idusuario"));
                    u.setNombreUsuario(rs.getString("nombreusuario"));
                    u.setContrasenaUsuario(rs.getString("contrasenausuario"));
                    u.setRolUsuario(rs.getString("rolusuario"));
                    usuarios.add(u);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar Login: " + ex.getMessage());
        }

        return usuarios.iterator();
    }

    public Login buscarPorId(int elId) {
        Login usuario = new Login();
        usuario.setNombreUsuario("USUARIO no existe");

        try {
            PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                    "SELECT * FROM usuarios WHERE idusuario=?");
            sql.setInt(1, elId);

            try (ResultSet rs = sql.executeQuery()) {
                while (rs.next()) {
                    usuario.setIdUsuario(rs.getInt("idusuario"));
                    usuario.setNombreUsuario(rs.getString("nombreusuario"));
                    usuario.setContrasenaUsuario(rs.getString("contrasenausuario"));
                    usuario.setRolUsuario(rs.getString("rolusuario"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar por Id: " + ex.getMessage());
        }

        return usuario;
    }
}
