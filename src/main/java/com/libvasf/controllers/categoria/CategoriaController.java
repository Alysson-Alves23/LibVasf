package com.libvasf.controllers.categoria;

import com.libvasf.models.Categoria;
import com.libvasf.services.CategoriaService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoriaController {

    private static final Logger logger = Logger.getLogger(CategoriaController.class.getName());
    private static final CategoriaService categoriaService = new CategoriaService();

    public void salvarCategoria(String nome) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);

        try {
            categoriaService.salvarCategoria(categoria);
            logger.info("Categoria salva com sucesso: " + categoria);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao salvar a categoria: " + categoria, e);
        }
    }

    public void editarCategoria(Long id, String nome) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome(nome);

        try {
            categoriaService.salvarCategoria(categoria);
            logger.info("Categoria editada com sucesso: " + categoria);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao editar a categoria: " + categoria, e);
        }
    }

    public Categoria buscarCategoriaPorId(Long id) {
        try {
            return categoriaService.buscarCategoriaPorId(id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar a categoria por ID: " + id, e);
            return null;
        }
    }

    public List<Categoria> listarCategorias() {
        try {
            return categoriaService.listarCategorias();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao listar as categorias", e);
            return null;
        }
    }

    public void removerCategoria(Long id) {
        try {
            categoriaService.removerCategoria(id);
            logger.info("Categoria removida com sucesso: ID = " + id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao remover a categoria: ID = " + id, e);
        }
    }
}