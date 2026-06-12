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

/**
 *
 * @author diyer
 */
public class Login {
    
    private  int  idUsuario;
    private  String nombreUsuario;
    private  String contraseñaUsuario;
    private  String rolUsuario;

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

    public String getContraseñaUsuario() {
        return contraseñaUsuario;
    }

    public void setContraseñaUsuario(String contraseñaUsuario) {
        this.contraseñaUsuario = contraseñaUsuario;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    @Override
    public String toString() {
        return "Login{" + "nombreUsuario=" + nombreUsuario + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Login other = (Login) obj;
        return this.idUsuario == other.idUsuario;
    }
   

    public Iterator<Login> Listar() {

    ArrayList<Login> losUsuarios = new ArrayList<>();

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM Usuarios");

        ResultSet rs = sql.executeQuery();

        Login unUsuario;

        while (rs.next()) {

            unUsuario = new Login();

            unUsuario.setIdUsuario(rs.getInt("idUsuario"));
            unUsuario.setNombreUsuario(rs.getString("nombreUsuario"));
            unUsuario.setContraseñaUsuario(rs.getString("contraseñaUsuario"));
            unUsuario.setRolUsuario(rs.getString("rolUsuario"));

            losUsuarios.add(unUsuario);
        }

    } catch (SQLException ex) {
        System.err.println("Error al listar usuario: " + ex.getMessage());
    }

    if (losUsuarios.isEmpty()) {
        Login miUsuario = new Login();
        miUsuario.setNombreUsuario("No hay nada registrado");
        losUsuarios.add(miUsuario);
    }

    return losUsuarios.iterator();
}

public void insertar() {
    try {
        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "INSERT INTO Usuarios VALUES(NULL,?,?,?)");

        sql.setString(1, this.getNombreUsuario());
        sql.setString(2, this.getContraseñaUsuario());
        sql.setString(3, this.getRolUsuario());

        sql.executeUpdate();

        System.out.println("insertado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al insertar : "
                + ex.getMessage());
    }
}

public void modificar() {

    try {
        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "UPDATE Usuarios "
                + "SET nombreUsuario=?, contraseñaUsuario=?, rolUsuario=? "
                + "WHERE idUsuario=?");

        sql.setString(1, this.getNombreUsuario());
        sql.setString(2, this.getContraseñaUsuario());
        sql.setString(3, this.getRolUsuario());
        sql.setInt(4, this.getIdUsuario());

        sql.executeUpdate();

        System.out.println("modificado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al modificar : "
                + ex.getMessage());
    }
}

public void eliminar() {

    try {
        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "DELETE FROM Usuarios "
                + "WHERE idUsuario=?");

        sql.setInt(1, this.getIdUsuario());

        sql.executeUpdate();

        System.out.println(this.getClass().getSimpleName()
                + " eliminado correctamente");

    } catch (SQLException ex) {
        System.err.println("Error al eliminar "
                + this.getClass().getSimpleName()
                + ": " + ex.getMessage());
    }
}

public Iterator<Login> buscar(String busqueda) {

    ArrayList<Login> losUsuarios = new ArrayList<>();

    try {

        System.out.println("Buscando: " + busqueda);

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM Usuarios "
                + "WHERE idUsuario LIKE ? "
                + "OR nombreUsuario LIKE ? "
                + "OR contraseñaUsuario LIKE ? "
                + "OR rolUsuario LIKE ?");

        sql.setString(1, "%" + busqueda + "%");
        sql.setString(2, "%" + busqueda + "%");
        sql.setString(3, "%" + busqueda + "%");
        sql.setString(4, "%" + busqueda + "%");

        ResultSet rs = sql.executeQuery();

        int contador = 0;

        while (rs.next()) {

            contador++;

            Login unUsuario = new Login();

            unUsuario.setIdUsuario(rs.getInt("idUsuario"));
            unUsuario.setNombreUsuario(rs.getString("nombreUsuario"));
            unUsuario.setContraseñaUsuario(rs.getString("contraseñaUsuario"));
            unUsuario.setRolUsuario(rs.getString("rolUsuario"));

            losUsuarios.add(unUsuario);

            System.out.println("encontrado: "
                    + unUsuario.getIdUsuario());
        }

        System.out.println("Total encontrados: " + contador);

    } catch (SQLException ex) {

        System.err.println("Error al buscar "
                + this.getClass().getSimpleName()
                + ": " + ex.getMessage());
    }

    return losUsuarios.iterator();
}

public Login buscarPorId(int elId) {

    Login unUsuario = new Login();
    unUsuario.setNombreUsuario("USUARIO no existe");

    try {

        PreparedStatement sql = ConexionBD.conexion.prepareStatement(
                "SELECT * FROM Usuarios WHERE idUsuario=?");

        sql.setInt(1, elId);

        ResultSet rs = sql.executeQuery();

        while (rs.next()) {

            unUsuario.setIdUsuario(rs.getInt("idUsuario"));
            unUsuario.setNombreUsuario(rs.getString("nombreUsuario"));
            unUsuario.setContraseñaUsuario(rs.getString("contraseñaUsuario"));
            unUsuario.setRolUsuario(rs.getString("rolUsuario"));
        }

    } catch (SQLException ex) {
        System.err.println("Error al buscar por Id: "
                + ex.getMessage());
    }

    return unUsuario;
}
    
    
    
    
    
}
