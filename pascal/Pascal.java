package pascal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class Pascal {
	public static final int LIMIT = 10;
	public static boolean debug = false;

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
		
		ArrayList<String> lines = new ArrayList<>();
		String tmpbuf = "";

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				tmpbuf += a[i][j];
			}
			lines.add(tmpbuf);
			tmpbuf = "";
		}

		for (int j = 0; j < a.length; j++) {
			for (int i = 0; i < a.length; i++) {
				tmpbuf += a[i][j];
			}
			lines.add(tmpbuf);
			tmpbuf = "";
		}

		for (int k = 0; k < a.length; k++) {
			for (int i = k, j = 0; k < a.length - 1 && i < a.length; i++, j++) {
				tmpbuf += a[i][j];
			}
			lines.add(tmpbuf);
			tmpbuf = "";

			for (int i = k, j = 0; k > 0 && i >= 0; i--, j++) {
				tmpbuf += a[i][j];
			}
			lines.add(tmpbuf);
			tmpbuf = "";
		}

		for (int k = 1; k < a.length - 1; k++) {
			for (int i = 0, j = k; j < a.length; i++, j++) {
				tmpbuf += a[i][j];
			}
			lines.add(tmpbuf);
			tmpbuf = "";

			for (int i = a.length - 1, j = k; j < a.length; i--, j++) {
				tmpbuf += a[i][j];
			}
			lines.add(tmpbuf);
			tmpbuf = "";
		}
		
		return evalSingle(lines);
	}

	public static int evalSingle(ArrayList<String> lines){
		TreeSet<Chain> chains = new TreeSet<>();
		char cPrev = '\u0000';
		int cQty = 0;
		for(String str: lines){
			for(char c: str.toCharArray()){
				if(cQty == 0){
					cPrev = c;
					cQty = 1;
				} else if(c == cPrev){
					cQty++;
				} else {
					if(cQty > 1){
						chains.add(new Chain(Integer.parseInt(cPrev+""),cQty));
					}
					cPrev = c;
					cQty = 1;
				}
			}
			if(cQty > 1){
				chains.add(new Chain(Integer.parseInt(cPrev+""),cQty));
			}
			cQty = 0;
		}
		return chains.first().getDigit();
	}
}


class Chain implements Comparable<Chain> {
	private int digit;
	private int qty;

	public Chain(int digit, int qty) {
		super();
		this.digit = digit;
		this.qty = qty;
	}

	public int getDigit() {
		return digit;
	}

	public int getQty() {
		return qty;
	}

	@Override
	public String toString() {
		return "Chain [digit=" + digit + ", qty=" + qty + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + digit;
		result = prime * result + qty;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chain other = (Chain) obj;
		if (digit != other.digit)
			return false;
		if (qty != other.qty)
			return false;
		return true;
	}

	@Override
	public int compareTo(Chain other) {
		if (other == null || this == null) {
			return -1000;
		} else {
			return (other.qty * 10 + other.digit)
					- (this.qty * 10 + this.digit);
		}
	}

}
