package BEAN;
public class Materiales_Ref {
    private int idMaterial;
    private String nombre;
    private String unidadMedidad;
    private double PrecioCosto;
    private String categoria;

    public Materiales_Ref() {
    }

    public Materiales_Ref(int idMaterial, String nombre, String unidadMedidad, double PrecioCosto, String categoria) {
        this.idMaterial = idMaterial;
        this.nombre = nombre;
        this.unidadMedidad = unidadMedidad;
        this.PrecioCosto = PrecioCosto;
        this.categoria = categoria;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidadMedidad() {
        return unidadMedidad;
    }

    public void setUnidadMedidad(String unidadMedidad) {
        this.unidadMedidad = unidadMedidad;
    }

    public double getPrecioCosto() {
        return PrecioCosto;
    }

    public void setPrecioCosto(double PrecioCosto) {
        this.PrecioCosto = PrecioCosto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    @Override
    public String toString() {
        return nombre; 
    }
    
    
}
