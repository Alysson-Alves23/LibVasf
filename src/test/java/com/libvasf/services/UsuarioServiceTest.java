package com.libvasf.services;

import com.libvasf.models.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)
@Transactional
public class UsuarioServiceTest {

    private static final Logger logger = Logger.getLogger(UsuarioServiceTest.class.getName());

    @InjectMocks
    private UsuarioService service;

    // Lista para rastrear os usuários criados durante os testes
    private final List<Long> usuariosCriados = new java.util.ArrayList<>();

    @AfterEach
    void cleanUp() {
//        // Remove os usuários criados durante os testes
//        for (Long userId : usuariosCriados) {
//            try {
//                service.removerUsuario(userId);
//                logger.info("Usuário removido durante o cleanup: ID = " + userId);
//            } catch (Exception e) {
//                logger.log(Level.SEVERE, "Erro ao remover usuário durante o cleanup: ID = " + userId, e);
//            }
//        }
//        usuariosCriados.clear();
    }

    @Nested
    @DisplayName("Testes de Criação de Usuário")
    class CreateUsuarioTests {

        @Test
        @DisplayName("Deve criar um usuário com sucesso")
        void shouldCreateUserWithSuccess() {
            Usuario usuario = userMock();

            try {
                service.salvarUsuario(usuario);
                assertNotNull(usuario.getId(), "O ID do usuário deveria ter sido gerado.");
                logger.info("Usuário criado com sucesso: " + usuario);
                // Rastreia o usuário para limpeza posterior
                usuariosCriados.add(usuario.getId());
            } catch (Exception e) {
                fail("Deveria ter adicionado sem exceção, mas retornou: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar criar usuário sem email")
        void shouldFailCreatingUserWithoutEmail() {
            Usuario usuario = userMock();
            usuario.setEmail(null); // Remove o email para simular erro

            Exception exception = assertThrows(Exception.class, () -> {
                service.salvarUsuario(usuario);
            });

            String expectedMessage = "Propriedades do banco de dados não foram carregadas corretamente";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage) || actualMessage.contains("not-null"),
                    "A exceção lançada deveria estar relacionada a email não nulo.");
            logger.warning("Exceção esperada ao criar usuário sem email: " + actualMessage);
        }
    }

    @Nested
    @DisplayName("Testes de Listagem de Usuários")
    class ListUsuariosTests {

        @Test
        @DisplayName("Deve listar usuários corretamente")
        void shouldListUsersCorrectly() {
            Usuario usuario1 = userMock("user1_" + System.currentTimeMillis() + "@example.com");
            Usuario usuario2 = userMock("user2_" + System.currentTimeMillis() + "@example.com");

            try {
                service.salvarUsuario(usuario1);
                service.salvarUsuario(usuario2);
                usuariosCriados.add(usuario1.getId());
                usuariosCriados.add(usuario2.getId());

                List<Usuario> usuarios = service.listarUsuarios();
                assertNotNull(usuarios, "A lista de usuários não deveria ser nula.");
                assertTrue(usuarios.size() >= 2, "A lista de usuários deveria conter pelo menos 2 usuários.");
                logger.info("Listagem de usuários realizada com sucesso. Total: " + usuarios.size());
            } catch (Exception e) {
                fail("Deveria ter listado usuários sem exceção, mas retornou: " + e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Usuário")
    class BuscarUsuarioTests {

        @Test
        @DisplayName("Deve buscar usuário por ID corretamente")
        void shouldFindUserById() {
            Usuario usuario = userMock();

            try {
                service.salvarUsuario(usuario);
                usuariosCriados.add(usuario.getId());

                Usuario encontrado = service.buscarUsuarioPorId(usuario.getId());
                assertNotNull(encontrado, "O usuário deveria ter sido encontrado pelo ID.");
                assertEquals(usuario.getNome(), encontrado.getNome(), "Os nomes dos usuários deveriam coincidir.");
                logger.info("Usuário encontrado pelo ID: " + encontrado);
            } catch (Exception e) {
                fail("Deveria ter buscado usuário por ID sem exceção, mas retornou: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Deve buscar usuário por Email corretamente")
        void shouldFindUserByEmail() {
            String uniqueEmail = "unique_" + System.currentTimeMillis() + "@example.com";
            Usuario usuario = userMock(uniqueEmail);

            try {
                service.salvarUsuario(usuario);
                usuariosCriados.add(usuario.getId());

                Usuario encontrado = service.buscarUsuarioPorEmail(usuario.getEmail());
                assertNotNull(encontrado, "O usuário deveria ter sido encontrado pelo nome.");
                assertEquals(usuario.getEmail(), encontrado.getEmail(), "Os emails dos usuários deveriam coincidir.");
                logger.info("Usuário encontrado pelo nome: " + encontrado);
            } catch (Exception e) {
                fail("Deveria ter buscado usuário por nome sem exceção, mas retornou: " + e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Testes de Remoção de Usuário")
    class RemoverUsuarioTests {

        @Test
        @DisplayName("Deve remover usuário corretamente")
        void shouldRemoveUserCorrectly() {
            Usuario usuario = userMock();

            try {
                service.salvarUsuario(usuario);
                Long userId = usuario.getId();
                assertNotNull(userId, "O ID do usuário deveria ter sido gerado.");
                usuariosCriados.add(userId);

                service.removerUsuario(userId);
                usuariosCriados.remove(userId); // Usuário já foi removido

                Usuario removido = service.buscarUsuarioPorId(userId);
                assertNull(removido, "O usuário deveria ter sido removido e não encontrado.");
                logger.info("Usuário removido corretamente: ID = " + userId);
            } catch (Exception e) {
                fail("Deveria ter removido usuário sem exceção, mas retornou: " + e.getMessage());
            }
        }
    }

    private Usuario userMock() {
        return userMock("test_" + System.currentTimeMillis() + "@example.com");
    }


    private Usuario userMock(String email) {
        Usuario usuario = new Usuario();
        usuario.setNome("John Doe");
        usuario.setEmail(email);
        usuario.setSenha("password");
        usuario.setIsAdmin(0);
        return usuario;
    }
}
