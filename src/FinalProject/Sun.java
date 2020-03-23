package FinalProject;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Sun extends Circle {
    private static final int POINTS_PER_HIT = 50;
    private static final int RADIUS = 25;
    private final int MIN_SPEED = 250;
    private final int MAX_SPEED = 350;
    private final Point START_POS = new Point(Game.SCENE_WIDTH / 2, 0);
    private final int MIN_Y_VEL = 80;
    private final int[] STAR_BOUNDARIES = {3, 10, 20};
    private final int[] POINT_AMOUNTS = {25, 50, 75}; // corresponds directly to point boundaries
    private final int[] CLOUD_AMOUNTS = {1, 3, 5}; // corresponds directly to point boundaries

    private Random rand;
    private Game game;
    private Point velocity;

    private enum PaddlePos {
        Left,
        Center,
        Right
    };

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

    public void registerHit(PaddlePos paddlePos) {
        // create small knockoff star (number depends on where hit on paddle)
        for(int i=0; i<(paddlePos == PaddlePos.Center ? 1 : 3); ++i) {
            game.createStar(new Point(this.getCenterX(), this.getCenterY()),
                    new Point(rand.nextInt((int)Game.SCENE_WIDTH), rand.nextInt((int)(Game.SCENE_HEIGHT - Paddle.FROM_BOTTOM))));
        }

        // update score (get more bonus points the more stars created)
        // add clouds based on number of stars already have
        int points = POINTS_PER_HIT; // baseline points per hit
        int numStars = game.getNumStars();
        int numCloudsToCreate = 0;
        for(int i=STAR_BOUNDARIES.length - 1; i>=0; --i) {
            if(numStars > STAR_BOUNDARIES[i]) {
                numCloudsToCreate = CLOUD_AMOUNTS[i]; // add more clouds
                points += POINT_AMOUNTS[i] * (paddlePos == PaddlePos.Center ? 1 : 3); // bonus points per star if have a lot of stars already
                break;
            }
        }
        game.updateScore(points);

        // create necessary clouds
        for(int i=0; i<numCloudsToCreate; ++i) {
            game.createCloud();
        }

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
                if(getCenterX() < p.getX() + Paddle.PADDLE_DIMENS.x / 6.0f) { // hit left edge
                    registerHit(PaddlePos.Left);
                }
                velocity = Point.reflect(velocity, Point.normalize(new Point(-1, -5)));
                setCenterY(Game.SCENE_HEIGHT - Paddle.FROM_BOTTOM - RADIUS - 5); // prevent from sticking to paddle
                registerHit(PaddlePos.Center);
            }
            // hit at right third
            else if(getCenterX() > p.getX() + 2 * Paddle.PADDLE_DIMENS.x / 3.0f) {
                if(getCenterX() > p.getX() + 2 * Paddle.PADDLE_DIMENS.x / 6.0f) { // hit left edge
                    registerHit(PaddlePos.Right);
                }
                velocity = Point.reflect(velocity, Point.normalize(new Point(1, -5)));
                setCenterY(Game.SCENE_HEIGHT - Paddle.FROM_BOTTOM - RADIUS - 5); // prevent from sticking to paddle
                registerHit(PaddlePos.Center);
            }
            // hit at middle
            else {
                velocity.y *= -1;
//                velocity.y = -1.0f * calcRandVelocity();
                registerHit(PaddlePos.Center);
            }

            // correct for nearly horizonal velocity
            if(Math.abs(velocity.y) < MIN_Y_VEL) {
                velocity.y = -1 * MIN_Y_VEL;
            }
        }
    }

    private double calcRandVelocity() {
        return MIN_SPEED + rand.nextInt(MAX_SPEED - MIN_SPEED);
    }
}
