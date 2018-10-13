package thesisfinal;

import static java.lang.Math.*;
import static thesisfinal.Misc.*;
import static thesisfinal.DhakaSimFrame.*;

public class Vector {

    double x;
    double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double dotProduct(Vector v) {
        return x * v.x + y * v.y;
    }
    
    public double magnitude() {
        return sqrt(magnitudeSquare());
    }
    
    public double magnitudeSquare() {
        return dotProduct(this);
    }
    
}
