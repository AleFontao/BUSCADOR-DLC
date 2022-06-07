package DAO;

import entities.Documento;
import entities.Posteo;
import entities.Vocabulario;

import javax.print.Doc;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class DAOdocumento {
    static Connection con = DBManager.getConnection();

    public static void insertarDocumento(Documento doc) {
        try {
            Statement stmt = con.createStatement();
            PreparedStatement ps = con.prepareStatement("INSERT INTO Buscador.Documento (nombreDocumento) VALUES (?)");

            ps.setString(1, doc.getNombreDocumento());
            ps.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //Podriamos traer todos los doc y cargarlos en una hash a la hora de cargar el servidor :´)
    public static String obtenerNombreDocumento(Integer idDocumento){
        String nombreDocumento = "";
        try{
            PreparedStatement ps = con.prepareStatement("SELECT nombreDocumento FROM Buscador.Documento WHERE idDocumento = ?");
            ps.setInt(1, idDocumento);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                nombreDocumento = rs.getString(1);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return nombreDocumento;
    }

    public static ArrayList<Documento> obtenerTodosLosDocumentos(){
        ArrayList<Documento> arrayDocumentos = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT idDocumento, nombreDocumento FROM Buscador.Documento ORDER BY idDocumento ASC");

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Documento documento = new Documento();
                documento.setId(rs.getInt(1));
                documento.setNombreDocumento(rs.getString(2));
                arrayDocumentos.add(documento);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return arrayDocumentos;
    }
}
