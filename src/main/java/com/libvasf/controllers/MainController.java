package com.libvasf.controllers;
import com.libvasf.MainApplication;
import com.libvasf.models.Autor;
import com.libvasf.models.Categoria;
import com.libvasf.models.Livro;
import com.libvasf.services.AutorService;
import com.libvasf.services.CategoriaService;
import com.libvasf.services.LivroService;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController extends ViewController {
    private static final Logger logger = Logger.getLogger(MainController.class.getName());
    private final LivroService livroService = new LivroService();
    CategoriaService categoriaService = new CategoriaService();
    AutorService autorService = new AutorService();

    // Existing fields
    public Hyperlink cadastrarCliente;
    public Hyperlink gerenciarLivros;
    public Hyperlink gerenciarUsuario;
    public Hyperlink cadastrarLivro;
    public Hyperlink cadastrarUsuario;
    public Hyperlink sairLink;
    public ImageView sairImg;
    public Hyperlink devolucao;
    public Hyperlink emprestimo;

    // Search fields
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
    private TableView<Livro> livrosTable;
    @FXML
    private TableColumn<Livro, String> tituloColumn;
    @FXML
    private TableColumn<Livro, String> autorColumn;
    @FXML
    private TableColumn<Livro, String> isbnColumn;
    @FXML
    private TableColumn<Livro, String> categoriaColumn;
    @FXML
    private TableColumn<Livro, String> copiasColumn;

    @FXML
    private void initialize() {
        // Existing initializations
        devolucao.setOnMouseClicked(this::handleDevolucao);
        emprestimo.setOnMouseClicked(this::handleEmprestimo);
        gerenciarLivros.setOnMouseClicked(this::handleGerenciarLivros);
        gerenciarUsuario.setOnMouseClicked(this::handleGerenciarUsuarios);
        cadastrarLivro.setOnMouseClicked(this::handleCadastrarLivros);
        cadastrarCliente.setOnMouseClicked(this::handleCadastrarCliente);
        cadastrarUsuario.setOnMouseClicked(this::handleCadastrarUsuarios);
        sairLink.setOnMouseClicked(this::handleSair);
        sairImg.setOnMouseClicked(this::handleSair);

        // New search initialization
        botaoPesquisar.setOnMouseClicked(this::handlePesquisar);
        limparResultados();

        // Configure table columns
        tituloColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitulo()));
        isbnColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIsbn().toString()));
        copiasColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getNumeroCopias())));
        
        // Columns that need additional service calls
        autorColumn.setCellValueFactory(data -> {
            List<Autor> autores = autorService.listarAutorPorLivroId(data.getValue().getId());
            return new SimpleStringProperty(autores.isEmpty() ? "Não informado" : autores.get(0).getNome());
        });
        
        categoriaColumn.setCellValueFactory(data -> {
            List<Categoria> categorias = categoriaService.listarCategoriasPorIdLivro(data.getValue().getId());
            return new SimpleStringProperty(categorias.isEmpty() ? "Não informado" : categorias.get(0).getNome());
        });

        // Add selection listener to table
        livrosTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Update details when row is selected
                updateDetailView(newSelection);
            }
        });
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
                livrosTable.getItems().clear();
                return;
            }

            exibirResultado(livrosEncontrados);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao pesquisar livros", e);
            showAlert("Erro", "Erro ao realizar a pesquisa", Alert.AlertType.ERROR);
        }
    }

    private void exibirResultado(List<Livro> livros) {
        livrosTable.getItems().clear();
        livrosTable.getItems().addAll(livros);
    }

    private void limparResultados() {
        nomeLabel.setText("Nome: ");
        autorLabel.setText("Autor: ");
        isbnLabel.setText("ISBN: ");
        categoriaLabel.setText("Categoria: ");
        numeroCopiasLabel.setText("N° de cópias: ");
    }

    // Add this method
    private void updateDetailView(Livro livro) {
        List<Autor> autores = autorService.listarAutorPorLivroId(livro.getId());
        List<Categoria> categorias = categoriaService.listarCategoriasPorIdLivro(livro.getId());
        
        nomeLabel.setText("Nome: " + livro.getTitulo());
        autorLabel.setText("Autor: " + (autores.isEmpty() ? "Não informado" : autores.get(0).getNome()));
        isbnLabel.setText("ISBN: " + livro.getIsbn());
        categoriaLabel.setText("Categoria: " + (categorias.isEmpty() ? "Não informado" : categorias.get(0).getNome()));
        numeroCopiasLabel.setText("N° de cópias: " + livro.getNumeroCopias());
    }

    // Existing methods remain unchanged
    // ... (keep all other existing methods)
    @FXML
    private void handleCadastrarCliente(MouseEvent event) {
        goTo("Libvasf","client-register");
    }
    @FXML
    private void handleDevolucao(MouseEvent event) {
        goTo("Libvasf","return-book-view");

    }
    @FXML
    private void handleEmprestimo(MouseEvent event) {
        goTo("Libvasf","lend-book-view");

    }
    @FXML
    private void handleGerenciarLivros(MouseEvent event) {
        goTo("Libvasf","gerenciar-categoria-view");
    }
    @FXML
    private void handleCadastrarLivros(MouseEvent event) {
        goTo("Libvasf","book-register-view");
    }
    @FXML
    private void handleGerenciarUsuarios(MouseEvent event) {
        if(MainApplication.getCurrentUser().getIsAdmin() != 0) {
            goTo("Gerenciar Usuário","list-user-view");
        }else{
            showAlert("Erro","Você não tem premissão para acessar essa função!", Alert.AlertType.ERROR);
        }

    }
    @FXML
    private void handleCadastrarUsuarios(MouseEvent event) {
        if(MainApplication.getCurrentUser().getIsAdmin() != 0) {
            goTo("Cadastrar Usuário", "create-user-view");
        }else{
            showAlert("Erro","Você não tem premissão para acessar essa função!", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void handleSair(MouseEvent event) {
        goTo("Libvasf","login-view");

    }
}
