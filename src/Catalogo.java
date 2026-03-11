import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Catalogo {

    private static final String URL = "jdbc:sqlite:src/datos/catalogo.db";

    public Catalogo() {
    }

    // ===========================================
    // 1. OBTENER CATEGORÍAS - DESCRIPCION GENERAL
    // ===========================================
    public List<String> obtenerDescripcionesGenerales() {
        List<String> generales = new ArrayList<>();
        // Usa DISTINCT para que SQL devuelva valores únicos, y ORDER BY para ordenarlos alfabéticamente.
        String sql = "SELECT DISTINCT descripcion_general FROM productos ORDER BY descripcion_general";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            //Enciende la seguridad de Claves Foraneas de SQLite.
            stmt.execute("PRAGMA foreign_keys = ON;");

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    generales.add(rs.getString("descripcion_general"));
                }
            }
        } catch (SQLException e) {
            System.out.println("[!] Error al obtener categorías: " + e.getMessage());
        }
        return generales;
    }

    // ============================================
    // OBTENER ELECCIONES - DESCRIPCION DE ELECCION
    // ============================================
    public List<String> obtenerEleccionesPorGeneral(String descGeneral) {
        List<String> elecciones = new ArrayList<>();
        String sql = "SELECT DISTINCT descripcion_eleccion FROM productos WHERE descripcion_general = ? ORDER BY descripcion_eleccion";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            //Crea un objeto Statement descartable para poder encender el Pragma y activar la seguridad de Claves Foraneas.
            conn.createStatement().execute("PRAGMA foreign_keys = ON;");
            pstmt.setString(1, descGeneral);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    elecciones.add(rs.getString("descripcion_eleccion"));
                }
            }
        } catch (SQLException e) {
            System.out.println("[!] Error al obtener elecciones: " + e.getMessage());
        }
        return elecciones;
    }

    // ===========================================
    // OBTENER LISTA DE PRODUCTOS CON COMPOSICIÓN
    // ===========================================
    public List<Producto> obtenerListaProductosPorEleccion(String descGeneral, String descEleccion) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.composicion FROM productos p " +
                "LEFT JOIN composiciones c ON p.codigo = c.codigo " +
                "WHERE p.descripcion_general = ? AND p.descripcion_eleccion = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.createStatement().execute("PRAGMA foreign_keys = ON;");
            pstmt.setString(1, descGeneral);
            pstmt.setString(2, descEleccion);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Convierte cada fila de la base de datos en un objeto Producto
                    Producto p = new Producto(
                            rs.getInt("codigo"),
                            rs.getString("descripcion_general"),
                            rs.getString("descripcion_eleccion"),
                            rs.getString("descripcion_especifica"),
                            rs.getDouble("precio")
                    );
                    String composicion = rs.getString("composicion");
                    if (composicion != null && !composicion.trim().isEmpty()) {
                        p.setComposicion(composicion);
                    }
                    productos.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("[!] Error al obtener la lista de productos: " + e.getMessage());
        }
        return productos;
    }

    // ==========================================
    // BUSCADOR DE PRODUCTO POR PALABRA
    // ==========================================
    public List<Producto> buscarProductosPorPalabra(String palabra) {
        List<Producto> productos = new ArrayList<>();
        // Busca coincidencias en la descripción general o específica.
        // Usa un LEFT JOIN para traer el producto y, si existe, su composición en una sola consulta.
        String sql = "SELECT p.*, c.composicion FROM productos p " +
                "LEFT JOIN composiciones c ON p.codigo = c.codigo " +
                "WHERE p.descripcion_general LIKE ? OR p.descripcion_especifica LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            //Crea un objeto Statement descartable para poder encender el Pragma y activar la seguridad de Claves Foraneas.
            conn.createStatement().execute("PRAGMA foreign_keys = ON;");

            // Los % indican que la palabra puede estar en cualquier parte del texto
            String busqueda = "%" + palabra + "%";
            pstmt.setString(1, busqueda);
            pstmt.setString(2, busqueda);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // 1. Se arma el producto base
                    // Convierte cada fila de la base de datos en un objeto Producto
                    Producto p = new Producto(
                            rs.getInt("codigo"),
                            rs.getString("descripcion_general"),
                            rs.getString("descripcion_eleccion"),
                            rs.getString("descripcion_especifica"),
                            rs.getDouble("precio")
                    );

                    // 2. Se extrae la composición del JOIN y se inyecta si no es nula
                    String composicion = rs.getString("composicion");
                    if (composicion != null && !composicion.trim().isEmpty()) {
                        p.setComposicion(composicion);
                    }
                    productos.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("[!] Error en el buscador: " + e.getMessage());
        }
        return productos;
    }
}