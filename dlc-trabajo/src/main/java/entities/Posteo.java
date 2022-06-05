package entities;

import javax.print.Doc;
import java.util.ArrayList;

public class Posteo {
    private int idDocumento;
    private int maxtf;
    private String palabra;
    


    public Posteo(int idDocumento, int maxtf, String palabra) {
        this.idDocumento = idDocumento;
        this.maxtf = maxtf;
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

    public int getMaxtf() {
        return maxtf;
    }

    public void setMaxtf(int maxtf) {
        this.maxtf = maxtf;
    }
}
