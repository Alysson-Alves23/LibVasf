package com.libvasf.services;

import com.libvasf.models.Usuario;
import com.libvasf.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UsuarioService {

    public void salvarUsuario(Usuario usuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(usuario);
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

    public List<Usuario> listarUsuarios() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Usuario> usuarios = session.createQuery("from Usuario", Usuario.class).list();
        session.close();
        return usuarios;
    }

    public Usuario buscarUsuarioPorId(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Usuario usuario = session.get(Usuario.class, id);
        session.close();
        return usuario;
    }

    public void removerUsuario(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Usuario usuario = session.get(Usuario.class, id);
            if (usuario != null) {
                session.delete(usuario);
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
