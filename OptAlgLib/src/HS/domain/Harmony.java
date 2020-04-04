package HS.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;

/**
 * 和声
 * 
 * @author hba
 *
 */
@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Harmony implements Comparable {
	/**
	 * 通过审美评价给出的品质，即目标函数
	 */
	private double performence;

	/**
	 * 音符的合并
	 */
	private Composition composition;

	public Harmony(Composition composition) {
		super();
		this.composition = composition;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return 0;
		}
		// 从小到大
		double tmp = this.performence - ((Harmony) (o)).getPerformence();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public String toString() {
		return "Harmony [ " + Arrays.toString(composition.getPitches()) + ", performence=" + performence + "]";
	}
}
