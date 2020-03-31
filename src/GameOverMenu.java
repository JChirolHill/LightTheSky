import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class GameOverMenu extends VBox {
    private static final String GAME_OVER_MSG = "Wait for the clouds to pass\nand gaze at the stars";
    private static final String SCORE_FILE = "scoreboard.txt";

    private Game game;
    private Text textFeedback;
    private Text textScore;
    private Text textHighScore;
    private int highScore;
    private ArrayList<Pair<Date, Integer>> scores;

    public GameOverMenu(Game game, Scene scene) {
        this.game = game;
        this.scores = new ArrayList<>();
        this.setMaxSize(Game.SCENE_WIDTH * 0.75, Game.SCENE_HEIGHT * 0.5);
        this.setAlignment(Pos.CENTER);
        this.setOpacity(0);
        this.setVisible(false);
        this.textFeedback = new Text();
        this.textFeedback.setFill(Color.WHITE);
        this.textFeedback.setTextAlignment(TextAlignment.CENTER);
        this.textFeedback.setText(GAME_OVER_MSG);
        this.textFeedback.setFont(Font.loadFont(getClass().getResourceAsStream("Assets/ShakeItOff.ttf"), 30));
        this.textScore = new Text();
        this.textScore.setFill(Color.WHITE);
        this.textScore.setFont(Font.loadFont(getClass().getResourceAsStream("Assets/ShakeItOff.ttf"), 20));
        this.textHighScore = new Text();
        this.textHighScore.setFill(Color.WHITE);
        this.textHighScore.setFont(Font.loadFont(getClass().getResourceAsStream("Assets/ShakeItOff.ttf"), 20));
        this.highScore = 0;

        // show feedback text
        this.getChildren().add(textFeedback);

        // show score
        this.getChildren().add(textScore);

        // show high score
        this.getChildren().add(textHighScore);

        // show play again button
        Button again = new Button("Play Again?");
        again.setFont(Font.loadFont(getClass().getResourceAsStream("Assets/ShakeItOff.ttf"), 20));
        again.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        again.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        again.setStyle("-fx-border-radius: 30;-fx-border-color: white;-fx-stroke-width:5;");
        again.setTextFill(Color.WHITE);
        again.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // clean up screen to start again
                game.handleStartOver();
            }
        });
        this.getChildren().add(again);

        // get high score
        readInScores();
    }

    // show this menu, first update all displaying elements
    public void show(int score) {
        textScore.setText(String.format("Score: %d", score));

        // high score and save logic
        if(score > highScore) {
            highScore = score;
            textHighScore.setFill(Color.YELLOW);
        }
        else {
            textHighScore.setFill(Color.WHITE);
        }
        textHighScore.setText(String.format("High Score: %d", highScore));
        scores.add(new Pair<>(new Date(), score));
        writeScores();

        // fade in the menu
        this.setVisible(true);
        FadeTransition ft = new FadeTransition();
        ft.setNode(this);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDuration(new Duration(200));
        ft.play();
    }

    // hide this menu
    public void hide() {
        this.setOpacity(0);
        this.setVisible(false);
    }

    // performs file parsing to get scores for this user and extract high score
    private void readInScores() {
        try {
            File scoreFile = new File(SCORE_FILE);
            if(scoreFile.exists()) {
                Scanner sc = new Scanner(scoreFile);
                while(sc.hasNextLine()) {
                    String[] split = sc.nextLine().split(",");
                    if(split.length != 2) {
                        throw new IOException("Invalidly formatted file.");
                    }
                    scores.add(new Pair<>(new Date(split[0]), Integer.parseInt(split[1])));

                    // check if high score
                    if(Integer.parseInt(split[1]) > highScore) {
                        highScore = Integer.parseInt(split[1]);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // performs file writing to store high score externally to game
    private void writeScores() {
        File outputFile = new File(SCORE_FILE);

        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(outputFile));
            for(Pair<Date, Integer> score : scores) {
                writer.println(String.format("%s,%d", score.getKey(), score.getValue()));
            }
            writer.close();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
