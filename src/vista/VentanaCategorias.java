package vista;

import controlador.ControladorCategorias;
import modelo.Categoria;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VentanaCategorias extends JFrame {

    private JPanel panelCategorias;
    private JTextField txtNombre;
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JTable tablaCategorias;
    private JLabel lblVentCategorias;
    private JLabel lblNombre;
    private JLabel lblListaC;

    private final ControladorCategorias controladorCategorias;

    private DefaultTableModel modeloTabla;

    private int idCategoriaSeleccionada = -1;

    //Constructor
    public VentanaCategorias() {

        controladorCategorias = new ControladorCategorias();

        setContentPane(panelCategorias);

        configurarVentana();
        configurarTabla();
        configurarEventos();

        cargarCategorias();
    }

    //Metodo para configurar la ventana
    private void configurarVentana() {

        setTitle("Biblioteca - Gestión de Categorías");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    //Metodo para configurar la tabla de categorias
    private void configurarTabla() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nombre"
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

        tablaCategorias.setModel(modeloTabla);
        tablaCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCategorias.setAutoCreateRowSorter(true);
        tablaCategorias.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaCategorias.getColumnModel().getColumn(1).setPreferredWidth(300);
    }

    //Metodo para configurr los eventos de los botones y la tabla
    private void configurarEventos() {

        btnAgregar.addActionListener(e -> guardarCategoria());

        btnModificar.addActionListener(e -> editarCategoria());

        btnEliminar.addActionListener(e -> eliminarCategoria());

        tablaCategorias.getSelectionModel().addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarCategoriaSeleccionada();
                    }
                });
    }

    //Metodo para cargar las categorias en la tabla
    private void cargarCategorias() {

        modeloTabla.setRowCount(0);
        List<Categoria> categorias = controladorCategorias.listarCategorias();

        for (Categoria categoria : categorias) {

            modeloTabla.addRow(
                    new Object[]{
                            categoria.getId(),
                            categoria.getNombre()
                    }
            );
        }
    }

    //Metodo para guardar una categoria
    private void guardarCategoria() {

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el nombre de la categoría.",
                    "Biblioteca",
                    JOptionPane.WARNING_MESSAGE
            );

            txtNombre.requestFocus();

            return;
        }

        Categoria categoria = new Categoria(nombre);

        if (controladorCategorias.agregarCategoria(categoria)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Categoría registrada correctamente.",
                    "Biblioteca",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarCategorias();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar la categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //Metodo para editar una categoria
    private void editarCategoria() {

        if (idCategoriaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría de la tabla para editar.",
                    "Biblioteca",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el nombre de la categoría.",
                    "Biblioteca",
                    JOptionPane.WARNING_MESSAGE
            );

            txtNombre.requestFocus();

            return;
        }

        Categoria categoria = new Categoria(idCategoriaSeleccionada, nombre);

        if (controladorCategorias.modificarCategoria(categoria)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Categoría actualizada correctamente.",
                    "Biblioteca",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarCategorias();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar la categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //Metodo para eliminar una categoria
    private void eliminarCategoria() {

        if (idCategoriaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría de la tabla para eliminar.",
                    "Biblioteca",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar la categoría #" +
                                idCategoriaSeleccionada +
                                "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (controladorCategorias.eliminarCategoria(idCategoriaSeleccionada)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Categoría eliminada correctamente.",
                    "Biblioteca",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarCategorias();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar la categoría.\n" +
                            "Puede que tenga libros asociados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //Metodo para cargar datos de categoria seleccionada
    private void cargarCategoriaSeleccionada() {

        int fila = tablaCategorias.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int filaModelo = tablaCategorias.convertRowIndexToModel(fila);

        Object valorId = modeloTabla.getValueAt(filaModelo, 0);
        Object valorNombre = modeloTabla.getValueAt(filaModelo, 1);

        if (valorId == null || valorNombre == null) {
            return;
        }

        try {
            idCategoriaSeleccionada = Integer.parseInt(valorId.toString());

        } catch (NumberFormatException e) {

            return;
        }

        txtNombre.setText(valorNombre.toString());
    }

    //Metodo para limpiar campos
    private void limpiarCampos() {

        txtNombre.setText("");

        idCategoriaSeleccionada = -1;
        tablaCategorias.clearSelection();

        txtNombre.requestFocus();
    }
}