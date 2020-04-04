package Util;

import java.util.stream.IntStream;

public class MathUtil {
	/**
	 * ¼ÆËã¾àÀë
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public static double calcDistance(double[] i, double[] j) {
		double disSum = IntStream.range(0, i.length).mapToDouble(d -> Math.pow(i[d] - j[d], 2)).sum();
		return Math.sqrt(disSum);
	}
}
