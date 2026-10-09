package vista;

import controlador.ControladorLogin;
import modelo.Usuario;
import javax.swing.*;

public class VentanaLogin extends JFrame {

    private JPanel panelLogin;
    private JLabel lblVentLogin;
    private JLabel lblCorreo;
    private JTextField txtCorreo;
    private JLabel lblContrasena;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;

    private final ControladorLogin controlador;

    //Constructor
    public VentanaLogin() {

        controlador = new ControladorLogin();

        configurarVentana();
        configurarEventos();
    }

    //Metodo para configurar la ventana
    private void configurarVentana() {

        setTitle("Biblioteca - Inicio de Sesión");
        setContentPane(panelLogin);
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    //Metodo para configurar los eventos de los botones
    private void configurarEventos() {

        btnIniciarSesion.addActionListener(e -> iniciarSesion());
    }

    //Metodo para Iniciar sesion
    private void iniciarSesion() {

        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        try {
            Usuario usuario = controlador.iniciarSesion(correo, contrasena);

            if (usuario == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Correo o contraseña incorrectos.",
                        "Error de inicio de sesión",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            abrirVentanaSegunRol(usuario);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Datos inválidos",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    //Metodo para abrir ventana segun el rol
    private void abrirVentanaSegunRol(Usuario usuario) {

        if (usuario.getRol().equalsIgnoreCase("BIBLIOTECARIO")) {

            new VentanaBibliotecario(usuario).setVisible(true);

        } else if (usuario.getRol().equalsIgnoreCase("ESTUDIANTE")) {

            new VentanaEstudiante(usuario).setVisible(true);

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "El usuario no tiene un rol válido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        dispose();
    }
}