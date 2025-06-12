package org.chekizybra.practika;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.HashMap;
import java.util.Map;

public class managerProfileTarifsController {

    @FXML private TableView<Map<String, Object>> tarifsTable;
    @FXML private TableColumn<Map<String, Object>, String> name;
    @FXML private TableColumn<Map<String, Object>, String> type;
    @FXML private TableColumn<Map<String, Object>, String> device;
    @FXML private TableColumn<Map<String, Object>, String> speed;
    @FXML private TableColumn<Map<String, Object>, String> channels;
    @FXML private TableColumn<Map<String, Object>, String> price;

    @FXML private TextField filterName, filterType, filterDevice;
    @FXML private VBox createNT;
    @FXML private TextField nameNT, typeNT, deviceNT, speedNT, channelsNT, priceNT;

    private ObservableList<Map<String, Object>> allTarifs = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws IOException {
        name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get("name").toString()));
        type.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get("type").toString()));
        device.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get("device").toString()));
        speed.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get("speed").toString()));
        channels.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get("channels").toString()));
        price.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get("price").toString()));

        loadTarifs();
    }

    private void loadTarifs() throws IOException {
        allTarifs.clear();
        String json = DatabaseRequests.get("tarifs", "?select=*");
        JSONArray arr = new JSONArray(json);

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

            allTarifs.add(row);
        }

        tarifsTable.setItems(FXCollections.observableArrayList(allTarifs));
    }

    @FXML
    private void applyFilters() {
        String nameFilter = filterName.getText().toLowerCase();
        String typeFilter = filterType.getText().toLowerCase();
        String deviceFilter = filterDevice.getText().toLowerCase();

        ObservableList<Map<String, Object>> filtered = FXCollections.observableArrayList();
        for (Map<String, Object> row : allTarifs) {
            if (row.get("name").toString().toLowerCase().contains(nameFilter)
                    && row.get("type").toString().toLowerCase().contains(typeFilter)
                    && row.get("device").toString().toLowerCase().contains(deviceFilter)) {
                filtered.add(row);
            }
        }

        tarifsTable.setItems(filtered);
    }

    @FXML
    private void resetFilters() {
        filterName.clear();
        filterType.clear();
        filterDevice.clear();
        tarifsTable.setItems(FXCollections.observableArrayList(allTarifs));
    }

    @FXML
    private void showCreateTarifForm() {
        createNT.setVisible(true);
    }

    @FXML
    private void addTarif() throws IOException {
        JSONObject newTarif = new JSONObject();
        newTarif.put("name", nameNT.getText());
        newTarif.put("type", Integer.parseInt(typeNT.getText()));
        newTarif.put("device_id", Integer.parseInt(deviceNT.getText()));
        newTarif.put("speed_mb", speedNT.getText());
        newTarif.put("channels", channelsNT.getText());
        newTarif.put("price_for_mounth", priceNT.getText());

        DatabaseRequests.post(newTarif, "tarifs");
        loadTarifs();
        createNT.setVisible(false);
        clearCreateForm();
    }

    private void clearCreateForm() {
        nameNT.clear();
        typeNT.clear();
        deviceNT.clear();
        speedNT.clear();
        channelsNT.clear();
        priceNT.clear();
    }

    @FXML
    private void goToActiveTarifs() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("managerProfile.fxml"));
        Stage stage = (Stage) tarifsTable.getScene().getWindow();
        stage.setScene(new Scene(root, 1400, 700));
    }
}
