package services;

import DAO.DAOdocumento;
import DAO.DAOposteo;
import entities.Documento;
import entities.Posteo;
import entities.Vocabulario;

import javax.print.Doc;
import javax.swing.text.StyledEditorKit;
import java.util.*;

public class Buscador {
    private HashMap<Integer, Posteo> hashPosteoBuscar;
    private Hashtable<String, Vocabulario> hashVocabulario;
    private ArrayList<Documento> ArrayDocumento = new ArrayList<>();
    public static void main(String args[]) {
        Buscador buscador = new Buscador();
        Scanner sc=new Scanner(System.in);
        System.out.println("Ingrese la/s palabra/s a buscar: ");
        String fraseABuscar = sc.nextLine();
        fraseABuscar.toUpperCase();
        String[] palabras = fraseABuscar.split(" ");

        for(String palabra: palabras) {
             buscador.buscar(palabra);
             buscador.calcularImportancia();
        }
        buscador.filtrarDocumentos();
    }

    public void buscar(String palabra){

        hashPosteoBuscar = DAOposteo.buscarPalabra(palabra);
    }

    public void calcularImportancia(){

        for (Map.Entry<Integer, Posteo> entry : hashPosteoBuscar.entrySet()) {
            Posteo posteo = entry.getValue();
            //Reemplazar el 598!
            float importancia = entry.getValue().calcularImportancia(598, hashPosteoBuscar.size());
            Documento documento = new Documento(posteo.getIdDocumento(), DAOdocumento.obtenerNombreDocumento(posteo.getIdDocumento()), importancia);
            if(ArrayDocumento.size() == 0) {
                ArrayDocumento.add(documento);
            }
            else{
                Boolean bandera = false;
                for(Documento documentoExistente: ArrayDocumento){
                    if(documentoExistente.getNombreDocumento().equals(documento.getNombreDocumento())){
                        documentoExistente.setValor(documentoExistente.getValor() + documento.getValor());
                        bandera=true;
                    }
                }
                if (!bandera) {
                    ArrayDocumento.add(documento);
                }
            }
        }
    }

    public void filtrarDocumentos(){
        ArrayDocumento.sort(Comparator.comparing(Documento::getValor).reversed());
        ArrayDocumento.forEach(x -> System.out.println(x.getValor() + " " + x.getNombreDocumento()));
    }
}
