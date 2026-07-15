# Sistema-Carpinteria-Java
Descripción
Este es un sistema de escritorio desarrollado para digitalizar y gestionar los presupuestos, clientes y proyectos de un taller de carpintería ("Ebanix"). Fue creado como un proyecto práctico para aplicar mis conocimientos de programación, pasando de los cálculos manuales a un entorno digital automatizado.

Tecnologías Utilizadas 💻
Lenguaje: Java

Entorno de Desarrollo (IDE): Apache NetBeans

Base de Datos: SQL Server

Interfaz Gráfica: Java Swing (JFrame, JPanel, Tablas)

Conceptos de POO Aplicados 🧠
Para este proyecto, apliqué los siguientes pilares de la Programación Orientada a Objetos:

Clases y Objetos: Creación de plantillas (Clases) para representar entidades del mundo real, como Cliente y Proyecto.

Encapsulamiento: Uso de atributos privados (private) protegidos mediante métodos getter y setter en las clases de tipo BEAN.

Arquitectura de Software (Separación de Responsabilidades): El código está estructurado en paquetes para mantener el orden:

BEAN: Donde viven nuestros objetos puros.

DAO: Donde está la lógica y la conexión directa con la base de datos (Consultas, Inserciones, Eliminación).

UI: Donde están las ventanas visuales.

UTIL: Para la conexión a SQL Server.
