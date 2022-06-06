package entities;

import java.util.HashMap;
import java.util.Map;

public class Vocabulario {
    private String palabra;
    private Integer nr; //En cuantos documentos se encuentra
    private Integer tf; //Cantidad de apariciones
    private Integer maxTf; //Cantidad maxima de apariciones en todos los doc

    public Vocabulario(String palabra, Integer nr, Integer tf, Integer maxTf) {
        this.palabra = palabra; //palabra eje: quixote
        this.nr = nr; //cantidad de archivos en los que aparece
        this.maxTf = maxTf; //cantidad maxima de veces que aparece en todos los archivos
        this.tf = tf;
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

    public float calcularImportancia(Integer cantidadDocs, HashMap<String, Posteo> posteoPalabraBuscada){
        float divisor = 0;
        for (Map.Entry<String, Posteo> entry : posteoPalabraBuscada.entrySet()) {
            divisor = (float) Math.pow ((float) entry.getValue().getTf() * Math.log10(cantidadDocs/posteoPalabraBuscada.size()),  2);
        }
        float importancia = (float) ((float)this.tf * Math.log10(cantidadDocs/this.nr)/(Math.sqrt(divisor)));

        return importancia;
    }

}
