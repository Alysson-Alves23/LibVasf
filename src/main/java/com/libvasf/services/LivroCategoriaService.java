package com.libvasf.services;

import com.libvasf.models.LivroCategoria;
import com.libvasf.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LivroCategoriaService {

    private static final Logger logger = Logger.getLogger(LivroCategoriaService.class.getName());

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

    public void salvarLivroCategoria(LivroCategoria livroCategoria) {

        if (livroCategoria.getLivro() == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }

        if (livroCategoria.getCategoria() == null) {
            throw new IllegalArgumentException("Categoria não pode ser nulo");
        }

        executeInsideTransaction(session -> {
            session.save(livroCategoria);
            logger.info("LivroCategoria salva com sucesso: " + livroCategoria);
        });
    }

    public List<LivroCategoria> listarLivroCategorias() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<LivroCategoria> livroCategorias = session.createQuery("from LivroCategoria", LivroCategoria.class).list();
            logger.info("Listagem de LivroCategorias realizada com sucesso. Total: " + livroCategorias.size());
            return livroCategorias;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao listar LivroCategorias: " + he.getMessage(), he);
            throw he;
        }
    }

    public LivroCategoria buscarLivroCategoriaPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LivroCategoria livroCategoria = session.get(LivroCategoria.class, id);
            if (livroCategoria != null) {
                logger.info("LivroCategoria encontrada pelo ID " + id + ": " + livroCategoria);
            } else {
                logger.warning("Nenhuma LivroCategoria encontrada com o ID " + id);
            }
            return livroCategoria;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao buscar LivroCategoria por ID " + id + ": " + he.getMessage(), he);
            throw he;
        }
    }

    public void removerLivroCategoria(Long id) {
        executeInsideTransaction(session -> {
            LivroCategoria livroCategoria = session.get(LivroCategoria.class, id);
            if (livroCategoria != null) {
                session.delete(livroCategoria);
                logger.info("LivroCategoria removida com sucesso: " + livroCategoria);
            } else {
                logger.warning("Nenhuma LivroCategoria encontrada para remover com o ID " + id);
            }
        });
    }

    @FunctionalInterface
    private interface SessionAction {
        void execute(Session session) throws HibernateException;
    }
}
