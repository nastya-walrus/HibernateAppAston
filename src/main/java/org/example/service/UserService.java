package org.example.service;

import org.example.DAO.UserDAO;
import org.example.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;


public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;
    private final BufferedReader reader;
    private boolean isExitCommand = false;

    public UserService(UserDAO userDAO, BufferedReader reader) {
        this.userDAO = userDAO;
        this.reader = reader;
    }

    public void run() throws IOException {
        LOGGER.info("UserService started");
        while (!isExitCommand) {
            welcomeMessage();
        }
        LOGGER.info("UserService stopped");
    }

    public void welcomeMessage() throws IOException {

        LOGGER.info("Printed commands list");

        System.out.println("Ожидаю команды. Напишите число (1-5). " +
                "\n[1]: Создать пользователя; " +
                "\n[2]: Найти пользователя; " +
                "\n[3]: Найти список пользователей; " +
                "\n[4]: Обновить информацию о пользователе; " +
                "\n[5]: Удалить пользователя" +
                "\n[6]: Выход");

        switch (reader.readLine()) {
            case "1" -> createUser();
            case "2" -> findUser();
            case "3" -> listUsers();
            case "4" -> updateUser();
            case "5" -> deleteUser();
            case "6" -> isExitCommand = true;
            default -> System.out.println("Неизвестная команда");
        }
    }

    public void createUser() throws IOException {

        LOGGER.info("User chose create user");

        System.out.print("Имя: ");
        String name = reader.readLine();
        System.out.print("Email: ");
        String email = reader.readLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(reader.readLine());

        User createdUser = new User(name, email, age, OffsetDateTime.now());
        userDAO.createUser(createdUser);
        System.out.println("Создан пользователь: " + createdUser);

        LOGGER.info("User {} created", createdUser);
    }

    public void findUser() throws IOException {

        LOGGER.info("User chose find user");

        System.out.print("Id пользователя: ");
        int id = Integer.parseInt(reader.readLine());
        User foundUser = userDAO.getUser(id);
        System.out.println(foundUser == null ? "Не найден" : foundUser);

        LOGGER.info("User {} found", foundUser);
    }

    public void listUsers() {
        LOGGER.info("User chose List users");
        List<User> allUsers = userDAO.getAllUsers();
        allUsers.forEach(System.out::println);
        LOGGER.info("Users {} listed", allUsers);
    }

    public void updateUser() throws IOException {

        LOGGER.info("User chose update user");

        System.out.print("Id пользователя для обновления: ");
        int userId = Integer.parseInt(reader.readLine());

        System.out.print("Новое имя: ");
        String newName = reader.readLine();
        System.out.print("Новый email: ");
        String newEmail = reader.readLine();
        System.out.print("Новый возраст: ");
        int newAge = Integer.parseInt(reader.readLine());

        User updatedUser = new User(newName, newEmail, newAge, OffsetDateTime.now());
        if (userDAO.updateUser(userId, updatedUser))
            System.out.println("Обновлено");

        LOGGER.info("User {} updated", updatedUser);
    }

    public void deleteUser() throws IOException {

        LOGGER.info("User chose delete user");

        System.out.print("Id удаляемого пользователя: ");
        int deletedId = Integer.parseInt(reader.readLine());
        userDAO.deleteUser(deletedId);
        System.out.println("Удалено (если существовало)");

        LOGGER.info("User {} deleted", deletedId);
    }
}
