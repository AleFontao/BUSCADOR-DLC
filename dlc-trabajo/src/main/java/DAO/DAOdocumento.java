package DAO;

import entities.Documento;
import entities.Vocabulario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

public class DAOdocumento {
    static Connection con = DBManager.getConnection();

    public static void insertarDocumento(Documento doc) {
        try {
            Statement stmt = con.createStatement();
            PreparedStatement ps = con.prepareStatement("INSERT INTO Buscador.Documento (tf) VALUES (?,?)");

            ps.setString(1, doc.getNombreDocumento());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
