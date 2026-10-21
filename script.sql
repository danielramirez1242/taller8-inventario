CREATE DATABASE IF NOT EXISTS tienda;
USE tienda;

CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    id_categoria INT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

INSERT INTO categoria (nombre)
VALUES ('Lacteos'), ('Aseo'), ('Bebidas'), ('Granos');

INSERT INTO producto (nombre, precio, cantidad, id_categoria) VALUES
('Leche entera', 4500.00, 20, 1),
('Jabon rey', 2800.00, 15, 2),
('Arroz Diana 1kg', 4200.00, 30, 4);

SELECT * FROM producto;