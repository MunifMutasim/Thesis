package thesisfinal;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.math.plot.*;
public class SensorVehicleAvgSpeedPlot {


  // create your PlotPanel (you can use it as a JPanel)
  Plot2DPanel plot = new Plot2DPanel();

  //plots vehicle average speed vs segment
  public SensorVehicleAvgSpeedPlot(double[] x)
  {
      // Adding Plots
      plot.addBarPlot("Speed Plot", x);
      // put the PlotPanel in a JFrame, as a JPanel
      JFrame frame = new JFrame("Vehicle Average Speed");
      frame.setSize(600, 600);
      frame.setContentPane(plot);
      frame.setVisible(true);
  }
}




