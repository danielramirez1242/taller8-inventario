package vista;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Categoria;
import model.Producto;

public class VentanaInventario extends JFrame {

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JTextField txtCantidad = new JTextField();
    private final JComboBox<Categoria> cmbCategoria = new JComboBox<>();

    private final DefaultTableModel modeloTabla;
    private final JTable tablaProductos;

    private int idSeleccionado = -1;

    public VentanaInventario() {
        setTitle("Inventario - Tienda de Don Ernesto");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));

        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 8, 8));
        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Precio:"));
        panelCampos.add(txtPrecio);
        panelCampos.add(new JLabel("Cantidad:"));
        panelCampos.add(txtCantidad);
        panelCampos.add(new JLabel("Categoría:"));
        panelCampos.add(cmbCategoria);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panelSuperior.add(panelCampos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Precio", "Cant.", "Categoría"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarFilaSeleccionada();
            }
        });

        cargarCategorias();
        cargarTabla();
    }

    private void cargarCategorias() {
        cmbCategoria.removeAllItems();
        List<Categoria> lista = categoriaDAO.listar();
        for (Categoria c : lista) {
            cmbCategoria.addItem(c);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Producto> lista = productoDAO.listar();
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombre(),
                p.getPrecio(),
                p.getCantidad(),
                p.getCategoria().getNombre()
            });
        }
    }

    private void cargarFilaSeleccionada() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            return;
        }

        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtPrecio.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtCantidad.setText(modeloTabla.getValueAt(fila, 3).toString());

        String nombreCat = modeloTabla.getValueAt(fila, 4).toString();
        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            Categoria item = cmbCategoria.getItemAt(i);
            if (item.getNombre().equals(nombreCat)) {
                cmbCategoria.setSelectedIndex(i);
                break;
            }
        }
    }

    private Producto leerDatosFormulario() {
        String nombre = txtNombre.getText().trim();
        String textoPrecio = txtPrecio.getText().trim();
        String textoCantidad = txtCantidad.getText().trim();
        Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();

        if (nombre.isEmpty() || textoPrecio.isEmpty() || textoCantidad.isEmpty() || categoria == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            double precio = Double.parseDouble(textoPrecio);
            int cantidad = Integer.parseInt(textoCantidad);
            return new Producto(idSeleccionado, nombre, precio, cantidad, categoria);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio y cantidad deben ser valores numéricos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Datos inválidos", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private void guardar() {
        Producto p = leerDatosFormulario();
        if (p == null) {
            return;
        }

        if (productoDAO.insertar(p)) {
            JOptionPane.showMessageDialog(this, "Producto registrado.");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void actualizar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Producto p = leerDatosFormulario();
        if (p == null) {
            return;
        }

        if (productoDAO.actualizar(p)) {
            JOptionPane.showMessageDialog(this, "Producto actualizado.");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar el producto seleccionado?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (productoDAO.eliminar(idSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Producto eliminado.");
                cargarTabla();
                limpiarFormulario();
            }
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }
        tablaProductos.clearSelection();
    }
}