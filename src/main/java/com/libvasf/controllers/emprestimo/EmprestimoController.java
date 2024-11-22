package com.libvasf.controllers.emprestimo;

import com.libvasf.models.Cliente;
import com.libvasf.models.Emprestimo;
import com.libvasf.models.Livro;
import com.libvasf.models.Usuario;
import com.libvasf.services.ClienteService;
import com.libvasf.services.EmprestimoService;
import com.libvasf.services.LivroService;
import com.libvasf.services.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmprestimoController {

    private static final Logger logger = Logger.getLogger(EmprestimoController.class.getName());
    private static final EmprestimoService emprestimoService = new EmprestimoService();
    private static final UsuarioService usuarioService = new UsuarioService();
    private static final LivroService livroService = new LivroService();
    private static final ClienteService clienteService = new ClienteService();

    /**
     * Busca um livro pelo título.
     *
     * @param titulo o título do livro a ser buscado
     * @return o livro encontrado ou null em caso de erro
     */
    public Livro buscarLivroPorTitulo(String titulo) {
     //   try {
      //      return livroService.buscarLivroPorTitulo(titulo);
      //  } catch (Exception e) {
       //     logger.log(Level.SEVERE, "Erro ao buscar livro por título: " + titulo, e);
       //     return null;
       // }
    }

    public List<Emprestimo> buscarEmprestimosPorNomeCliente(String nomeCliente) {
        try {
            return emprestimoService.buscarEmprestimosPorNomeCliente(nomeCliente);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar empréstimos pelo nome do cliente: " + nomeCliente, e);
            return null;
        }
    }

    /**
     * Busca um cliente pelo nome.
     *
     * @param nome o nome do cliente a ser buscado
     * @return o cliente encontrado ou null em caso de erro
     */
    public Cliente buscarClientePorNome(String nome) {
        try {
            return clienteService.buscarClientePorNome(nome);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar cliente por nome: " + nome, e);
            return null;
        }
    }



    public void realizarEmprestimo(Long usuarioId, Long livroId, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, Long clienteId) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
            Livro livro = livroService.buscarLivroPorId(livroId);
            Cliente cliente = clienteService.buscarClientePorId(clienteId);

            if (usuario == null || livro == null || cliente == null) {
                throw new IllegalArgumentException("Usuário, Livro ou Cliente não encontrado.");
            }

            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setUsuario(usuario);
            emprestimo.setLivro(livro);
            emprestimo.setCliente(cliente);
            emprestimo.setDataHoraInicio(dataHoraInicio);
            emprestimo.setDataHoraFim(dataHoraFim);

            emprestimoService.realizarEmprestimo(emprestimo);
            logger.info("Empréstimo realizado com sucesso: " + emprestimo);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao realizar empréstimo", e);
        }
    }

    public void realizarDevolucao(Long id) {
        try {
            emprestimoService.realizarDevolucao(id);
            logger.info("Devolução realizada com sucesso para o empréstimo ID: " + id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao realizar a devolução para o empréstimo ID: " + id, e);
        }
    }



    public Emprestimo buscarEmprestimoPorId(Long id) {
        try {
            return emprestimoService.buscarEmprestimoPorId(id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar o empréstimo por ID: " + id, e);
            return null;
        }
    }

    public List<Emprestimo> listarEmprestimos() {
        try {
            return emprestimoService.listarEmprestimos();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao listar os empréstimos", e);
            return null;
        }
    }

    public void removerEmprestimo(Long id) {
        try {
            emprestimoService.removerEmprestimo(id);
            logger.info("Empréstimo removido com sucesso: ID = " + id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao remover o empréstimo: ID = " + id, e);
        }
    }
}