package uk.ac.brunel.ovidiuparvu.jdbscan.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.brunel.ovidiuparvu.jdbscan.exception.error.InputException;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.TimeSerie;

/**
 * Class used for reading the time series from a provided file
 */
public class InputReader {

	private static final String ERR_POINTS_NR = "Number of points for timeseries is not equal.";
	private static final String ERR_FILE_EMPTY = "The input file is empty.";
	private static final String SEPARATOR = ",";
	private static final String ERR_READ_FILE = "An error occurred while trying to read from the input file.";

	
	public static Input readInput(File file) throws InputException {
		if ((file != null) && (file.canRead())) {
			try {
				Input input = readInputFromFile(file);
				
				verifyInput(input);
				
				return input;
			} catch (InputException exception) {
				throw exception;
			} catch (IOException e) {
				throw new InputException(ERR_READ_FILE);
			}
		}
		
		throw new InputException(ERR_READ_FILE);
	}

	private static void verifyInput(Input input) throws InputException {
		int validSize = input.getTimes().size();
		int nrOfTimeSeries = input.getTimeSeries().size();
		
		for (int i = 0; i < nrOfTimeSeries; i++) {
			if (input.getTimeSeries().get(i).getNrOfConcentrations() != validSize)
				throw new InputException(ERR_POINTS_NR);
		}
	}

	private static Input readInputFromFile(File file) throws IOException, InputException {
		Input input = new Input();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		// Read the time sequence first
		readTimeSequence(reader, input);
		
		// Afterwards read all the time series - each one is equivalent to 1 line from the file
		while (reader.ready()) {
			readTimeSerie(reader, input);
		}
		
		return input;
	}

	private static void readTimeSerie(BufferedReader reader, Input input) throws IOException {
		String[] lineTokens = reader.readLine().split(SEPARATOR);
		TimeSerie timeSerie = new TimeSerie();
		
		for (String token : lineTokens) {
			Double concentration = Double.parseDouble(token);
			
			timeSerie.addConcentration(concentration);
		}
		
		input.addTimeSerie(timeSerie);
	}

	private static List<Double> readTimeSequence(BufferedReader reader, Input input) throws IOException, InputException {
		if (reader.ready()) {
			String[] lineTokens = reader.readLine().split(SEPARATOR);
			List<Double> timeSequence = new ArrayList<Double>();
			
			for (String lineToken : lineTokens) {
				Double timePoint = Double.parseDouble(lineToken);
				
				input.addTimePoint(timePoint);
			}
			
			return timeSequence;
		}
		
		throw new InputException(ERR_FILE_EMPTY);
	}

}
