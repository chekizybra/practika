package org.chekizybra.practika;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class AuthenticationController {
    @FXML
    private VBox login;
    @FXML
    private VBox workerRegister;
    @FXML
    private VBox clientRegister;

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
        items.addAll("Менеджер");
        workTypes.setItems(items);
    }
    //поля с ошибкой
    @FXML private Label loginerrormassage;
    @FXML private Label workererrormassage;
    @FXML private Label clienterrormassage;

    //логин
    @FXML private TextField username;
    @FXML private PasswordField password;

    //клиент
    @FXML private TextField clientUsername;
    @FXML private TextField clientfio;
    @FXML private TextField clientPhone;
    @FXML private TextField clientEmail;
    @FXML private TextField clientAddress;
    @FXML private TextField clientPassword;
    @FXML private TextField clientConfirimPassword;

    //работник
    @FXML private TextField workerUsername;
    @FXML private TextField workerfio;
    @FXML private TextField workerPhone;
    @FXML private TextField workerPassword;
    @FXML private TextField workerConfirimPassword;

    public void setErrormassage(Label errormassage,String errortext){
        errormassage.setText(errortext);
    }

    @FXML
    public void registerClient() throws IOException {
        // ... предыдущие проверки остаются без изменений ...
        if (clientUsername.getText().isEmpty() || clientfio.getText().isEmpty() || clientPhone.getText().isEmpty() || clientEmail.getText().isEmpty() || clientAddress.getText().isEmpty() || clientPassword.getText().isEmpty() || clientConfirimPassword.getText().isEmpty()) {
            setErrormassage(clienterrormassage, "Заполните все поля");
            return;
        }
        if (!clientPassword.getText().equals(clientConfirimPassword.getText())) {
            setErrormassage(workererrormassage, "Пароли не совпадают!");
            return;
        }

        // Создаем пользователя
        JSONObject clientuser = new JSONObject();
        clientuser.put("password", clientPassword.getText());
        clientuser.put("username", clientUsername.getText());
        clientuser.put("role", "2");

        String userResponse = DatabaseRequests.post(clientuser, "users");

        // Получаем ID созданного пользователя
        String usernameselect = "?username=eq." + clientUsername.getText() + "&select=id";
        String userData = DatabaseRequests.get("users", usernameselect);
        JSONArray userArray = new JSONArray(userData);
        if (userArray.length() == 0) {
            throw new RuntimeException("");
        }
        int userId = userArray.getJSONObject(0).getInt("id");


        JSONObject client = new JSONObject();
        client.put("user_id", userId);
        client.put("fio", clientfio.getText());
        client.put("phone", clientPhone.getText());
        client.put("email", clientEmail.getText());
        client.put("address", clientAddress.getText());

        String clientResponse = DatabaseRequests.post(client, "clients");
        System.out.println("Client created: " + clientResponse);
    }
    @FXML
    public void registerWorker () throws IOException {
        if (workerUsername.getText().isEmpty() || workerfio.getText().isEmpty() || workerPhone.getText().isEmpty() || workerPassword.getText().isEmpty() || workerConfirimPassword.getText().isEmpty()) {
            setErrormassage(clienterrormassage, "Заполните все поля");
            return;
        }
        if (!workerPassword.getText().equals(workerConfirimPassword.getText())) {
            setErrormassage(workererrormassage, "Пароли не совпадают!");
            return;
        }
        if (workTypes.getValue() == null) {
            setErrormassage(workererrormassage, "Выберите тип работ");
            return;
        }
        JSONObject workeruser = new JSONObject();
        workeruser.put("username", workerUsername.getText());
        workeruser.put("password", workerPassword.getText());
        workeruser.put("role", "3");
        DatabaseRequests.post(workeruser, "users");
        System.out.println(workeruser);

        //user_id по username
        String usernameselect = "?username=eq." + workerUsername.getText() + "&select=id";
        String userIdResponse = DatabaseRequests.get("users", usernameselect);
        JSONArray userArray = new JSONArray(userIdResponse);
        if (userArray.length() == 0) {
            throw new RuntimeException("");
        }

        int userId = userArray.getJSONObject(0).getInt("id");

        int work_type = 0;
        switch (workTypes.getValue()){
            case "Служба поддержки" : work_type = 1; break;
            case "Выездной сотрудник": work_type = 2; break;
        }
        //json с user_id
        JSONObject worker = new JSONObject();
        worker.put("user_id", userId);
        worker.put("fio", workerfio.getText());
        worker.put("work_type_id",work_type);
        worker.put("phone", workerPhone.getText());
        DatabaseRequests.post(worker, "/rest/v1/workers");
    }


    @FXML
    public void login() {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            setErrormassage(loginerrormassage, "Заполните все поля");
            return;
        }

        try {
            // Получаем пользователя по username
            String filter = "?username=eq." + username.getText();
            String response = DatabaseRequests.get("users", filter);
            System.out.println(response);
            JSONArray users = new JSONArray(response);

            if (users.length() == 0) {
                setErrormassage(loginerrormassage, "Пользователь не найден");
                return;
            }

            JSONObject user = users.getJSONObject(0);
            String storedPassword = user.getString("password");

            if (!storedPassword.equals(password.getText())) {
                setErrormassage(loginerrormassage, "Неверный пароль");
                return;
            }

            String usernameselect = "?username=eq." + username.getText() + "&select=*";
            System.out.println(usernameselect);
            String userIdResponse = DatabaseRequests.get("users", usernameselect);
            System.out.println("userIdResponse = " + userIdResponse);
            JSONArray userArray = new JSONArray(userIdResponse);
            if (userArray.length() == 0) {
                throw new RuntimeException("");
            }
            int userId = userArray.getJSONObject(0).getInt("id");

            session.currentUserId = userId;
            //переход на другую сцену по роли

            int userRole = userArray.getJSONObject(0).getInt("role");
            FXMLLoader loader = null;

            switch (userRole) {
                case 1: // администратор
                    loader = new FXMLLoader(getClass().getResource("admin_panel.fxml"));
                    break;
                case 2: // клиент
                    loader = new FXMLLoader(getClass().getResource("profile.fxml"));
                    break;
                case 3: // сотрудник
                    loader = new FXMLLoader(getClass().getResource("managerProfileTarifs.fxml"));
                    break;
            }

            Parent root = loader.load();
            Stage stage = (Stage) username.getScene().getWindow();
            Scene scene = new Scene(root, 1400, 700);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            setErrormassage(loginerrormassage, "Ошибка при входе");
        }
    }
}
