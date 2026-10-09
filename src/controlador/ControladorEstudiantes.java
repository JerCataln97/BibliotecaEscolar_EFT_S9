package controlador;

import dao.EstudianteDAO;
import dao.impl.EstudianteDAOImpl;
import modelo.Estudiante;
import java.util.List;

public class ControladorEstudiantes {

    private final EstudianteDAO estudianteDAO;

    //Constructor
    public ControladorEstudiantes() {
        estudianteDAO = new EstudianteDAOImpl();
    }

    //Metodo para listar los estudiantes
    public List<Estudiante> listarEstudiantes() {
        return estudianteDAO.readAll();
    }

    //Metodo para buscar un estudiante por su ID
    public Estudiante buscarEstudiante(int id) {
        return estudianteDAO.readById(id);
    }

    //Metodo para agregar un estudiante
    public boolean agregarEstudiante(Estudiante estudiante) {
        return estudianteDAO.create(estudiante);
    }

    //Metodo para modificar un estudiante
    public boolean modificarEstudiante(Estudiante estudiante) {
        return estudianteDAO.update(estudiante);
    }

    //Metodo para eliminar un estudiante
    public boolean eliminarEstudiante(int id) {
        return estudianteDAO.delete(id);
    }
}
