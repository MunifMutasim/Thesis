package thesisfinal;

import java.util.ArrayList;

import static java.lang.Math.*;
import static thesisfinal.Misc.*;
import static thesisfinal.DhakaSimFrame.*;

public class Demand {

    private int source;
    private int destination;
    private int demand;
    private ArrayList<Path> pathList = new ArrayList<>();

    public Demand(int source, int destination, int demand) {
        this.source = source;
        this.destination = destination;
        this.demand = demand;
        this.pathList = new ArrayList<>();
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public Path getPath(int index) {
        return pathList.get(index);
    }

    public void addPath(Path path) {
        pathList.add(path);
    }

    public int getNumberOfPaths() {
        return pathList.size();
    }

}