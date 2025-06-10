package org.chekizybra.practika;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

public class AuthentifactionController {
    @FXML
    private VBox login;
    @FXML
    private VBox workerRegister;
    @FXML
    private VBox clientRegister;
    @FXML
    private VBox obfields;

    @FXML
    public void showregister() {
        clientRegister.setVisible(true);
        login.setVisible(false);

    }

    @FXML
    public void showlogin() {
        login.setVisible(true);
        clientRegister.setVisible(false);
        workerRegister.setVisible(false);
    }

    @FXML
    public void choseClientReg() {
        clientRegister.setVisible(true);
        workerRegister.setVisible(false);
        login.setVisible(false);
    }

    @FXML
    public void choseWorkerReg() {
        workerRegister.setVisible(true);
        clientRegister.setVisible(false);
        login.setVisible(false);
    }

    @FXML
    private ComboBox<String> workTypes;

    @FXML
    public void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll("Служба Поддержки", "Выездной", "Выездной", "Выездной", "Выездной");
        workTypes.setItems(items);
    }


    @FXML private TextField username;
    @FXML private PasswordField password;

    //клиент
    @FXML private TextField clientUsername;
    @FXML private TextField clientfio;
    @FXML private TextField clientPhone;
    @FXML private TextField clientEmail;
    @FXML private TextField clientAddress;

    //работник
    @FXML private TextField workerUsername;
    @FXML private TextField workerFullName;
    @FXML private TextField workerPhone;





    @FXML
    public void registerClient() {
        if (!clientPassword.getTextField().equals(clientConfirmPassword.getTextField())) {
            System.out.println("Ошибка"+ "Пароли не совпадают!");
            return;
        }

        JSONObject clientJson = new JSONObject();
        clientJson.put("type", "client");
        clientJson.put("username", clientUsername.getTextField());
        clientJson.put("fullName", clientFullName.getTextField());
        clientJson.put("phone", clientPhone.getTextField());
        clientJson.put("email", clientEmail.getTextField());
        clientJson.put("address", clientAddress.getTextField());
        clientJson.put("password", clientPassword.getTextField());

        System.out.println("Client JSON: " + clientJson.toString());
        // Here you would typically send this JSON to your backend
    }

    // Handle worker registration
    @FXML
    public void registerWorker() {
        if (!workerPassword.getTextField().equals(workerConfirmPassword.getTextField())) {
            System.out.println("Ошибка"+ "Пароли не совпадают!");
            return;
        }

        if (workTypes.getValue() == null) {
            System.out.println("Ошибка"+ "Выберите тип работ!");
            return;
        }

        JSONObject workerJson = new JSONObject();
        workerJson.put("type", "worker");
        workerJson.put("username", workerUsername.getTextField());
        workerJson.put("fullName", workerFullName.getTextField());
        workerJson.put("phone", workerPhone.getTextField());
        workerJson.put("workType", workTypes.getValue());
        workerJson.put("password", workerPassword.getTextField());

        System.out.println("Worker JSON: " + workerJson.toString());
        // Here you would typically send this JSON to your backend
    }

    // Handle login
    @FXML
    public void handleLogin() {
        String username = username.getTextField();
        String password = password.getTextField();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Ошибка"+ "Заполните все поля!");
            return;
        }

        JSONObject loginJson = new JSONObject();
        loginJson.put("username", username);
        loginJson.put("password", password);

        System.out.println("Login JSON: " + loginJson.toString());
        // Here you would typically send this JSON to your backend
    }
}
