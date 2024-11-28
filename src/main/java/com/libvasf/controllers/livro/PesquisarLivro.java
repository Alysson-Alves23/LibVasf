package com.libvasf.controllers.livro;

import com.libvasf.controllers.ViewController;
import com.libvasf.models.Livro;
import com.libvasf.services.LivroService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PesquisarLivro extends ViewController {

    private static final Logger logger = Logger.getLogger(PesquisarLivro.class.getName());
    private final LivroService livroService = new LivroService();

    @FXML
    private TextField pesquisarLivros;
    @FXML
    private Button botaoPesquisar;
    @FXML
    private TextField autor;
    @FXML
    private TextField editora;
    @FXML
    private TextField ano;
    @FXML
    private CheckBox ficcao;
    @FXML
    private CheckBox didaticos;
    @FXML
    private CheckBox academicos;
    @FXML
    private CheckBox literatura;
    @FXML
    private CheckBox cientificos;
    @FXML
    private CheckBox ordenarAutor;
    @FXML
    private CheckBox ordenarEditora;
    @FXML
    private CheckBox ordenarAno;
    @FXML
    private CheckBox ordenarCategoria;
    @FXML
    private Label nomeLabel;
    @FXML
    private Label autorLabel;
    @FXML
    private Label isbnLabel;
    @FXML
    private Label categoriaLabel;
    @FXML
    private Label numeroCopiasLabel;

    @FXML
    public void initialize() {
        System.out.println("Initialize method called");
        logger.info("Initialize method called");

        if (botaoPesquisar == null) {
            System.out.println("botaoPesquisar is null!");
            return;
        }

        botaoPesquisar.setOnMouseClicked(this::handlePesquisar);
        limparResultados();
    }

    private void handlePesquisar(MouseEvent event) {
        String termoPesquisa = pesquisarLivros.getText().trim();
        System.out.println("Termo de pesquisa: " + termoPesquisa);
        if (termoPesquisa.isEmpty()) {
            showAlert("Aviso", "Digite um termo para pesquisar", Alert.AlertType.WARNING);
            return;
        }

        try {
            List<Livro> livrosEncontrados = livroService.buscarPorTitulo(termoPesquisa);
            
            if (livrosEncontrados.isEmpty()) {
                showAlert("Informação", "Nenhum livro encontrado", Alert.AlertType.INFORMATION);
                limparResultados();
                return;
            }

            // Exibe o primeiro livro encontrado
            Livro livro = livrosEncontrados.get(0);
            exibirResultado(livro);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao pesquisar livros", e);
            showAlert("Erro", "Erro ao realizar a pesquisa", Alert.AlertType.ERROR);
        }
    }

    private void exibirResultado(Livro livro) {
        nomeLabel.setText("Nome: " + livro.getTitulo());
        isbnLabel.setText("ISBN: " + livro.getIsbn());
        numeroCopiasLabel.setText("N° de cópias: " + livro.getNumeroCopias());
        
        // Verifica se há publicações associadas ao livro
        if (!livro.getPublicacoes().isEmpty()) {
            autorLabel.setText("Autor: " + livro.getPublicacoes().get(0).getAutor().getNome());
        } else {
            autorLabel.setText("Autor: Não informado");
        }
    }

    private void limparResultados() {
        nomeLabel.setText("Nome: ");
        autorLabel.setText("Autor: ");
        isbnLabel.setText("ISBN: ");
        categoriaLabel.setText("Categoria: ");
        numeroCopiasLabel.setText("N° de cópias: ");
    }

    public void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleVoltar() {
        goTo("Libvasf", "main-view");
    }
}