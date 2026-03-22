import dao.Catalogo;
import dao.ConexionDB;
import vista.MenuPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {

        // Inicia el diseño moderno en Modo Claro por defecto
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("No se pudo inicializar FlatLaf");
        }

        // 1. Se corrobora que el archivo catalogo.db exista.
        ConexionDB.crearBaseDeDatos();

        // 2. Se instancia el DAO, el puente a SQL.
        Catalogo catalogo = new Catalogo();

        // 3. Se enciende el Menú Principal de forma segura.
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal menu = new MenuPrincipal(catalogo);
            menu.mostrar();
        });
    }
}