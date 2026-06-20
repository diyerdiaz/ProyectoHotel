package Controlador;

import java.util.Iterator;
import javax.swing.JOptionPane;
import modelo.Cliente;
import modelo.ConexionBD;
import modelo.Login;
import util.Encriptador;
import vista.MDIRegistroUsuario;
import vista.MDILogin;

public class ControladorRegistroUsuario {

    private final MDIRegistroUsuario vistaRegistro;
    private final Login modeloLogin;
    private final Cliente modeloCliente;
    private final MDILogin vistaLogin;

    public ControladorRegistroUsuario(MDIRegistroUsuario vistaRegistro, MDILogin vistaLogin) {
        this.vistaRegistro = vistaRegistro;
        this.vistaLogin = vistaLogin;
        this.modeloLogin = new Login();
        this.modeloCliente = new Cliente();

        ConexionBD.getInstance();
        this.vistaRegistro.getBtnSiguiente().addActionListener(e -> registrarUsuario());
        this.vistaRegistro.getLblIniciarSesion().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                volverAlLogin();
            }
        });
    }

    public void registrarUsuario() {
        String cedula = vistaRegistro.getTxtCedula().getText().trim();
        String nombre = vistaRegistro.getTxtNombre().getText().trim();
        String apellido = vistaRegistro.getTxtApellido().getText().trim();
        String correo = vistaRegistro.getTxtCorreo().getText().trim();
        String telefono = vistaRegistro.getTxtTelefono().getText().trim();
        String usuario = vistaRegistro.getTxtUsuario().getText().trim();
        String contrasena = vistaRegistro.getTxtContrasena().getText();

        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty()
                || correo.isEmpty() || telefono.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vistaRegistro,
                    "Por favor complete todos los campos",
                    "Campos vacios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Iterator<Login> usuarios = modeloLogin.buscar(usuario);
        while (usuarios.hasNext()) {
            Login u = usuarios.next();
            if (usuario.equals(u.getNombreUsuario())) {
                JOptionPane.showMessageDialog(vistaRegistro,
                        "El nombre de usuario ya existe",
                        "Usuario duplicado",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            long cedulaNumero = Long.parseLong(cedula);
            long telefonoNumero = Long.parseLong(telefono);

            Login nuevoUsuario = new Login();
            nuevoUsuario.setNombreUsuario(usuario);
            nuevoUsuario.setContrasenaUsuario(Encriptador.hashSHA256(contrasena));
            nuevoUsuario.setRolUsuario("cliente");
            nuevoUsuario.insertar();

            Iterator<Login> usuariosInsertados = modeloLogin.buscar(usuario);
            int idUsuario = 0;
            while (usuariosInsertados.hasNext()) {
                Login u = usuariosInsertados.next();
                if (usuario.equals(u.getNombreUsuario())) {
                    idUsuario = u.getIdUsuario();
                    break;
                }
            }

            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setNombre(nombre);
            nuevoCliente.setApellido(apellido);
            nuevoCliente.setDocumento(cedulaNumero);
            nuevoCliente.setCorreo(correo);
            nuevoCliente.setTelefono(telefonoNumero);
            nuevoCliente.setDireccion("No especificada");
            nuevoCliente.setIdUsuario(idUsuario);
            nuevoCliente.insertar();

            JOptionPane.showMessageDialog(vistaRegistro,
                    "Registro exitoso. Ahora puede iniciar sesion",
                    "Registro completado",
                    JOptionPane.INFORMATION_MESSAGE);

            volverAlLogin();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaRegistro,
                    "La cedula y el telefono deben ser numeros enteros sin signos ni espacios",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaRegistro,
                    "Error al registrar: " + ex.getMessage(),
                    "Error de registro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void volverAlLogin() {
        vistaRegistro.dispose();
        vistaLogin.setVisible(true);
    }
}
