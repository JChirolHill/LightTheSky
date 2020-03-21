package FinalProject;

import javafx.animation.AnimationTimer;

public class Driver extends AnimationTimer {
    private Game game;
    private long prevTime;
    private boolean firstTime = true;

    public Driver(Game game) {
        super();
        this.game = game;
    }

    @Override
    public void handle(long now) {
        if(firstTime) {
            prevTime = now;
            firstTime = false;
        } else {
            double delta = (now - prevTime) * 1.0e-9;
            prevTime = now;
            game.getSun().move(delta);
        }
    }
}