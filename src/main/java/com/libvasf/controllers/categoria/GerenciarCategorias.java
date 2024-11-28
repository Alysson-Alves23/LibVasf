package com.libvasf.controllers.categoria;

import com.libvasf.controllers.ViewController;
import com.libvasf.models.Categoria;
import com.libvasf.services.CategoriaService;
import com.libvasf.services.LivroCategoriaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class GerenciarCategorias extends ViewController {

    public TextField pesquisarField;
    public ListView<Categoria> categoriasListView;
    public Button pesquisar;
    public Button excluir;
    private final CategoriaService categoriaService = new CategoriaService();
    private final LivroCategoriaService livroCategoriaService = new LivroCategoriaService();
    public Button sair;
    public Button adicionarCategoria;
    public TextField nomeField;

    @FXML
    private void initialize() {
        // Carrega todas as categorias ao iniciar a tela
        carregarCategorias("");

        // Configuração do botão "Pesquisar"
        pesquisar.setOnMouseClicked(this::pesquisarCategorias);

        // Configuração do botão "Adicionar"
        adicionarCategoria.setOnMouseClicked(this::adicionarCategoria);

        // Configuração do botão "Excluir"
        excluir.setOnMouseClicked(this::excluirCategoria);

        sair.setOnMouseClicked(this::voltar);
    }

    private void voltar(MouseEvent event) {
        back();
    }

    private void carregarCategorias(String filtro) {
        try {
            // Carregar categorias com ou sem filtro
            List<Categoria> categorias;
            if (filtro == null || filtro.isEmpty()) {
                categorias = categoriaService.listarCategorias(); // Carrega todas as categorias
            } else {
                categorias = categoriaService.buscarCategoriasPorNome(filtro); // Busca com base no filtro
            }

            // Converte a lista de categorias em uma ObservableList
            ObservableList<Categoria> categoriasObservableList = FXCollections.observableArrayList(categorias);
            categoriasListView.setItems(categoriasObservableList);

            // Define uma fábrica de células personalizada para exibir apenas o nome da categoria
            categoriasListView.setCellFactory(param -> new ListCell<Categoria>() {
                @Override
                protected void updateItem(Categoria categoria, boolean empty) {
                    super.updateItem(categoria, empty);
                    setText(empty || categoria == null ? null : categoria.getNome());
                }
            });

        } catch (Exception e) {
            logger.severe("Erro ao carregar categorias: " + e.getMessage());
            showAlert("Erro", "Não foi possível carregar as categorias.", Alert.AlertType.ERROR);
        }
    }


    private void pesquisarCategorias(MouseEvent event) {
        String filtro = pesquisarField.getText().trim();
        carregarCategorias(filtro);
    }

    private void adicionarCategoria(MouseEvent event) {
        String nomeCategoria = nomeField.getText().trim();
        if (nomeCategoria.isEmpty()) {
            showAlert("Erro", "O nome da categoria não pode estar vazio.", Alert.AlertType.WARNING);
            return;
        }

        try {
            Categoria novaCategoria = new Categoria();
            novaCategoria.setNome(nomeCategoria);
            categoriaService.salvarCategoria(novaCategoria);
            showAlert("Sucesso", "Categoria adicionada com sucesso.", Alert.AlertType.INFORMATION);
            carregarCategorias(""); // Atualiza a lista
        } catch (Exception e) {
            logger.severe("Erro ao adicionar categoria: " + e.getMessage());
            showAlert("Erro", "Não foi possível adicionar a categoria.", Alert.AlertType.ERROR);
        }
    }

    private void excluirCategoria(MouseEvent event) {
        Categoria categoriaSelecionada = categoriasListView.getSelectionModel().getSelectedItem();
        if (categoriaSelecionada == null) {
            showAlert("Erro", "Nenhuma categoria selecionada.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int numeroDeLivros = livroCategoriaService.listarLivroPorCategoria(categoriaSelecionada.getId()).size();
            String mensagem;
            if (numeroDeLivros > 0) {
                mensagem = "Essa categoria contém " + numeroDeLivros + " livro(s). Ao excluí-la, os livros serão excluídos também. Deseja continuar?";
            } else {
                mensagem = "Deseja realmente excluir a categoria " + categoriaSelecionada.getNome() + "?";
            }

            // Exibe o popup de confirmação
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, mensagem, ButtonType.YES, ButtonType.NO);
            confirmacao.setTitle("Confirmação");
            confirmacao.showAndWait();

            if (confirmacao.getResult() == ButtonType.YES) {
                livroCategoriaService.removerRelacoesPorCategoria(categoriaSelecionada.getId());
                categoriaService.removerCategoria(categoriaSelecionada.getId());
                showAlert("Sucesso", "Categoria excluída com sucesso.", Alert.AlertType.INFORMATION);
                carregarCategorias("");
            }
        } catch (Exception e) {
            logger.severe("Erro ao excluir categoria: " + e.getMessage());
            showAlert("Erro", "Não foi possível excluir a categoria.", Alert.AlertType.ERROR);
        }
    }
}
