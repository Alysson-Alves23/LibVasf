package com.libvasf.services;

import com.libvasf.models.Categoria;
import com.libvasf.models.Livro;
import com.libvasf.models.LivroCategoria;
import com.libvasf.models.Publicacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)

class LivroCategoriaServiceTest {

    private static final Logger logger = Logger.getLogger(LivroCategoriaServiceTest.class.getName());

    @InjectMocks
    private LivroCategoriaService service;
    @InjectMocks
    private CategoriaService categoriaService;
    @InjectMocks
    private LivroService livroService;

    private final List<Long> livrosCriados = new java.util.ArrayList<>();
    private final List<Long> categoriasCriadas = new java.util.ArrayList<>();
    private final List<Long> livroCategoriasCriadas = new java.util.ArrayList<>();

    @AfterEach
    void cleanUp() {
        for (Long id : livroCategoriasCriadas) {
            try {
                service.removerLivroCategoria(id);
                logger.info("LivroCategoria removida durante o cleanup: ID = " + id);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao remover LivroCategoria durante o cleanup: ID = " + id, e);
            }
        }
        livroCategoriasCriadas.clear();
    }

    @Nested
    @DisplayName("Testes de Cadastro de LivroCategoria")
    class CreateLivroCategoriaTests {

        @Test
        @DisplayName("Deve cadastrar uma relação LivroCategoria com sucesso")
        void shouldCreateLivroCategoriaWithSuccess() {
            LivroCategoria livroCategoria = livroCategoriaMock();

            try {
                service.salvarLivroCategoria(livroCategoria);
                assertNotNull(livroCategoria.getId(), "O ID da relação LivroCategoria deveria ter sido gerado.");
                logger.info("LivroCategoria criada com sucesso: " + livroCategoria);
                livroCategoriasCriadas.add(livroCategoria.getId());
            } catch (Exception e) {
                fail("Deveria ter adicionado sem exceção, mas retornou: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Não deve cadastrar LivroCategoria sem Livro")
        void shouldNotCreateLivroCategoriaWithoutLivro() {
            Categoria categoria = categoriaMock();
            LivroCategoria livroCategoria = new LivroCategoria();
            livroCategoria.setCategoria(categoria);

            try {
                service.salvarLivroCategoria(livroCategoria);
                fail("Deveria lançar uma exceção devido à ausência do Livro");
            } catch (Exception e) {
                assertTrue(e.getMessage().contains("Livro não pode ser nulo"), "A mensagem de erro deve informar que o Livro é obrigatório.");
            }
        }

        @Test
        @DisplayName("Não deve cadastrar LivroCategoria sem Categoria")
        void shouldNotCreateLivroCategoriaWithoutCategoria() {
            Livro livro = livroMock();
            LivroCategoria livroCategoria = new LivroCategoria();
            livroCategoria.setLivro(livro);

            try {
                service.salvarLivroCategoria(livroCategoria);
                fail("Deveria lançar uma exceção devido à ausência da Categoria");
            } catch (Exception e) {
                assertTrue(e.getMessage().contains("Categoria não pode ser nulo"), "A mensagem de erro deve informar que a Categoria é obrigatória.");
            }
        }
    }

    @Nested
    @DisplayName("Testes de Pesquisa de LivroCategoria")
    class SearchLivroCategoriaTests {

        @Test
        @DisplayName("Deve buscar LivroCategoria por ID corretamente")
        void shouldFindLivroCategoriaById() {
            LivroCategoria livroCategoria = livroCategoriaMock();
            service.salvarLivroCategoria(livroCategoria);
            livroCategoriasCriadas.add(livroCategoria.getId());

            LivroCategoria resultado = service.buscarLivroCategoriaPorId(livroCategoria.getId());
            assertNotNull(resultado, "A pesquisa deveria retornar um resultado.");
            assertEquals(livroCategoria.getId(), resultado.getId(), "O ID da relação deveria corresponder.");
        }
    }


    @Nested
    @DisplayName("Testes de Remoção de LivroCategoria")
    class DeleteLivroCategoriaTests {

        @Test
        @DisplayName("Deve remover uma LivroCategoria com sucesso")
        void shouldRemoveLivroCategoriaSuccessfully() {
            LivroCategoria livroCategoria = livroCategoriaMock();
            livroCategoriasCriadas.add(livroCategoria.getId());

            service.removerLivroCategoria(livroCategoria.getId());
            LivroCategoria resultado = service.buscarLivroCategoriaPorId(livroCategoria.getId());
            assertNull(resultado, "A LivroCategoria deveria ter sido removida.");
        }

    }

    private LivroCategoria livroCategoriaMock() {
        LivroCategoria livroCategoria = new LivroCategoria();
        livroCategoria.setCategoria(categoriaMock());
        livroCategoria.setLivro(livroMock());

        service.salvarLivroCategoria(livroCategoria);
        return livroCategoria;
    }

    private Livro livroMock(){
        Livro livro = new Livro();
        livro.setTitulo("Livro de Teste");
        livro.setIsbn(123456789);
        livro.setCategoria("Ficção");
        livro.setDisponivel(true);
        livroService.salvarLivro(livro);
        livrosCriados.add(livro.getId());
        return livro;
    }

    private Categoria categoriaMock() {
        Categoria categoria = new Categoria();
        categoria.setNome("Ficção");
        categoriaService.salvarCategoria(categoria);
        categoriasCriadas.add(categoria.getId());
        return categoria;
    }
}
