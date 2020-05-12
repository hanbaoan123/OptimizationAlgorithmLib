package AFSA.domain;

import java.util.Arrays;

import Common.Position;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Fish implements Comparable {
	/**
	 * 食物源位置
	 */
	private Position position;
	/**
	 * 食物数量
	 */
	private double foodAmount = -Double.MAX_VALUE;
	/**
	 * 视距
	 */
	private double visual;

	public Fish(Position position) {
		super();
		this.position = position;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return 0;
		}
		// 从小到大
		double tmp = this.foodAmount - ((Fish) (o)).getFoodAmount();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public String toString() {
		return "Pisition [ " + Arrays.toString(position.getPositionCode()) + ", foodAmount=" + foodAmount + "]";
	}

}
