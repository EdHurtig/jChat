package HurtigTechnologies;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Utils {

	public static String Version = "1.2";

	/**
	 * --------------------------------------------------------- 
	 * UTILITIES CLASS FOR JAVA PROGRAMMING @ LSRHS 
	 * AUTHOR: EDDIE HURTIG
	 * SOME CODE (c) MIKE MALONE 
	 * ------------------------------------------------------------
	 */

	// Remove When Not Developing This Class
	public static void main(String[] args) {
		printInfo();
		
		System.out.println(readLine("End of Program - 'enter' to terminate"));
	}

	public static void printInfo() {
		printMSG("UTILITIES VERSION: "
				+ Version
				+ " FOR JAVA PROGRAMMING @ LSRHS\nAUTHOR: EDDIE HURTIG\nSOME CODE (c) MIKE MALONE");
	}

	public static PrintStream c = System.out;

	// ------------------- print_r ------------------- //
	
	// like print_r in PHP but it does JSON which makes it 10x better
	// Prints out the name and value of all fields, properties, ect. in the
	// given object
	
	// ------------------- MATH UTILITIES ------------------- //

	// Converts 1000000.234343 to 1,000,000.234343
	public static String commafy(double num) {
		String[] parts = String.valueOf(num).split("\\.");
		return reverse(joinStrArr(splitEvery(reverse(parts[0]), 3), ",")) + '.'
				+ parts[1];
	}

	// A good randomInt Method
	public static int randomInt(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1)); // Inclusive of
																// Min and Max
																// :: [ Min ,
																// Max ]
	}

	// Utility method that sums all the elements in an array of integers
	public static long sumArray(int[] arr) {
		long sum = 0;
		for (int i : arr)
			sum += i;
		return sum;
	}

	public static double sumArray(double[] arr) {
		double sum = 0.0;
		for (double d : arr)
			sum += d;
		return sum;
	}

	// Sleep utility method
	// seccond (1000 = 1 second)
	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// ------------------- STRING OPPERATIONS ------------------- //

	public static char toLower(char c) {
		if (c >= 'A' && c <= 'Z') {
			return (char) ((int) (c - 'A') + (int) 'a');
		}
		return c;
	}

	public static char toUpper(char c) {
		if (c >= 'a' && c <= 'z') {
			return (char) ((int) (c + 'A') - (int) 'a');
		}
		return c;
	}

	public static String cleanString(String word) {
		String cleanWord = "";
		for (int i = 0; i < word.length(); i++) {
			if ((word.charAt(i) >= 'a' && word.charAt(i) <= 'z'))
				cleanWord += word.charAt(i);

		}

		return cleanWord;
	}

	public static String joinStrArr(String[] arr, String sep) {
		String ret = "";
		for (String s : arr) {
			ret += s + sep;
		}
		return ret.substring(0, ret.length() - sep.length());
	}

	public static String[] splitEvery(String s, int n) {
		return s.split("(?<=\\G" + multipleStr(".", n) + ")");
	}

	public static String multipleStr(String s, int n) {
		if (n == 1)
			return s;
		return s + multipleStr(s, n - 1);

	}

	// Copies the char n times - like multiplSTR
	public static String getManyChars(int number, char c) {
		String dashedLine = "";
		for (int i = 0; i < number; i++) {
			dashedLine += c;
		}
		return dashedLine;
	}

	public static String centerWithPad(String message, int number, String pad,
			char sepChar) {
		return pad + center(message, sepChar, number - (2 * pad.length()))
				+ pad;
	}

	public static String center(String message, char sepChar, int number) {
		String line = "";
		String pad = getManyChars((number - message.length()) / 2, ' ');

		line = pad + message + pad;
		if (line.length() < number) {
			line += " ";
		}
		return line;
	}

	// RECURSIVE: reverse a string from "abc" to "cba"
	public static String reverse(String s) {

		if (s.length() <= 1) {
			return s;
		}

		return reverse(s.substring(1, s.length())) + s.charAt(0);
	}

	// ------------------- User Interactions ------------------- //

	public static String readLine(String prompt) {
		String line = null;
		Console c = System.console();
		if (c != null) {
			line = c.readLine(prompt);
		} else {
			System.out.print(prompt + ">>");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(System.in));
			try {
				line = bufferedReader.readLine();
			} catch (IOException e) {
				// Ignore
			}
		}
		return line;
	}

	// ------------------- User Interface ------------------- //

	public static int UIWidthChars = 60;
	public static char SepChar = '-';
	public static String PadStr = " * ";

	public static void printMSG(String message) {
		printMSG(message.split("\n"));
	}

	public static void printMSG(String[] messages) {
		System.out.println(getMSG(messages));

	}

	public static String getMSG(String message) {
		return getMSG(message.split("\n"));
	}

	public static String getMSG(String[] messages) {
		String ret;
		ret = "\n" + getManyChars(UIWidthChars, SepChar) + "\n";
		for (String message : messages) {
			for (String s : message.split("\n")) {
				ret += centerWithPad(s, UIWidthChars, PadStr, SepChar) + "\n";
			}
		}
		ret += getManyChars(UIWidthChars, SepChar) + "\n" + "\n";
		return ret;
	}

	// ------------------- File IO ------------------- //

	// readFile >> Lines to Array<string>

	public static String readFile(String fileName, int maxLines) {
		try {

			// shows what the path is (for testing/debugging)
			// System.out.println(System.getProperty("user.dir"));

			// longer form of file name w/ path (if needed)
			// FileInputStream fstream = new
			// FileInputStream(System.getProperty("user.dir") + "/" + fileName);

			// shorter form of file name assumes file in current directory
			// (which is the project directory
			// if no subdirectory option used)
			FileInputStream fstream = new FileInputStream(fileName);

			// Create a stream and reader for the file
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			int nLines = 0;
			String content = "";

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				nLines++;
				content += strLine + "\n";

				if (maxLines > 0 && nLines >= maxLines) {
					break;
				}
			}

			// Close the input stream
			in.close();
			return content;
		} catch (Exception e) { // Catch exception if any
			System.err.println("Error: " + e.getMessage());
			return "Error: " + e.getMessage();
		}

	}

	public static int versionCompare(String v1, String v2) {
		if (v1.equals(v2)) return 0;
		
		ArrayList<String> v1arr = new ArrayList<String>();
		ArrayList<String> v2arr = new ArrayList<String>();
		String[] v1s = v1.split("\\.");
		String[] v2s = v2.split("\\.");
		for (String s1 : v1s)
			v1arr.add(s1);
		for (String s2 : v2s)
			v2arr.add(s2);
		if (v1arr.get(0).equals(v2arr.get(0))) {
			v1arr.remove(0);
			v2arr.remove(0);
			v1 = "";
			v2 = "";
			for (String s3 : v1arr)
				v1 += s3 + ".";
			for (String s4 : v2arr)
				v2 += s4 + ".";
			System.out.println("v1: " + v1);
			System.out.println("v2: " + v2);
			if (v1.equals("")) return -1;
			if (v2.equals("")) return 1;
			
			v1 = v1.substring(0, v1.length() -1);
			v2 = v2.substring(0, v2.length() -1);	
			return versionCompare(v1,v2);
		}
		else
			return (Integer.parseInt(v1arr.get(0)) < Integer.parseInt(v2arr.get(0)) ? -1 : 1);
			
	}
}
	
