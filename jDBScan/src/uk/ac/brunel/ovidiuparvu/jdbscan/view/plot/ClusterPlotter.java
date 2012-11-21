package uk.ac.brunel.ovidiuparvu.jdbscan.view.plot;

import java.util.List;

import javax.swing.JFrame;

import uk.ac.brunel.ovidiuparvu.jdbscan.model.Cluster;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.TimeSerie;
import uk.ac.brunel.ovidiuparvu.jdbscan.view.ClusterViewer;

import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.swing.JPlot;

public class ClusterPlotter extends ClusterViewer {

	private static final String CLUSTER 	= "C";
	private static final String SEPARATOR 	= "_";
	
	private JPlot 		plot;
	private double[][] 	currentDataSet;

	public ClusterPlotter(List<Cluster> clusters, Input input) {
		super(clusters, input);
		
		plot 			= new JPlot();
		currentDataSet 	= new double[times.size()][2];
	}

	@Override
	public void run() {
        int nrOfClusters = clusters.size();
        
        for (int i = 0; i < nrOfClusters; i++) {
        	List<Integer> timeSeriesIndexes = clusters.get(i).getTimeSeriesIndexes();
        	int nrOfElementsInCluster = timeSeriesIndexes.size();
        	
        	for (int j = 0; j < nrOfElementsInCluster; j++) {
        		fillDataSet(timeSeriesIndexes.get(j));
        		createNewDataSetPlot(i, j);
        	}
        }
        
        plot.plot();

        displayPlot();
	}

	private void createNewDataSetPlot(int i, int j) {
		DataSetPlot dataSetPlot = new DataSetPlot(currentDataSet);
		
		dataSetPlot.getPlotStyle().setLineType(NamedPlotColor.BEIGE);
        dataSetPlot.setTitle(CLUSTER + SEPARATOR + i + SEPARATOR + j);
        
        plot.getJavaPlot().addPlot(dataSetPlot);
		
	}

	private void fillDataSet(Integer indexOfTimeSerie) {
		TimeSerie timeSerie = timeSeries.get(indexOfTimeSerie);
		int nrOfConcentrations = times.size();
		
		for (int i = 0; i < nrOfConcentrations; i++) {
			currentDataSet[i][0] = times.get(i);
			currentDataSet[i][1] = timeSerie.getConcentrationAt(i);
		}
	}

	private void displayPlot() {
		JFrame frame = new JFrame();
		
        setFrameProperties(frame);
	}

	private void setFrameProperties(JFrame frame) {
		frame.getContentPane().add(plot);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}

}
