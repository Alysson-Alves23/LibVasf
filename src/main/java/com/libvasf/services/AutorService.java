package com.libvasf.services;

import com.libvasf.models.Autor;
import com.libvasf.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.util.logging.Level;
import java.util.logging.Logger;

public class AutorService {

    private final Logger logger = Logger.getLogger(AutorService.class.getName());

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

    public void salvarAutor(Autor autor) {
        executeInsideTransaction(session -> {
            session.saveOrUpdate(autor);
            logger.info("Autor salvo com sucesso: " + autor);
        });
    }

    public void removerAutor(Long autorId) {

    }
}
