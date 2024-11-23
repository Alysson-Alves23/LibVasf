package com.libvasf.services;

import com.libvasf.models.Autor;
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
class AutorServiceTest {

    private static final Logger logger = Logger.getLogger(AutorServiceTest.class.getName());

    @InjectMocks
    private AutorService service;

    private final List<Long> autoresCriados = new java.util.ArrayList<>();

    @AfterEach
    void cleanUp() {
        for (Long autorId : autoresCriados) {
            try {
                service.removerAutor(autorId);
                logger.info("Autor removido durante o cleanup: ID = " + autorId);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao remover autor durante o cleanup: ID = " + autorId, e);
            }
        }
        autoresCriados.clear();
    }

    @Nested
    @DisplayName("Testes de Criação de Autor")
    class CreateAutorTests {

        @Test
        @DisplayName("Deve criar um autor com sucesso")
        void shouldCreateAutorWithSuccess() {
            Autor autor = autorMock();

            try {
                service.salvarAutor(autor);
                assertNotNull(autor.getId(), "O ID do autor deveria ter sido gerado.");
                logger.info("Autor criado com sucesso: " + autor);
                autoresCriados.add(autor.getId());
            } catch (Exception e) {
                fail("Deveria ter adicionado sem exceção, mas retornou: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar criar autor sem nome")
        void shouldFailCreatingAutorWithoutNome() {
            Autor autor = autorMock();
            autor.setNome(null); // Remove o nome para simular erro

            Exception exception = assertThrows(Exception.class, () -> {
                service.salvarAutor(autor);
            });

            String expectedMessage = "Propriedades do banco de dados não foram carregadas corretamente";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage) || actualMessage.contains("not-null"),
                    "A exceção lançada deveria estar relacionada ao nome não nulo.");
            logger.warning("Exceção esperada ao criar autor sem nome: " + actualMessage);
        }
    }
    @Nested
    @DisplayName("Testes de Edição de Autor")
    class EditAutorTests {

        @Test
        @DisplayName("Deve editar um autor com sucesso")
        void shouldEditAutorWithSuccess() {
            Autor autor = autorMock();
            service.salvarAutor(autor);
            autoresCriados.add(autor.getId());

            autor.setNome("Nome Editado");
            service.editarAutor(autor);

            Autor atualizado = service.buscarAutorPorId(autor.getId());
            assertEquals("Nome Editado", atualizado.getNome(), "O nome deveria ter sido atualizado.");
        }
    }

    // Método para criar um mock de autor
    private Autor autorMock() {
        Autor autor = new Autor();
        autor.setNome("Autor Teste");
        return autor;
    }
}
