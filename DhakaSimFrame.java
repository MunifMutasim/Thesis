package thesisfinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

import static java.lang.Math.*;
import static thesisfinal.Misc.*;
import static thesisfinal.DhakaSimFrame.*;

public class DhakaSimFrame extends JFrame {

    public static int simulationStep;
    public static int simulationEndTime;
    public static int pixelPerStrip;
    public static int pixelPerMeter;
    public static int pixelPerFootpathStrip;
    public static int simulationSpeed;
    public static int encounterPerAccident;//-----------------------------------rename and redefine
    public static double stripWidth;
    public static double footpathStripWidth;
    public static double maximumSpeed;
    public static boolean pedestrainmode;

    public DhakaSimFrame() {
        initialize();
        getContentPane().add(new OptionPanel(this));
        setTitle("DhakaSim");
        setSize(1250, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void initialize() {
        try {
            //Reading FILE..
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("parameter.txt")));
            while (true) {
                String dataLine = bufferedReader.readLine();
                if (dataLine == null) {
                    bufferedReader.close();
                    break;
                }
                StringTokenizer stringTokenizer = new StringTokenizer(dataLine);
                String name = stringTokenizer.nextToken();
                String value = stringTokenizer.nextToken();
                switch (name) {
                    case "SimulationEndTime":
                        simulationEndTime = Integer.parseInt(value);
                        break;
                    case "PixelPerStrip":
                        pixelPerStrip = Integer.parseInt(value);
                        break;
                    case "PixelPerMeter":
                        pixelPerMeter = Integer.parseInt(value);
                        break;
                    case "PixelPerFootpathStrip":
                        pixelPerFootpathStrip = Integer.parseInt(value);
                        break;
                    case "SimulationSpeed":
                        simulationSpeed = Integer.parseInt(value);
                        break;
                    case "EncounterPerAccident":
                        encounterPerAccident = Integer.parseInt(value);
                        break;
                    case "StripWidth":
                        stripWidth = Double.parseDouble(value);
                        break;
                    case "FootpathStripWidth":
                        footpathStripWidth = Double.parseDouble(value);
                        break;
                    case "MaximumSpeed":
                        maximumSpeed = Double.parseDouble(value);
                        break;
                    case "PedestrainMode":
                        pedestrainmode = value.equals("On");
                        break;
                    default:
                        break;
                }
            }
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(DhakaSimPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
