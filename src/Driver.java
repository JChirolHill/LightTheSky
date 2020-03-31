import javafx.animation.AnimationTimer;

import java.util.ArrayList;

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

            // move sun
            game.getSun().move(delta);

            // move clouds
            ArrayList<Cloud> toRemove = new ArrayList<>();
            for(Cloud c : game.clouds) {
                Cloud temp = c.move(delta);
                if(temp != null) {
                    toRemove.add(temp);
                }
            }
            // delete all clouds that need deleting
            for(Cloud c : toRemove) {
                game.removeCloud(c);
            }

            // poll paddle to move if key pressed
            if(game.paddle.xDir != 0.0f) {
                game.paddle.handleKeyMove();
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.firstTime = true;
    }
}