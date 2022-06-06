package DAO;

import entities.Documento;
import entities.Posteo;
import entities.Vocabulario;

import java.sql.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class DAOposteo {
    static Connection con = DBManager.getConnection();

    public static void insertarPosteo(HashMap<String, Posteo> posteo) {

        try {
           
            PreparedStatement ps = con.prepareStatement("INSERT INTO Buscador.Posteo (idDocumento, palabra, tf) VALUES (?,?,?)");

            for (Map.Entry<String, Posteo> entry : posteo.entrySet()) {
                String key = entry.getKey();
                Posteo value = entry.getValue();
                ps.setInt(1,value.getIdDocumento());
                ps.setString(2,value.getPalabra());
                ps.setInt(3,value.getTf());
                ps.addBatch();

            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<Integer, Posteo>  buscarPalabra(String palabraABuscar){
        HashMap<Integer, Posteo> hashPosteo = new HashMap<>();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT id, palabra, idDocumento, tf FROM Buscador.Posteo WHERE palabra = ?");
            ps.setString(1, palabraABuscar);
            ResultSet rs = ps.executeQuery();


            while(rs.next()){
                //int idDocumento, int tf, String palabra
                Posteo posteo = new Posteo(rs.getInt(3),rs.getInt(4),rs.getString(2));
                hashPosteo.put(posteo.getIdDocumento(), posteo);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return hashPosteo;

    }
}
