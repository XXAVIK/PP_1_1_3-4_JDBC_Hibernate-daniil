package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection = null;
    private final static String CREATE_DB = "CREATE DATABASE IF NOT EXISTS my_db";        //SQL запрос на создание database
    private final static String USE_DB = "USE my_db";                                     //SQL запрос на использование database
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Users" +       //SQL запрос на создание таблицы
            "(" +

            "  id int NOT NULL AUTO_INCREMENT,\n" +
            "  name varchar(15),\n" +
            "  lastname varchar(25),\n" +
            "  age int,\n" +
            "  PRIMARY KEY (id)\n"
            + ");";
    private final static String DROP_TB = "DROP TABLE IF EXISTS users";                    //SQL запрос на удаление таблицы

    private final static String SAVE_USER = "INSERT INTO users (name, lastName, age) VALUES(?,?,?)"; //Сохранение

    private final static String CLEAN_TABLE = "DELETE FROM users";                         //Очистка таблицы

    private final static String GET_USERS = "SELECT id, name, lastname, age from users";   // Получение всех данных о USER

    public UserDaoJDBCImpl() {
        connection = Util.getDbConnection();     // Получаем соединение с БД
    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(USE_DB);
            statement.execute(CREATE_DB);
            statement.execute(USE_DB);
            statement.execute(CREATE_TABLE);

            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error creating Database");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_DB);
            statement.execute(USE_DB);
            statement.execute(DROP_TB);

            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error while dropping table");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER);
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_DB);
            statement.execute(USE_DB);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

            connection.commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            System.out.println("Error while saving");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id=" + id)) {
            preparedStatement.executeUpdate();

            connection.commit();
            System.out.println("User удален");
        } catch (SQLException e) {
            System.out.println("Error while removing");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        User user = new User();
        List<User> userList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_USERS);
            while (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
                System.out.println(user);
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error while getting list");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CLEAN_TABLE)) {
            preparedStatement.executeUpdate();

            connection.commit();
            System.out.println("Таблица очищена");
        } catch (SQLException e) {
            System.out.println("Error while cleaning");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
