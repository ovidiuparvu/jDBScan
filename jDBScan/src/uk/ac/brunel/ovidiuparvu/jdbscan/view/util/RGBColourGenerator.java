package uk.ac.brunel.ovidiuparvu.jdbscan.view.util;

public class RGBColourGenerator {

	private static final int RANGE_RED_MIN = 0;
	private static final int RANGE_GREEN_MIN = 0;
	private static final int RANGE_BLUE_MIN = 0;
	
	private static final int RANGE_RED_MAX = 200;
	private static final int RANGE_GREEN_MAX = 200;
	private static final int RANGE_BLUE_MAX = 200;
	
	private static final int CHANNELS_NUMBER = 3;
	private static int totalColourSpaceSize;
	private static int startingColourPoint;
	private static int stepForEachColour;
	
	/**
	 * Generate "number" colours uniformly distributed on the range
	 * RGB: <RANGE_RED_MIN, RANGE_GREEN_MIN, RANGE_BLUE_MIN> - <RANGE_RED_MAX, RANGE_GREEN_MAX, RANGE_BLUE_MAX>
	 * 
	 * colour_i = a + b * i, where
	 * 		a = MIN_RED + MIN_GREEN + MIN_BLUE
	 * 		b = TOTAL_COLOUR_SPACE/(number - 1)
	 * 
	 * and 
	 * 		Amount_Red_i   = colour_i / ((RANGE_GREEN_MAX - RANGE_GREEN_MIN) * (RANGE_BLUE_MAX - RANGE_BLUE_MIN))
	 * 		Amount_Green_i = (colour_i - [Amount_Red_i * ((RANGE_GREEN_MAX - RANGE_GREEN_MIN) * (RANGE_BLUE_MAX - RANGE_BLUE_MIN))]) /
	 *                       (RANGE_BLUE_MAX - RANGE_BLUE_MIN)
	 * 	    Amount_Blue_i  = colour_i - [Amount_Red_i * ((RANGE_GREEN_MAX - RANGE_GREEN_MIN) * (RANGE_BLUE_MAX - RANGE_BLUE_MIN))]
	 * 								  - [Amount_Green_i * (RANGE_BLUE_MAX - RANGE_BLUE_MIN)]
	 * 
	 * @param number
	 * @return
	 */
	public static int[][] generate(int number) {
		int[][] colours  = new int[number][CHANNELS_NUMBER];
		int[] tmpColours = new int[number];
		
		init(number);
		
		for (int i = 0; i < number; i++) {
			tmpColours[i] = startingColourPoint + stepForEachColour * i;
			
			colours[i][0] = getAmountOfRed	(tmpColours[i]);
			colours[i][1] = getAmountOfGreen(tmpColours[i], colours[i][0]);
			colours[i][2] = getAmountOfBlue	(tmpColours[i], colours[i][0], colours[i][1]);
		}
		
		return colours;
	}

	private static int getAmountOfBlue(int colour, int red, int green) {
		return colour -
			   ((((RANGE_GREEN_MAX - RANGE_GREEN_MIN) * (RANGE_BLUE_MAX - RANGE_BLUE_MIN)) * red) +
				((RANGE_BLUE_MAX - RANGE_BLUE_MIN) * green));
	}

	private static int getAmountOfGreen(int colour, int red) {
		return (colour - (((RANGE_GREEN_MAX - RANGE_GREEN_MIN) * (RANGE_BLUE_MAX - RANGE_BLUE_MIN)) * red)) /
	           (RANGE_BLUE_MAX - RANGE_BLUE_MIN); 
	}

	private static int getAmountOfRed(int colour) {
		return colour / ((RANGE_GREEN_MAX - RANGE_GREEN_MIN) *
			             (RANGE_BLUE_MAX - RANGE_BLUE_MIN));
	}

	private static void init(int number) {
		totalColourSpaceSize = getTotalColorSpaceSize();
		startingColourPoint  = getColourStartingPoint();
		stepForEachColour    = totalColourSpaceSize / (number - 1);
	}

	private static int getColourStartingPoint() {
		return RANGE_RED_MIN + RANGE_GREEN_MIN + RANGE_BLUE_MIN;
	}

	private static int getTotalColorSpaceSize() {
		return (RANGE_RED_MAX - RANGE_RED_MIN)     *
		       (RANGE_GREEN_MAX - RANGE_GREEN_MIN) *
		       (RANGE_BLUE_MAX - RANGE_BLUE_MIN);
	}
}
