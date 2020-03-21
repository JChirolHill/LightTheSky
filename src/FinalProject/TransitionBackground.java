package FinalProject;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class TransitionBackground extends StackPane {
    private static final String BACKGROUND_FILE = "FinalProject/sunset.jpg";
    private static final String FOREGROUND_FILE = "FinalProject/night.jpg";

    private Game game;
    private Pane background;
    private Pane foreground;

    public TransitionBackground(Game game, Pane root) {
        // initialize
        this.game = game;
        this.background = new Pane();
        this.foreground = new Pane();

        // add to stack
        this.getChildren().add(background);
        this.getChildren().add(foreground);
        this.getChildren().add(root);

        // set images for each
        this.background.setBackground(new Background(new BackgroundImage(new Image(BACKGROUND_FILE),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        this.foreground.setBackground(new Background(new BackgroundImage(new Image(FOREGROUND_FILE),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        // hide foreground from start
        this.foreground.setOpacity(0);
    }

    public void transitionToForeground() {
        FadeTransition ft = new FadeTransition();
        ft.setNode(foreground);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDuration(new Duration(2000));
        ft.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                game.handleBackgroundTransitionOver();
            }
        });
        ft.play();
    }
}
