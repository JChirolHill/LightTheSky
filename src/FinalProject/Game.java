package FinalProject;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class Game extends Application {
    public static final float SCENE_WIDTH = 600.0f;
    public static final float SCENE_HEIGHT = 800.0f;

    private StackPane superRoot;
    private Pane root;
    private TransitionBackground transBackground;
    private GameOverMenu gameOverMenu;
    private Text textScore;
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
    // TODO show score on top while playing
    // TODO more elaborate scoring routine

    @Override
    public void start(Stage stage) {
        // set up stage
        stage.setTitle("Sunset");
        root = new Pane();
        transBackground = new TransitionBackground(this, root);
        superRoot = new StackPane();
        superRoot.getChildren().add(transBackground);
//        root.setFocusTraversable(true);
//        root.requestFocus();
        Scene scene = new Scene(superRoot, SCENE_WIDTH, SCENE_HEIGHT);
        setUpStage(stage);
        stage.setScene(scene);
        stage.show();

        // create game over menu
        gameOverMenu = new GameOverMenu(this);
        superRoot.getChildren().add(gameOverMenu);

        // create score text in top right corner
        textScore = new Text();
        textScore.setFill(Color.WHITE);
        textScore.setX(SCENE_WIDTH - 100);
        textScore.setY(50);
        updateScore(0);
        root.getChildren().add(textScore);

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

    public void handleGameOver() {
        // stop whatever driver is doing to keep the computer sane
        driver.stop();

        // hide score display
        textScore.setVisible(false);

        // make stars shine animation and fade to night
        transBackground.transitionToForeground();
    }

    public void handleStartOver() {
        // hide game over menu
        gameOverMenu.hide();

        // reset necessary elements
        sun.reset();
        transBackground.reset();
        driver.start();
        textScore.setVisible(true);

        // remove all stars
        root.getChildren().removeAll(stars);
        stars.clear();

        // reset score
        score = 0;
    }

    public void handleBackgroundTransitionOver() {
        // show game over menu
        gameOverMenu.show(score);
    }

    // increase score by amt and update the display
    public void updateScore(int amt) {
        score += amt;
        textScore.setText(String.format("Score: %d", score));
    }

    public int getNumStars() {
        return this.stars.size();
    }
}
