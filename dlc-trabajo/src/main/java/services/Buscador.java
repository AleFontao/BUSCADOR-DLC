package services;

import DAO.DAOdocumento;
import DAO.DAOposteo;
import entities.Documento;
import entities.Posteo;
import entities.Vocabulario;

import javax.print.Doc;
import java.util.*;

public class Buscador {
    private HashMap<Integer, Posteo> hashPosteoBuscar;
    private Hashtable<String, Vocabulario> hashVocabulario;
    private ArrayList<Documento> ArrayDocumento;
    public static void main(String args[]) {
        Buscador buscador = new Buscador();
        buscador.buscar();
        buscador.calcularImportancia();
        buscador.filtrarDocumentos();
    }

    public void buscar(){
        String palabraABuscar = "QUIJOTE";
        hashPosteoBuscar = DAOposteo.buscarPalabra(palabraABuscar);
    }

    public void calcularImportancia(){
        ArrayDocumento = new ArrayList<>();
        for (Map.Entry<Integer, Posteo> entry : hashPosteoBuscar.entrySet()) {
            Posteo posteo = entry.getValue();
            float importancia = entry.getValue().calcularImportancia(598, hashPosteoBuscar.size());
            Documento documento = new Documento(posteo.getIdDocumento(), DAOdocumento.obtenerNombreDocumento(posteo.getIdDocumento()), importancia);
            ArrayDocumento.add(documento);
        }
    }

    public void filtrarDocumentos(){
        ArrayDocumento.sort(Comparator.comparing(Documento::getValor).reversed());
        ArrayDocumento.forEach(x -> System.out.println(x.getValor() + " " + x.getNombreDocumento()));
    }
}
