package com.libvasf.controllers.usuario.viewControllers;

import com.libvasf.controllers.ViewController;
import com.libvasf.controllers.usuario.UsuarioController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CriarUsuarioController extends ViewController{
    public TextField emailField;
    public PasswordField passwordField;
    public CheckBox isAdmin;
    public Button cadastrar;
    public TextField nameField;
    public ImageView sairImg;
    public Hyperlink sairLink;

    @FXML
    private void initialize() {

      cadastrar.setOnMouseClicked(this::handleCadastrar);
      sairImg.setOnMouseClicked(this::handleSair);
      sairLink.setOnMouseClicked(this::handleSair);

    }
    private void handleCadastrar(MouseEvent event) {

        String nome = nameField.getText();
        String senha = passwordField.getText();
        String email = emailField.getText();
        int admin = isAdmin.isSelected()?1:0;

          if(UsuarioController.cadastrarUsuario(nome, senha, email, admin)){
              showAlert("Sucesso","Usuário "+nome+ " criado com sucesso!",Alert.AlertType.INFORMATION);
              back();
          }else{
              showAlert("Falha","Não foi possível criar o usuário, verifique as informações! ",Alert.AlertType.ERROR);
          }


    }
    private void handleSair(MouseEvent event) {
        back();
    }


}
