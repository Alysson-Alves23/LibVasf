package com.libvasf.controllers.livro;

import com.libvasf.MainApplication;
import com.libvasf.controllers.ViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class GerenciarLivro extends ViewController
{

    public Hyperlink gerenciarLivros;
    public Hyperlink cadastrarLivro;
    public Hyperlink sairLink;
    public ImageView sairImg;
    public Hyperlink gerenciarAutor;


    @FXML
    private void initialize() {


        gerenciarLivros.setOnMouseClicked(this::handleGerenciarLivros);
        cadastrarLivro.setOnMouseClicked(this::handleCadastrarLivros);
        sairLink.setOnMouseClicked(this::handleSair);
        sairImg.setOnMouseClicked(this::handleSair);

    }
    @FXML
    private void handleGerenciarLivros(MouseEvent event) {
    }
    @FXML
    private void handleCadastrarLivros(MouseEvent event) {
        goTo("Libvasf","book-register-view");
    }
    @FXML
    private void handleGerenciarAutor(MouseEvent event) {
    }
    @FXML
    private void handleSair(MouseEvent event) {
        goTo("Libvasf","main-view");

    }
}
