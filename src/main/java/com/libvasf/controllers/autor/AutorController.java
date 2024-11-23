package com.libvasf.controllers.autor;

import com.libvasf.models.Autor;
import com.libvasf.services.AutorService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutorController {

    private static final Logger logger = Logger.getLogger(AutorController.class.getName());
    private static final AutorService autorService = new AutorService();

    public void salvarAutor(String nome) {
        Autor autor = new Autor();
        autor.setNome(nome);

        try {
            autorService.salvarAutor(autor);
            logger.info("Autor salvo com sucesso: " + autor);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao salvar o autor: " + autor, e);
        }
    }

    public void editarAutor(Long id, String nome) {
        Autor autor = new Autor();
        autor.setId(id);
        autor.setNome(nome);

        try {
            autorService.editarAutor(autor);
            logger.info("Autor editado com sucesso: " + autor);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao editar o autor: " + autor, e);
        }
    }

    public Autor buscarAutorPorId(Long id) {
        try {
            return autorService.buscarAutorPorId(id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar o autor por ID: " + id, e);
            return null;
        }
    }

    public List<Autor> listarAutores() {
        try {
            return autorService.listarAutores();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao listar os autores", e);
            return null;
        }
    }

    public void removerAutor(Long id) {
        try {
            autorService.removerAutor(id);
            logger.info("Autor removido com sucesso: ID = " + id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao remover o autor: ID = " + id, e);
        }
    }
}