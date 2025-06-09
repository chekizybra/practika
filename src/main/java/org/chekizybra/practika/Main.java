package org.chekizybra.practika;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage mainStage; // глобально доступное окно

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader.load(), 600, 500);
        mainStage.setScene(scene);
        mainStage.setTitle("Авторизация");
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}