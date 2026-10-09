package dao;

import modelo.Categoria;
import java.util.List;

public interface CategoriaDAO {

    boolean create(Categoria categoria);

    List<Categoria> readAll();

    Categoria readById(int id);

    boolean update(Categoria categoria);

    boolean delete(int id);
}
