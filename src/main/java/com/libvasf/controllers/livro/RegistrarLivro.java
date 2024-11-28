package com.libvasf.controllers.livro;

import com.libvasf.controllers.ViewController;
import com.libvasf.models.Autor;
import com.libvasf.models.Categoria;
import com.libvasf.controllers.livro.LivroController;
import com.libvasf.services.AutorService;
import com.libvasf.services.CategoriaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarLivro extends ViewController {

    @FXML
    public TextField titulo;

    @FXML
    public TextField isbn;

    @FXML
    public ChoiceBox<Categoria> categoria;

    @FXML
    public TextField copias;

    @FXML
    public TextField autor;

    @FXML
    public TextField ano;

    @FXML
    public Button salvarButton;

    @FXML
    public Button voltar;

    private final LivroController livroController = new LivroController();
    private static final Logger logger = Logger.getLogger(RegistrarLivro.class.getName());

    @FXML
    public void initialize() {
        carregarCategorias();
        salvarButton.setOnMouseClicked(this::salvarLivro);
        voltar.setOnMouseClicked(this::sair);
    }

    private void carregarCategorias() {
        try {
            CategoriaService categoriaService = new CategoriaService();
            List<Categoria> categorias = categoriaService.listarCategorias();
            categoria.setItems(FXCollections.observableArrayList(categorias));

            // Configurando o ChoiceBox para exibir apenas o nome da categoria
            categoria.setConverter(new javafx.util.StringConverter<Categoria>() {
                @Override
                public String toString(Categoria categoria) {
                    return categoria != null ? categoria.getNome() : ""; // Exibe o nome da categoria
                }

                @Override
                public Categoria fromString(String string) {
                    return null; // Não usado, pois o ChoiceBox trabalha diretamente com os objetos
                }
            });

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao carregar categorias", e);
            showAlert("Erro", "Não foi possível carregar as categorias.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void salvarLivro(MouseEvent event) {
        try {
            String tituloLivro = titulo.getText();
            String autorLivro = autor.getText();
            Integer isbnLivro = Integer.parseInt(isbn.getText());
            Categoria categoriaSelecionada = categoria.getValue();
            Integer numeroCopias = Integer.parseInt(copias.getText());
            Integer anoPublicacao = Integer.parseInt(ano.getText());

            if (tituloLivro.isEmpty() || categoriaSelecionada == null || autorLivro.isEmpty()) {
                showAlert("Campos obrigatórios", "Preencha todos os campos obrigatórios.", Alert.AlertType.WARNING);
                return;
            }
            Autor autor = AutorService.buscarAutorPorNome(autorLivro);
            System.out.println(autor);
            livroController.salvarLivro(
                    tituloLivro,
                    isbnLivro,
                    true, // Assumindo que o livro está disponível no cadastro inicial
                    numeroCopias,
                    categoriaSelecionada.getId(), // Obtemos o ID da categoria
                    autorLivro,
                    anoPublicacao
            );

            showAlert("Sucesso", "Livro cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            limparCampos();
        } catch (NumberFormatException e) {
            showAlert("Erro de validação", "Certifique-se de que os campos ISBN, Cópias, Autor e Ano sejam numéricos.", Alert.AlertType.WARNING);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao salvar o livro", e);
            showAlert("Erro", "Ocorreu um erro ao tentar salvar o livro.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void sair(MouseEvent event) {
        goTo("libvasf","main-view");
    }

    private void limparCampos() {
        titulo.clear();
        isbn.clear();
        categoria.setValue(null);
        copias.clear();
        autor.clear();
        ano.clear();
    }
}
