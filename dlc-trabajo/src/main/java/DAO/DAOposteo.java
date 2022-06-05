package DAO;

import entities.Documento;
import entities.Posteo;
import entities.Vocabulario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class DAOposteo {
    static Connection con = DBManager.getConnection();

    public static void insertarPosteo(HashMap<String, Posteo> posteo) {

        try {
           
            PreparedStatement ps = con.prepareStatement("INSERT INTO Buscador.Posteo (idDocumento, palabra) VALUES (?,?)");

            for (Map.Entry<String, Posteo> entry : posteo.entrySet()) {
                String key = entry.getKey();
                Posteo value = entry.getValue();
                ps.setInt(1,value.getIdDocumento());
                ps.setString(2,value.getPalabra());
                ps.addBatch();

            }


            System.out.println("lote");
            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
