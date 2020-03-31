import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.util.Duration;

public class Star extends Circle {
    public static final int STAR_RADIUS = 10;

    private Point dest;

    public Star(Point start, Point dest, boolean chirality) {
        this.dest = dest;
        this.setFill(new ImagePattern(new Image("Assets/star.png")));
        this.setCenterX(start.x);
        this.setCenterY(start.y);
        this.setRadius(STAR_RADIUS);

        // set animation to get to destination
        Point midpoint1 = new Point(chirality ? 0 : Game.SCENE_WIDTH, start.y - (dest.y - start.y) / 3);
        Point midpoint2 = new Point(start.y - (dest.y - start.y) * 2 / 3, chirality ? Game.SCENE_WIDTH : 0);
        CubicCurve cubiccurve = new CubicCurve(
                start.x, start.y, midpoint1.x, midpoint1.y, midpoint2.x, midpoint2.y, dest.x, dest.y);
        PathTransition pt = new PathTransition();
        pt.setNode(this);
        pt.setPath(cubiccurve);
        pt.setDuration(new Duration(1000));
        pt.play();
    }
}
