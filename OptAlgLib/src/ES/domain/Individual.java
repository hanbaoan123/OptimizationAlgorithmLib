package ES.domain;

import java.util.Arrays;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Individual implements Cloneable, Comparable {
	/**
	 * 适应度值
	 */
	private double fitness;
	private Code code;
	/**
	 * 每个个体的变异强度
	 */
	private double mutStrength;
	private Random random = new Random();

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return 0;
		}
		double tmp = this.fitness - ((Individual) (o)).getFitness();
		// 从大到小排列
		if (tmp < 0)
			return 1;
		if (tmp > 0)
			return -1;
		return 0;
	}

	@Override
	public Individual clone() throws CloneNotSupportedException {
		Individual individual = (Individual) super.clone();
		individual.setCode(code.clone());
		return individual;
	}

	public Individual(Code code) {
		super();
		this.code = code;
	}

	public String toString() {
		return "Individual [ " + Arrays.toString(code.getCode()) + ", fitness=" + fitness + "]";
	}
}
