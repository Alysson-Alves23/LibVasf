package com.libvasf.services;

import com.libvasf.models.Cliente;
import com.libvasf.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ClienteService {

    public void salvarCliente(Cliente cliente) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(cliente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<Cliente> listarClientes() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Cliente> clientes = session.createQuery("from Cliente", Cliente.class).list();
        session.close();
        return clientes;
    }

    public Cliente buscarClientePorId(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Cliente cliente = session.get(Cliente.class, id);
        session.close();
        return cliente;
    }

    public void removerCliente(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Cliente cliente = session.get(Cliente.class, id);
            if (cliente != null) {
                session.delete(cliente);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
