package org.example.DAO;

import org.example.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.OffsetDateTime;
import java.util.List;

@Testcontainers
class UserDAOTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    private SessionFactory sessionFactory;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        sessionFactory = createSessionFactory();
        userDAO = new UserDAO(sessionFactory);
    }

    private SessionFactory createSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        configuration.setProperty("hibernate.connection.url", POSTGRES.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", POSTGRES.getUsername());
        configuration.setProperty("hibernate.connection.password", POSTGRES.getPassword());

        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");

        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.addAnnotatedClass(User.class);

        return configuration.buildSessionFactory();
    }

    @AfterEach
    public void tearDown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    @Test
    void testCreateAndGetUser() {
        User user = new User("Alice", "alice@example.com", 30, OffsetDateTime.now());
        userDAO.createUser(user);

        User retrieved = userDAO.getUser(user.getId());
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("Alice", retrieved.getName());
    }

    @Test
    void testUpdateUser() {
        User user = new User("Bob", "bob@example.com", 40, OffsetDateTime.now());
        userDAO.createUser(user);

        user.setName("Bobby");
        user.setEmail("bobby@example.com");
        user.setAge(41);
        boolean updated = userDAO.updateUser(user.getId(), user);

        Assertions.assertTrue(updated);
        User result = userDAO.getUser(user.getId());
        Assertions.assertEquals("Bobby", result.getName());
    }

    @Test
    void testDeleteUser() {
        User user = new User("Charlie", "charlie@example.com", 25, OffsetDateTime.now());
        userDAO.createUser(user);
        userDAO.deleteUser(user.getId());
        Assertions.assertNull(userDAO.getUser(user.getId()));
    }

    @Test
    void testGetAllUsers() {
        userDAO.createUser(new User("Tom", "tom@example.com", 22, OffsetDateTime.now()));
        userDAO.createUser(new User("Jerry", "jerry@example.com", 24, OffsetDateTime.now()));

        List<User> users = userDAO.getAllUsers();
        Assertions.assertEquals(2, users.size());
    }
}
