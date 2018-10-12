package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeUI implements Initializable {

    @FXML
    private ComboBox<String> blockType;

    private String[] blockTypeNames = new String[]{"字母模式", "色盲模式-蓝", "色盲模式-绿", "色盲模式-红"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBlockTypeBox();
    }

    @FXML
    private void startGameAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameUI.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("ConnectConnectSee");

        GameUI gameUI = loader.getController();
        gameUI.setPrimaryStage(primaryStage);
        gameUI.setBlockType(blockType.getSelectionModel().getSelectedIndex());
        gameUI.initGame();

        primaryStage.show();
    }

    @FXML
    private void aboutAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("aboutUI.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("ConnectConnectSee");

        primaryStage.show();
    }

    @FXML
    private void settingsAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settingsUI.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("设置");

        SettingsUI sui = loader.getController();
        sui.setPrimaryStage(primaryStage);

        primaryStage.show();
    }

    private void setBlockTypeBox() {
        blockType.getItems().addAll(blockTypeNames);
        blockType.getSelectionModel().select(0);
    }
}
