package com.libvasf.controllers.publicacao;

import com.libvasf.models.Publicacao;
import com.libvasf.models.Autor;
import com.libvasf.models.Livro;
import com.libvasf.services.PublicacaoService;
import com.libvasf.services.AutorService;
import com.libvasf.services.LivroService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PublicacaoController {

    private static final Logger logger = Logger.getLogger(PublicacaoController.class.getName());
    private static final PublicacaoService publicacaoService = new PublicacaoService();
    private static final AutorService autorService = new AutorService();
    private static final LivroService livroService = new LivroService();

    public void salvarPublicacao(Long livroId, Long autorId, Integer ano) {
        try {
            Livro livro = livroService.buscarLivroPorId(livroId);
            Autor autor = autorService.buscarAutorPorId(autorId);

            if (livro == null || autor == null) {
                throw new IllegalArgumentException("Livro ou Autor não encontrado.");
            }

            Publicacao publicacao = new Publicacao();
            publicacao.setLivro(livro);
            publicacao.setAutor(autor);
            publicacao.setAno(ano);

            publicacaoService.salvarPublicacao(publicacao);
            logger.info("Publicação salva com sucesso: " + publicacao);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao salvar a publicação", e);
        }
    }

    public void editarPublicacao(Long id, Long livroId, Long autorId, Integer ano) {
        try {
            Livro livro = livroService.buscarLivroPorId(livroId);
            Autor autor = autorService.buscarAutorPorId(autorId);

            if (livro == null || autor == null) {
                throw new IllegalArgumentException("Livro ou Autor não encontrado.");
            }

            Publicacao publicacao = new Publicacao();
            publicacao.setId(id);
            publicacao.setLivro(livro);
            publicacao.setAutor(autor);
            publicacao.setAno(ano);

            publicacaoService.editarPublicacao(publicacao);
            logger.info("Publicação editada com sucesso: " + publicacao);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao editar a publicação", e);
        }
    }

    public Publicacao buscarPublicacaoPorId(Long id) {
        try {
            return publicacaoService.buscarPublicacaoPorId(id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar a publicação por ID: " + id, e);
            return null;
        }
    }

    public List<Publicacao> listarPublicacoes() {
        try {
            return publicacaoService.listarPublicacoes();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao listar as publicações", e);
            return null;
        }
    }

    public void removerPublicacao(Long id) {
        try {
            publicacaoService.removerPublicacao(id);
            logger.info("Publicação removida com sucesso: ID = " + id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao remover a publicação: ID = " + id, e);
        }
    }
}