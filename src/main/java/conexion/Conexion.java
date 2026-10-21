package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/tienda";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                null,
                "No se pudo conectar a la base de datos: " + e.getMessage(),
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
    }
}