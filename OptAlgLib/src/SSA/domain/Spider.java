package SSA.domain;

import java.util.Arrays;

import Common.Position;
import FA.domain.Firefly;
import lombok.Getter;
import lombok.Setter;

/**
 * 蜘蛛
 * 
 * @author hba
 *
 */
@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Spider implements Comparable {
	/**
	 * 蜘蛛在蜘蛛网上的位置
	 */
	private Position position;
	/**
	 * 适应度值
	 */
	private double fitness;
	/**
	 * 目标振动
	 */
	private double targetVibration = 0;
	/**
	 * 目标位置
	 */
	private double[] targetPosition;
	/**
	 * 振动
	 */
	private double vibration;
	/**
	 * 上一次目标振动后的迭代次数
	 */
	private int cs;
	/**
	 * 上一次的移动量
	 */
	private double[] movenment;
	/**
	 * 引导移动的维度掩码
	 */
	private int[] mask;

	public Spider(Position position) {
		super();
		this.position = position;
		this.targetPosition = position.getPositionCode();
		this.mask = new int[position.getDimension()];
		this.movenment = new double[position.getDimension()];
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return 0;
		}
		double tmp = this.fitness - ((Spider) (o)).getFitness();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public String toString() {
		return "Spider [ " + Arrays.toString(position.getPositionCode()) + ", fitness=" + fitness + "]";
	}
}
