package org.example.config;

import org.example.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SessionFactoryConfiguration implements AutoCloseable{
    private final SessionFactory sessionFactory;

    public SessionFactoryConfiguration() {
        this("hibernate.properties");
    }

    public SessionFactoryConfiguration(String propertiesFile) {
        try {
            Properties properties = new Properties();
            try (InputStream input = getClass().getClassLoader()
                    .getResourceAsStream(propertiesFile)) {
                if (input == null) {
                    throw new RuntimeException(propertiesFile + " не найден в resources");
                }
                properties.load(input);
            }

            Configuration configuration = new Configuration();
            configuration.setProperties(properties);

            configuration.addAnnotatedClass(User.class);

            this.sessionFactory = configuration.buildSessionFactory();

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения " + propertiesFile, e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации SessionFactory", e);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public void close() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
