package dao;

import modelo.Prestamo;
import modelo.Reporte;
import java.util.List;

public interface PrestamoDAO {

    boolean create(Prestamo prestamo);

    List<Prestamo> readAll();

    Prestamo readById(int id);

    boolean update(Prestamo prestamo);

    boolean delete(int id);

    List<Prestamo> buscarPorEstudiante(int idEstudiante);

    List<Prestamo> buscarPrestamosActivos();

    List<Prestamo> buscarPrestamosAtrasados();

    List<Reporte> buscarLibrosMasPrestados();
}