package thesisfinal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import sun.security.krb5.internal.crypto.Des3;

public class Node {
    private int index;
    private int id;
    //private 
            double x;
    //private 
            double y;
    private int timePassed;
    
    //private 
            ArrayList<Integer> linkList = new ArrayList<>();
    private ArrayList<Pair> pairList = new ArrayList<>();
    //private 
            ArrayList<Pseudo> pseudoList = new ArrayList<>();
    //private 
            ArrayList<Vehicle> vehicleList = new ArrayList<>();
    

    public Node(int index, int nodId, double x, double y) {
        this.index = index;
        this.id = nodId;
        this.x = x;
        this.y = y;
        this.timePassed = 0;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(int timePassed) {
        this.timePassed = timePassed;
    }
    
    public int getLink(int index) {
        return linkList.get(index);
    }
    
    public void addList(int link) {
        linkList.add(link);
    }
    
    public int numberOfLinks() {
        return linkList.size();
    }

    public Pair getPair(int index) {
        return pairList.get(index);
    }
    
    public void addPair(Pair pair) {
        pairList.add(pair);
    }
    
    public int numberOfPairs() {
        return pairList.size();
    }

    public Pseudo getPseudo(int index) {
        return pseudoList.get(index);
    }
    
    public void addPseudo(Pseudo pseudo) {
        pseudoList.add(pseudo);
    }
    
    public int numberOfPseudos() {
        return pseudoList.size();
    }
    
    public Vehicle getVehicle(int index) {
        return vehicleList.get(index);
    }
    
    public void addVehicle(Vehicle vehicle) {
        vehicleList.add(vehicle);
    }
    
    public int numberOfVehicles() {
        return vehicleList.size();
    }
        
    public boolean nodeClear() {
        //System.out.print("Inside Node clear for Node = "+this.id+"Number of pair "+this.numberOfPairs());
        //System.out.println("Number of pseudo "+this.numberOfPseudos());
        for (int i = 0; i < pairList.size(); i++) {
            if (pairList.get(i).getActive() != 0) {
                //found one pair which is active
                //System.out.println("Found One pair which is active");
                return false;
            }
        }
        for (int i = 0; i < pseudoList.size(); i++) {
            if (pseudoList.get(i).getVehicleCount() != 0) {
                //found one pesudo which has vehicles
                return false;
            }
        }
        return true;
    }

    public boolean contradicts(Pair pair1, Pair pair2) {
        double x1 = pair1.getX1();
        double x2 = pair1.getX2();
        double x3 = pair2.getX1();
        double x4 = pair2.getX2();
        double y1 = pair1.getY1();
        double y2 = pair1.getY2();
        double y3 = pair2.getY1();
        double y4 = pair2.getY2();
        double a = x1 - x2;
        double b = y1 - y2;
        double c = x3 - x4;
        double d = y3 - y4;
        double e = -(a * d / b * c) * x3 + a * (y3 - y1) / b + x1;
        double f = 1 - a * d / b * c;
        double x = e / f;
        double y = d * (x - x3) / c + y3;
        double g = Math.sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y));
        double h = Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
        double i = g + h;
        double j = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        float k = (float) i;
        float l = (float) j;
        //return k == l;
        double slope1 = (y2 - y1) / (x2 - x1);
        double slope2 = (y4 - y3) / (x4 - x3);
        return Math.abs(slope1) != Math.abs(slope2);
    }

    public void swtich(int simulationTime) {
        Random rand = new Random();
        for (int i = 0; i < pairList.size(); i++) {
            pairList.get(i).setActive(1);
        }
        if (simulationTime - timePassed > 30) {
            for (int i = 0; i < pairList.size(); i++) {
                pairList.get(i).setActive(0);
            }
            if (nodeClear()) {
                int select = 0;
                if (pairList.size() != 0) {
                    select = Math.abs(rand.nextInt() % pairList.size());
                }
                for (int i = 0; i < pairList.size(); i++) {
                    if (!contradicts(pairList.get(select), pairList.get(i))) {
                        pairList.get(i).setActive(1);
                    }
                }
                for (int i = 0; i < pairList.size(); i++) {
                    if (pairList.get(i).getActive() == 1) {
                        //System.out.print(pairiano.get(i).getX() + " " + pairiano.get(i).getY() + " ");
                    }
                }
                //System.out.println();
                //System.out.println();
                timePassed = simulationTime;
            }
        }
    }

    public boolean pairianoExists(int x, int y) {
        boolean exists = false;
        for (int i = 0; i < pairList.size(); i++) {
            if (pairList.get(i).getX() == x && pairList.get(i).getY() == y) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public boolean getPairianoActive(int x, int y) {
        int i;
        for (i = 0; i < pairList.size(); i++) {
            if (pairList.get(i).getX() == x && pairList.get(i).getY() == y) {
                break;
            }
        }
        return pairList.get(i).getActive() == 1;
    }

    public void addPairiano(int x, int y, double x1, double y1, double x2, double y2) {
        pairList.add(new Pair(x, y, x1, y1, x2, y2, 0));
    }

    boolean pseudoExists(int startLink, int startStrip, int endLink, int endStrip) {
        boolean exists = false;
        for (int i = 0; i < pseudoList.size(); i++) {
            if (pseudoList.get(i).startLink == startLink
                    && pseudoList.get(i).startStrip == startStrip
                    && pseudoList.get(i).endLink == endLink
                    && pseudoList.get(i).endStrip == endStrip) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    int getMyPseudo(int startLink, int startStrip, int endLink, int endStrip) {
        int i;
        for (i = 0; i < pseudoList.size(); i++) {
            if (pseudoList.get(i).startLink == startLink
                    && pseudoList.get(i).startStrip == startStrip
                    && pseudoList.get(i).endLink == endLink
                    && pseudoList.get(i).endStrip == endStrip) {
                break;
            }
        }
        return i;
    }





    public boolean collisionDetector(Vehicle v1, Vehicle v2) {
        Vector[] cornersOfRectangle = new Vector[8];
        cornersOfRectangle[0] = new Vector(v1.getX1(), v1.getY1());
        cornersOfRectangle[1] = new Vector(v1.getX2(), v1.getY2());
        cornersOfRectangle[2] = new Vector(v1.getX3(), v1.getY3());
        cornersOfRectangle[3] = new Vector(v1.getX4(), v1.getY4());
        cornersOfRectangle[4] = new Vector(v2.getX1(), v2.getY1());
        cornersOfRectangle[5] = new Vector(v2.getX2(), v2.getY2());
        cornersOfRectangle[6] = new Vector(v2.getX3(), v2.getY3());
        cornersOfRectangle[7] = new Vector(v2.getX4(), v2.getY4());
        Vector[] axis = new Vector[4];
        axis[0] = new Vector(cornersOfRectangle[0].x - cornersOfRectangle[1].x, cornersOfRectangle[0].y - cornersOfRectangle[1].y);
        axis[1] = new Vector(cornersOfRectangle[1].x - cornersOfRectangle[2].x, cornersOfRectangle[1].y - cornersOfRectangle[2].y);
        axis[2] = new Vector(cornersOfRectangle[4].x - cornersOfRectangle[5].x, cornersOfRectangle[4].y - cornersOfRectangle[5].y);
        axis[3] = new Vector(cornersOfRectangle[5].x - cornersOfRectangle[6].x, cornersOfRectangle[5].y - cornersOfRectangle[6].y);
        for (int i = 0; i < 4; i++) {
            double[] scalars = new double[8];
            for (int j = 0; j < 8; j++) {
                double x = cornersOfRectangle[j].dotProduct(axis[i]) * axis[i].x / axis[i].magnitudeSquare();
                double y = cornersOfRectangle[j].dotProduct(axis[i]) * axis[i].y / axis[i].magnitudeSquare();
                Vector projection = new Vector(x, y);
                scalars[j] = projection.dotProduct(axis[i]);
            }
            double minA = Math.min(scalars[0], Math.min(scalars[1], Math.min(scalars[2], scalars[3])));
            double maxA = Math.max(scalars[0], Math.max(scalars[1], Math.max(scalars[2], scalars[3])));
            double minB = Math.min(scalars[4], Math.min(scalars[5], Math.min(scalars[6], scalars[7])));
            double maxB = Math.max(scalars[4], Math.max(scalars[5], Math.max(scalars[6], scalars[7])));
            if (minB <= maxA) {
                continue;
            }
            if (maxB >= minA) {
                continue;
            }
            return false;
        }
        return true;
    }

    public boolean doOverlap(Vehicle v) {
        //System.out.println("");
        for(int i = 0; i < vehicleList.size(); i++) {
            if(vehicleList.get(i) != v) {
                vehicleList.get(i).calculateCornerPoints();
                if(collisionDetector(vehicleList.get(i), v)) {
                    return true;
                }
            }
        }
        return false;
    }
}
