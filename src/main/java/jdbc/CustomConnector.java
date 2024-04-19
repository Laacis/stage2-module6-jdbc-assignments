package jdbc;

import java.sql.*;


public class CustomConnector {
    public Connection getConnection(String url) throws SQLException {
        try {
            return DriverManager.getConnection(url);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection(String url, String user, String password) throws SQLException {
        try {
            return DriverManager.getConnection(url, user, password);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
