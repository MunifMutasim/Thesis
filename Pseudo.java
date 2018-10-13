package thesisfinal;

import static thesisfinal.Misc.getDistance;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mishkat076
 */
public class Pseudo {

        int startLink;
        int startStrip;
        int endLink;
        int endStrip;
        double startPointX;
        double startPointY;
        double endPointX;
        double endPointY;
        int vehicleCount;

    public Pseudo(int startLink, int startStrip, int endLink, int endStrip, double startPointX, double startPointY, double endPointX, double endPointY, int vehicleCount) {
        this.startLink = startLink;
        this.startStrip = startStrip;
        this.endLink = endLink;
        this.endStrip = endStrip;
        this.startPointX = startPointX;
        this.startPointY = startPointY;
        this.endPointX = endPointX;
        this.endPointY = endPointY;
        this.vehicleCount = vehicleCount;
    }

    public int getStartLink() {
        return startLink;
    }
    
    public void increaseVehicleCount() {
        vehicleCount++;
    }
    
    public void decreaseVehicleCount() {
        vehicleCount--;
    }

    public void setStartLink(int startLink) {
        this.startLink = startLink;
    }

    public int getStartStrip() {
        return startStrip;
    }

    public void setStartStrip(int startStrip) {
        this.startStrip = startStrip;
    }

    public int getEndLink() {
        return endLink;
    }

    public void setEndLink(int endLink) {
        this.endLink = endLink;
    }

    public int getEndStrip() {
        return endStrip;
    }

    public void setEndStrip(int endStrip) {
        this.endStrip = endStrip;
    }

    public double getStartPointX() {
        return startPointX;
    }

    public void setStartPointX(double startPointX) {
        this.startPointX = startPointX;
    }

    public double getStartPointY() {
        return startPointY;
    }

    public void setStartPointY(double startPointY) {
        this.startPointY = startPointY;
    }

    public double getEndPointX() {
        return endPointX;
    }

    public void setEndPointX(double endPointX) {
        this.endPointX = endPointX;
    }

    public double getEndPointY() {
        return endPointY;
    }

    public void setEndPointY(double endPointY) {
        this.endPointY = endPointY;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(int vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    double getLength() {
        return getDistance(startPointX, startPointY, endPointX, endPointY);
    }


    
}
