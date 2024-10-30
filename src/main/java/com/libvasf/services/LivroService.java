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
    public void removerLivro(Long livroId) {
    }

    private static final Logger logger = Logger.getLogger(LivroService.class.getName());

    @FunctionalInterface
    private interface SessionAction {
        void execute(Session session) throws HibernateException;
    }

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

    public List<Livro> buscarPorTitulo(String livroDeTeste) {

        return List.of();
    }
}
