package com.libvasf.services;

import com.libvasf.models.Cliente;
import com.libvasf.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteService {

    private static final Logger logger = Logger.getLogger(ClienteService.class.getName());


    public Cliente buscarClientePorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Realiza a consulta utilizando HQL para buscar o cliente pelo nome
            return session.createQuery("FROM Cliente WHERE nome = :nome", Cliente.class)
                    .setParameter("nome", nome)
                    .uniqueResult();
        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "Erro ao buscar cliente por nome: " + nome, e);
            return null;
        }
    }

    @FunctionalInterface
    public interface SessionAction {
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
                if (transaction.isActive()) transaction.rollback();
                logger.log(Level.SEVERE, "Erro durante a transação: " + he.getMessage(), he);
                throw he;
            }
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Falha ao abrir sessão: " + he.getMessage(), he);
            throw he;
        }
    }

    public void salvarCliente(Cliente cliente) {
        if(Objects.isNull(cliente.getNome())) {
            throw new IllegalArgumentException("Nome não pode ser nulo");
        }
        executeInsideTransaction(session -> session.save(cliente));
        logger.info("Cliente salvo com sucesso: " + cliente);
    }

    public List<Cliente> listarClientes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Cliente", Cliente.class).list();
        }
    }

    public Cliente buscarClientePorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Cliente.class, id);
        }
    }

    public void removerCliente(Long id) {
        executeInsideTransaction(session -> {
            Cliente cliente = session.get(Cliente.class, id);
            if (cliente != null) {
                session.delete(cliente);
                logger.info("Cliente removido com sucesso: " + cliente);
            } else {
                logger.warning("Cliente com ID " + id + " não encontrado para remoção.");
            }
        });
    }

    public void editarCliente(Cliente cliente) {
        executeInsideTransaction(session -> {
            session.update(cliente);
            logger.info("Cliente atualizado com sucesso: " + cliente);
        });
    }
}
