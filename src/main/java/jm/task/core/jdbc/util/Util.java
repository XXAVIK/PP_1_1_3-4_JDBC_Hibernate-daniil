package jm.task.core.jdbc.util;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {


//    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "springcourse";

    public Connection getDbConnection() {
        Connection connection = null;
        try {
//            DriverManager.registerDriver(driver);
//            Class.forName(DB_DRIVER);                убрал драйвер, вроде он не нужен с этой версией?
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("connected");



        } catch (SQLException e) {
            System.out.println("error");
            throw new RuntimeException(e);

        }
        return connection;
    }

}
