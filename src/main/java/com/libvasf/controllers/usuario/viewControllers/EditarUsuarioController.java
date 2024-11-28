package com.libvasf.controllers.usuario.viewControllers;

import com.libvasf.controllers.ViewController;
import com.libvasf.controllers.usuario.UsuarioController;
import com.libvasf.models.Usuario;
import com.libvasf.services.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

public class EditarUsuarioController extends ViewController {

    public Button sair;
    public CheckBox cargo;
    public Button Excluir;
    private Usuario usuario;
    private final UsuarioService usuarioService = new UsuarioService();
    @FXML
    private TextField nomeField;
    @FXML
    private TextField emailField;

    @FXML
    private TextField senhaField;
    @FXML
    private Button salvarButton;

    @FXML
    private void initialize() {
        Excluir.setOnMouseClicked(this::excluir);
        salvarButton.setOnMouseClicked(this::salvarAlteracoes);
        sair.setOnMouseClicked(this::sair);

    }
    private void excluir(MouseEvent event) {
        if (usuario == null) {
            showAlert("Erro", "Nenhum usuário carregado para exclusão.", Alert.AlertType.WARNING);
            return;
        }

        // Exibir o popup de confirmação
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmar Exclusão");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Tem certeza de que deseja excluir o usuário " + usuario.getNome() + "?");

        // Capturar a resposta do usuário
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Excluir o usuário usando o serviço
                usuarioService.removerUsuario(usuario.getId());
                showAlert("Sucesso", "Usuário excluído com sucesso.", Alert.AlertType.INFORMATION);
                back(); // Retorna à tela anterior após exclusão
            } catch (Exception e) {
                showAlert("Erro", "Falha ao excluir o usuário. Tente novamente.", Alert.AlertType.ERROR);
            }
        }
    }

    // Método chamado para definir o usuário a ser editado
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        preencherCampos();
    }


    private void preencherCampos() {
        if (usuario != null) {
            nomeField.setText(usuario.getNome());
            emailField.setText(usuario.getEmail());
            senhaField.setText(usuario.getSenha());
            cargo.setSelected(usuario.getIsAdmin() == 1);
        }
    }

    // Método chamado ao clicar no botão "Salvar"
    @FXML
    private void salvarAlteracoes(MouseEvent event) {
        if (usuario != null) {
            usuario.setNome(nomeField.getText());
            usuario.setEmail(emailField.getText());
            usuario.setSenha(senhaField.getText());
            usuario.setIsAdmin(cargo.isSelected()?1:0);
            try{
                usuarioService.atualizarUsuario(usuario);
                showAlert("Sucesso","Usuário "+nomeField.getText()+ " editado com sucesso!", Alert.AlertType.INFORMATION);
                back();
            }catch(Exception e){
                showAlert("Falha","Não foi possível editar o usuário, verifique as informações! ",Alert.AlertType.ERROR);
            }

          ;

            // Exibe uma mensagem de confirmação ou navega para outra tela
            System.out.println("Usuário atualizado com sucesso!");
        }
    }
    @FXML
    private void sair(MouseEvent event) {
        back();
    }

}
