package dao.impl;

import dao.EstudianteDAO;
import modelo.Estudiante;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOImpl implements EstudianteDAO {

    //Metodo para crear un estudiante
    @Override
    public boolean create(Estudiante estudiante) {

        String sql = """
                INSERT INTO estudiantes
                (nombre, rut, curso, correo)
                VALUES (?, ?, ?, ?)
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, estudiante.getNombre());
                ps.setString(2, estudiante.getRut());
                ps.setString(3, estudiante.getCurso());
                ps.setString(4, estudiante.getCorreo());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al crear estudiante: " + e.getMessage());

            return false;
        }
    }

    //Metodo para obtener todos los estudiantes
    @Override
    public List<Estudiante> readAll() {

        List<Estudiante> estudiantes = new ArrayList<>();

        String sql = "SELECT * FROM estudiantes";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    estudiantes.add(
                            new Estudiante(
                                    rs.getInt("id"),
                                    rs.getString("nombre"),
                                    rs.getString("rut"),
                                    rs.getString("curso"),
                                    rs.getString("correo")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar estudiantes: " + e.getMessage());
        }

        return estudiantes;
    }

    //Metodo para buscar un estudiante por su ID
    @Override
    public Estudiante readById(int id) {

        String sql = "SELECT * FROM estudiantes WHERE id = ?";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        return new Estudiante(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("rut"),
                                rs.getString("curso"),
                                rs.getString("correo")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar estudiante: " + e.getMessage());
        }

        return null;
    }

    //Metodo para actulizar un estudiante
    @Override
    public boolean update(Estudiante estudiante) {

        String sql = """
                UPDATE estudiantes
                SET nombre = ?,
                    rut = ?,
                    curso = ?,
                    correo = ?
                WHERE id = ?
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, estudiante.getNombre());
                ps.setString(2, estudiante.getRut());
                ps.setString(3, estudiante.getCurso());
                ps.setString(4, estudiante.getCorreo());
                ps.setInt(5, estudiante.getId());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar estudiante: " + e.getMessage());

            return false;
        }
    }

    //Metodo para eliminar un estudiante
    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar estudiante: " + e.getMessage());

            return false;
        }
    }
}