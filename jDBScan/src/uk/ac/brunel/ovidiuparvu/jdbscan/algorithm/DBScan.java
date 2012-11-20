package uk.ac.brunel.ovidiuparvu.jdbscan.algorithm;

import java.util.ArrayList;
import java.util.List;

import uk.ac.brunel.ovidiuparvu.jdbscan.exception.error.InputException;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Cluster;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.TimeSerie;

/**
 * Implementation of the density based clustering algorithm DBSCAN
 * 
 * A description of the algorithm can be found on Wikipedia (Last check: 16/11/2012)
 */
public class DBScan {

	private static final int CLUSTER_NOISE_IDX = 0;
	private static double eps = 0.0;
	private static int minNrOfNeighbours = 0;
	
	private static List<Boolean> 	visitedList 	= new ArrayList<Boolean>();
	private static List<Cluster> 	clusters 		= null;
	private static List<TimeSerie> 	timeSeries 		= null;
	private static double[][] 		distanceMatrix 	= null;
	
	public static List<Cluster> run(List<TimeSerie> timeSeries, double eps, int minNrOfNeighbours) throws InputException {
		clusters = new ArrayList<Cluster>();
		
		init(timeSeries, eps, minNrOfNeighbours);
		
		int indexOfUnvisited = getIndexOfUnvisited();
		
		while (indexOfUnvisited != -1) {
			TimeSerie timeSerie = timeSeries.get(indexOfUnvisited);
			
			markAsVisited(indexOfUnvisited);
			
			List<TimeSerie> neighbours = getNeighboursWithinRange(indexOfUnvisited);
			
			if (neighbours.size() < minNrOfNeighbours) {
				clusters.get(CLUSTER_NOISE_IDX).addTimeSerieIndex(indexOfUnvisited);
			} else {
				Cluster cluster = new Cluster();
				
				clusters.add(cluster);
				
				expandCluster(timeSerie, neighbours, cluster);
			}
			
			indexOfUnvisited = getIndexOfUnvisited();
		}
		
		return clusters;
	}

	private static void expandCluster(TimeSerie timeSerie, List<TimeSerie> neighbours, Cluster cluster) {
		cluster.addTimeSerieIndex(timeSeries.indexOf(timeSerie));
		
		int nrOfNeighbours = neighbours.size();
		
		for (int i = 0; i < nrOfNeighbours; i++) {
			int indexNeighbourTimeSerie = timeSeries.indexOf(neighbours.get(i));
			
			if (visitedList.get(indexNeighbourTimeSerie) == false) {
				visitedList.set(indexNeighbourTimeSerie, true);
				
				List<TimeSerie> neighboursOfNeighbour = getNeighboursWithinRange(indexNeighbourTimeSerie);
				
				if (neighboursOfNeighbour.size() >= minNrOfNeighbours) {
					neighbours.addAll(neighboursOfNeighbour);
					
					nrOfNeighbours = neighbours.size();
				}
			}
			
			if (isNotMemeberOfClusters(indexNeighbourTimeSerie)) {
				cluster.addTimeSerieIndex(indexNeighbourTimeSerie);
			}
		}
	}

	private static boolean isNotMemeberOfClusters(int indexNeighbourTimeSerie) {
		int nrOfClusters = clusters.size();
		
		for (int i = 1; i < nrOfClusters; i++) {
			if (clusters.get(i).getTimeSeriesIndexes().contains(indexNeighbourTimeSerie))
				return false;
		}
		
		return true;
	}

	private static List<TimeSerie> getNeighboursWithinRange(int index) {
		List<TimeSerie> neighbours = new ArrayList<TimeSerie>();
		
		for (int i = 0; i < distanceMatrix.length; i++) {
			if (i != index) {
				double distance = distanceMatrix[index][i];
				
				if (distance < eps) {
					neighbours.add(timeSeries.get(i));
				}
			}
		}
		
		return neighbours;
	}

	private static void markAsVisited(int index) {
		visitedList.set(index, true);
	}

	private static int getIndexOfUnvisited() {
		int index = 0;
		
		while (index < visitedList.size()) {
			if (visitedList.get(index) == false) {
				return index;
			} else {
				index++;
			}
		}
		
		return -1;
	}

	/**
	 * The first cluster is reserved for "noise" time series
	 */
	private static void init(List<TimeSerie> timeSeries, double eps, int minNrOfNeighbours) throws InputException {
		distanceMatrix = getDistanceMatrix(timeSeries);
		
		clusters.add(new Cluster());
		
		DBScan.eps = eps;
		DBScan.minNrOfNeighbours = minNrOfNeighbours;
		DBScan.timeSeries = timeSeries;
		
		for (int i = 0; i < timeSeries.size(); i++) {
			visitedList.add(false);
		}
	}

	/**
	 * Construct the matrix which contains the distance between any two time series
	 * 
	 * However, only the lower half (w.r.t. diagonal) has to be computed, because
	 * dist(a, b) = dist(b, a)
	 */
	private static double[][] getDistanceMatrix(List<TimeSerie> timeSeries) throws InputException {
		double[][] distanceMatrix = new double[timeSeries.size()][timeSeries.size()];
		
		// Construct the lower half of the matrix
		for (int i = 0; i < timeSeries.size(); i++) {
			for (int j = 0; j < i; j++) {
				distanceMatrix[i][j] = timeSeries.get(i).distanceTo(timeSeries.get(j));
			}
		}
		
		// Copy the values from lower half to upper half of the matrix
		for (int i = 0; i < timeSeries.size(); i++) {
			for (int j = (i + 1); j < timeSeries.size(); j++) {
				distanceMatrix[i][j] = distanceMatrix[j][i];
			}
		}
		
		// Diagonal values should be zero
		for (int i = 0; i < timeSeries.size(); i++) {
			distanceMatrix[i][i] = 0;
		}
		
		return distanceMatrix;
	}

}
