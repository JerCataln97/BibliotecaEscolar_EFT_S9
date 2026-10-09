package dao;

import modelo.Usuario;
import java.util.List;

public interface UsuarioDAO {

    boolean create(Usuario usuario);

    List<Usuario> readAll();

    Usuario readById(int id);

    Usuario login(String correo, String contrasena);

    boolean update(Usuario usuario);

    boolean delete(int id);
}
