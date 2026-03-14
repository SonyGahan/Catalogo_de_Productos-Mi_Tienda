package modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoTest {

    @Test
    public void testInyeccionDeComposicion() {
        // 1. ARRANGE (Preparar el escenario)
        // Crea un producto de prueba ficticio
        Producto miProducto = new Producto(999, "SNACKS", "MIX", "Mix de Frutos Secos", 5000.0);
        String composicionEsperada = "Contiene: Nueces - Almendras - Pasas";

        // 2. ACT (Actuar / Ejecutar la acción)
        // Inyecta la composición usando tu método setter
        miProducto.setComposicion(composicionEsperada);

        // 3. ASSERT (Afirmar / Comprobar el resultado)
        // Le pregunta a Java: "¿Es verdad que el producto ahora tiene composición?"
        assertTrue(miProducto.tieneComposicion(), "El producto debería decir que SI tiene composición.");
        // Pregunta: "¿La composición guardada es exactamente igual a la que le mandamos?"
        assertEquals(composicionEsperada, miProducto.getComposicion(), "Los textos de la composición no coinciden.");
    }

    @Test
    public void testProductoSinComposicion() {
        // 1. ARRANGE (Preparar)
        Producto aceite = new Producto(1065, "ACEITES DE GIRASOL", "ACEITE PARA FREIR GREEN OLIVE:", "Aceite de Girasol Blend GREEN OLIVE Mezcla x 3 Lts.", 12132.0);

        // 2. ACT (Actuar)
        // No le inyectamos nada.

        // 3. ASSERT (Afirmar)
        assertFalse(aceite.tieneComposicion(), "El producto NO debería tener composición, porque no se la agregamos.");
        assertEquals("", aceite.getComposicion(), "El texto de la composición debería estar vacío.");
    }

    @Test
    public void testAsignacionDeCodigoYPrecio() {
        // 1. ARRANGE (Preparar el escenario)
        // Define qué valores exactos se van a usar
        int codigoEsperado = 777;
        double precioEsperado = 15500.75;

        // Crea el producto con esos valores
        Producto productoPrueba = new Producto(codigoEsperado, "CAFÉ", "CAFÉ MOLIDO", "Café Molido Brasil x 500g", precioEsperado);

        // 2. ACT (Actuar)
        // Extrae los valores que el objeto guardó internamente
        int codigoReal = productoPrueba.getCodigo();
        double precioReal = productoPrueba.getPrecio();

        // 3. ASSERT (Afirmar)
        // Compara lo que espera vs lo que realmente guardó
        assertEquals(codigoEsperado, codigoReal, "El código del producto no se guardó correctamente.");
        assertEquals(precioEsperado, precioReal, "El precio del producto no se guardó correctamente.");
    }
}