package com.libvasf.services;

import com.libvasf.models.Emprestimo;
import com.libvasf.models.Livro;
import com.libvasf.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmprestimoService {

    private static final Logger logger = Logger.getLogger(EmprestimoService.class.getName());

    public List<Emprestimo> buscarEmprestimosPorNomeCliente(String nomeCliente) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Emprestimo e WHERE e.cliente.nome = :nomeCliente", Emprestimo.class)
                    .setParameter("nomeCliente", nomeCliente)
                    .list();
        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "Erro ao buscar empréstimos pelo nome do cliente: " + nomeCliente, e);
            return null;
        }
    }
    public List<Emprestimo> buscarEmprestimosPorCpf(String cpf) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Emprestimo e WHERE e.cliente.cpf = :cpf", Emprestimo.class)
                    .setParameter("cpf", cpf)
                    .list();
        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "Erro ao buscar empréstimos pelo cpf do cliente: " + cpf, e);
            return null;
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
            } catch (HibernateException he) {
                if (transaction != null) transaction.rollback();
                logger.log(Level.SEVERE, "Erro durante a transação", he);
                throw he;
            }
        }
    }

    public void realizarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo.getCliente() == null) throw new IllegalArgumentException("Cliente não pode ser nulo");

        Livro livro = emprestimo.getLivro();
        if(!livro.getDisponivel()) throw new IllegalArgumentException("Não há cópias disponíveis");

        livro.setNumeroCopias(livro.getNumeroCopias() - 1);
        emprestimo.setDataHoraInicio(LocalDateTime.now());
        emprestimo.setIsActive(true);

        executeInsideTransaction(session -> {
            session.saveOrUpdate(emprestimo);
            session.update(livro);
            logger.info("Emprestimo realizado com sucesso: "+ emprestimo);
        });
    }


    public Emprestimo buscarEmprestimoPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Emprestimo emprestimo = session.get(Emprestimo.class, id);
            if (emprestimo == null){
                throw new IllegalArgumentException("Empréstimo não encontrado");
            }
            return emprestimo;
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao buscar empréstimo por ID" + id, he);
            throw he;
        }
    }


    public void atualizarUsuario(Emprestimo emprestimo) {
        try {
            executeInsideTransaction(session -> {
                session.update(emprestimo);
                logger.info("Emprestimo Atualizado com Sucesso: " + emprestimo.getId());
            });
        }catch (HibernateException he) {
            throw he;
        }

    }
    public void removerEmprestimo(Long id) {
        executeInsideTransaction(session -> {
            Emprestimo emprestimo = session.get(Emprestimo.class, id);
            if (emprestimo != null) {
                session.delete(emprestimo);
                logger.info("Empréstimo removido com sucesso: " + emprestimo);
            }
            else logger.warning("Nenhum empréstimo encontrado para remoção com ID: " + id);
        });
    }

    public void realizarDevolucao(Long id) {
        executeInsideTransaction(session -> {
            Emprestimo emprestimo = session.get(Emprestimo.class, id);
            if (emprestimo == null || emprestimo.isClosed()) {
                throw new IllegalArgumentException("Empréstimo não encontrado ou já fechado");
            }
            Livro livro = emprestimo.getLivro();
            livro.setNumeroCopias((livro.getNumeroCopias() + 1));
            emprestimo.setDataHoraInicio(LocalDate.now().atStartOfDay());
            emprestimo.setIsActive(false);

            session.update(emprestimo);
            session.update(livro);
            logger.info("Devolução realizada com sucesso para o empréstimo ID: "+ id);
        });
    }

    public List<Emprestimo> listarEmprestimos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Emprestimo", Emprestimo.class).list();
        } catch (HibernateException he) {
            logger.log(Level.SEVERE, "Erro ao listar empréstimos", he);
            throw he;
        }
    }
}
