package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO myusers (firstname, lastname, age) VALUES (?, ?, ?)";
    private static final String updateUserSQL = "UPDATE myusers  SET firstname = ?, lastname = ?, age = ? WHERE id = ?";
    private static final String deleteUser = "DELETE * FROM myusers WHERE id = ?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id = ?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE firstname = ?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";

    public Long createUser(User user) {
        Long id = null;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(createUserSQL);

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            int updatedRows = ps.executeUpdate();

            if (updatedRows > 0){
                ResultSet rs = ps.getResultSet();
                if (rs.next()){
                    id = rs.getLong("id");
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
        }
        return id;
    }

    public User findUserById(Long userId) {
        User user = new User();

        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findUserByIdSQL);
            ps.setLong(1, userId);
            getUserFromResult(user);
        } catch (SQLException e) {
            throw new RuntimeException( e);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
        }

        return user;
    }

    public User findUserByName(String userName) {
        User user = new User();

        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareCall(findUserByNameSQL);

            ps.setString(1,userName);
            getUserFromResult(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
        }

        return user;
    }

    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();

        try {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.createStatement();

            try (ResultSet rs = st.executeQuery(findAllUserSQL)){
                while(rs.next()){
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setFirstName(rs.getString("firstname"));
                    user.setLastName(rs.getString("lastname"));
                    user.setAge(rs.getInt("age"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement(st);
            closeConnection(connection);
        }

        return users;
    }

    public User updateUser(User user) {
        Long userId = user.getId();

        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(updateUserSQL);

            ps.setString(1,user.getFirstName());
            ps.setString(2,user.getLastName());
            ps.setInt(3,user.getAge());
            ps.setLong(4,userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            closeStatement(ps);
            closeConnection(connection);
        }

        return findUserById(userId);
    }

    public void deleteUser(Long userId) {
        try{
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(deleteUser);
            ps.setLong(1, userId);
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
        }
    }

    private void closeConnection(Connection connection) {
        try{
            if (connection != null) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeStatement(Statement ps) {
        try{
            if (ps != null) ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getUserFromResult(User user) throws SQLException {
        try(ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setAge(rs.getInt("age"));
            }
        }
    }
}
