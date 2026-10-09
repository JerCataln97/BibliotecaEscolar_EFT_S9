package vista;

import controlador.ControladorPrestamos;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class VentanaPrestamos extends JFrame {

    private JPanel panelPrestamos;
    private JLabel lblVentPrestamos;
    private JLabel lblEstudiante;
    private JLabel lblLibro;
    private JLabel lblFechaPrestamo;
    private JLabel lblFechaDevolucion;
    private JLabel lblDevuelto;
    private JLabel lblListaP;
    private JComboBox<Estudiante> cmbEstudiante;
    private JComboBox<Libro> cmbLibro;
    private JTextField txtFechaPrestamo;
    private JTextField txtFechaDevolucion;
    private JCheckBox chkDevuelto;
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JTable tablaPrestamos;

    private final ControladorPrestamos controladorPrestamos;

    private int idPrestamoSeleccionado = -1;

    //Constructor
    public VentanaPrestamos() {

        controladorPrestamos = new ControladorPrestamos();

        configurarVentana();
        configurarTabla();
        cargarEstudiantes();
        cargarLibros();
        cargarPrestamos();
        configurarEventos();

        txtFechaPrestamo.setText(LocalDate.now().toString());
    }

    //Metodo para configurar la ventana
    private void configurarVentana() {

        setTitle("Biblioteca - Gestión de Préstamos");
        setContentPane(panelPrestamos);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    //Metodo para configurar la tabla de prestamos
    private void configurarTabla() {

        tablaPrestamos.setModel(
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Estudiante",
                                "Libro",
                                "Fecha préstamo",
                                "Fecha devolución",
                                "Devuelto"
                        },
                        0
                )
        );

        tablaPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPrestamos.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                cargarPrestamoSeleccionado();
            }
        });
    }

    //Metodo para configurar los eventos de los botones
    private void configurarEventos() {

        btnAgregar.addActionListener(e -> guardarPrestamo());

        btnModificar.addActionListener(e -> editarPrestamo());

        btnEliminar.addActionListener(e -> eliminarPrestamo());

        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    //Metodo para cargar los estudiantes
    private void cargarEstudiantes() {

        cmbEstudiante.removeAllItems();

        List<Estudiante> estudiantes = controladorPrestamos.listarEstudiantes();

        for (Estudiante estudiante : estudiantes) {
            cmbEstudiante.addItem(estudiante);
        }
    }

    //Metodo para cargar los libros
    private void cargarLibros() {

        cmbLibro.removeAllItems();

        List<Libro> libros = controladorPrestamos.listarLibros();

        for (Libro libro : libros) {
            cmbLibro.addItem(libro);
        }
    }

    //Metodo para cargar los prestamos
    private void cargarPrestamos() {

        DefaultTableModel modeloTabla = (DefaultTableModel) tablaPrestamos.getModel();

        modeloTabla.setRowCount(0);

        List<Prestamo> prestamos = controladorPrestamos.listarPrestamos();

        for (Prestamo prestamo : prestamos) {
            String nombreEstudiante = "";

            Estudiante estudiante = controladorPrestamos.buscarEstudiante(prestamo.getIdEstudiante());

            if (estudiante != null) {
                nombreEstudiante = estudiante.getNombre();
            }

            String tituloLibro = "";

            Libro libro = controladorPrestamos.buscarLibro(prestamo.getIdLibro());

            if (libro != null) {
                tituloLibro = libro.getTitulo();
            }

            modeloTabla.addRow(
                    new Object[]{
                            prestamo.getId(),
                            nombreEstudiante,
                            tituloLibro,
                            prestamo.getFechaPrestamo(),
                            prestamo.getFechaDevolucion(),
                            prestamo.isDevuelto()
                    }
            );
        }
    }

    //Metodo para guardar un prestamo
    private void guardarPrestamo() {

        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();
        Libro libro = (Libro) cmbLibro.getSelectedItem();

        if (estudiante == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar un estudiante.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (libro == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar un libro.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String fechaTexto = txtFechaPrestamo.getText().trim();

        LocalDate fechaPrestamo;

        try {
            fechaPrestamo = LocalDate.parse(fechaTexto);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "La fecha debe tener el formato:\n"
                            + "AAAA-MM-DD",
                    "Fecha inválida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (libro.getStock() <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "El libro seleccionado no tiene stock disponible.",
                    "Sin stock",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        LocalDate fechaDevolucion = fechaPrestamo.plusDays(7);

        Prestamo prestamo = new Prestamo(
                estudiante.getId(),
                libro.getId(),
                fechaPrestamo,
                fechaDevolucion,
                false
        );

        Thread hiloPrestamo = new Thread(() -> {

            boolean resultado = controladorPrestamos.registrarPrestamo(prestamo);

            SwingUtilities.invokeLater(() -> {

                if (resultado) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Préstamo registrado correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    limpiarCampos();
                    cargarLibros();
                    cargarPrestamos();

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No se pudo registrar el préstamo.\n"
                                    + "Puede que el libro ya no tenga stock disponible.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

        });

        hiloPrestamo.start();
    }

    //Metodo para editar un prestamo
    private void editarPrestamo() {

        if (idPrestamoSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un préstamo de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();
        Libro libroNuevo = (Libro) cmbLibro.getSelectedItem();

        if (estudiante == null || libroNuevo == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar estudiante y libro.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        LocalDate fechaPrestamo;

        try {
            fechaPrestamo = LocalDate.parse(txtFechaPrestamo.getText().trim());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "La fecha debe tener el formato AAAA-MM-DD.",
                    "Fecha inválida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        LocalDate fechaDevolucion = null;

        if (!txtFechaDevolucion.getText().trim().isEmpty()) {

            try {
                fechaDevolucion = LocalDate.parse(txtFechaDevolucion.getText().trim());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "La fecha de devolución no es válida.",
                        "Fecha inválida",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        }

        boolean devuelto = chkDevuelto.isSelected();

        Prestamo prestamo = new Prestamo(
                        idPrestamoSeleccionado,
                        estudiante.getId(),
                        libroNuevo.getId(),
                        fechaPrestamo,
                        fechaDevolucion,
                        devuelto
                );

        Thread hiloModificacion = new Thread(() -> {

            boolean resultado = controladorPrestamos.modificarPrestamo(prestamo);

            SwingUtilities.invokeLater(() -> {

                if (resultado) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Préstamo modificado correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    limpiarCampos();
                    cargarLibros();
                    cargarPrestamos();

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No se pudo modificar el préstamo.\n"
                                    + "Verifica el stock del libro seleccionado.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

        });

        hiloModificacion.start();
    }

    //Metodo para eliminar un prestamo
    private void eliminarPrestamo() {

        if (idPrestamoSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un préstamo de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                        this,
                        "¿Seguro que deseas eliminar este préstamo?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta != JOptionPane.YES_OPTION) {

            return;
        }

        Thread hiloEliminacion = new Thread(() -> {

            boolean resultado = controladorPrestamos.eliminarPrestamo(idPrestamoSeleccionado);

            SwingUtilities.invokeLater(() -> {

                if (resultado) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Préstamo eliminado correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    limpiarCampos();
                    cargarPrestamos();
                    cargarLibros();

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No se pudo eliminar el préstamo.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

        });

        hiloEliminacion.start();
    }

    //Metodo para cargar los datos de un prestamo seleccionado
    private void cargarPrestamoSeleccionado() {

        int fila = tablaPrestamos.getSelectedRow();

        if (fila < 0) {

            return;
        }

        idPrestamoSeleccionado = (int) tablaPrestamos.getValueAt(fila, 0);
        String nombreEstudiante = tablaPrestamos.getValueAt(fila, 1).toString();
        String tituloLibro = tablaPrestamos.getValueAt(fila, 2).toString();
        txtFechaPrestamo.setText(tablaPrestamos.getValueAt(fila, 3).toString());
        Object fechaDevolucion = tablaPrestamos.getValueAt(fila, 4);

        if (fechaDevolucion != null) {
            txtFechaDevolucion.setText(fechaDevolucion.toString());

        } else {
            txtFechaDevolucion.setText("");
        }

        Object devuelto = tablaPrestamos.getValueAt(fila, 5);
        chkDevuelto.setSelected(Boolean.parseBoolean(devuelto.toString()));
        seleccionarEstudiante(nombreEstudiante);
        seleccionarLibro(tituloLibro);
    }

    //Metodo para seleecionar un estudiante
    private void seleccionarEstudiante(String nombreEstudiante) {

        for (int i = 0;
             i < cmbEstudiante.getItemCount();
             i++) {

            Estudiante estudiante = cmbEstudiante.getItemAt(i);

            if (estudiante.getNombre().equals(nombreEstudiante)) {
                cmbEstudiante.setSelectedIndex(i);

                break;
            }
        }
    }

    //Metodo para seleccionar un libro
    private void seleccionarLibro(String tituloLibro) {

        for (int i = 0;
             i < cmbLibro.getItemCount();
             i++) {

            Libro libro = cmbLibro.getItemAt(i);

            if (libro.getTitulo().equals(tituloLibro)) {
                cmbLibro.setSelectedIndex(i);

                break;
            }
        }
    }

    //Metodo para limpiar campos
    private void limpiarCampos() {

        idPrestamoSeleccionado = -1;

        if (cmbEstudiante.getItemCount() > 0) {
            cmbEstudiante.setSelectedIndex(0);
        }

        if (cmbLibro.getItemCount() > 0) {
            cmbLibro.setSelectedIndex(0);
        }

        txtFechaPrestamo.setText(LocalDate.now().toString());
        txtFechaDevolucion.setText("");
        chkDevuelto.setSelected(false);
        tablaPrestamos.clearSelection();
    }
}