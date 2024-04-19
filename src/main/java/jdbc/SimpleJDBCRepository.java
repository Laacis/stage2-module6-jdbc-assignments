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

    private static final String createUserSQL = "INSERT INTO myusers DEFAULT VALUES";
    private static final String updateUserSQL = "UPDATE myusers  SET firstname = ?, lastname = ?, age = ? WHERE id = ?";
    private static final String deleteUser = "DELETE * FROM myusers WHERE id = ?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id = ?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE firstname = ?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";

    public Long createUser() {
        Long id = null;
        try {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.prepareCall(createUserSQL);

            int updatedRows = ps.executeUpdate();
            if (updatedRows > 0){
                ResultSet rs = ps.getResultSet();
                if (rs.next()){
                    id = rs.getLong("id");
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Something went wrong while working with db:", e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e){
                throw new RuntimeException("Something went wrong while closing connection or statement:", e);
            }
        }
        return id;
    }

    public User findUserById(Long userId) {
        User user = new User();
        ResultSet rs = null;

        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareCall(findUserByIdSQL);
            ps.setLong(1, userId);

            rs = ps.executeQuery();
            while (rs.next()) {
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setAge(rs.getInt("age"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Something went wrong while working with db:", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something went wrong while closing connection, statement or ResultSet:", e);
            }
        }

        return user;
    }

    public User findUserByName(String userName) {
        User user = new User();
        ResultSet rs = null;

        try {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.prepareCall(findUserByNameSQL);

            ps.setString(1,userName);
            rs = ps.executeQuery();

            while(rs.next()){
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setAge(rs.getInt("age"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Something went wrong while working with db:", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something went wrong while closing connection, statement or ResultSet:", e);
            }
        }

        return user;
    }

    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();
        ResultSet rs = null;

        try {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.prepareCall(findAllUserSQL);

            rs = ps.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setAge(rs.getInt("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Something went wrong while working with db:", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something went wrong while closing connection, statement or ResultSet:", e);
            }
        }

        return users;
    }

    public User updateUser(User user) {
        Long userId = user.getId();

        try {
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.prepareCall(updateUserSQL);

            ps.setString(1,user.getFirstName());
            ps.setString(2,user.getLastName());
            ps.setInt(3,user.getAge());
            ps.setLong(4,userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Something went wrong while working with db:", e);
        }finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something went wrong while closing connection or statement:", e);
            }
        }

        return findUserById(userId);
    }

    private void deleteUser(Long userId) {
        try{
            connection = CustomDataSource.getInstance().getConnection();
            st = connection.prepareCall(deleteUser);
            ps.setLong(1, userId);
            ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException("Something went wrong while working with db:", e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Something went wrong while closing connection or statement:", e);
            }
        }
    }
}
