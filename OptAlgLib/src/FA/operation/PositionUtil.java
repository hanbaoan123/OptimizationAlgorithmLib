package FA.operation;

import java.util.stream.IntStream;


public class PositionUtil implements Cloneable {
	public static double[] sub(double[] positiona, double[] positionb) {
		double[] position = new double[positiona.length];
		position = IntStream.range(0, positiona.length).mapToDouble(i -> positiona[i] - positionb[i]).toArray();
		return position;
	}

	public static double[] add(double[] positiona, double[] positionb) {
		double[] position = new double[positiona.length];
		position = IntStream.range(0, positiona.length).mapToDouble(i -> positiona[i] + positionb[i]).toArray();
		return position;
	}

	public static double[] multi(double[] positiona, double multiplier) {
		double[] position = new double[positiona.length];
		position = IntStream.range(0, positiona.length).mapToDouble(i -> positiona[i] * multiplier).toArray();
		return position;
	}
}
