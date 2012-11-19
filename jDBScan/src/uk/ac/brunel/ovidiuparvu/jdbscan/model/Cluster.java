package uk.ac.brunel.ovidiuparvu.jdbscan.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for a cluster 
 */
public class Cluster {

	private static int currentId = 0;
	
	private int id;
	private List<Integer> timeSeriesIndexes;
	
	
	public Cluster() {
		id = currentId++;
		timeSeriesIndexes = new ArrayList<Integer>();
	}
	
	public int getId() {
		return id;
	}

	public List<Integer> getTimeSeriesIndexes() {
		return timeSeriesIndexes;
	}

	public void addTimeSerieIndex(Integer timeSerieIndex) {
		if (!timeSeriesIndexes.contains(timeSerieIndex))
			timeSeriesIndexes.add(timeSerieIndex);
	}
	
}
