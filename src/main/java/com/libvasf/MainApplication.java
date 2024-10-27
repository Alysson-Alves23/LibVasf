package com.libvasf;

import com.libvasf.models.Usuario;
import com.libvasf.utils.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.Objects;


public class MainApplication extends Application {
    private static Usuario usuario;
    public static Stage currentStage;
    @Override
    public void start(Stage stage) throws IOException {
        SessionFactory s = HibernateUtil.getSessionFactory();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/libvasf/views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        currentStage = stage;
        stage.setTitle("Libvasf");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/logo.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setWidth(1440);
        stage.setHeight(1024);
        stage.show();
    }
    public static void setCurrentUser(Usuario u){
        usuario = u;
    }
    public static Usuario getCurrentUser(){
        return usuario;
    }
    public static void main(String[] args) {
        launch();
    }
}