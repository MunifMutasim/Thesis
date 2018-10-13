package thesisfinal;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import static java.lang.Math.*;
import static thesisfinal.Misc.*;
import static thesisfinal.DhakaSimFrame.*;

public class Misc {

    public static double returnX3(double x1, double y1, double x2, double y2, double d) {
        double u = x2 - x1;
        double v = y2 - y1;
        double temp = u;
        u = -v;
        v = temp;
        double denom = sqrt(u * u + v * v);
        u = u / denom;
        v = v / denom;
        return x1 + d * u;
    }

    public static double returnY3(double x1, double y1, double x2, double y2, double d) {
        double u = x2 - x1;
        double v = y2 - y1;
        double temp = u;
        u = -v;
        v = temp;
        double denom = sqrt(u * u + v * v);
        u = u / denom;
        v = v / denom;
        return y1 + d * v;
    }

    public static double returnX4(double x1, double y1, double x2, double y2, double d) {
        double u = x2 - x1;
        double v = y2 - y1;
        double temp = u;
        u = -v;
        v = temp;
        double denom = sqrt(u * u + v * v);
        u = u / denom;
        v = v / denom;
        return x2 + d * u;
    }

    public static double returnY4(double x1, double y1, double x2, double y2, double d) {
        double u = x2 - x1;
        double v = y2 - y1;
        double temp = u;
        u = -v;
        v = temp;
        double denom = sqrt(u * u + v * v);
        u = u / denom;
        v = v / denom;
        return y2 + d * v;
    }

    public static double returnX5(double x1, double y1, double x2, double y2, double d) {
        double u = x2 - x1;
        double v = y2 - y1;
        double temp = u;
        u = v;
        v = -temp;
        double denom = sqrt(u * u + v * v);
        u = u / denom;
        v = v / denom;
        return x1 + d * u;
    }

    public static double returnY5(double x1, double y1, double x2, double y2, double d) {
        double u = x2 - x1;
        double v = y2 - y1;
        double temp = u;
        u = v;
        v = -temp;
        double denom = sqrt(u * u + v * v);
        u = u / denom;
        v = v / denom;
        return y1 + d * v;
    }

    public static double returnX6(double x1, double y1, double x2, double y2, double d) {
        double u = x2 - x1;
        double v = y2 - y1;
        double temp = u;
        u = v;
        v = -temp;
        double denom = sqrt(u * u + v * v);
        u = u / denom;
        v = v / denom;
        return x2 + d * u;
    }

    public static double returnY6(double x1, double y1, double x2, double y2, double d) {
        double u = x2 - x1;
        double v = y2 - y1;
        double temp = u;
        u = v;
        v = -temp;
        double denom = sqrt(u * u + v * v);
        u = u / denom;
        v = v / denom;
        return y2 + d * v;
    }

    public static double getDistance(double x1, double y1, double x2, double y2) {
        return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static boolean doIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        Point2D.Double temp1 = new Point2D.Double(x1, y1);
        Point2D.Double temp2 = new Point2D.Double(x2, y2);
        Point2D.Double temp3 = new Point2D.Double(x3, y3);
        Point2D.Double temp4 = new Point2D.Double(x4, y4);
        boolean intersects = Line2D.linesIntersect(temp1.x, temp1.y, temp2.x, temp2.y, temp3.x, temp3.y, temp4.x, temp4.y);
        boolean shareAnyPoint = shareAnyPoint(temp1, temp2, temp3, temp4);
        return intersects && !shareAnyPoint;
    }

    public static boolean shareAnyPoint(Point2D.Double A, Point2D.Double B, Point2D.Double C, Point2D.Double D) {
        if (isPointOnTheLine(A, B, C)) {
            return true;
        } else if (isPointOnTheLine(A, B, D)) {
            return true;
        } else if (isPointOnTheLine(C, D, A)) {
            return true;
        } else {
            return isPointOnTheLine(C, D, B);
        }
    }

    public static boolean isPointOnTheLine(Point2D.Double A, Point2D.Double B, Point2D.Double P) {
        double m = (B.y - A.y) / (B.x - A.x);
        if (Double.isInfinite(m)) {
            return abs(A.x - P.x) < 0.001;
        }
        return abs((P.y - A.y) - m * (P.x - A.x)) < 0.001;
    }
}
