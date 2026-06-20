package Controlador;

<<<<<<< Updated upstream
import modelo.Login;
import modelo.ConexionBD;
import vista.MDILogin;
import util.Encriptador;
=======
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
>>>>>>> Stashed changes
import java.util.Iterator;
import java.util.Properties;
import javax.swing.JOptionPane;
import modelo.ConexionBD;
import modelo.Login;
import vista.MDIRegistroUsuario;
import vista.MDILogin;
import vista.VentanaPrincipal;

public class ControladorLogin {

    private final MDILogin vistaLogin;
    private final Login modeloLogin;

    public ControladorLogin(MDILogin vistaLogin) {
        this.vistaLogin = vistaLogin;
        this.modeloLogin = new Login();

        ConexionBD.getInstance();
        this.vistaLogin.getJButton1().addActionListener(e -> iniciarSesion());
        this.vistaLogin.getJButton2().addActionListener(e -> abrirRegistroUsuario());
        cargarCredencialesGuardadas();
    }

    public void iniciarSesion() {
<<<<<<< Updated upstream
        String usuario = vistaLogin.getTxtUsuario().getText();
        String contraseña = new String(vistaLogin.getTxtContraseña().getPassword());
        String contraseñaHash = Encriptador.hashSHA256(contraseña);
        
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vistaLogin, 
                "Por favor ingrese usuario y contraseña", 
                "Campos vacíos", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
=======
        String usuario = vistaLogin.getJTextField1().getText().trim();
        String contrasena = vistaLogin.getJTextField2().getText();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vistaLogin,
                    "Por favor ingrese usuario y contrasena",
                    "Campos vacios",
                    JOptionPane.WARNING_MESSAGE);
>>>>>>> Stashed changes
            return;
        }

        Iterator<Login> usuarios = modeloLogin.buscar(usuario);
        Login usuarioValido = null;

        while (usuarios.hasNext()) {
            Login u = usuarios.next();
<<<<<<< Updated upstream
            if (u.getNombreUsuario().equals(usuario) && 
                u.getContraseñaUsuario().equals(contraseñaHash)) {
                encontrado = true;
=======
            if (u.getNombreUsuario() != null
                    && u.getNombreUsuario().equals(usuario)
                    && u.getContrasenaUsuario() != null
                    && u.getContrasenaUsuario().equals(contrasena)) {
>>>>>>> Stashed changes
                usuarioValido = u;
                break;
            }
        }
<<<<<<< Updated upstream
        
        if (encontrado) {
            javax.swing.JOptionPane.showMessageDialog(vistaLogin, 
                "¡Bienvenido " + usuarioValido.getNombreUsuario() + "!", 
                "Login exitoso", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            // Guardar credenciales si el checkbox está seleccionado
            if (vistaLogin.getChkRecordarme().isSelected()) {
=======

        if (usuarioValido != null) {
            JOptionPane.showMessageDialog(vistaLogin,
                    "Bienvenido " + usuarioValido.getNombreUsuario() + "!",
                    "Login exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

            if (vistaLogin.getJCheckBox1().isSelected()) {
>>>>>>> Stashed changes
                guardarCredenciales(usuario);
            } else {
                eliminarCredencialesGuardadas();
            }

            VentanaPrincipal principal = new VentanaPrincipal(usuarioValido);
            principal.setVisible(true);
            vistaLogin.dispose();
        } else {
<<<<<<< Updated upstream
            javax.swing.JOptionPane.showMessageDialog(vistaLogin, 
                "Usuario o contraseña incorrectos", 
                "Error de autenticación", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            
            // Limpiar campos en caso de error
            vistaLogin.getTxtContraseña().setText("");
        }
    }
    
=======
            JOptionPane.showMessageDialog(vistaLogin,
                    "Usuario o contrasena incorrectos",
                    "Error de autenticacion",
                    JOptionPane.ERROR_MESSAGE);
            vistaLogin.getJTextField2().setText("");
        }
    }

>>>>>>> Stashed changes
    private void guardarCredenciales(String usuario) {
        try {
            Properties props = new Properties();
            props.setProperty("usuario", usuario);
<<<<<<< Updated upstream
            
            java.io.FileOutputStream fos = new java.io.FileOutputStream("credenciales.properties");
            props.store(fos, "Credenciales guardadas");
            fos.close();
            
            System.out.println("Credenciales guardadas exitosamente");
        } catch (java.io.IOException ex) {
=======
            props.setProperty("recordarme", "true");

            try (FileOutputStream fos = new FileOutputStream("credenciales.properties")) {
                props.store(fos, "Credenciales guardadas");
            }
        } catch (IOException ex) {
>>>>>>> Stashed changes
            System.err.println("Error al guardar credenciales: " + ex.getMessage());
        }
    }

    private void cargarCredencialesGuardadas() {
        File archivo = new File("credenciales.properties");
        if (!archivo.exists()) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(archivo)) {
            Properties props = new Properties();
            props.load(fis);

            String usuario = props.getProperty("usuario");
<<<<<<< Updated upstream
            
            if (usuario != null) {
                vistaLogin.getTxtUsuario().setText(usuario);
                vistaLogin.getChkRecordarme().setSelected(true);
=======
            String recordar = props.getProperty("recordarme", "false");

            if (usuario != null) {
                vistaLogin.getJTextField1().setText(usuario);
                vistaLogin.getJCheckBox1().setSelected(Boolean.parseBoolean(recordar));
>>>>>>> Stashed changes
            }
        } catch (IOException ex) {
            System.out.println("No hay credenciales guardadas");
        }
    }

    private void eliminarCredencialesGuardadas() {
        File archivo = new File("credenciales.properties");
        if (archivo.exists() && !archivo.delete()) {
            System.err.println("No se pudo eliminar el archivo de credenciales guardadas");
        }
    }

    public void abrirRegistroUsuario() {
        MDIRegistroUsuario registro = new MDIRegistroUsuario();
        registro.setVisible(true);
        vistaLogin.setVisible(false);
        new ControladorRegistroUsuario(registro, vistaLogin);
    }
}
