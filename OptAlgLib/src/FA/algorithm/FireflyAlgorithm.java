package FA.algorithm;

import java.awt.font.NumericShaper.Range;

/**
 * 萤火虫算法接口
 * 
 * @author hba
 *
 */
public interface FireflyAlgorithm {
	/**
	 * 种群初始化
	 */
	public void initPop();

	/**
	 * 计算亮度
	 */
	public void calcuLight();

	/**
	 * 计算欧氏距离
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public double calcDistance(double[] a, double[] b);

	/**
	 * 萤火虫移动
	 */
	public void fireflyMove();

	/**
	 * 算法运行
	 * 
	 */
	public void start();

}
