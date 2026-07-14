
import BEAN.Detalle_Presupuesto;
import UTIL.DbBean;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Detalle_PresupuestoDAO {

    // 1. GUARDAR UN ÍTEM (Registrar)
    public boolean registrarDetalle(Detalle_Presupuesto det) {
        boolean flag = false;
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_GuardarDetalle(?, ?, ?, ?)}";

        try {
            CallableStatement cs = cn.prepareCall(sql);
            cs.setInt(1, det.getIdProyecto());
            cs.setInt(2, det.getIdMaterial());
            cs.setDouble(3, det.getCantidad());
            cs.setDouble(4, det.getPrecioMomentaneo());

            int i = cs.executeUpdate();
            flag = (i == 1);
        } catch (SQLException e) {
            System.out.println("Error al guardar detalle: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return flag;
    }

    // 2. LISTAR DETALLES DE UN PROYECTO
    public Vector<Detalle_Presupuesto> listarDetallesPorProyecto(int idProyecto) {
        Vector<Detalle_Presupuesto> lista = new Vector<>();
        DbBean db = new DbBean();
        Connection cn = db.connect();
        // JOIN para poder (en el futuro) mostrar el nombre del material si quisieras
        String sql = "SELECT d.*, m.nombre FROM Detalle_Presupuesto d "
                   + "INNER JOIN Materiales_Ref m ON d.idMaterial = m.idMaterial "
                   + "WHERE d.idProyecto = ?";

        try {
            java.sql.PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, idProyecto);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Detalle_Presupuesto obj = new Detalle_Presupuesto();
                obj.setIdDetalle(rs.getInt("idDetalle"));
                obj.setIdProyecto(rs.getInt("idProyecto"));
                obj.setIdMaterial(rs.getInt("idMaterial"));
                obj.setCantidad(rs.getDouble("cantidad"));
                obj.setPrecioMomentaneo(rs.getDouble("precioMomentaneo"));
                obj.setSubtotal(rs.getDouble("subtotal"));
                
                lista.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar detalles: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return lista;
    }

    // 3. ACTUALIZAR DETALLE (NUEVO)
    public boolean actualizarDetalle(Detalle_Presupuesto det) {
        boolean flag = false;
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_ActualizarDetalle(?, ?, ?)}";

        try {
            CallableStatement cs = cn.prepareCall(sql);
            cs.setInt(1, det.getIdDetalle()); // Identificador único de la fila
            cs.setDouble(2, det.getCantidad()); // Nueva cantidad
            cs.setDouble(3, det.getPrecioMomentaneo()); // Nuevo precio

            int i = cs.executeUpdate();
            flag = (i == 1);
        } catch (SQLException e) {
            System.out.println("Error al actualizar detalle: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return flag;
    }

    // 4. ELIMINAR DETALLE (NUEVO)
    public boolean eliminarDetalle(int idDetalle) {
        boolean flag = false;
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_EliminarDetalle(?)}";

        try {
            CallableStatement cs = cn.prepareCall(sql);
            cs.setInt(1, idDetalle);

            int i = cs.executeUpdate();
            flag = (i == 1);
        } catch (SQLException e) {
            System.out.println("Error al eliminar detalle: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return flag;
    }
}