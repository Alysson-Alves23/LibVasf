package com.libvasf.services;


import com.libvasf.models.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService service;

    @Nested
    class createUsuario {
        @Test
        @DisplayName("Should create a user with sucess")
        void shouldCreateUserWithSucess() {
            Usuario usuario = userMock();
            try {
                service.salvarUsuario(usuario);
            }catch (Exception e) {
                fail("should have added without exception, but it returned: " + e.getMessage());
            }

        }

    }
    private Usuario userMock(){
        Usuario usuario = new Usuario();

        usuario.setId(1L);
        usuario.setNome("John Doe");
        usuario.setCpf("12345678900");
        usuario.setEmail("john@example.com");
        usuario.setSenha("password");
        usuario.setIsAdmin(0);
        return usuario;
    }

}
