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

public class CategoriaDAO {

    public List<Categoria> listar() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre FROM categoria ORDER BY id_categoria";

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return categorias;
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categorias.add(new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
                    ));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar categorías: " + e.getMessage());
        }
        return categorias;
    }
}