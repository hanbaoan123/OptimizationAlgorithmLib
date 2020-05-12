package FA.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import Common.ObjectiveFun;
import Common.Position;
import FA.domain.Firefly;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 标准FA算法
 * 
 * @author hba
 *
 */
@AllArgsConstructor
@Builder
@Setter
@Getter
public class FANormal implements FireflyAlgorithm {
	/**
	 * 萤火虫的种群表示
	 */
	protected List<Firefly> fireflies;
	/**
	 * 最大代数
	 */
	private int maxGen;
	/**
	 * 种群大小
	 */
	private int popNum;
	/**
	 * 维度
	 */
	private int dim;
	/**
	 * 吸引度beta
	 */
	@Builder.Default
	private double initAttraction = 1.0;
	/**
	 * 介质对光的吸收系数
	 */
	@Builder.Default
	private double gamma = 1.0;
	/**
	 * 扰动的步长因子
	 */
	@Builder.Default
	private double alpha = 0.2;
	/**
	 * 目标函数
	 */
	private ObjectiveFun objectiveFun;
	/**
	 * 自适应步长alpha
	 */
	@Builder.Default
	private boolean isAdaptive = false;
	/**
	 * 步长alpha衰减因子delta
	 */
	@Builder.Default
	private double delta = 0.97;
	/**
	 * 迭代计数器
	 */
	@Builder.Default
	private int iterator = 0;
	/**
	 * 随机数
	 */
	@Builder.Default
	private Random random = new Random();
	/**
	 * 是否使用matlab绘图
	 */
	@Builder.Default
	private boolean isDraw = true;

	/**
	 * 
	 * @param maxGen 最大迭代次数
	 * @param popNum 种群数量
	 * @param dim    维度
	 */
	public FANormal(int maxGen, int popNum, int dim) {

	}

	public void incrementIter() {
		iterator++;
		if (isAdaptive) {
			alpha *= delta;
		}
	}

	@Override
	public void initPop() {
		// TODO Auto-generated method stub
		System.out.println("**********种群初始化**********");
		fireflies = new ArrayList<>();
		for (int i = 0; i < popNum; i++) {
			fireflies.add(new Firefly(new Position(this.dim, objectiveFun.getRange())));
		}
	}

	@Override
	public double calcDistance(double[] a, double[] b) {
		// TODO Auto-generated method stub
		assert a.length == b.length;
		double distance = 0;
		int n = a.length;
		distance = IntStream.range(0, n).mapToDouble(i -> (a[i] - b[i]) * (a[i] - b[i])).sum();
		return Math.sqrt(distance);
	}

	@Override
	public void fireflyMove() {
		// TODO Auto-generated method stub
		for (int i = 0; i < popNum; i++) {
			for (int j = 0; j < popNum; j++) {
				Firefly fireflyi = fireflies.get(i);
				Firefly fireflyj = fireflies.get(j);
				if (i != j && fireflyj.getLight() > fireflyi.getLight()) { // 当萤火虫j的亮度大于萤火虫i的亮度时
					double[] codei = fireflyi.getPosition().getPositionCode();
					double[] codej = fireflyj.getPosition().getPositionCode();
					// 计算萤火虫之间的距离
					double disij = calcDistance(codei, codej);
					// 计算吸引度beta
					double attraction = initAttraction * Math.pow(Math.E, -gamma * disij * disij); // 计算萤火虫j对萤火虫i的吸引度
					double[] scale = fireflyi.getPosition().getRange().getScale();
					double[] newPositionCode = IntStream.range(0, this.dim).mapToDouble(ind -> codei[ind]
							+ attraction * (codej[ind] - codei[ind]) + alpha * (random.nextDouble() - 0.5) * scale[ind])
							.toArray();
					fireflyi.getPosition().setPositionCode(newPositionCode);
					// 立即更新
					// fireflyi.setLight(this.getObjectiveFun().getObjValue(newPositionCode));
				}
			}
		}
		// 对最亮的萤火虫进行随机移动
		Firefly bestFirefly = fireflies.get(popNum - 1);
		double[] scale = bestFirefly.getPosition().getRange().getScale();
		double[] newPositionCode = IntStream.range(0, dim).mapToDouble(
				i -> bestFirefly.getPosition().getPositionCode()[i] + alpha * (random.nextDouble() - 0.5) * scale[i])
				.toArray();
		bestFirefly.getPosition().setPositionCode(newPositionCode);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void calcuLight() {
		// TODO Auto-generated method stub
		for (Firefly firefly : fireflies) {
			firefly.setLight(this.getObjectiveFun().getObjValue((firefly.getPosition().getPositionCode())));
		}
		Collections.sort(fireflies);
		// 展示萤火虫分布
	}

	public void printFirflies() {
		for (Firefly firefly : fireflies) {
			System.out.println(firefly);
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		initPop();
		while (this.iterator < maxGen) {
			calcuLight();
			fireflyMove();
			incrementIter();
			System.out.println("**********第" + iterator + "代最优解：" + fireflies.get(popNum - 1) + "**********");
		}
		System.out.println("------------------------------------");
		// printFirflies();
	}
}
