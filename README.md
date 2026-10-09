🧠 Evaluacion Final Transversal – Desarrollo Orientado a Objetos II

Nombre del proyecto: Biblioteca Escolar EFT S9

👤 Autor del proyecto

Nombre completo: Jeremy Daniel Catalan Boisier

Carrera: Analista Programador Computacional

📘 Descripción general del sistema

Este proyecto corresponde a la evaluacion final transversal de la asignatura Desarrollo Orientado a Objetos II.

El proyecto permite gestionar la biblioteca escolar mediante una aplicacion desarrollada en Java Swing, conectada a una base de datos en MySQL. Permite administrar usuarios, libros, categorias y prestamos, diferenciando los permisos segun el rol de usuario que tengan (bibliotecario o estudiante).

Para el desarrollo se utilizo programacion orientada a objetos, el patron DAO, una estructura MVC y un conector JDBC para la conexion con la base de datos.

🧱 Estructura general del proyecto

📁 lb/

└── conector JDBC

📁 src/

├── controlador/ # (ControladorCategorias, ControladorEstudiantes, ControladorLibros, ControladorLogin, ControladorPrestamos, ControladorReportes)

├── dao/ # (CategoriaDAO, EstudianteDAO, LibroDAO, PrestamoDAO, UsuarioDAO)

└── impl/ # (CategoriaDAOImpl, EstudianteDAOImpl, LibroDAOImpl, PrestamoDAOImpl, UsuarioDAOImpl)
├── main/ # (Main)

├── modelo/ # (Categoria, Estudiante, Libro, Prestamo, Reporte, Usuario)

├── util/ # (DatabaseConnection)

└── vista/ # (VentanaBibliotecario, VentanaCategorias, VentanaConsultarL, VentanaDevolverP, VentanaEstudiante, VentanaEstudiantes, VentanaLibros, VentanaLogin, VentanaPrestamos, VentanaReportes, VentanaSolicitarP)

⚙️ Instrucciones para clonar y ejecutar el proyecto

Abrir el proyecto en IntelliJ IDEA.

Ejecuta el archivo Main.java que se encuentra en el paquete main.
