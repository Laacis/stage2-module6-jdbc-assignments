package jdbc;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static final SQLException SQL_EXCEPTION = new SQLException();
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.name = name;
    }

    public static CustomDataSource getInstance() {
        if (instance == null) {
            synchronized (CustomDataSource.class) {
                if (instance == null) {
                    try (InputStream inputStream = CustomDataSource.class.getClassLoader().getResourceAsStream("app.properties")){
                        if (inputStream == null){
                            throw  new RuntimeException("app.properties file not found");
                        }
                        Properties properties = new Properties();
                        properties.load(inputStream);
                        String driver = properties.getProperty("postgres.driver");
                        String url = properties.getProperty("postgres.url");
                        String password = properties.getProperty("postgres.password");
                        String name = properties.getProperty("postgres.name");

                        instance = new CustomDataSource(driver, url, password, name);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        CustomConnector conn = new CustomConnector();
        return conn.getConnection(url, name, password);

    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        CustomConnector conn = new CustomConnector();
        return conn.getConnection(url, username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw  SQL_EXCEPTION;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw  SQL_EXCEPTION;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw  SQL_EXCEPTION;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw  SQL_EXCEPTION;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw  SQL_EXCEPTION;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw  SQL_EXCEPTION;
    }
}
