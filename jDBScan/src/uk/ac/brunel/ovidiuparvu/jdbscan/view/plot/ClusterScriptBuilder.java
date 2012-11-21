package uk.ac.brunel.ovidiuparvu.jdbscan.view.plot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Random;

import uk.ac.brunel.ovidiuparvu.jdbscan.model.Cluster;
import uk.ac.brunel.ovidiuparvu.jdbscan.model.Input;
import uk.ac.brunel.ovidiuparvu.jdbscan.view.ClusterViewer;
import uk.ac.brunel.ovidiuparvu.jdbscan.view.util.RGBColourGenerator;

public abstract class ClusterScriptBuilder extends ClusterViewer {

	protected static final String ERR_SCRIPT = "An error occurred while trying to create the plot script.";
	protected static final String TAB_SPACE = "\t ";
	
	protected static String PATH_HEADER = "";
	protected static String PATH_SCRIPT = "";
	
	protected static final int COLOURS_GENERATE_NUMBER = 50;
	protected static final int SHUFFLE_ROUNDS = 100;
	protected static final int CHANNELS_NUMBER = 3;
	
	protected PrintWriter writer = null;
	protected BufferedReader reader = null;
	
	protected int[][] colours = null;
	
	
	public ClusterScriptBuilder(List<Cluster> clusters, Input input) {
		super(clusters, input);
	}

	@Override
	public void run() {
		try {
			init();
			printScriptContents();
			printScriptFooter();
		} catch (IOException ex) {
			System.out.println(ERR_SCRIPT);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	protected abstract void printScriptContents();
	
	protected void printScriptFooter() {
		writer.println("pause -1 \"Hit any key to continue\"");
	}

	protected boolean isFirstLine(int i, int j) {
		return ((i == 0) && (j == 0));
	}

	protected boolean isLastLine(int i, int nrOfClusters, int j,
			int nrOfElementsInCluster) {
		return ((i == (nrOfClusters - 1)) && 
				(j == (nrOfElementsInCluster - 1)));
	}

	protected String getRgbColourSpec(int i) {
		return "#" + Integer.toHexString(colours[i][0]) + 
				     Integer.toHexString(colours[i][1]) +
				     Integer.toHexString(colours[i][2]);
	}
	
	/**
	 * Copy the header of the plot script from the input file to the output file
	 * @throws IOException
	 */
	private void init() throws IOException {
		initColours();
		
		writer = new PrintWriter(new FileWriter(PATH_SCRIPT));
		reader = new BufferedReader(new FileReader(PATH_HEADER));
		
		while (reader.ready()) {
			writer.println(reader.readLine());
		}
		
		reader.close();
	}
	
	private void initColours() {
		int[][] tmpColours = RGBColourGenerator.generate(COLOURS_GENERATE_NUMBER);
		int nrOfNeededColours = clusters.size();
		
		shuffleColours(tmpColours);
		
		getColours(tmpColours, nrOfNeededColours);
	}

	private void getColours(int[][] tmpColours, int nrOfNeededColours) {
		colours = new int[nrOfNeededColours][CHANNELS_NUMBER];
		
		for (int i = 0; i < nrOfNeededColours; i++) {
			colours[i] = tmpColours[i];
		}
	}

	private void shuffleColours(int[][] tmpColours) {
		Random randomGenerator = new Random((new Date()).getTime());
		
		for (int i = 0; i < SHUFFLE_ROUNDS; i++) {
			int j = randomGenerator.nextInt(COLOURS_GENERATE_NUMBER);
			int k = j;
			
			while (k == j) {
				k = randomGenerator.nextInt(COLOURS_GENERATE_NUMBER);
			}
			
			switchColours(tmpColours, j, k);
		}
		
	}

	private void switchColours(int[][] tmpColours, int j, int k) {
		int[] tmp = tmpColours[j];
		tmpColours[j] = tmpColours[k];
		tmpColours[k] = tmp;
	}
	
}
