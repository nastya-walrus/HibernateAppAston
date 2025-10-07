package org.example.DAO;

import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;


public class UserDAO {

    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void createUser(User person) {
        try (Session session = sessionFactory.getCurrentSession()) {

            Transaction transaction = session.beginTransaction();

            try {
                session.persist(person);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) transaction.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    public User getUser(int id) {
        try (Session session = sessionFactory.getCurrentSession()) {

            Transaction transaction = session.beginTransaction();

            try {
                User resultUser = session.find(User.class, id);
                transaction.commit();
                return resultUser;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) transaction.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    public List<User> getAllUsers() {
        try (Session session = sessionFactory.getCurrentSession()) {

            Transaction transaction = session.beginTransaction();

            try {
                List<User> resultList = session.createQuery("from User p", User.class)
                        .getResultList();
                transaction.commit();
                return resultList;
            }
            catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException(e);
            }
        }
    }


    public boolean updateUser(int id, User updatedUser) {
        try (Session session = sessionFactory.getCurrentSession()) {

            Transaction transaction = session.beginTransaction();

            try {
                User userToBeUpdated = session.find(User.class, id);
                if (userToBeUpdated == null) {
                    System.out.println("Пользователь с таким Id не найден");
                    return false;
                }
                userToBeUpdated.setName(updatedUser.getName());
                userToBeUpdated.setEmail(updatedUser.getEmail());
                userToBeUpdated.setAge(updatedUser.getAge());
                userToBeUpdated.setCreatedAt(updatedUser.getCreatedAt());

                transaction.commit();
                return true;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteUser(int id) {
        try (Session session = sessionFactory.getCurrentSession()) {

            Transaction transaction = session.beginTransaction();

            try {
                User user = session.find(User.class, id);
                if (user != null) {
                    session.remove(user);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) transaction.rollback();
                throw e;
            }
        }
    }
}