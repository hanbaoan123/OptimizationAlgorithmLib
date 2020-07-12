package BA.domain;

import java.util.Arrays;

import AFSA.domain.Fish;
import Common.Position;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Bat implements Comparable {
	/**
	 * 蝙蝠位置
	 */
	private Position position;
	/**
	 * 速度
	 */
	private double[] velocity;
	/**
	 * 适应度
	 */
	private double fitness = Double.MAX_VALUE;

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return 0;
		}
		// 从小到大
		double tmp = this.fitness - ((Bat) (o)).getFitness();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public String toString() {
		return "Pisition [ " + Arrays.toString(position.getPositionCode()) + ", fitness=" + fitness + "]";
	}

	public Bat(Position position) {
		super();
		this.position = position;
		this.velocity = new double[position.getDimension()];
	}
}
