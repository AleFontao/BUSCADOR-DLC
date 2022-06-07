package services;

import DAO.DAOdocumento;
import DAO.DAOposteo;
import entities.Documento;
import entities.Posteo;
import entities.Vocabulario;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DAO.DAOvocabulario;

import javax.print.Doc;

public class Indexer {
    Integer palabras = 0;
    private StringTokenizer tokens;
    private Hashtable<String, Vocabulario> hashVocabulario; //Uso una hash table como estructura para ir almacenando las palabras del vocabulario
    private HashMap<String, Posteo> hashPosteo;

    public static void main(String args[]) {
        Indexer indexer = new Indexer();
        indexer.leerArchivos();
    }

    public Indexer() {
        this.hashVocabulario = new Hashtable<>();
        this.hashPosteo = new HashMap<>();
    }

    public void leerArchivos() {
        String url = "src/main/resources/DocumentosTP1/";
        File direccionArchivo = new File(url);
        ArrayList<File> listaDocumentos = new ArrayList<>();

        listarArchivosEnCarpeta(direccionArchivo, listaDocumentos);
        int contadorDocumentos = 1;
        long startTime = System.currentTimeMillis();
        for (File documento : listaDocumentos) {

            indexar(documento, contadorDocumentos);
            //Metemos el doc a la bd
            Documento docParaBD = new Documento();
            docParaBD.setNombreDocumento(documento.getName());
            DAOdocumento.insertarDocumento(docParaBD);
            contadorDocumentos++;


        }
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println(endTime);

        DAOvocabulario.insertarVocabulario(this.hashVocabulario);


    }

    public void listarArchivosEnCarpeta(File carpeta, ArrayList<File> listaDocumentos) {
        for (File archivo : carpeta.listFiles()) {
            if (archivo.isDirectory()) {
                listarArchivosEnCarpeta(archivo, listaDocumentos);
            } else {
                if (archivo.getName().endsWith(".txt")) {
                    listaDocumentos.add(archivo);
                }
                //System.out.println(fileEntry.getName());
            }
        }
        System.out.println("Cantidad Archivos: " + listaDocumentos.size());
    }

    //Habria que ver como hacer para agregar e indexar muchos archivos
    public void agregarArchivo(File documento){
        ArrayList<Documento> arrayDocumentos = buscarDocumentos();
        for(Documento doc: arrayDocumentos){
            if(!doc.getNombreDocumento().equals(documento)){
                indexar(documento, arrayDocumentos.size() + 1);
                new File(documento, "src/main/resources/DocumentosTP1");
            }
        }
    }

    public void indexar(File documento, Integer idDoc) {

        //Separar logica del chorizo(que funciona) este
        try (BufferedReader info = new BufferedReader(new InputStreamReader(new FileInputStream(new File(documento.getPath())), "ISO-8859-1"))) {
            String linea = info.readLine();
            while (linea != null) {
                tokens = new StringTokenizer(linea);

                while (tokens.hasMoreTokens()) {
                    String frase = tokens.nextToken().toUpperCase();
                    String[] palabras;
                    //Creo que esta todo en ingles por lo cual no hace falta las tildes
                    Pattern p = Pattern.compile("[^ÁÉÍÓÚA-Z]");
                    Matcher match = p.matcher(frase);
                    //String[ ] ws = linea.split("[^ÁÉÍÓÚA-Z]");
                    palabras = frase.split(" ");
                    if (match.find()) {
                        frase = match.replaceAll(" ");
                        palabras = frase.split(" ");
                    }

                    for(String palabra: palabras) {


                        if (palabra.length() > 1 && !palabra.equals(" ")) {
                            if (palabra.length() > 25) {
                                this.palabras++;
                                continue;
                            }
                            //Si no existe la palabra la agregamos
                            if (!this.hashVocabulario.containsKey(palabra)) {
                                meterPalabraPorPrimeraVezVoc(palabra);
                                crearPosteo(documento, idDoc, palabra);
                            } else {
                                if (!hashPosteo.containsKey(palabra)) {
                                    crearPosteo(documento, idDoc, palabra);
                                } else {
                                    aumentarPosteo(palabra);
                                }
                                aumentarVocabulario(palabra);
                            }
                        }
                    }
                }
                linea = info.readLine();
            }

            DAOposteo.insertarPosteo(this.hashPosteo);
            this.hashPosteo.clear();

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void aumentarNr(String palabra){
        Vocabulario vocabulario = this.hashVocabulario.get(palabra);
        vocabulario.setNr(vocabulario.getNr() + 1);
        this.hashVocabulario.put(palabra, vocabulario);
    }

    public void meterPalabraPorPrimeraVezVoc(String palabra){
        Vocabulario vocabulario = new Vocabulario();
        vocabulario.setPalabra(palabra);
        //Usamos de key la palabra para volver a obtener el vocabulario.
        this.hashVocabulario.put(palabra, vocabulario);
        this.palabras++;
    }

    public void aumentarVocabulario(String palabra){
        Vocabulario vocabulario = this.hashVocabulario.get(palabra);
        vocabulario.setTf(vocabulario.getTf() + 1);
        aumentarMaxTf(palabra, vocabulario); //Aumentamos el maxTf
        //Falta meter el posteo de vuelta
        this.hashVocabulario.put(palabra, vocabulario);
    }

    public void aumentarMaxTf(String palabra, Vocabulario vocabulario){
        Posteo posteo = this.hashPosteo.get(palabra);
        if (posteo != null) {
            if (vocabulario.getMaxTf() < posteo.getTf()) {
                vocabulario.setMaxTf(posteo.getTf());
            }
        }
        this.hashPosteo.put(palabra, posteo);
    }

    public void aumentarPosteo(String palabra){
        Posteo posteo = this.hashPosteo.get(palabra);
        posteo.setTf(posteo.getTf() + 1);
    }


    public void crearPosteo(File documento, Integer idDoc, String palabra) {
        Documento doc = new Documento();
        doc.setNombreDocumento(documento.getName());
        Posteo posteo = new Posteo();
        posteo.setIdDocumento(idDoc);
        posteo.setPalabra(palabra);
        posteo.setTf(1);
        this.hashPosteo.put(palabra, posteo);
        aumentarNr(palabra);
        return;
    }
    
    public  ArrayList<Documento> buscarDocumentos(){
        ArrayList<Documento> arrayDocumentos = DAOdocumento.obtenerTodosLosDocumentos();
        return arrayDocumentos;
    }
}
