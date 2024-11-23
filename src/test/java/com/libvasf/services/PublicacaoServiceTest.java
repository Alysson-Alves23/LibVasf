package com.libvasf.services;

import com.libvasf.models.Publicacao;
import com.libvasf.models.Autor;
import com.libvasf.models.Livro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class PublicacaoServiceTest {

    private static final Logger logger = Logger.getLogger(PublicacaoServiceTest.class.getName());

    @InjectMocks
    private PublicacaoService service;

    @InjectMocks
    private AutorService autorService;

    @InjectMocks
    private LivroService livroService;

    private final List<Long> publicacoesCriadas = new java.util.ArrayList<>();
    private final List<Long> livrosCriados = new java.util.ArrayList<>();
    private final List<Long> autoresCriados = new java.util.ArrayList<>();

    @AfterEach
    void cleanUp() {
        // Remover publicações
        for (Long publicacaoId : publicacoesCriadas) {
            try {
                service.removerPublicacao(publicacaoId);
                logger.info("Publicação removida durante o cleanup: ID = " + publicacaoId);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao remover publicação durante o cleanup: ID = " + publicacaoId, e);
            }
        }
        publicacoesCriadas.clear();

        //Remover autores
        for (Long autorId : autoresCriados) {
            try {
                autorService.removerAutor(autorId);
                logger.info("Autor removido durante o cleanup: ID = " + autorId);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao remover autor durante o cleanup: ID = " + autorId, e);
            }
        }
        autoresCriados.clear();

        //Remover livros
        for (Long livroId : livrosCriados) {
            try {
                livroService.removerLivro(livroId);
                logger.info("Livro removido durante o cleanup: ID = " + livroId);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao remover livro durante o cleanup: ID = " + livroId, e);
            }
        }
        livrosCriados.clear();
    }

    @Nested
    @DisplayName("Testes de Cadastro de Publicação")
    class CreatePublicacaoTests {

        @Test
        @DisplayName("Deve criar uma publicação com sucesso")
        void shouldCreatePublicacaoWithSuccess() {
            Publicacao publicacao = publicacaoMock();

            try {
                service.salvarPublicacao(publicacao);
                assertNotNull(publicacao.getId(), "O ID da publicação deveria ter sido gerado.");
                logger.info("Publicação criada com sucesso: " + publicacao);
                publicacoesCriadas.add(publicacao.getId());
            } catch (Exception e) {
                fail("Deveria ter adicionado sem exceção, mas retornou: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Deve falhar ao tentar criar publicação sem livro")
        void shouldFailCreatingPublicacaoWithoutLivro() {
            Publicacao publicacao = publicacaoMock();
            publicacao.setLivro(null);

            Exception exception = assertThrows(Exception.class, () -> {
                service.salvarPublicacao(publicacao);
            });

            String expectedMessage = "Livro não pode ser nulo";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage),
                    "A exceção lançada deveria estar relacionada ao livro não nulo.");
        }
    }

    @Nested
    @DisplayName("Testes de Edição de Publicação")
    class EditPublicacaoTests {

        @Test
        @DisplayName("Deve editar uma publicação com sucesso")
        void shouldEditPublicacaoWithSuccess() {
            Publicacao publicacao = publicacaoMock();
            service.salvarPublicacao(publicacao);
            publicacoesCriadas.add(publicacao.getId());

            publicacao.setAno(2025);
            service.editarPublicacao(publicacao);

            Publicacao atualizada = service.buscarPublicacaoPorId(publicacao.getId());
            assertEquals(2025, atualizada.getAno(), "O ano deveria ter sido atualizado.");
        }
    }

    @Nested
    @DisplayName("Testes de Remoção de Publicação")
    class RemovePublicacaoTests {

        @Test
        @DisplayName("Deve remover uma publicação com sucesso")
        void shouldRemovePublicacaoWithSuccess() {
            Publicacao publicacao = publicacaoMock();
            service.salvarPublicacao(publicacao);
            publicacoesCriadas.add(publicacao.getId());

            service.removerPublicacao(publicacao.getId());
            publicacoesCriadas.remove(publicacao.getId());

            Publicacao removida = service.buscarPublicacaoPorId(publicacao.getId());
            assertNull(removida, "A publicação deveria ter sido removida.");
        }
    }

    @Nested
    @DisplayName("Testes de Listagem de Publicações")
    class ListPublicacaoTests {

        @Test
        @DisplayName("Deve listar publicações corretamente")
        void shouldListPublicacoesCorrectly() {
            Publicacao publicacao1 = publicacaoMock();
            Publicacao publicacao2 = publicacaoMock();
            service.salvarPublicacao(publicacao1);
            service.salvarPublicacao(publicacao2);
            publicacoesCriadas.add(publicacao1.getId());
            publicacoesCriadas.add(publicacao2.getId());

            List<Publicacao> publicacoes = service.listarPublicacoes();
            assertNotNull(publicacoes, "A lista de publicações não deveria ser nula.");
            assertTrue(publicacoes.size() >= 2, "A lista de publicações deveria conter pelo menos 2 publicações.");
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Publicação")
    class BuscarPublicacaoTests {

        @Test
        @DisplayName("Deve buscar publicação por ID corretamente")
        void shouldFindPublicacaoById() {
            Publicacao publicacao = publicacaoMock();
            service.salvarPublicacao(publicacao);
            publicacoesCriadas.add(publicacao.getId());

            Publicacao encontrada = service.buscarPublicacaoPorId(publicacao.getId());
            assertNotNull(encontrada, "A publicação deveria ter sido encontrada pelo ID.");
            assertEquals(publicacao.getAno(), encontrada.getAno(), "Os anos das publicações deveriam coincidir.");
        }

        @Test
        @DisplayName("Deve falhar ao buscar publicação com ID inválido")
        void shouldFailFindPublicacaoWithInvalidId() {
            Long invalidId = 999L;

            Publicacao encontrada = service.buscarPublicacaoPorId(invalidId);
            assertNull(encontrada, "Nenhuma publicação deveria ser encontrada com o ID inválido.");
        }
    }

    private Publicacao publicacaoMock() {
        Publicacao publicacao = new Publicacao();
        publicacao.setLivro(livroMock());
        publicacao.setAutor(autorMock());
        publicacao.setAno(2023);
        service.salvarPublicacao(publicacao);
        return publicacao;
    }

    private Livro livroMock() {
        Livro livro = new Livro();
        livro.setTitulo("Livro de Teste");
        livro.setIsbn(123456789);
        livro.setDisponivel(true);
        livroService.salvarLivro(livro);
        livrosCriados.add(livro.getId());
        return livro;
    }

    private Autor autorMock() {
        Autor autor = new Autor();
        autor.setNome("Autor de Teste");
        autorService.salvarAutor(autor);
        autoresCriados.add(autor.getId());
        return autor;
    }
}
