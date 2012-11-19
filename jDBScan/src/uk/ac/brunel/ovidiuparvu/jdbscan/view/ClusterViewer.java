package uk.ac.brunel.ovidiuparvu.jdbscan.view;

import java.util.List;

import uk.ac.brunel.ovidiuparvu.jdbscan.model.Cluster;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.TimeSerie;

public abstract class ClusterViewer implements Runnable {

	protected List<Cluster> clusters;
	protected List<TimeSerie> timeSeries;
	protected List<Double> times;
	
	
	public ClusterViewer(List<Cluster> clusters, Input input) {
		this.clusters = clusters;
		this.timeSeries = input.getTimeSeries();
		this.times = input.getTimes();
	}
	
}
