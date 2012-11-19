package uk.ac.brunel.ovidiuparvu.jdbscan.view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import uk.ac.brunel.ovidiuparvu.jdbscan.model.Cluster;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;

public class ClusterPrinter extends ClusterViewer {

	private static final String OUTPUT_FILE 		= "clusters.txt";
	private static final String ERR_FILE_PRINT 		= "An error occurred while printing the results in the output file.";
	private static final String SEPARATOR_COLUMN	= " ";
	private static final String SEPARATOR_CLUSTER	= "_";
	private static final String HEADER_TIME 		= "Time";
	private static final String HEADER_CLUSTER 		= "C";

	
	public ClusterPrinter(List<Cluster> clusters, Input input) {
		super(clusters, input);
	}
	
	@Override
	public void run() {
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(new FileWriter(OUTPUT_FILE));
			
			printResultsToFile(writer);
		} catch (IOException exception) {
			System.out.println(ERR_FILE_PRINT);;
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	private void printResultsToFile(PrintWriter writer) {
		printFileHeader(writer);
		printFileContents(writer);
	}

	private void printFileContents(PrintWriter writer) {
		int nrOfConcentrations = times.size();
		int nrOfClusters = clusters.size();
		
		for (int i = 0; i < nrOfConcentrations; i++) {
			writeToFile(writer, times.get(i), false);
			
			for (int j = 0; j < nrOfClusters; j++) {
				List<Integer> timeSerieIndexes = clusters.get(j).getTimeSeriesIndexes();
				int nrOfElementsInCluster = timeSerieIndexes.size();
				
				for (int k = 0; k < nrOfElementsInCluster; k++) {
					if (isLastElement(nrOfClusters, j, nrOfElementsInCluster, k)) {
						writeToFile(writer, timeSeries.get(timeSerieIndexes.get(k)).getConcentrationAt(i), true);
					} else {
						writeToFile(writer, timeSeries.get(timeSerieIndexes.get(k)).getConcentrationAt(i), false);
					}
				}
			}
		}
	}

	private void printFileHeader(PrintWriter writer) {
		writeToFile(writer, HEADER_TIME, false);
		
		int nrOfClusters = clusters.size();
		
		for (int i = 0; i < nrOfClusters; i++) {
			int nrOfElementsInCluster = clusters.get(i).getTimeSeriesIndexes().size();
			
			for (int j = 0; j < nrOfElementsInCluster; j++) {
				if (isLastElement(nrOfClusters, i, nrOfElementsInCluster, j)) {
					writeToFile(writer, getHeaderClusterString(i, j), true);
				} else {
					writeToFile(writer, getHeaderClusterString(i, j), false);
				}
			}
		}
	}

	private String getHeaderClusterString(int i, int j) {
		return HEADER_CLUSTER + SEPARATOR_CLUSTER + i + SEPARATOR_CLUSTER + j;
	}

	private void writeToFile(PrintWriter writer, String text, boolean isLastElement) {
		if (isLastElement) {
			writer.println(text);
		} else {
			writer.print(text + SEPARATOR_COLUMN);
		}
	}
	
	private void writeToFile(PrintWriter writer, Double element, boolean isLastElement) {
		if (isLastElement) {
			writer.println(element.toString());
		} else {
			writer.print(element.toString() + SEPARATOR_COLUMN);
		}
	}
	
	private boolean isLastElement(int nrOfClusters, int j,
			int nrOfElementsInCluster, int k) {
		return (j == (nrOfClusters - 1)) && (k == (nrOfElementsInCluster - 1));
	}

}
