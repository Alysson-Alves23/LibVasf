package com.libvasf.controllers.livro;

import com.libvasf.models.*;
import com.libvasf.services.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LivroController {

    private static final Logger logger = Logger.getLogger(LivroController.class.getName());
    private static final LivroService livroService = new LivroService();
    private static final PublicacaoService publicacaoService = new PublicacaoService();
    private static final LivroCategoriaService livroCategoriaService = new LivroCategoriaService();
    private static final CategoriaService categoriaService = new CategoriaService();
    private static final AutorService autorService = new AutorService();

    public void salvarLivro(String titulo, Integer isbn, Boolean disponivel, Integer copias, Long categoria, String autor, Integer ano) {
        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setIsbn(isbn);
        livro.setDisponivel(disponivel);
        livro.setNumeroCopias(copias);


        Publicacao publicacao = new Publicacao();
        publicacao.setLivro(livro);
        publicacao.setAno(ano);

        LivroCategoria livroCategoria = new LivroCategoria();
        livroCategoria.setLivro(livro);

        try {
            livroCategoria.setCategoria(categoriaService.buscarCategoriaPorId(categoria));
            Autor autorBook = autorService.buscarAutorPorNome(autor);
            if(autorBook == null) {
                autorBook = new Autor();
                autorBook.setNome(autor);
                autorService.salvarAutor(autorBook);
            }
            publicacao.setAutor(autorBook);
            livroService.salvarLivro(livro);
            publicacaoService.salvarPublicacao(publicacao);
            livroCategoriaService.salvarLivroCategoria(livroCategoria);
            logger.info("Livro salvo com sucesso: " + livro);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao salvar o livro: " + livro, e);
        }

    }

    public void editarLivro(Long id, String titulo, Integer isbn, Boolean disponivel, int copias, Long autor,Long categoria, Integer ano) {
        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setIsbn(isbn);
        livro.setDisponivel(disponivel);
        livro.setNumeroCopias(copias);

        Publicacao publicacao = new Publicacao();
        publicacao.setLivro(livro);

        publicacao.setAno(ano);
        try {
            publicacao.setAutor(autorService.buscarAutorPorId(autor));

            livroService.salvarLivro(livro);
            publicacaoService.salvarPublicacao(publicacao);
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