package com.libvasf.controllers.usuario;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private void initialize() {
        // Inicializador, se necessário
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

        // Exemplo de lógica de autenticação (pode ser substituído pela real)
        if (username.equals("admin") && password.equals("admin")) {
            showAlert("Sucesso", "Login realizado com sucesso!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erro", "Username ou senha incorretos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleRegisterButtonAction(MouseEvent event) {
        // Implementação do redirecionamento para tela de cadastro
        showAlert("Cadastro", "Direcionando para a tela de cadastro.", Alert.AlertType.INFORMATION);
        // Aqui você pode carregar uma nova tela ou realizar outras ações
    }

    @FXML
    private void handleForgotPassword(MouseEvent event) {
        // Exemplo de ação para recuperar senha
        showAlert("Recuperar senha", "Instruções para recuperação de senha foram enviadas.", Alert.AlertType.INFORMATION);
    }

    // Método auxiliar para exibir mensagens de alerta
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
