package jm.task.core.jdbc.util;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;

import java.sql.*;

public class Util {

    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "springcourse";

    public static Connection getDbConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            connection.setAutoCommit(false);
            System.out.println("connected");
        } catch (SQLException e) {
            System.out.println("error");
            throw new RuntimeException(e);
        }
        return connection;
    }

}
