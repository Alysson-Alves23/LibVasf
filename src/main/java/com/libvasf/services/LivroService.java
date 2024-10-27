package com.libvasf.services;

import com.libvasf.models.Livro;
import com.libvasf.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LivroService {

    private static final Logger logger = Logger.getLogger(LivroService.class.getName());

    private void executeInsideTransaction(SessionAction action) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                action.execute(session);
                transaction.commit();
                logger.info("Transação executada com sucesso.");
            } catch (HibernateException he) {
                if (transaction != null) transaction.rollback();
                logger.log(Level.SEVERE, "Erro durante a transação: " + he.getMessage(), he);
                throw he;
            }
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Falha ao abrir sessão: " + he.getMessage(), he);
            throw he;
        }
    }

    /**
     * Salva um novo livro no banco de dados.
     *
     * @param livro O objeto Livro a ser salvo.
     */
    public void salvarLivro(Livro livro) {
        executeInsideTransaction(session -> {
            session.save(livro);
            logger.info("Livro salvo com sucesso: " + livro);
        });
    }

    /**
     * Lista todos os livros do banco de dados.
     *
     * @return Uma lista de objetos Livro.
     */
    public List<Livro> listarLivros() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Livro> livros = session.createQuery("from Livro", Livro.class).list();
            logger.info("Listagem de livros realizada com sucesso. Total: " + livros.size());
            return livros;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao listar livros: " + he.getMessage(), he);
            throw he;
        }
    }

    /**
     * Busca um livro pelo seu ID.
     *
     * @param id O ID do livro.
     * @return O objeto Livro correspondente, ou null se não encontrado.
     */
    public Livro buscarLivroPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Livro livro = session.get(Livro.class, id);
            if (livro != null) {
                logger.info("Livro encontrado pelo ID " + id + ": " + livro);
            } else {
                logger.warning("Nenhum livro encontrado com o ID " + id);
            }
            return livro;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao buscar livro por ID " + id + ": " + he.getMessage(), he);
            throw he;
        }
    }

    /**
     * Busca um livro pelo seu título.
     *
     * @param titulo O título do livro.
     * @return O objeto Livro correspondente, ou null se não encontrado.
     */
    public Livro buscarLivroPorTitulo(String titulo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Livro livro = session.createQuery("from Livro where titulo = :titulo", Livro.class)
                    .setParameter("titulo", titulo)
                    .uniqueResult();
            if (livro != null) {
                logger.info("Livro encontrado pelo título '" + titulo + "': " + livro);
            } else {
                logger.warning("Nenhum livro encontrado com o título '" + titulo + "'");
            }
            return livro;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao buscar livro por título '" + titulo + "': " + he.getMessage(), he);
            throw he;
        }
    }

    /**
     * Remove um livro pelo seu ID.
     *
     * @param id O ID do livro a ser removido.
     */
    public void removerLivro(Long id) {
        executeInsideTransaction(session -> {
            Livro livro = session.get(Livro.class, id);
            if (livro != null) {
                session.delete(livro);
                logger.info("Livro removido com sucesso: " + livro);
            } else {
                logger.warning("Nenhum livro encontrado para remover com o ID " + id);
            }
        });
    }

    @FunctionalInterface
    private interface SessionAction {
        void execute(Session session) throws HibernateException;
    }
}
