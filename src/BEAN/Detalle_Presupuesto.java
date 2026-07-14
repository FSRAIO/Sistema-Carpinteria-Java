
package BEAN;

public class Detalle_Presupuesto {
    private int idDetalle;
    private int idProyecto;
    private int idMaterial;
    private double cantidad;
    private double precioMomentaneo;
    private double subtotal;

    public Detalle_Presupuesto() {
    }

    public Detalle_Presupuesto(int idDetalle, int idProyecto, int idMaterial, double cantidad, double precioMomentaneo, double subtotal) {
        this.idDetalle = idDetalle;
        this.idProyecto = idProyecto;
        this.idMaterial = idMaterial;
        this.cantidad = cantidad;
        this.precioMomentaneo = precioMomentaneo;
        this.subtotal = subtotal;
    }
    

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioMomentaneo() {
        return precioMomentaneo;
    }

    public void setPrecioMomentaneo(double precioMomentaneo) {
        this.precioMomentaneo = precioMomentaneo;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    
    
    
}
