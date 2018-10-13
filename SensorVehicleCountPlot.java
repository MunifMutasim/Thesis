package thesisfinal;

import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;
public class SensorVehicleCountPlot {


  // create your PlotPanel (you can use it as a JPanel)
  Plot2DPanel plot = new Plot2DPanel();
  //plots vehicle count vs segment
  public SensorVehicleCountPlot(double[] x)
  {
      // Adding Plots
      plot.addBarPlot("Vehicle Count Plot", x);
      // put the PlotPanel in a JFrame, as a JPanel
      JFrame frame = new JFrame("Vehicle Count");
      frame.setSize(600, 600);
      frame.setContentPane(plot);
      frame.setVisible(true);
  }
}




