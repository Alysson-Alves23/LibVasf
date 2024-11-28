package com.libvasf.controllers.livro;

import com.libvasf.controllers.ViewController;
import com.libvasf.models.Livro;
import com.libvasf.services.LivroService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PesquisaLivro extends ViewController {

    @FXML
    private TextField pesquisarLivroField;
    @FXML
    private Button pesquisarLivro;
    @FXML
    private TextField pesquisarClienteField;
    @FXML
    private Button pesquisarCliente;
    @FXML
    private ComboBox<String> periodoComboBox;
    @FXML
    private Button sair;
    @FXML
    private Button salvarButton;

    private static final Logger logger = Logger.getLogger(PesquisaLivro.class.getName());
    private final LivroService livroService = new LivroService();

    @FXML
    private void initialize() {
        pesquisarLivro.setOnMouseClicked(event -> handlePesquisarLivro());
        sair.setOnMouseClicked(event -> handleSair());
        salvarButton.setOnMouseClicked(event -> handleSalvar());
    }

    private void handlePesquisarLivro() {
        String tituloLivro = pesquisarLivroField.getText();
        if (tituloLivro.isEmpty()) {
            showAlert("Erro", "Por favor, insira o código do livro.", Alert.AlertType.ERROR);
            return;
        }

        try {
            Livro livro = livroService.buscarLivroPorTitulo(tituloLivro);
            if (livro != null) {
                showAlert("Sucesso", "Livro encontrado: " + livro.getTitulo(), Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erro", "Livro não encontrado.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar o livro: " + tituloLivro, e);
            showAlert("Erro", "Ocorreu um erro ao buscar o livro.", Alert.AlertType.ERROR);
        }
    }

    private void handleSair() {
        goTo("Libvasf", "main-view");
    }

    private void handleSalvar() {
        // Implementar lógica para salvar o empréstimo
        showAlert("Sucesso", "Empréstimo salvo com sucesso!", Alert.AlertType.INFORMATION);
    }

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/libvasf/views/lend-book-view.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Pesquisar Livro");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}