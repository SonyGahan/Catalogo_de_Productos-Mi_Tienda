# Sistema de Consulta de Productos 🛒

Un sistema ligero y eficiente desarrollado en **Java 17** para la gestión, filtrado y consulta de un catálogo de productos. El sistema lee datos estructurados desde archivos `.csv`, permitiendo al usuario realizar búsquedas jerárquicas (de lo general a lo específico) e inyectando dinámicamente las composiciones de los productos utilizando **Java Streams**.

## 🚀 Características Principales

* **Persistencia y Seguridad (Backend):** Posee una base de datos local (SQLite) que no pierde información al apagar la PC. Implementa transacciones para que, si un mes el archivo CSV viene roto, la base de datos aborte la carga y proteja el catálogo viejo. Además, utiliza empaquetado (Batching) para que cargue miles de filas en milisegundos.
* **Lógica de Negocio Completa:** El sistema respeta la jerarquía de tus datos (Familia -> Grupo -> Productos específicos). Permite búsquedas flexibles (por menú o por palabra libre) y procesa la información matemáticamente sumando los totales para armar un presupuesto.
* **Experiencia de Usuario (UX) Profesional:** La interfaz gráfica (Swing) tiene un diseño institucional limpio. Cuenta con placeholders ("--- Seleccione ---"), se resetea automáticamente para no confundir al usuario, y tiene ventanas emergentes de confirmación y error.
* **Integración Comercial:** Genera un listado del detalle de la operación seleccionada. Exporta automáticamente dicho listado hacia la API de WhatsApp.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17
* **Paradigma:** Programación Orientada a Objetos (POO)
* **Diseño de Arquitectura:** UML (Casos de Uso, Clases, Actividades, Secuencia) modelado en Draw.io.

---

## 📐 Arquitectura y Diseño del Sistema

A continuación se detallan los diagramas UML que respaldan la arquitectura de la solución:

### 1. Diagrama de Casos de Uso
Define las interacciones principales del usuario con el sistema, incluyendo la carga de datos y las extensiones de búsqueda.

> ![Diagrama de Casos de Uso](assets/diagrama_casos_de_usos.png)

### 2. Diagrama de Clases
Muestra la estructura orientada a objetos, destacando la separación de responsabilidades entre el `Producto`, el `Catalogo` (cerebro del filtrado) y la `InterfazUsuario`.

> ![Diagrama de Clases](assets/diagrama_clases.png)

### 3. Diagrama de Actividades
Ilustra el flujo de ejecución paso a paso, desde que el usuario elige una acción (Consultar o Actualizar) hasta que el sistema muestra el detalle final.

> ![Diagrama de Actividades](assets/diagrama_actividades.png)

### 4. Diagrama de Secuencia
Representa la línea de tiempo de los mensajes entre los objetos cuando se realiza la consulta de un producto y su composición.

> ![Diagrama de Secuencia](assets/diagrama_secuencia.png)

---

## ⚙️ Instalación y Ejecución

1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/sealar24/proyectoAlejandra.git](https://github.com/sealar24/proyectoAlejandra.git)

## 👨‍💻 Autores
* SonyGahan - Desarrollo y Diseño de Arquitectura
* sealar24 - Desarrollo y Diseño de Arquitectura