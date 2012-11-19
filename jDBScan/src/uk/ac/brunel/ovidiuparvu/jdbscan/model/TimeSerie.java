package uk.ac.brunel.ovidiuparvu.jdbscan.model;

import java.util.ArrayList;
import java.util.List;

import uk.ac.brunel.ovidiuparvu.jdbscan.exception.error.InputException;

/**
 * Represent the time serie of a protein
 */
public class TimeSerie {
	
	private static final String ERR_TIMESERIES_LEN = "The lengths of the 2 timeseries are not equal.";
	
	private List<Double> concentrations;

	
	public TimeSerie() {
		concentrations = new ArrayList<Double>();
	}
	
	/**
	 * The distance is computed as the Euclidean distance between each pair
	 * of points in the time series
	 * @throws InputException 
	 */
	public double distanceTo(TimeSerie anotherTimeSerie) throws InputException {
		if (concentrations.size() != anotherTimeSerie.concentrations.size())
			throw new InputException(ERR_TIMESERIES_LEN);
		
		double distance = 0.0;
		
		for (int i = 0; i < concentrations.size(); i++) {
			distance += Math.abs(
							concentrations.get(i) - 
							anotherTimeSerie.concentrations.get(i)
						);
		}
		
		return distance;
	}
	
	public void addConcentration(double concentration) {
		concentrations.add(concentration);
	}
	
	public double getConcentrationAt(int index) {
		assert((index >= 0) && (index < concentrations.size()));
		
		return concentrations.get(index);
	}
	
	public int getNrOfConcentrations() {
		return concentrations.size();
	}
	
	@Override
	public String toString() {
		return concentrations.toString();
	}
	
}
