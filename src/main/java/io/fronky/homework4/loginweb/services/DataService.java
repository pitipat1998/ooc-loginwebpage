package io.fronky.homework4.loginweb.services;

import io.fronky.homework4.loginweb.DBConnector;
import io.fronky.homework4.loginweb.Routable;
import io.fronky.homework4.loginweb.users.UserService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataService {

    private String connectionString;
    private String dbName;
    private Properties connectionProperties;
    private Connection dbConn;

    private UserService userService;
    private SecurityService securityService;

    private static final List<Class<? extends DBConnector>> connectors = new ArrayList<>();

    static {
        connectors.add(UserService.class);
    }

    public DataService(String connectionString, String dbName, String dbDriver, Properties connectionProperties) throws Exception{
        this.dbName = dbName;
        Connection dbConn = DriverManager.getConnection(connectionString, connectionProperties);
        createDB(dbConn);
        dbConn.close();

        this.connectionString = connectionString;
        this.dbConn = DriverManager.getConnection(connectionString+dbName, connectionProperties);
        this.connectionProperties = connectionProperties;
        this.securityService = SecurityService.getInstance();
    }

    private void createDB(Connection dbConn) throws SQLException {
        String sql = "CREATE DATABASE IF NOT EXISTS " + dbName +";";
        PreparedStatement prepStmt = dbConn.prepareStatement(sql);
        prepStmt.execute();
    }

    public void init() throws SQLException{
        for (Class<? extends DBConnector> connectorClass : connectors) {
            try {
                Method method = connectorClass.getMethod("getInstance", null);
                DBConnector connector = (DBConnector) method.invoke(null, null);
                connector.setDBConn(dbConn);
                connector.init();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e){
                e.printStackTrace();
            } catch (InvocationTargetException e){
                e.printStackTrace();
            }
        }
    }
}
