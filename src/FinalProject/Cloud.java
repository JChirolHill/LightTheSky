package FinalProject;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Cloud extends Rectangle {
    private final Point CLOUD_DIMENS = new Point(373, 100);
    private final double SMALL_MULTIPLIER = 0.5;
    private final double LARGE_MULTIPLIER = 2;
    private final int LOWEST_FROM_PADDLE = (int)Paddle.FROM_BOTTOM - 40;
    private final int MIN_SPEED = 50;
    private final int MAX_SPEED = 100;

    public enum CloudSize {
        Small,
        Normal,
        Large
    }
    public enum CloudDirection {
        Left,
        Right
    }

    private Game game;
    private Random rand;
    private double velocity;
    private CloudSize size;
    private CloudDirection dir;

    public Cloud(Game game) {
        this.game = game;
        this.rand = new Random();
        this.velocity = calcRandVelocity();

        // set size and direction randomly
        CloudSize[] sizes = CloudSize.values();
        this.size = sizes[rand.nextInt(sizes.length)];
        CloudDirection[] dirs = CloudDirection.values();
        this.dir = dirs[rand.nextInt(dirs.length)];

        // set dimensions
        switch (size) {
            case Small:
                this.setWidth(SMALL_MULTIPLIER * CLOUD_DIMENS.x);
                this.setHeight(SMALL_MULTIPLIER * CLOUD_DIMENS.y);
                break;
            case Normal:
                this.setWidth(CLOUD_DIMENS.x);
                this.setHeight(CLOUD_DIMENS.y);
                break;
            case Large:
                this.setWidth(LARGE_MULTIPLIER * CLOUD_DIMENS.x);
                this.setHeight(LARGE_MULTIPLIER * CLOUD_DIMENS.y);
                break;
        }

        // set position
        if(dir == CloudDirection.Left) { // cloud moving left
            this.setX(Game.SCENE_WIDTH);
            this.setFill(new ImagePattern(new Image("FinalProject/Assets/cloudleft.png")));
        }
        else { // cloud moving right
            this.setX(-1 * this.getWidth());
            this.setFill(new ImagePattern(new Image("FinalProject/Assets/cloudright.png")));
        }
        this.setY(rand.nextInt((int)Game.SCENE_HEIGHT - (int)this.getHeight() - LOWEST_FROM_PADDLE));
    }

    // returns itself to signal should be destroyed, else returns null and moves appropriately
    public Cloud move(double delta) {
        if(dir == CloudDirection.Left) {
            this.setX(this.getX() - velocity * delta);
        }
        else {
            this.setX(this.getX() + velocity * delta);
        }

        // check if went off the side to destroy
        if((dir == CloudDirection.Left && this.getX() + this.getWidth() <= 0)
            || (dir == CloudDirection.Right && this.getX() >= Game.SCENE_WIDTH)) {
            return this;
        }
        return null;
    }

    private double calcRandVelocity() {
        return (this.dir == CloudDirection.Left ? -1 : 1) * (MIN_SPEED + rand.nextInt(MAX_SPEED - MIN_SPEED));
    }
}
