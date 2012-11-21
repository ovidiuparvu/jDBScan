package uk.ac.brunel.ovidiuparvu.jdbscan.view.plot;

import java.util.List;

import uk.ac.brunel.ovidiuparvu.jdbscan.model.Cluster;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;

public class ClusterPlotScriptBuilder extends ClusterScriptBuilder {

	private static final String INPUT_FILE = "clusters_plot.txt";

	public ClusterPlotScriptBuilder(List<Cluster> clusters, Input input) {
		super(clusters, input);
		
		PATH_HEADER = "data/plot.in";
		PATH_SCRIPT = "data/plot.plt";
	}

	@Override
	protected void printScriptContents() {
		int nrOfClusters = clusters.size();
		
		for (int i = 0; i < nrOfClusters; i++) {
			String rgbColourSpec = getRgbColourSpec(i);
			
			List<Integer> timeSeriesIndexes = clusters.get(i).getTimeSeriesIndexes();
			int nrOfElementsInCluster = timeSeriesIndexes.size();
			
			for (int j = 0; j < nrOfElementsInCluster; j++) {
				// Indexing is 1-based and we skip the time column
				int index = timeSeriesIndexes.get(j) + 2;
				
				if (isFirstLine(i, j)) {
					writer.println(getTimeSerieLine(rgbColourSpec, index, 0));
				} else if (isLastLine(i, nrOfClusters, j, nrOfElementsInCluster)) {
					writer.println(getTimeSerieLine(rgbColourSpec, index, 2));
				} else {
					writer.println(getTimeSerieLine(rgbColourSpec, index, 1));
				}
			}
		}
	}
	
	protected String getTimeSerieLine(String rgbColourSpec, int index, int position) {
		if (position == 0) 
			return "plot \"" + INPUT_FILE + "\" using 1:" + index + " with lines linecolor rgbcolor \"" + rgbColourSpec + "\", \\";
		else if (position == 1)
			return TAB_SPACE + "\"" + INPUT_FILE + "\" using 1:" + index + " with lines linecolor rgbcolor \"" + rgbColourSpec + "\", \\";	
		else
			return TAB_SPACE + "\"" + INPUT_FILE + "\" using 1:" + index + " with lines linecolor rgbcolor \"" + rgbColourSpec + "\"";
	}

}
