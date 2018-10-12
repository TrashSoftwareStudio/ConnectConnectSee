package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsUI implements Initializable {

    @FXML
    private ListView<SettingItem> listView;

    @FXML
    private ScrollPane scrollPane;

    private Stage primaryStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initListView();
        listAction();
    }

    @FXML
    private void confirmAction() {
        for (SettingItem si : listView.getItems()) {
            si.save();
        }
        primaryStage.close();
    }

    @FXML
    private void cancelAction() {
        primaryStage.close();
    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void listAction() {
        listView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                newValue.open(scrollPane);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }));
    }

    private void initListView() {
        SettingItem gameSetting = new SettingItem("游戏", "gameSettingsUI.fxml");

        listView.getItems().addAll(gameSetting);
    }

}
