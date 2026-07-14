package DAO;

import BEAN.Proyecto;
import UTIL.DbBean;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ProyectoDAO {

    public int registrarPresupuesto(BEAN.Proyecto pro, javax.swing.table.DefaultTableModel modelo) {
        
        int idRetorno = 0; // Si falla, devolveremos 0
        
        UTIL.DbBean db = new UTIL.DbBean();
        Connection cn = db.connect();
        
        try {
            cn.setAutoCommit(false); 

            // --- 1. CABECERA ---
            String sqlCabecera = "{call sp_RegistrarProyecto(?,?,?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement cs = cn.prepareCall(sqlCabecera);
            
            cs.setInt(1, pro.getIdCliente());
            cs.setString(2, pro.getNombreProyecto());
            cs.setString(3, pro.getDescripcion());
            cs.setDate(4, pro.getFechaInicio());
            cs.setDate(5, pro.getFechaEntregaPactada());
            cs.setString(6, pro.getEstado());
            cs.setDouble(7, pro.getCostoMateriales());
            cs.setDouble(8, pro.getManoObraPorcentaje());
            cs.setDouble(9, pro.getPrecioVentaFinal());
            cs.setDouble(10, pro.getMontoAdelanto());
            cs.setDouble(11, pro.getSaldoPendiente());
            
            cs.registerOutParameter(12, java.sql.Types.INTEGER);
            
            cs.executeUpdate();
            
            // CAMBIO 2: Guardamos este ID para devolverlo al final
            idRetorno = cs.getInt(12); 
            
            // --- 2. DETALLE ---
            String sqlDetalle = "{call sp_RegistrarDetalle(?, ?, ?, ?, ?)}";
            CallableStatement csDetalle = cn.prepareCall(sqlDetalle);
            
            for (int i = 0; i < modelo.getRowCount(); i++) {
                // ... (tu código de lectura de tabla sigue igual) ...
                int idMat = Integer.parseInt(modelo.getValueAt(i, 0).toString());
                double precio = Double.parseDouble(modelo.getValueAt(i, 2).toString());
                int cant = Integer.parseInt(modelo.getValueAt(i, 3).toString());
                double sub = Double.parseDouble(modelo.getValueAt(i, 4).toString());

                csDetalle.setInt(1, idRetorno); // Usamos el ID capturado
                csDetalle.setInt(2, idMat);
                csDetalle.setInt(3, cant);
                csDetalle.setDouble(4, precio);
                csDetalle.setDouble(5, sub);
                
                csDetalle.executeUpdate();
            }
            
            cn.commit();
            // (Ya no usamos flag = true, porque devolveremos el ID)
            
        } catch (Exception e) {
            try { cn.rollback(); } catch (SQLException ex) {}
            System.out.println("Error: " + e.getMessage());
            idRetorno = 0; // Si hubo error, devolvemos 0
        } finally {
            db.desconectar();
        }
        
        return idRetorno; // CAMBIO 3: Devolvemos el número mágico
    }

    // 2. LISTAR (Simple)
    public Vector<Proyecto> listarProyectos() {
        Vector<Proyecto> lista = new Vector<>();
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "SELECT * FROM Proyectos ORDER BY idProyecto DESC"; 

        try {
            java.sql.PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Proyecto obj = new Proyecto();
                obj.setIdProyecto(rs.getInt("idProyecto"));
                obj.setIdCliente(rs.getInt("idCliente"));
                obj.setNombreProyecto(rs.getString("nombreProyecto"));
                obj.setDescripcion(rs.getString("descripcion")); // Agregado
                obj.setFechaInicio(rs.getDate("fechaInicio"));
                obj.setFechaEntregaPactada(rs.getDate("fechaEntregaPactada")); // Agregado
                obj.setEstado(rs.getString("estado")); 
                obj.setCostoMateriales(rs.getDouble("costoMateriales")); // Agregado
                obj.setPrecioVentaFinal(rs.getDouble("precioVentaFinal"));
                obj.setMontoAdelanto(rs.getDouble("montoAdelanto")); // Agregado
                obj.setSaldoPendiente(rs.getDouble("saldoPendiente"));
                
                lista.add(obj);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar proyectos: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return lista;
    }

    // 3. ACTUALIZAR PROYECTO (NUEVO)
    public boolean actualizarProyecto(Proyecto pro) {
        boolean flag = false;
        DbBean db = new DbBean();
        Connection cn = db.connect();
        // Llamamos al nuevo procedure de actualización
        String sql = "{call sp_ActualizarProyecto(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"; 

        try {
            CallableStatement cs = cn.prepareCall(sql);
            cs.setInt(1, pro.getIdProyecto()); // ¡El ID es la clave para saber cuál tocar!
            cs.setString(2, pro.getNombreProyecto());
            cs.setString(3, pro.getDescripcion());
            cs.setDate(4, pro.getFechaInicio());
            cs.setDate(5, pro.getFechaEntregaPactada());
            cs.setString(6, pro.getEstado()); // ej: "En Proceso", "Terminado"
            cs.setDouble(7, pro.getCostoMateriales());
            cs.setDouble(8, pro.getManoObraPorcentaje());
            cs.setDouble(9, pro.getPrecioVentaFinal());
            cs.setDouble(10, pro.getMontoAdelanto());

            int i = cs.executeUpdate();
            flag = (i == 1);
        } catch (SQLException e) {
            System.out.println("Error al actualizar proyecto: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return flag;
    }

    // 4. ELIMINAR PROYECTO (NUEVO - ELIMINACIÓN EN CASCADA)
    public boolean eliminarProyecto(int idProyecto) {
        boolean flag = false;
        DbBean db = new DbBean();
        Connection cn = db.connect();
        String sql = "{call sp_EliminarProyecto(?)}";

        try {
            CallableStatement cs = cn.prepareCall(sql);
            cs.setInt(1, idProyecto);

            int i = cs.executeUpdate();
            // Aquí puede devolver más de 1 porque borra detalles + proyecto
            // Así que simplemente validamos si i > 0
            flag = (i > 0); 
        } catch (SQLException e) {
            System.out.println("Error al eliminar proyecto: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return flag;
    }
    public boolean cerrarVenta(int idProyecto, double manoObra, double precioFinal, double adelanto, double saldo) {
        boolean flag = false;
        UTIL.DbBean db = new UTIL.DbBean();
        Connection cn = db.connect();
        
        try {
            // Llamamos al procedure que actualiza los montos
            String sql = "{call sp_CerrarVentaProyecto(?, ?, ?, ?, ?)}";
            CallableStatement cs = cn.prepareCall(sql);
            
            cs.setInt(1, idProyecto);
            cs.setDouble(2, manoObra);
            cs.setDouble(3, precioFinal);
            cs.setDouble(4, adelanto);
            cs.setDouble(5, saldo);
            
            int r = cs.executeUpdate();
            if (r > 0) {
                flag = true;
            }
            
        } catch (Exception e) {
            System.out.println("Error al cerrar venta: " + e.getMessage());
        } finally {
            db.desconectar();
        }
        return flag;
    }
    public javax.swing.table.DefaultTableModel listarHistorial() {
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel();
        // Definimos las columnas
        modelo.addColumn("ID");
        modelo.addColumn("Cliente");
        modelo.addColumn("Proyecto");
        modelo.addColumn("Fecha");
        modelo.addColumn("Estado");
        modelo.addColumn("Precio Final");
        
        UTIL.DbBean db = new UTIL.DbBean();
        Connection cn = db.connect();
        try {
            CallableStatement cs = cn.prepareCall("{call sp_ListarHistorial()}");
            ResultSet rs = cs.executeQuery();
            
            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getInt("idProyecto");
                fila[1] = rs.getString("NombreCliente");
                fila[2] = rs.getString("nombreProyecto");
                fila[3] = rs.getDate("fechaInicio");
                fila[4] = rs.getString("estado");
                fila[5] = rs.getDouble("precioVentaFinal");
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            System.out.println("Error listar historial: " + e);
        } finally {
            db.desconectar();
        }
        return modelo;
    }
    // TRAER RESUMEN PARA EL DASHBOARD
    public double[] obtenerResumenDashboard() {
        double[] datos = new double[2]; // Posicion 0: Cantidad, Posicion 1: Dinero
        
        UTIL.DbBean db = new UTIL.DbBean();
        Connection cn = db.connect();
        try {
            CallableStatement cs = cn.prepareCall("{call sp_ObtenerResumen()}");
            ResultSet rs = cs.executeQuery();
            
            if (rs.next()) {
                datos[0] = rs.getInt("CantidadPendientes");
                datos[1] = rs.getDouble("DineroPorCobrar");
            }
        } catch (Exception e) {
            System.out.println("Error dashboard: " + e);
        } finally {
            db.desconectar();
        }
        return datos;
    }
    
    
    }