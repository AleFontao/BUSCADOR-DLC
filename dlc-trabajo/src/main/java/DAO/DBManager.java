package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/Buscador?user=root&password=123");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return conn;
    }

}
