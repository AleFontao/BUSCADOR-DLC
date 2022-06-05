package DAO;


import entities.Vocabulario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;

public class DAOvocabulario {

    static Connection con = DBManager.getConnection();
    public static void insertarVocabulario(Hashtable<String, Vocabulario> vocabulario){

        long startTime = System.currentTimeMillis();
        try {
            Statement stmt = con.createStatement();
            PreparedStatement ps = con.prepareStatement("INSERT INTO Vocabulario (palabra, tf, nr) VALUES (?,?,?)");
            int i = 0;
            Enumeration<Vocabulario> vocHash = vocabulario.elements();
            while(vocHash.hasMoreElements()){
                Vocabulario palabra = vocHash.nextElement();
                ps.setString(1, palabra.getPalabra());
                ps.setInt(2,palabra.getTf());
                ps.setInt(3,palabra.getNr());
                ps.addBatch();



                if(ps.getFetchSize() > 30000){//Metemos de a lotes de 30000 filas a la bd
                    System.out.println("Lote");
                    ps.executeBatch();
                }
                //Todo esto me tarda aprox 40 seg en meter todo el vocabulario: 700.000 rows aprox
            }
            ps.executeBatch();
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println(endTime);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
