package dao;

import modelo.Estudiante;
import java.util.List;

public interface EstudianteDAO {

    boolean create(Estudiante estudiante);

    List<Estudiante> readAll();

    Estudiante readById(int id);

    boolean update(Estudiante estudiante);

    boolean delete(int id);
}