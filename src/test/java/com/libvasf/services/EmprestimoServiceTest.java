package com.libvasf.services;

import com.libvasf.models.Emprestimo;
import com.libvasf.models.Cliente;
import com.libvasf.models.Livro;
import com.libvasf.models.Usuario;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class EmprestimoServiceTest {

    private static final Logger logger = Logger.getLogger(EmprestimoServiceTest.class.getName());

    @InjectMocks
    private EmprestimoService service;
    @InjectMocks
    private ClienteService clienteService;
    @InjectMocks
    private LivroService livroService;
    @InjectMocks
    UsuarioService usuarioService;

    private final List<Long> emprestimosCriados = new java.util.ArrayList<>();
    private final List<Long> clientesCriados = new java.util.ArrayList<>();
    private final List<Long> usuariosCriados = new java.util.ArrayList<>();
    private final List<Long> livrosCriados = new java.util.ArrayList<>();

    @AfterEach
    void cleanUp() {
        // Remover empréstimos
        for (Long emprestimoId : emprestimosCriados) {
            try {
                service.removerEmprestimo(emprestimoId);
                logger.info("Empréstimo removido durante o cleanup: ID = " + emprestimoId);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao remover empréstimo durante o cleanup: ID = " + emprestimoId, e);
            }
        }
        emprestimosCriados.clear();

        // Remover clientes
        for (Long clienteId : clientesCriados) {
            try {
                clienteService.removerCliente(clienteId);
                logger.info("Cliente removido durante o cleanup: ID = " + clienteId);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao remover cliente durante o cleanup: ID = " + clienteId, e);
            }
        }
        clientesCriados.clear();

        // Remover usuários
        for (Long usuarioId : usuariosCriados) {
            try {
                usuarioService.removerUsuario(usuarioId);
                logger.info("Usuário removido durante o cleanup: ID = " + usuarioId);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao remover usuário durante o cleanup: ID = " + usuarioId, e);
            }
        }
        usuariosCriados.clear();

        // Remover livros
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
    @DisplayName("Testes de Empréstimo de Livro")
    class RealizarEmprestimoTests {

        @Test
        @DisplayName("Deve realizar um empréstimo com sucesso")
        void shouldCreateEmprestimoWithSuccess() {
            Emprestimo emprestimo = emprestimoMock();

            try {
                service.realizarEmprestimo(emprestimo);
                assertNotNull(emprestimo.getId(), "O ID do empréstimo deveria ter sido gerado.");
                logger.info("Empréstimo criado com sucesso: " + emprestimo);
                emprestimosCriados.add(emprestimo.getId());
            } catch (Exception e) {
                fail("Deveria ter realizado o empréstimo sem exceção, mas retornou: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Deve falhar ao realizar empréstimo sem cliente")
        void shouldFailCreateEmprestimoWithoutCliente() {
            Emprestimo emprestimo = emprestimoMock();
            emprestimo.setCliente(null); // Remove o cliente

            Exception exception = assertThrows(Exception.class, () -> {
                service.realizarEmprestimo(emprestimo);
            });

            String expectedMessage = "Cliente não pode ser nulo";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage),
                    "A exceção lançada deveria estar relacionada ao cliente não nulo.");
        }

        @Test
        @DisplayName("Deve falhar ao realizar empréstimo quando não há cópias disponíveis")
        void shouldFailCreateEmprestimoWithoutAvailableCopies() {
            Emprestimo emprestimo = emprestimoMock();
            emprestimo.getLivro().setNumeroCopias(0); // Simula que não há cópias disponíveis

            Exception exception = assertThrows(Exception.class, () -> {
                service.realizarEmprestimo(emprestimo);
            });

            String expectedMessage = "Não há cópias disponíveis";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage),
                    "A exceção lançada deveria estar relacionada à falta de cópias.");
        }
    }

    @Nested
    @DisplayName("Testes de Devolução de Livro")
    class RealizarDevolucaoTests {

        @Test
        @DisplayName("Deve realizar devolução com sucesso")
        void shouldReturnLivroWithSuccess() {
            Emprestimo emprestimo = emprestimoMock();
            service.realizarEmprestimo(emprestimo);
            emprestimosCriados.add(emprestimo.getId());

            service.realizarDevolucao(emprestimo.getId());
            Emprestimo devolvido = service.buscarEmprestimoPorId(emprestimo.getId());

            assertNotNull(devolvido, "O empréstimo devolvido deveria ser encontrado.");
            assertTrue(devolvido.isClosed(), "O status de devolução deveria ser atualizado.");
        }

        @Test
        @DisplayName("Deve falhar ao tentar devolver livro sem empréstimo ativo")
        void shouldFailReturnLivroWithoutActiveEmprestimo() {
            Long emprestimoIdInvalido = 999L; // ID inválido

            Exception exception = assertThrows(Exception.class, () -> {
                service.realizarDevolucao(emprestimoIdInvalido);
            });

            String expectedMessage = "Empréstimo não encontrado";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage),
                    "A exceção lançada deveria estar relacionada à inexistência do empréstimo.");
        }
    }

    @Nested
    @DisplayName("Testes de Listagem de Empréstimos")
    class ListEmprestimosTests {

        @Test
        @DisplayName("Deve listar todos os empréstimos")
        void shouldListAllEmprestimos() {
            Emprestimo emprestimo1 = emprestimoMock();
            Emprestimo emprestimo2 = emprestimoMock();
            service.realizarEmprestimo(emprestimo1);
            service.realizarEmprestimo(emprestimo2);
            emprestimosCriados.add(emprestimo1.getId());
            emprestimosCriados.add(emprestimo2.getId());

            List<Emprestimo> emprestimos = service.listarEmprestimos();
            assertNotNull(emprestimos, "A lista de empréstimos não deveria ser nula.");
            assertTrue(emprestimos.size() >= 2, "A lista de empréstimos deveria conter pelo menos 2 registros.");
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Empréstimo")
    class BuscarEmprestimoTests {

        @Test
        @DisplayName("Deve buscar empréstimo por ID corretamente")
        void shouldFindEmprestimoById() {
            Emprestimo emprestimo = emprestimoMock();
            service.realizarEmprestimo(emprestimo);
            emprestimosCriados.add(emprestimo.getId());

            Emprestimo encontrado = service.buscarEmprestimoPorId(emprestimo.getId());
            assertNotNull(encontrado, "O empréstimo deveria ter sido encontrado pelo ID.");
            assertEquals(emprestimo.getLivro().getTitulo(), encontrado.getLivro().getTitulo(),
                    "Os títulos dos livros deveriam coincidir.");
        }
    }

    // Método para criar um mock de empréstimo
    private Emprestimo emprestimoMock() {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setCliente(clienteMock());
        emprestimo.setLivro(livroMock());
        emprestimo.setUsuario(usuarioMock());
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataHoraInicio(LocalDate.now().atStartOfDay());
        emprestimo.setDataHoraFim(LocalDate.now().plusDays(7).atStartOfDay());

        emprestimosCriados.add(emprestimo.getId());
        return emprestimo;
    }

    private Cliente clienteMock() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setTelefone("219392133");
        cliente.setCpf("999999999");
        cliente.setEmail("teste" + System.currentTimeMillis() + "@email.com");
        cliente.setSenha("123456");
        clienteService.salvarCliente(cliente);
        clientesCriados.add(cliente.getId());
        return cliente;
    }

    private Usuario usuarioMock() {
        Usuario usuario = new Usuario();
        usuario.setNome("John Doe");
        usuario.setEmail("abcd" + System.currentTimeMillis() + "@email.com");
        usuario.setSenha("password");
        usuario.setIsAdmin(0);
        usuarioService.salvarUsuario(usuario);
        usuariosCriados.add(usuario.getId());
        return usuario;
    }

    private Livro livroMock() {
        Livro livro = new Livro();
        livro.setTitulo("Livro de Teste");
        livro.setNumeroCopias(1);
        livro.setIsbn(123);
        livro.setDisponivel(true);
        livroService.salvarLivro(livro);
        livrosCriados.add(livro.getId());
        return livro;
    }

}
