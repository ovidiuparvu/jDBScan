package uk.ac.brunel.ovidiuparvu.jdbscan.view;

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
import uk.ac.brunel.ovidiuparvu.jdbscan.view.util.RGBColourGenerator;

public class ClusterPlotScriptBuilder extends ClusterViewer {

	private static final int COLOURS_GENERATE_NUMBER = 50;
	private static final String ERR_SCRIPT = "An error occurred while trying to create the plot script.";
	private static final String PATH_HEADER = "data/plot.in";
	private static final String PATH_SCRIPT = "data/plot.plt";
	private static final int SHUFFLE_ROUNDS = 20;
	private static final int CHANNELS_NUMBER = 3;
	private static final String TAB_SPACE = "\t ";
	
	private static PrintWriter writer = null;
	private static BufferedReader reader = null;
	private static int[][] colours = null;
	
	
	public ClusterPlotScriptBuilder(List<Cluster> clusters, Input input) {
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

	private void printScriptFooter() {
		writer.println("pause -1 \"Hit any key to continue\"");
	}

	private void printScriptContents() {
		int nrOfClusters = clusters.size();
		
		for (int i = 0; i < nrOfClusters; i++) {
			String rgbColourSpec = getRgbColourSpec(i);
			
			List<Integer> timeSeriesIndexes = clusters.get(i).getTimeSeriesIndexes();
			int nrOfElementsInCluster = timeSeriesIndexes.size();
			
			for (int j = 0; j < nrOfElementsInCluster; j++) {
				// Indexing is 1-based and we skip the time column
				int index = timeSeriesIndexes.get(j) + 2;
				
				if (isFirstLine(i, j)) {
					writer.println(getTimeSerieLine(rgbColourSpec, index, 0));
				} else if (isLastLine(i, nrOfClusters, j, nrOfElementsInCluster)) {
					writer.println(getTimeSerieLine(rgbColourSpec, index, 2));
				} else {
					writer.println(getTimeSerieLine(rgbColourSpec, index, 1));
				}
			}
		}
	}

	private boolean isFirstLine(int i, int j) {
		return ((i == 0) && (j == 0));
	}

	private boolean isLastLine(int i, int nrOfClusters, int j,
			int nrOfElementsInCluster) {
		return ((i == (nrOfClusters - 1)) && 
				(j == (nrOfElementsInCluster - 1)));
	}

	private String getTimeSerieLine(String rgbColourSpec, int index, int position) {
		if (position == 0) 
			return "plot \"clusters.txt\" using 1:" + index + " with lines linecolor rgbcolor \"" + rgbColourSpec + "\", \\";
		else if (position == 1)
			return TAB_SPACE + "\"clusters.txt\" using 1:" + index + " with lines linecolor rgbcolor \"" + rgbColourSpec + "\", \\";	
		else
			return TAB_SPACE + "\"clusters.txt\" using 1:" + index + " with lines linecolor rgbcolor \"" + rgbColourSpec + "\"";
	}

	private String getRgbColourSpec(int i) {
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
