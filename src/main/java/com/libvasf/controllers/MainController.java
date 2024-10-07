package com.libvasf.controllers;
import com.libvasf.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


import java.io.IOException;

public class MainController extends ViewController {
    public Hyperlink cadastrarCliente;
    public Hyperlink gerenciarLivros;
    public Hyperlink gerenciarUsuario;
    public Hyperlink cadastrarLivro;
    public Hyperlink cadastrarUsuario;
    public Hyperlink sairLink;
    public ImageView sairImg;


    @FXML
    private void initialize() {

        gerenciarLivros.setOnMouseClicked(this::handleGerenciarLivros);
        gerenciarUsuario.setOnMouseClicked(this::handleGerenciarUsuarios);
        cadastrarLivro.setOnMouseClicked(this::handleCadastrarLivros);
        cadastrarCliente.setOnMouseClicked(this::handleCadastrarCliente);
        cadastrarUsuario.setOnMouseClicked(this::handleCadastrarUsuarios);
        sairLink.setOnMouseClicked(this::handleSair);
        sairImg.setOnMouseClicked(this::handleSair);

    }
    @FXML
    private void handleCadastrarCliente(MouseEvent event) {
    }
    @FXML
    private void handleGerenciarLivros(MouseEvent event) {
    }
    @FXML
    private void handleCadastrarLivros(MouseEvent event) {
    }
    @FXML
    private void handleGerenciarUsuarios(MouseEvent event) {

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
