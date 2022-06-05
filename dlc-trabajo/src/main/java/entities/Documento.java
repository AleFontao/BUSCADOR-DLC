package entities;

public class Documento {
    //Existen 593 documentos en el archivo que nos pasaron
    private Integer id;
    private String nombreDocumento;
    private Integer valor;

    public Documento(Integer id, String nombreDocumento, Integer valor) {
        this.id = id;
        this.nombreDocumento = nombreDocumento;
        this.valor = valor;
    }

    public Documento() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }
}

