package entities;

import java.util.ArrayList;

public class Posteo {
    private ArrayList<Documento> listaDocumento;
    private int tf;


    public Posteo(ArrayList<Documento> listaDocumento, int tf) {
        this.listaDocumento = listaDocumento;

        this.tf = tf;
    }

    public ArrayList<Documento> getListaDocumento() {
        return listaDocumento;
    }

    public void setListaDocumento(ArrayList<Documento> listaDocumento) {
        this.listaDocumento = listaDocumento;
    }

    public int getTf() {
        return tf;
    }

    public void setTf(int tf) {
        this.tf = tf;
    }
}
