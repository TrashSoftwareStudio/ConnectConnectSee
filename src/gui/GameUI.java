package gui;

import core.Matrix;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

import static core.Utility.containsPoint;

public class GameUI implements Initializable {

    @FXML
    private GridPane pane;

    @FXML
    private Label timeLabel, scoreLabel, costLabel, messageLabel;

    private Matrix matrix;

    private Node[][] nodes;

    private int height = 10;
    private int width = 10;

    private int selectedRow;
    private int selectedColumn;

    private boolean isConnecting;

    private Button lastButton;

    private double blockWidth = 40.0;
    private double blockHeight = 40.0;

    private static final Color bgColor = Color.WHITESMOKE;
    private static final Color lineColor = Color.RED;

    private ArrayList<int[]> animationList;

    private Stage primaryStage;
    private int blockType;

    private int restartTime = 3;

    private int score;
    private int scoreMultiplier = 2;

    boolean isRunning;

    private int collapse;

    private Timer timer;

    private Button hintBtn1, hintBtn2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageLabel.setTextFill(Color.RED);
    }

    void initGame() {
        startGame();
        draw();
    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setOnCloseAction();
    }

    void setBlockType(int blockType) {
        this.blockType = blockType;
    }

    private void autoWash() {
        matrix.wash();
        draw();
        restartTime -= 1;
    }

    @FXML
    private void washAction() {
        if (score >= scoreMultiplier) {
            matrix.wash();
            draw();
            subtractScore();
            checkLost();
        } else {
            messageLabel.setText("分数不足！");
        }
    }

    @FXML
    private void hintAction() {
        if (score >= scoreMultiplier) {
            int[][] hint = matrix.getHint();
            if (hint != null) {
                int r1 = hint[0][0] + 1;
                int c1 = hint[0][1] + 1;
                int r2 = hint[1][0] + 1;
                int c2 = hint[1][1] + 1;
                hintBtn1 = (Button) nodes[r1][c1];
                hintBtn2 = (Button) nodes[r2][c2];
                showHint(r1, c1, r2, c2);
                subtractScore();
            } else {
                messageLabel.setText("无牌可消除！");
            }
        } else {
            messageLabel.setText("分数不足！");
        }
    }

    private void showHint(int r1, int c1, int r2, int c2) {
        if (blockType == Matrix.ALPHABET) {
            hintBtn1.setStyle("-fx-border-color: orange; -fx-border-width: 3px;");
            hintBtn2.setStyle("-fx-border-color: orange; -fx-border-width: 3px;");
        } else if (blockType == Matrix.COLOR_BLUE || blockType == Matrix.COLOR_GREEN || blockType == Matrix.COLOR_RED) {
            hintBtn1.setStyle(String.format("-fx-border-color: orange; -fx-border-width: 3px; " +
                    "-fx-background-color: #%s", matrix.getBlock(r1 - 1, c1 - 1).toString()));
            hintBtn2.setStyle(String.format("-fx-border-color: orange; -fx-border-width: 3px; " +
                    "-fx-background-color: #%s", matrix.getBlock(r2 - 1, c2 - 1).toString()));
        }

    }

    private void startGame() {
        nodes = new Node[height + 2][width + 2];
        matrix = new Matrix(height, width);
        collapse = GameSettingsUI.getCollapseIndex();
        matrix.initialize(blockType);
        matrix.wash();
    }

    private void setOnCloseAction() {
        primaryStage.setOnCloseRequest(e -> isRunning = false);
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
                Button bt = new Button();
                if (blockType == Matrix.ALPHABET) {
                    bt.setText(matrix.getBlock(y, x).toString());
                } else if (blockType == Matrix.COLOR_BLUE || blockType == Matrix.COLOR_GREEN ||
                        blockType == Matrix.COLOR_RED) {
                    bt.setStyle(String.format("-fx-background-color: #%s", matrix.getBlock(y, x).toString()));
                } else {
                    throw new RuntimeException("No such style");
                }
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
        if (!isRunning) {
            isRunning = true;
            timer = new Timer(this);
            Thread t = new Thread(timer);
            t.start();
        }
        int r = GridPane.getRowIndex(bt) - 1;
        int c = GridPane.getColumnIndex(bt) - 1;
        if (!isConnecting) {
            selectedRow = r;
            selectedColumn = c;
            lastButton = bt;
        } else {
            animationList = matrix.tryConnect(selectedRow, selectedColumn, r, c);
            if (animationList != null) {
                messageLabel.setText("");
                bt.setManaged(false);
                lastButton.setManaged(false);
                lastButton = null;
                addScore();
                if (animationList.size() > 0) {
                    ArrayList<int[]> listWithBlocks = new ArrayList<>();
                    listWithBlocks.add(new int[]{r + 1, c + 1});
                    listWithBlocks.addAll(animationList);
                    listWithBlocks.add(new int[]{selectedRow + 1, selectedColumn + 1});
                    drawConnection(listWithBlocks);
                    PauseTransition pause = new PauseTransition(Duration.millis(150));
                    pause.setOnFinished(e -> {
                        if (collapse != 0) {
                            matrix.verticalCollapse(r + 1, c + 1, selectedRow + 1, selectedColumn + 1);
                            if (collapse == 2) {
                                matrix.columnCollapse(c + 1, selectedColumn + 1);
//                                matrix.horizontalCollapse(r + 1, c + 1, selectedRow + 1, selectedColumn + 1);
                            }
                        }
                        draw();
                    });
                    pause.play();
                } else {
                    if (collapse != 0) {
                        matrix.verticalCollapse(r + 1, c + 1, selectedRow + 1, selectedColumn + 1);
                        if (collapse == 2) {
                            matrix.columnCollapse(c + 1, selectedColumn + 1);
//                            matrix.horizontalCollapse(r + 1, c + 1, selectedRow + 1, selectedColumn + 1);
                        }
                    }
                    draw();
                }
                if (matrix.isWin()) {
                    showWin();
                }
                checkLost();
            }
        }
        changeButtonStatus();
        isConnecting = !isConnecting;
    }

    private void changeButtonStatus() {
        if (!isConnecting) {
            if (blockType == Matrix.ALPHABET) {
                lastButton.setFont(new Font(15));
                lastButton.setTextFill(Color.LIMEGREEN);
            } else {
                lastButton.setStyle(String.format("-fx-border-color: red; -fx-border-width: 5px; " +
                        "-fx-background-color: #%s", matrix.getBlock(selectedRow, selectedColumn).toString()));
            }
        } else if (lastButton != null) {
            if (blockType == Matrix.ALPHABET) {
                lastButton.setFont(new Font(12));
                lastButton.setTextFill(Color.BLACK);
            } else {
                lastButton.setStyle(String.format("-fx-background-color: #%s",
                        matrix.getBlock(selectedRow, selectedColumn).toString()));
            }
        }
    }

    private void addScore() {
        score += 1;
        scoreLabel.setText(String.valueOf(score));
    }

    private void subtractScore() {
        score -= scoreMultiplier;
        scoreLabel.setText(String.valueOf(score));
        scoreMultiplier *= 2;
        costLabel.setText(String.valueOf(scoreMultiplier));
    }

    private void checkLost() {
        if (matrix.getHint() == null) {
            if (restartTime > 0) {
                autoWash();
            } else {
                if (score < scoreMultiplier) {
                    showLost();
                }
            }
        }
    }

    private void showWin() {
        isRunning = false;
        Alert winInfo = new Alert(Alert.AlertType.INFORMATION);
        winInfo.setTitle("666");
        winInfo.setHeaderText("你赢了");
        winInfo.setContentText(String.format("真tm6\n得分：%d，用时：%d", score, timer.getTimeSec()));
        winInfo.showAndWait();

        close();
    }

    private void showLost() {
        isRunning = false;
        Alert winInfo = new Alert(Alert.AlertType.INFORMATION);
        winInfo.setTitle("777");
        winInfo.setHeaderText("你个垃圾");
        winInfo.setContentText(String.format("你没救了\n得分：%d，用时：%d", score, timer.getTimeSec()));
        winInfo.showAndWait();

        close();
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
            throw new RuntimeException("???");
        }
    }

    void updateTime(long mills) {
        Platform.runLater(() -> timeLabel.setText(String.valueOf(mills / 1000)));
    }

    private void close() {
        primaryStage.close();
    }
}


class Timer implements Runnable {

    private long timeCountMills;

    private GameUI parent;

    Timer(GameUI parent) {
        this.parent = parent;
    }

    @SuppressWarnings("all")
    int getTimeSec() {
        return (int) Math.ceil(timeCountMills / 1000);
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long lastUpdatedTime = startTime;
        long currentTime;
        while (parent.isRunning) {
            currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdatedTime >= 1000) {
                lastUpdatedTime = currentTime;
                timeCountMills = currentTime - startTime;
                parent.updateTime(timeCountMills);
            }
        }
    }
}
