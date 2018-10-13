package thesisfinal;

import java.util.ArrayList;

import static java.lang.Math.*;
import static thesisfinal.Misc.*;
import static thesisfinal.DhakaSimFrame.*;

public class Path {

    private int source;
    private int destination;
    private ArrayList<Integer> linkList = new ArrayList<>();//Link gular index save kore rakhe..Not link id...Link index

    public Path(int source, int destination) {
        this.source = source;
        this.destination = destination;
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

    public int getLink(int index) {
        return linkList.get(index);
    }

    public void addLink(int link) {
        linkList.add(link);
    }

    public int getNumberOfLinks() {
        return linkList.size();
    }

}
