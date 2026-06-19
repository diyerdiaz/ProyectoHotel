/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.Login;
import modelo.ConexionBD;
import vista.MDILogin;
import util.Encriptador;
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
        this.vistaLogin.getJButton2().addActionListener(e -> abrirRegistroUsuario());
        
        // Cargar credenciales guardadas si existe el checkbox recordarme
        cargarCredencialesGuardadas();
    }
    
    public void iniciarSesion() {
        String usuario = vistaLogin.getTxtUsuario().getText();
        String contraseña = new String(vistaLogin.getTxtContraseña().getPassword());
        String contraseñaHash = Encriptador.hashSHA256(contraseña);
        
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
                u.getContraseñaUsuario().equals(contraseñaHash)) {
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
            
            // Guardar credenciales si el checkbox está seleccionado
            if (vistaLogin.getChkRecordarme().isSelected()) {
                guardarCredenciales(usuario);
            } else {
                eliminarCredencialesGuardadas();
            }
            
            // Cerrar ventana de login
            vistaLogin.dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(vistaLogin, 
                "Usuario o contraseña incorrectos", 
                "Error de autenticación", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            
            // Limpiar campos en caso de error
            vistaLogin.getTxtContraseña().setText("");
        }
    }
    
    private void guardarCredenciales(String usuario) {
        try {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("usuario", usuario);
            
            java.io.FileOutputStream fos = new java.io.FileOutputStream("credenciales.properties");
            props.store(fos, "Credenciales guardadas");
            fos.close();
            
            System.out.println("Credenciales guardadas exitosamente");
        } catch (java.io.IOException ex) {
            System.err.println("Error al guardar credenciales: " + ex.getMessage());
        }
    }
    
    private void cargarCredencialesGuardadas() {
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream("credenciales.properties");
            java.util.Properties props = new java.util.Properties();
            props.load(fis);
            fis.close();
            
            String usuario = props.getProperty("usuario");
            
            if (usuario != null) {
                vistaLogin.getTxtUsuario().setText(usuario);
                vistaLogin.getChkRecordarme().setSelected(true);
            }
        } catch (java.io.IOException ex) {
            // No hay credenciales guardadas, no hacer nada
            System.out.println("No hay credenciales guardadas");
        }
    }
    
    private void eliminarCredencialesGuardadas() {
        try {
            java.io.File archivo = new java.io.File("credenciales.properties");
            if (archivo.exists()) {
                archivo.delete();
                System.out.println("Credenciales guardadas eliminadas");
            }
        } catch (Exception ex) {
            System.err.println("Error al eliminar credenciales: " + ex.getMessage());
        }
    }
    
    public void abrirRegistroUsuario() {
        vista.MDIRegistroUsuario registro = new vista.MDIRegistroUsuario();
        registro.setVisible(true);
        vistaLogin.setVisible(false);
        
        // Inicializar el controlador de registro
        new Controlador.ControladorRegistroUsuario(registro, vistaLogin);
    }
}
