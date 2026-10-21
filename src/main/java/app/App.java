package app;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import vista.VentanaInventario;

public class App {

    public static void main(String[] args) {
        try (Connection con = Conexion.getConexion()) {
            if (con != null) {
                System.out.println("Conexión exitosa");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión de prueba: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            new VentanaInventario().setVisible(true);
        });
    }
}