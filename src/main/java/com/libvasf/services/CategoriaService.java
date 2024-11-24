package com.libvasf.services;

import com.libvasf.models.Categoria;
import com.libvasf.models.Usuario;
import com.libvasf.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Transient;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoriaService {

    private static final Logger logger = Logger.getLogger(CategoriaService.class.getName());

    public List<Categoria> buscarCategoriasPorNome(String filtro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Categoria> categorias = session.createQuery("from Categoria where categoria_nome like :nome", Categoria.class)
                    .setParameter("nome",filtro+"%").getResultList();
            logger.info("Listagem de categorias realizada com sucesso. Total: " + categorias.size());
            return categorias;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao listar categorias: " + he.getMessage(), he);
            throw he;
        }


    }

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
    public void salvarCategoria(Categoria categoria) {
        executeInsideTransaction(session -> {
            session.save(categoria);
            logger.info("Categoria salva com sucesso: " + categoria);
        });
    }

    public List<Categoria> listarCategorias() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Categoria> categorias = session.createQuery("from Categoria", Categoria.class).list();
            logger.info("Listagem de categorias realizada com sucesso. Total: " + categorias.size());
            return categorias;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao listar categorias: " + he.getMessage(), he);
            throw he;
        }
    }

    public Categoria buscarCategoriaPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Categoria categoria = session.get(Categoria.class, id);
            if (categoria != null) {
                logger.info("Categoria encontrada pelo ID " + id + ": " + categoria);
            } else {
                logger.warning("Nenhuma categoria encontrada com o ID " + id);
            }
            return categoria;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao buscar categoria por ID " + id + ": " + he.getMessage(), he);
            throw he;
        }
    }

    public void removerCategoria(Long id) {
        executeInsideTransaction(session -> {
            Categoria categoria = session.get(Categoria.class, id);
            if (categoria != null) {
                session.delete(categoria);
                logger.info("Categoria removida com sucesso: " + categoria);
            } else {
                logger.warning("Nenhuma categoria encontrada para remover com o ID " + id);
            }
        });
    }


}
