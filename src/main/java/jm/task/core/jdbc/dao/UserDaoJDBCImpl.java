package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    Util util = new Util();
    User user = new User();
    Connection connection = util.getDbConnection();
//test git
    private static Long idCounter = 0L;
    private final static String CREATE_DB = "CREATE DATABASE IF NOT EXISTS my_db";
    private final static String USE_DB = "USE my_db";
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Users (\n" +
            "  id int,\n" +
            "  name varchar(15),\n" +
            "  lastname varchar(25),\n" +
            "  age int\n" +

            ");";
    private final static String DROP_TB = "DROP TABLE IF EXISTS users";

    private final static String SAVE_USER = "INSERT INTO users (id, name, lastName, age) VALUES(?,?,?,?)";

    private final static String REMOVE_USER = "DELETE FROM users WHERE id=?";

    private final static String CLEAN_TABLE = "DELETE FROM users";

    private final static String GET_USERS = "SELECT id, name, lastname, age from users";
    private final static String GET_USER = "Select name, lastname, age from users where id = ?";
    private List<User> userList = new ArrayList<>();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_DB); //если не создавать датабазу, вылетит ошибка в тестах, т.к таблицу некуда поместить
            statement.execute(USE_DB);    //пробовал запихнуть все эти три команды в одну,
            // но у меня никак не запускается код так, либо ошибка синтаксиса пишет,либо работает неправильно
            statement.execute(CREATE_TABLE);
        } catch (SQLException e) {
            System.out.println("Error creating db");
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_DB); //аналогично комм выше
            statement.execute(USE_DB);
            statement.execute(DROP_TB);
        } catch (SQLException e) {
            System.out.println("Error while dropping");
            throw new RuntimeException(e);
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        User user1 = new User();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER)) {
            preparedStatement.setLong(1,++idCounter);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3, lastName);
            preparedStatement.setByte(4, age);

            preparedStatement.executeUpdate();
            System.out.println("User с именем – "+name+" добавлен в базу данных, id="+ idCounter);
            //решил тут немного от себя написать
            // , надеюсьь ничего страшного)
        } catch (SQLException e) {
            System.out.println("Error while saving");
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        User user1 = new User();
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_USER);
             PreparedStatement preparedStatement1 = connection.prepareStatement(GET_USER)) {
            preparedStatement.setLong(1,id);
            preparedStatement1.setLong(1,id);

            ResultSet resultSet = preparedStatement1.executeQuery();
            resultSet.next();




            user.setId(id);
            user.setName(resultSet.getString("name"));
            user.setLastName(resultSet.getString("lastname"));
            user.setAge(resultSet.getByte("age"));
            preparedStatement.executeUpdate();

            System.out.println("User с именем – "+user.getName()+" ,фамилией "+user.getLastName()+ " - удален, id="+user.getId());
            //решил тут немного от себя написать, а то не очень понятно тесты норм или нет гоняются
            // , надеюсьь ничего страшного)
        } catch (SQLException e) {
            System.out.println("Error while removing");
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_USERS);

            while (resultSet.next()){
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
                System.out.println(user);
            }
        } catch (SQLException e) {
            System.out.println("Error while getting list");
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CLEAN_TABLE)) {
            preparedStatement.executeUpdate();
            System.out.println("Таблица очищена");
        } catch (SQLException e) {
            System.out.println("Error while cleaning");
            throw new RuntimeException(e);
        }
        idCounter =0L;
    }
}
