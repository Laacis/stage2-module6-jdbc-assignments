package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
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
            Properties properties = new Properties();
            try (FileReader fileReader = new FileReader("src/main/resources/app.properties")) {
                properties.load(fileReader);
                String driver = properties.getProperty("postgres.driver");
                String url = properties.getProperty("postgres.url");
                String password = properties.getProperty("postgres.password");
                String name = properties.getProperty("postgres.name");
                instance = new CustomDataSource(driver, url, password, name);
            } catch (IOException e) {
                throw new RuntimeException("Unable to load properties from app.properties file", e);
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new CustomConnector().getConnection(url, name, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new CustomConnector().getConnection(url, username, password);
    }

    @Override
    public PrintWriter getLogWriter() {
        throw new UnsupportedOperationException("Unsupported method 'getLogWriter()'");
    }

    @Override
    public void setLogWriter(PrintWriter out) {
        throw new UnsupportedOperationException("Unsupported method 'setLogWriter()'");
    }

    @Override
    public void setLoginTimeout(int seconds) {
        throw new UnsupportedOperationException("Unsupported method 'setLoginTimeout()'");
    }

    @Override
    public int getLoginTimeout() {
        throw new UnsupportedOperationException("Unsupported method 'getLoginTimeout()'");
    }

    @Override
    public Logger getParentLogger() {
        throw new UnsupportedOperationException("Unsupported method 'getParentLogger()'");
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        throw new UnsupportedOperationException("Unsupported method 'unwrap()'");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        throw new UnsupportedOperationException("Unsupported method 'isWrapperFor()'");
    }
}


