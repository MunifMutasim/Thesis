package thesisfinal;

import java.awt.Graphics2D;
import java.util.ArrayList;

import static java.lang.Math.*;
import static thesisfinal.Misc.*;
import static thesisfinal.DhakaSimFrame.*;

public class Link {

    private int index;
    private int id;
    private int upNode;
    private int downNode;
    private ArrayList<Segment> segmentList = new ArrayList<>();

    public Link(int index, int id, int upNode, int downNode) {
        this.index = index;
        this.id = id;
        this.upNode = upNode;
        this.downNode = downNode;
        //System.out.println("Link id "+this.id+" Index = "+this.index);
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

    public int getUpNode() {
        return upNode;
    }

    public void setUpNode(int upNode) {
        this.upNode = upNode;
    }

    public int getDownNode() {
        return downNode;
    }

    public void setDownNode(int downNode) {
        this.downNode = downNode;
    }

    public Segment getSegment(int index) {
        return segmentList.get(index);
    }

    public void addSegment(Segment segment) {
        segmentList.add(segment);
    }

    public int getNumberOfSegments() {
        return segmentList.size();
    }

    public void draw(Graphics2D g2d) {
        for (int i = 0; i < getNumberOfSegments(); i++) {
            getSegment(i).draw(g2d);
        }
    }
    
}
