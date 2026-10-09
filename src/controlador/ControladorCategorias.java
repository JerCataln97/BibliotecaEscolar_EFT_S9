package controlador;

import dao.CategoriaDAO;
import dao.impl.CategoriaDAOImpl;
import modelo.Categoria;
import java.util.List;

public class ControladorCategorias {

    private final CategoriaDAO categoriaDAO;

    //Constructor
    public ControladorCategorias() {
        categoriaDAO = new CategoriaDAOImpl();
    }

    //Metodo para listar las categorias
    public List<Categoria> listarCategorias() {
        return categoriaDAO.readAll();
    }

    //Metodo para buscar una categoria por su ID
    public Categoria buscarCategoria(int id) {
        return categoriaDAO.readById(id);
    }

    //Metodo para agregar una catgoria
    public boolean agregarCategoria(Categoria categoria) {
        return categoriaDAO.create(categoria);
    }

    //Metodo para modificar una categoria
    public boolean modificarCategoria(Categoria categoria) {
        return categoriaDAO.update(categoria);
    }

    //Metodo para eliminar una categoria
    public boolean eliminarCategoria(int id) {
        return categoriaDAO.delete(id);
    }
}