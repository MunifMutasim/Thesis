package thesisfinal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;

import static java.lang.Math.*;
import static thesisfinal.Misc.*;
import static thesisfinal.DhakaSimFrame.*;

public class DhakaSimPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

    private ArrayList<Node> nodeList = new ArrayList<>();
    private ArrayList<Link> linkList = new ArrayList<>();
    private ArrayList<Object> objectList = new ArrayList<>();
    private ArrayList<Vehicle> vehicleList = new ArrayList<>();
    private ArrayList<Demand> demandList = new ArrayList<>();
    private ArrayList<Path> pathList = new ArrayList<>();
    private ArrayList<Integer> nextGenerationTime = new ArrayList<>();
    private ArrayList<Integer> numberOfVehiclesToGenerate = new ArrayList<>();

    private double translateX;
    private double translateY;
    private int referenceX = -1000;
    private int referenceY = -1000;
    private double scale;

    private BufferedWriter log;
    private JFrame jFrame;
    private Timer timer;

    private Random random = new Random(System.nanoTime());

    public DhakaSimPanel(JFrame jFrame) {
        this.jFrame = jFrame;
        try {
            log = new BufferedWriter(new FileWriter(new File("log.txt")));
        } catch (IOException ex) {
            Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        readNetwork();
        readPath();
        readDemand();
        addPathToDemand();
        for (int index = 0; index < demandList.size(); index++) {
            nextGenerationTime.add(1);

            double demand = (double) demandList.get(index).getDemand();          //returns number of vehicle
            double demandRatio = 3600.00 / demand;
            System.out.println("Index = "+index+" Demand = "+demand+" Demand ratio = "+demandRatio);

            if (demandRatio > 1) {
                numberOfVehiclesToGenerate.add(1);
            } else {
                numberOfVehiclesToGenerate.add((int) Math.round(1 / demandRatio));
            }
        }
        timer = new Timer(simulationSpeed, this);
        timer.start();
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        AffineTransform afflineTransform = g2d.getTransform();
        afflineTransform.translate(getWidth() / 2, getHeight() / 2);
        afflineTransform.scale(scale, scale);
        afflineTransform.translate(-getWidth() / 2, -getHeight() / 2);
        afflineTransform.translate(translateX, translateY);
        g2d.setTransform(afflineTransform);
        drawRoadNetwork(g2d);
        try {
            log.write("start");
            log.newLine();
        } catch (IOException ex) {
            Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (pedestrainmode) {
            try {
                log.write("object");
                log.newLine();
            } catch (IOException ex) {
                Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (int i = 0; i < objectList.size(); i++) {
                objectList.get(i).drawMobileObject(log, g2d, pixelPerStrip, pixelPerMeter, pixelPerFootpathStrip);
            }
        }
        try {
            log.write("vehicle");
            log.newLine();
        } catch (IOException ex) {
            Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int index = 0; index < vehicleList.size(); index++) {
            vehicleList.get(index).drawVehicle(log, g2d, pixelPerStrip, pixelPerMeter, pixelPerFootpathStrip);
        }
        try {
            log.write("end");
            log.newLine();
        } catch (IOException ex) {
            Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private int randomVehiclePath(int numberOfPaths) {
        return abs(random.nextInt()) % numberOfPaths;
    }

    private double randomVehicleSpeed() {
        return (abs(random.nextInt()) % 9) + 1;
    }

    private double randomVehicleAcceleration(double speed) {
        return speed / 3;
    }

    private int randomVehicleType() {
        return abs(random.nextInt()) % 11;
    }

    class ValueHolder {
        boolean reverseLink;
        boolean reverseSegment;
    }

    //Understand This
    public ValueHolder f(double x, double y, Segment firstSegment, Segment lastSegment) {
        ValueHolder valueHolder = new ValueHolder();
        double distance1 = getDistance(x, y, firstSegment.getStartX(), firstSegment.getStartY());
        //System.out.println("DISTANCE1 = "+distance1);
        double distance2 = getDistance(x, y, firstSegment.getEndX(), firstSegment.getEndY());
        //System.out.println("DISTANCE2 = "+distance2);
        double distance3 = getDistance(x, y, lastSegment.getStartX(), lastSegment.getStartY());
        //System.out.println("DISTANCE3 = "+distance3);
        double distance4 = getDistance(x, y, lastSegment.getEndX(), lastSegment.getEndY());
        //System.out.println("DISTANCE4 = "+distance4);
        double min = min(distance1, min(distance2, min(distance3, distance4)));
        if (min == distance1) {
            valueHolder.reverseLink = false;
            valueHolder.reverseSegment = false;
        } else if (min == distance2) {
            valueHolder.reverseLink = false;
            valueHolder.reverseSegment = true;
        } else if (min == distance3) {
            valueHolder.reverseLink = true;
            valueHolder.reverseSegment = false;
        } else if (min == distance4) {
            valueHolder.reverseLink = true;
            valueHolder.reverseSegment = true;
        }
        return valueHolder;
    }

    public void run() {
        simulationStep++;
        
        //System.out.println("VehicleList Size = "+vehicleList.size());
        for (int i = 0; i < vehicleList.size(); i++) {
            if (vehicleList.get(i).isToRemove()) {
                //System.out.println("Removing "+i+" th Vehicle");
                vehicleList.get(i).freeStrips();
                vehicleList.remove(i);
            }
        }
        if (pedestrainmode) {
            for (int i = 0; i < objectList.size(); i++) {
                if (objectList.get(i).isToRemove()) {
                    objectList.remove(i);
                }
            }
            for (int i = 0; i < linkList.size(); i++) {
                if (Math.abs(random.nextInt()) % 10 == 0) {
                    int randomSegmentId = Math.abs(random.nextInt()) % linkList.get(i).getNumberOfSegments();
                    Segment randomSegment = linkList.get(i).getSegment(randomSegmentId);
                    //randomPos = Math.abs(numGenerator.nextInt()) % (int) (randomSegment.getSegmentLength());
                    int min = 9;
                    int max = (int) (randomSegment.getLength() - 9);
                    if ((max - min) + 1 <= 0) {
                        continue;
                    }
                    int randomPos = random.nextInt((max - min) + 1) + min;
                    double randomObjSpeed = Math.abs(random.nextInt()) % 2 + 0.5;
                    boolean bo = random.nextBoolean();
                    int strip;
                    if (!bo) {
                        strip = randomSegment.numberOfStrips() - 1;
                    } else {
                        strip = 0;
                    }
                    Object mobj = new Object(randomSegment, strip, randomPos, randomObjSpeed);
                    objectList.add(mobj);
                }
            }
        }
        for (int i = 0; i < demandList.size(); i++) {
            if (nextGenerationTime.get(i) == simulationStep) {
                int numberOfPaths = demandList.get(i).getNumberOfPaths();
                //System.out.println("numberOfVehiclesToGenerate.get(i) "+numberOfVehiclesToGenerate.get(i));
                for (int j = 0; j < numberOfVehiclesToGenerate.get(i); j++) {
                    
                    int pathIndex = randomVehiclePath(numberOfPaths);
                    
                    double speed = randomVehicleSpeed();
                    double acceleration = randomVehicleAcceleration(speed);
                    int type = randomVehicleType();
                    double length = Vehicle.Length(type);
                    double width = Vehicle.Width(type);
                    System.out.println("For i = "+i+" and j = "+j);
                    System.out.println("pathIndex = "+pathIndex);
                    System.out.println("speed = "+speed);
                    System.out.println("acceleration = "+acceleration);
                    System.out.println("vehicle type = "+type);
                    
                    int numberOfStrips = Vehicle.numberOfStrips(type);
                    Path path = demandList.get(i).getPath(pathIndex);
                    
                    Node sourceNode = nodeList.get(path.getSource());
                    Link link = linkList.get(path.getLink(0));
                    System.out.println("Source = "+path.getSource());
                    System.out.println("Destination = "+path.getDestination());
                    System.out.println("Link = "+link.getId());

                    ValueHolder valueHolder = f(sourceNode.x, sourceNode.y, link.getSegment(0), link.getSegment(link.getNumberOfSegments() - 1));
                    Segment segment = valueHolder.reverseLink ? link.getSegment(link.getNumberOfSegments() - 1) : link.getSegment(0);
                    System.out.println("Segment Link "+segment.getLinkIndex()+" Segment id = "+segment.getId());
                    int start, end;
                    if (valueHolder.reverseSegment) {
                        start = segment.numberOfStrips() / 2 + 1;
                        end = segment.numberOfStrips() - 1;
                    } else {
                        start = 1;
                        end = segment.numberOfStrips() / 2 - 1;
                    }

                    for (int k = start; k + numberOfStrips - 1 < end; k++) {
                        boolean flag = true;
                        for (int l = k; l < k + numberOfStrips; l++) {
                            if (!segment.getStrip(l).isGapforAddingVehicle(length)) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
                            double startX, startY, endX, endY;
                            if (valueHolder.reverseSegment) {
                                startX = segment.getEndX();
                                startY = segment.getEndY();
                                endX = segment.getStartX();
                                endY = segment.getStartY();
                            } else {
                                startX = segment.getStartX();
                                startY = segment.getStartY();
                                endX = segment.getEndX();
                                endY = segment.getEndY();
                            }
                            vehicleList.add(new Vehicle(type, speed, acceleration, color, i, pathIndex, 0, startX, startY, endX, endY, valueHolder.reverseLink, valueHolder.reverseSegment, link, segment.getIndex(), k));
                            break;
                        }
                    }
                }
                int demand = demandList.get(i).getDemand();
                double demandRatio = 3600 / demand;
                //if demandratio>1, then we generate vehicles once in some drawing turn .Else we draw in each turn. 
                if (demandRatio > 1) {
                    int nextTime = nextGenerationTime.get(i) + (int) Math.round(demandRatio);
                    nextGenerationTime.set(i, nextTime);
                } else {
                    int nextTime = 
                            
                            
                            nextGenerationTime.get(i) + 1;
                    nextGenerationTime.set(i, nextTime);
                }
            }
        }

        if (pedestrainmode) {
            for (int i = 0; i < objectList.size(); i++) {
                if (objectList.get(i).hasCrossedRoad() || objectList.get(i).isInAccident()) {
                    objectList.get(i).setToRemove(true);
                } else {
                    objectList.get(i).moveForward();
                }
            }
        }

        for (int i = 0; i < vehicleList.size(); i++) {
            Vehicle vehicle = vehicleList.get(i);
            System.out.println("INSIDE CULPRIT");
            System.out.println("Vehicle Link "+vehicle.getLink().getId());
            if (vehicleList.get(i).isJunctionMode()) {
                //in junction
                System.out.println("Junction Mode");
                if (!vehicleList.get(i).isAtJunctionEnd()) {
                    //at junction middle
                    System.out.println("Junction Middle");
                    vehicleList.get(i).moveforward();
                } else {
                    //at junction end
                    //System.out.println("At Junction End");
                    int demandIndex = vehicle.getDemandIndex();
                    int pathIndex = vehicle.getPathIndex();
                    int linkIndexOnPath = vehicle.getLinkIndexOnPath();

                    int newlinkIndex = demandList.get(demandIndex).getPath(pathIndex).getLink(linkIndexOnPath + 1);
                    Link link = linkList.get(newlinkIndex);

                    Segment firstSegment = link.getSegment(0);
                    Segment lastSegment = link.getSegment(link.getNumberOfSegments() - 1);

                    ValueHolder valueHolder = f(vehicle.getSegEndX(), vehicle.getSegEndY(), firstSegment, lastSegment);

                    Segment segment;
                    if (valueHolder.reverseLink) {
                        segment = lastSegment;
                    } else {
                        segment = firstSegment;
                    }

                    int stripIndex;
                    if (vehicle.isReverseSegment() != valueHolder.reverseSegment) {
                        stripIndex = segment.numberOfStrips() - vehicle.getNumberOfStrips() - vehicle.getStripIndex() + 1;
                    } else {
                        stripIndex = vehicle.getStripIndex();
                    }

                    boolean flag = true;
                    for (int j = 0; j < vehicle.getNumberOfStrips(); j++) {
                        if (!segment.getStrip(stripIndex + j).isGapforAddingVehicle(vehicle.getLength())) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        //check this
                        vehicle.getNode().pseudoList.get(vehicleList.get(i).getPseudoIndex()).decreaseVehicleCount();
                        vehicle.getNode().vehicleList.remove(vehicleList.get(i));
                        vehicle.setNode(null);
                        vehicle.setDistanceInJunction(0);

                        vehicle.freeStrips();

                        vehicle.setJunctionMode(false);
                        vehicle.linkChange(linkIndexOnPath + 1, link, segment.getIndex(), stripIndex);
                        vehicle.setReverseLink(valueHolder.reverseLink);
                        vehicle.setReverseSegment(valueHolder.reverseSegment);
                        if (valueHolder.reverseSegment) {
                            vehicle.setSegStartX(segment.getEndX());
                            vehicle.setSegStartY(segment.getEndY());
                            vehicle.setSegEndX(segment.getStartX());
                            vehicle.setSegEndY(segment.getStartY());
                        } else {
                            vehicle.setSegStartX(segment.getStartX());
                            vehicle.setSegStartY(segment.getStartY());
                            vehicle.setSegEndX(segment.getEndX());
                            vehicle.setSegEndY(segment.getEndY());
                        }
                    }
                }
            } else if (!vehicleList.get(i).isAtSegmentEnd()) {
                boolean previous = vehicle.isPassedSensor();
                vehicle.moveforward();
                boolean now = vehicle.isPassedSensor();
                if (!previous && now) {
                    vehicle.getLink().getSegment(vehicle.getSegmentIndex()).updateInformation(vehicle.getSpeed());
                }
            } else {
                //at segment end
                if ((vehicleList.get(i).isReverseLink() && vehicleList.get(i).getLink().getSegment(vehicleList.get(i).getSegmentIndex()).isFirstSegment())
                        || (!vehicleList.get(i).isReverseLink() && vehicleList.get(i).getLink().getSegment(vehicleList.get(i).getSegmentIndex()).isLastSegment())) {
                    //at link end
                    Vehicle v = vehicleList.get(i);
                    int demandIndex = v.getDemandIndex();
                    int pathIndex = v.getPathIndex();
                    int pathLinkIndex = v.getLinkIndexOnPath();
                    int lastLinkInPathIndex = demandList.get(demandIndex).getPath(pathIndex).getNumberOfLinks() - 1;
                    if (pathLinkIndex == lastLinkInPathIndex) {
                        //at path end
                        vehicleList.get(i).setToRemove(true);
                    } else {
                        //at path middle
                        //System.out.println("At Path Middle");
                        int oldLinkIndex = demandList.get(demandIndex).getPath(pathIndex).getLink(pathLinkIndex);
                        int newLinkIndex = demandList.get(demandIndex).getPath(pathIndex).getLink(pathLinkIndex + 1);
                        int oldAndNewStrip = v.getStripIndex();

                        Node no;
                        if (!v.isReverseLink()) {
                            //System.out.println("Before Exception");
                            //System.out.println("Link "+v.getLink().getId());
                            
                            //Debugged By Me
                            Link l = v.getLink();
                            //System.out.println("Link ID = "+l.getId());
                            int node_down = l.getDownNode();
                            //System.out.println("DownNode ID = "+node_down);
                            
                            int ns=nodeList.size();
                            int mm;
                            for(mm=0;mm<ns;mm++){
                                if(nodeList.get(mm).getId()==node_down){
                                    //System.out.println("Match Found For INDEX = "+mm);
                                    break;
                                }
                            }
                            no = nodeList.get(mm);
                            //End of debugging
                            
                            //no = nodeList.get(v.getLink().getDownNode());
                            
                            //System.out.println("Node ID "+no.getId());
                        } else {
                            //Debugged By Me
                            int node_up = v.getLink().getUpNode();
                            //System.out.println("UpNode Id = "+node_up);
                            int ns=nodeList.size();
                            int mm;
                            for(mm=0;mm<ns;mm++){
                                if(nodeList.get(mm).getId()==node_up){
                                    //System.out.println("Match Found For INDEX = "+mm);
                                    break;
                                }
                            }
                            no = nodeList.get(mm);
                            //Debugged by Me
                            //no = nodeList.get(v.getLink().getUpNode());
                        }

                        no.swtich(simulationStep);

                        if (!no.pseudoExists(oldLinkIndex, newLinkIndex, oldAndNewStrip, oldAndNewStrip)) {
                            Segment lastSegment = v.getLink().getSegment(v.getSegmentIndex());
                            ValueHolder valueHolder = f(v.getSegEndX(), v.getSegEndY(), linkList.get(newLinkIndex).getSegment(0), linkList.get(newLinkIndex).getSegment(linkList.get(newLinkIndex).getNumberOfSegments() - 1));

                            Segment firstSegment;
                            if (valueHolder.reverseLink) {
                                firstSegment = linkList.get(newLinkIndex).getSegment(linkList.get(newLinkIndex).getNumberOfSegments() - 1);
                            } else {
                                firstSegment = linkList.get(newLinkIndex).getSegment(0);
                            }

                            double x4, y4, x_3, y_3;
                            int w = 1 * pixelPerFootpathStrip + (v.getStripIndex() - 1) * pixelPerStrip;
                            double x1 = lastSegment.getStartX() * pixelPerMeter;
                            double y1 = lastSegment.getStartY() * pixelPerMeter;
                            double x2 = lastSegment.getEndX() * pixelPerMeter;
                            double y2 = lastSegment.getEndY() * pixelPerMeter;
                            double x3 = returnX3(x1, y1, x2, y2, w);
                            double y3 = returnY3(x1, y1, x2, y2, w);
                            x4 = returnX4(x1, y1, x2, y2, w);
                            y4 = returnY4(x1, y1, x2, y2, w);
                            double dist1 = (x3 - v.getSegEndX() * pixelPerMeter) * (x3 - v.getSegEndX() * pixelPerMeter) + (y3 - v.getSegEndY() * pixelPerMeter) * (y3 - v.getSegEndY() * pixelPerMeter);
                            double dist2 = (x4 - v.getSegEndX() * pixelPerMeter) * (x4 - v.getSegEndX() * pixelPerMeter) + (y4 - v.getSegEndY() * pixelPerMeter) * (y4 - v.getSegEndY() * pixelPerMeter);
                            if (dist1 < dist2) {
                                x4 = x3;
                                y4 = y3;
                            }

                            double x_1;
                            double y_1;
                            double x_2;
                            double y_2;
                            double w1;
                            if (v.isReverseSegment() != valueHolder.reverseSegment) {
                                x_1 = firstSegment.getStartX() * pixelPerMeter;
                                y_1 = firstSegment.getStartY() * pixelPerMeter;
                                x_2 = firstSegment.getEndX() * pixelPerMeter;
                                y_2 = firstSegment.getEndY() * pixelPerMeter;
                                w1 = v.getLink().getSegment(v.getSegmentIndex()).getSegmentWidth() * pixelPerMeter - w;
                            } else {
                                x_1 = firstSegment.getStartX() * pixelPerMeter;
                                y_1 = firstSegment.getStartY() * pixelPerMeter;
                                x_2 = firstSegment.getEndX() * pixelPerMeter;
                                y_2 = firstSegment.getEndY() * pixelPerMeter;
                                w1 = w;
                            }
                            x_3 = returnX3(x_1, y_1, x_2, y_2, w1);
                            y_3 = returnY3(x_1, y_1, x_2, y_2, w1);
                            double x_4 = returnX4(x_1, y_1, x_2, y_2, w1);
                            double y_4 = returnY4(x_1, y_1, x_2, y_2, w1);
                            double dist3 = (x_3 - v.getSegEndX() * pixelPerMeter) * (x_3 - v.getSegEndX() * pixelPerMeter) + (y_3 - v.getSegEndY() * pixelPerMeter) * (y_3 - v.getSegEndY() * pixelPerMeter);
                            double dist4 = (x_4 - v.getSegEndX() * pixelPerMeter) * (x_4 - v.getSegEndX() * pixelPerMeter) + (y_4 - v.getSegEndY() * pixelPerMeter) * (y_4 - v.getSegEndY() * pixelPerMeter);
                            if (dist3 > dist4) {
                                x_3 = x_4;
                                y_3 = y_4;
                            }

                            no.addPseudo(new Pseudo(oldLinkIndex, newLinkIndex, oldAndNewStrip, oldAndNewStrip, x4, y4, x_3, y_3, 0));

                            if (!no.pairianoExists(oldLinkIndex, newLinkIndex)) {
                                no.addPairiano(oldLinkIndex, newLinkIndex, x4, y4, x_3, y_3);
                            }

                        }
                        if (no.getPairianoActive(oldLinkIndex, newLinkIndex)) {
                            v.setPseudoIndex(no.getMyPseudo(oldLinkIndex, newLinkIndex, oldAndNewStrip, oldAndNewStrip));
                            v.setNode(no);
                            v.calculateCornerPoints();
                            if (!no.doOverlap(v)) {
                                no.vehicleList.add(v);
                                v.setDistanceInJunction(0);
                                v.setJunctionMode(true);
                                no.pseudoList.get(v.getPseudoIndex()).increaseVehicleCount();
                            }
                        }

                    }
                } else {
                    //at link middle
                    Link link = vehicle.getLink();

                    Segment segment;
                    if (vehicle.isReverseLink()) {
                        segment = link.getSegment(vehicle.getSegmentIndex() - 1);
                    } else {
                        segment = link.getSegment(vehicle.getSegmentIndex() + 1);
                    }

                    ValueHolder valueHolder = f(vehicle.getSegEndX(), vehicle.getSegEndY(), segment, segment);

                    int stripIndex;
                    if (vehicle.isReverseSegment() != valueHolder.reverseSegment) {
                        stripIndex = segment.numberOfStrips() - vehicle.getNumberOfStrips() - vehicle.getStripIndex() + 1;
                    } else {
                        stripIndex = vehicle.getStripIndex();
                    }

                    boolean flag = true;
                    for (int j = 0; j < vehicle.getNumberOfStrips(); j++) {
                        if (!segment.getStrip(stripIndex + j).isGapforAddingVehicle(vehicle.getLength())) {
                            flag = false;
                        }
                    }

                    if (flag) {

                        vehicle.freeStrips();

                        vehicle.segmentChange(segment.getIndex(), stripIndex);

                        vehicle.setReverseSegment(valueHolder.reverseSegment);
                        if (valueHolder.reverseSegment) {
                            vehicle.setSegStartX(segment.getEndX());
                            vehicle.setSegStartY(segment.getEndY());
                            vehicle.setSegEndX(segment.getStartX());
                            vehicle.setSegEndY(segment.getStartY());
                        } else {
                            vehicle.setSegStartX(segment.getStartX());
                            vehicle.setSegStartY(segment.getStartY());
                            vehicle.setSegEndX(segment.getEndX());
                            vehicle.setSegEndY(segment.getEndY());
                        }
                    }
                }
            }
        }

    }

    public void drawRoadNetwork(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < linkList.size(); i++) {
            linkList.get(i).draw(g2d);
        }
        ArrayList<double[]> lineList = new ArrayList<>();
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            for (int j = 0; j < node.numberOfLinks(); j++) {
                //System.out.println("Before Exception");
                Link link = linkList.get(node.getLink(j));
                double x1, y1, x2, y2, x3, y3, x4, y4;
                if (link.getUpNode() == node.getId()) {
                    x1 = link.getSegment(0).getStartX() * pixelPerMeter;
                    y1 = link.getSegment(0).getStartY() * pixelPerMeter;
                    x2 = link.getSegment(0).getEndX() * pixelPerMeter;
                    y2 = link.getSegment(0).getEndY() * pixelPerMeter;

                    double width = 2 * pixelPerFootpathStrip + (link.getSegment(0).numberOfStrips() - 2) * pixelPerStrip;

                    x3 = returnX3(x1, y1, x2, y2, width);
                    y3 = returnY3(x1, y1, x2, y2, width);
                    x4 = returnX4(x1, y1, x2, y2, width);
                    y4 = returnY4(x1, y1, x2, y2, width);
                } else {
                    x1 = link.getSegment(link.getNumberOfSegments() - 1).getStartX() * pixelPerMeter;
                    y1 = link.getSegment(link.getNumberOfSegments() - 1).getStartY() * pixelPerMeter;
                    x2 = link.getSegment(link.getNumberOfSegments() - 1).getEndX() * pixelPerMeter;
                    y2 = link.getSegment(link.getNumberOfSegments() - 1).getEndY() * pixelPerMeter;

                    double w = 2 * pixelPerFootpathStrip + (link.getSegment(link.getNumberOfSegments() - 1).numberOfStrips() - 2) * pixelPerStrip;

                    x3 = returnX3(x1, y1, x2, y2, w);
                    y3 = returnY3(x1, y1, x2, y2, w);
                    x4 = returnX4(x1, y1, x2, y2, w);
                    y4 = returnY4(x1, y1, x2, y2, w);

                    x1 = x2;
                    y1 = y2;
                    x3 = x4;
                    y3 = y4;
                }
                for (int k = 0; k < node.numberOfLinks(); k++) {
                    if (j != k) {
                        Link linkPrime = linkList.get(node.getLink(k));
                        double x1Prime, y1Prime, x2Prime, y2Prime, x3Prime, y3Prime, x4Prime, y4Prime;
                        if (linkPrime.getUpNode() == node.getId()) {
                            x1Prime = linkPrime.getSegment(0).getStartX() * pixelPerMeter;
                            y1Prime = linkPrime.getSegment(0).getStartY() * pixelPerMeter;
                            x2Prime = linkPrime.getSegment(0).getEndX() * pixelPerMeter;
                            y2Prime = linkPrime.getSegment(0).getEndY() * pixelPerMeter;

                            double w = 2 * pixelPerFootpathStrip + (linkPrime.getSegment(0).numberOfStrips() - 2) * pixelPerStrip;

                            x3Prime = returnX3(x1Prime, y1Prime, x2Prime, y2Prime, w);
                            y3Prime = returnY3(x1Prime, y1Prime, x2Prime, y2Prime, w);
                            x4Prime = returnX4(x1Prime, y1Prime, x2Prime, y2Prime, w);
                            y4Prime = returnY4(x1Prime, y1Prime, x2Prime, y2Prime, w);
                        } else {
                            x1Prime = linkPrime.getSegment(linkPrime.getNumberOfSegments() - 1).getStartX() * pixelPerMeter;
                            y1Prime = linkPrime.getSegment(linkPrime.getNumberOfSegments() - 1).getStartY() * pixelPerMeter;
                            x2Prime = linkPrime.getSegment(linkPrime.getNumberOfSegments() - 1).getEndX() * pixelPerMeter;
                            y2Prime = linkPrime.getSegment(linkPrime.getNumberOfSegments() - 1).getEndY() * pixelPerMeter;

                            double w = 2 * pixelPerFootpathStrip + (linkPrime.getSegment(linkPrime.getNumberOfSegments() - 1).numberOfStrips() - 2) * pixelPerStrip;

                            x3Prime = returnX3(x1Prime, y1Prime, x2Prime, y2Prime, w);
                            y3Prime = returnY3(x1Prime, y1Prime, x2Prime, y2Prime, w);
                            x4Prime = returnX4(x1Prime, y1Prime, x2Prime, y2Prime, w);
                            y4Prime = returnY4(x1Prime, y1Prime, x2Prime, y2Prime, w);

                            x1Prime = x2Prime;
                            y1Prime = y2Prime;
                            x3Prime = x4Prime;
                            y3Prime = y4Prime;
                        }

                        double[] one = new double[]{x1, y1, x1Prime, y1Prime};
                        double[] two = new double[]{x1, y1, x3Prime, y3Prime};
                        double[] three = new double[]{x3, y3, x1Prime, y1Prime};
                        double[] four = new double[]{x3, y3, x3Prime, y3Prime};
                        lineList.add(one);
                        lineList.add(two);
                        lineList.add(three);
                        lineList.add(four);
                    }
                }
            }
        }
        for (int i = 0; i < lineList.size(); i++) {
            boolean doIntersect = false;
            for (int j = 0; j < lineList.size(); j++) {
                if (i != j) {
                    if (doIntersect(lineList.get(i)[0], lineList.get(i)[1],
                            lineList.get(i)[2], lineList.get(i)[3],
                            lineList.get(j)[0], lineList.get(j)[1],
                            lineList.get(j)[2], lineList.get(j)[3])) {
                        doIntersect = true;
                        break;
                    }
                }
            }
            if (!doIntersect) {
                g2d.drawLine((int) round(lineList.get(i)[0]),
                        (int) round(lineList.get(i)[1]),
                        (int) round(lineList.get(i)[2]),
                        (int) round(lineList.get(i)[3]));
            }
        }
        for (int i = 0; i < linkList.size(); i++) {
            Link link = linkList.get(i);
            Segment segment = link.getSegment(0);
            for (int j = 1; j < link.getNumberOfSegments(); j++) {
                double x1 = segment.getStartX() * pixelPerMeter;
                double y1 = segment.getStartY() * pixelPerMeter;
                double x2 = segment.getEndX() * pixelPerMeter;
                double y2 = segment.getEndY() * pixelPerMeter;

                double w = 2 * pixelPerFootpathStrip + (segment.numberOfStrips() - 2) * pixelPerStrip;

                double x3 = returnX3(x1, y1, x2, y2, w);
                double y3 = returnY3(x1, y1, x2, y2, w);
                double x4 = returnX4(x1, y1, x2, y2, w);
                double y4 = returnY4(x1, y1, x2, y2, w);

                double x1Prime = link.getSegment(j).getStartX() * pixelPerMeter;
                double y1Prime = link.getSegment(j).getStartY() * pixelPerMeter;
                double x2Prime = link.getSegment(j).getEndX() * pixelPerMeter;
                double y2Prime = link.getSegment(j).getEndY() * pixelPerMeter;

                double wPrime = 2 * pixelPerFootpathStrip + (link.getSegment(j).numberOfStrips() - 2) * pixelPerStrip;

                double x3Prime = returnX3(x1Prime, y1Prime, x2Prime, y2Prime, wPrime);
                double y3Prime = returnY3(x1Prime, y1Prime, x2Prime, y2Prime, wPrime);
                double x4Prime = returnX4(x1Prime, y1Prime, x2Prime, y2Prime, wPrime);
                double y4Prime = returnY4(x1Prime, y1Prime, x2Prime, y2Prime, wPrime);

                g2d.drawLine((int) round(x2), (int) round(y2), (int) round(x1Prime), (int) round(y1Prime));
                g2d.drawLine((int) round(x4), (int) round(y4), (int) round(x3Prime), (int) round(y3Prime));

                segment = link.getSegment(j);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (simulationStep < simulationEndTime) {
            run();
            repaint();
        } else if (simulationStep == simulationEndTime) {
            simulationStep++;
            
            int numberOfSegments = 0;
            for(int i = 0; i < linkList.size(); i++) {
                numberOfSegments += linkList.get(i).getNumberOfSegments();
            }
            
            double sensorVehicleCount[] = new double[numberOfSegments];
            double sensorVehicleAvgSpeed[] = new double[numberOfSegments];
            double accidentCount[] = new double[numberOfSegments];
            int index, index2, ct = 0;

            for (index = 0; index < linkList.size(); index++) {
                for (index2 = 0; index2 < linkList.get(index).getNumberOfSegments(); index2++) {
                    sensorVehicleCount[ct] = linkList.get(index).getSegment(index2).getSensorVehicleCount();
                    sensorVehicleAvgSpeed[ct] = linkList.get(index).getSegment(index2).getSensorVehicleAvgSpeed();
                    accidentCount[ct] = linkList.get(index).getSegment(index2).getAccidentCount();
                    ct++;
                }
            }
            double y[] = {100, 119, 120};
            //plots graph
            SensorVehicleCountPlot p1 = new SensorVehicleCountPlot(sensorVehicleCount);
            SensorVehicleAvgSpeedPlot p2 = new SensorVehicleAvgSpeedPlot(sensorVehicleAvgSpeed);
            AccidentCountPlot p3 = new AccidentCountPlot(accidentCount);
            simulationStep++;
        
            
            
            
            JPanel jPanel = new JPanel();
            JButton replay = new JButton(new ImageIcon("replay.png"));
            replay.setBackground(Color.WHITE);
            JButton pause = new JButton(new ImageIcon("pause.png"));
            pause.setBackground(Color.WHITE);
            JButton rewind = new JButton(new ImageIcon("rewind.png"));
            rewind.setBackground(Color.WHITE);
            JButton fastForward = new JButton(new ImageIcon("fast_forward.png"));
            fastForward.setBackground(Color.WHITE);
            jPanel.add(replay);
            jPanel.add(pause);
            jPanel.add(rewind);
            jPanel.add(fastForward);
            JSlider jSlider = new JSlider(JSlider.VERTICAL, 0, 0, 0);
            jFrame.getContentPane().add(jPanel, BorderLayout.SOUTH);
            jFrame.getContentPane().add(jSlider, BorderLayout.WEST);
            jFrame.repaint();
            jFrame.revalidate();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        referenceX = e.getX();
        referenceY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        translateX += e.getX() - referenceX;
        translateY += e.getY() - referenceY;
        referenceX = e.getX();
        referenceY = e.getY();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }

    public void readNetwork() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("link.txt"))));
            String dataLine = bufferedReader.readLine();
            int numLinks = Integer.parseInt(dataLine);
            for (int i = 0; i < numLinks; i++) {
                dataLine = bufferedReader.readLine();
                StringTokenizer stringTokenizer = new StringTokenizer(dataLine, " ");
                int linkId = Integer.parseInt(stringTokenizer.nextToken());
                int nodeId1 = Integer.parseInt(stringTokenizer.nextToken());
                int nodeId2 = Integer.parseInt(stringTokenizer.nextToken());
                int segmentCount = Integer.parseInt(stringTokenizer.nextToken());
                Link link = new Link(i, linkId, nodeId1, nodeId2);
                for (int j = 0; j < segmentCount; j++) {
                    dataLine = bufferedReader.readLine();
                    stringTokenizer = new StringTokenizer(dataLine, " ");
                    int segmentId = Integer.parseInt(stringTokenizer.nextToken());
                    double startX = Double.parseDouble(stringTokenizer.nextToken());
                    double startY = Double.parseDouble(stringTokenizer.nextToken());
                    double endX = Double.parseDouble(stringTokenizer.nextToken());
                    double endY = Double.parseDouble(stringTokenizer.nextToken());
                    double segmentWidth = Double.parseDouble(stringTokenizer.nextToken());
                    boolean firstSegment = false;
                    if (j == 0) {
                        firstSegment = true;
                    }
                    boolean lastSegment = false;
                    if (j == segmentCount - 1) {
                        lastSegment = true;
                    }
                    Segment segment = new Segment(i, j, segmentId, startX, startY, endX, endY, segmentWidth, lastSegment, firstSegment);
                    link.addSegment(segment);
                }
                linkList.add(link);
                //System.out.println("Printing Link and Upnode and Downnode");
                //System.out.println("Link Id = "+link.getId()+" UpNode = "+link.getUpNode()+" DownNode = "+link.getDownNode());
                
            }
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("node.txt"))));
            dataLine = bufferedReader.readLine();
            int numNodes = Integer.parseInt(dataLine);
            for (int i = 0; i < numNodes; i++) {
                dataLine = bufferedReader.readLine();
                StringTokenizer stringTokenizer = new StringTokenizer(dataLine, " ");
                int nodeId = Integer.parseInt(stringTokenizer.nextToken());
                double centerX = Double.parseDouble(stringTokenizer.nextToken());
                double centerY = Double.parseDouble(stringTokenizer.nextToken());
                Node node = new Node(i, nodeId, centerX, centerY);
                while (stringTokenizer.hasMoreTokens()) {
                    node.linkList.add(getLinkIndex(Integer.parseInt(stringTokenizer.nextToken())));
                }
                nodeList.add(node);
                //System.out.println("Node Id = "+nodeId+" Index "+i);
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int getNodeIndex(int nodeId) {
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).getId() == nodeId) {
                return nodeList.get(i).getIndex();
            }
        }
        return -1;
    }

    int getLinkIndex(int linkId) {
        for (int i = 0; i < linkList.size(); i++) {
            if (linkList.get(i).getId() == linkId) {
                return linkList.get(i).getIndex();
            }
        }
        return -1;
    }

    public void readDemand() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("demand.txt"))));
            String dataLine = bufferedReader.readLine();
            int numDemands = Integer.parseInt(dataLine);
            for (int i = 0; i < numDemands; i++) {
                dataLine = bufferedReader.readLine();
                StringTokenizer stringTokenizer = new StringTokenizer(dataLine, " ");
                int nodeId1 = Integer.parseInt(stringTokenizer.nextToken());
                int nodeId2 = Integer.parseInt(stringTokenizer.nextToken());
                int demand = Integer.parseInt(stringTokenizer.nextToken());
                demandList.add(new Demand(getNodeIndex(nodeId1), getNodeIndex(nodeId2), demand));
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readPath() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("path.txt"))));
            String dataLine = bufferedReader.readLine();
            int numPaths = Integer.parseInt(dataLine);
            //System.out.println("Number of paths = "+numPaths);
            for (int i = 0; i < numPaths; i++) {
                dataLine = bufferedReader.readLine();
                StringTokenizer stringTokenizer = new StringTokenizer(dataLine, " ");
                int nodeId1 = Integer.parseInt(stringTokenizer.nextToken());
                int nodeId2 = Integer.parseInt(stringTokenizer.nextToken());
                Path path = new Path(getNodeIndex(nodeId1), getNodeIndex(nodeId2));
                while (stringTokenizer.hasMoreTokens()) {
                    int linkId = Integer.parseInt(stringTokenizer.nextToken());
                    path.addLink(getLinkIndex(linkId));
                }
                pathList.add(path);
            }
            bufferedReader.close();
            
//            System.out.println("Printing path ");
//            for(int i=0;i<numPaths;i++){
//                System.out.println("Path "+i+" Source = "+pathList.get(i).getSource()+" Destitnation = "+pathList.get(i).getDestination());
//                System.out.println("Link indexes : ");
//                for(int k=0;k<pathList.get(i).getNumberOfLinks();k++){
//                    System.out.println(pathList.get(i).getLink(k));
//                }
//            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addPathToDemand() {
        for (int i = 0; i < pathList.size(); i++) {
            for (int j = 0; j < demandList.size(); j++) {
                if (demandList.get(j).getSource() == pathList.get(i).getSource() && demandList.get(j).getDestination() == pathList.get(i).getDestination()) {
                    demandList.get(j).addPath(pathList.get(i));
                }
            }
        }
    }
}
