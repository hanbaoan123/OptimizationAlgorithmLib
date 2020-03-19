package ES.domain;

import java.util.stream.IntStream;

import Common.Range;

public class Code implements Cloneable {
	/**
	 * 维度
	 */
	private int dimension;
	/**
	 * 编码
	 */
	private double[] code;
	/**
	 * 每个维度上的变异强度
	 */
	private double[] mutStrength;

	public double[] getMutStrength() {
		return mutStrength;
	}

	public void setMutStrength(double[] mutStrength) {
		this.mutStrength = mutStrength;
	}

	/**
	 * 位置取值范围
	 */
	private Range range;

	@Override
	public Code clone() throws CloneNotSupportedException {
		return (Code) super.clone();
	}

	public Code(int dimension, Range range) {
		super();
		this.dimension = dimension;
		this.range = range;
		code = new double[this.dimension];
		this.mutStrength = new double[this.dimension];
		for (int i = 0; i < dimension; i++) {
			code[i] = range.getLow()[i] + (range.getHigh()[i] - range.getLow()[i]) * Math.random();
		}
	}

	public Code(int dimension, double high, double low) {
		this.dimension = dimension;
		code = new double[dimension];
		this.mutStrength = new double[this.dimension];
		for (int i = 0; i < dimension; i++) {
			code[i] = low + (high - low) * Math.random();
		}
	}

	public Code(int dimension, Range range, double mutStrength) {
		// TODO Auto-generated constructor stub
		super();
		this.dimension = dimension;
		this.range = range;
		code = new double[this.dimension];
		this.mutStrength = new double[this.dimension];
		for (int i = 0; i < dimension; i++) {
			code[i] = range.getLow()[i] + (range.getHigh()[i] - range.getLow()[i]) * Math.random();
			this.mutStrength[i] = mutStrength;
		}

	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public double[] getCode() {
		return code;
	}

	public void setCode(double[] code) {
		this.code = code;
		if (this.range != null) {
			this.code = IntStream.range(0, code.length).mapToDouble(i -> {
				if (code[i] > range.getHigh()[i]) {
					this.code[i] = range.getHigh()[i];
				}
				if (code[i] < range.getLow()[i]) {
					this.code[i] = range.getLow()[i];
				}
				return this.code[i];
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
