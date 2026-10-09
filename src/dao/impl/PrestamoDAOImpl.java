package dao.impl;

import dao.PrestamoDAO;
import modelo.Prestamo;
import modelo.Reporte;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAOImpl implements PrestamoDAO {

    //Metodo para crear un prestamo
    @Override
    public boolean create(Prestamo prestamo) {

        String sql = """
                INSERT INTO prestamos
                (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto)
                VALUES (?, ?, ?, ?, ?)
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, prestamo.getIdEstudiante());
                ps.setInt(2, prestamo.getIdLibro());
                ps.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));

                if (prestamo.getFechaDevolucion() != null) {
                    ps.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));

                } else {
                    ps.setNull(4, Types.DATE);
                }

                ps.setBoolean(5, prestamo.isDevuelto());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al crear préstamo: " + e.getMessage());

            return false;
        }
    }

    //Metodo para obtener todos los prestamos
    @Override
    public List<Prestamo> readAll() {

        List<Prestamo> prestamos = new ArrayList<>();

        String sql = "SELECT * FROM prestamos";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    prestamos.add(mapearPrestamo(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar préstamos: " + e.getMessage());
        }

        return prestamos;
    }

    //Metodo para buscar un prestamo por su ID
    @Override
    public Prestamo readById(int id) {

        String sql = """
                SELECT *
                FROM prestamos
                WHERE id = ?
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        return mapearPrestamo(rs);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar préstamo: " + e.getMessage());
        }

        return null;
    }

    //Metodo para actulizar un prestamo
    @Override
    public boolean update(Prestamo prestamo) {

        String sql = """
                UPDATE prestamos
                SET id_estudiante = ?,
                    id_libro = ?,
                    fecha_prestamo = ?,
                    fecha_devolucion = ?,
                    devuelto = ?
                WHERE id = ?
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, prestamo.getIdEstudiante());
                ps.setInt(2, prestamo.getIdLibro());
                ps.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));

                if (prestamo.getFechaDevolucion() != null) {

                    ps.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));

                } else {
                    ps.setNull(4, Types.DATE);
                }

                ps.setBoolean(5, prestamo.isDevuelto());
                ps.setInt(6, prestamo.getId());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar préstamo: " + e.getMessage());

            return false;
        }
    }

    //Metodo para eliminar un prestamo
    @Override
    public boolean delete(int id) {

        String sql = """
                DELETE FROM prestamos
                WHERE id = ?
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar préstamo: " + e.getMessage());

            return false;
        }
    }

    //Metodo para buscar los prestamos de un estudiante
    @Override
    public List<Prestamo> buscarPorEstudiante(int idEstudiante) {

        List<Prestamo> prestamos = new ArrayList<>();

        String sql = """
                SELECT *
                FROM prestamos
                WHERE id_estudiante = ?
                ORDER BY fecha_prestamo DESC
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, idEstudiante);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        prestamos.add(mapearPrestamo(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar préstamos del estudiante: " + e.getMessage());
        }

        return prestamos;
    }

    //Metodo para buscar los prestamos activos
    @Override
    public List<Prestamo> buscarPrestamosActivos() {

        List<Prestamo> prestamos = new ArrayList<>();

        String sql = """
                SELECT *
                FROM prestamos
                WHERE devuelto = false
                ORDER BY fecha_prestamo DESC
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    prestamos.add(mapearPrestamo(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar préstamos activos: " + e.getMessage());
        }

        return prestamos;
    }

    //Metodo para buscar los prestamos atrasados
    @Override
    public List<Prestamo> buscarPrestamosAtrasados() {

        List<Prestamo> prestamos = new ArrayList<>();

        String sql = """
                SELECT *
                FROM prestamos
                WHERE devuelto = false
                  AND fecha_devolucion < CURDATE()
                ORDER BY fecha_devolucion ASC
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    prestamos.add(mapearPrestamo(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar préstamos atrasados: " + e.getMessage());
        }

        return prestamos;
    }

    //Metodo para obtener los libros mas prestados
    @Override
    public List<Reporte> buscarLibrosMasPrestados() {

        List<Reporte> reportes = new ArrayList<>();

        String sql = """
                SELECT
                    l.id,
                    l.titulo,
                    COUNT(p.id) AS cantidad
                FROM prestamos p
                INNER JOIN libros l
                    ON p.id_libro = l.id
                GROUP BY l.id, l.titulo
                ORDER BY cantidad DESC
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    reportes.add(
                            new Reporte(
                                    rs.getInt("id"),
                                    rs.getString("titulo"),
                                    rs.getInt("cantidad")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al generar reporte de libros más prestados: " + e.getMessage());
        }

        return reportes;
    }

    //Metodo para convertir un resultado de la base de datos en un prestamo
    private Prestamo mapearPrestamo(ResultSet rs) throws SQLException {

        Date fechaPrestamo = rs.getDate("fecha_prestamo");
        Date fechaDevolucion = rs.getDate("fecha_devolucion");

        return new Prestamo(
                rs.getInt("id"),
                rs.getInt("id_estudiante"),
                rs.getInt("id_libro"),
                fechaPrestamo != null
                        ? fechaPrestamo.toLocalDate()
                        : null,
                fechaDevolucion != null
                        ? fechaDevolucion.toLocalDate()
                        : null,
                rs.getBoolean("devuelto")
        );
    }
}