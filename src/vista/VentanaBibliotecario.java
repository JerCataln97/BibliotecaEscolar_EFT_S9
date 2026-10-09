package vista;

import modelo.Usuario;
import javax.swing.*;

public class VentanaBibliotecario extends JFrame {

    private JPanel panelBibliotecario;
    private JLabel lblUsuario;
    private JButton btnLibros;
    private JButton btnEstudiantes;
    private JButton btnCategorias;
    private JButton btnPrestamos;
    private JButton btnCerrarSesion;
    private JButton btnReportes;

    private final Usuario usuario;

    //Constructor
    public VentanaBibliotecario(Usuario usuario) {
        this.usuario = usuario;

        setContentPane(panelBibliotecario);

        configurarVentana();
        configurarEventos();

        lblUsuario.setText("Usuario: " + usuario.getNombre() + " | Rol: " + usuario.getRol());
    }

    //Metodo para configurar la ventana
    private void configurarVentana() {

        setTitle("Biblioteca - Panel Bibliotecario");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    //Metodo para onfigurar los eventos de los botones
    private void configurarEventos() {

        btnLibros.addActionListener(e -> abrirLibros());

        btnEstudiantes.addActionListener(e -> abrirEstudiantes());

        btnCategorias.addActionListener(e -> abrirCategorias());

        btnReportes.addActionListener(e -> abrirReportes());

        btnPrestamos.addActionListener(e -> abrirPrestamos());

        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    //Metodo para abrir la ventana de libros
    private void abrirLibros() {

        new VentanaLibros().setVisible(true);
    }

    //Metodo para abrir la ventana de estudiantes
    private void abrirEstudiantes() {

        new VentanaEstudiantes().setVisible(true);
    }

    //Metodo para abrir la ventana de categorias
    private void abrirCategorias() {

        new VentanaCategorias().setVisible(true);
    }

    //Metodo para abrir la ventana de reportes
    private void abrirReportes() {

        new VentanaReportes().setVisible(true);
    }

    //Metodo para abrir la ventana de prestamos
    private void abrirPrestamos() {

        new VentanaPrestamos().setVisible(true);
    }

    //Metodo para cerrar sesion
    private void cerrarSesion() {

        new VentanaLogin().setVisible(true);

        dispose();
    }
}