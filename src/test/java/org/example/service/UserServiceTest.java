package org.example.service;

import org.example.DAO.UserDAO;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserDAO userDAO;
    private BufferedReader reader;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userDAO = mock(UserDAO.class);
        reader = mock(BufferedReader.class);
        userService = new UserService(userDAO, reader);
    }

    @Test
    public void testCreateUser() throws IOException {
        Mockito.when(reader.readLine()).thenReturn("Максим", "maxim@mail.ru", "30");

        userService.createUser();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDAO).createUser(captor.capture());

        User created = captor.getValue();
        assertEquals("Максим", created.getName());
        assertEquals("maxim@mail.ru", created.getEmail());
        assertEquals(30, created.getAge());
        assertNotNull(created.getCreatedAt());
    }

    @Test
    public void testFindUserFound() throws IOException {
        when(reader.readLine()).thenReturn("1");
        User user = new User("Денис", "denis@mail.ru", 29, OffsetDateTime.now());
        when(userDAO.getUser(1)).thenReturn(user);

        userService.findUser();

        verify(userDAO).getUser(1);
    }

    @Test

    public void testFindUserNotFound() throws IOException {
        when(reader.readLine()).thenReturn("999");
        when(userDAO.getUser(999)).thenReturn(null);

        userService.findUser();

        verify(userDAO).getUser(999);
    }

    @Test
    public void testListUsers() {
        List<User> mockUsers = List.of(
                new User("Антон", "anton@mail.ru", 22, OffsetDateTime.now()),
                new User("Олег", "oleg@mail.ru", 24, OffsetDateTime.now())
        );
        when(userDAO.getAllUsers()).thenReturn(mockUsers);

        userService.listUsers();

        verify(userDAO).getAllUsers();
    }

    @Test
    public void testUpdateUser() throws IOException {
        when(reader.readLine()).thenReturn("1", "NewName", "newname@mail.ru", "35");
        when(userDAO.updateUser(eq(1), any(User.class))).thenReturn(true);

        userService.updateUser();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDAO).updateUser(eq(1), captor.capture());

        User updated = captor.getValue();
        assertEquals("NewName", updated.getName());
        assertEquals("newname@mail.ru", updated.getEmail());
        assertEquals(35, updated.getAge());
    }

    @Test
    public void testDeleteUser() throws IOException {
        when(reader.readLine()).thenReturn("2");

        userService.deleteUser();

        verify(userDAO).deleteUser(2);
    }

    @Test
    public void testWelcomeMessageExit() throws IOException {
        when(reader.readLine()).thenReturn("6");

        userService.welcomeMessage();
    }

    @Test
    public void testWelcomeMessageUnknownCommand() throws IOException {
        when(reader.readLine()).thenReturn("999");

        userService.welcomeMessage();

        verifyNoInteractions(userDAO);
    }
}
