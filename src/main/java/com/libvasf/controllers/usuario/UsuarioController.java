package com.libvasf.controllers.usuario;

import com.libvasf.MainApplication;
import com.libvasf.models.Usuario;
import com.libvasf.services.UsuarioService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioController {

    private static final Logger logger = Logger.getLogger(UsuarioController.class.getName());
    private static final UsuarioService usuarioService = new UsuarioService();

    /**
     * Método para realizar o login de um usuário.
     *
     * @param email Nome do usuário.
     * @param senha   Senha do usuário.
     * @return true se o login for bem-sucedido, false caso contrário.
     */
    public static boolean login(String email, String senha) {
        try {
            final Usuario user = usuarioService.buscarUsuarioPorEmail(email);

            if (user != null && user.getSenha().equals(senha)) {
                MainApplication.setCurrentUser(user);
                logger.info("Login bem-sucedido para o usuário: " + email);
                return true;
            } else {
                logger.warning("Falha no login para o usuário: " + email);
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro durante o login do usuário: " + email, e);
            return false;
        }
    }

    /**
     * Método para cadastrar um novo usuário.
     *
     * @param usuario Nome do usuário.
     * @param senha   Senha do usuário.
     * @param email   Email do usuário.
     * @param isAdmin Indica se o usuário é administrador (1 para sim, 0 para não).
     */
    public static boolean cadastrarUsuario(String usuario, String senha, String email, int isAdmin) {
        try {
            final Usuario user = new Usuario();
            user.setNome(usuario);
            user.setEmail(email);
            user.setSenha(senha);
            user.setIsAdmin(isAdmin);
            usuarioService.salvarUsuario(user);
            logger.info("Usuário cadastrado com sucesso: " + usuario);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao cadastrar o usuário: " + usuario, e);
        }
        return false;
    }
    public static void excluirUsuario(long id) {
        try{
            usuarioService.removerUsuario(id);
        }catch (Exception e){
            throw  e;
        }
    }
    /**
     * Método para realizar o logout do usuário atual.
     */
    public static void logout() {
        try {
            final Usuario currentUser = MainApplication.getCurrentUser();
            if (currentUser != null) {
                MainApplication.setCurrentUser(null);
                logger.info("Usuário deslogado: " + currentUser.getNome());
            } else {
                logger.warning("Tentativa de logout sem usuário logado.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro durante o logout.", e);
        }
    }
}
