package com.libvasf.controllers.emprestimo;

import com.libvasf.MainApplication;
import com.libvasf.controllers.ViewController;
import com.libvasf.models.Cliente;
import com.libvasf.models.Livro;
import com.libvasf.services.ClienteService;
import com.libvasf.services.LivroService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

public class EmprestimoViewController extends ViewController {

    public Button pesquisarCliente;
    public Button pesquisarLivro;
    public Button sair;
    @FXML
    private TextField pesquisarLivroField;

    @FXML
    private TextField pesquisarClienteField;

    @FXML
    private ComboBox<Integer> periodoComboBox;

    @FXML
    private Button salvarButton;



    private final EmprestimoController emprestimoController = new EmprestimoController();
    private final LivroService livroService = new LivroService();
    private final ClienteService clienteService = new ClienteService();
    @FXML
    public void initialize() {
        // Inicializar o ComboBox com valores de período
        periodoComboBox.getItems().addAll(7,14,30);

        sair.setOnMouseClicked(this::voltar);
        salvarButton.setOnMouseClicked(this::salvarEmprestimo);
        pesquisarCliente.setOnMouseClicked(this::verificarCliente);
        pesquisarLivro.setOnMouseClicked(this::verificarISBN);
    }

    private void voltar(MouseEvent event) {
        back();
    }

    private void verificarISBN(MouseEvent event) {
        try {


            Livro livro = livroService.buscarLivroPorId(Long.valueOf(pesquisarLivroField.getText()));

            if (livro != null) {
                showAlert("Sucesso", "Livro encontrado: " + livro.getTitulo(), Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erro", "Livro não encontrado.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Erro", "Erro ao buscar Livro: " + e.getMessage(), Alert.AlertType.ERROR);
        }


    }

    private void salvarEmprestimo(MouseEvent event) {
        try {
            // Pegando valores dos campos
            String cpfCliente = pesquisarClienteField.getText();
            Integer periodoSelecionado = periodoComboBox.getValue();
            if(cpfCliente == null || periodoSelecionado == null || pesquisarLivroField.getText() == null) {
                showAlert("Erro", "Preencha Todos os Campos", Alert.AlertType.ERROR);
            }

            // Buscando dados no backend
            Livro livro = livroService.buscarLivroPorId(Long.valueOf(pesquisarLivroField.getText()));
            Cliente cliente = clienteService.buscarClientePorCpf(cpfCliente);

            if (livro == null) {
                showAlert("Erro", "Livro não encontrado.", Alert.AlertType.ERROR);
                return;
            }

            if (cliente == null) {
                showAlert("Erro", "Cliente não encontrado.", Alert.AlertType.ERROR);
                return;
            }
            // Calculando a data de devolução
            LocalDateTime dataAtual = LocalDateTime.now();
            LocalDateTime dataDevolucao = dataAtual.plusDays(periodoSelecionado);

            // Formatando a data de devolução
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataDevolucaoFormatada = dataDevolucao.format(formatter);


            // Registrando o empréstimo
            emprestimoController.realizarEmprestimo(MainApplication.getCurrentUser().getId(), livro.getId() , dataAtual, dataDevolucao, cliente.getId());
            showAlert(
                    "Sucesso",
                    "Empréstimo realizado com sucesso.\nData de Devolução: " + dataDevolucaoFormatada,
                    Alert.AlertType.INFORMATION
            );
            back();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao realizar empréstimo", e);
            showAlert("Erro", "Erro ao realizar empréstimo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void verificarCliente(MouseEvent event) {
        try {
            String cpfCliente = pesquisarClienteField.getText();

            if (cpfCliente == null || cpfCliente.isEmpty()) {
                showAlert("Erro", "Por favor, informe um CPF válido.", Alert.AlertType.ERROR);
                return;
            }

            Cliente cliente = clienteService.buscarClientePorCpf(cpfCliente);

            if (cliente != null) {
                showAlert("Sucesso", "Cliente encontrado: " + cliente.getNome(), Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erro", "Cliente não encontrado.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Erro", "Erro ao buscar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
