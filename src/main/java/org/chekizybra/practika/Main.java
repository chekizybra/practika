package org.chekizybra.practika;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("authentication.fxml"));
        Scene scene = new Scene(loader.load(), 400, 650);
        mainStage.setScene(scene);
        mainStage.setTitle("Авторизация");
        mainStage.setResizable(false);
        mainStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}