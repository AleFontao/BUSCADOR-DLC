package entities;

public class Vocabulario {
    private String palabra;
    private Integer nr; //En cuantos documentos se encuentra
    private Integer tf; //Cantidad de apariciones
    private Integer maxTf; //Cantidad maxima de apariciones en todos los doc

    public Vocabulario(String palabra, Integer nr, Integer tf, Integer maxTf) {
        this.palabra = palabra;
        this.nr = nr;
        this.tf = tf;
        this.maxTf = maxTf;
    }

    public Vocabulario() {
        this("", 0, 1, 1);
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public Integer getNr() {
        return nr;
    }

    public void setNr(Integer nr) {
        this.nr = nr;
    }

    public Integer getTf() {
        return tf;
    }

    public void setTf(Integer tf) {
        this.tf = tf;
    }

    public Integer getMaxTf() {return maxTf;}

    public void setMaxTf(Integer maxTf) {this.maxTf = maxTf;}

}
