package ABC.domain;

import java.util.Arrays;

import FA.domain.Firefly;
import FA.domain.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * s食物源
 * 
 * @author hba
 *
 */
@SuppressWarnings("rawtypes")
@Setter
@Getter
public class FoodSource implements Comparable {
	/**
	 * 食物源位置
	 */
	private Position position;
	/**
	 * 食物数量
	 */
	private double foodAmount = -Double.MAX_VALUE;
	/**
	 * 尝试次数
	 */
	private int trials = 0;

	public FoodSource(Position position) {
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
		double tmp = this.foodAmount - ((FoodSource) (o)).getFoodAmount();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public String toString() {
		return "FoodSource [ " + Arrays.toString(position.getPositionCode()) + ", foodAmount=" + foodAmount + "]";
	}
}
