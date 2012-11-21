package uk.ac.brunel.ovidiuparvu.jdbscan.view.print;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import uk.ac.brunel.ovidiuparvu.jdbscan.model.Cluster;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;
import uk.ac.brunel.ovidiuparvu.jdbscan.view.ClusterViewer;

public abstract class ClusterPrinter extends ClusterViewer {

	protected static final String ERR_FILE_PRINT 	= "An error occurred while printing the results in the output file.";
	protected static final String SEPARATOR_COLUMN	= " ";
	protected static final String SEPARATOR_CLUSTER	= "_";
	protected static final String HEADER_TIME 		= "#Time";
	protected static final String HEADER_CLUSTER 	= "C";
	
	protected static String OUTPUT_FILE 			= "";
	
	protected PrintWriter writer;

	
	public ClusterPrinter(List<Cluster> clusters, Input input) {
		super(clusters, input);
		
		writer = null;
	}
	
	@Override
	public void run() {
		try {
			writer = new PrintWriter(new FileWriter(OUTPUT_FILE));
			
			printResultsToFile(writer);
		} catch (IOException exception) {
			System.out.println(ERR_FILE_PRINT);
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	protected abstract void printResultsToFile(PrintWriter writer);

	protected String getHeaderClusterString(int i, int j) {
		return HEADER_CLUSTER + SEPARATOR_CLUSTER + i + SEPARATOR_CLUSTER + j;
	}

	protected void writeToFile(String text, boolean isLastElement) {
		if (isLastElement) {
			writer.println(text);
		} else {
			writer.print(text + SEPARATOR_COLUMN);
		}
	}
	
	protected void writeToFile(Double element, boolean isLastElement) {
		if (isLastElement) {
			writer.println(element.toString());
		} else {
			writer.print(element.toString() + SEPARATOR_COLUMN);
		}
	}
	
	protected boolean isLastElement(int nrOfClusters, int j,
			int nrOfElementsInCluster, int k) {
		return (j == (nrOfClusters - 1)) && (k == (nrOfElementsInCluster - 1));
	}

}
