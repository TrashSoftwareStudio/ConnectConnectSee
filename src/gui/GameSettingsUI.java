package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import util.ConfigLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSettingsUI implements Initializable, ItemController {

    @FXML
    private ComboBox<String> collapseModeBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCollapseModeBox();
    }

    private void setCollapseModeBox() {
        collapseModeBox.getItems().addAll("无", "向下", /* "全部向左下", */ "整列向左下");
        int index = getCollapseIndex();
        collapseModeBox.getSelectionModel().select(index);
    }

    static int getCollapseIndex() {
        String s = ConfigLoader.getConfig("collapse");
        int index;
        if (s != null) {
            switch (s) {
                case "down":
                    index = 1;
                    break;
                case "down_left":
                    index = 2;
                    break;
                case "col_left_down":
                    index = 3;
                    break;
                default:
                    index = 0;
                    break;
            }
        } else {
            index = 0;
        }
        return index;
    }

    @Override
    public void saveSettings() {
        int index = collapseModeBox.getSelectionModel().getSelectedIndex();
        String s;
        if (index == 1) s = "down";
        else if (index == 2) s = "down_left";
        else if (index == 3) s = "col_left_down";
        else s = "none";
        ConfigLoader.writeConfig("collapse", s);
    }
}
