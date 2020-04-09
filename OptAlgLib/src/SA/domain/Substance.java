package SA.domain;

import java.util.Arrays;

import FA.domain.Position;
import lombok.Getter;
import lombok.Setter;

/**
 * 物质
 * 
 * @author hba
 *
 */
@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Substance {
	/**
	 * 原子位置
	 */
	private Position position;
	/**
	 * 物质能量
	 */
	private double energy;

	public Substance(Position position) {
		super();
		this.position = position;
	}

	public String toString() {
		return "Substance [ " + Arrays.toString(position.getPositionCode()) + ", energy=" + energy + "]";
	}
}
