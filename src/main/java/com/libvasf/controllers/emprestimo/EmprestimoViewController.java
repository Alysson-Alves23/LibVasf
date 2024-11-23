package com.libvasf.controllers.emprestimo;

import com.libvasf.models.Cliente;
import com.libvasf.models.Livro;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmprestimoViewController {

    @FXML
    private TextField pesquisarLivroField;

    @FXML
    private TextField pesquisarClienteField;

    @FXML
    private ComboBox<String> periodoComboBox;

    @FXML
    private TextField dataEntregaField;

    @FXML
    private Button salvarButton;

    private final EmprestimoController emprestimoController = new EmprestimoController();

    @FXML
    public void initialize() {
        // Inicializar o ComboBox com valores de período
        periodoComboBox.getItems().addAll("7 dias", "14 dias", "30 dias");

        salvarButton.setOnAction(event -> salvarEmprestimo());
    }

    private void salvarEmprestimo() {
        try {
            // Pegando valores dos campos
            String tituloLivro = pesquisarLivroField.getText();
            String nomeCliente = pesquisarClienteField.getText();
            String periodoSelecionado = periodoComboBox.getValue();
            String dataEntregaStr = dataEntregaField.getText();

            // Buscando dados no backend
            Livro livro = emprestimoController.buscarLivroPorTitulo(tituloLivro);
            Cliente cliente = emprestimoController.buscarClientePorNome(nomeCliente);

            if (livro == null || cliente == null) {
                throw new IllegalArgumentException("Livro ou cliente não encontrado.");
            }

            // Parse da data de entrega
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime dataHoraFim = LocalDateTime.parse(dataEntregaStr, formatter);

            // Registrando o empréstimo
            emprestimoController.realizarEmprestimo(1L, livro.getId(), LocalDateTime.now(), dataHoraFim, cliente.getId());
            System.out.println("Empréstimo realizado com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao realizar empréstimo: " + e.getMessage());
        }
    }
}

