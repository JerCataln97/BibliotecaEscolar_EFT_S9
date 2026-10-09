package vista;

import controlador.ControladorReportes;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Reporte;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class VentanaReportes extends JFrame {

    private JPanel panelReportes;
    private JButton btnMasPrestados;
    private JButton btnHistorial;
    private JButton btnActivos;
    private JButton btnAtrasados;
    private JComboBox<Estudiante> cmbEstudiante;
    private JTable tablaReportes;
    private JLabel lblEstudiante;

    private final ControladorReportes controladorReportes;

    //Constructor
    public VentanaReportes() {

        controladorReportes = new ControladorReportes();

        configurarVentana();
        configurarTabla();
        cargarEstudiantes();
        configurarEventos();
    }

    //Metodo para configurar la ventana
    private void configurarVentana() {

        setTitle("Reportes - Biblioteca Escolar");
        setContentPane(panelReportes);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    //Metodo para configurar la tabla
    private void configurarTabla() {

        tablaReportes.setModel(new DefaultTableModel());
        tablaReportes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    //Metodo para configurar los eventos de los botones
    private void configurarEventos() {

        btnMasPrestados.addActionListener(e -> mostrarLibrosMasPrestados());

        btnHistorial.addActionListener(e -> mostrarHistorial());

        btnActivos.addActionListener(e -> mostrarPrestamosActivos());

        btnAtrasados.addActionListener(e -> mostrarPrestamosAtrasados());
    }

    //Metodo para cargar los estudiantes
    private void cargarEstudiantes() {

        cmbEstudiante.removeAllItems();

        List<Estudiante> estudiantes = controladorReportes.listarEstudiantes();

        for (Estudiante estudiante : estudiantes) {
            cmbEstudiante.addItem(estudiante);
        }
    }

    //Metodo para mostrar los libros mas prestados
    private void mostrarLibrosMasPrestados() {

        limpiarTabla();

        DefaultTableModel modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID Libro",
                                "Título",
                                "Cantidad de préstamos"
                        },
                        0
                );

        List<Reporte> lista = controladorReportes.obtenerLibrosMasPrestados();

        for (Reporte reporte : lista) {
            modeloTabla.addRow(
                    new Object[]{
                            reporte.getIdLibro(),
                            reporte.getTitulo(),
                            reporte.getCantidadPrestamos()
                    }
            );
        }

        tablaReportes.setModel(modeloTabla);
    }

    //Metodo para mostrar el historial de prestamos
    private void mostrarHistorial() {

        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();

        if (estudiante == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un estudiante."
            );

            return;
        }

        limpiarTabla();

        DefaultTableModel modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Libro",
                                "Fecha préstamo",
                                "Fecha vencimiento",
                                "Devuelto",
                                "Estado"
                        },
                        0
                );

        List<Prestamo> prestamos = controladorReportes.obtenerHistorialEstudiante(estudiante.getId());

        for (Prestamo prestamo : prestamos) {
            Libro libro = controladorReportes.obtenerLibro(prestamo.getIdLibro());

            String titulo =
                    libro != null
                            ? libro.getTitulo()
                            : "Libro no encontrado";

            modeloTabla.addRow(
                    new Object[]{
                            prestamo.getId(),
                            titulo,
                            prestamo.getFechaPrestamo(),
                            prestamo.getFechaDevolucion(),
                            prestamo.isDevuelto()
                                    ? "Sí"
                                    : "No",
                            determinarEstado(prestamo)
                    }
            );
        }

        tablaReportes.setModel(modeloTabla);
    }

    //Metodo para mostrar los prestamos activos
    private void mostrarPrestamosActivos() {

        limpiarTabla();

        DefaultTableModel modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Estudiante",
                                "Libro",
                                "Fecha préstamo",
                                "Fecha vencimiento",
                                "Estado"
                        },
                        0
                );

        List<Prestamo> prestamos = controladorReportes.obtenerPrestamosActivos();

        for (Prestamo prestamo : prestamos) {
            Estudiante estudiante = controladorReportes.obtenerEstudiante(prestamo.getIdEstudiante());
            Libro libro = controladorReportes.obtenerLibro(prestamo.getIdLibro());

            modeloTabla.addRow(
                    new Object[]{
                            prestamo.getId(),
                            estudiante != null
                                    ? estudiante.getNombre()
                                    : "No encontrado",
                            libro != null
                                    ? libro.getTitulo()
                                    : "No encontrado",
                            prestamo.getFechaPrestamo(),
                            prestamo.getFechaDevolucion(),
                            determinarEstado(prestamo)
                    }
            );
        }

        tablaReportes.setModel(modeloTabla);
    }

    //Metodo para mostrar los prestamos atrasados
    private void mostrarPrestamosAtrasados() {

        limpiarTabla();

        DefaultTableModel modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Estudiante",
                                "Libro",
                                "Fecha préstamo",
                                "Fecha vencimiento",
                                "Días de atraso"
                        },
                        0
                );

        List<Prestamo> prestamos = controladorReportes.obtenerPrestamosAtrasados();

        for (Prestamo prestamo : prestamos) {
            Estudiante estudiante = controladorReportes.obtenerEstudiante(prestamo.getIdEstudiante());
            Libro libro = controladorReportes.obtenerLibro(prestamo.getIdLibro());

            long diasAtraso = ChronoUnit.DAYS.between(prestamo.getFechaDevolucion(), LocalDate.now());

            modeloTabla.addRow(
                    new Object[]{
                            prestamo.getId(),
                            estudiante != null
                                    ? estudiante.getNombre()
                                    : "No encontrado",
                            libro != null
                                    ? libro.getTitulo()
                                    : "No encontrado",
                            prestamo.getFechaPrestamo(),
                            prestamo.getFechaDevolucion(),
                            diasAtraso
                    }
            );
        }

        tablaReportes.setModel(modeloTabla);
    }

    //Metodo para determinar el estado del prestamo
    private String determinarEstado(Prestamo prestamo) {

        if (prestamo.isDevuelto()) {
            return "DEVUELTO";
        }

        if (prestamo.getFechaDevolucion() != null
                && LocalDate.now().isAfter(
                prestamo.getFechaDevolucion()
        )) {
            return "ATRASADO";
        }
        return "VIGENTE";
    }

    //Metodo para limpiar la tabla
    private void limpiarTabla() {

        tablaReportes.setModel(new DefaultTableModel());
    }
}