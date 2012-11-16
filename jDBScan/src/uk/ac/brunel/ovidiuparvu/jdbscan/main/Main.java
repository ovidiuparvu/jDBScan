package uk.ac.brunel.ovidiuparvu.jdbscan.main;

import java.io.File;
import java.util.InputMismatchException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.ac.brunel.ovidiuparvu.jdbscan.algorithm.DBScan;
import uk.ac.brunel.ovidiuparvu.jdbscan.exception.error.InputException;
import uk.ac.brunel.ovidiuparvu.jdbscan.input.InputReader;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Cluster;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;

public class Main {

	private static final String PARAM_HELP = "help";
	private static double eps = 0;
	private static int minNrOfNeighbours = 0;
	private static String filePath = null;
	
	
	public static void main(String[] args) {
		try {
			initParameters(args);
			
			File file = new File(filePath);
			Input input = InputReader.readInput(file);
			
			List<Cluster> clusters = DBScan.run(input.getTimeSeries(), eps, minNrOfNeighbours);
			
			System.out.println(clusters);
		}  catch (InputException e) {
			System.out.println(e.getMessage());
		} catch (InputMismatchException e) {
			printHelpMessage();
		} catch (ExecutionException e) {
			printErrorMessage();
		}
	}


	private static void initParameters(String[] args) throws ExecutionException {
		if ((args.length == 1) && (args[0].equalsIgnoreCase(PARAM_HELP))) {
			throw new InputMismatchException();
		} else if (args.length == 3) {
			getParameters(args);
		} else {
			throw new ExecutionException(null);
		}
	}


	private static void getParameters(String[] args) throws ExecutionException {
		try {
			eps = Double.parseDouble(args[0]);
			minNrOfNeighbours = Integer.parseInt(args[1]);
			filePath = args[2];
		} catch (Exception ex) {
			throw new ExecutionException(null);
		}
	}


	private static void printErrorMessage() {
		System.out.println("Incorrect syntax or number of parameters. Please execute the program as indicated below:");
		System.out.println();
		System.out.println("  java -jar jdbscan.jar <distance_threshold> <minimum_number_of_neighbours> <filepath>");
		System.out.println();
	}


	private static void printHelpMessage() {
		System.out.println("The jDBScan program is a density based clustering algorithm implemented in Java.");
		System.out.println();
		System.out.println("Usage:");
		System.out.println("  java -jar jdbscan.jar <distance_threshold> <minimum_number_of_neighbours> <filepath>");
		System.out.println();
	}

}