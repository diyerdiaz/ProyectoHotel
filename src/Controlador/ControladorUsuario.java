package Controlador;

import modelo.Login;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;

public class ControladorUsuario {

    private Login modeloLogin;

    public ControladorUsuario() {
        this.modeloLogin = new Login();
    }

    public Iterator<Login> listarUsuarios() {
        return modeloLogin.Listar();
    }

    public void insertarUsuario(String nombre, String contrasena, String rol) {
        Login user = new Login();
        user.setNombreUsuario(nombre);
        user.setContrasenaUsuario(contrasena);
        user.setRolUsuario(rol);
        user.insertar();
    }

    public void modificarUsuario(int id, String nombre, String contrasena, String rol) {
        Login user = new Login();
        user.setIdUsuario(id);
        user.setNombreUsuario(nombre);
        user.setContrasenaUsuario(contrasena);
        user.setRolUsuario(rol);
        user.modificar();
    }

    public void eliminarUsuario(int id) {
        Login user = new Login();
        user.setIdUsuario(id);
        user.eliminar();
    }

    public Iterator<Login> buscarUsuario(String busqueda) {
        return modeloLogin.buscar(busqueda);
    }

    public Login buscarUsuarioPorId(int id) {
        return modeloLogin.buscarPorId(id);
    }

    public void cargarTablaUsuarios(javax.swing.JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        Iterator<Login> usuarios = listarUsuarios();
        while (usuarios.hasNext()) {
            Login u = usuarios.next();
            if (u.getNombreUsuario() != null && !u.getNombreUsuario().equals("No hay nada registrado")) {
                model.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNombreUsuario(),
                    u.getRolUsuario()
                });
            }
        }
    }
}
