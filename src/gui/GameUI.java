package gui;

import core.Matrix;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

import static core.Utility.arrayArrayListToString;
import static core.Utility.containsPoint;

public class GameUI implements Initializable {

    @FXML
    private GridPane pane;

    private Matrix matrix;

    private Node[][] nodes;

    private int height = 8;
    private int width = 8;

    private int selectedRow;
    private int selectedColumn;

    private boolean isConnecting;

    private Button lastButton;

    private double blockWidth = 40.0;
    private double blockHeight = 40.0;

    private Color bgColor = Color.WHITESMOKE;
    private Color lineColor = Color.RED;

    private ArrayList<int[]> animationList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startGame();
        draw();
    }

    @FXML
    private void washAction() {
        matrix.wash();
        draw();
    }

    private void startGame() {
        nodes = new Node[height + 2][width + 2];
        matrix = new Matrix(height, width);
        matrix.initialize(0);
        matrix.wash();

    }

    private void showHidingBlocks() {
        for (int i = 0; i < width + 2; i++) {
            Canvas c1 = new Canvas();
            c1.setWidth(blockWidth);
            c1.setHeight(blockHeight);
            c1.getGraphicsContext2D().setFill(bgColor);
            c1.getGraphicsContext2D().fillRect(0, 0, blockWidth, blockHeight);
            Canvas c2 = new Canvas();
            c2.setWidth(blockWidth);
            c2.setHeight(blockHeight);
            c2.getGraphicsContext2D().setFill(bgColor);
            c2.getGraphicsContext2D().fillRect(0, 0, blockWidth, blockHeight);
            nodes[0][i] = c1;
            nodes[height + 1][i] = c2;
            pane.add(c1, i, 0);
            pane.add(c2, i, height + 1);
        }
        for (int i = 1; i < height + 1; i++) {
            Canvas c1 = new Canvas();
            c1.setWidth(blockWidth);
            c1.setHeight(blockHeight);
            c1.getGraphicsContext2D().setFill(bgColor);
            c1.getGraphicsContext2D().fillRect(0, 0, blockWidth, blockHeight);
            Canvas c2 = new Canvas();
            c2.setWidth(blockWidth);
            c2.setHeight(blockHeight);
            c2.getGraphicsContext2D().setFill(bgColor);
            c2.getGraphicsContext2D().fillRect(0, 0, blockWidth, blockHeight);
            pane.add(c1, 0, i);
            pane.add(c2, width + 1, i);
            nodes[i][0] = c1;
            nodes[i][width + 1] = c2;
        }
    }

    private void draw() {
        pane.getChildren().clear();
        showHidingBlocks();
        int size = height * width;
        for (int i = 0; i < size; i++) {
            int y = i / width;
            int x = i % width;
            if (!matrix.isEliminated(y, x)) {
                Button bt = new Button(matrix.getBlock(y, x).toString());
                bt.setPrefSize(blockWidth, blockHeight);
                bt.setOnMouseClicked(e -> clickAction(bt));
                pane.add(bt, x + 1, y + 1);
                nodes[y + 1][x + 1] = bt;
            } else {
                Canvas ca = new Canvas();
                ca.setHeight(blockHeight);
                ca.setWidth(blockWidth);
                ca.getGraphicsContext2D().setFill(bgColor);
                ca.getGraphicsContext2D().fillRect(0.0, 0.0, blockWidth, blockHeight);
                pane.add(ca, x + 1, y + 1);
                nodes[y + 1][x + 1] = ca;
            }
        }
    }

    private void clickAction(Button bt) {
        int r = GridPane.getRowIndex(bt) - 1;
        int c = GridPane.getColumnIndex(bt) - 1;
        if (!isConnecting) {
            selectedRow = r;
            selectedColumn = c;
            lastButton = bt;
            lastButton.setFont(new Font(15));
            lastButton.setTextFill(Color.LIMEGREEN);
        } else {
            lastButton.setFont(new Font(12));
            lastButton.setTextFill(Color.BLACK);
            animationList = matrix.tryConnect(selectedRow, selectedColumn, r, c);
            if (animationList != null) {
                bt.setManaged(false);
                lastButton.setManaged(false);
                lastButton = null;
                if (animationList.size() > 0) {
                    ArrayList<int[]> listWithBlocks = new ArrayList<>();
                    listWithBlocks.add(new int[]{r + 1, c + 1});
                    listWithBlocks.addAll(animationList);
                    listWithBlocks.add(new int[]{selectedRow + 1, selectedColumn + 1});
                    drawConnection(listWithBlocks);
                    PauseTransition pause = new PauseTransition(Duration.millis(150));
                    pause.setOnFinished(e -> draw());
                    pause.play();
                } else {
                    draw();
                }
                if (matrix.isWin()) {
                    showWin();
                }
            }
        }
        isConnecting = !isConnecting;
    }

    private void showWin() {
        Alert winInfo = new Alert(Alert.AlertType.INFORMATION);
        winInfo.setTitle("666");
        winInfo.setHeaderText("你赢了");
        winInfo.setContentText("真tm6");
        winInfo.showAndWait();
    }

    private void drawConnection(ArrayList<int[]> listWithBlocks) {
        if (animationList != null) {
            double x0 = blockWidth / 2;
            double y0 = blockHeight / 2;
            for (int[] cor : animationList) {
                Node n = nodes[cor[0]][cor[1]];
                if (n instanceof Canvas) {
                    Canvas c = (Canvas) n;
                    int graphType = getGraphicType(cor, listWithBlocks);
                    GraphicsContext gc = c.getGraphicsContext2D();
                    gc.setStroke(lineColor);
                    gc.setLineWidth(4.0);
                    if (graphType == 0) {
                        gc.strokeLine(0.0, y0, blockWidth, y0);
                    } else if (graphType == 1) {
                        gc.strokeLine(x0, 0.0, x0, blockHeight);
                    } else if (graphType == 3) {
                        gc.strokeLine(0.0, y0, x0, y0);
                        gc.strokeLine(x0, y0, x0, 0.0);
                    } else if (graphType == 4) {
                        gc.strokeLine(x0, 0.0, x0, y0);
                        gc.strokeLine(x0, y0, blockWidth, y0);
                    } else if (graphType == 5) {
                        gc.strokeLine(x0, blockHeight, x0, y0);
                        gc.strokeLine(x0, y0, blockWidth, y0);
                    } else if (graphType == 6) {
                        gc.strokeLine(0.0, y0, x0, y0);
                        gc.strokeLine(x0, y0, x0, blockHeight);
                    }
                }
            }
        }

    }

    private int getGraphicType(int[] cor, ArrayList<int[]> listWithBlocks) {
        if (cor[2] < 2) return cor[2];
        else {
            return getTurnDirection(cor, listWithBlocks);
        }
    }

    /**
     * @param point the starting point
     * @param list  the list containing all points
     * @return 3 if _|, 4 if L, 5 if┌, 6 if ┐.
     */
    private int getTurnDirection(int[] point, ArrayList<int[]> list) {
//        arrayArrayListToString(list);
        int r = point[0];
        int c = point[1];
        int[] left = new int[]{r, c - 1};
        int[] right = new int[]{r, c + 1};
        int[] up = new int[]{r - 1, c};
        int[] down = new int[]{r + 1, c};
        if (containsPoint(list, left) && containsPoint(list, up)) {
            return 3;
        } else if (containsPoint(list, left) && containsPoint(list, down)) {
            return 6;
        } else if (containsPoint(list, right) && containsPoint(list, up)) {
            return 4;
        } else if (containsPoint(list, right) && containsPoint(list, down)) {
            return 5;
        } else {
//            System.out.println(Arrays.toString(point));
            throw new RuntimeException("???");
        }
    }
}
