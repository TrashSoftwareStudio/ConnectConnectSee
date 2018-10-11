package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import main.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutUI implements Initializable {

    @FXML
    private Label versionLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        versionLabel.setText(Main.VERSION);
    }
}
