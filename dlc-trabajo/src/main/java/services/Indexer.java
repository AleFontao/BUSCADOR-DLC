package services;

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
        int i=0;
        long startTime = System.currentTimeMillis();
        for (File documento : listaDocumentos) {

            indexar(documento, i);
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
                    //Se saca las palabras de un caracter y los espacios

                    if (palabra.length() > 1 && !palabra.equals(" ")) {
                        if(palabra.length() > 25){
                            this.palabras++;
                            continue;
                        }
                        Vocabulario vocabulario;
                        Posteo posteo = new Posteo();
                        //Si no existe la palabra la agregamos
                        if (!hashVocabulario.containsKey(palabra)) {
                            vocabulario = new Vocabulario();
                            vocabulario.setPalabra(palabra);

                            //Usamos de key la palabra para volver obtener el vocabulario.
                            hashVocabulario.put(palabra, vocabulario);
                            this.palabras++;
                        } else {
                            //Palabras que aparecen mas de una vez
                            vocabulario = hashVocabulario.get(palabra);
                            vocabulario.setTf(vocabulario.getTf() + 1);



                            if(hashPosteo.get(palabra) != null){
                                posteo = hashPosteo.get(palabra);
                                posteo.setTf(hashPosteo.get(palabra).getTf() + 1);
                                hashPosteo.put(palabra,posteo);
                                if(vocabulario.getMaxTf() > hashPosteo.get(palabra).getTf()){
                                    vocabulario.setMaxTf(vocabulario.getTf());
                                }
                            }

                            hashVocabulario.put(palabra, vocabulario);
                        }
                        if(!hashPosteo.containsKey(palabra)){

                            /*vocabulario = new Vocabulario();
                            vocabulario = hashVocabulario.get(palabra);*/
                            indexarPosteo(documento, idDoc, palabra, hashVocabulario.get(palabra).getTf());

                            //Aumentamos en 1 el nr de vocabulario
                            vocabulario = hashVocabulario.get(palabra);
                            vocabulario.setNr(vocabulario.getNr() + 1);
                            hashVocabulario.put(palabra, vocabulario);


                        }
                    }
                }
                linea = info.readLine();

            }
            DAOposteo.insertarPosteo(hashPosteo);
            hashPosteo.clear();

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void indexarPosteo(File documento, Integer idDoc, String palabra, Integer tf)
    {
        Documento doc = new Documento();
        doc.setNombreDocumento(documento.getName());
        Posteo posteo = new Posteo();
        posteo.setIdDocumento(idDoc);
        posteo.setPalabra(palabra);
        posteo.setTf(tf);
        hashPosteo.put(palabra, posteo);

        return;
    }
}
