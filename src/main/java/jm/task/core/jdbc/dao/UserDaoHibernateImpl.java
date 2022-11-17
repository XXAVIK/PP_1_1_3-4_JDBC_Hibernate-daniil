package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final static String DROP_TB = "DROP TABLE IF EXISTS users";
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Users" +       //SQL запрос на создание таблицы
            "(" +

            "  id int NOT NULL AUTO_INCREMENT,\n" +
            "  name varchar(15),\n" +
            "  lastname varchar(25),\n" +
            "  age int,\n" +
            "  PRIMARY KEY (id)\n"
            + ");";

    public UserDaoHibernateImpl() {
    }


    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getDbConnectionHibernate().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(CREATE_TABLE).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Error while creating table");
            throw new RuntimeException(e);
        }

    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getDbConnectionHibernate().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(DROP_TB).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Error while dropping table");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setAge(age);
        Transaction transaction = null;
        try (Session session = Util.getDbConnectionHibernate().getCurrentSession()) { //openSession()

            transaction = session.beginTransaction();

            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Error while saving user");
            throw new RuntimeException(e);
        }

    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = Util.getDbConnectionHibernate().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE User WHERE id=" + id).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Error while removing user by Id");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List<User> userList = null;
        try (Session session = Util.getDbConnectionHibernate().openSession()) {
            transaction = session.beginTransaction();
            userList = session.createQuery("from User")
                    .getResultList();
            for (User u : userList) {
                System.out.println(u);
            }
            System.out.println(userList);
            session.getTransaction().commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Error while getting Users");
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getDbConnectionHibernate().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            try (Session session = Util.getDbConnectionHibernate().openSession()) {
                transaction = session.beginTransaction();
                transaction.rollback();
            }
            System.out.println("Error while cleaning table");
            throw new RuntimeException(e);
        }
    }

}
