
package UTIL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbBean {
    public Connection dbCon;
    
    public String url = "jdbc:sqlserver://localhost:1433;databaseName=EbanixSac;encrypt=false;trustServerCertificate=true;loginTimeout=30;";
    
    public String user = "sa"; 
    
    public String password = "123456789"; 

    public DbBean() {
    }

    public Connection connect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            dbCon = DriverManager.getConnection(url, user, password);
            System.out.println("--- ¡CONEXIÓN EXITOSA CON EBANIX SAC! ---");
            
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR CRÍTICO: No encuentro la librería .jar. ¿Seguro que la agregaste?");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("ERROR SQL: No pude entrar a la base de datos.");
            System.out.println("Verifica: 1. Que el servicio SQL esté prendido. 2. Tu usuario/clave.");
            System.out.println("Mensaje técnico: " + e.getMessage());
        }
        return dbCon;
    }
    
    public void desconectar() {
        try {
            if (dbCon != null) {
                dbCon.close();
            }
        } catch (SQLException e) {
        }
    }

    public static void main(String[] args) {
        DbBean obj = new DbBean();
        obj.connect();
    }

    
    
}
