package org.example.DAO;

import org.example.AbstractIntegrationTest;
import org.example.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.List;

@Testcontainers
class UserDAOTest extends AbstractIntegrationTest {

    public UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        super.setUp();
        userDAO = new UserDAO(sessionFactory);
    }

    @DisplayName("Проверка создания и получения пользователя")
    @Test
    void testCreateAndGetUser() {
        User user = new User("Alice", "alice@example.com", 30, OffsetDateTime.now());
        userDAO.createUser(user);

        User retrieved = userDAO.getUser(user.getId());
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("Alice", retrieved.getName());
    }

    @DisplayName("Проверка обновления юзера")
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

    @DisplayName("Проверка удаления юзера")
    @Test
    void testDeleteUser() {
        User user = new User("Charlie", "charlie@example.com", 25, OffsetDateTime.now());
        userDAO.createUser(user);
        userDAO.deleteUser(user.getId());
        Assertions.assertNull(userDAO.getUser(user.getId()));
    }

    @DisplayName("Проверка получения списка юзеров")
    @Test
    void testGetAllUsers() {
        userDAO.createUser(new User("Tom", "tom@example.com", 22, OffsetDateTime.now()));
        userDAO.createUser(new User("Jerry", "jerry@example.com", 24, OffsetDateTime.now()));

        List<User> users = userDAO.getAllUsers();
        Assertions.assertEquals(2, users.size());
    }
}
