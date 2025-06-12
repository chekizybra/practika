package org.chekizybra.practika;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class tarifSpisokController {

    @FXML private Label titleLabel;
    @FXML private TableView<Map<String, Object>> tarifsTable;
    @FXML private TableColumn<Map<String, Object>, String> name;
    @FXML private TableColumn<Map<String, Object>, String> type;
    @FXML private TableColumn<Map<String, Object>, String> speed;
    @FXML private TableColumn<Map<String, Object>, String> channels;
    @FXML private TableColumn<Map<String, Object>, String> deviceType;
    @FXML private TableColumn<Map<String, Object>, String> price;
    @FXML private TableColumn<Map<String, Object>, String> action;

    @FXML private VBox adresField;
    @FXML private TextField adresTextField;

    private Map<String, Object> selectedRow;

    @FXML
    public void initialize() throws IOException {
        name.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().get("name"))));
        type.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().get("type"))));
        deviceType.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().get("device"))));
        speed.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().get("speed"))));
        channels.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().get("channels"))));
        price.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().get("price"))));

        loadTarifs();
        addButtons();
    }
    private void loadTarifs() throws IOException {
            String json = DatabaseRequests.get("tarifs", "?select=*");
            JSONArray arr = new JSONArray(json);
            ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Map<String, Object> row = new HashMap<>();

                row.put("id", obj.optInt("id", 0));
                row.put("name", obj.optString("name", "неизвестно"));
                row.put("price", obj.optString("price_for_mounth", "0"));
                row.put("channels", obj.optString("channels", "0"));
                row.put("speed", obj.optString("speed_mb", "0"));

                // тип тарифа
                String typeName = "неизвестно";
                int typeId = obj.optInt("type", 0);
                String typeJson = DatabaseRequests.get("tarif_types", "?id=eq." + typeId);
                JSONArray typeArr = new JSONArray(typeJson);
                if (!typeArr.isEmpty()) {
                    typeName = typeArr.getJSONObject(0).optString("type", "неизвестно");
                }
                row.put("type", typeName);

                // тип устройства
                String deviceName = "неизвестно";
                int deviceId = obj.optInt("device_id", 0);
                String deviceJson = DatabaseRequests.get("devices", "?id=eq." + deviceId);
                JSONArray deviceArr = new JSONArray(deviceJson);
                if (deviceArr.length() > 0) {
                    deviceName = deviceArr.getJSONObject(0).optString("device", "неизвестно");
                }
                row.put("device", deviceName);

                data.add(row);
            }

            tarifsTable.setItems(data);

        }

    private void addButtons() {
        action.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Подключить");

            {
                btn.setOnAction(event -> {
                    Map<String, Object> row = getTableView().getItems().get(getIndex());
                    showAdresWindow(row);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    private void showAdresWindow(Map<String, Object> row) {
        selectedRow = row;
        adresField.setVisible(true);
        tarifsTable.setVisible(false);
    }

    @FXML
    private void confirmAddressAndAddTarif() throws IOException {
        if (selectedRow == null) return;

        String address = adresTextField.getText().trim();
        if (address.isEmpty()) return;

        int tarifId = (int) selectedRow.get("id");
        int userId = session.currentUserId;

        String json = DatabaseRequests.get("clients", "?user_id=eq." + userId);
        JSONArray arr = new JSONArray(json);
        int client_user_id = arr.getJSONObject(0).optInt("id");

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        JSONObject newTarif = new JSONObject();
        newTarif.put("client_id", client_user_id);
        newTarif.put("tarif_id", tarifId);
        newTarif.put("address", address);
        newTarif.put("status", 1);
        newTarif.put("start_date", start.format(formatter));
        newTarif.put("end_date", end.format(formatter));

        DatabaseRequests.post(newTarif, "active_tarifs");

        System.out.println("Тариф подключен!");

        // очистка и возврат к таблице
        adresTextField.clear();
        adresField.setVisible(false);
        tarifsTable.setVisible(true);
    }

    @FXML
    public void goToProfile() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        Stage stage = (Stage) tarifsTable.getScene().getWindow();
        stage.setScene(new Scene(root, 1400, 700));
    }
}
