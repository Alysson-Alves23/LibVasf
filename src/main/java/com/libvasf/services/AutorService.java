package com.libvasf.services;

import com.libvasf.models.Autor;
import com.libvasf.models.Categoria;
import com.libvasf.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.sun.tools.attach.VirtualMachine.list;


public class AutorService {

    private static final Logger logger = Logger.getLogger(AutorService.class.getName());

    private void executeInsideTransaction(SessionAction action){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try {
                action.execute(session);
                transaction.commit();
                logger.info("Transação executada com sucesso.");
            } catch (HibernateException he){
                if(transaction.isActive()) transaction.rollback();
                logger.log(Level.SEVERE, "Erro durante a transação: "+ he.getMessage(), he);
                throw he;
            }
        }catch(HibernateException he){
            logger.log(Level.SEVERE, "Falha ao abrir sessão: "+ he.getMessage(), he);
            throw he;
        }
    }

    public void salvarAutor(Autor autor) {
        executeInsideTransaction(session ->{
            session.save(autor);
            logger.info("Autor salvo com sucesso: " + autor);
        });
    }

    public Autor buscarAutorPorId(Long id){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Autor autor = session.get(Autor.class, id);
            if(autor != null){
                logger.info("Autor encontrado pelo ID " + id + ": " + autor);
            } else{
                logger.warning("Nehnum autor encontrado com o ID " + id);
            }
            return autor;
        } catch (HibernateException he){
            logger.log(Level.SEVERE, "Erro ao buscar autor por ID: " + id, he);
            throw he;
        }
    }

    public List<Autor> listarAutores(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            List<Autor> autores = session.createQuery("from Autor", Autor.class).list();
            logger.info("Autores encontrados com sucesso.  Total: "+ autores.size());
            return autores;
        } catch (HibernateException he){
            logger.log(Level.SEVERE, "Erro ao buscar os Autores" + he.getMessage(), he);
            throw he;
        }
    }

    public List<Autor> listarAutorPorLivroId(Long id){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            List<Autor> autores = session.createQuery(
                        "SELECT DISTINCT A FROM Autor A " +
                            "JOIN Publicacao P ON P.autor.id = A.id " +
                            "WHERE P.livro.id = :id",
                            Autor.class)
                    .setParameter("id", id)
                    .getResultList();
            logger.info("Autores encontrados com sucesso.  Total: "+ autores.size());
            return autores;
        } catch (HibernateException he){
            logger.log(Level.SEVERE, "Erro ao buscar os Autores" + he.getMessage(), he);
            throw he;
        }
    }

    public static Autor buscarAutorPorNome(String nome){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Autor autor = session.createQuery("from Autor where nome = :nome", Autor.class)
                    .setParameter("nome", nome)
                    .uniqueResult();
            if (autor != null) {
                logger.info("Autor encontrado pelo ID " + nome + ": " + autor);
            } else {
                logger.warning("Nehnum autor encontrado com o ID " + nome);
            }
            return autor;
        } catch (HibernateException he){
            logger.log(Level.SEVERE, "Erro ao buscar Autor por nome: " + he.getMessage(), he);
            throw he;
        }

    }
    public void removerAutor(Long autorId) {
        executeInsideTransaction(session ->{
            Autor autor = session.get(Autor.class, autorId);
            if(autor != null){
                session.delete(autor);
                logger.info("Autor removido com sucesso: " + autor);
            } else{
                logger.warning("Nenhum autor encontrado para remover com o ID " + autorId);
            }
        });
    }
    public void editarAutor(Autor autor) {
        executeInsideTransaction(session -> {
            session.update(autor);
            logger.info("Autor editado com sucesso: " + autor);
        });
    }
    @FunctionalInterface
    private interface SessionAction {
        void execute(Session session) throws HibernateException;
    }
}
