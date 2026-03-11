public class Producto {
    private int codigo;
    private String descripcionGeneral;
    private String descripcionEleccion;
    private String descripcionEspecifica;
    private double precio;
    private String composicion; // Inicialmente puede ser null

    // Constructor
    public Producto(int codigo, String descripcionGeneral, String descripcionEleccion,
                    String descripcionEspecifica, double precio) {
        this.codigo = codigo;
        this.descripcionGeneral = descripcionGeneral;
        this.descripcionEleccion = descripcionEleccion;
        this.descripcionEspecifica = descripcionEspecifica;
        this.precio = precio;

        this.composicion = ""; // Por defecto vacío
    }

    // Método que determina si el producto tiene una composicion, (mix de producto)
    public boolean tieneComposicion() {
        return this.composicion != null && !this.composicion.trim().isEmpty();
    }

    // Getters
    public int getCodigo() { return codigo; }
    public String getDescripcionGeneral() { return descripcionGeneral; }
    public String getDescripcionEleccion() { return descripcionEleccion; }
    public String getDescripcionEspecifica() { return descripcionEspecifica; }
    public double getPrecio() { return precio; }
    public String getComposicion() { return composicion; }

    // Setters
    public void setComposicion(String composicion) { this.composicion = composicion; }
    public void setPrecio(double precio) { this.precio = precio; }

    @Override
    public String toString() {
        return codigo + " - " + descripcionEspecifica + " | $" + precio;
    }
}