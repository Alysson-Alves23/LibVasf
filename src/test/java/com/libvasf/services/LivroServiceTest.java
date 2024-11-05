package com.libvasf.services;

import com.libvasf.models.Livro;
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
class LivroServiceTest {

    private static final Logger logger = Logger.getLogger(LivroServiceTest.class.getName());

    @InjectMocks
    private LivroService service;

    private final List<Long> livrosCriados = new java.util.ArrayList<>();

    @AfterEach
    void cleanUp() {
        for (Long livroId : livrosCriados) {
            try {
                service.removerLivro(livroId);
                logger.info("Livro removido durante o cleanup: ID = " + livroId);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao remover livro durante o cleanup: ID = " + livroId, e);
            }
        }
        livrosCriados.clear();
    }

    @Nested
    @DisplayName("Testes de Cadastro de Livro")
    class CreateLivroTests {

        @Test
        @DisplayName("Deve cadastrar um livro com sucesso")
        void shouldCreateLivroWithSuccess() {
            Livro livro = livroMock();

            try {
                service.salvarLivro(livro);
                assertNotNull(livro.getId(), "O ID do livro deveria ter sido gerado.");
                logger.info("Livro criado com sucesso: " + livro);
                livrosCriados.add(livro.getId());
            } catch (Exception e) {
                fail("Deveria ter adicionado sem exceção, mas retornou: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Deve falhar ao cadastrar livro sem ISBN")
        void shouldFailCreatingLivroWithoutISBN() {
            Livro livro = livroMock();
            livro.setIsbn(null);

            Exception exception = assertThrows(Exception.class, () -> {
                service.salvarLivro(livro);
            });

            String expectedMessage = "ISBN não pode ser nulo";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage),
                    "A exceção lançada deveria estar relacionada ao ISBN não nulo.");
        }
    }

    @Nested
    @DisplayName("Testes de Pesquisa de Livro")
    class SearchLivroTests {

        @Test
        @DisplayName("Deve buscar livro por título corretamente")
        void shouldFindLivroByTitle() {
            Livro livro = livroMock();
            service.salvarLivro(livro);
            livrosCriados.add(livro.getId());

            List<Livro> resultados = service.buscarPorTitulo("Livro de Teste");
            assertNotNull(resultados, "A pesquisa deveria retornar resultados.");
            assertTrue(resultados.size() > 0, "Deveria ter retornado pelo menos um livro.");
        }

        @Test
        @DisplayName("Deve falhar ao buscar livro por título inexistente")
        void shouldFailFindLivroWithInvalidTitle() {
            List<Livro> resultados = service.buscarPorTitulo("Título Inexistente");
            assertTrue(resultados.isEmpty(), "A pesquisa não deveria retornar resultados.");
        }
    }

    private Livro livroMock() {
        Livro livro = new Livro();
        livro.setTitulo("Livro de Teste");
        livro.setIsbn(1234567890);
        livro.setCategoria("Ficção");
        livro.setNumeroCopias(5);
        livro.setDisponivel(true);
        service.salvarLivro(livro);
        return livro;
    }
}
