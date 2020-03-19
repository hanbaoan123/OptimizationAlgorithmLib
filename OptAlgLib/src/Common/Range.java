package Common;

import java.util.stream.IntStream;

import lombok.Value;

/**
 * 自变量取值范围
 * 
 * @author hba
 *
 */

public class Range {
	/**
	 * 维度
	 */
	private int dim;
	/**
	 * 取值上限
	 */
	private double[] high;
	/**
	 * 取值下限
	 */
	private double[] low;
	/**
	 * 取值范围
	 */
	private double[] scale;

	public Range(int dim, double[] high, double[] low) {
		super();
		this.dim = dim;
		this.high = high;
		this.low = low;
	}

	public int getDim() {
		return dim;
	}

	public void setDim(int dim) {
		this.dim = dim;
	}

	public double[] getHigh() {
		return high;
	}

	public void setHigh(double[] high) {
		this.high = high;
	}

	public double[] getLow() {
		return low;
	}

	public void setLow(double[] low) {
		this.low = low;
	}

	public double[] getScale() {
		this.scale = IntStream.range(0, high.length).mapToDouble(i -> high[i] - low[i]).toArray();
		return scale;
	}

	public void setScale(double[] scale) {
		this.scale = scale;
	}
}
