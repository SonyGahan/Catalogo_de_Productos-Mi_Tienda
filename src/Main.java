import dao.Catalogo;
import dao.ConexionDB;
import vista.InterfazUsuario;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // 1. Se corrobora que el archivo catalogo.db exista.
        ConexionDB.crearBaseDeDatos();

        // 2. Se instancia el DAO, el puente a SQL.
        Catalogo catalogo = new Catalogo();

        // 3. Se enciende la Interfaz Gráfica de forma segura.
        SwingUtilities.invokeLater(() -> {
            InterfazUsuario ui = new InterfazUsuario(catalogo);
            ui.mostrar();
        });
    }
}