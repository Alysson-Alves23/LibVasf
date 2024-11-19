package com.libvasf.controllers.livro;

import com.libvasf.models.Livro;
import com.libvasf.services.LivroService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LivroController {

    private static final Logger logger = Logger.getLogger(LivroController.class.getName());
    private static final LivroService livroService = new LivroService();

    public void salvarLivro(String titulo, Integer isbn, Boolean disponivel, int copias) {
        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setIsbn(isbn);
        livro.setDisponivel(disponivel);
        livro.setNumeroCopias(copias);

        try {
            livroService.salvarLivro(livro);
            logger.info("Livro salvo com sucesso: " + livro);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao salvar o livro: " + livro, e);
        }
    }

    public void editarLivro(Long id, String titulo, Integer isbn, Boolean disponivel, int copias) {
        Livro livro = new Livro();
        livro.setId(id);
        livro.setTitulo(titulo);
        livro.setIsbn(isbn);
        livro.setDisponivel(disponivel);
        livro.setNumeroCopias(copias);

        try {
            livroService.editarLivro(livro);
            logger.info("Livro editado com sucesso: " + livro);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao editar o livro: " + livro, e);
        }
    }

    public Livro buscarLivroPorId(Long id) {
        try {
            return livroService.buscarLivroPorId(id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar o livro por ID: " + id, e);
            return null;
        }
    }

    public List<Livro> listarLivros() {
        try {
            return livroService.listarLivros();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao listar os livros", e);
            return null;
        }
    }

    public void removerLivro(Long id) {
        try {
            livroService.removerLivro(id);
            logger.info("Livro removido com sucesso: ID = " + id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao remover o livro: ID = " + id, e);
        }
    }
}