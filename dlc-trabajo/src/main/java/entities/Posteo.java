package entities;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public float calcularImportancia(Integer cantidadDocs, Integer nr){
        /*float divisor = 0;
        for (Map.Entry<String, Posteo> entry : posteoPalabraBuscada.entrySet()) {
            divisor = (float) Math.pow ((float) entry.getValue().getTf() * Math.log10(cantidadDocs/posteoPalabraBuscada.size()),  2);
        }*/
        float importancia = (float) ((float)this.tf * Math.log10(cantidadDocs/nr));

        return importancia;
    }

}
