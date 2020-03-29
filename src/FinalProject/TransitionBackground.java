package FinalProject;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

public class TransitionBackground extends StackPane {
    private static final String BACKGROUND_FILE = "FinalProject/Assets/sunset.png";
    private static final String FOREGROUND_FILE = "FinalProject/Assets/night.png";
    private static final String OCEAN_FILE_LIGHT = "FinalProject/Assets/lightoceanhighlight.png";
    private static final String OCEAN_FILE_DARK = "FinalProject/Assets/darkoceanhighlight.png";

    private Game game;
    private Pane background;
    private Pane foreground;
    private Pane oceanLight;

    public TransitionBackground(Game game) {
        // initialize
        this.game = game;
        this.background = new Pane();
        this.foreground = new Pane();

        // add to stack
        this.getChildren().add(background);
        this.getChildren().add(foreground);

        // create panes for ocean
        oceanLight = new Pane();
        oceanLight.setBackground(new Background(new BackgroundImage(new Image(OCEAN_FILE_LIGHT),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(Game.SCENE_WIDTH, Game.SCENE_HEIGHT, false, false, false, true))));
        this.getChildren().add(oceanLight);
        Pane oceanDark = new Pane();
        oceanDark.setBackground(new Background(new BackgroundImage(new Image(OCEAN_FILE_DARK),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(Game.SCENE_WIDTH, Game.SCENE_HEIGHT, false, false, false, true))));
        this.getChildren().add(oceanDark);

        // set images for each
        this.background.setBackground(new Background(new BackgroundImage(new Image(BACKGROUND_FILE),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(Game.SCENE_WIDTH, Game.SCENE_HEIGHT, false, false, false, true))));
        this.foreground.setBackground(new Background(new BackgroundImage(new Image(FOREGROUND_FILE),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(Game.SCENE_WIDTH, Game.SCENE_HEIGHT, false, false, false, true))));

        // hide foreground from start
        this.foreground.setOpacity(0);
        this.oceanLight.setOpacity(0);
    }

    public void transitionToForeground() {
        // transition for background
        FadeTransition ft1 = new FadeTransition();
        ft1.setNode(foreground);
        ft1.setFromValue(0);
        ft1.setToValue(1);
        ft1.setDuration(new Duration(3000));
        ft1.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                game.handleBackgroundTransitionOver();
            }
        });
        ft1.play();

        // transition to show ocean lines
        FadeTransition ft2 = new FadeTransition();
        ft2.setNode(oceanLight);
        ft2.setFromValue(0);
        ft2.setToValue(1);
        ft2.setDuration(new Duration(3000));
        ft2.play();
    }

    public void reset() {
        this.foreground.setOpacity(0);
        this.oceanLight.setOpacity(0);
    }
}
