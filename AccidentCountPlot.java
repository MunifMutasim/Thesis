package thesisfinal;

import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

public class AccidentCountPlot {

    Plot2DPanel plot = new Plot2DPanel();

    public AccidentCountPlot(double[] x) {
        plot.addBarPlot("Accident Count Plot", x);
        JFrame frame = new JFrame("Accident Count");
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);
    }
}
