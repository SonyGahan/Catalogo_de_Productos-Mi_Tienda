package vista;

import dao.Catalogo;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

public class InterfazUsuario {
    private JFrame frame;
    private JTextField txtBuscar;
    private JComboBox<String> comboGeneral;
    private JComboBox<String> comboEleccion;
    private Catalogo catalogo;

    // Tablas Interactivas
    private JTable tablaBuscador;
    private DefaultTableModel modeloBuscador;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JLabel lblTotalCotizacion;
    private double sumaTotal = 0.0;

    // Paleta de colores Institucional
    private final Color AZUL_CORPORATIVO = new Color(0, 86, 179);
    private final Color GRIS_SUAVE = new Color(200, 200, 200);
    private final Color ROJO_SUAVE = new Color(220, 53, 69);
    private final Color VERDE_WPP = new Color(37, 211, 102);
    private final Color GRIS_OSCURO = new Color(50, 50, 50);

    public InterfazUsuario(Catalogo catalogo) {
        this.catalogo = catalogo;
        construirInterfaz();
    }

    private void construirInterfaz() {
        frame = new JFrame("Catálogo de Productos - Mi tienda");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Maneja el ancho de la ventana principal que contiene las 3 columnas principales
        frame.setSize(1350, 720);
        frame.setLayout(new BorderLayout(10, 10));

        // ==========================================
        // 1. PANEL SUPERIOR - Buscador y Filtros
        // ==========================================
        JPanel panelNorte = new JPanel(new BorderLayout());

        JPanel panelBuscador = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBuscador.add(new JLabel("🔍 Buscar Producto:"));
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnBuscar = new JButton("Buscar");
        JButton btnNuevaConsulta = new JButton("Nueva Consulta");
        JButton btnVerLista = new JButton("Generar Listado");

        estilizarBoton(btnBuscar, AZUL_CORPORATIVO, Color.WHITE);
        estilizarBoton(btnNuevaConsulta, GRIS_SUAVE, Color.BLACK);
        estilizarBoton(btnVerLista, AZUL_CORPORATIVO, Color.WHITE);
        btnVerLista.setEnabled(false);

        panelBuscador.add(txtBuscar);
        panelBuscador.add(btnBuscar);
        panelBuscador.add(btnNuevaConsulta);
        panelBuscador.add(btnVerLista);

        // Subpanel Filtros
        JPanel panelFiltros = new JPanel(new GridLayout(2, 2, 10, 10));
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Font fuenteLabels = new Font("Segoe UI", Font.BOLD, 14);
        JLabel lblCategoria = new JLabel("1. Familia:");
        lblCategoria.setFont(fuenteLabels);
        panelFiltros.add(lblCategoria);

        comboGeneral = new JComboBox<>();
        comboGeneral.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelFiltros.add(comboGeneral);

        JLabel lblProducto = new JLabel("2. Grupo:");
        lblProducto.setFont(fuenteLabels);
        panelFiltros.add(lblProducto);

        comboEleccion = new JComboBox<>();
        comboEleccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboEleccion.setEnabled(false);
        panelFiltros.add(comboEleccion);

        panelNorte.add(panelBuscador, BorderLayout.NORTH);
        panelNorte.add(panelFiltros, BorderLayout.CENTER);
        frame.add(panelNorte, BorderLayout.NORTH);

        // ===============================================
        // 2. PANEL CENTRAL - 3 Columnas Listas y Flechas
        // ===============================================

        // Tabla Izquierda: Resultados de Búsqueda
        String[] columnasBuscador = {"Código", "Descripción", "Precio", "Composición"};
        modeloBuscador = new DefaultTableModel(columnasBuscador, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaBuscador = new JTable(modeloBuscador);
        tablaBuscador.setRowHeight(25);
        // Habilita la selección múltiple con Shift o Ctrl.
        tablaBuscador.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // Se definen anchos preferidos fijos.
        tablaBuscador.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaBuscador.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaBuscador.getColumnModel().getColumn(1).setPreferredWidth(250);
        tablaBuscador.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaBuscador.getColumnModel().getColumn(3).setPreferredWidth(600);

        // El JScrollPane detectará automáticamente que la tabla es ancha y mostrará la barra.
        JScrollPane scrollIzquierdo = new JScrollPane(tablaBuscador);
        scrollIzquierdo.setBorder(BorderFactory.createTitledBorder("1. Productos Encontrados"));
        scrollIzquierdo.setPreferredSize(new Dimension(650, 0)); // Ancho fijo preferido

        // Tabla Derecha: Carrito / Cotización
        String[] columnasCarrito = {"Código", "Descripción", "Precio"};
        modeloCarrito = new DefaultTableModel(columnasCarrito, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setRowHeight(25);
        tablaCarrito.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        tablaCarrito.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaCarrito.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaCarrito.getColumnModel().getColumn(2).setPreferredWidth(80);

        JScrollPane scrollDerecho = new JScrollPane(tablaCarrito);
        scrollDerecho.setBorder(BorderFactory.createTitledBorder("2. Mi Cotización"));

        // Panel contenedor para la tabla derecha y el TOTAL
        JPanel panelDerechoCompleto = new JPanel(new BorderLayout());
        panelDerechoCompleto.setPreferredSize(new Dimension(500, 0)); // Ancho fijo preferido
        panelDerechoCompleto.add(scrollDerecho, BorderLayout.CENTER);

        lblTotalCotizacion = new JLabel("TOTAL: $ 0.00");
        lblTotalCotizacion.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotalCotizacion.setForeground(AZUL_CORPORATIVO);
        lblTotalCotizacion.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotalCotizacion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelDerechoCompleto.add(lblTotalCotizacion, BorderLayout.SOUTH);

        // COLUMNA CENTRAL: Botones de Transferencia de productos en ambos sentidos
        JPanel panelTransferencia = new JPanel(new GridBagLayout()); // Centra el contenido maravillosamente
        JPanel panelBotonesTransf = new JPanel(new GridLayout(4, 1, 0, 15)); // 4 botones apilados

        JButton btnAgregarSeleccion = new JButton(" > ");
        JButton btnAgregarTodos = new JButton(" >> ");
        JButton btnQuitarSeleccion = new JButton(" < ");
        JButton btnQuitarTodos = new JButton(" << ");

        estilizarBotonTransferencia(btnAgregarSeleccion);
        estilizarBotonTransferencia(btnAgregarTodos);
        estilizarBotonTransferencia(btnQuitarSeleccion);
        estilizarBotonTransferencia(btnQuitarTodos);

        // se incorpora un tip textual para guiar al usuario
        btnAgregarSeleccion.setToolTipText("Agregar producto(s) seleccionado(s)");
        btnAgregarTodos.setToolTipText("Agregar TODOS los productos de la lista");
        btnQuitarSeleccion.setToolTipText("Quitar producto(s) seleccionado(s) de la cotización");
        btnQuitarTodos.setToolTipText("Vaciar toda la cotización");

        panelBotonesTransf.add(btnAgregarSeleccion);
        panelBotonesTransf.add(btnAgregarTodos);
        panelBotonesTransf.add(btnQuitarSeleccion);
        panelBotonesTransf.add(btnQuitarTodos);

        panelTransferencia.add(panelBotonesTransf); // Lo ubica en el centro del GridBag

        // Ensamble del Panel Central Completo
        JPanel panelCentral = new JPanel(new BorderLayout(15, 0)); // 15px de separación entre columnas
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panelCentral.add(scrollIzquierdo, BorderLayout.WEST);
        panelCentral.add(panelTransferencia, BorderLayout.CENTER);
        panelCentral.add(panelDerechoCompleto, BorderLayout.EAST);

        frame.add(panelCentral, BorderLayout.CENTER);

        // ==========================================
        // 3. PANEL INFERIOR - Acciones Finales
        // ==========================================
        JPanel panelBotonSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        JButton btnWhatsApp = new JButton("💬 Enviar por WhatsApp");
        JButton btnSalir = new JButton("❌ Salir");

        estilizarBoton(btnWhatsApp, VERDE_WPP, Color.WHITE);
        estilizarBoton(btnSalir, ROJO_SUAVE, Color.WHITE);

        panelBotonSur.add(btnWhatsApp);
        panelBotonSur.add(btnSalir);

        frame.add(panelBotonSur, BorderLayout.SOUTH);

        // ==========================================
        // EVENTOS Y LÓGICA DE TRANSFERENCIA
        // ==========================================

        cargarCategorias();

        // EVENTO 1: Al cambiar Familia - GENERAL
        comboGeneral.addActionListener(e -> {
            String categoria = (String) comboGeneral.getSelectedItem();
            if (categoria != null && !categoria.startsWith("---")) {
                cargarElecciones(categoria);
                comboEleccion.setEnabled(true);
                btnVerLista.setEnabled(false);
                modeloBuscador.setRowCount(0);
            } else {
                comboEleccion.removeAllItems();
                comboEleccion.setEnabled(false);
                btnVerLista.setEnabled(false);
            }
        });

        // EVENTO 2: Al cambiar Grupo - ELECCION
        comboEleccion.addActionListener(e -> {
            String eleccion = (String) comboEleccion.getSelectedItem();
            if (eleccion != null && !eleccion.startsWith("---")) {
                btnVerLista.setEnabled(true);
            } else {
                btnVerLista.setEnabled(false);
            }
        });

        // EVENTO 3: Botón Generar Listado
        btnVerLista.addActionListener(e -> {
            String general = (String) comboGeneral.getSelectedItem();
            String eleccion = (String) comboEleccion.getSelectedItem();
            if (general != null && eleccion != null && !general.startsWith("---") && !eleccion.startsWith("---")) {
                List<Producto> productos = catalogo.obtenerListaProductosPorEleccion(general, eleccion);
                llenarTablaBuscador(productos);
            }
        });

        // EVENTO 4: Botón Buscar por texto
        btnBuscar.addActionListener(e -> {
            String palabra = txtBuscar.getText().trim();
            if(!palabra.isEmpty()){
                if(comboGeneral.getItemCount() > 0) {
                    comboGeneral.setSelectedIndex(0);
                }
                List<Producto> productos = catalogo.buscarProductosPorPalabra(palabra);
                llenarTablaBuscador(productos);
            }
        });

        // EVENTO 5: Botón Nueva Consulta - Limpia todos los campos
        btnNuevaConsulta.addActionListener(e -> {
            txtBuscar.setText("");
            modeloBuscador.setRowCount(0);
            if(comboGeneral.getItemCount() > 0) {
                comboGeneral.setSelectedIndex(0);
            }
        });

        // ==============================================
        // LÓGICA DE LOS BOTONES DE TRANSFERENCIA/FLECHAS
        // ==============================================

        // 1. [ > ] Mover Seleccionados al Carrito
        btnAgregarSeleccion.addActionListener(e -> {
            int[] filas = tablaBuscador.getSelectedRows();
            for (int fila : filas) {
                String codigo = tablaBuscador.getValueAt(fila, 0).toString();
                String descripcion = tablaBuscador.getValueAt(fila, 1).toString();
                double precio = (double) tablaBuscador.getValueAt(fila, 2);

                modeloCarrito.addRow(new Object[]{codigo, descripcion, precio});
                sumaTotal += precio;
            }
            actualizarLabelTotal();
            tablaBuscador.clearSelection(); // Deselecciona para evitar doble click accidental
        });

        // 2. [ >> ] Mover TODOS al Carrito
        btnAgregarTodos.addActionListener(e -> {
            for (int fila = 0; fila < modeloBuscador.getRowCount(); fila++) {
                String codigo = modeloBuscador.getValueAt(fila, 0).toString();
                String descripcion = modeloBuscador.getValueAt(fila, 1).toString();
                double precio = (double) modeloBuscador.getValueAt(fila, 2);

                modeloCarrito.addRow(new Object[]{codigo, descripcion, precio});
                sumaTotal += precio;
            }
            actualizarLabelTotal();
        });

        // 3. [ < ] Quitar Seleccionados del Carrito
        btnQuitarSeleccion.addActionListener(e -> {
            int[] filas = tablaCarrito.getSelectedRows();
            // Para borrar varias filas de una tabla, siempre se debe hacer desde abajo hacia arriba.
            for (int i = filas.length - 1; i >= 0; i--) {
                int fila = filas[i];
                double precioRestar = (double) tablaCarrito.getValueAt(fila, 2);
                sumaTotal -= precioRestar;
                modeloCarrito.removeRow(fila);
            }
            actualizarLabelTotal();
        });

        // 4. [ << ] Quitar TODOS - Reemplaza al viejo botón "Vaciar Cotización"
        btnQuitarTodos.addActionListener(e -> {
            modeloCarrito.setRowCount(0);
            sumaTotal = 0.0;
            actualizarLabelTotal();
        });

        // =================================================
        // LÓGICA DEL DOBLE CLIC - Se mantiene como "Atajo"
        // =================================================

        // EVENTO 6: Seleccion de Productos: Doble clic en Tabla Búsqueda -> Pasa al Carrito
        tablaBuscador.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablaBuscador.getSelectedRow();
                    if (fila != -1) {
                        String codigo = tablaBuscador.getValueAt(fila, 0).toString();
                        String descripcion = tablaBuscador.getValueAt(fila, 1).toString();
                        double precio = (double) tablaBuscador.getValueAt(fila, 2);

                        modeloCarrito.addRow(new Object[]{codigo, descripcion, precio});
                        sumaTotal += precio;
                        actualizarLabelTotal();
                    }
                }
            }
        });

        // EVENTO 7: Quita un producto seleccionado: Doble clic en Tabla Carrito -> Elimina del Carrito
        tablaCarrito.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablaCarrito.getSelectedRow();
                    if (fila != -1) {
                        double precioRestar = (double) tablaCarrito.getValueAt(fila, 2);
                        modeloCarrito.removeRow(fila);
                        sumaTotal -= precioRestar;
                        actualizarLabelTotal();
                    }
                }
            }
        });

        // ----------------------------------------------------

        // EVENTO 8: Botón Enviar por WhatsApp
        btnWhatsApp.addActionListener(e -> {
            if (modeloCarrito.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frame, "La cotización está vacía. Agregue productos primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("🛒 *NUEVO PEDIDO / COTIZACIÓN*\n");
                sb.append("-------------------------------------------------\n");

                for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
                    String cod = modeloCarrito.getValueAt(i, 0).toString();
                    String desc = modeloCarrito.getValueAt(i, 1).toString();
                    double prec = (double) modeloCarrito.getValueAt(i, 2);
                    sb.append("▪️ ").append(cod).append(" - ").append(desc).append(" | *$").append(prec).append("*\n");
                }

                sb.append("-------------------------------------------------\n");
                sb.append("💰 *TOTAL: $").append(String.format("%.2f", sumaTotal)).append("*\n");

                String encodedText = URLEncoder.encode(sb.toString(), "UTF-8");
                String url = "https://wa.me/?text=" + encodedText;
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al abrir WhatsApp.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // EVENTO 9: Botón Salir
        btnSalir.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });

        frame.setLocationRelativeTo(null);
    }

    // ==========================================
    // MÉTODOS AUXILIARES DE DISEÑO
    // ==========================================

    private void estilizarBoton(JButton boton, Color colorFondo, Color colorTexto) {
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMargin(new java.awt.Insets(10, 20, 10, 20));
    }

    // Un diseño compacto y llamativo para las flechas.
    private void estilizarBotonTransferencia(JButton boton) {
        boton.setBackground(GRIS_OSCURO);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Consolas", Font.BOLD, 18)); // Consolas hace que las flechas se vean mejor
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMargin(new java.awt.Insets(10, 25, 10, 25));
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    private void cargarCategorias() {
        comboGeneral.removeAllItems();
        comboGeneral.addItem("--- Seleccione una Familia ---");
        List<String> categorias = catalogo.obtenerDescripcionesGenerales();
        for (String cat : categorias) {
            comboGeneral.addItem(cat);
        }
    }

    private void cargarElecciones(String categoria) {
        comboEleccion.removeAllItems();
        comboEleccion.addItem("--- Seleccione un Grupo ---");
        List<String> elecciones = catalogo.obtenerEleccionesPorGeneral(categoria);
        for (String eleccion : elecciones) {
            comboEleccion.addItem(eleccion);
        }
    }

    private void llenarTablaBuscador(List<Producto> productos) {
        modeloBuscador.setRowCount(0);
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No se encontraron productos.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Producto p : productos) {
            String comp = p.tieneComposicion() ? p.getComposicion() : "";
            modeloBuscador.addRow(new Object[]{
                    p.getCodigo(),
                    p.getDescripcionEspecifica(),
                    p.getPrecio(),
                    comp
            });
        }
    }

    private void actualizarLabelTotal() {
        lblTotalCotizacion.setText(String.format("TOTAL: $ %.2f", sumaTotal));
    }
}