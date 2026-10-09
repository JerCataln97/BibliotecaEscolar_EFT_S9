package vista;

import controlador.ControladorPrestamos;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentanaSolicitarP extends JFrame {

    private JComboBox<Libro> cmbSolicitarP;
    private JPanel panelSolicitarP;
    private JLabel lblVentSolicitar;
    private JLabel lblSeleccione;
    private JButton btnSolicitarP;
    private JLabel lblMisPrestamos;
    private JTable tablaPrestamos;

    private final Usuario usuario;
    private final ControladorPrestamos controladorPrestamos;

    private List<Libro> librosDisponibles;
    private DefaultTableModel modeloTabla;

    //Constructor
    public VentanaSolicitarP(Usuario usuario) {
        this.usuario = usuario;

        controladorPrestamos = new ControladorPrestamos();

        configurarVentana();
        configurarTabla();
        configurarEventos();

        cargarLibrosDisponibles();
        cargarPrestamos();
    }

    //Metodo para configurar la ventana
    private void configurarVentana() {

        setTitle("Biblioteca - Solicitar préstamo");
        setContentPane(panelSolicitarP);
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    //Metodo para configurar la tabla de prestamos
    private void configurarTabla() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Libro",
                        "Fecha préstamo",
                        "Fecha límite",
                        "Estado"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        tablaPrestamos.setModel(modeloTabla);
        tablaPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    //Metodo para configurar los eventos de los botones
    private void configurarEventos() {

        btnSolicitarP.addActionListener(e -> solicitarPrestamo());
    }

    //Metodo para cargar los libros disponibles
    private void cargarLibrosDisponibles() {

        cmbSolicitarP.removeAllItems();

        librosDisponibles = new ArrayList<>();
        List<Libro> libros = controladorPrestamos.listarLibros();

        for (Libro libro : libros) {

            if (libro.getStock() > 0) {
                librosDisponibles.add(libro);
                cmbSolicitarP.addItem(libro);
            }
        }

        if (librosDisponibles.isEmpty()) {
            btnSolicitarP.setEnabled(false);

            JOptionPane.showMessageDialog(
                    this,
                    "No hay libros disponibles para préstamo.",
                    "Sin disponibilidad",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {
            btnSolicitarP.setEnabled(true);
        }
    }

    //Metodo para cargar los prestamos
    private void cargarPrestamos() {

        modeloTabla.setRowCount(0);

        List<Prestamo> prestamos = controladorPrestamos.listarPrestamos();

        for (Prestamo prestamo : prestamos) {

            if (prestamo.getIdEstudiante() != usuario.getId()) {

                continue;
            }

            Libro libro = controladorPrestamos.buscarLibro(prestamo.getIdLibro());

            String nombreLibro;

            if (libro != null) {
                nombreLibro = libro.getTitulo();

            } else {
                nombreLibro = "Libro no encontrado";
            }

            String estado;

            if (prestamo.isDevuelto()) {
                estado = "Devuelto";

            } else if (
                    prestamo.getFechaDevolucion() != null
                            && prestamo.getFechaDevolucion()
                            .isBefore(LocalDate.now())
            ) {
                estado = "Atrasado";

            } else {
                estado = "Activo";
            }

            modeloTabla.addRow(
                    new Object[]{
                            prestamo.getId(),
                            nombreLibro,
                            prestamo.getFechaPrestamo(),
                            prestamo.getFechaDevolucion(),
                            estado
                    }
            );
        }
    }

    //Metodo para solicitar un prestamo
    private void solicitarPrestamo() {

        Libro libroSeleccionado = (Libro) cmbSolicitarP.getSelectedItem();

        if (libroSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un libro.",
                    "Préstamo",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea solicitar el libro:\n\n"
                                + libroSeleccionado.getTitulo()
                                + "?",
                        "Confirmar préstamo",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta != JOptionPane.YES_OPTION) {

            return;
        }

        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = fechaPrestamo.plusDays(7);

        Prestamo prestamo = new Prestamo(
                        usuario.getId(),
                        libroSeleccionado.getId(),
                        fechaPrestamo,
                        fechaDevolucion,
                        false
                );

        btnSolicitarP.setEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {

                    @Override
                    protected Boolean doInBackground() {

                        return controladorPrestamos.registrarPrestamo(prestamo);
                    }

                    @Override
                    protected void done() {

                        try {
                            boolean creado = get();

                            if (!creado) {
                                JOptionPane.showMessageDialog(
                                        VentanaSolicitarP.this,
                                        "No se pudo registrar el préstamo.\n"
                                                + "Es posible que el libro "
                                                + "ya no tenga stock.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );

                                return;
                            }

                            JOptionPane.showMessageDialog(
                                    VentanaSolicitarP.this,
                                    "Préstamo solicitado correctamente.\n\n"
                                            + "Libro: "
                                            + libroSeleccionado.getTitulo()
                                            + "\nFecha del préstamo: "
                                            + fechaPrestamo
                                            + "\nFecha límite: "
                                            + fechaDevolucion,
                                    "Préstamo registrado",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                            cargarLibrosDisponibles();
                            cargarPrestamos();

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(
                                    VentanaSolicitarP.this,
                                    "Error al procesar el préstamo:\n"
                                            + e.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                        } finally {
                            btnSolicitarP.setEnabled(librosDisponibles != null && !librosDisponibles.isEmpty());
                        }
                    }
                };

        worker.execute();
    }
}