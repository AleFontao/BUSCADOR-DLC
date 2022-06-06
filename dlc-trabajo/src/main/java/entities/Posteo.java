package entities;

import javax.print.Doc;
import java.util.ArrayList;

public class Posteo {
    private int idDocumento;
    private int tf;
    private String palabra;
    


    public Posteo(int idDocumento, int tf, String palabra) {
        this.idDocumento = idDocumento;
        this.tf = tf; //veces
        this.palabra = palabra;
    }

    public Posteo() {

    }
    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public void setTf(int tf) {
        this.tf = tf;
    }

    public int getTf() {
        return tf;
    }


}
