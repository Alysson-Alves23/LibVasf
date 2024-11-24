package com.libvasf.controllers.emprestimo;

import com.libvasf.controllers.ViewController;
import com.libvasf.controllers.emprestimo.EmprestimoController;
import com.libvasf.models.Cliente;
import com.libvasf.models.Emprestimo;
import com.libvasf.services.ClienteService;
import com.libvasf.services.EmprestimoService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;
import java.util.List;
public class DevolucaoViewController extends ViewController {

    public Button buscarCliente;
    @FXML
    private TextField cpfField;

    @FXML
    private ListView<String> livrosListView;

    @FXML
    private TextField inicio;

    @FXML
    private TextField vencimento;

    @FXML
    private Button devolverButton;

    @FXML
    private Button sair;

    private final EmprestimoService emprestimoService = new EmprestimoService();
    private EmprestimoController emprestimoController = new EmprestimoController();
    private List<Emprestimo> emprestimosPendentes;

    @FXML
    public void initialize() {
        // Configurações iniciais
        buscarCliente.setOnAction(event -> pesquisarCliente());
        devolverButton.setOnAction(event -> devolverLivro());
        livrosListView.setOnMouseClicked(event -> carregarDetalhesEmprestimo());
        sair.setOnAction(event -> back());
    }

    @FXML
    private void pesquisarCliente() {
        try {
            String cpf = cpfField.getText();
            emprestimosPendentes = emprestimoService.buscarEmprestimosPorCpf(cpf);
            ClienteService clienteService = new ClienteService();
            Cliente cliente = clienteService.buscarClientePorCpf(cpf);
            if(cliente == null) {
                showAlert("Erro","Cliente não cadastrado!",Alert.AlertType.ERROR);
            }else{
                showAlert("Sucesso","Cliente: "+ cliente.getNome(),Alert.AlertType.INFORMATION);
            }
            if (emprestimosPendentes.isEmpty()) {
                showAlert("Aviso", "Nenhum empréstimo pendente encontrado para este cliente.", Alert.AlertType.INFORMATION);
                livrosListView.getItems().clear();
                return;
            }

            livrosListView.getItems().clear();
            for (Emprestimo emprestimo : emprestimosPendentes) {
                if (!emprestimo.isClosed()) {
                    livrosListView.getItems().add("Livro: " + emprestimo.getLivro().getTitulo() +
                            " | ID: " + emprestimo.getId());
                }
            }
        } catch (Exception e) {
            showAlert("Erro", "Erro ao pesquisar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void carregarDetalhesEmprestimo() {
        // Identifica o empréstimo selecionado
        int selectedIndex = livrosListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= emprestimosPendentes.size()) {
            return;
        }

        Emprestimo emprestimoSelecionado = emprestimosPendentes.get(selectedIndex);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        inicio.setText(emprestimoSelecionado.getDataHoraInicio().toLocalDate().format(formatter));
        vencimento.setText(emprestimoSelecionado.getDataHoraFim().toLocalDate().format(formatter));
    }

    private void devolverLivro() {
        try {
            // Identifica o empréstimo selecionado
            int selectedIndex = livrosListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex < 0 || selectedIndex >= emprestimosPendentes.size()) {
                showAlert("Erro", "Selecione um empréstimo para devolver.", Alert.AlertType.ERROR);
                return;
            }

            Emprestimo emprestimoSelecionado = emprestimosPendentes.get(selectedIndex);

            // Marca o empréstimo como encerrado
            emprestimoController.encerrarEmprestimo(emprestimoSelecionado.getId());
            showAlert("Sucesso", "Devolução realizada com sucesso.", Alert.AlertType.INFORMATION);

            // Atualiza a lista de empréstimos pendentes
            pesquisarCliente();

            // Limpa os campos de detalhes
            inicio.clear();
            vencimento.clear();
        } catch (Exception e) {
            showAlert("Erro", "Erro ao devolver o livro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
