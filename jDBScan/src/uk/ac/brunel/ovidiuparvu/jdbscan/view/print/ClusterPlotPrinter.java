package uk.ac.brunel.ovidiuparvu.jdbscan.view.print;

import java.io.PrintWriter;
import java.util.List;

import uk.ac.brunel.ovidiuparvu.jdbscan.model.Cluster;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;

public class ClusterPlotPrinter extends ClusterPrinter {

	public ClusterPlotPrinter(List<Cluster> clusters, Input input) {
		super(clusters, input);
		
		OUTPUT_FILE = "data/clusters_plot.txt";
	}
	
	protected void printResultsToFile(PrintWriter writer) {
		//printFileHeader(writer);
		printFileContents(writer);
	}

	protected void printFileContents(PrintWriter writer) {
		int nrOfConcentrations = times.size();
		int nrOfClusters = clusters.size();
		
		for (int i = 0; i < nrOfConcentrations; i++) {
			writeToFile(times.get(i), false);
			
			for (int j = 0; j < nrOfClusters; j++) {
				List<Integer> timeSerieIndexes = clusters.get(j).getTimeSeriesIndexes();
				int nrOfElementsInCluster = timeSerieIndexes.size();
				
				for (int k = 0; k < nrOfElementsInCluster; k++) {
					if (isLastElement(nrOfClusters, j, nrOfElementsInCluster, k)) {
						writeToFile(timeSeries.get(timeSerieIndexes.get(k)).getConcentrationAt(i), true);
					} else {
						writeToFile(timeSeries.get(timeSerieIndexes.get(k)).getConcentrationAt(i), false);
					}
				}
			}
		}
	}

	protected void printFileHeader(PrintWriter writer) {
		writeToFile(HEADER_TIME, false);
		
		int nrOfClusters = clusters.size();
		
		for (int i = 0; i < nrOfClusters; i++) {
			int nrOfElementsInCluster = clusters.get(i).getTimeSeriesIndexes().size();
			
			for (int j = 0; j < nrOfElementsInCluster; j++) {
				if (isLastElement(nrOfClusters, i, nrOfElementsInCluster, j)) {
					writeToFile(getHeaderClusterString(i, j), true);
				} else {
					writeToFile(getHeaderClusterString(i, j), false);
				}
			}
		}
	}

}
