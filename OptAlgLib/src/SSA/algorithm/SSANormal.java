package SSA.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import AFSA.domain.Fish;
import Common.ObjectiveFun;
import Common.Position;
import SSA.domain.Spider;
import Util.MathUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 社会蜘蛛算法(Social Spider Algorithm ，SSA)
 * 
 * @author hba
 *
 */
@Builder
@AllArgsConstructor
@Setter
@Getter
public class SSANormal implements SSAIface {
	/**
	 * 社会蜘蛛群
	 */
	protected List<Spider> spiders;
	/**
	 * 最大代数
	 */
	protected int maxGen;
	/**
	 * 维度
	 */
	protected int dim;
	/**
	 * 振动衰减率
	 */
	protected double ra;
	/**
	 * 掩码mask更改几率
	 */
	protected double pc;
	/**
	 * 掩码为1的概率
	 */
	protected double pm;
	/**
	 * 蜘蛛个数
	 */
	protected int spiderAmount;
	/**
	 * 迭代计数器
	 */
	@Builder.Default
	protected int iterator = 0;
	/**
	 * 随机数
	 */
	@Builder.Default
	protected Random random = new Random();
	/**
	 * 最优蜘蛛
	 */
	@Builder.Default
	protected Spider bestSpider;
	/**
	 * 目标函数
	 */
	protected ObjectiveFun objectiveFun;

	public void incrementIter() {
		iterator++;
	}

	@Override
	public void initPop() {
		// TODO Auto-generated method stub
		System.out.println("**********种群初始化**********");
		spiders = new ArrayList<>();
		for (int i = 0; i < spiderAmount; i++) {
			spiders.add(new Spider(new Position(this.dim, objectiveFun.getRange())));
		}
	}

	private void calcFitnessAndVibration() {
		// TODO Auto-generated method stub
		for (Spider spider : spiders) {
			spider.setFitness(this.objectiveFun.getObjValue(spider.getPosition().getPositionCode()));
			double I = Math.log(1 + 1.0 / (spider.getFitness() + Math.pow(10, -100)/* C */));
			spider.setVibration(I);
		}
		Collections.sort(spiders);
		if (spiders.get(0).getFitness() < bestSpider.getFitness()) {
			bestSpider.setFitness(spiders.get(0).getFitness());
			bestSpider.getPosition().setPositionCode(spiders.get(0).getPosition().getPositionCode());
		}
	}

	@Override
	public void start() {
		bestSpider = new Spider(new Position(this.dim, this.objectiveFun.getRange()));
		bestSpider.setFitness(Double.MAX_VALUE);
		initPop();
		while (this.iterator < maxGen) {
			// 计算适应度值和振动
			calcFitnessAndVibration();
			// 各个维度标准差的平均值
			double[] averageArray = IntStream.range(0, this.dim).mapToDouble(dim -> {
				return spiders.stream().mapToDouble(spider -> spider.getPosition().getPositionCode()[dim]).average()
						.getAsDouble();
			}).toArray();

			double meanDeviation = IntStream.range(0, this.dim).mapToDouble(dim -> {
				return Math.pow(spiders.stream()
						.mapToDouble(
								spider -> Math.pow(spider.getPosition().getPositionCode()[dim] - averageArray[dim], 2))
						.sum() / this.spiderAmount, 0.5);
			}).average().getAsDouble();

			for (Spider spidera : spiders) {
				// 计算传播后的振动V
				List<Double> V = new ArrayList<Double>();
				for (Spider spiderb : spiders) {
					double distance = 0;
					if (spidera != spiderb) {
						distance = MathUtil.calcManhattanDistance(spidera.getPosition().getPositionCode(),
								spiderb.getPosition().getPositionCode());
					}
					double newIntensity = spiderb.getVibration() * Math.exp(-distance / (meanDeviation * ra));
					V.add(newIntensity);
				}
				// 从V中选择最大的振动
				double vbest = V.stream().mapToDouble(v -> v).max().getAsDouble();
				int bestInd = V.indexOf(vbest);
				// 重新设置目标振动
				if (vbest > spidera.getTargetVibration()) {
					spidera.setTargetVibration(vbest);
					spidera.setTargetPosition(spiders.get(bestInd).getPosition().getPositionCode());
					// 重置Cs
					spidera.setCs(0);
				} else {
					// 更新Cs
					spidera.setCs(spidera.getCs() + 1);
				}
				double r = random.nextDouble();
				// 很多代cs没有更新时，就有更大可能更新掩码
				if (r > Math.pow(this.pc, spidera.getCs())) {
					// 如果更改掩码，则以pm的概率变为1。
					// 对于全部为1或为0时，随机选择某一维度进行更改
//					long zeroLen = Arrays.stream(spidera.getMask()).filter(i -> i == 0).count();
//					long oneLen = Arrays.stream(spidera.getMask()).filter(i -> i == 1).count();
//					// 全为1
//					if (zeroLen == 0) {
//						spidera.getMask()[random.nextInt(this.dim)] = 0;
//					}
//					// 全为0
//					if (oneLen == 0) {
//						spidera.getMask()[random.nextInt(this.dim)] = 1;
//					}

					for (int i = 0; i < this.dim; i++) {
						double rr = random.nextDouble();
						// 以概率pm设置为1
						if (rr <= this.pm) {
							spidera.getMask()[i] = 1;
						} else {
							spidera.getMask()[i] = 0;
						}
					}
				}
				// 计算pfollow
				double[] pfollow = new double[this.dim];
				for (int maskInd = 0; maskInd < spidera.getMask().length; maskInd++) {
					// 从目标位置中选择
					if (spidera.getMask()[maskInd] == 0) {
						pfollow[maskInd] = spidera.getTargetPosition()[maskInd];
					} else {
						// 从随机选择的蜘蛛中选择
						pfollow[maskInd] = spiders.get(random.nextInt(this.spiderAmount)).getPosition()
								.getPositionCode()[maskInd];
					}
				}
				// 随机行走，计算下一次迭代的位置
				double[] currPos = spidera.getPosition().getPositionCode();
				double[] movement = IntStream.range(0, this.dim)
						.mapToDouble(dim -> spidera.getMovenment()[dim] * random.nextDouble()
								+ (pfollow[dim] - currPos[dim]) * random.nextDouble())
						.toArray();
				double[] newPos = IntStream.range(0, this.dim).mapToDouble(dim -> currPos[dim] + movement[dim])
						.toArray();
				spidera.getPosition().setPositionCode(newPos);
				spidera.setMovenment(movement);
			}
			System.out.println("**********第" + iterator + "代最优解：" + bestSpider + "**********");
			incrementIter();
		}
	}
}
