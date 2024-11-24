package com.libvasf.controllers.cliente;

import com.libvasf.controllers.ViewController;
import com.libvasf.controllers.usuario.UsuarioController;
import com.libvasf.models.Cliente;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class RegistrarCliente extends ViewController{
    public TextField telefone;
    @FXML
    private TextField nome;
    @FXML
    private TextField email;
    @FXML
    private TextField cpf;
    @FXML
    private Button voltar;
    @FXML
    private Button salvarButton;
    private ClienteController clienteController = new ClienteController();
    @FXML
    private void initialize() {

        salvarButton.setOnMouseClicked(this::handleCadastrar);
        voltar.setOnMouseClicked(this::handleSair);


    }
    private void handleCadastrar(MouseEvent event) {

        String nome = this.nome.getText();
        String email = this.email.getText();
        String cpf = this.cpf.getText();
        String telefone = this.telefone.getText();
        if(clienteController.salvarCliente(nome,cpf,email,telefone)){
            showAlert("Sucesso","CLiente "+nome+ " criado com sucesso!", Alert.AlertType.INFORMATION);
            back();
        }else{
            showAlert("Falha","Não foi possível criar o usuário, verifique as informações! ",Alert.AlertType.ERROR);
        }


    }
    private void handleSair(MouseEvent event) {
        back();
    }

}
