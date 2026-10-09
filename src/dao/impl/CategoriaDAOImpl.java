package dao.impl;

import dao.CategoriaDAO;
import modelo.Categoria;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {

    //Metodo para crear una categoria
    @Override
    public boolean create(Categoria categoria) {

        String sql = "INSERT INTO categorias (nombre) VALUES (?)";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, categoria.getNombre());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al crear categoría: " + e.getMessage());

            return false;
        }
    }

    //Metodo para obtener todas las categorias
    @Override
    public List<Categoria> readAll() {

        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT * FROM categorias";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    categorias.add(new Categoria(rs.getInt("id"), rs.getString("nombre")));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar categorías: " + e.getMessage());
        }

        return categorias;
    }

    //Metodo para buscar una categoria por si ID
    @Override
    public Categoria readById(int id) {

        String sql = "SELECT * FROM categorias WHERE id = ?";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        return new Categoria(rs.getInt("id"), rs.getString("nombre"));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar categoría: " + e.getMessage());
        }

        return null;
    }

    //Metodo para actualizar una categoria
    @Override
    public boolean update(Categoria categoria) {

        String sql = """
                UPDATE categorias
                SET nombre = ?
                WHERE id = ?
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, categoria.getNombre());
                ps.setInt(2, categoria.getId());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar categoría: " + e.getMessage());

            return false;
        }
    }

    //Metodo para eliminar una categoria
    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM categorias WHERE id = ?";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar categoría: " + e.getMessage());

            return false;
        }
    }
}