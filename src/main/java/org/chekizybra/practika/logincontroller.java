package org.chekizybra.practika;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class logincontroller {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
            Scene scene = new Scene(loader.load());
            Main.mainStage.setScene(scene); // используем тот же Stage
            Main.mainStage.setTitle("Личный кабинет");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}