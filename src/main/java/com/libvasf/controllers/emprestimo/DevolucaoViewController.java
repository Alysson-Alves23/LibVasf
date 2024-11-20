package com.libvasf.controllers.emprestimo;

import com.libvasf.controllers.emprestimo.EmprestimoController;
import com.libvasf.models.Emprestimo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class DevolucaoViewController {

    @FXML
    private TextField cpfField;

    @FXML
    private ListView<String> livrosListView;

    @FXML
    private Button devolverButton;

    private final EmprestimoController emprestimoController = new EmprestimoController();

    @FXML
    public void initialize() {
        devolverButton.setOnAction(event -> realizarDevolucao());
    }

    private void realizarDevolucao() {
        try {
            String nomeCliente = cpfField.getText();

            // Instância do controller
            EmprestimoController emprestimoController = new EmprestimoController();

            // Buscar os empréstimos associados ao cliente
            List<Emprestimo> emprestimos = emprestimoController.buscarEmprestimosPorNomeCliente(nomeCliente);

            if (emprestimos == null || emprestimos.isEmpty()) {
                throw new IllegalArgumentException("Nenhum empréstimo encontrado para este cliente.");
            }

            // Supondo que você selecione o primeiro empréstimo da lista
            Emprestimo emprestimoSelecionado = emprestimos.get(0);

            // Realizar devolução
            emprestimoController.realizarDevolucao(emprestimoSelecionado.getId());
            System.out.println("Devolução realizada com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao realizar devolução: " + e.getMessage());
        }
    }

}
