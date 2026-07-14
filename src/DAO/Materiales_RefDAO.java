
package DAO;

import BEAN.Materiales_Ref;
import UTIL.DbBean;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Materiales_RefDAO {
    
    public Vector<Materiales_Ref> listarMateriales() {
        Vector<Materiales_Ref> lista = new Vector<>();
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_ListarMateriales()}";

        try {
            CallableStatement cs = cn.prepareCall(sql);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Materiales_Ref obj = new Materiales_Ref(); // <--- Usamos TU nombre de clase
                obj.setIdMaterial(rs.getInt("idMaterial"));
                obj.setNombre(rs.getString("nombre"));
                obj.setUnidadMedidad(rs.getString("unidadMedidad")); // Ojo aquí con tu getter
                obj.setPrecioCosto(rs.getDouble("precioCosto"));
                obj.setCategoria(rs.getString("categoria"));
                
                lista.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return lista;
    }
    
   public boolean registrarMaterial(BEAN.Materiales_Ref mat) {
    boolean flag = false;
    DbBean db = new DbBean();
    Connection cn = db.connect();
    
    // OJO AQUÍ: Solo hay 4 signos de interrogación (?, ?, ?, ?)
    String sql = "{call sp_RegistrarMaterial(?, ?, ?, ?)}";

    try {
        CallableStatement cs = cn.prepareCall(sql);
        
        // NO enviamos el ID (cs.setInt(1, mat.getId...)) porque es Nuevo
        
        // Empezamos directo con los datos:
        cs.setString(1, mat.getNombre());
        cs.setString(2, mat.getUnidadMedidad());
        cs.setDouble(3, mat.getPrecioCosto());
        cs.setString(4, mat.getCategoria());
        
        // ¡Y YA ESTÁ! Si tienes un cs.setString(5, ...) BORRALO.
        // Ese es el que te está dando el error.

        int i = cs.executeUpdate();
        flag = (i == 1);
    } catch (SQLException e) {
        System.out.println("Error al guardar: " + e.getMessage());
    } finally {
        db.desconectar();
    }
    return flag;
}
     
    public boolean actualizaMateriales(Materiales_Ref mat){
        boolean flag = false;
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_ActualizarMaterial(?,?,?,?,?)}";
        try{
            CallableStatement cs = cn.prepareCall(sql);
            cs.setInt(1, mat.getIdMaterial());
            cs.setString(2, mat.getNombre());
            cs.setString(3, mat.getUnidadMedidad());
            cs.setDouble(4, mat.getPrecioCosto());
            cs.setString(5, mat.getCategoria());
            
            int i = cs.executeUpdate();
            flag = (i == 1);
        }catch (SQLException e){
            System.out.println("Error al actualizar" + e.getMessage());
        }finally{
            db.desconectar();
        }
        return flag;
    }
    
    public boolean elminarMaterial(int id){
        boolean flag = false;
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_EliminarMaterial(?)}";
        
        try{
            CallableStatement cs = cn.prepareCall(sql);
            cs.setInt(1, id);
            
            int i = cs.executeUpdate();
            flag = (i == 1);
        }catch(SQLException e){
            System.out.println("Error al elimnar" + e.getMessage());
        }finally{
            db.desconectar();
        }
        return flag;
        
        
    }
    public Vector<Materiales_Ref> buscarMateriales(String texto) {
        Vector<Materiales_Ref> lista = new Vector<>();
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_BuscarMaterial(?)}";

        try {
            CallableStatement cs = cn.prepareCall(sql);
            cs.setString(1, texto); // Pasamos lo que escribió tu papá
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Materiales_Ref obj = new Materiales_Ref();
                obj.setIdMaterial(rs.getInt("idMaterial"));
                obj.setNombre(rs.getString("nombre"));
                obj.setUnidadMedidad(rs.getString("unidadMedidad"));
                obj.setPrecioCosto(rs.getDouble("precioCosto"));
                obj.setCategoria(rs.getString("categoria"));
                
                lista.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return lista;
    }
}
