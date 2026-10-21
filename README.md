# Taller 8 - Inventario Tienda de Don Ernesto

## Pasos para la ejecución del proyecto

1. Iniciar el servicio de MySQL en XAMPP (puerto 3306).
2. Ejecutar el archivo `script.sql` en phpMyAdmin o MySQL Workbench para crear la base de datos `tienda`, las tablas `categoria` y `producto`, y los datos iniciales.
3. Verificar el usuario y contraseña de MySQL en `src/main/java/conexion/Conexion.java` (por defecto usa `root` y clave vacía).
4. Abrir el proyecto en NetBeans para que Maven descargue la dependencia `mysql-connector-java` configurada en el `pom.xml`.
5. Ejecutar la clase principal `app.App`.
