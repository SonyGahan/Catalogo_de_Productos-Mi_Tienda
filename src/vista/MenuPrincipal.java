package vista;

import dao.Catalogo;
import dao.ConexionDB;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class MenuPrincipal {
    private JFrame frame;
    private Catalogo catalogo;

    // Paleta de colores Institucional
    private final Color AZUL_CORPORATIVO = new Color(0, 86, 179);
    private final Color VERDE_WPP = new Color(37, 211, 102);
    private final Color ROJO_SUAVE = new Color(220, 53, 69);
    private final Color GRIS_OSCURO = new Color(50, 50, 50);

    public MenuPrincipal(Catalogo catalogo) {
        this.catalogo = catalogo;
        construirInterfaz();
    }

    private void construirInterfaz() {
        // 1. Configuración de la Ventana
        frame = new JFrame("Sistema de Gestión - Menú Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Amplia un poquito el ancho y alto para que todo respire perfecto
        frame.setSize(420, 420);
        frame.setLayout(new BorderLayout());

        // 2. Título Superior
        JLabel lblTitulo = new JLabel("Panel de Control", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(lblTitulo, BorderLayout.NORTH);

        // 3. Panel Central con Botones
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));

        JButton btnPedidos = new JButton("🛒 Pedidos por Productos");
        JButton btnActualizar = new JButton("🔄 Actualizar Catálogo");
        JButton btnSalir = new JButton("❌ Salir del Sistema");

        estilizarBoton(btnPedidos, VERDE_WPP, Color.WHITE);
        estilizarBoton(btnActualizar, AZUL_CORPORATIVO, Color.WHITE);
        estilizarBoton(btnSalir, ROJO_SUAVE, Color.WHITE);

        panelBotones.add(btnPedidos);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnSalir);

        frame.add(panelBotones, BorderLayout.CENTER);

        // 4. Panel Inferior - Botón de Tema a seleccionar
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        JButton btnTema = new JButton("🌙 Cambiar a Modo Oscuro");
        estilizarBoton(btnTema, GRIS_OSCURO, Color.WHITE);
        panelSur.add(btnTema);
        frame.add(panelSur, BorderLayout.SOUTH);

        // ==========================================
        // EVENTOS DE LOS BOTONES
        // ==========================================

        // EVENTO GRAFICO: Cambiar Tema: Claro/Oscuro
        btnTema.addActionListener(e -> {
            try {
                // Pregunta si el tema actual es oscuro
                boolean esOscuro = UIManager.getLookAndFeel() instanceof FlatDarkLaf;

                if (esOscuro) {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    btnTema.setText("🌙 Cambiar a Modo Oscuro");
                    btnTema.setBackground(GRIS_OSCURO);
                    btnTema.setForeground(Color.WHITE);
                } else {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    btnTema.setText("☀️ Cambiar a Modo Claro");
                    btnTema.setBackground(new Color(240, 240, 240));
                    btnTema.setForeground(Color.BLACK);
                }
                // La ventana se redibuje con el nuevo tema
                SwingUtilities.updateComponentTreeUI(frame);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // EVENTO 1: Abrir la pantalla de Pedidos - Catálogo
        btnPedidos.addActionListener(e -> {
            InterfazUsuario ui = new InterfazUsuario(catalogo);
            ui.mostrar();
            frame.setVisible(false);
        });

        // EVENTO 2: Actualizar la Base de Datos
        btnActualizar.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(frame,
                    "¿Desea borrar la base de datos actual y cargar el nuevo catálogo desde los archivos CSV?",
                    "Actualización Mensual", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(frame, "Iniciando actualización. Esto puede tomar unos segundos...", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                boolean exito = ConexionDB.cargarDatosDesdeCSV("src/datos/tabla_productos.csv", "src/datos/tabla_componentes.csv");

                if(exito){
                    JOptionPane.showMessageDialog(frame, "Base de datos actualizada correctamente. ¡Catálogo listo!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Error al leer los archivos CSV o guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // EVENTO 3: Salir de Programa
        btnSalir.addActionListener(e -> {
            System.exit(0);
        });

        // Centra la ventana en la pantalla
        frame.setLocationRelativeTo(null);
    }

    // Método auxiliar de diseño
    private void estilizarBoton(JButton boton, Color colorFondo, Color colorTexto) {
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Esto respeta los bordes redondeados y efectos modernos de FlatLaf.
        boton.setMargin(new java.awt.Insets(10, 20, 10, 20));
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}