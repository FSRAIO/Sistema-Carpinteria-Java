package DAO;

import BEAN.Clientes;
import UTIL.DbBean;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ClientesDAO {

    // 1. REGISTRAR
    public boolean registrarCliente(Clientes cli) {
        boolean flag = false;
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_GuardarCliente(?, ?, ?, ?)}";

        try {
            CallableStatement cs = cn.prepareCall(sql);
            cs.setString(1, cli.getNombre());
            cs.setString(2, cli.getTelefono());
            cs.setString(3, cli.getDireccion());
            cs.setString(4, cli.getEmail());

            int i = cs.executeUpdate();
            flag = (i == 1);
        } catch (SQLException e) {
            System.out.println("Error al guardar cliente: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return flag;
    }

    // 2. BUSCAR
    public Vector<Clientes> buscarCliente(String busqueda) {
        Vector<Clientes> lista = new Vector<>();
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_BuscarCliente(?)}";

        try {
            CallableStatement cs = cn.prepareCall(sql);
            cs.setString(1, busqueda);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Clientes obj = new Clientes();
                obj.setIdCliente(rs.getInt("idCliente"));
                obj.setNombre(rs.getString("nombre"));
                obj.setTelefono(rs.getString("telefono"));
                obj.setDireccion(rs.getString("direccion"));
                obj.setEmail(rs.getString("email"));
                obj.setFechaRegistro(rs.getTimestamp("fechaRegistro"));
                lista.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return lista;
    }

    // 3. ACTUALIZAR (Nuevo)
    public boolean actualizarCliente(Clientes cli) {
        boolean flag = false;
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_ActualizarCliente(?, ?, ?, ?, ?)}"; // 5 datos (ID + 4 campos)

        try {
            CallableStatement cs = cn.prepareCall(sql);
            cs.setInt(1, cli.getIdCliente()); // ¡Vital! El ID no cambia, solo sirve para buscar
            cs.setString(2, cli.getNombre());
            cs.setString(3, cli.getTelefono());
            cs.setString(4, cli.getDireccion());
            cs.setString(5, cli.getEmail());

            int i = cs.executeUpdate();
            flag = (i == 1);
        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return flag;
    }

    public boolean eliminarCliente(int id) {
        boolean flag = false;
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_EliminarCliente(?)}";

        try {
            CallableStatement cs = cn.prepareCall(sql);
            cs.setInt(1, id);

            int i = cs.executeUpdate();
            flag = (i == 1);
        } catch (SQLException e) {
            // AQUÍ SALDRÁ UN ERROR SI EL CLIENTE TIENE PROYECTOS (Es normal, es seguridad)
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return flag;
    }
}