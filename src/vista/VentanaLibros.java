package vista;

import controlador.ControladorLibros;
import modelo.Categoria;
import modelo.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VentanaLibros extends JFrame {

    private JPanel panelLibros;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtISBN;
    private JTextField txtEditorial;
    private JTextField txtStock;
    private JComboBox<Categoria> cmbCategoria;
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnLimpiar;
    private JButton btnEliminar;
    private JTable tablaLibros;
    private JLabel lblVentLibros;
    private JLabel lblTitulo;
    private JLabel lblAutor;
    private JLabel lblISBN;
    private JLabel lblEditorial;
    private JLabel lblStock;
    private JLabel lblCategoria;
    private JLabel lblListaL;

    private final ControladorLibros controladorLibros;

    private DefaultTableModel modeloTabla;

    private int idLibroSeleccionado = -1;

    //Constructor
    public VentanaLibros() {

        controladorLibros = new ControladorLibros();

        setContentPane(panelLibros);

        configurarVentana();
        configurarTabla();
        configurarEventos();

        cargarCategorias();
        cargarLibros();
    }

    //Metodo para configurar la ventana
    private void configurarVentana() {

        setTitle("Biblioteca - Gestión de Libros");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    //Metodo para configurar la tabla de libros
    private void configurarTabla() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Título",
                        "Autor",
                        "ISBN",
                        "Editorial",
                        "Stock",
                        "Categoría"
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

        tablaLibros.setModel(modeloTabla);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaLibros.setAutoCreateRowSorter(true);
        tablaLibros.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaLibros.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaLibros.getColumnModel().getColumn(2).setPreferredWidth(120);
    }

    //Metodo para configurar los eventos de los botones y la tabla
    private void configurarEventos() {

        btnAgregar.addActionListener(e -> guardarLibro());

        btnModificar.addActionListener(e -> editarLibro());

        btnEliminar.addActionListener(e -> eliminarLibro());

        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaLibros.getSelectionModel().addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarLibroSeleccionado();
                    }
                });
    }

    //Metodo para cargar las categorias
    private void cargarCategorias() {

        cmbCategoria.removeAllItems();

        List<Categoria> categorias = controladorLibros.listarCategorias();

        for (Categoria categoria : categorias) {
            cmbCategoria.addItem(categoria);
        }
    }

    //Metodo para seleccionar una categoria
    private void seleccionarCategoria(String nombreCategoria) {

        for (int i = 0;
             i < cmbCategoria.getItemCount();
             i++) {

            Categoria categoria = cmbCategoria.getItemAt(i);

            if (categoria.getNombre().equals(nombreCategoria)) {
                cmbCategoria.setSelectedIndex(i);

                break;
            }
        }
    }

    //Metodo para cargar los libros en la tabla
    private void cargarLibros() {

        modeloTabla.setRowCount(0);

        List<Libro> libros = controladorLibros.listarLibros();

        for (Libro libro : libros) {
            String nombreCategoria = "";

            Categoria categoria = controladorLibros.buscarCategoria(libro.getIdCategoria());

            if (categoria != null) {
                nombreCategoria = categoria.getNombre();
            }

            modeloTabla.addRow(
                    new Object[]{
                            libro.getId(),
                            libro.getTitulo(),
                            libro.getAutor(),
                            libro.getIsbn(),
                            libro.getEditorial(),
                            libro.getStock(),
                            nombreCategoria
                    }
            );
        }
    }

    //Metodo para guardar un libro
    private void guardarLibro() {

        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtISBN.getText().trim();
        String editorial = txtEditorial.getText().trim();
        String stockTexto = txtStock.getText().trim();

        if (titulo.isEmpty()
                || autor.isEmpty()
                || isbn.isEmpty()
                || editorial.isEmpty()
                || stockTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes completar todos los campos.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();

        if (categoria == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar una categoría.",
                    "Categoría",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int stock;

        try {
            stock = Integer.parseInt(stockTexto);

            if (stock < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "El stock debe ser un número entero mayor o igual a 0.",
                    "Stock inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Libro libro = new Libro(
                        titulo,
                        autor,
                        isbn,
                        editorial,
                        stock,
                        categoria.getId()
                );

        if (controladorLibros.agregarLibro(libro)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Libro agregado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarLibros();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo agregar el libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //Metodo para editar un libro
    private void editarLibro() {

        if (idLibroSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un libro de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtISBN.getText().trim();
        String editorial = txtEditorial.getText().trim();
        String stockTexto = txtStock.getText().trim();

        if (titulo.isEmpty()
                || autor.isEmpty()
                || isbn.isEmpty()
                || editorial.isEmpty()
                || stockTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes completar todos los campos.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();

        if (categoria == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes seleccionar una categoría.",
                    "Categoría",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int stock;

        try {
            stock = Integer.parseInt(stockTexto);

            if (stock < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "El stock debe ser un número entero mayor o igual a 0.",
                    "Stock inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Libro libro = new Libro(
                        idLibroSeleccionado,
                        titulo,
                        autor,
                        isbn,
                        editorial,
                        stock,
                        categoria.getId()
                );

        if (controladorLibros.modificarLibro(libro)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Libro modificado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarLibros();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo modificar el libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //Metodo para eliminar un libro
    private void eliminarLibro() {

        if (idLibroSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un libro de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                        this,
                        "¿Seguro que deseas eliminar este libro?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (controladorLibros.eliminarLibro(idLibroSeleccionado)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Libro eliminado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarLibros();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el libro.\n"
                            + "Puede que tenga préstamos asociados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //Metodo para cargar los datos de un libro seleccionado
    private void cargarLibroSeleccionado() {

        int fila = tablaLibros.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int filaModelo = tablaLibros.convertRowIndexToModel(fila);

        Object valorId = modeloTabla.getValueAt(filaModelo, 0);
        Object valorTitulo = modeloTabla.getValueAt(filaModelo, 1);
        Object valorAutor = modeloTabla.getValueAt(filaModelo, 2);
        Object valorISBN = modeloTabla.getValueAt(filaModelo, 3);
        Object valorEditorial = modeloTabla.getValueAt(filaModelo, 4);
        Object valorStock = modeloTabla.getValueAt(filaModelo, 5);
        Object valorCategoria = modeloTabla.getValueAt(filaModelo, 6);

        if (valorId == null
                || valorTitulo == null
                || valorAutor == null
                || valorISBN == null
                || valorEditorial == null
                || valorStock == null
                || valorCategoria == null) {

            return;
        }

        try {
            idLibroSeleccionado = Integer.parseInt(valorId.toString());

        } catch (NumberFormatException e) {

            return;
        }

        txtTitulo.setText(valorTitulo.toString());
        txtAutor.setText(valorAutor.toString());
        txtISBN.setText(valorISBN.toString());
        txtEditorial.setText(valorEditorial.toString());
        txtStock.setText(valorStock.toString());

        seleccionarCategoria(valorCategoria.toString());
    }

    //Metodo para limpiar campos
    private void limpiarCampos() {

        txtTitulo.setText("");
        txtAutor.setText("");
        txtISBN.setText("");
        txtEditorial.setText("");
        txtStock.setText("");

        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }

        idLibroSeleccionado = -1;
        tablaLibros.clearSelection();
        txtTitulo.requestFocus();
    }
}