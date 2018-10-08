package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeUI implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

        primaryStage.show();
    }
}
