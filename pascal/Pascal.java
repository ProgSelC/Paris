package pascal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Pascal {
	public static final int LIMIT = 10;
	public static boolean debug = true;

	public static void main(String[] args) {
		System.out.println(evalTask("Паскаль.txt"));
	}

	public static String evalTask(String filename) {
		boolean beginRead = false;
		int spacesCount = 0;
		int lineCount = 0;

		String[] symb = new String[LIMIT];
		String message = "";

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String str = "";
			while ((str = br.readLine()) != null) {
				if (beginRead) {
					if (str.matches("[0-9]{10}\t.*")) {
		
						symb[lineCount] = str;
						lineCount++;

						if (lineCount == LIMIT) {
							message += convertToChar(symb);
							lineCount = 0;
							// break;
						}
					}
				}
				if (str.matches("\\s*")) {
					if (++spacesCount == 2) {
						beginRead = true;
					}
				} else {
					spacesCount = 0;
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading file!");
		}
		if (debug)
			System.out.println();

		return message;
	}

	public static char convertToChar(String[] symb) {
		char[][] a = new char[LIMIT][LIMIT];
		int result = 0;
		for (int j = 0; j < symb[0].split("\t").length; j++) {
			for (int i = 0; i < symb.length; i++) {
				a[i] = symb[i].split("\t")[j].toCharArray();
			}
			result = result * 10 + findMaxChain(a);
		}

		if (debug)
			System.out.print(result + "|");
		
		return (char) result;
	}

	public static int findMaxChain(char[][] a) {
		int maxLen = 0;
		char dg = '\u0000';
		int curLen = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 1; j < a.length; j++) {
				if (a[i][j] == a[i][j - 1]) {
					curLen++;
				} else {
					if (curLen > maxLen) {
						dg = a[i][j - 1];
						maxLen = curLen;
						curLen = 0;
					} else if (curLen == maxLen && a[i][j - 1] > dg) {
						dg = a[i][j - 1];
						curLen = 0;
					} else {
						curLen = 0;
					}
				}
			}
			curLen = 0;
		}
		for (int j = 0; j < a.length; j++) {
			for (int i = 1; i < a.length; i++) {
				if (a[i][j] == a[i - 1][j]) {
					curLen++;
				} else {
					if (curLen > maxLen) {
						dg = a[i - 1][j];
						maxLen = curLen;
						curLen = 0;
					} else if (curLen == maxLen && a[i - 1][j] > dg) {
						dg = a[i - 1][j];
						curLen = 0;
					} else {
						curLen = 0;
					}
				}
			}
			curLen = 0;
		}
		for (int i = 1; i < a.length; i++) {
			curLen = 0;
			if (a[i][i] == a[i - 1][i - 1]) {
				curLen++;
			} else {
				if (curLen > maxLen) {
					dg = a[i - 1][i - 1];
					maxLen = curLen;
					curLen = 0;
				} else if (curLen == maxLen && a[i - 1][i - 1] > dg) {
					dg = a[i - 1][i - 1];
					curLen = 0;
				} else {
					curLen = 0;
				}
			}
		}
		for (int i = 1; i < a.length; i++) {
			curLen = 0;
			if (a[i][a.length - (i + 1)] == a[i - 1][a.length - i]) {
				curLen++;
			} else {
				if (curLen > maxLen) {
					dg = a[i - 1][a.length - i];
					maxLen = curLen;
					curLen = 0;
				} else if (curLen == maxLen && a[i - 1][a.length - i] > dg) {
					dg = a[i - 1][a.length - i];
					curLen = 0;
				} else {
					curLen = 0;
				}
			}
		}
		return Integer.parseInt(dg + "");
	}

}
