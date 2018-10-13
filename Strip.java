package thesisfinal;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;
import static thesisfinal.Misc.*;
import static thesisfinal.DhakaSimFrame.*;

public class Strip {

    private int segmentIndex;
    private int stripIndex;
    private boolean isFootPathStrip;

    ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
    ArrayList<Object> objectList = new ArrayList<Object>();
    //Constructor sets segment index and strip index

    public Strip(int segIndex, int strIndex, boolean isfoot) {
        segmentIndex = segIndex;
        stripIndex = strIndex;
        isFootPathStrip = isfoot;
    }

    public boolean isFp() {
        return isFootPathStrip;
    }

    //gets strip index
    public int getStripIndex() {
        return stripIndex;
    }

    //adds vehicle to the strip's vehicle list when vehicle comes over the strip
    public void addVehicle(Vehicle v) {
        vehicleList.add(v);
    }

    //removes vehicle from the strip's vehicle list when vehicle lefts the strip 
    public void delVehicle(Vehicle v) {
        vehicleList.remove(v);
    }

    public void addObject(Object obj) {
        objectList.add(obj);
    }

    public void delObject(Object obj) {
        objectList.remove(obj);
    }

    //for a vehicle on this strip,finds another vehicle on the same strip with minimum distance ahead.
    public Vehicle probableLeader(double distance, double length) {
        double min = 9999;
        Vehicle ret = null;
        for (int i = 0; i < vehicleList.size(); i++) {
            if (vehicleList.get(i).getDistanceInSegment() > distance + 0.1) {
                double compare = vehicleList.get(i).getDistanceInSegment() - distance;
                if (compare < min) {
                    min = compare;
                    ret = vehicleList.get(i);
                }
            }
        }
        return ret;
    }

    //checks whether there is space for a vehicle to move forward without a collision 
    //and keeping a threshold distance
    public boolean isGapforForwardMovement(Vehicle v) {
        double thresholdDistance = 0.12;
        double upperLimit = v.getDistanceInSegment() + v.getLength() + v.getSpeed() + thresholdDistance;
        double lowerLimit = v.getDistanceInSegment();
        Random rand = new Random();
        boolean accident = false;

        for (int sp = 0; sp < vehicleList.size(); sp++) {
            if (vehicleList.get(sp) != v && (vehicleList.get(sp).getDistanceInSegment() > lowerLimit && vehicleList.get(sp).getDistanceInSegment() < upperLimit
                    || vehicleList.get(sp).getDistanceInSegment() + vehicleList.get(sp).getLength() > lowerLimit && vehicleList.get(sp).getDistanceInSegment()
                    + vehicleList.get(sp).getLength() < upperLimit)) {
                return false;
            }
        }
        //____________________
        if (v.isReverseSegment()) {
            double lower = lowerLimit;
            double upper = upperLimit;
            lowerLimit = v.getLink().getSegment(v.getSegmentIndex()).getLength() - upper;
            upperLimit = v.getLink().getSegment(v.getSegmentIndex()).getLength() - lower;
        }
        //____________________
        if (rand.nextInt() % DhakaSimFrame.encounterPerAccident == 0) {
            accident = true;
        }
        for (int i = 0; i < objectList.size(); i++) {
            double objpos = objectList.get(i).getInitPos();
            if (lowerLimit < objpos && objpos < upperLimit) {
                if (!accident) {
                    return false;
                } else {
                    //System.out.println(lowerLimit + " " + objpos + " " + upperLimit);
                }
                objectList.get(i).getSegment().setAccidentCount(objectList.get(i).getSegment().getAccidentCount() + 1);//updateAccidentcount();
                objectList.get(i).inAccident = true;
                delObject(objectList.get(i));
            }
        }
        return true;

    }

    public boolean hasGapForObject(Object obj) {
        double lowerLimit, upperLimit, thresholdDistance = 0.08;
        Vehicle v;

        Random rand = new Random();
        boolean accident = (rand.nextInt() % DhakaSimFrame.encounterPerAccident == 0) ? true : false;
        for (int i = 0; i < vehicleList.size(); i++) {
            v = vehicleList.get(i);
            upperLimit = v.getDistanceInSegment() + v.getLength() + thresholdDistance;
            lowerLimit = v.getDistanceInSegment();
            //____________________
            if (v.isReverseSegment()) {
                double lower = lowerLimit;
                double upper = upperLimit;
                lowerLimit = v.getLink().getSegment(v.getSegmentIndex()).getLength() - upper;
                upperLimit = v.getLink().getSegment(v.getSegmentIndex()).getLength() - lower;
            }
            //____________________
            if (lowerLimit < obj.getInitPos() && obj.getInitPos() < upperLimit) {
                if (!accident) {
                    return false;
                } else {
                    //System.out.println(lowerLimit + " " + obj.getInitPos() + " " + upperLimit);
                }
                obj.getSegment().setAccidentCount(obj.getSegment().getAccidentCount() + 1);//updateAccidentcount();
                obj.inAccident = true;
                delObject(obj);
            }
        }
        return true;
    }

    //checks whether there is adequate space for adding a new vehicle 
    public boolean isGapforAddingVehicle(double vehicleLength) {

        double lowerLimit = 0.08;
        double upperLimit = 0.6 + vehicleLength;
        boolean accident = false;
        Random rand = new Random();
        for (int sp = 0; sp < vehicleList.size(); sp++) {
            if ((vehicleList.get(sp).getDistanceInSegment() < upperLimit
                    && vehicleList.get(sp).getDistanceInSegment() > lowerLimit)) {
                //System.out.println(vehicleList.get(sp).getDistance()+" "+ upperLimit+" "+lowerLimit);
                return false;
            }

        }

        for (int i = 0; i < objectList.size(); i++) {
            double objpos = objectList.get(i).getInitPos();
            if (lowerLimit < objpos && objpos < upperLimit) {
                if (!accident) {
                    return false;
                } else {
                    //println(lowerLimit + " " + objpos + " " + upperLimit);
                }
                objectList.get(i).getSegment().setAccidentCount(objectList.get(i).getSegment().getAccidentCount() + 1);//updateAccidentcount();
                objectList.get(i).inAccident = true;
                delObject(objectList.get(i));
            }
        }
        return true;

    }

    /*similar to isGapForMoveForward but doesn't consider vehicle speed, checks whether there  
        *is enough space for a given vehicle forward movement.
     */
    public boolean isGapForStripChange(Vehicle v) {
        double thresholdDistance = 0.1;
        double lowerLimit1 = v.getDistanceInSegment() - thresholdDistance;
        double upperLimit1 = v.getDistanceInSegment() + v.getLength() + thresholdDistance;

        for (int sp = 0; sp < vehicleList.size(); sp++) {
            double lowerLimit2 = vehicleList.get(sp).getDistanceInSegment() - thresholdDistance;
            double upperLimit2 = vehicleList.get(sp).getDistanceInSegment()
                    + vehicleList.get(sp).getLength() + thresholdDistance;
            if ((lowerLimit1 >= lowerLimit2 && lowerLimit1 <= upperLimit2
                    || upperLimit1 >= lowerLimit2 && upperLimit1 <= upperLimit2)
                    || (lowerLimit2 >= lowerLimit1 && lowerLimit2 <= upperLimit1
                    || upperLimit2 >= lowerLimit1 && upperLimit2 <= upperLimit1)) {
                return false;
            }
        }
        //____________________
        if (v.isReverseSegment()) {
            double lower = lowerLimit1;
            double upper = upperLimit1;
            lowerLimit1 = v.getLink().getSegment(v.getSegmentIndex()).getLength() - upper;
            upperLimit1 = v.getLink().getSegment(v.getSegmentIndex()).getLength() - lower;
        }
        //____________________                
        boolean accident = false;
        Random rand = new Random();
        if (rand.nextInt() % DhakaSimFrame.encounterPerAccident == 0) {
            accident = true;
        }
        for (int i = 0; i < objectList.size(); i++) {
            double objpos = objectList.get(i).getInitPos();
            if (lowerLimit1 < objpos && objpos < upperLimit1 ) {
                if (!accident) {
                    return false;
                } else {
                    //System.out.println(lowerLimit1 + " " + objpos + " " + upperLimit1);
                }
                objectList.get(i).getSegment().setAccidentCount(objectList.get(i).getSegment().getAccidentCount() + 1);//updateAccidentcount();
                objectList.get(i).inAccident = true;
                delObject(objectList.get(i));
            }
        }

        return true;

    }

}
