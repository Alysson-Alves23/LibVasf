package com.libvasf.controllers.usuario.viewControllers;

import com.libvasf.MainApplication;
import com.libvasf.controllers.ViewController;
import com.libvasf.models.Usuario;
import com.libvasf.services.UsuarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ListarUsuariosController extends ViewController {

    public Button sair;
    @FXML
    private TextField pesquisarField;
    @FXML
    private ListView<Usuario> usuariosListView;
    @FXML
    private Button editarButton;

    private ObservableList<Usuario> usuariosList;
    
    UsuarioService usuarioService = new UsuarioService();
    @FXML
    public void initialize() {
        usuariosList = FXCollections.observableArrayList(usuarioService.listarUsuarios());
        usuariosListView.setItems(usuariosList);
        
        usuariosListView.setCellFactory(param -> new ListCell<Usuario>() {
            @Override
            protected void updateItem(Usuario Usuario, boolean empty) {
                super.updateItem(Usuario, empty);
                setText(empty || Usuario == null ? null :  Usuario.getEmail());
            }
        });

      
        editarButton.setDisable(true);

    
        usuariosListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            editarButton.setDisable(newSelection == null);

            editarButton.setOnMouseClicked(this::editarUsuario);

        });
        sair.setOnMouseClicked(this::sair);

        pesquisarField.textProperty().addListener((obs, oldText, newText) -> {
            filtrarUsuarios(newText);
        });
    }
    @FXML
    private void sair(MouseEvent event) {
        goTo("Home","main-view");
    }

    @FXML
    private void editarUsuario(MouseEvent event) {
        Usuario usuarioSelecionado = usuariosListView.getSelectionModel().getSelectedItem();
        if (usuarioSelecionado != null) {
            navegarParaEditarUsuario(usuarioSelecionado);
        }

    }

    private void filtrarUsuarios(String texto) {
        usuariosList.setAll(usuarioService.buscarUsuarioPorEmailPartial(texto));
    }

    private void navegarParaEditarUsuario(Usuario Usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/libvasf/views/edit-user-view.fxml"));
            Parent root = loader.load();
            backScene = MainApplication.currentStage.getScene();
            Stage stage = MainApplication.currentStage;
            backTitle = stage.getTitle();
            EditarUsuarioController controller = loader.getController();
            controller.setUsuario(Usuario);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
