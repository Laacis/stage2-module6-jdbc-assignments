package jdbc;

import java.sql.*;


public class CustomConnector {
    public Connection getConnection(String url) throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return conn;
    }

    public Connection getConnection(String url, String user, String password) throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return conn;
    }
}
