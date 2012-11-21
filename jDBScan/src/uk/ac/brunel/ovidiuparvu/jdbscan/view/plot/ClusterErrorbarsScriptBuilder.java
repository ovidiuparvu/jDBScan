package uk.ac.brunel.ovidiuparvu.jdbscan.view.plot;

import java.util.List;

import uk.ac.brunel.ovidiuparvu.jdbscan.model.Cluster;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;

public class ClusterErrorbarsScriptBuilder extends ClusterScriptBuilder {

	private static final String PLOT_COLUMN_SEPARATOR = ":";
	private static final String INPUT_FILE = "clusters_errorbars.txt";
	
	
	public ClusterErrorbarsScriptBuilder(List<Cluster> clusters, Input input) {
		super(clusters, input);
		
		PATH_HEADER = "data/errorbars.in";
		PATH_SCRIPT = "data/errorbars.plt";
	}

	@Override
	protected void printScriptContents() {
		int nrOfClusters = clusters.size();
		
		for (int i = 0; i < nrOfClusters; i++) {
			String rgbColourSpec = getRgbColourSpec(i);
			
			if (i == 0) {
				writer.println(getClusterLine(rgbColourSpec, i, 0));
			} else if (i == (nrOfClusters - 1)){
				writer.println(getClusterLine(rgbColourSpec, i, 2));
			} else {
				writer.println(getClusterLine(rgbColourSpec, i, 1));
			}
		}
	}
	
	private String getClusterLine(String rgbColourSpec, int index, int position) {
		if (position == 0) 
			return "plot \"" + INPUT_FILE + "\" using 1:" + getIndexSpecification(index) + " with yerrorlines linecolor rgbcolor \"" + rgbColourSpec + "\", \\";
		else if (position == 1)
			return TAB_SPACE + "\"" + INPUT_FILE + "\" using 1:" + getIndexSpecification(index) + " with yerrorlines linecolor rgbcolor \"" + rgbColourSpec + "\", \\";	
		else
			return TAB_SPACE + "\"" + INPUT_FILE + "\" using 1:" + getIndexSpecification(index) + " with yerrorlines linecolor rgbcolor \"" + rgbColourSpec + "\"";
	}

	private String getIndexSpecification(int index) {
		return ((3 * index) + 4) + PLOT_COLUMN_SEPARATOR +
			   ((3 * index) + 2) + PLOT_COLUMN_SEPARATOR +
			   ((3 * index) + 3);
			   
	}

}
