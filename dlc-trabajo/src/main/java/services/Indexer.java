package services;

import DAO.DAOdocumento;
import DAO.DAOposteo;
import entities.Documento;
import entities.Posteo;
import entities.Vocabulario;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DAO.DAOvocabulario;

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
        hashVocabulario = new Hashtable<>();
        hashPosteo = new HashMap<>();
    }

    public void leerArchivos() {
        String url = "C:\\Users\\alefo\\Downloads\\DocumentosTP1\\";
        File direccionArchivo = new File(url);
        ArrayList<File> listaDocumentos = new ArrayList<>();

        listarArchivosEnCarpeta(direccionArchivo, listaDocumentos);
        int i = 0;
        long startTime = System.currentTimeMillis();
        for (File documento : listaDocumentos) {

            indexar(documento, i);
            //Metemos el doc a la bd
            Documento docParaBD = new Documento();
            docParaBD.setNombreDocumento(documento.getName());
            DAOdocumento.insertarDocumento(docParaBD);
            i++;

        }
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println(endTime);

        DAOvocabulario.insertarVocabulario(hashVocabulario);


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

    public void indexar(File documento, Integer idDoc) {
        //Separar logica del chorizo(que funciona) este
        try (BufferedReader info = new BufferedReader(new InputStreamReader(new FileInputStream(new File(documento.getPath())), "ISO-8859-1"))) {
            String linea = info.readLine();
            while (linea != null) {
                tokens = new StringTokenizer(linea);

                while (tokens.hasMoreTokens()) {
                    String palabra = tokens.nextToken().toUpperCase();

                    //Creo que esta todo en ingles por lo cual no hace falta las tildes
                    Pattern p = Pattern.compile("[^ÁÉÍÓÚA-Z]");
                    Matcher match = p.matcher(palabra);
                    //String[ ] ws = linea.split("[^ÁÉÍÓÚA-Z]");
                    if (match.find()) {
                        palabra = match.replaceAll("");
                    }

                    if (palabra.length() > 1 && !palabra.equals(" ")) {
                        if (palabra.length() > 25) {
                            this.palabras++;
                            continue;
                        }
                        //Si no existe la palabra la agregamos
                        if (!hashVocabulario.containsKey(palabra)) {
                           meterPalabraPorPrimeraVezVoc(palabra);
                           crearPosteo(documento, idDoc, palabra, hashVocabulario.get(palabra).getTf());
                        } else {
                            if (!hashPosteo.containsKey(palabra)) {
                                crearPosteo(documento, idDoc, palabra, hashVocabulario.get(palabra).getTf());
                            }
                            else{
                               aumentarPosteo(palabra);
                            }
                            aumentarVocabulario(palabra);
                        }
                    }
                }
                linea = info.readLine();
            }
            System.out.println("-------------------");
            DAOposteo.insertarPosteo(hashPosteo);
            hashPosteo.clear();

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void aumentarNr(String palabra){
        Vocabulario vocabulario = hashVocabulario.get(palabra);
        vocabulario.setNr(vocabulario.getNr() + 1);
        hashVocabulario.put(palabra, vocabulario);
    }

    public void meterPalabraPorPrimeraVezVoc(String palabra){
        Vocabulario vocabulario = new Vocabulario();
        vocabulario.setPalabra(palabra);
        //Usamos de key la palabra para volver a obtener el vocabulario.
        hashVocabulario.put(palabra, vocabulario);
        this.palabras++;
    }

    public void aumentarVocabulario(String palabra){
        Vocabulario vocabulario = hashVocabulario.get(palabra);
        vocabulario.setTf(vocabulario.getTf() + 1);
        aumentarMaxTf(palabra, vocabulario); //Aumentamos el maxTf
        //Falta meter el posteo de vuelta
        hashVocabulario.put(palabra, vocabulario);
    }

    public void aumentarMaxTf(String palabra, Vocabulario vocabulario){
        Posteo posteo = hashPosteo.get(palabra);
        if (posteo != null) {
            if (vocabulario.getMaxTf() < posteo.getTf()) {
                vocabulario.setMaxTf(posteo.getTf());
            }
        }
        hashPosteo.put(palabra, posteo);
    }

    public void aumentarPosteo(String palabra){
        Posteo posteo = hashPosteo.get(palabra);
        posteo.setTf(posteo.getTf() + 1);
    }


    public void crearPosteo(File documento, Integer idDoc, String palabra, Integer tf) {

        Documento doc = new Documento();
        doc.setNombreDocumento(documento.getName());
        Posteo posteo = new Posteo();
        posteo.setIdDocumento(idDoc);
        posteo.setPalabra(palabra);
        posteo.setTf(tf);
        hashPosteo.put(palabra, posteo);
        aumentarNr(palabra);

        return;
    }
}
