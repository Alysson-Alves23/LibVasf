package com.libvasf.controllers.livroCategoria;

import com.libvasf.models.Categoria;
import com.libvasf.models.Livro;
import com.libvasf.models.LivroCategoria;
import com.libvasf.services.CategoriaService;
import com.libvasf.services.LivroCategoriaService;
import com.libvasf.services.LivroService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LivroCategoriaController {

    private static final Logger logger = Logger.getLogger(LivroCategoriaController.class.getName());
    private static final LivroCategoriaService livroCategoriaService = new LivroCategoriaService();
    private static final LivroService livroService = new LivroService();
    private static final CategoriaService categoriaService = new CategoriaService();

    public void salvarLivroCategoria(Long livroId, Long categoriaId) {
        LivroCategoria livroCategoria = new LivroCategoria();

        Livro livro = livroService.buscarLivroPorId(livroId);
        Categoria categoria = categoriaService.buscarCategoriaPorId(categoriaId);

        if (livro == null || categoria == null) {
            logger.log(Level.SEVERE, "Livro ou Categoria não encontrados.");
            return;
        }

        livroCategoria.setLivro(livro);
        livroCategoria.setCategoria(categoria);

        try {
            livroCategoriaService.salvarLivroCategoria(livroCategoria);
            logger.info("LivroCategoria salva com sucesso: " + livroCategoria);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao salvar a LivroCategoria: " + livroCategoria, e);
        }
    }

    public void editarLivroCategoria(Long id, Long livroId, Long categoriaId) {
        LivroCategoria livroCategoria = livroCategoriaService.buscarLivroCategoriaPorId(id);

        if (livroCategoria == null) {
            logger.log(Level.SEVERE, "LivroCategoria não encontrada.");
            return;
        }

        Livro livro = livroService.buscarLivroPorId(livroId);
        Categoria categoria = categoriaService.buscarCategoriaPorId(categoriaId);

        if (livro == null || categoria == null) {
            logger.log(Level.SEVERE, "Livro ou Categoria não encontrados.");
            return;
        }

        livroCategoria.setLivro(livro);
        livroCategoria.setCategoria(categoria);

        try {
            livroCategoriaService.editarLivroCategoria(livroCategoria);
            logger.info("LivroCategoria editada com sucesso: " + livroCategoria);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao editar a LivroCategoria: " + livroCategoria, e);
        }
    }

    public LivroCategoria buscarLivroCategoriaPorId(Long id) {
        try {
            return livroCategoriaService.buscarLivroCategoriaPorId(id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar a LivroCategoria por ID: " + id, e);
            return null;
        }
    }

    public List<LivroCategoria> listarLivroCategorias() {
        try {
            return livroCategoriaService.listarLivroCategorias();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao listar as LivroCategorias", e);
            return null;
        }
    }

    public void removerLivroCategoria(Long id) {
        try {
            livroCategoriaService.removerLivroCategoria(id);
            logger.info("LivroCategoria removida com sucesso: ID = " + id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao remover a LivroCategoria: ID = " + id, e);
        }
    }
}