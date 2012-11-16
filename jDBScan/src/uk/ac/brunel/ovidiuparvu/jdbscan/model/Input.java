package uk.ac.brunel.ovidiuparvu.jdbscan.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for the input read from the file
 */
public class Input {

	private List<TimeSerie> timeSeries;
	private List<Double> times;
	
	
	public Input() {
		timeSeries = new ArrayList<TimeSerie>();
		times = new ArrayList<Double>();
	}
	
	public List<TimeSerie> getTimeSeries() {
		return timeSeries;
	}

	public List<Double> getTimes() {
		return times;
	}

	public void addTimeSerie(TimeSerie timeSerie) {
		timeSeries.add(timeSerie);
	}
	
	public void addTimePoint(Double time) {
		times.add(time);
	}
	
}
