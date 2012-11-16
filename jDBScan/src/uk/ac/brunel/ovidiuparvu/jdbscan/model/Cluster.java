package uk.ac.brunel.ovidiuparvu.jdbscan.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for a cluster 
 */
public class Cluster {

	private static int currentId = 0;
	
	private int id;
	private List<TimeSerie> timeSeries;
	
	
	public Cluster() {
		id = currentId++;
		timeSeries = new ArrayList<TimeSerie>();
	}
	
	public int getId() {
		return id;
	}

	public List<TimeSerie> getTimeSeries() {
		return timeSeries;
	}

	public void addPoint(TimeSerie timeSerie) {
		timeSeries.add(timeSerie);
	}
	
}
