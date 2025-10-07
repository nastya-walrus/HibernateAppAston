package org.example.service;

import org.example.DAO.UserDAO;
import org.example.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;
    private final BufferedReader reader;
    private boolean isExitCommand = false;

    public UserService(UserDAO userDAO, BufferedReader reader) {
        this.userDAO = userDAO;
        this.reader = reader;
    }

    public void run() throws IOException {
        while (!isExitCommand) {
            welcomeMessage(userDAO);
        }
    }

    public void welcomeMessage(UserDAO userDAO) throws IOException {
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
        System.out.print("Имя: ");
        String name = reader.readLine();
        System.out.print("Email: ");
        String email = reader.readLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(reader.readLine());

        User createdUser = new User(name, email, age, OffsetDateTime.now());
        userDAO.createUser(createdUser);
        System.out.println("Создан пользователь: " + createdUser);
    }

    public void findUser() throws IOException {
        System.out.print("Id пользователя: ");
        int id = Integer.parseInt(reader.readLine());
        User foundUser = userDAO.getUser(id);
        System.out.println(foundUser == null ? "Не найден" : foundUser);
    }

    public void listUsers() {
        List<User> allUsers = userDAO.getAllUsers();
        allUsers.forEach(System.out::println);
    }

    public void updateUser() throws IOException {
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
    }

    public void deleteUser() throws IOException {
        System.out.print("Id удаляемого пользователя: ");
        int deletedId = Integer.parseInt(reader.readLine());
        userDAO.deleteUser(deletedId);
        System.out.println("Удалено (если существовало)");
    }
}
