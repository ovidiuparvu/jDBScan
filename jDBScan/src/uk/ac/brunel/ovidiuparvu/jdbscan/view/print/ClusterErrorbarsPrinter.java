package uk.ac.brunel.ovidiuparvu.jdbscan.view.print;

import java.io.PrintWriter;
import java.util.List;

import uk.ac.brunel.ovidiuparvu.jdbscan.model.Cluster;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.TimeSerie;

public class ClusterErrorbarsPrinter extends ClusterPrinter {

	private static final int VARIATION_DATA_SIZE  = 3;
	private static final int VARIATION_MIN_INDEX  = 0;
	private static final int VARIATION_MAX_INDEX  = 1;
	private static final int VARIATION_MEAN_INDEX = 2;
	private static final int STEP_SIZE = 500;

	private Double[][][] clusterVariationData;
	
	private Double[] clusterMinimumData;
	private Double[] clusterMaximumData;
	private Double[] clusterMeanData;
	
	private int nrOfConcentrations;
	private int nrOfClusters;
	
	
	public ClusterErrorbarsPrinter(List<Cluster> clusters, Input input) {
		super(clusters, input);
		
		initialise();
		
		OUTPUT_FILE = "data/clusters_errorbars.txt";
	}

	@Override
	protected void printResultsToFile(PrintWriter writer) {
		for (int i = 0; i < nrOfConcentrations; i += STEP_SIZE) {
			writeToFile(times.get(i), false);
			
			for (int j = 0; j < nrOfClusters; j++) {
				for (int k = 0; k < VARIATION_DATA_SIZE; k++) {
					if (isLastElement(nrOfClusters, j, VARIATION_DATA_SIZE, k)) {
						writeToFile(clusterVariationData[j][k][i], true);
					} else {
						writeToFile(clusterVariationData[j][k][i], false);
					}
				}
			}
		}
	}
	
	private void initialise() {
		nrOfConcentrations = times.size();
		nrOfClusters = clusters.size();
		
		initialise(nrOfClusters, nrOfConcentrations);
		
		for (int i = 0; i < nrOfClusters; i++) {
			List<Integer> timeSeriesIndexes = clusters.get(i).getTimeSeriesIndexes();
			int nrOfElementsInCluster = timeSeriesIndexes.size();
			
			computeClusterVariationData(nrOfConcentrations, timeSeriesIndexes, nrOfElementsInCluster);
			
			updateClusterVariationData(i);
		}
	}

	private void computeClusterVariationData(int nrOfConcentrations, List<Integer> timeSeriesIndexes, int nrOfElementsInCluster) {
		for (int j = 0; j < nrOfElementsInCluster; j++) {
			TimeSerie timeSerie = timeSeries.get(timeSeriesIndexes.get(j));
			
			for (int k = 0; k < nrOfConcentrations; k += STEP_SIZE) {
				double concentration = timeSerie.getConcentrationAt(k);
				
				updateMinimum(k, concentration);
				updateMaximum(k, concentration);
				updateMean(k, concentration);
			}
		}
	}

	private void updateClusterVariationData(int clusterIndex) {
		for (int i = 0; i < nrOfConcentrations; i += STEP_SIZE) {
			clusterVariationData[clusterIndex][VARIATION_MIN_INDEX][i] = clusterMinimumData[i];
			clusterVariationData[clusterIndex][VARIATION_MAX_INDEX][i] = clusterMaximumData[i];
			clusterVariationData[clusterIndex][VARIATION_MEAN_INDEX][i] = clusterMeanData[i];
		}
	}

	private void initialise(int nrOfClusters, int nrOfConcentrations) {
		clusterVariationData = new Double[nrOfClusters][VARIATION_DATA_SIZE][nrOfConcentrations];
		
		clusterMinimumData = new Double[nrOfConcentrations];
		clusterMaximumData = new Double[nrOfConcentrations];
		clusterMeanData = new Double[nrOfConcentrations];
		
		initialiseData(clusterMinimumData, (Double.MAX_VALUE / 2));
		initialiseData(clusterMaximumData, (Double.MIN_VALUE / 2));
		initialiseData(clusterMeanData, 0);
	}

	private void initialiseData(Double[] clusterData, double value) {
		for (int i = 0; i < nrOfConcentrations; i++) {
			clusterData[i] = value;
		}
	}

	private void updateMean(int index, double concentration) {
		clusterMeanData[index] += (concentration / nrOfConcentrations);
	}

	private void updateMaximum(int index, double concentration) {
		if (concentration > clusterMaximumData[index]) {
			clusterMaximumData[index] = concentration;
		}
	}

	private void updateMinimum(int index, double concentration) {
		if (concentration < clusterMinimumData[index]) {
			clusterMinimumData[index] = concentration;
		}
	}

}
