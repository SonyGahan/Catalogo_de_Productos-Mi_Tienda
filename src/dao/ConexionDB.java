package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ConexionDB {
    // Ruta donde se creará físicamente el archivo de la base de datos
    private static final String URL = "jdbc:sqlite:src/datos/catalogo.db";

    public static void crearBaseDeDatos() {
        // 1. Tabla de Productos - reflejando los atributos de la clase modelo.Producto
        String tablaProductos = "CREATE TABLE IF NOT EXISTS productos ("
                + " codigo INTEGER PRIMARY KEY,"
                + " descripcion_general TEXT NOT NULL,"
                + " descripcion_eleccion TEXT,"
                + " descripcion_especifica TEXT,"
                + " precio REAL"
                + ");";

        // 2. Tabla de Composiciones
        String tablaComposiciones = "CREATE TABLE IF NOT EXISTS composiciones ("
                + " codigo INTEGER PRIMARY KEY,"
                + " composicion TEXT NOT NULL,"
                + " FOREIGN KEY (codigo) REFERENCES productos(codigo)"
                + ");";

        // El bloque try-with-resources asegura que la conexión se cierre automáticamente
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // Se estructuran las sentencias SQL
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute(tablaProductos);
            stmt.execute(tablaComposiciones);

            System.out.println("[*] Base de datos SQLite y tablas creadas con éxito.");

        } catch (SQLException e) {
            System.out.println("[!] Error al crear la base de datos: " + e.getMessage());
        }
    }

    // MÉTODO PARA CARGAR LOS CSV A LA BASE DE DATOS
    public static boolean cargarDatosDesdeCSV(String rutaProductos, String rutaComposiciones) {
        String insertProducto = "INSERT OR IGNORE INTO productos (codigo, descripcion_general, descripcion_eleccion, descripcion_especifica, precio) VALUES (?, ?, ?, ?, ?)";
        String insertComposicion = "INSERT OR IGNORE INTO composiciones (codigo, composicion) VALUES (?, ?)";

        // 1. Se declara la conexión afuera del segundo try para poder hacer el "rollback" si algo falla
        try (Connection conn = DriverManager.getConnection(URL)) {

            // Se inicia la transaccion - Se pausa el guardado automatico de SQLite
            conn.setAutoCommit(false);

            try (Statement stmtLimpieza = conn.createStatement();
                 PreparedStatement pstmtProd = conn.prepareStatement(insertProducto);
                 PreparedStatement pstmtComp = conn.prepareStatement(insertComposicion)) {

                stmtLimpieza.execute("PRAGMA foreign_keys = ON;");

                // ===============================================
                // 0. VACIAR TABLAS para la actualización mensual
                // ===============================================
                System.out.println("[*] Vaciando catálogo anterior...");
                stmtLimpieza.execute("DELETE FROM composiciones;");
                stmtLimpieza.execute("DELETE FROM productos;");
                System.out.println("[+] Tablas limpias y listas para los nuevos precios.");


                // ==========================================
                // 1. Cargar Productos
                // ==========================================
                System.out.println("[*] Leyendo tabla_productos.csv...");
                List<String> lineasProductos = Files.readAllLines(Paths.get(rutaProductos));

                // Empieza desde i = 1 para saltar solo la fila de los títulos
                for (int i = 1; i < lineasProductos.size(); i++) {
                    String linea = lineasProductos.get(i);
                    if (linea.trim().isEmpty()) continue;

                    // Separa por punto y coma
                    String[] partes = linea.split(";");

                    pstmtProd.setInt(1, Integer.parseInt(partes[0].trim()));
                    pstmtProd.setString(2, partes[1].trim());
                    pstmtProd.setString(3, partes[2].trim());
                    pstmtProd.setString(4, partes[3].trim());


                    // Limpieza del precio: Se elimina el caracter '$', los puntos de miles y espacios.
                    // Si hubiera comas decimales, se pasan a puntos.
                    String precioLimpio = partes[4].replace("$", "")
                            .replace(".", "")
                            .replace(",", ".")
                            .trim();

                    pstmtProd.setDouble(5, Double.parseDouble(precioLimpio));

                    pstmtProd.addBatch(); // Se arma un solo paquete de filas empaquetadas, en vez de enviar de a una por separado.
                }
                pstmtProd.executeBatch(); // Se envía todo el paquete junto.
                System.out.println("[+] Productos insertados correctamente en SQLite.");


                // ==========================================
                // 2. Cargar Composiciones
                // ==========================================
                System.out.println("[*] Leyendo tabla_componentes.csv...");
                List<String> lineasComposiciones = Files.readAllLines(Paths.get(rutaComposiciones));

                // Empieza desde i = 1 para saltar el título
                for (int i = 1; i < lineasComposiciones.size(); i++) {
                    String linea = lineasComposiciones.get(i);
                    if (linea.trim().isEmpty()) continue;

                    // Separa por punto y coma
                    String[] partes = linea.split(";");

                    pstmtComp.setInt(1, Integer.parseInt(partes[0].trim()));
                    pstmtComp.setString(2, partes[1].trim());

                    pstmtComp.addBatch(); // Se arma un solo paquete de filas empaquetadas, en vez de enviar de a una por separado.
                }
                pstmtComp.executeBatch(); // Se envía todo el paquete junto.
                System.out.println("[+] Composiciones insertadas correctamente en SQLite.");


                // ==============================================================
                // 3. Si todo salio bien, se guardan los cambios definitivamente
                // ==============================================================
                conn.commit();
                System.out.println("[+] Actualización exitosa. Cambios guardados en la BD.");
                return true;

            } catch (Exception exInterna) {
                conn.rollback();
                System.out.println("[!] Fallo durante la lectura. Se han restaurado los datos anteriores. Error: " + exInterna.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("[!] Error crítico de conexión a la base de datos: " + e.getMessage());
            return false;
        }
    }
}