package vista;

import modelo.Usuario;
import javax.swing.*;

public class VentanaEstudiante extends JFrame {

    private JPanel panelEstudiante;
    private JLabel lblUsuario;
    private JButton btnSolicitarP;
    private JButton btnDevolverP;
    private JButton btnCerrarSesion;
    private JButton btnConsultar;

    private final Usuario usuario;

    //Constructor
    public VentanaEstudiante(Usuario usuario) {
        this.usuario = usuario;

        setContentPane(panelEstudiante);

        configurarVentana();
        configurarEventos();

        lblUsuario.setText("Usuario: " + usuario.getNombre() + " | Rol: " + usuario.getRol());
    }

    //Metodo para configurar la ventana
    private void configurarVentana() {

        setTitle("Biblioteca - Estudiante");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    //Metodo para configurar los eventos de los botones
    private void configurarEventos() {

        btnConsultar.addActionListener(e -> abrirConsulta());

        btnSolicitarP.addActionListener(e -> abrirSolicitud());

        btnDevolverP.addActionListener(e -> abrirDevolucion());

        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    //Metodo para abrir la ventana de consulta
    private void abrirConsulta() {

        new VentanaConsultarL().setVisible(true);
    }

    //Metodo para abrir la ventana de solicitud
    private void abrirSolicitud() {

        new VentanaSolicitarP(usuario).setVisible(true);
    }

    //Metodo para abrir la ventana de devolucion
    private void abrirDevolucion() {

        new VentanaDevolverP(usuario).setVisible(true);
    }

    //Metodo para cerrar sesion
    private void cerrarSesion() {

        new VentanaLogin().setVisible(true);

        dispose();
    }
}