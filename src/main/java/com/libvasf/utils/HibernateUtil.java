package com.libvasf.utils;

import com.libvasf.models.Autor;
import com.libvasf.models.Cliente;
import com.libvasf.models.Emprestimo;
import com.libvasf.models.Livro;
import com.libvasf.models.Publicacao;
import com.libvasf.models.Usuario;
import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Dotenv dotenv = Dotenv.load();
            final String dbHost = dotenv.get("DB_HOST", "localhost"); // Tornar configurável
            final String dbPort = dotenv.get("DB_PORT", "3306");
            final String dbName = dotenv.get("DB_DATABASE");
            final String dbUser = dotenv.get("DB_USER");
            final String dbPassword = dotenv.get("DB_PASSWORD");

            if (dbName == null || dbUser == null || dbPassword == null) {
                throw new RuntimeException("Propriedades do banco de dados não foram carregadas corretamente");
            }

            final String dbUrl = String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, dbName);

            // Configuração do Hibernate
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update"); // Removido duplicado

            // Configurações do banco de dados
            configuration.setProperty("hibernate.connection.url", dbUrl);
            configuration.setProperty("hibernate.connection.username", dbUser);
            configuration.setProperty("hibernate.connection.password", dbPassword);

            // Adicionar as entidades mapeadas
            configuration.addAnnotatedClass(Cliente.class);
            configuration.addAnnotatedClass(Usuario.class);
            configuration.addAnnotatedClass(Livro.class);
            configuration.addAnnotatedClass(Autor.class);
            configuration.addAnnotatedClass(Emprestimo.class);
            configuration.addAnnotatedClass(Publicacao.class);

            sessionFactory = configuration.buildSessionFactory();
            System.out.println("SessionFactory criada com sucesso.");

            verificarEInserirUsuarioRoot();

        } catch (Throwable ex) {
            System.err.println("Falha na criação da SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static void verificarEInserirUsuarioRoot() {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Verifica se existe um usuário administrador
            Usuario usuarioRoot = session.createQuery("FROM Usuario WHERE nome = :nome", Usuario.class).setParameter("nome", "root").uniqueResult();

            if (usuarioRoot == null) {
                Usuario root = new Usuario();
                root.setNome("root");
                root.setEmail("root@admin.com");
                root.setSenha("root");
                root.setIsAdmin(1);

                session.save(root);
                System.out.println("Usuário root criado com sucesso.");
            } else {
                System.out.println("Usuário root já existe.");
            }

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Erro ao verificar/inserir usuário root: " + e);
            e.printStackTrace();
        }
    }
}
