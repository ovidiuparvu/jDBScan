package uk.ac.brunel.ovidiuparvu.jdbscan.test;

import java.io.File;

import uk.ac.brunel.ovidiuparvu.jdbscan.exception.error.InputException;
import uk.ac.brunel.ovidiuparvu.jdbscan.input.InputReader;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;
import uk.ac.brunel.ovidiuparvu.jdbscan.view.util.RGBColourGenerator;

/**
 * Class for running unit tests
 */
@SuppressWarnings("unused")
public class Test {
	
	private static final String SEPARATOR = " ";
	private static final String INPUT_FILE_PATH = "c:\\Users\\cspgoop\\Dropbox\\Brunel\\eFacts\\exercises\\time_series\\erkpp.txt";

	
	public static void main(String[] args) {
		runColourTest(20);
	}

	public static void runInputTest(String filePath) throws InputException {
		File file = new File(filePath);

		Input input = InputReader.readInput(file);

		System.out.println(input);
	}

	public static void runColourTest(int number) {
		int[][] colours = RGBColourGenerator.generate(number);

		printColours(colours, number);
	}

	private static void printColours(int[][] colours, int number) {
		for (int i = 0; i < number; i++) {
			System.out.println(colours[i][0] + SEPARATOR + colours[i][1] + SEPARATOR + colours[i][2]);
		}
	}
	
}
