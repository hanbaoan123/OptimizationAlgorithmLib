package HS.domain;

import java.util.stream.IntStream;

import Common.Range;

/**
 * 音符的合并
 * 
 * @author hba
 *
 */
public class Composition {
	/**
	 * 乐器数量，即维度数
	 */
	private int numOfInstruments;
	/**
	 * 每个乐器的音符
	 */
	private double[] pitches;
	/**
	 * 音符取值范围
	 */
	private Range range;

	public Composition(int numOfInstruments, Range range) {
		super();
		this.numOfInstruments = numOfInstruments;
		this.range = range;
		pitches = new double[this.numOfInstruments];
		if (range != null) {
			for (int i = 0; i < numOfInstruments; i++) {
				pitches[i] = range.getLow()[i] + (range.getHigh()[i] - range.getLow()[i]) * Math.random();
			}
		}
	}

	public Composition(int numOfInstruments, double high, double low) {
		this.numOfInstruments = numOfInstruments;
		pitches = new double[numOfInstruments];
		for (int i = 0; i < numOfInstruments; i++) {
			pitches[i] = low + (high - low) * Math.random();
		}
	}

	public void setPitches(double[] pitches) {
		this.pitches = pitches;
		if (this.range != null) {
			this.pitches = IntStream.range(0, pitches.length).mapToDouble(i -> {
				if (pitches[i] > range.getHigh()[i]) {
					this.pitches[i] = range.getHigh()[i];
				}
				if (pitches[i] < range.getLow()[i]) {
					this.pitches[i] = range.getLow()[i];
				}
				return this.pitches[i];
			}).toArray();
		}
	}

	public int getNumOfInstruments() {
		return numOfInstruments;
	}

	public void setNumOfInstruments(int numOfInstruments) {
		this.numOfInstruments = numOfInstruments;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	public double[] getPitches() {
		return pitches;
	}
}
