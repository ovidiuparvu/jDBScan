package uk.ac.brunel.ovidiuparvu.jdbscan.test;

import java.io.File;

import uk.ac.brunel.ovidiuparvu.jdbscan.exception.error.InputException;
import uk.ac.brunel.ovidiuparvu.jdbscan.input.InputReader;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;

/**
 * Class for running unit tests
 */
public class Test {

	private static final String INPUT_FILE_PATH = "c:\\Users\\cspgoop\\Dropbox\\Brunel\\eFacts\\exercises\\time_series\\erkpp.txt";

	public static void main(String[] args) {
		try {
			runInputTest(INPUT_FILE_PATH);
		} catch (InputException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void runInputTest(String filePath) throws InputException {
		File file = new File(filePath);
		
		Input input = InputReader.readInput(file);
		
		System.out.println(input);
	}
	
}
