package FinalProject;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Game extends Application {
    public static final float SCENE_WIDTH = 600.0f;
    public static final float SCENE_HEIGHT = 800.0f;

    private StackPane superRoot;
    private Pane root;
    private GameOverMenu gameOverMenu;
    private Sun sun;
    private Paddle paddle;
    private Driver driver;
    private int score;
    private ArrayList<Star> stars;

    public void init() {
        score = 0;
        stars = new ArrayList<>();
    }

    public static void main(String[] args) {
        launch(args);
    }
    //TODO show score on top while playing
    // TODO more elaborate scoring routine
    // TODO fix bug where when play again goes straight to game over

    @Override
    public void start(Stage stage) {
        // set up stage
        stage.setTitle("Sunset");
        root = new Pane();
        superRoot = new StackPane();
        superRoot.getChildren().add(root);
//        root.setFocusTraversable(true);
//        root.requestFocus();
        Scene scene = new Scene(superRoot, SCENE_WIDTH, SCENE_HEIGHT);
        setUpStage(stage);
        stage.setScene(scene);
        stage.show();

        // create game over menu
        gameOverMenu = new GameOverMenu(this);
        superRoot.getChildren().add(gameOverMenu);

        // create the sun
        sun = new Sun(this);
        root.getChildren().add(sun);

        // create paddle
        paddle = new Paddle(this);
        root.getChildren().add(paddle);

        // move listener for mouse to move paddle
        root.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                paddle.handleMouseMove(new Point(event.getX(), event.getY()));
            }
        });

        // move listener for keyboard left and right or A and D
//        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                System.out.println("pressed");
//                if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
//                    System.out.println("Left");
//                    paddle.handleKeyMove(-1.0f);
//                }
//                else if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
//                    System.out.println("Right");
//                    paddle.handleKeyMove(1.0f);
//                }
//            }
//        });

        // set up the animation for sun
        driver = new Driver(this);
        driver.start();
    }

    // prevent stretching of edges
    private void setUpStage(Stage stage) {
        stage.setMinWidth(SCENE_WIDTH);
        stage.setMaxWidth(SCENE_WIDTH);
        stage.setMinHeight(SCENE_HEIGHT);
        stage.setMaxHeight(SCENE_HEIGHT);
    }

    public Sun getSun() {
        return sun;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void createStar(Point start, Point dest) {
        Star s = new Star(start, dest);
        stars.add(s);
        root.getChildren().add(s);
    }

    public void handleGameOver(boolean win) {
        // stop whatever driver is doing to keep the computer sane
        driver.stop();

        if(win) {
            // make stars shine animation
            // TODO
        }

        // show game over menu
        // TODO calculate the score as you go
        gameOverMenu.show(win, score);
    }

    public void handleStartOver() {
        // hide game over menu
        gameOverMenu.hide();

        // reset sun and driver
        sun.reset();
        driver.start();

        // remove all stars
        root.getChildren().removeAll(stars);
        stars.clear();

        // reset score
        score = 0;
    }

    public void increaseScore(int amt) {
        score += amt;
    }
}
