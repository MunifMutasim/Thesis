package thesisfinal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import static java.lang.Math.*;
import static thesisfinal.Misc.*;
import static thesisfinal.DhakaSimFrame.*;

public class Segment {

    private int linkIndex;
    private int index;
    private int id;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double segmentWidth;
    private boolean lastSegment;
    private boolean firstSegment;
    private double sensor;
//    private int vehicleCountAtSensor;
//    private double averageSpeedAtSensor;
//    private int accidentCount;
    private int sensorVehicleCount;
    private double sensorVehicleAvgSpeed;
    private int accidentCount;

    private ArrayList<Strip> stripList = new ArrayList<>();

    public Segment(int linkIndex, int index, int id, double startX, double startY, double endX, double endY, double segmentWidth, boolean lastSegment, boolean firstSegment) {
        this.linkIndex = linkIndex;
        this.index = index;
        this.id = id;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.segmentWidth = segmentWidth;
        this.lastSegment = lastSegment;
        this.firstSegment = firstSegment;
        sensor = 0.5 * getDistance(startX, startY, endX, endY);
        sensorVehicleCount = 0;
        sensorVehicleAvgSpeed = 0;
        accidentCount = 0;
        initialize();
    }

    void initialize() {
        int stripCount = 2 + (int) round((segmentWidth - 2 * footpathStripWidth) / stripWidth);
        
        for (int i = 0; i < stripCount; i++) {
            if (i == 0) {
                stripList.add(new Strip(index, i, true));
            } else if (i == stripCount - 1) {
                stripList.add(new Strip(index, i, true));
            } else {
                stripList.add(new Strip(index, i, false));
            }
        }
    }

    public int getLinkIndex() {
        return linkIndex;
    }

    public void setLinkIndex(int linkIndex) {
        this.linkIndex = linkIndex;
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

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public double getSegmentWidth() {
        return segmentWidth;
    }

    public void setSegmentWidth(double segmentWidth) {
        this.segmentWidth = segmentWidth;
    }

    public boolean isLastSegment() {
        return lastSegment;
    }

    public void setLastSegment(boolean lastSegment) {
        this.lastSegment = lastSegment;
    }

    public boolean isFirstSegment() {
        return firstSegment;
    }

    public void setFirstSegment(boolean firstSegment) {
        this.firstSegment = firstSegment;
    }

    public double getSensor() {
        return sensor;
    }

    public void setSensor(double sensor) {
        this.sensor = sensor;
    }

    public int getVehicleCountAtSensor() {
        return sensorVehicleCount;
    }

    public void setVehicleCountAtSensor(int vehicleCountAtSensor) {
        this.sensorVehicleCount = vehicleCountAtSensor;
    }

    public double getAverageSpeedAtSensor() {
        return sensorVehicleAvgSpeed;
    }

    public void setAverageSpeedAtSensor(double averageSpeedAtSensor) {
        this.sensorVehicleAvgSpeed = averageSpeedAtSensor;
    }

//    public int getAccidentCount() {
//        return accidentCount;
//    }

    public void setAccidentCount(int accidentCount) {
        this.accidentCount = accidentCount;
    }
    
    public Strip getStrip(int index) {
        return stripList.get(index);
    } 
    
    public int numberOfStrips() {
        return stripList.size();
    }
    
    public double getLength() {
        return getDistance(startX, startY, endX, endY);
    }

    public void updateInformation(double speed) {
        sensorVehicleCount++;
        sensorVehicleAvgSpeed = (sensorVehicleAvgSpeed * (sensorVehicleCount - 1) + speed) / sensorVehicleCount;
    }

    public void draw(Graphics2D g2d) {
        int x1 = (int) round(startX * pixelPerMeter);
        int y1 = (int) round(startY * pixelPerMeter);
        int x2 = (int) round(endX * pixelPerMeter);
        int y2 = (int) round(endY * pixelPerMeter);
        int w = pixelPerFootpathStrip + (numberOfStrips() - 1) * pixelPerStrip;
        int x3 = (int) round(returnX3(x1, y1, x2, y2, w));
        int y3 = (int) round(returnY3(x1, y1, x2, y2, w));
        int x4 = (int) round(returnX4(x1, y1, x2, y2, w));
        int y4 = (int) round(returnY4(x1, y1, x2, y2, w));
        g2d.setColor(Color.WHITE);
        g2d.drawLine(x1, y1, x2, y2);
        g2d.drawLine(x3, y3, x4, y4);
    }
    
            //counts number of vehicle passed over the sensor and updates average speed
	public void updateSensorInfo(double newSpeed)
        {
            sensorVehicleCount++;
            sensorVehicleAvgSpeed = sensorVehicleAvgSpeed/sensorVehicleCount*(sensorVehicleCount-1)+
                    newSpeed/sensorVehicleCount;
        }
        
        public int getSensorVehicleCount()
        {
            return sensorVehicleCount;
        }
        
        public double getSensorVehicleAvgSpeed()
        {
            return sensorVehicleAvgSpeed;
        }
        
        public void updateAccidentcount()
        {
            accidentCount++;
        }
        
        public int getAccidentCount()
        {
            return accidentCount++;
        }


}
