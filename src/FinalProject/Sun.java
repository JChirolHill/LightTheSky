package FinalProject;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Sun extends Circle {
    private static final int POINTS_PER_HIT = 100;
    private static final int RADIUS = 25;
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
        this.setRadius(RADIUS);
    }

    public void registerHit() {
        // update score
        game.increaseScore(POINTS_PER_HIT);

        // create a small knockoff star
        game.createStar(new Point(this.getCenterX(), this.getCenterY()),
                new Point(rand.nextInt((int)Game.SCENE_WIDTH), rand.nextInt((int)(Game.SCENE_HEIGHT - Paddle.FROM_BOTTOM))));

        // put the sun back in front (so stays in front of all stars)
        this.toFront();
    }

    public void move(double delta) {
        this.setCenterX(this.getCenterX() + velocity.x * delta);
        this.setCenterY(this.getCenterY() + velocity.y * delta);

        if(this.getCenterX() - this.getRadius() < 0) { // left wall
            this.setCenterX(this.getRadius());
            velocity.x *= -1;
        }
        if(this.getCenterY() - this.getRadius() < 0) { // top wall
            this.setCenterY(this.getRadius());
            velocity.y *= -1;
        }
        if(this.getCenterX() + this.getRadius() > Game.SCENE_WIDTH) { // right wall
            this.setCenterX(Game.SCENE_WIDTH - this.getRadius());
            velocity.x *= -1;
        }
        if(this.getCenterY() + this.getRadius() > Game.SCENE_HEIGHT) { // LOSE, sun fell below paddle
            game.handleGameOver();
        }

        // check for paddle collisions
        Paddle p = game.getPaddle();
        if(p.didCollide(this)) {
            // hit at left third
            if(getCenterX() < p.getX() + Paddle.PADDLE_DIMENS.x / 3.0f) {
                velocity = Point.reflect(velocity, Point.normalize(new Point(-1, -5)));
                setCenterY(getCenterY() - 5); // prevent from sticking to paddle
            }
            // hit at right third
            else if(getCenterX() > p.getX() + 2 * Paddle.PADDLE_DIMENS.x / 3.0f) {
                velocity = Point.reflect(velocity, Point.normalize(new Point(1, -5)));
                setCenterY(getCenterY() - 5); // prevent from sticking to paddle
            }
            // hit at middle
            else {
                velocity.y *= -1;
//                velocity.y = -1.0f * calcRandVelocity();
            }

            // correct for nearly horizonal velocity
            if(Math.abs(velocity.y) < 50) {
                velocity.y = -50;
            }

            // hit logic
            registerHit();
        }
    }

    private double calcRandVelocity() {
        return MIN_SPEED + rand.nextInt(MAX_SPEED - MIN_SPEED);
    }
}
