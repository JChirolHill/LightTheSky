package FinalProject;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Sun extends Circle {
    private static final int POINTS_PER_HIT = 100;
    private static final int START_RADIUS = 20;
    private static final int RADIUS_DECREMENT = 5;
    private final int MIN_SPEED = 200;
    private final int MAX_SPEED = 300;
    private final Point START_POS = new Point(Game.SCENE_WIDTH / 2, 0);

    private Random rand;
    private Game game;
    private Point velocity;

    public Sun(Game game) {
        this.rand = new Random();
        this.game = game;
        this.velocity = new Point(calcRandVelocity(), calcRandVelocity());
        reset();
        this.setFill(new ImagePattern(new Image("FinalProject/hobbiton.jpg")));
    }

    public void reset() {
        this.setCenterX(START_POS.x);
        this.setCenterY(START_POS.y);
        this.setRadius(START_RADIUS);
    }

    public void registerHit() {
        // update score
        game.increaseScore(POINTS_PER_HIT);

        // reduce the radius size
        if(this.getRadius() - RADIUS_DECREMENT <= 0) {
            game.handleGameOver(true);
        }
        this.setRadius(this.getRadius() - RADIUS_DECREMENT);

        // create a small knockoff star
        game.createStar(new Point(this.getCenterX(), this.getCenterY()),
                new Point(rand.nextInt((int)Game.SCENE_WIDTH), rand.nextInt((int)(Game.SCENE_HEIGHT - Paddle.FROM_BOTTOM))));

        // put the sun back in front (so stays in front of all stars)
        this.toFront();
    }

    public void move(double delta) {
        this.setCenterX(this.getCenterX() + velocity.x * delta);
        this.setCenterY(this.getCenterY() + velocity.y * delta);

        if(this.getCenterX() - this.getRadius() < 0) {
            this.setCenterX(this.getRadius());
            velocity.x = calcRandVelocity();
        }
        if(this.getCenterY() - this.getRadius() < 0) {
            this.setCenterY(this.getRadius());
            velocity.y = calcRandVelocity();
        }
        if(this.getCenterX() + this.getRadius() > Game.SCENE_WIDTH) {
            this.setCenterX(Game.SCENE_WIDTH - this.getRadius());
            velocity.x = -1.0f * calcRandVelocity();
        }
        if(this.getCenterY() + this.getRadius() > Game.SCENE_HEIGHT) { // LOSE, sun fell below paddle
            game.handleGameOver(false);
//            this.setCenterY(Game.SCENE_HEIGHT - this.getRadius());
//            velocity.y = -1.0f * calcRandVelocity();
        }

        // check for paddle collisions
        if(game.getPaddle().didCollide(this)) {
            velocity.y = -1.0f * calcRandVelocity();
            registerHit();
        }
    }

    private double calcRandVelocity() {
        return MIN_SPEED + rand.nextInt(MAX_SPEED - MIN_SPEED);
    }
}
