package com.libvasf.services;

import com.libvasf.models.Usuario;
import com.libvasf.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioService {

    private static final Logger logger = Logger.getLogger(UsuarioService.class.getName());

    /**
     * Método auxiliar para executar operações que necessitam de transação.
     *
     * @param action A ação a ser executada dentro da transação.
     */
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

    public Usuario buscarUsuarioPorEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Usuario usuario = session.createQuery("from Usuario where email = :email", Usuario.class)
                    .setParameter("email", email)
                    .uniqueResult();
            if (usuario != null) {
                logger.info("Usuário encontrado pelo nome '" + email + "': " + usuario);
            } else {
                logger.warning("Nenhum usuário encontrado com o nome '" + email + "'");
            }
            return usuario;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao buscar usuário por nome '" + email + "': " + he.getMessage(), he);
            throw he;
        }
    }

    @FunctionalInterface
    private interface SessionAction {
        void execute(Session session) throws HibernateException;
    }

    /**
     * Salva um novo usuário no banco de dados.
     *
     * @param usuario O objeto Usuario a ser salvo.
     */
    public void salvarUsuario(Usuario usuario) {
        executeInsideTransaction(session -> {
            session.save(usuario);
            logger.info("Usuário salvo com sucesso: " + usuario);
        });
    }

    /**
     * Lista todos os usuários do banco de dados.
     *
     * @return Uma lista de objetos Usuario.
     */
    public List<Usuario> listarUsuarios() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Usuario> usuarios = session.createQuery("from Usuario", Usuario.class).list();
            logger.info("Listagem de usuários realizada com sucesso. Total: " + usuarios.size());
            return usuarios;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao listar usuários: " + he.getMessage(), he);
            throw he;
        }
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id O ID do usuário.
     * @return O objeto Usuario correspondente, ou null se não encontrado.
     */
    public Usuario buscarUsuarioPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Usuario usuario = session.get(Usuario.class, id);
            if (usuario != null) {
                logger.info("Usuário encontrado pelo ID " + id + ": " + usuario);
            } else {
                logger.warning("Nenhum usuário encontrado com o ID " + id);
            }
            return usuario;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao buscar usuário por ID " + id + ": " + he.getMessage(), he);
            throw he;
        }
    }

    /**
     * Busca um usuário pelo seu nome.
     *
     * @param nome O nome do usuário.
     * @return O objeto Usuario correspondente, ou null se não encontrado.
     */
    public Usuario buscarUsuarioPorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Usuario usuario = session.createQuery("from Usuario where nome = :nome", Usuario.class)
                    .setParameter("nome", nome)
                    .uniqueResult();
            if (usuario != null) {
                logger.info("Usuário encontrado pelo nome '" + nome + "': " + usuario);
            } else {
                logger.warning("Nenhum usuário encontrado com o nome '" + nome + "'");
            }
            return usuario;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao buscar usuário por nome '" + nome + "': " + he.getMessage(), he);
            throw he;
        }
    }

    /**
     * Remove um usuário pelo seu ID.
     *
     * @param id O ID do usuário a ser removido.
     */
    public void removerUsuario(Long id) {
        executeInsideTransaction(session -> {
            Usuario usuario = session.get(Usuario.class, id);
            if (usuario != null) {
                session.delete(usuario);
                logger.info("Usuário removido com sucesso: " + usuario);
            } else {
                logger.warning("Nenhum usuário encontrado para remover com o ID " + id);
            }
        });
    }
}
