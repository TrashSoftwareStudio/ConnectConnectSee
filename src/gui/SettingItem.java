package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

class SettingItem {

    private String name;

    private String fxml;

    private ItemController controller;

    SettingItem(String name, String fxml) {
        this.name = name;
        this.fxml = fxml;
    }

    void open(ScrollPane pane) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));

        AnchorPane root = loader.load();
        pane.setContent(root);

        controller = loader.getController();
    }

    void save() {
        controller.saveSettings();
    }

    @Override
    public String toString() {
        return name;
    }
}
