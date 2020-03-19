package Common;

public class ObjectiveFun {
	public static final int Max = 0;
	public static final int Min = 1;
	/**
	 * 自变量取值范围
	 */
	private Range range;
	private int Direction = Max;

	public ObjectiveFun() {
		super();
		int dim = 2;
		double[] high = new double[dim];
		double[] low = new double[dim];
		for (int i = 0; i < dim; i++) {
			low[i] = -5;
			high[i] = 5;
		}
		this.range = new Range(dim, high, low);
	}

	public int getDirection() {
		return Direction;
	}

	public void setDirection(int direction) {
		Direction = direction;
	}

	public ObjectiveFun(Range range) {
		super();
		this.range = range;
	}

	/**
	 * 返回目标函数值
	 * 
	 * @param positionCode
	 * @return
	 */
	public double getObjValue(double[] positionCode) {
//		double son = Math.sin(Math.sqrt((positionCode[0] * positionCode[0] + positionCode[1] * positionCode[1])));
//		son = son * son - 0.5;
//		double mot = 1 + 0.001 * (positionCode[0] * positionCode[0] + positionCode[1] * positionCode[1]);
//		mot = mot * mot;
//		return son / mot - 0.5;
		// double value = Math.pow(positionCode[0] - 2, 2) + Math.pow(positionCode[1] -
		// 3, 2);
		double value = Math.pow(Math.E, -Math.pow(positionCode[0] - 4, 2) - Math.pow(positionCode[1] - 4, 2))
				+ Math.pow(Math.E, -Math.pow(positionCode[0] + 4, 2) - Math.pow(positionCode[1] - 4, 2))
				+ 2 * Math.pow(Math.E, -Math.pow(positionCode[0], 2) - Math.pow(positionCode[1] + 4, 2))
				+ 2 * Math.pow(Math.E, -Math.pow(positionCode[0], 2) - Math.pow(positionCode[1], 2));
		return value;

	}

	/**
	 * 返回目标函数的字符串表达，用于matlab画图
	 * 
	 * @return
	 */
	public String getObjStr() {
		return "exp(-(x-4)^2-(y-4)^2)+exp(-(x+4)^2-(y-4)^2)+2*exp(-x^2-(y+4)^2)+2*exp(-x^2-y^2)";
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}
}
