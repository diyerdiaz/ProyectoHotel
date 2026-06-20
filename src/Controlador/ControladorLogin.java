package Controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.JOptionPane;
import modelo.ConexionBD;
import modelo.Login;
import util.Encriptador;
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
        String usuario = vistaLogin.getTxtUsuario().getText().trim();
        String contrasena = new String(vistaLogin.getTxtContraseña().getPassword());
        String contrasenaHash = Encriptador.hashSHA256(contrasena);

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vistaLogin,
                    "Por favor ingrese usuario y contraseña",
                    "Campos vacios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Iterator<Login> usuarios = modeloLogin.buscar(usuario);
        Login usuarioValido = null;

        while (usuarios.hasNext()) {
            Login u = usuarios.next();
            if (u.getNombreUsuario() != null
                    && u.getNombreUsuario().equals(usuario)
                    && u.getContrasenaUsuario() != null
                    && (u.getContrasenaUsuario().equals(contrasenaHash)
                    || u.getContrasenaUsuario().equals(contrasena))) {
                usuarioValido = u;
                break;
            }
        }

        if (usuarioValido != null) {
            if (contrasena.equals(usuarioValido.getContrasenaUsuario())) {
                usuarioValido.setContrasenaUsuario(contrasenaHash);
                usuarioValido.modificar();
            }

            JOptionPane.showMessageDialog(vistaLogin,
                    "Bienvenido " + usuarioValido.getNombreUsuario() + "!",
                    "Login exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

            if (vistaLogin.getChkRecordarme().isSelected()) {
                guardarCredenciales(usuario);
            } else {
                eliminarCredencialesGuardadas();
            }

            VentanaPrincipal principal = new VentanaPrincipal(usuarioValido);
            principal.setVisible(true);
            vistaLogin.dispose();
        } else {
            JOptionPane.showMessageDialog(vistaLogin,
                    "Usuario o contraseña incorrectos",
                    "Error de autenticacion",
                    JOptionPane.ERROR_MESSAGE);
            vistaLogin.getTxtContraseña().setText("");
        }
    }

    private void guardarCredenciales(String usuario) {
        try {
            Properties props = new Properties();
            props.setProperty("usuario", usuario);

            try (FileOutputStream fos = new FileOutputStream("credenciales.properties")) {
                props.store(fos, "Credenciales guardadas");
            }
        } catch (IOException ex) {
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
            if (usuario != null) {
                vistaLogin.getTxtUsuario().setText(usuario);
                vistaLogin.getChkRecordarme().setSelected(true);
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
