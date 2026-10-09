package dao.impl;

import dao.LibroDAO;
import modelo.Libro;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAOImpl implements LibroDAO {

    //Metodo para crear un libro
    @Override
    public boolean create(Libro libro) {

        String sql = """
                INSERT INTO libros
                (titulo, autor, isbn, editorial, stock, id_categoria)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, libro.getTitulo());
                ps.setString(2, libro.getAutor());
                ps.setString(3, libro.getIsbn());
                ps.setString(4, libro.getEditorial());
                ps.setInt(5, libro.getStock());
                ps.setInt(6, libro.getIdCategoria());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al crear libro: " + e.getMessage());

            return false;
        }
    }

    //Metodo para obtener todos los libros
    @Override
    public List<Libro> readAll() {

        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT * FROM libros";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    libros.add(new Libro(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("isbn"),
                            rs.getString("editorial"),
                            rs.getInt("stock"),
                            rs.getInt("id_categoria")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar libros: " + e.getMessage());
        }

        return libros;
    }

    //Metodo para buscar un libro por su ID
    @Override
    public Libro readById(int id) {

        String sql = "SELECT * FROM libros WHERE id = ?";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        return new Libro(
                                rs.getInt("id"),
                                rs.getString("titulo"),
                                rs.getString("autor"),
                                rs.getString("isbn"),
                                rs.getString("editorial"),
                                rs.getInt("stock"),
                                rs.getInt("id_categoria")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar libro: " + e.getMessage());
        }

        return null;
    }

    //Metodo para actualizar un libro
    @Override
    public boolean update(Libro libro) {

        String sql = """
                UPDATE libros
                SET titulo = ?,
                    autor = ?,
                    isbn = ?,
                    editorial = ?,
                    stock = ?,
                    id_categoria = ?
                WHERE id = ?
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, libro.getTitulo());
                ps.setString(2, libro.getAutor());
                ps.setString(3, libro.getIsbn());
                ps.setString(4, libro.getEditorial());
                ps.setInt(5, libro.getStock());
                ps.setInt(6, libro.getIdCategoria());
                ps.setInt(7, libro.getId());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar libro: " + e.getMessage());

            return false;
        }
    }

    //Metodo para actulizar el stock de un libro
    @Override
    public boolean actualizarStock(int id, int nuevoStock) {

        String sql = """
                UPDATE libros
                SET stock = ?
                WHERE id = ?
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, nuevoStock);
                ps.setInt(2, id);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());

            return false;
        }
    }

    //Metodo para eliminar un libro
    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM libros WHERE id = ?";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar libro: " + e.getMessage());

            return false;
        }
    }
}