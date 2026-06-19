/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import modelo.Login;
import modelo.Cliente;
import modelo.ConexionBD;
import vista.MDIRegistroUsuario;
import vista.MDILogin;
import util.Encriptador;
import java.util.Iterator;

/**
 *
 * @author alejo
 */
public class ControladorRegistroUsuario {
    
    private MDIRegistroUsuario vistaRegistro;
    private Login modeloLogin;
    private Cliente modeloCliente;
    private MDILogin vistaLogin;
    
    public ControladorRegistroUsuario(MDIRegistroUsuario vistaRegistro, MDILogin vistaLogin) {
        this.vistaRegistro = vistaRegistro;
        this.vistaLogin = vistaLogin;
        this.modeloLogin = new Login();
        this.modeloCliente = new Cliente();
        
        // Inicializar la conexión a la base de datos
        ConexionBD.getInstance();
        
        // Configurar listeners
        this.vistaRegistro.getBtnSiguiente().addActionListener(e -> registrarUsuario());
        this.vistaRegistro.getLblIniciarSesion().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                volverAlLogin();
            }
        });
    }
    
    public void registrarUsuario() {
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
            return;
        }
        
        // Validar que el usuario no exista
        Iterator<Login> usuarios = modeloLogin.buscar(usuario);
        boolean usuarioExiste = false;
        
        while (usuarios.hasNext()) {
            Login u = usuarios.next();
            if (u.getNombreUsuario().equals(usuario)) {
                usuarioExiste = true;
                break;
            }
        }
        
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
        
        try {
            // Insertar usuario en la tabla Usuarios
            Login nuevoUsuario = new Login();
            nuevoUsuario.setNombreUsuario(usuario);
            nuevoUsuario.setContraseñaUsuario(Encriptador.hashSHA256(contraseña));
            nuevoUsuario.setRolUsuario("cliente"); // Rol por defecto
            nuevoUsuario.insertar();
            
            // Obtener el ID del usuario recién insertado
            Iterator<Login> usuariosInsertados = modeloLogin.buscar(usuario);
            int idUsuario = 0;
            while (usuariosInsertados.hasNext()) {
                Login u = usuariosInsertados.next();
                if (u.getNombreUsuario().equals(usuario)) {
                    idUsuario = u.getIdUsuario();
                    break;
                }
            }
            
            // Insertar cliente en la tabla Cliente
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setNombre(nombre);
            nuevoCliente.setApellido(apellido);
            nuevoCliente.setDocumento(Integer.parseInt(cedula));
            nuevoCliente.setCorreo(correo);
            nuevoCliente.setTelefono(Integer.parseInt(telefono));
            nuevoCliente.setDireccion("No especificada"); // Campo opcional
            nuevoCliente.setIdUsuario(idUsuario);
            nuevoCliente.insertar();
            
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, 
                "¡Registro exitoso! Ahora puede iniciar sesión", 
                "Registro completado", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            // Volver al login
            volverAlLogin();
            
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, 
                "La cédula y teléfono deben ser números", 
                "Error de formato", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, 
                "Error al registrar: " + ex.getMessage(), 
                "Error de registro", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void volverAlLogin() {
        vistaRegistro.dispose();
        vistaLogin.setVisible(true);
    }
}
