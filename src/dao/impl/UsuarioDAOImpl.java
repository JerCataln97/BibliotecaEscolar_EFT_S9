package dao.impl;

import dao.UsuarioDAO;
import modelo.Usuario;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    //Metodo para crear un usuario
    @Override
    public boolean create(Usuario usuario) {

        String sql = """
                INSERT INTO usuarios
                (nombre, rut, correo, contraseña, rol)
                VALUES (?, ?, ?, ?, ?)
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getRut());
                ps.setString(3, usuario.getCorreo());
                ps.setString(4, usuario.getContrasena());
                ps.setString(5, usuario.getRol());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());

            return false;
        }
    }

    //Metodo para obtener todos los usuarios
    @Override
    public List<Usuario> readAll() {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT * FROM usuarios";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    usuarios.add(
                            new Usuario(
                                    rs.getInt("id"),
                                    rs.getString("nombre"),
                                    rs.getString("rut"),
                                    rs.getString("correo"),
                                    rs.getString("contraseña"),
                                    rs.getString("rol")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    //Metodo para buscar un usuario por su ID
    @Override
    public Usuario readById(int id) {

        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        return new Usuario(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("rut"),
                                rs.getString("correo"),
                                rs.getString("contraseña"),
                                rs.getString("rol")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }

    //Metodo para iniciar sesion
    @Override
    public Usuario login(String correo, String contrasena) {

        String sql = """
                SELECT * FROM usuarios
                WHERE correo = ? AND contraseña = ?
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, correo);
                ps.setString(2, contrasena);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        return new Usuario(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("rut"),
                                rs.getString("correo"),
                                rs.getString("contraseña"),
                                rs.getString("rol")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
        }

        return null;
    }

    //Metodo para actualizar un usuario
    @Override
    public boolean update(Usuario usuario) {

        String sql = """
                UPDATE usuarios
                SET nombre = ?,
                    rut = ?,
                    correo = ?,
                    contraseña = ?,
                    rol = ?
                WHERE id = ?
                """;

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getRut());
                ps.setString(3, usuario.getCorreo());
                ps.setString(4, usuario.getContrasena());
                ps.setString(5, usuario.getRol());
                ps.setInt(6, usuario.getId());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());

            return false;
        }
    }

    //Metodo para eliminar un usuario
    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM usuarios WHERE id = ?";

        try {
            Connection con = DatabaseConnection.getInstance();

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());

            return false;
        }
    }
}