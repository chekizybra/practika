package org.chekizybra.practika;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class profileController {
    @FXML
    private Label fioLabel;

    @FXML
    private TableView<Map<String, Object>> ActiveTarif;

    @FXML private TableColumn<Map<String, Object>, String> tarifName;
    @FXML private TableColumn<Map<String, Object>, String> startDate;
    @FXML private TableColumn<Map<String, Object>, String> endDate;
    @FXML private TableColumn<Map<String, Object>, String> address;
    @FXML private TableColumn<Map<String, Object>, String> status;

    int currentUserId = session.currentUserId;

    @FXML
    public void initialize() throws IOException {
        System.out.println("начало");
        // Инициализация колонок таблицы
        tarifName.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().get("tarifName"))));
        address.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().get("address"))));
        status.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().get("status"))));
        startDate.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().get("startDate"))));
        endDate.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().get("endDate"))));




        fetchUserFio(currentUserId);
        onRefresh();
    }
    String currentclientid = "";
    public void fetchUserFio(int userId) throws IOException {
        String response = DatabaseRequests.get("clients", "?user_id=eq." + userId);
        JSONArray arr = new JSONArray(response);
        if (arr.length() > 0) {
            JSONObject obj = arr.getJSONObject(0);
            currentclientid = obj.optString("id");
            String fio = obj.optString("fio", "неизвестно");
            fioLabel.setText("ФИО: " + fio);
        }
    }

    public void onRefresh() throws IOException {
        String json = DatabaseRequests.get("active_tarifs", "?client_id=eq." + currentclientid);
        JSONArray arr = new JSONArray(json);
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            Map<String, Object> row = new HashMap<>();

            int tarifId = obj.optInt("tarif_id", 0);
            int statusId = obj.optInt("status", 0);

            row.put("id", tarifId); // Важно: сохраняем ID тарифа для дальнейшей работы

            //название тарифа
            String tarifName = "неизвестно";
            if (tarifId != 0) {
                String tarifJson = DatabaseRequests.get("tarifs", "?id=eq." + tarifId);
                JSONArray tarifArr = new JSONArray(tarifJson);
                if (tarifArr.length() > 0) {
                    tarifName = tarifArr.getJSONObject(0).optString("name", "неизвестно");
                }
            }
            row.put("tarifName", tarifName);

            //статус
            String statusName = "неизвестно";
            if (statusId != 0) {
                String statusJson = DatabaseRequests.get("status_types", "?id=eq." + statusId);
                JSONArray statusArr = new JSONArray(statusJson);
                if (statusArr.length() > 0) {
                    statusName = statusArr.getJSONObject(0).optString("type", "неизвестно");
                }
            }
            row.put("status", statusName);

            row.put("startDate", obj.optString("start_date", "не указано"));
            row.put("endDate", obj.optString("end_date", "не указано"));
            row.put("address", obj.optString("address", "не указан"));

            data.add(row);
        }

        ActiveTarif.setItems(data);
    }

    @FXML
    private void vTarifi() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tarifSpisok.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ActiveTarif.getScene().getWindow();
        Scene scene = new Scene(root, 1400, 700);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}