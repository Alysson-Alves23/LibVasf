package com.libvasf.services;

import com.libvasf.models.Publicacao;
import com.libvasf.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Id;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PublicacaoService {

    private final Logger logger = Logger.getLogger(PublicacaoService.class.getName());

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
            } catch (HibernateException he) {
                if (transaction != null) transaction.rollback();
                logger.log(Level.SEVERE, "Erro durante a transação", he);
                throw he;
            }
        }
    }

    public void salvarPublicacao(Publicacao publicacao) {
        if (publicacao.getLivro() == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }

        if (publicacao.getAutor() == null) {
            throw new IllegalArgumentException("Autor não pode ser nulo");
        }

        executeInsideTransaction(session -> {
            session.saveOrUpdate(publicacao);
            logger.info("Publicação salva com sucesso: " + publicacao);
        });
    };

    public Publicacao buscarPublicacaoPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Publicacao.class, id);
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao buscar publicação por ID: " + id, he);
            throw he;
        }
    }

    public List<Publicacao> listarPublicacoes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Publicacao", Publicacao.class).list();
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao listar publicações", he);
            throw he;
        }
    }

    public void removerPublicacao(Long id) {
        executeInsideTransaction(session -> {
            Publicacao publicacao = session.get(Publicacao.class, id);
            if (publicacao != null){
                session.delete(publicacao);
                logger.info("Publicação removida com sucesso: " + publicacao);
            } else {
                logger.warning("Nenhuma publicação encontrada para remoção com ID: " + id);
            }
        });
    }

    public void editarPublicacao(Publicacao publicacao) {
        executeInsideTransaction(session -> {
            session.update(publicacao);
            logger.info("Publicação editada com sucesso: " + publicacao);
        });
    }
}
