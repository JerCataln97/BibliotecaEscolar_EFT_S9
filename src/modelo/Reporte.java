package modelo;

public class Reporte {

    //Atributos
    private int idLibro;
    private String titulo;
    private int cantidadPrestamos;

    //Constructor
    public Reporte(int idLibro, String titulo, int cantidadPrestamos) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.cantidadPrestamos = cantidadPrestamos;
    }

    //Getters y Setters
    public int getIdLibro() {
        return idLibro;
    }
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getCantidadPrestamos() {
        return cantidadPrestamos;
    }
    public void setCantidadPrestamos(int cantidadPrestamos) {
        this.cantidadPrestamos = cantidadPrestamos;
    }
}
