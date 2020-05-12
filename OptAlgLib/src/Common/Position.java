package Common;

import java.util.stream.IntStream;

public class Position {
	/**
	 * 维度
	 */
	private int dimension;
	/**
	 * 位置编码
	 */
	private double[] positionCode;
	/**
	 * 位置取值范围
	 */
	private Range range;

	public Position(int dimension, Range range) {
		super();
		this.dimension = dimension;
		this.range = range;
		positionCode = new double[this.dimension];
		if (range != null) {
			for (int i = 0; i < dimension; i++) {
				positionCode[i] = range.getLow()[i] + (range.getHigh()[i] - range.getLow()[i]) * Math.random();
			}
		}
	}

	public Position(int dimension, double high, double low) {
		this.dimension = dimension;
		positionCode = new double[dimension];
		for (int i = 0; i < dimension; i++) {
			positionCode[i] = low + (high - low) * Math.random();
		}
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public double[] getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(double[] positionCode) {
		this.positionCode = positionCode;
		if (this.range != null) {
			this.positionCode = IntStream.range(0, positionCode.length).mapToDouble(i -> {
				if (positionCode[i] > range.getHigh()[i]) {
					this.positionCode[i] = range.getHigh()[i];
				}
				if (positionCode[i] < range.getLow()[i]) {
					this.positionCode[i] = range.getLow()[i];
				}
				return this.positionCode[i];
			}).toArray();
		}
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

}
