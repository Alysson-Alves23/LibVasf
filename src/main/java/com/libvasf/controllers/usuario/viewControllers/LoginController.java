package com.libvasf.controllers.usuario.viewControllers;

import com.libvasf.controllers.ViewController;
import com.libvasf.controllers.usuario.UsuarioController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController extends ViewController {


    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink forgotPassword;

    @FXML
    private void initialize() {

        loginButton.setOnMouseClicked(this::handleLoginButtonAction);
        forgotPassword.setOnMouseClicked(this::handleForgotPassword);
    }

    @FXML
    private void handleLoginButtonAction(MouseEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Verificação simples de campos vazios
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Erro", "Por favor, preencha todos os campos!", Alert.AlertType.ERROR);
            return;
        }

        try {
            if (UsuarioController.login(username, password)) {
                // Navegar para a tela de Dashboard
                goTo("Home","main-view");
            } else {
                showAlert("Erro", "Usuário ou senha inválidos.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro durante o login: ", e);
            showAlert("Erro", "Ocorreu um erro durante o login. Tente novamente.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleForgotPassword(MouseEvent event) {
        // Exemplo de ação para recuperar senha
        showAlert("Recuperar senha", "Instruções para recuperação de senha foram enviadas.", Alert.AlertType.INFORMATION);
    }


}
