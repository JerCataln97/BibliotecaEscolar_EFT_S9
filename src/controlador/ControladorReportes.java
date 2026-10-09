package controlador;

import dao.EstudianteDAO;
import dao.LibroDAO;
import dao.PrestamoDAO;
import dao.impl.EstudianteDAOImpl;
import dao.impl.LibroDAOImpl;
import dao.impl.PrestamoDAOImpl;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Reporte;
import java.util.List;

public class ControladorReportes {

    private final PrestamoDAO prestamoDAO;
    private final EstudianteDAO estudianteDAO;
    private final LibroDAO libroDAO;

    //Constructor
    public ControladorReportes() {

        prestamoDAO = new PrestamoDAOImpl();
        estudianteDAO = new EstudianteDAOImpl();
        libroDAO = new LibroDAOImpl();
    }

    //Metodo para listar los estudiantes
    public List<Estudiante> listarEstudiantes() {
        return estudianteDAO.readAll();
    }

    //Metodo para obtener los libros mas prestados
    public List<Reporte> obtenerLibrosMasPrestados() {
        return prestamoDAO.buscarLibrosMasPrestados();
    }

    //Metodo para obtener el historial de prestamos de un estudiante
    public List<Prestamo> obtenerHistorialEstudiante(int idEstudiante) {
        return prestamoDAO.buscarPorEstudiante(idEstudiante);
    }

    //Metodo para obtener los prestamos activos
    public List<Prestamo> obtenerPrestamosActivos() {
        return prestamoDAO.buscarPrestamosActivos();
    }

    //Metodo para obtener los prestamos atrasados
    public List<Prestamo> obtenerPrestamosAtrasados() {
        return prestamoDAO.buscarPrestamosAtrasados();
    }

    //Metodo para obtener un estudiante por su ID
    public Estudiante obtenerEstudiante(int id) {
        return estudianteDAO.readById(id);
    }

    //Metodo para obtener un libro por su ID
    public Libro obtenerLibro(int id) {
        return libroDAO.readById(id);
    }
}
