package com.libvasf.services;

import com.libvasf.models.Categoria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    private static final Logger logger = Logger.getLogger(CategoriaServiceTest.class.getName());

    @InjectMocks
    private CategoriaService service;

    private final List<Long> categoriasCriadas = new ArrayList<>();

    @AfterEach
    void cleanUp() {
        for (Long id : categoriasCriadas) {
            try {
                service.removerCategoria(id);
                logger.info("Categoria removida durante o cleanup: ID = " + id);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao remover Categoria durante o cleanup: ID = " + id, e);
            }
        }
        categoriasCriadas.clear();
    }

    @Nested
    @DisplayName("Testes de Cadastro de Categoria")
    class CreateCategoriaTests {

        @Test
        @DisplayName("Deve cadastrar Categoria com sucesso")
        void shouldCreateCategoriaWithSuccess() {
            Categoria categoria = categoriaMock();
            System.out.println(categoria.getNome());
            try {
                service.salvarCategoria(categoria);
                assertNotNull(categoria.getId(), "O ID da Categoria deveria ter sido gerado.");
                logger.info("Categoria criada com sucesso: " + categoria);
                categoriasCriadas.add(categoria.getId());
            } catch (Exception e) {
                fail("Deveria ter adicionado sem exceção, mas retornou: " + e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Testes de Pesquisa de Categoria")
    class SearchCategoriaTests {

        @Test
        @DisplayName("Deve listar Categorias corretamente")
        void shouldListCategorias() {
            Categoria categoria = categoriaMock();
            service.salvarCategoria(categoria);
            categoriasCriadas.add(categoria.getId());

            List<Categoria> resultados = service.listarCategorias();
            assertNotNull(resultados, "A listagem deveria retornar resultados.");
            assertTrue(resultados.size() > 0, "Deveria ter retornado pelo menos uma Categoria.");
        }

        @Test
        @DisplayName("Deve buscar Categoria por ID com sucesso")
        void shouldFindCategoriaById() {
            Categoria categoria = categoriaMock();
            service.salvarCategoria(categoria);
            categoriasCriadas.add(categoria.getId());

            Categoria categoriaEncontrada = service.buscarCategoriaPorId(categoria.getId());
            assertNotNull(categoriaEncontrada, "A categoria deveria ser encontrada.");
            assertEquals(categoria.getId(), categoriaEncontrada.getId(), "O ID da categoria não corresponde.");
        }

    }

    @Nested
    @DisplayName("Testes de Edição de Categoria")
    class EditCategoriaTests {

        @Test
        @DisplayName("Deve editar Categoria com sucesso")
        void shouldEditCategoriaWithSuccess() {
            Categoria categoria = categoriaMock();
            service.salvarCategoria(categoria);
            categoriasCriadas.add(categoria.getId());

            categoria.setNome("Ficção Científica");
            try {
                service.salvarCategoria(categoria); // Re-salvando para atualização
                Categoria categoriaAtualizada = service.buscarCategoriaPorId(categoria.getId());
                assertEquals("Ficção Científica", categoriaAtualizada.getNome(), "O nome da categoria não foi atualizado.");
            } catch (Exception e) {
                fail("Deveria ter atualizado a categoria sem exceção, mas retornou: " + e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Testes de Remoção de Categoria")
    class RemoveCategoriaTests {

        @Test
        @DisplayName("Deve remover Categoria com sucesso")
        void shouldRemoveCategoriaWithSuccess() {
            Categoria categoria = categoriaMock();
            service.salvarCategoria(categoria);
            categoriasCriadas.add(categoria.getId());

            try {
                service.removerCategoria(categoria.getId());
                Categoria categoriaRemovida = service.buscarCategoriaPorId(categoria.getId());
                assertNull(categoriaRemovida, "A categoria deveria ter sido removida.");
            } catch (Exception e) {
                fail("Deveria ter removido a categoria sem exceção, mas retornou: " + e.getMessage());
            }
        }

    }

    private Categoria categoriaMock() {
        Categoria categoria = new Categoria();
        categoria.setNome("Ficção");
        service.salvarCategoria(categoria);
        return categoria;
    }
}
