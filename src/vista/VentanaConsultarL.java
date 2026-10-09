package vista;

import controlador.ControladorLibros;
import modelo.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VentanaConsultarL extends JFrame {

    private JPanel panelConsultarL;
    private JLabel lblConsultar;
    private JTable tablaLibros;

    private final ControladorLibros controladorLibros;

    private DefaultTableModel modeloTabla;

    //Constructor
    public VentanaConsultarL() {

        controladorLibros = new ControladorLibros();

        setContentPane(panelConsultarL);

        configurarVentana();
        configurarTabla();

        cargarLibros();
    }

    //Metodo para configurar la ventana
    private void configurarVentana() {

        setTitle("Biblioteca - Consultar libros");
        setSize(850, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
                        "Stock"
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
    }

    //Metodo para cargar los libros en la tabla
    private void cargarLibros() {

        modeloTabla.setRowCount(0);

        List<Libro> libros = controladorLibros.listarLibros();

        for (Libro libro : libros) {

            modeloTabla.addRow(
                    new Object[]{
                            libro.getId(),
                            libro.getTitulo(),
                            libro.getAutor(),
                            libro.getIsbn(),
                            libro.getEditorial(),
                            libro.getStock()
                    }
            );
        }
    }
}