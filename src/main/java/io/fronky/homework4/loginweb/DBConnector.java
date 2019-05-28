package io.fronky.homework4.loginweb;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBConnector {

    void setDBConn(Connection connection);

    void init() throws SQLException;

}
