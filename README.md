# Sistema de Consulta de Productos y Punto de Venta (POS) 🛒

Un sistema robusto y eficiente desarrollado en **Java 17** con interfaz gráfica **(Swing)** para la gestión, filtrado y generación de cotizaciones a través de un catálogo de productos. El sistema migra datos estructurados desde archivos .csv hacia una base de datos relacional embebida **(SQLite)**. Destaca por su panel de cotización interactivo (carrito), que permite al usuario realizar búsquedas jerárquicas o libres, calcular totales en tiempo real e inyectar dinámicamente las composiciones de los productos mediante consultas **SQL (JOIN)**, para finalmente exportar los tickets directamente a WhatsApp.

## 🚀 Características Principales

* **⚙️ Arquitectura Backend y Persistencia Segura:** Integración con base de datos embebida (SQLite) que garantiza la retención segura de la información. Implementa Transacciones SQL (Commit/Rollback) para proteger el catálogo existente ante errores de lectura o archivos CSV corruptos, y utiliza procesamiento por lotes (Batching) para lograr la inserción de miles de registros en milisegundos.
* **🛡️ Flujo de Trabajo y Seguridad:** Incorpora un Menú Principal ("Panel de Control") que separa lógicamente el rol de Administración (actualización de base de datos) del rol de Ventas (armado de pedidos), previniendo modificaciones accidentales durante la atención al cliente.
* **🧠 Lógica de Negocio Avanzada:** El sistema gestiona y respeta la jerarquía relacional de los datos (Familia -> Grupo -> Productos específicos). Incorpora un motor de búsqueda dual (filtros en cascada y búsqueda libre por texto) perfectamente sincronizado.
* **🎨 Experiencia de Usuario (UX) Profesional e Interactiva:** La vista de ventas evoluciona hacia un sistema POS dividiendo la pantalla mediante un `JSplitPane`. Implementa tablas interactivas (`JTable`) que permiten al usuario agregar productos al carrito o quitarlos con un simple **doble clic**, recalculando el presupuesto matemático en tiempo real.
* **📱 Integración Comercial:** Transformación del carrito de compras en tickets detallados listos para el cliente. Cuenta con conectividad directa a la API de WhatsApp (wa.me), lo que permite exportar y enviar la cotización completa de manera automatizada con un solo clic.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17
* **Paradigma:** Programación Orientada a Objetos (POO)
* **Diseño de Arquitectura:** UML (Casos de Uso, Clases, Actividades, Secuencia) modelado en Draw.io.

---

## 📷 Capturas de Pantalla

A continuación te mostramos cómo se ve la aplicación:

![Presentación del Sistema](/assets/Final.JPG)

---

![Vista Principal](/assets/imagen1.JPG)
---

## 📐 Arquitectura y Diseño del Sistema

A continuación se detallan los diagramas UML que respaldan la arquitectura de la solución:

### 1. Diagrama de Casos de Uso
Define las interacciones principales del usuario con el sistema, incluyendo la carga de datos y las extensiones de búsqueda.

> ![Diagrama de Casos de Uso](assets/diagrama_casos_de_usos.png)

### 2. Diagrama de Clases
Muestra la estructura orientada a objetos, destacando la separación de responsabilidades entre el `modelo.Producto`, el `dao.Catalogo` (cerebro del filtrado) y la `vista.InterfazUsuario`.

> ![Diagrama de Clases](assets/diagrama_clases.png)

### 3. Diagrama de Actividades
Ilustra el flujo de ejecución paso a paso, desde que el usuario elige una acción (Consultar o Actualizar) hasta que el sistema muestra el detalle final.

> ![Diagrama de Actividades](assets/diagrama_actividades.png)

### 4. Diagrama de Secuencia
Representa la línea de tiempo de los mensajes entre los objetos cuando se realiza la consulta de un producto y su composición.

> ![Diagrama de Secuencia](assets/diagrama_secuencia.png)

---

## 🔧 Instalación y Uso

Si deseas correr este proyecto localmente:

1.  Clona el repositorio:
    ```bash
    git clone [https://github.com/SonyGahan/Catalogo_de_Productos-Mi_Tienda.git](https://github.com/SonyGahan/Catalogo_de_Productos-Mi_Tienda.git)
    ```
2.  Ejecuta el programa para comenzar a trabajar con la tienda.

---

## 💡 Contribuciones

Las contribuciones son bienvenidas. Si deseas mejorar el proyecto o agregar nuevas funcionalidades, sigue estos pasos:

1. **Haz un Fork** del repositorio.
2. Crea una nueva rama con una descripción clara:
   ```bash
   git checkout -b nueva-funcionalidad
   ```
5. Crea un **Pull Request** en este repositorio.

---

## 📬 Contacto

Si tienes alguna duda o sugerencia, puedes contactarme a través de GitHub:

[GitHub: SonyGahan](https://github.com/SonyGahan)

---

## 📝 Licencia

Este proyecto está bajo la **Licencia MIT**. Consulta el archivo [LICENSE](LICENSE) para más detalles.

---

## 💻 Agradecimientos

🚀 Gracias por visitar mi repositorio y por tu interés en este proyecto. ¡Espero que te sea útil! 😄

## ⌨️ Construido con ❤️ por Sony Gahan 😊