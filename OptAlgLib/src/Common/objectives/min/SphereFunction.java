package Common.objectives.min;

import java.util.Arrays;

import Common.ObjectiveFun;
import Common.Range;

/**
 * @see http://www.sfu.ca/~ssurjano/spheref.html
 * 
 *      The Sphere function has d local minima except for the global one. It is
 *      continuous, convex and unimodal.
 * 
 * @author hba
 *
 */
public class SphereFunction extends ObjectiveFun {

	public SphereFunction() {
		int dim = 10;
		double[] high = new double[dim];
		double[] low = new double[dim];
		for (int i = 0; i < dim; i++) {
			low[i] = -5.12;
			high[i] = 5.12;
		}
		this.range = new Range(dim, high, low);
		this.Direction = this.Min;
	}

	public SphereFunction(Range range) {
		super(range);
		this.range = range;
	}

	public double getObjValue(double[] positionCode) {
		double value = Arrays.stream(positionCode).map(d -> d * d).sum();
		return value;
	}
}
