package com.libvasf.controllers;
import com.libvasf.MainApplication;
import com.libvasf.models.Autor;
import com.libvasf.models.Categoria;
import com.libvasf.models.Livro;
import com.libvasf.models.Publicacao;
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
import java.util.stream.Collectors;
import java.util.ArrayList;

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
        String autorPesquisa = autor.getText().trim();
        String anoPesquisa = ano.getText().trim();
        
        try {
            List<Livro> livrosEncontrados = livroService.buscarPorTitulo(termoPesquisa);
            
            // Apply filters
            livrosEncontrados = aplicarFiltros(livrosEncontrados);
            
            // Apply sorting
            livrosEncontrados = aplicarOrdenacao(livrosEncontrados);
            
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

    private List<Livro> aplicarFiltros(List<Livro> livros) {
        return livros.stream()
            .filter(livro -> {
                // Filter by autor if specified
                if (!autor.getText().trim().isEmpty()) {
                    List<Autor> autores = autorService.listarAutorPorLivroId(livro.getId());
                    boolean autorMatch = autores.stream()
                        .anyMatch(a -> a.getNome().toLowerCase()
                        .contains(autor.getText().toLowerCase()));
                    if (!autorMatch) return false;
                }
                
                // Filter by ano if specified
                if (!ano.getText().trim().isEmpty()) {
                    boolean anoMatch = livro.getPublicacoes().stream()
                        .anyMatch(p -> p.getAno().toString().equals(ano.getText()));
                    if (!anoMatch) return false;
                }
                
                // Filter by categoria
                if (ficcao.isSelected() || didaticos.isSelected() || 
                    academicos.isSelected() || literatura.isSelected() || 
                    cientificos.isSelected()) {
                    
                    List<Categoria> categorias = categoriaService.listarCategoriasPorIdLivro(livro.getId());
                    boolean categoriaMatch = categorias.stream()
                        .anyMatch(c -> (ficcao.isSelected() && c.getNome().equals("ficcao")) ||
                                     (didaticos.isSelected() && c.getNome().equals("didaticos")) ||
                                     (academicos.isSelected() && c.getNome().equals("academicos")) ||
                                     (literatura.isSelected() && c.getNome().equals("literatura")) ||
                                     (cientificos.isSelected() && c.getNome().equals("cientificos")));
                    if (!categoriaMatch) return false;
                }
                
                return true;
            })
            .collect(Collectors.toList());
    }

    private List<Livro> aplicarOrdenacao(List<Livro> livros) {
        List<Livro> livrosOrdenados = new ArrayList<>(livros);
        
        if (ordenarAutor.isSelected()) {
            livrosOrdenados.sort((l1, l2) -> {
                String autor1 = autorService.listarAutorPorLivroId(l1.getId())
                    .stream().map(Autor::getNome).findFirst().orElse("");
                String autor2 = autorService.listarAutorPorLivroId(l2.getId())
                    .stream().map(Autor::getNome).findFirst().orElse("");
                return autor1.compareTo(autor2);
            });
        }
        
        if (ordenarAno.isSelected()) {
            livrosOrdenados.sort((l1, l2) -> {
                Integer ano1 = l1.getPublicacoes().stream()
                    .map(Publicacao::getAno).findFirst().orElse(0);
                Integer ano2 = l2.getPublicacoes().stream()
                    .map(Publicacao::getAno).findFirst().orElse(0);
                return ano1.compareTo(ano2);
            });
        }
        
        if (ordenarCategoria.isSelected()) {
            livrosOrdenados.sort((l1, l2) -> {
                String cat1 = categoriaService.listarCategoriasPorIdLivro(l1.getId())
                    .stream().map(Categoria::getNome).findFirst().orElse("");
                String cat2 = categoriaService.listarCategoriasPorIdLivro(l2.getId())
                    .stream().map(Categoria::getNome).findFirst().orElse("");
                return cat1.compareTo(cat2);
            });
        }
        
        return livrosOrdenados;
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
