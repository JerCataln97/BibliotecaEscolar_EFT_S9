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

public class VentanaDevolverP extends JFrame {

    private JPanel panelDevolverP;
    private JLabel lblVentDevolver;
    private JComboBox cmbDevolverP;
    private JButton btnDevolverP;
    private JLabel lblSeleccione;
    private JTable tablaPrestamos;
    private JLabel lblMisPrestamos;

    private final Usuario usuario;
    private final ControladorPrestamos controladorPrestamos;

    private List<Prestamo> prestamosActivos;
    private DefaultTableModel modeloTabla;

    //Constructor
    public VentanaDevolverP(Usuario usuario) {
        this.usuario = usuario;

        controladorPrestamos = new ControladorPrestamos();

        setContentPane(panelDevolverP);

        configurarVentana();
        configurarTabla();
        configurarEventos();

        cargarPrestamos();
    }

    //Metodo para configurar la ventana
    private void configurarVentana() {

        setTitle("Biblioteca - Devolver préstamo");
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
                    int column) {

                return false;
            }
        };

        tablaPrestamos.setModel(modeloTabla);
        tablaPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    //Metodo para configurar los eventos de los botones
    private void configurarEventos() {

        btnDevolverP.addActionListener(e -> devolverPrestamo());
    }

    //Metodo para cargar los prestamos
    private void cargarPrestamos() {

        cmbDevolverP.removeAllItems();
        modeloTabla.setRowCount(0);
        prestamosActivos = new ArrayList<>();

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

            if (!prestamo.isDevuelto()) {

                prestamosActivos.add(prestamo);
                cmbDevolverP.addItem(prestamo);
            }
        }

        btnDevolverP.setEnabled(!prestamosActivos.isEmpty());
    }

    //Metodo pra devolver un prestamo
    private void devolverPrestamo() {

        Prestamo prestamoSeleccionado = (Prestamo) cmbDevolverP.getSelectedItem();

        if (prestamoSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un préstamo.",
                    "Devolución",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Libro libro = controladorPrestamos.buscarLibro(prestamoSeleccionado.getIdLibro());

        if (libro == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el libro asociado al préstamo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea devolver el libro:\n\n"
                                + libro.getTitulo()
                                + "?",
                        "Confirmar devolución",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta != JOptionPane.YES_OPTION) {

            return;
        }

        prestamoSeleccionado.setDevuelto(true);
        btnDevolverP.setEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {

                    @Override
                    protected Boolean doInBackground() {

                        return controladorPrestamos.devolverPrestamo(prestamoSeleccionado);
                    }

                    @Override
                    protected void done() {

                        try {
                            boolean resultado = get();

                            if (resultado) {
                                JOptionPane.showMessageDialog(
                                        VentanaDevolverP.this,
                                        "Libro devuelto correctamente.\n\n"
                                                + "Libro: "
                                                + libro.getTitulo(),
                                        "Devolución registrada",
                                        JOptionPane.INFORMATION_MESSAGE
                                );

                                cargarPrestamos();

                            } else {
                                prestamoSeleccionado.setDevuelto(false);

                                JOptionPane.showMessageDialog(
                                        VentanaDevolverP.this,
                                        "No se pudo registrar la devolución.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );

                                cargarPrestamos();
                            }

                        } catch (Exception e) {
                            prestamoSeleccionado.setDevuelto(false);

                            JOptionPane.showMessageDialog(
                                    VentanaDevolverP.this,
                                    "Error al procesar la devolución: "
                                            + e.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            cargarPrestamos();

                        } finally {
                            btnDevolverP.setEnabled(prestamosActivos != null && !prestamosActivos.isEmpty());
                        }
                    }
                };

        worker.execute();
    }
}