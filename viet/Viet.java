package viet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class Viet {
	public static final int LIMIT = 10;
	public static boolean debug = false;

	public static void main(String[] args) {
		System.out.println(evalTask("Француа Виет .txt"));
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
					if (str.matches("[OXSF]{10}\t.*")) {
						if (debug)
							System.out.println(str);

						symb[lineCount] = str;
						lineCount++;

						if (lineCount == LIMIT) {
							message += convertToChar(symb);
							lineCount = 0;
						}
					}
				}
				if (str.matches("\\s*")) {
					if (++spacesCount == 4) {
						beginRead = true;
					}
				} else {
					spacesCount = 0;
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading file!");
		}

		return message;
	}

	public static char convertToChar(String[] symb) {
		char[][] a = new char[LIMIT][LIMIT];
		int result = 0;
		for (int j = 0; j < symb[0].split("\t").length; j++) {
			for (int i = 0; i < symb.length; i++) {
				a[i] = symb[i].split("\t")[j].toCharArray();
			}
			result = result * 10 + findPath(a);
		}
		return (char) result;
	}

	public static int findPath(char[][] a) {
		Point startPt = null;
		TreeSet<Route> successRts = new TreeSet<>();
		Route tmpRt = new Route();
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				if (a[i][j] == 'S') {
					startPt = new Point(i, j);
				}
			}
		}
		testPath(a, startPt, tmpRt, successRts);
		if (debug)
			System.out.println(successRts);
		
		return (successRts.isEmpty()) ? 0 : successRts.first().length() - 1;
	}

	public static void testPath(char[][] a, Point pt, Route tmpRt,
			TreeSet<Route> successRts) {
		Route localRt = new Route();
		localRt.addAll(tmpRt);
		localRt.add(pt);

		if (a[pt.getX()][pt.getY()] == 'F') {
			successRts.add(localRt);
			return;
		} else if (!successRts.isEmpty() && successRts.first().length() == 1) {
			return;
		} else if (tmpRt.contains(pt)) {
			return;
		}

		if (localRt.length() < LIMIT) {
			if (pt.getX() + 1 < a.length
					&& (a[pt.getX() + 1][pt.getY()] != 'X')) {
				testPath(a, new Point(pt.getX() + 1, pt.getY()), localRt,
						successRts);
			}

			if (pt.getX() - 1 >= 0 && (a[pt.getX() - 1][pt.getY()] != 'X')) {
				testPath(a, new Point(pt.getX() - 1, pt.getY()), localRt,
						successRts);
			}

			if (pt.getY() + 1 < a.length
					&& (a[pt.getX()][pt.getY() + 1] != 'X')) {
				testPath(a, new Point(pt.getX(), pt.getY() + 1), localRt,
						successRts);
			}

			if (pt.getY() - 1 >= 0 && (a[pt.getX()][pt.getY() - 1] != 'X')) {
				testPath(a, new Point(pt.getX(), pt.getY() - 1), localRt,
						successRts);
			}
		}
	}
}

class Route implements Comparable<Route> {
	private ArrayList<Point> pl = new ArrayList<>();

	public Route() {
		super();
	}

	public void add(Point p) {
		pl.add(p);
	}

	public void addAll(Route rt) {
		pl.addAll(rt.getList());
	}

	public boolean contains(Point p) {
		return pl.contains(p);
	}

	public ArrayList<Point> getList() {
		return pl;
	}

	public int length() {
		return pl.size();
	}

	@Override
	public String toString() {
		String result = "";
		for (Point p : pl) {
			result += p + "-";
		}
		return result;
	}

	@Override
	public int compareTo(Route other) {
		if (this == null || other == null) {
			return 10;
		} else {
			return this.pl.size() - other.pl.size();
		}
	}
}

class Point {
	private int x;
	private int y;

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
