import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

public class InterfazUsuario {
    private JFrame frame;
    private JTextField txtBuscar;
    private JComboBox<String> comboGeneral;
    private JComboBox<String> comboEleccion;
    private JTextArea textDetalles;
    private Catalogo catalogo;
    private JButton btnVerLista;

    // Paleta de colores Institucional
    private final Color AZUL_CORPORATIVO = new Color(0, 86, 179);
    private final Color GRIS_SUAVE = new Color(230, 230, 230);
    private final Color ROJO_SUAVE = new Color(220, 53, 69);
    private final Color VERDE_WPP = new Color(37, 211, 102);
    private final Color FONDO_BLANCO = Color.WHITE;

    public InterfazUsuario(Catalogo catalogo) {
        this.catalogo = catalogo;
        construirInterfaz();
    }

    private void construirInterfaz() {
        // 1. Configuración de la Ventana Principal
        frame = new JFrame("Catálogo de Productos - Mi Tienda");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 650); // Un poco más grande para el listado
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(FONDO_BLANCO);

        // 2. Panel Superior - Buscador, Filtros y Actualización
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(FONDO_BLANCO);

        // -- Subpanel Buscador --
        JPanel panelBuscador = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBuscador.setBackground(FONDO_BLANCO);
        panelBuscador.add(new JLabel("🔍 Buscar Producto:"));
        txtBuscar = new JTextField(25);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton btnBuscar = new JButton("Buscar");
        estilizarBoton(btnBuscar, AZUL_CORPORATIVO, Color.WHITE);
        panelBuscador.add(txtBuscar);
        panelBuscador.add(btnBuscar);

        // -- Subpanel Filtros --
        JPanel panelFiltros = new JPanel(new GridLayout(2, 2, 10, 15));
        panelFiltros.setBackground(FONDO_BLANCO);
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Tipografía para las etiquetas - Labels
        Font fuenteLabels = new Font("Segoe UI", Font.BOLD, 14);

        JLabel lblCategoria = new JLabel("1. Familia (General):");
        lblCategoria.setFont(fuenteLabels);
        panelFiltros.add(lblCategoria);

        comboGeneral = new JComboBox<>();
        comboGeneral.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboGeneral.setBackground(Color.WHITE);
        panelFiltros.add(comboGeneral);

        JLabel lblProducto = new JLabel("2. Grupo (Elección):");
        lblProducto.setFont(fuenteLabels);
        panelFiltros.add(lblProducto);

        comboEleccion = new JComboBox<>();
        comboEleccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboEleccion.setBackground(Color.WHITE);
        comboEleccion.setEnabled(false);
        panelFiltros.add(comboEleccion);

        // -- Subpanel Actualizar --
        JButton btnActualizar = new JButton("🔄 Actualizar Catálogo");
        estilizarBoton(btnActualizar, AZUL_CORPORATIVO, Color.WHITE);
        JPanel panelBtnActualizar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtnActualizar.setBackground(FONDO_BLANCO);
        panelBtnActualizar.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 20));
        panelBtnActualizar.add(btnActualizar);

        // Ensamble del Panel Norte
        JPanel panelCentroNorte = new JPanel(new BorderLayout());
        panelCentroNorte.add(panelBuscador, BorderLayout.NORTH);
        panelCentroNorte.add(panelFiltros, BorderLayout.CENTER);

        panelNorte.add(panelCentroNorte, BorderLayout.CENTER);
        panelNorte.add(panelBtnActualizar, BorderLayout.EAST);
        frame.add(panelNorte, BorderLayout.NORTH);

        // 3. Panel Central - Ticket de Detalles - Listado de Productos
        textDetalles = new JTextArea();
        textDetalles.setEditable(false);
        textDetalles.setFont(new Font("Consolas", Font.PLAIN, 14));
        textDetalles.setBackground(new Color(250, 250, 252));
        textDetalles.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(textDetalles);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Detalle y Cotización"
        ));
        scrollPane.getViewport().setBackground(FONDO_BLANCO);

        // Panel contenedor para darle márgenes a los lados al área de texto
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(FONDO_BLANCO);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        frame.add(panelCentral, BorderLayout.CENTER);

        // ==========================================
        // 4. Panel Inferior - Botones de acción
        // ==========================================
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBoton.setBackground(FONDO_BLANCO);

        JButton btnNuevaConsulta = new JButton("Limpiar Consulta");
        btnVerLista = new JButton("Generar Listado");
        JButton btnWhatsApp = new JButton("Enviar por WhatsApp");
        JButton btnSalir = new JButton("Salir");

        // Se aplican estilos de diseño
        estilizarBoton(btnNuevaConsulta, GRIS_SUAVE, Color.BLACK);
        estilizarBoton(btnVerLista, AZUL_CORPORATIVO, Color.WHITE);
        estilizarBoton(btnWhatsApp, VERDE_WPP, Color.WHITE);
        estilizarBoton(btnSalir, ROJO_SUAVE, Color.WHITE);

        btnVerLista.setEnabled(false);

        panelBoton.add(btnNuevaConsulta);
        panelBoton.add(btnVerLista);
        panelBoton.add(btnWhatsApp);
        panelBoton.add(btnSalir);

        frame.add(panelBoton, BorderLayout.SOUTH);

        // ==========================================
        // EVENTOS DE LA INTERFAZ
        // ==========================================

        cargarCategorias();

        // EVENTO 1: Al cambiar Familia - GENERAL
        comboGeneral.addActionListener(e -> {
            String categoria = (String) comboGeneral.getSelectedItem();
            if (categoria != null) {
                cargarElecciones(categoria);
                comboEleccion.setEnabled(true);
                btnVerLista.setEnabled(true);
                textDetalles.setText("");
            }
        });

        // EVENTO 2: Botón Generar Listado
        btnVerLista.addActionListener(e -> {
            String general = (String) comboGeneral.getSelectedItem();
            String eleccion = (String) comboEleccion.getSelectedItem();
            if (general != null && eleccion != null) {
                List<Producto> productos = catalogo.obtenerListaProductosPorEleccion(general, eleccion);
                mostrarListado(general, eleccion, productos);
            }
        });

        // EVENTO 3: Botón Buscar por palabra
        btnBuscar.addActionListener(e -> {
            String palabra = txtBuscar.getText().trim();
            if(!palabra.isEmpty()){
                List<Producto> productos = catalogo.buscarProductosPorPalabra(palabra);
                mostrarListado("BÚSQUEDA", "Coincidencias para: '" + palabra + "'", productos);
            }
        });

        // EVENTO 4: Botón Enviar por WhatsApp
        btnWhatsApp.addActionListener(e -> {
            String texto = textDetalles.getText();
            if (texto.isEmpty() || texto.contains("Actualizando base de datos")) {
                JOptionPane.showMessageDialog(frame, "No hay datos en pantalla para enviar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                // Codificamos el texto para que los espacios y saltos de línea funcionen en la URL
                String encodedText = URLEncoder.encode(texto, "UTF-8");
                String url = "https://wa.me/?text=" + encodedText;
                Desktop.getDesktop().browse(new URI(url)); // Abre el navegador predeterminado
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al abrir WhatsApp.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // EVENTO 5: Botón Actualizar Catalogo
        btnActualizar.addActionListener(e -> {
            UIManager.put("OptionPane.background", FONDO_BLANCO);
            UIManager.put("Panel.background", FONDO_BLANCO);

            int confirmacion = JOptionPane.showConfirmDialog(frame,
                    "¿Desea borrar la base de datos actual y cargar el nuevo catálogo?",
                    "Actualización Mensual", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                textDetalles.setText("\n   Actualizando base de datos...\n   Por favor espere.");
                boolean exito = clases.ConexionDB.cargarDatosDesdeCSV("src/datos/tabla_productos.csv", "src/datos/tabla_componentes.csv");
                if(exito){
                    cargarCategorias();
                    textDetalles.setText("\n   ¡Catálogo actualizado con éxito desde los archivos CSV!");
                    JOptionPane.showMessageDialog(frame, "Base de datos actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    textDetalles.setText("\n   [!] Error al actualizar el catálogo.");
                    JOptionPane.showMessageDialog(frame, "Error al leer los archivos CSV.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // EVENTO 6: Al presionar "Nueva Consulta"
        btnNuevaConsulta.addActionListener(e -> {
            txtBuscar.setText("");
            textDetalles.setText("");
            comboEleccion.removeAllItems();
            comboEleccion.setEnabled(false);
            btnVerLista.setEnabled(false);

            if(comboGeneral.getItemCount() > 0) {
                comboGeneral.setSelectedIndex(0);
            }
        });

        // EVENTO 7: Al presionar "Salir"
        btnSalir.addActionListener(e -> {
            System.exit(0);
        });

        frame.setLocationRelativeTo(null);
    }

    // ==========================================
    // MÉTODO PARA DISEÑAR LOS BOTONES
    // ==========================================
    private void estilizarBoton(JButton boton, Color colorFondo, Color colorTexto) {
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Opcional: Esto ayuda en Windows a que respete el color de fondo exacto
        boton.setOpaque(true);
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    private void cargarCategorias() {
        // Impide que salte el evento "addActionListener" mientras se carga la lista
        comboGeneral.removeAllItems();
        List<String> categorias = catalogo.obtenerDescripcionesGenerales();
        for (String cat : categorias) {
            comboGeneral.addItem(cat);
        }
    }

    private void cargarElecciones(String categoria) {
        comboEleccion.removeAllItems();
        List<String> elecciones = catalogo.obtenerEleccionesPorGeneral(categoria);
        for (String eleccion : elecciones) {
            comboEleccion.addItem(eleccion);
        }
    }

    // ==========================================
    // MÉTODO QUE ARMA EL TICKET - LISTADO
    // ==========================================
    private void mostrarListado(String general, String eleccion, List<Producto> productos) {
        if (productos.isEmpty()) {
            textDetalles.setText("\n  No se encontraron productos para esta búsqueda.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append(" FAMILIA : ").append(general).append("\n");
        sb.append(" GRUPO   : ").append(eleccion).append("\n");
        sb.append("=================================================================\n\n");

        double sumaTotal = 0;

        for (Producto p : productos) {
            sb.append(String.format(" %-6s | %-40s | $ %.2f\n",
                    p.getCodigo(),
                    recortarTexto(p.getDescripcionEspecifica(), 40),
                    p.getPrecio()));

            if (p.tieneComposicion()) {
                sb.append("   [Composición: ").append(p.getComposicion()).append("]\n");
            }
            sb.append("-----------------------------------------------------------------\n");

            sumaTotal += p.getPrecio();
        }

        sb.append(String.format("\n TOTAL SUMADO: $ %.2f\n", sumaTotal));
        sb.append("=================================================================\n");

        textDetalles.setText(sb.toString());
        // Movemos el scroll hacia arriba para que el usuario lea desde el principio
        textDetalles.setCaretPosition(0);
    }

    // Método auxiliar para evitar que textos muy largos rompan la tablita visual
    private String recortarTexto(String texto, int maxLength) {
        if (texto.length() <= maxLength) return texto;
        return texto.substring(0, maxLength - 3) + "...";
    }
}