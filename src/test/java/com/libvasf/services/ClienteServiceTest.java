package com.libvasf.services;

import com.libvasf.models.Cliente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    private static final Logger logger = Logger.getLogger(ClienteServiceTest.class.getName());

    @InjectMocks
    private ClienteService service;

    private final List<Long> clientesCriados = new java.util.ArrayList<>();

    @AfterEach
    void cleanUp() {
        for (Long clienteId : clientesCriados) {
            try {
                service.removerCliente(clienteId);
                logger.info("Cliente removido durante o cleanup: ID = " + clienteId);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao remover cliente durante o cleanup: ID = " + clienteId, e);
            }
        }
        clientesCriados.clear();
    }

    @Nested
    @DisplayName("Testes de Cadastro de Cliente")
    class CreateClienteTests {

        @Test
        @DisplayName("Deve criar um cliente com sucesso")
        void shouldCreateClienteWithSuccess() {
            Cliente cliente = clienteMock();

            try {
                service.salvarCliente(cliente);
                assertNotNull(cliente.getId(), "O ID do cliente deveria ter sido gerado.");
                logger.info("Cliente criado com sucesso: " + cliente);
                clientesCriados.add(cliente.getId());
            } catch (Exception e) {
                fail("Deveria ter adicionado sem exceção, mas retornou: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Deve falhar ao tentar criar cliente sem nome")
        void shouldFailCreatingClienteWithoutNome() {
            Cliente cliente = clienteMock();
            cliente.setNome(null);

            Exception exception = assertThrows(Exception.class, () -> {
                service.salvarCliente(cliente);
            });

            String expectedMessage = "Nome não pode ser nulo";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage),
                    "A exceção lançada deveria estar relacionada ao nome não nulo.");
        }
    }

    @Nested
    @DisplayName("Testes de Edição de Cliente")
    class EditClienteTests {

        @Test
        @DisplayName("Deve editar um cliente com sucesso")
        void shouldEditClienteWithSuccess() {
            Cliente cliente = clienteMock();
            service.salvarCliente(cliente);
            clientesCriados.add(cliente.getId());

            cliente.setNome("Nome Editado");
            service.editarCliente(cliente);

            Cliente atualizado = service.buscarClientePorId(cliente.getId());
            assertEquals("Nome Editado", atualizado.getNome(), "O nome deveria ter sido atualizado.");
        }
    }

    @Nested
    @DisplayName("Testes de Remoção de Cliente")
    class RemoveClienteTests {

        @Test
        @DisplayName("Deve remover um cliente com sucesso")
        void shouldRemoveClienteWithSuccess() {
            Cliente cliente = clienteMock();
            service.salvarCliente(cliente);
            clientesCriados.add(cliente.getId());

            service.removerCliente(cliente.getId());
            clientesCriados.remove(cliente.getId());

            Cliente removido = service.buscarClientePorId(cliente.getId());
            assertNull(removido, "O cliente deveria ter sido removido.");
        }
    }

    @Nested
    @DisplayName("Testes de Listagem de Cliente")
    class ListClienteTests {

        @Test
        @DisplayName("Deve listar clientes corretamente")
        void shouldListClientesCorrectly() {
            Cliente cliente1 = clienteMock();
            Cliente cliente2 = clienteMock();
            service.salvarCliente(cliente1);
            service.salvarCliente(cliente2);
            clientesCriados.add(cliente1.getId());
            clientesCriados.add(cliente2.getId());

            List<Cliente> clientes = service.listarClientes();
            assertNotNull(clientes, "A lista de clientes não deveria ser nula.");
            assertTrue(clientes.size() >= 2, "A lista de clientes deveria conter pelo menos 2 clientes.");
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Cliente")
    class BuscarClienteTests {

        @Test
        @DisplayName("Deve buscar cliente por ID corretamente")
        void shouldFindClienteById() {
            Cliente cliente = clienteMock();
            service.salvarCliente(cliente);
            clientesCriados.add(cliente.getId());

            Cliente encontrado = service.buscarClientePorId(cliente.getId());
            assertNotNull(encontrado, "O cliente deveria ter sido encontrado pelo ID.");
            assertEquals(cliente.getNome(), encontrado.getNome(), "Os nomes dos clientes deveriam coincidir.");
        }

        @Test
        @DisplayName("Deve falhar ao buscar cliente com ID inválido")
        void shouldFailFindClienteWithInvalidId() {
            Long invalidId = 999L;

            Cliente encontrado = service.buscarClientePorId(invalidId);
            assertNull(encontrado, "Nenhum cliente deveria ser encontrado com o ID inválido.");
        }
    }

    private Cliente clienteMock() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setCpf("12345678900");
        cliente.setEmail("cliente@teste.com");
        cliente.setTelefone("999999999");
        return cliente;
    }
}
