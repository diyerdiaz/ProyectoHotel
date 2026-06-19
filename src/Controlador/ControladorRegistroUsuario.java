package Controlador;

import java.util.Iterator;
import javax.swing.JOptionPane;
import modelo.Cliente;
import modelo.ConexionBD;
import modelo.Login;
import vista.MDILogin;
<<<<<<< Updated upstream
import util.Encriptador;
import java.util.Iterator;
=======
import vista.MDIRegistroUsuario;
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
        String cedula = vistaRegistro.getTxtCedula().getText();
        String nombre = vistaRegistro.getTxtNombre().getText();
        String apellido = vistaRegistro.getTxtApellido().getText();
        String correo = vistaRegistro.getTxtCorreo().getText();
        String telefono = vistaRegistro.getTxtTelefono().getText();
        String usuario = vistaRegistro.getTxtUsuario().getText();
        String contraseña = new String(vistaRegistro.getTxtContraseña().getPassword());
        
        // Validar campos
        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || 
            correo.isEmpty() || telefono.isEmpty() || usuario.isEmpty() || contraseña.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, 
                "Por favor complete todos los campos", 
                "Campos vacíos", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
=======
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
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        
        if (usuarioExiste) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, 
                "El nombre de usuario ya existe", 
                "Usuario duplicado", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (cedula.length() > 10) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, 
                "La cédula no puede tener más de 10 dígitos", 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (telefono.length() > 10) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, 
                "El teléfono no puede tener más de 10 dígitos", 
                "Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
=======

>>>>>>> Stashed changes
        try {
            long cedulaNumero = Long.parseLong(cedula);
            long telefonoNumero = Long.parseLong(telefono);

            Login nuevoUsuario = new Login();
            nuevoUsuario.setNombreUsuario(usuario);
<<<<<<< Updated upstream
            nuevoUsuario.setContraseñaUsuario(Encriptador.hashSHA256(contraseña));
            nuevoUsuario.setRolUsuario("cliente"); // Rol por defecto
=======
            nuevoUsuario.setContrasenaUsuario(contrasena);
            nuevoUsuario.setRolUsuario("cliente");
>>>>>>> Stashed changes
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
