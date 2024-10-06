package com.libvasf.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Carrega variáveis do arquivo .env
            Dotenv dotenv = Dotenv.load();

            // Recuperar variáveis do .env
            String dbHost = "localhost"; // Assumindo que o banco está rodando localmente
            String dbPort = dotenv.get("DB_PORT");
            String dbName = dotenv.get("DB_DATABASE");
            String dbUser = dotenv.get("DB_USER");
            String dbPassword = dotenv.get("DB_PASSWORD");

            // Construir a URL do banco de dados
            String dbUrl = String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, dbName);

            // Verifica se todas as variáveis foram carregadas corretamente
            if (dbUrl == null || dbUser == null || dbPassword == null) {
                throw new RuntimeException("Propriedades do banco de dados não foram carregadas corretamente");
            }

            // Configuração do Hibernate
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            // Configurações do banco de dados
            configuration.setProperty("hibernate.connection.url", dbUrl);
            configuration.setProperty("hibernate.connection.username", dbUser);
            configuration.setProperty("hibernate.connection.password", dbPassword);

            // Adicionar as entidades mapeadas
            configuration.addAnnotatedClass(com.libvasf.models.Cliente.class);
            configuration.addAnnotatedClass(com.libvasf.models.Usuario.class);
            configuration.addAnnotatedClass(com.libvasf.models.Livro.class);
            configuration.addAnnotatedClass(com.libvasf.models.Autor.class);
            configuration.addAnnotatedClass(com.libvasf.models.Emprestimo.class);
            configuration.addAnnotatedClass(com.libvasf.models.Publicacao.class);
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Falha na criação da SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
