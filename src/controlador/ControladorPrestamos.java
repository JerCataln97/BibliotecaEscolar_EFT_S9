package controlador;

import dao.EstudianteDAO;
import dao.LibroDAO;
import dao.PrestamoDAO;
import dao.impl.EstudianteDAOImpl;
import dao.impl.LibroDAOImpl;
import dao.impl.PrestamoDAOImpl;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ControladorPrestamos {

    private final PrestamoDAO prestamoDAO;
    private final LibroDAO libroDAO;
    private final EstudianteDAO estudianteDAO;

    //Objeto utilizado para controlar el acceso concurrente a las operaciones del prestamo
    private static final Object BLOQUEO_PRESTAMO = new Object();

    //Constructor
    public ControladorPrestamos() {

        prestamoDAO = new PrestamoDAOImpl();
        libroDAO = new LibroDAOImpl();
        estudianteDAO = new EstudianteDAOImpl();
    }

    //Metodo par listar los prestamos
    public List<Prestamo> listarPrestamos() {
        return prestamoDAO.readAll();
    }

    //Metodo para buscar un prestamo por su ID
    public Prestamo buscarPrestamo(int id) {
        return prestamoDAO.readById(id);
    }

    //Metodo para listar los estudiantes
    public List<Estudiante> listarEstudiantes() {
        return estudianteDAO.readAll();
    }

    //Metodo para listar los libros
    public List<Libro> listarLibros() {
        return libroDAO.readAll();
    }

    //Metodo para buscar un estudiante por su ID
    public Estudiante buscarEstudiante(int id) {
        return estudianteDAO.readById(id);
    }

    //Metodo para buscar un libro por su ID
    public Libro buscarLibro(int id) {
        return libroDAO.readById(id);
    }

    //Metodo para registrar un  prestamo y actualizar el stock
    public boolean registrarPrestamo(Prestamo prestamo) {

        synchronized (BLOQUEO_PRESTAMO) {

            Connection con = null;

            try {
                con = DatabaseConnection.getInstance();
                con.setAutoCommit(false);

                Libro libroActual = libroDAO.readById(prestamo.getIdLibro());

                if (libroActual == null) {
                    con.rollback();
                    return false;
                }

                if (libroActual.getStock() <= 0) {
                    con.rollback();
                    return false;
                }

                boolean prestamoCreado = prestamoDAO.create(prestamo);

                if (!prestamoCreado) {
                    con.rollback();
                    return false;
                }

                boolean stockActualizado = libroDAO.actualizarStock(libroActual.getId(), libroActual.getStock() - 1);

                if (!stockActualizado) {
                    con.rollback();
                    return false;
                }

                con.commit();

                return true;

            } catch (SQLException e) {
                System.out.println("Error en la operación de préstamo: " + e.getMessage());

                try {
                    if (con != null) {
                        con.rollback();
                    }

                } catch (SQLException rollbackException) {
                    System.out.println("Error al hacer rollback: " + rollbackException.getMessage());
                }

                return false;

            } finally {

                try {
                    if (con != null) {
                        con.setAutoCommit(true);
                    }

                } catch (SQLException e) {
                    System.out.println("Error al restaurar AutoCommit: " + e.getMessage());
                }
            }
        }
    }

    //Metodo para modificar un prestamo y actualizar el stock
    public boolean modificarPrestamo(Prestamo prestamoNuevo) {

        synchronized (BLOQUEO_PRESTAMO) {

            Connection con = null;

            try {
                con = DatabaseConnection.getInstance();
                con.setAutoCommit(false);

                Prestamo prestamoAnterior = prestamoDAO.readById(prestamoNuevo.getId());

                if (prestamoAnterior == null) {
                    con.rollback();
                    return false;
                }

                Libro libroAnterior = libroDAO.readById(prestamoAnterior.getIdLibro());
                Libro libroNuevo = libroDAO.readById(prestamoNuevo.getIdLibro());

                if (libroAnterior == null || libroNuevo == null) {
                    con.rollback();
                    return false;
                }

                if (!prestamoNuevo.isDevuelto()
                        && !prestamoAnterior.isDevuelto()
                        && prestamoAnterior.getIdLibro()
                        != prestamoNuevo.getIdLibro()
                        && libroNuevo.getStock() <= 0) {

                    con.rollback();
                    return false;
                }

                if (prestamoAnterior.isDevuelto()
                        && !prestamoNuevo.isDevuelto()
                        && libroNuevo.getStock() <= 0) {

                    con.rollback();
                    return false;
                }

                boolean actualizado = prestamoDAO.update(prestamoNuevo);

                if (!actualizado) {
                    con.rollback();
                    return false;
                }

                boolean stockCorrecto = true;

                if (!prestamoAnterior.isDevuelto()
                        && prestamoNuevo.isDevuelto()) {

                    stockCorrecto = libroDAO.actualizarStock(libroAnterior.getId(), libroAnterior.getStock() + 1);
                }

                else if (prestamoAnterior.isDevuelto()
                        && !prestamoNuevo.isDevuelto()) {

                    stockCorrecto = libroDAO.actualizarStock(libroNuevo.getId(), libroNuevo.getStock() - 1);
                }

                else if (!prestamoAnterior.isDevuelto()
                        && !prestamoNuevo.isDevuelto()
                        && prestamoAnterior.getIdLibro()
                        != prestamoNuevo.getIdLibro()) {

                    boolean devolverLibroAnterior = libroDAO.actualizarStock(libroAnterior.getId(), libroAnterior.getStock() + 1);

                    boolean descontarLibroNuevo = false;

                    if (devolverLibroAnterior) {

                        descontarLibroNuevo = libroDAO.actualizarStock(libroNuevo.getId(), libroNuevo.getStock() - 1);
                    }

                    stockCorrecto = devolverLibroAnterior && descontarLibroNuevo;
                }

                if (!stockCorrecto) {
                    con.rollback();
                    return false;
                }

                con.commit();

                return true;

            } catch (SQLException e) {
                System.out.println("Error al modificar préstamo: " + e.getMessage());

                try {
                    if (con != null) {
                        con.rollback();
                    }

                } catch (SQLException rollbackException) {
                    System.out.println("Error al hacer rollback: " + rollbackException.getMessage());
                }

                return false;

            } finally {

                try {
                    if (con != null) {
                        con.setAutoCommit(true);
                    }

                } catch (SQLException e) {
                    System.out.println("Error al restaurar AutoCommit: " + e.getMessage());
                }
            }
        }
    }

    //Metodo para eliminar un prestamo y actualizar el stock
    public boolean eliminarPrestamo(int id) {

        synchronized (BLOQUEO_PRESTAMO) {

            Connection con = null;

            try {
                con = DatabaseConnection.getInstance();
                con.setAutoCommit(false);

                Prestamo prestamo = prestamoDAO.readById(id);

                if (prestamo == null) {
                    con.rollback();
                    return false;
                }

                Libro libro = libroDAO.readById(prestamo.getIdLibro());

                if (libro == null) {
                    con.rollback();
                    return false;
                }

                boolean eliminado = prestamoDAO.delete(id);

                if (!eliminado) {
                    con.rollback();
                    return false;
                }

                if (!prestamo.isDevuelto()) {

                    boolean stockActualizado = libroDAO.actualizarStock(libro.getId(), libro.getStock() + 1);

                    if (!stockActualizado) {
                        con.rollback();
                        return false;
                    }
                }

                con.commit();

                return true;

            } catch (SQLException e) {
                System.out.println("Error al eliminar préstamo: " + e.getMessage());

                try {
                    if (con != null) {
                        con.rollback();
                    }

                } catch (SQLException rollbackException) {
                    System.out.println("Error al hacer rollback: " + rollbackException.getMessage());
                }

                return false;

            } finally {

                try {
                    if (con != null) {
                        con.setAutoCommit(true);
                    }

                } catch (SQLException e) {
                    System.out.println("Error al restaurar AutoCommit: " + e.getMessage());
                }
            }
        }
    }

    //Metodo para registrar la devolucion de un prestamo y actualizar el stock
    public boolean devolverPrestamo(Prestamo prestamo) {

        synchronized (BLOQUEO_PRESTAMO) {

            Connection con = null;

            try {
                con = DatabaseConnection.getInstance();
                con.setAutoCommit(false);

                Libro libroActual = libroDAO.readById(prestamo.getIdLibro());

                if (libroActual == null) {
                    con.rollback();
                    return false;
                }

                boolean prestamoActualizado = prestamoDAO.update(prestamo);

                if (!prestamoActualizado) {
                    con.rollback();
                    return false;
                }

                boolean stockActualizado = libroDAO.actualizarStock(libroActual.getId(), libroActual.getStock() + 1);

                if (!stockActualizado) {
                    con.rollback();
                    return false;
                }

                con.commit();

                return true;

            } catch (SQLException e) {
                System.out.println("Error en la devolución: " + e.getMessage());

                try {
                    if (con != null) {
                        con.rollback();
                    }

                } catch (SQLException rollbackException) {
                    System.out.println("Error al hacer rollback: " + rollbackException.getMessage());
                }

                return false;

            } finally {

                try {
                    if (con != null) {
                        con.setAutoCommit(true);
                    }

                } catch (SQLException e) {
                    System.out.println("Error al restaurar AutoCommit: " + e.getMessage());
                }
            }
        }
    }
}