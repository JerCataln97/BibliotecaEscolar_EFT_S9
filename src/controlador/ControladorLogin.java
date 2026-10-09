package controlador;

import dao.UsuarioDAO;
import dao.impl.UsuarioDAOImpl;
import modelo.Usuario;

public class ControladorLogin {

    private final UsuarioDAO usuarioDAO;

    //Constructor
    public ControladorLogin() {
        usuarioDAO = new UsuarioDAOImpl();
    }

    //Metodo para iniciar sesion
    public Usuario iniciarSesion(String correo, String contrasena) {

        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("Debes ingresar el correo.");
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("Debes ingresar la contraseña.");
        }

        return usuarioDAO.login(correo.trim(), contrasena);
    }
}