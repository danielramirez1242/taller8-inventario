package dao;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Categoria;
import model.Producto;

public class ProductoDAO {

    public boolean insertar(Producto p) {
        String sql = "INSERT INTO producto (nombre, precio, cantidad, id_categoria) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setDouble(2, p.getPrecio());
                ps.setInt(3, p.getCantidad());
                ps.setInt(4, p.getCategoria().getIdCategoria());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el producto: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> listar() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.precio, p.cantidad, "
                   + "c.id_categoria, c.nombre AS nombre_categoria "
                   + "FROM producto p "
                   + "INNER JOIN categoria c ON p.id_categoria = c.id_categoria "
                   + "ORDER BY p.id_producto";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return productos;
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria cat = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre_categoria")
                    );
                    Producto prod = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        cat
                    );
                    productos.add(prod);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar productos: " + e.getMessage());
        }
        return productos;
    }

    public Producto buscarPorId(int id) {
        String sql = "SELECT p.id_producto, p.nombre, p.precio, p.cantidad, "
                   + "c.id_categoria, c.nombre AS nombre_categoria "
                   + "FROM producto p "
                   + "INNER JOIN categoria c ON p.id_categoria = c.id_categoria "
                   + "WHERE p.id_producto = ?";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return null;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Categoria cat = new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getString("nombre_categoria")
                        );
                        return new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getInt("cantidad"),
                            cat
                        );
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el producto: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, cantidad = ?, id_categoria = ? WHERE id_producto = ?";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getNombre());
                ps.setDouble(2, p.getPrecio());
                ps.setInt(3, p.getCantidad());
                ps.setInt(4, p.getCategoria().getIdCategoria());
                ps.setInt(5, p.getIdProducto());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
            return false;
        }
    }
}