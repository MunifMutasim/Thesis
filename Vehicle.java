package thesisfinal;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.*;
import static thesisfinal.Misc.*;
import static thesisfinal.DhakaSimFrame.*;

public class Vehicle {
    private int type;
    private double length;
    private double width;
    private int numberOfStrips;
    private double speed;
    private double acceleration;
    private Color color;
    private int demandIndex;
    private int pathIndex;
    private int linkIndexOnPath;
    private double x1;
    private double x2;
    private double x3;
    private double x4;
    private double y1;
    private double y2;
    private double y3;
    private double y4;
    private double segStartX;
    private double segStartY;
    private double segEndX;
    private double segEndY;
    private double juncStartX;
    private double juncStartY;
    private double juncEndX;
    private double juncEndY;
    private boolean junctionMode;
    private boolean reverseLink;
    private boolean reverseSegment;
    private boolean passedSensor;
    private boolean toRemove;
    private Link link;
    private int segmentIndex;
    private int stripIndex;
    private double distanceInSegment;
    private Node node;
    private int pseudoIndex;
    private int distanceInJunction;
    private Vehicle leader;

    public Vehicle(int type, double speed, double acceleration, Color color, int demandIndex, int pathIndex, int linkIndexOnPath, double segStartX, double segStartY, double segEndX, double segEndY, boolean reverseLink, boolean reverseSegment, Link link, int segmentIndex, int stripIndex) {
        this.type = type;

        this.length = Length(type);
        this.width = Width(type);
        this.numberOfStrips = numberOfStrips(type);

        this.speed = speed;
        this.acceleration = acceleration;
        this.color = color;
        this.demandIndex = demandIndex;
        this.pathIndex = pathIndex;
        this.linkIndexOnPath = linkIndexOnPath;
        this.segStartX = segStartX;
        this.segStartY = segStartY;
        this.segEndX = segEndX;
        this.segEndY = segEndY;

        this.junctionMode = false;

        this.reverseLink = reverseLink;
        this.reverseSegment = reverseSegment;

        this.passedSensor = false;
        this.toRemove = false;

        this.link = link;
        this.segmentIndex = segmentIndex;
        this.stripIndex = stripIndex;

        this.distanceInSegment = 0.1;

        occupyStrips();

    }

    public void occupyStrips() {
        Segment segment = link.getSegment(segmentIndex);
        for (int i = 0; i < numberOfStrips; i++) {
            segment.getStrip(stripIndex + i).addVehicle(this);
        }
    }

    public void freeStrips() {
        for (int i = stripIndex; i < stripIndex + numberOfStrips; i++) {
            link.getSegment(segmentIndex).getStrip(i).delVehicle(this);
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getNumberOfStrips() {
        return numberOfStrips;
    }

    public void setNumberOfStrips(int numberOfStrips) {
        this.numberOfStrips = numberOfStrips;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDemandIndex() {
        return demandIndex;
    }

    public void setDemandIndex(int demandIndex) {
        this.demandIndex = demandIndex;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    public int getLinkIndexOnPath() {
        return linkIndexOnPath;
    }

    public void setLinkIndexOnPath(int linkIndexOnPath) {
        this.linkIndexOnPath = linkIndexOnPath;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getX3() {
        return x3;
    }

    public void setX3(double x3) {
        this.x3 = x3;
    }

    public double getX4() {
        return x4;
    }

    public void setX4(double x4) {
        this.x4 = x4;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public double getY3() {
        return y3;
    }

    public void setY3(double y3) {
        this.y3 = y3;
    }

    public double getY4() {
        return y4;
    }

    public void setY4(double y4) {
        this.y4 = y4;
    }

    public double getSegStartX() {
        return segStartX;
    }

    public void setSegStartX(double segStartX) {
        this.segStartX = segStartX;
    }

    public double getSegStartY() {
        return segStartY;
    }

    public void setSegStartY(double segStartY) {
        this.segStartY = segStartY;
    }

    public double getSegEndX() {
        return segEndX;
    }

    public void setSegEndX(double segEndX) {
        this.segEndX = segEndX;
    }

    public double getSegEndY() {
        return segEndY;
    }

    public void setSegEndY(double segEndY) {
        this.segEndY = segEndY;
    }

    public double getJuncStartX() {
        return juncStartX;
    }

    public void setJuncStartX(double juncStartX) {
        this.juncStartX = juncStartX;
    }

    public double getJuncStartY() {
        return juncStartY;
    }

    public void setJuncStartY(double juncStartY) {
        this.juncStartY = juncStartY;
    }

    public double getJuncEndX() {
        return juncEndX;
    }

    public void setJuncEndX(double juncEndX) {
        this.juncEndX = juncEndX;
    }

    public double getJuncEndY() {
        return juncEndY;
    }

    public void setJuncEndY(double juncEndY) {
        this.juncEndY = juncEndY;
    }

    public boolean isJunctionMode() {
        return junctionMode;
    }

    public void setJunctionMode(boolean junctionMode) {
        this.junctionMode = junctionMode;
    }

    public boolean isReverseLink() {
        return reverseLink;
    }

    public void setReverseLink(boolean reverseLink) {
        this.reverseLink = reverseLink;
    }

    public boolean isReverseSegment() {
        return reverseSegment;
    }

    public void setReverseSegment(boolean reverseSegment) {
        this.reverseSegment = reverseSegment;
    }

    public boolean isPassedSensor() {
        return passedSensor;
    }

    public void setPassedSensor(boolean passedSensor) {
        this.passedSensor = passedSensor;
    }

    public boolean isToRemove() {
        return toRemove;
    }

    public void setToRemove(boolean toRemove) {
        this.toRemove = toRemove;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public int getSegmentIndex() {
        return segmentIndex;
    }

    public void setSegmentIndex(int segmentIndex) {
        this.segmentIndex = segmentIndex;
    }

    public int getStripIndex() {
        return stripIndex;
    }

    public void setStripIndex(int stripIndex) {
        this.stripIndex = stripIndex;
    }

    public double getDistanceInSegment() {
        return distanceInSegment;
    }

    public void setDistanceInSegment(double distanceInSegment) {
        this.distanceInSegment = distanceInSegment;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getPseudoIndex() {
        return pseudoIndex;
    }

    public void setPseudoIndex(int pseudoIndex) {
        this.pseudoIndex = pseudoIndex;
    }

    public int getDistanceInJunction() {
        return distanceInJunction;
    }

    public void setDistanceInJunction(int distanceInJunction) {
        this.distanceInJunction = distanceInJunction;
    }

    public Vehicle getLeader() {
        return leader;
    }

    public void setLeader(Vehicle leader) {
        this.leader = leader;
    }


    public void moveright() {
        Segment segment = link.getSegment(segmentIndex);
        if (stripIndex + numberOfStrips < segment.numberOfStrips()
                && segment.getStrip(stripIndex + numberOfStrips).isGapForStripChange(this)) {
            segment.getStrip(stripIndex).delVehicle(this);
            segment.getStrip(stripIndex + numberOfStrips).addVehicle(this);
            setStripIndex(stripIndex + 1);
        }

    }

    public void moveleft() {
        Segment segment = link.getSegment(segmentIndex);
        if (stripIndex != 1
                && segment.getStrip(stripIndex - 1).isGapForStripChange(this)) {
            segment.getStrip(stripIndex + numberOfStrips - 1).delVehicle(this);
            segment.getStrip(stripIndex - 1).addVehicle(this);
            setStripIndex(stripIndex - 1);
        }
    }

    //faulty
    public boolean controlSpeed() {
      
        //System.out.println("Control Speed Link Number "+this.getLink().getId());
        if (speed < maximumSpeed) {
            speed += acceleration;
        }
        for (int i = stripIndex; i < stripIndex + numberOfStrips; i++) {
            if (link.getSegment(segmentIndex).getStrip(i).isGapforForwardMovement(this) == false) {
                //System.out.println("No Gap for Forward movement in "+this.getLink().getId());
                if (acceleration == 0 || (speed -= acceleration) <= 0) {
                    return false;
                }
                i--;
            }
        }
        return true;
    }

    //faulty
    public boolean moveforward() {
        System.out.println("MOVEEEE FORWARDDDDDDDDDDDD");
        //System.out.println("Move Forward Node Number "+this.getNode().getId());
        //System.out.println("Move Forward Link Number "+this.getLink().getId());
        Segment segment = link.getSegment(segmentIndex);
        if (!junctionMode) {
            
            if (controlSpeed()) {
                distanceInSegment += speed;
                if (!passedSensor) {
                    if (distanceInSegment > segment.getSensor()) {
                        passedSensor = true;
                        
                    }
                }
                //System.out.println("Not JunctionMode control speed = true");
                return true;
            }
            //System.out.println("Not JunctionMode control speed = false");
            return false;
        } else {
            //System.out.println("Junction Mode");
            //System.out.println("Node "+this.node.getId());
            //System.out.println("Link "+this.getLink().getId());
            speed += acceleration;
            distanceInJunction += speed;
            calculateCornerPoints();
            if (node.doOverlap(this)) {
                distanceInJunction -= speed;
                speed -= acceleration;
                
            }
        }
        return false;
    }

    //faulty
    public void calculateCornerPoints() {
        double x_1 = node.getPseudo(pseudoIndex).startPointX;
        double x_2 = node.getPseudo(pseudoIndex).endPointX;
        double y_1 = node.getPseudo(pseudoIndex).startPointY;
        double y_2 = node.getPseudo(pseudoIndex).endPointY;
        double p_s_l = Math.sqrt((x_1 - x_2) * (x_1 - x_2) + (y_1 - y_2) * (y_1 - y_2));
        double l = getLength() * DhakaSimFrame.pixelPerMeter;
        double d_i_j = distanceInJunction * DhakaSimFrame.pixelPerMeter;
        double t1 = d_i_j / p_s_l;
        x1 = (1 - t1) * x_1 + t1 * x_2;
        y1 = (1 - t1) * y_1 + t1 * y_2;
        double t2 = (d_i_j + l) / p_s_l;
        x2 = (1 - t2) * x_1 + t2 * x_2;
        y2 = (1 - t2) * y_1 + t2 * y_2;
        int w = getNumberOfStrips() * DhakaSimFrame.pixelPerFootpathStrip;
        x3 = Misc.returnX3(x1, y1, x2, y2, w);
        y3 = Misc.returnY3(x1, y1, x2, y2, w);
        x4 = Misc.returnX4(x1, y1, x2, y2, w);
        y4 = Misc.returnY4(x1, y1, x2, y2, w);
    }

    public boolean isAtJunctionEnd() {
        Pseudo pseudo = node.getPseudo(pseudoIndex);
        return (distanceInJunction + length + speed + 0.1) * pixelPerMeter > pseudo.getLength();
    }

    public boolean isAtSegmentEnd() {
        Segment segment = link.getSegment(segmentIndex);
        return distanceInSegment + length + speed + 0.1 > segment.getLength();
    }


    public void segmentChange(int segmentIndex, int stripIndex) {
        this.segmentIndex = segmentIndex;
        this.stripIndex = stripIndex;
        this.distanceInSegment = 0.1;
        this.passedSensor = false;
        occupyStrips();
    }

    public void linkChange(int linkIndexOnPath, Link link, int segmentIndex, int stripIndex) {
        this.linkIndexOnPath = linkIndexOnPath;
        this.link = link;
        segmentChange(segmentIndex, stripIndex);
    }

    public static double Width(int type) {
        switch (type) {
            case 1:
                return 1.76;
            case 2:
                return 1.78;
            case 3:
                return 2.13;
            case 4:
                return 2.02;
            case 5:
                return 1.8;
            case 6:
                return 1.3;
            case 7:
                return 1.22;
            case 8:
                return 0.75;
            case 9:
                return 0.61;
            case 10:
                return 2.46;
            case 11:
                return 2.44;
            default:
                return 1.76;
        }
    }

    public static double Length(int type) {
        switch (type) {
            case 1:
                return 4.54;
            case 2:
                return 4.29;
            case 3:
                return 4.47;
            case 4:
                return 5.78;
            case 5:
                return 5.5;
            case 6:
                return 2.63;
            case 7:
                return 2.51;
            case 8:
                return 2.13;
            case 9:
                return 1.78;
            case 10:
                return 8.46;
            case 11:
                return 6.7;
            default:
                return 4.54;
        }
    }

    public static int numberOfStrips(int type) {
        return (int) Math.ceil(Width(type) / stripWidth);
    }

    //faulty
    public void drawVehicle(BufferedWriter log, Graphics g, int pixelPerStrip, int pixelPerMeter, int pixelPerFootPathStrip) {
        if (!junctionMode) {

            Segment segment = link.getSegment(segmentIndex);
            double segmentLength = segment.getLength();
            //System.out.println("in normal mode"+segmentLength);
            //Math.sqrt(Math.pow(seg.getStartingPntX()-seg.getEndPntX(), 2)+Math.pow(seg.getStartingPntY()-seg.getEndPntY(), 2));

            //--->strategy: if its source then change it to int right away
            //int length = (int) Math.ceil(getLength());
            //Using internally section or ratio formula,it finds the coordinates along which vehicles are
            double xp = (getDistanceInSegment() * segEndX + (segmentLength - getDistanceInSegment()) * segStartX) / segmentLength * pixelPerMeter;
            double yp = (getDistanceInSegment() * segEndY + (segmentLength - getDistanceInSegment()) * segStartY) / segmentLength * pixelPerMeter;
            double xq = ((getDistanceInSegment() + length) * segEndX + (segmentLength - (getDistanceInSegment() + length)) * segStartX) / segmentLength * pixelPerMeter;
            double yq = ((getDistanceInSegment() + length) * segEndY + (segmentLength - (getDistanceInSegment() + length)) * segStartY) / segmentLength * pixelPerMeter;

            int w = /*segment.getFootPathStripCount()*/ 1 * pixelPerFootPathStrip + (stripIndex - /*segment.getFootPathStripCount()*/ 1) * pixelPerStrip;
            //finds the coordinates of vehicles starting and ending upper points depending on which strip their upper(left) portion are.
            int x1, y1, x2, y2;
            if (reverseSegment) {
                x1 = (int) Math.round(Misc.returnX5(xp, yp, xq, yq, w));
                y1 = (int) Math.round(Misc.returnY5(xp, yp, xq, yq, w));
                x2 = (int) Math.round(Misc.returnX6(xp, yp, xq, yq, w));
                y2 = (int) Math.round(Misc.returnY6(xp, yp, xq, yq, w)); //(v.getStrip().getStripIndex())*stripPixelCount
            } else {
                x1 = (int) Math.round(Misc.returnX3(xp, yp, xq, yq, w));
                y1 = (int) Math.round(Misc.returnY3(xp, yp, xq, yq, w));
                x2 = (int) Math.round(Misc.returnX4(xp, yp, xq, yq, w));
                y2 = (int) Math.round(Misc.returnY4(xp, yp, xq, yq, w)); //(v.getStrip().getStripIndex())*stripPixelCount                
            }
            //int width = (int) Math.ceil(getWidth())*stripPixelCount;
            int width = (int) Math.round(this.width * pixelPerMeter);
            //finds the coordinates of perpendicularly oppsite lower(right) points
            int x3, y3, x4, y4;
            if (reverseSegment) {
                x3 = (int) Math.round(Misc.returnX5(xp, yp, xq, yq, w + width));
                y3 = (int) Math.round(Misc.returnY5(xp, yp, xq, yq, w + width));
                x4 = (int) Math.round(Misc.returnX6(xp, yp, xq, yq, w + width));
                y4 = (int) Math.round(Misc.returnY6(xp, yp, xq, yq, w + width));
            } else {
                x3 = (int) Math.round(Misc.returnX3(xp, yp, xq, yq, w + width));
                y3 = (int) Math.round(Misc.returnY3(xp, yp, xq, yq, w + width));
                x4 = (int) Math.round(Misc.returnX4(xp, yp, xq, yq, w + width));
                y4 = (int) Math.round(Misc.returnY4(xp, yp, xq, yq, w + width));
            }
            g.setColor(color);
            int[] xs = {x1, x2, x4, x3};
            int[] ys = {y1, y2, y4, y3};
            g.fillPolygon(xs, ys, 4);
            try {
                log.write(x1 + " " + x2 + " " + x3 + " " + x4 + " " + y1 + " " + y2 + " " + y3 + " " + y4);
                log.newLine();
            } catch (IOException ex) {
                Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

            //int distanceInJunc = 10;
            double segmentLength = Math.sqrt(Math.pow(node.getPseudo(pseudoIndex).startPointX - node.getPseudo(pseudoIndex).endPointX, 2) + Math.pow(node.getPseudo(pseudoIndex).startPointY - node.getPseudo(pseudoIndex).endPointY, 2));
            //Math.sqrt(Math.pow(seg.getStartingPntX()-seg.getEndPntX(), 2)+Math.pow(seg.getStartingPntY()-seg.getEndPntY(), 2));
            //System.out.println("in junction mode: " + segmentLength);
            //double len = getLength() * mpRatio;//(int) Math.ceil(getLength());
            //int length = (int) len;//length *= mpRatio;
            int length = (int) (getLength() * pixelPerMeter);

            int dis = distanceInJunction * pixelPerMeter;
            //Using internally section or ratio formula,it finds the coordinates along which vehicles are
            double xp = (dis * node.getPseudo(pseudoIndex).endPointX + (segmentLength - dis) * node.getPseudo(pseudoIndex).startPointX) / segmentLength;// * mpRatio;
            double yp = (dis * node.getPseudo(pseudoIndex).endPointY + (segmentLength - dis) * node.getPseudo(pseudoIndex).startPointY) / segmentLength;// * mpRatio;
            double xq = ((dis + length) * node.getPseudo(pseudoIndex).endPointX + (segmentLength - (dis + length)) * node.getPseudo(pseudoIndex).startPointX) / segmentLength;// * mpRatio;
            double yq = ((dis + length) * node.getPseudo(pseudoIndex).endPointY + (segmentLength - (dis + length)) * node.getPseudo(pseudoIndex).startPointY) / segmentLength;// * mpRatio;

            int x1 = (int) Math.round(xp);
            int y1 = (int) Math.round(yp);
            int x2 = (int) Math.round(xq);
            int y2 = (int) Math.round(yq);

            //int w = seg.getFpStripCount() * fpStripPixelCount + (getStrip().getStripIndex() - seg.getFpStripCount()) * stripPixelCount;
            //finds the coordinates of vehicles starting and ending upper points depending on which strip their upper(left) portion are.
            //int x1 = (int) Math.round(XY.returnX3(xp, yp, xq, yq, w));
            //int y1 = (int) Math.round(XY.returnY3(xp, yp, xq, yq, w));
            //int x2 = (int) Math.round(XY.returnX4(xp, yp, xq, yq, w));
            //int y2 = (int) Math.round(XY.returnY4(xp, yp, xq, yq, w)); //(v.getStrip().getStripIndex())*stripPixelCount
            //int width = (int) Math.ceil(getWidth())*stripPixelCount;
            //int width = segment.getFootPathStripCount() * pixelPerFootPathStrip + (getStrip().getStripIndex() - segment.getFootPathStripCount()) * pixelPerStrip;
            int width = (int) Math.round(this.width * pixelPerMeter);

            int x3, y3, x4, y4;
            //finds the coordinates of perpendicularly oppsite lower(right) points
            if (reverseSegment) {
                x3 = (int) Math.round(Misc.returnX5(x1, y1, x2, y2, width));
                y3 = (int) Math.round(Misc.returnY5(x1, y1, x2, y2, width));
                x4 = (int) Math.round(Misc.returnX6(x1, y1, x2, y2, width));
                y4 = (int) Math.round(Misc.returnY6(x1, y1, x2, y2, width));
            } else {
                x3 = (int) Math.round(Misc.returnX3(x1, y1, x2, y2, width));
                y3 = (int) Math.round(Misc.returnY3(x1, y1, x2, y2, width));
                x4 = (int) Math.round(Misc.returnX4(x1, y1, x2, y2, width));
                y4 = (int) Math.round(Misc.returnY4(x1, y1, x2, y2, width));
            }
            g.setColor(color);
            int[] xs = {x1, x2, x4, x3};
            int[] ys = {y1, y2, y4, y3};
            g.fillPolygon(xs, ys, 4);
            try {
                log.write(x1 + " " + x2 + " " + x3 + " " + x4 + " " + y1 + " " + y2 + " " + y3 + " " + y4);
                log.newLine();
            } catch (IOException ex) {
                Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
