package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class UsuarioDAO {

    public boolean validarUsuario(String usuario, String clave) {
        boolean acceso = false; // Por defecto, la puerta está cerrada 🔒
        UTIL.DbBean db = new UTIL.DbBean();
        Connection cn = db.connect();
        
        try {
            String sql = "{call sp_ValidarUsuario(?, ?)}";
            CallableStatement cs = cn.prepareCall(sql);
            cs.setString(1, usuario);
            cs.setString(2, clave);
            
            ResultSet rs = cs.executeQuery();
            
            if (rs.next()) {
                // Si la consulta trajo un resultado, ¡ES CORRECTO! 🔓
                acceso = true;
            }
            
        } catch (Exception e) {
            System.out.println("Error login: " + e);
        } finally {
            db.desconectar();
        }
        return acceso;
    }
}