package io.fronky.homework4.loginweb.users;

import io.fronky.homework4.loginweb.DBConnector;
import org.apache.commons.text.CaseUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService implements DBConnector {

    private static UserService userService;

    private Connection dbConn;

    private UserService(){ }

    public static UserService getInstance(){
        if (userService == null)
        {
            //synchronized block to remove overhead
            synchronized (UserService.class)
            {
                if(userService==null)
                {
                    // if instance is null, initialize
                    userService = new UserService();
                }
            }
        }
        return userService;
    }

    @Override
    public void setDBConn(Connection connection) {
        this.dbConn = connection;
    }

    @Override
    public void init() throws SQLException {
        createTable();
        create("admin", "12345", "Pitipat", "Chairoj");
        create("root", "pass", "root", "groot");
    }

    private void createTable() throws SQLException {
        String sql = "DROP TABLE IF EXISTS users;";
        PreparedStatement prepStmt = dbConn.prepareStatement(sql);
        prepStmt.execute();
        sql = "CREATE TABLE IF NOT EXISTS users(" +
                "username VARCHAR(255) UNIQUE PRIMARY KEY," +
                "password VARCHAR(255)," +
                "first_name VARCHAR(255)," +
                "last_name VARCHAR(255));";
        prepStmt = dbConn.prepareStatement(sql);
        prepStmt.execute();
    }

    public void create(String username, String password, String firstName, String lastName) throws SQLException {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String sql = "INSERT INTO users(username, password, first_name, last_name) " +
                "VALUES (?,?,?,?);";
        PreparedStatement prepStmt = dbConn.prepareStatement(sql);
        prepStmt.setString(1, username);
        prepStmt.setString(2, hashed);
        prepStmt.setString(3, CaseUtils.toCamelCase(firstName, true, null));
        prepStmt.setString(4, CaseUtils.toCamelCase(lastName, true, null));
        prepStmt.execute();
    }

    public User get(String username) throws SQLException{
        String sql = "SELECT * FROM users WHERE username = ?;";
        PreparedStatement preparedStatement = dbConn.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return new User(resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"));
        }
        else{
            return null;
        }
    }

    public void remove(String username) throws SQLException{
        String sql = "DELETE FROM users WHERE username = ?;";
        PreparedStatement preparedStatement = dbConn.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.executeUpdate();
    }

    public void update(String username, String password, String firstName, String lastName) throws SQLException{
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String sql = "UPDATE users SET password=?, first_name=?, last_name=? WHERE username=?;";
        PreparedStatement preparedStatement = dbConn.prepareStatement(sql);
        preparedStatement.setString(1, hashed);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, username);
        preparedStatement.executeUpdate();
    }

    public boolean containsUser(String username) throws SQLException{
        return get(username) != null;
    }

    public List<User> getUsers() throws SQLException{
        String sql = "SELECT * FROM users;";
        PreparedStatement prepStmt = dbConn.prepareStatement(sql);
        ResultSet resultSet = prepStmt.executeQuery();

        List<User> users = new ArrayList<>();
        while(resultSet.next()){
            users.add(new User(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name")
            ));
        }
        return users;
    }

}
