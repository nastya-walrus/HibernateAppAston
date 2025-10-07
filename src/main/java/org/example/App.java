package org.example;

import org.example.DAO.UserDAO;
import org.example.config.SessionFactoryConfiguration;
import org.example.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws IOException {

        try (SessionFactoryConfiguration sessionFactoryConfiguration = new SessionFactoryConfiguration()) {
            UserService service = new UserService(
                    new UserDAO(sessionFactoryConfiguration.getSessionFactory()),
                    new BufferedReader(new InputStreamReader(System.in)));
            service.run();
        }
    }
}