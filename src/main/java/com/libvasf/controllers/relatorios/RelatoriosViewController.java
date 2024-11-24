package com.libvasf.controllers.relatorios;

import com.libvasf.controllers.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RelatoriosViewController extends ViewController {

    @FXML
    private ComboBox<String> criteriosComboBox;

    @FXML
    private TableView<RelatorioRow> relatorioTable;

    // Dados fictícios para o exemplo
    private final ObservableList<RelatorioRow> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar opções do ComboBox
        criteriosComboBox.setItems(FXCollections.observableArrayList(
                "Livros Emprestados",
                "Livros Reservados",
                "Livros Disponíveis",
                "Histórico de Empréstimos"
        ));

        // Configurar a tabela
        configurarTabela();
    }

    private void configurarTabela() {
        // Limpa qualquer configuração anterior
        relatorioTable.getColumns().clear();

        // Configurar colunas dinamicamente
        TableColumn<RelatorioRow, String> coluna1 = new TableColumn<>("Título");
        coluna1.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<RelatorioRow, String> coluna2 = new TableColumn<>("Status");
        coluna2.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<RelatorioRow, String> coluna3 = new TableColumn<>("Cliente");
        coluna3.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        relatorioTable.getColumns().addAll(coluna1, coluna2, coluna3);
    }

    @FXML
    private void gerarRelatorio() {
        String criterio = criteriosComboBox.getValue();

        // Verifica se um critério foi selecionado
        if (criterio == null || criterio.isEmpty()) {
            System.err.println("Por favor, selecione um critério para gerar o relatório.");
            return;
        }

        // Limpar dados anteriores
        data.clear();

        // Obter os dados baseados no critério selecionado
        List<RelatorioRow> relatorioDados = buscarDadosRelatorio(criterio);
        data.addAll(relatorioDados);

        // Exibir os dados na tabela
        relatorioTable.setItems(data);
    }

    private List<RelatorioRow> buscarDadosRelatorio(String criterio) {
        // Substituir pelo código real para buscar os dados do relatório
        return switch (criterio) {
            case "Livros Emprestados" -> List.of(
                    new RelatorioRow("Livro A", "Emprestado", "Cliente X"),
                    new RelatorioRow("Livro B", "Emprestado", "Cliente Y")
            );
            case "Livros Reservados" -> List.of(
                    new RelatorioRow("Livro C", "Reservado", "Cliente Z")
            );
            case "Livros Disponíveis" -> List.of(
                    new RelatorioRow("Livro D", "Disponível", "Nenhum")
            );
            case "Histórico de Empréstimos" -> List.of(
                    new RelatorioRow("Livro A", "Devolvido", "Cliente X"),
                    new RelatorioRow("Livro B", "Devolvido", "Cliente Y")
            );
            default -> List.of();
        };
    }

    @FXML
    private void exportarParaPDF() {
        // Aqui você pode integrar com uma biblioteca como iText ou Apache PDFBox
        System.out.println("Exportando para PDF...");
        System.out.println("Funcionalidade de exportar para PDF ainda não implementada.");
    }

    @FXML
    private void exportarParaCSV() {
        if (data.isEmpty()) {
            System.err.println("Não há dados para exportar. Gere um relatório primeiro.");
            return;
        }

        try (FileWriter writer = new FileWriter("relatorio.csv")) {
            // Cabeçalhos do CSV
            writer.append("Título,Status,Cliente\n");

            // Adiciona os dados ao arquivo CSV
            for (RelatorioRow row : data) {
                writer.append(String.format("%s,%s,%s\n", row.getTitulo(), row.getStatus(), row.getCliente()));
            }

            System.out.println("Relatório exportado para CSV com sucesso: relatorio.csv");
        } catch (IOException e) {
            System.err.println("Erro ao exportar para CSV: " + e.getMessage());
        }
    }

    // Classe auxiliar para representar uma linha do relatório
    public static class RelatorioRow {
        private final String titulo;
        private final String status;
        private final String cliente;

        public RelatorioRow(String titulo, String status, String cliente) {
            this.titulo = titulo;
            this.status = status;
            this.cliente = cliente;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getStatus() {
            return status;
        }

        public String getCliente() {
            return cliente;
        }
    }
}
