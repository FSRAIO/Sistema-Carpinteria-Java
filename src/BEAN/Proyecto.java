
package BEAN;

import java.sql.Date;


public class Proyecto {
    private int idProyecto;
    private int idCliente;
    private String nombreProyecto;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaEntregaPactada;
    private String estado;
    private double costoMateriales;
    private double manoObraPorcentaje;
    private double precioVentaFinal;
    private double montoAdelanto;
    private double saldoPendiente;

    public Proyecto() {
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaEntregaPactada() {
        return fechaEntregaPactada;
    }

    public void setFechaEntregaPactada(Date fechaEntregaPactada) {
        this.fechaEntregaPactada = fechaEntregaPactada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getCostoMateriales() {
        return costoMateriales;
    }

    public void setCostoMateriales(double costoMateriales) {
        this.costoMateriales = costoMateriales;
    }

    public double getManoObraPorcentaje() {
        return manoObraPorcentaje;
    }

    public void setManoObraPorcentaje(double manoObraPorcentaje) {
        this.manoObraPorcentaje = manoObraPorcentaje;
    }

    public double getPrecioVentaFinal() {
        return precioVentaFinal;
    }

    public void setPrecioVentaFinal(double precioVentaFinal) {
        this.precioVentaFinal = precioVentaFinal;
    }

    public double getMontoAdelanto() {
        return montoAdelanto;
    }

    public void setMontoAdelanto(double montoAdelanto) {
        this.montoAdelanto = montoAdelanto;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(double saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }
    
    
    
}
