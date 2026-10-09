package controlador;

import dao.CategoriaDAO;
import dao.LibroDAO;
import dao.impl.CategoriaDAOImpl;
import dao.impl.LibroDAOImpl;
import modelo.Categoria;
import modelo.Libro;
import java.util.List;

public class ControladorLibros {

    private final LibroDAO libroDAO;
    private final CategoriaDAO categoriaDAO;

    //Constructor
    public ControladorLibros() {

        libroDAO = new LibroDAOImpl();
        categoriaDAO = new CategoriaDAOImpl();
    }

    //Metodo para listar los libros
    public List<Libro> listarLibros() {
        return libroDAO.readAll();
    }

    //Metodo para buscar un libro por su ID
    public Libro buscarLibro(int id) {
        return libroDAO.readById(id);
    }

    //Metodo para agregar un libro
    public boolean agregarLibro(Libro libro) {
        return libroDAO.create(libro);
    }

    //Metodo para modificar un libro
    public boolean modificarLibro(Libro libro) {
        return libroDAO.update(libro);
    }

    //Metodo para eliminar un libro
    public boolean eliminarLibro(int id) {
        return libroDAO.delete(id);
    }

    //Metodo para actulizar el stock de un libro
    public boolean actualizarStock(int id, int nuevoStock) {
        return libroDAO.actualizarStock(id, nuevoStock);
    }

    //Metodo para listar las categorias
    public List<Categoria> listarCategorias() {
        return categoriaDAO.readAll();
    }

    //Metodo para buscar una categoria por su ID
    public Categoria buscarCategoria(int id) {
        return categoriaDAO.readById(id);
    }
}