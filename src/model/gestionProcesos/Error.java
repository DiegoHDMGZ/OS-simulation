package model.gestionProcesos;


public class Error {
    private int codigo;
    private String descripcion;

    public Error(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    
}
