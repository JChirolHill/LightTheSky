package FinalProject;

import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.util.Duration;

public class Star extends Circle {
    private static final int STAR_RADIUS = 10;

    private Point dest;

    public Star(Point start, Point dest) {
        this.dest = dest;
        this.setFill(Color.ORANGE);
        this.setCenterX(start.x);
        this.setCenterY(start.y);
        this.setRadius(STAR_RADIUS);

        // set animation to get to destination
//        TranslateTransition tt = new TranslateTransition();
//        tt.setNode(this);
//        tt.setByX(dest.x - start.x);
//        tt.setByY(dest.y - start.y);
//        tt.play();
        CubicCurve cubiccurve = new CubicCurve(
                start.x, start.y, 0, 700, 600, 600, dest.x, dest.y);
        PathTransition pt = new PathTransition();
        pt.setNode(this);
        pt.setPath(cubiccurve);
        pt.setDuration(new Duration(1000));
        pt.play();
    }

//    private Point equiDist()
}
