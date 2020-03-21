/**
 * 
 */
package AFSA.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import ABC.domain.FoodSource;
import AFSA.domain.Fish;
import Common.ObjectiveFun;
import FA.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hba 人工鱼群算法
 */
@Builder
@AllArgsConstructor
@Setter
@Getter
public class AFSANormal implements AFSA {
	/**
	 * 食物源
	 */
	protected List<Fish> fishes;
	/**
	 * 最大代数
	 */
	protected int maxGen;
	/**
	 * 鱼数
	 */
	protected int fishAmount;
	/**
	 * 尝试次数
	 */
	protected int trials = 0;
	/**
	 * 拥挤因子
	 */
	protected double delta;
	/**
	 * 视距
	 */
	protected double visual;
	/**
	 * 步长
	 */
	protected double step;
	/**
	 * 维度
	 */
	protected int dim;
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
	 * 目标函数
	 */
	protected ObjectiveFun objectiveFun;

	@Override
	public void initPop() {
		// TODO Auto-generated method stub
		System.out.println("**********种群初始化**********");
		fishes = new ArrayList<>();
		for (int i = 0; i < fishAmount; i++) {
			fishes.add(new Fish(new Position(this.dim, objectiveFun.getRange())));
		}
		calcFoodAmount();
	}

	private void calcFoodAmount() {
		// TODO Auto-generated method stub
		for (Fish fish : fishes) {
			fish.setFoodAmount(this.objectiveFun.getObjValue(fish.getPosition().getPositionCode()));
		}
		Collections.sort(fishes);
	}

	public void incrementIter() {
		iterator++;
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		Fish bestFish = new Fish(new Position(this.dim, this.objectiveFun.getRange()));
		if (this.random == null) {
			this.random = new Random();
		}
		initPop();
		bestFish = fishes.get(fishes.size() - 1);
		while (this.iterator < maxGen) {
			for (Fish fish : fishes) {
				// 群聚
				Fish swarmFish = AF_Swarm(fish);
				// 追随
				Fish followFish = AF_Follow(fish);
				// 觅食
				Fish preyFish = AF_Prey(fish);
				// 最优
				if (swarmFish.getFoodAmount() > bestFish.getFoodAmount()) {
					bestFish = swarmFish;
				}
				if (followFish.getFoodAmount() > bestFish.getFoodAmount()) {
					bestFish = followFish;
				}
				if (preyFish.getFoodAmount() > bestFish.getFoodAmount()) {
					bestFish = preyFish;
				}
			}
			incrementIter();
			System.out.println("**********第" + iterator + "代最优解：" + bestFish + "**********");
		}
	}

	@Override
	public Fish AF_Prey(Fish fish) {
		// TODO Auto-generated method stub
		Fish preyFish = new Fish(new Position(this.dim, fish.getPosition().getRange()));
		// 尝试次数
		for (int t = 0; t < this.trials; t++) {
			// 首先按照视距随机产生一个位置
			double[] newPositionCode = IntStream.range(0, this.dim)
					.mapToDouble(i -> fish.getPosition().getPositionCode()[i] + random.nextDouble() * this.visual)
					.toArray();
			preyFish.getPosition().setPositionCode(newPositionCode);
			double newFoodAmount = this.objectiveFun.getObjValue(preyFish.getPosition().getPositionCode());
			preyFish.setFoodAmount(newFoodAmount);
			// 如果随机产生位置有更多的食物，则向该位置移动
			if (newFoodAmount > fish.getFoodAmount()) {
				return preyFish;
			}
		}
		// 尝试一定次数后，按照步长随机选择位置
		double[] newPositionCode = IntStream.range(0, this.dim)
				.mapToDouble(i -> fish.getPosition().getPositionCode()[i] + random.nextDouble() * this.step).toArray();
		preyFish.getPosition().setPositionCode(newPositionCode);
		double newFoodAmount = this.objectiveFun.getObjValue(preyFish.getPosition().getPositionCode());
		preyFish.setFoodAmount(newFoodAmount);
		return preyFish;
	}

	@Override
	public Fish AF_Follow(Fish fish) {
		// TODO Auto-generated method stub
		Fish followFish = new Fish(new Position(this.dim, fish.getPosition().getRange()));
		double maxFoodAmount = Double.NEGATIVE_INFINITY;
		// 最大位置
		Position maxpc = new Position(dim, null);
		// 最大鱼
		Fish maxFish;
		Fish maxFish_ = null;
		double nf;
		double nf_ = 0;
		for (Fish fishj : fishes) {
			if (fishj == fish) {
				continue;
			}
			if (calcDistance(fish, fishj) < visual && fishj.getFoodAmount() > maxFoodAmount) {
				maxFish_ = fishj;
				maxFoodAmount = fishj.getFoodAmount();
				maxpc.setPositionCode(fishj.getPosition().getPositionCode());
			}
		}
		// 当前fish视距内最优fish的邻域
		for (Fish fishj : fishes) {
			if (fishj == maxFish_) {
				continue;
			}
			if (calcDistance(maxFish_, fishj) < visual) {
				nf_++;
			}
		}
		nf = nf_;

		// 视距内没有鱼
		if (maxFish_ == null) {
			maxFish = fish;
		} else {
			maxFish = maxFish_;
		}
		// 不太拥挤且最优处食物更多
		if (nf / this.fishAmount < this.delta && maxFish.getFoodAmount() > fish.getFoodAmount()) {
			double[] newPositionCode = IntStream.range(0, this.dim)
					.mapToDouble(i -> fish.getPosition().getPositionCode()[i]
							+ (maxFish.getPosition().getPositionCode()[i] - fish.getPosition().getPositionCode()[i])
									/ calcDistance(maxFish, fish) * step * random.nextDouble())
					.toArray();
			followFish.getPosition().setPositionCode(newPositionCode);
			// 注意一定从鱼中重新取位置，因为给鱼的位置赋值后会检查边界
			followFish.setFoodAmount(this.objectiveFun.getObjValue(followFish.getPosition().getPositionCode()));

		} else {
			return AF_Prey(fish);
		}
		return followFish;
	}

	@Override
	public Fish AF_Swarm(Fish fish) {
		// TODO Auto-generated method stub
		Fish swarmFish = new Fish(new Position(this.dim, fish.getPosition().getRange()));
		// 视距内鱼的数量
		double nf;
		double nf_ = 0;
		// 中心位置
		Position xc = new Position(dim, null);
		for (Fish fishj : fishes) {
			if (fishj == fish) {
				continue;
			}
			if (calcDistance(fish, fishj) < visual) {
				nf_++;
				double[] xcSum = IntStream.range(0, this.dim)
						.mapToDouble(i -> xc.getPositionCode()[i] + fishj.getPosition().getPositionCode()[i]).toArray();
				xc.setPositionCode(xcSum);
			}
		}
		nf = nf_;
		double[] xcCode = nf_ == 0 ? fish.getPosition().getPositionCode()
				: IntStream.range(0, this.dim).mapToDouble(i -> xc.getPositionCode()[i] / nf).toArray();
		double cxFoodAmount = this.getObjectiveFun().getObjValue(xcCode);
		// 不太拥挤且中心食物更多
		if (nf / this.fishAmount < this.delta && cxFoodAmount > fish.getFoodAmount()) {
			double[] newPositionCode = IntStream.range(0, this.dim).mapToDouble(
					i -> fish.getPosition().getPositionCode()[i] + (xcCode[i] - fish.getPosition().getPositionCode()[i])
							/ calcDistance(xcCode, fish.getPosition().getPositionCode()) * step * random.nextDouble())
					.toArray();
			swarmFish.getPosition().setPositionCode(newPositionCode);
			// 注意一定从鱼中重新取位置，因为给鱼的位置赋值后会检查边界
			swarmFish.setFoodAmount(this.objectiveFun.getObjValue(swarmFish.getPosition().getPositionCode()));

		} else {
			return AF_Prey(fish);
		}

		return swarmFish;
	}

	private double calcDistance(Fish fishi, Fish fishj) {
		// TODO Auto-generated method stub
		if (fishi == null || fishj == null) {
			return Double.POSITIVE_INFINITY;
		}
		return calcDistance(fishi.getPosition().getPositionCode(), fishj.getPosition().getPositionCode());
	}

	private double calcDistance(double[] positionCodei, double[] positionCodej) {
		// TODO Auto-generated method stub
		double sumDis = IntStream.range(0, positionCodei.length)
				.mapToDouble(i -> Math.pow(positionCodei[i] - positionCodej[i], 2)).sum();
		return Math.sqrt(sumDis);
	}

}
