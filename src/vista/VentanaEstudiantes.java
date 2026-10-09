package vista;

import controlador.ControladorEstudiantes;
import modelo.Estudiante;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VentanaEstudiantes extends JFrame {

    private JPanel panelEstudiantes;
    private JTextField txtNombre;
    private JTextField txtRut;
    private JTextField txtCurso;
    private JTextField txtCorreo;
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JTable tablaEstudiantes;
    private JLabel lblVentEstudiantes;
    private JLabel lblNombre;
    private JLabel lblRut;
    private JLabel lblCurso;
    private JLabel lblCorreo;
    private JLabel lblListaE;

    private final ControladorEstudiantes controladorEstudiantes;

    private DefaultTableModel modeloTabla;

    private int idEstudianteSeleccionado = -1;

    //Constructor
    public VentanaEstudiantes() {

        controladorEstudiantes = new ControladorEstudiantes();

        setContentPane(panelEstudiantes);

        configurarVentana();
        configurarTabla();
        configurarEventos();

        cargarEstudiantes();
    }

    //Metodo para configurar la ventana
    private void configurarVentana() {

        setTitle("Biblioteca - Gestión de Estudiantes");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    //Metodo para configurar la tabla de estudiantes
    private void configurarTabla() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nombre",
                        "RUT",
                        "Curso",
                        "Correo"
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

        tablaEstudiantes.setModel(modeloTabla);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEstudiantes.setAutoCreateRowSorter(true);
    }

    //Metodo para configurar los eventos de los botones y la tabla
    private void configurarEventos() {

        btnAgregar.addActionListener(e -> guardarEstudiante());

        btnModificar.addActionListener(e -> editarEstudiante());

        btnEliminar.addActionListener(e -> eliminarEstudiante());

        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaEstudiantes.getSelectionModel().addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarEstudianteSeleccionado();
                    }
                });
    }

    //Metodo para cargar los estudiantes en la tabla
    private void cargarEstudiantes() {

        modeloTabla.setRowCount(0);

        List<Estudiante> estudiantes = controladorEstudiantes.listarEstudiantes();

        for (Estudiante estudiante : estudiantes) {

            modeloTabla.addRow(
                    new Object[]{
                            estudiante.getId(),
                            estudiante.getNombre(),
                            estudiante.getRut(),
                            estudiante.getCurso(),
                            estudiante.getCorreo()
                    }
            );
        }
    }

    //Metodo para guardar un estudiante
    private void guardarEstudiante() {

        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isEmpty()
                || rut.isEmpty()
                || curso.isEmpty()
                || correo.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes completar todos los campos.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Estudiante estudiante = new Estudiante(
                        nombre,
                        rut,
                        curso,
                        correo
                );

        if (controladorEstudiantes.agregarEstudiante(estudiante)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante agregado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarEstudiantes();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo agregar el estudiante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //Metodo para editar un estudiante
    private void editarEstudiante() {

        if (idEstudianteSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un estudiante de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isEmpty()
                || rut.isEmpty()
                || curso.isEmpty()
                || correo.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes completar todos los campos.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Estudiante estudiante = new Estudiante(
                        idEstudianteSeleccionado,
                        nombre,
                        rut,
                        curso,
                        correo
                );

        if (controladorEstudiantes.modificarEstudiante(estudiante)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante modificado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarEstudiantes();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo modificar el estudiante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //Metodo para eliminar un estudiante
    private void eliminarEstudiante() {

        if (idEstudianteSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un estudiante de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                        this,
                        "¿Seguro que deseas eliminar este estudiante?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (controladorEstudiantes.eliminarEstudiante(idEstudianteSeleccionado)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante eliminado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarEstudiantes();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el estudiante.\n"
                            + "Puede que tenga préstamos asociados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //Metodo para cargar los datos de un estudiante seleccionado
    private void cargarEstudianteSeleccionado() {

        int fila = tablaEstudiantes.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int filaModelo = tablaEstudiantes.convertRowIndexToModel(fila);

        Object valorId = modeloTabla.getValueAt(filaModelo, 0);
        Object valorNombre = modeloTabla.getValueAt(filaModelo, 1);
        Object valorRut = modeloTabla.getValueAt(filaModelo, 2);
        Object valorCurso = modeloTabla.getValueAt(filaModelo, 3);
        Object valorCorreo = modeloTabla.getValueAt(filaModelo, 4);

        if (valorId == null
                || valorNombre == null
                || valorRut == null
                || valorCurso == null
                || valorCorreo == null) {

            return;
        }

        try {
            idEstudianteSeleccionado = Integer.parseInt(valorId.toString());

        } catch (NumberFormatException e) {

            return;
        }

        txtNombre.setText(valorNombre.toString());
        txtRut.setText(valorRut.toString());
        txtCurso.setText(valorCurso.toString());
        txtCorreo.setText(valorCorreo.toString());
    }

    //Metodo para limpiar campos
    private void limpiarCampos() {

        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");

        idEstudianteSeleccionado = -1;
        tablaEstudiantes.clearSelection();
        txtNombre.requestFocus();
    }
}