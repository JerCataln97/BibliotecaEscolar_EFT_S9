package dao;

import modelo.Libro;
import java.util.List;

public interface LibroDAO {

    boolean create(Libro libro);

    List<Libro> readAll();

    Libro readById(int id);

    boolean update(Libro libro);

    boolean delete(int id);

    boolean actualizarStock(int id, int nuevoStock);
}
