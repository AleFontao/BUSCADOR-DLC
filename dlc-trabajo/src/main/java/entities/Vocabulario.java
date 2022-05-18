package entities;

public class Vocabulario {
    private Integer id;
    private Integer nr; //En cuantos documentos se encuentra
    private Integer tf; //Cantidad de apariciones

    public Vocabulario(Integer id, Integer nr, Integer tf) {
        this.id = id;
        this.nr = nr;
        this.tf = tf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
