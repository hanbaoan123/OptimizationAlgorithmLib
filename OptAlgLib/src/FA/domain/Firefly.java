package FA.domain;


import java.util.Arrays;
import java.util.stream.IntStream;

import Common.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * 萤火虫实体类
 * 
 * @author hba
 *
 */
@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Firefly implements Comparable {

	private double light; // 萤火虫的亮度
	private double maxAttraction; // 最大吸引度，每次移动后置零
	private Position position;
	private double[] moveDirection;

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return 0;
		}
		double tmp = this.light - ((Firefly) (o)).getLight();
		if (tmp < 0)
			return -1;
		if (tmp > 0)
			return 1;
		return 0;
	}

	public Firefly(Position position) {
		super();
		this.position = position;
	}

	public void move() {
		if (maxAttraction > 0) {
			double[] newPositionCOde = IntStream.range(0, position.getDimension())
					.mapToDouble(i -> position.getPositionCode()[i] + moveDirection[i]).toArray();
			this.position.setPositionCode(newPositionCOde);
			maxAttraction = 0;
		}
	}

	public String toString() {
		return "Firefly [ " + Arrays.toString(position.getPositionCode()) + ", light=" + light + "]";
	}
}
