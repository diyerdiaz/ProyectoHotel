/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.Login;
import modelo.ConexionBD;
import vista.MDILogin;
import vista.MDIPrincipal;
import java.util.Iterator;

/**
 *
 * @author alejo
 */
public class ControladorLogin {
    
    private MDILogin vistaLogin;
    private Login modeloLogin;
    
    public ControladorLogin(MDILogin vistaLogin) {
        this.vistaLogin = vistaLogin;
        this.modeloLogin = new Login();
        
        // Inicializar la conexión a la base de datos
        ConexionBD.getInstance();
        
        // Configurar listeners
        this.vistaLogin.getJButton1().addActionListener(e -> iniciarSesion());
    }
    
    public void iniciarSesion() {
        String usuario = vistaLogin.getJTextField1().getText();
        String contraseña = vistaLogin.getJTextField2().getText();
        
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vistaLogin, 
                "Por favor ingrese usuario y contraseña", 
                "Campos vacíos", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Buscar usuario en la base de datos
        Iterator<Login> usuarios = modeloLogin.buscar(usuario);
        boolean encontrado = false;
        Login usuarioValido = null;
        
        while (usuarios.hasNext()) {
            Login u = usuarios.next();
            if (u.getNombreUsuario().equals(usuario) && 
                u.getContraseñaUsuario().equals(contraseña)) {
                encontrado = true;
                usuarioValido = u;
                break;
            }
        }
        
        if (encontrado) {
            javax.swing.JOptionPane.showMessageDialog(vistaLogin, 
                "¡Bienvenido " + usuarioValido.getNombreUsuario() + "!", 
                "Login exitoso", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            // Abrir ventana principal
            MDIPrincipal principal = new MDIPrincipal();
            principal.setVisible(true);
            vistaLogin.dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(vistaLogin, 
                "Usuario o contraseña incorrectos", 
                "Error de autenticación", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}
