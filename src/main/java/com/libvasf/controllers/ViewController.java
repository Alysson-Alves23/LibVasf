package com.libvasf.controllers;

import com.libvasf.MainApplication;
import com.libvasf.controllers.usuario.viewControllers.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewController{
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    protected static Scene backScene;
    protected String backTitle;
    protected void goTo(String title, String viewname) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/libvasf/views/"+viewname+".fxml"));
            backScene = MainApplication.currentStage.getScene();
            Stage stage = MainApplication.currentStage;
            backTitle = stage.getTitle();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(title);

            stage.show();
            logger.info("Navegação para Dashboard realizada com sucesso.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao carregar main-view.fxml: ", e);
            showAlert("Erro", "Não foi possível abrir a Dashboard.", Alert.AlertType.ERROR);
        }
    }

    // Método auxiliar para exibir mensagens de alerta
    protected void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void back(){


        Stage stage = MainApplication.currentStage;
        String auxtitle = stage.getTitle();
        Scene altscene = stage.getScene();
        stage.setScene(backScene);
        stage.setTitle(backTitle);

        backTitle  = auxtitle;
        backScene  = altscene;
        stage.show();
        logger.info("Navegação para Dashboard realizada com sucesso.");
    }


}
