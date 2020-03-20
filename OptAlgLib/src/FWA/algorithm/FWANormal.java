package FWA.algorithm;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import ABC.domain.FoodSource;
import Common.ObjectiveFun;
import FA.domain.Position;
import FWA.domian.Firework;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class FWANormal implements FWAIface {
	/**
	 * 烟花种群
	 */
	protected List<Firework> fireworks;
	/**
	 * 爆炸火花种群
	 */
	protected List<Firework> explosionSparks;
	/**
	 * 高斯变异火花
	 */
	protected List<Firework> gaussianSparks;
	/**
	 * 烟花个数
	 */
	protected int fireworkNum;
	/**
	 * 爆炸半径A
	 */
	protected double explosionRadiusA;
	/**
	 * 爆炸火花数量M
	 */
	protected int explosionSparkNumberM;
	/**
	 * 高斯变异火花数量M
	 */
	protected int gaussianSparkNumberM;
	/**
	 * 迭代计数器
	 */
	@Builder.Default
	protected int iterator = 0;
	/**
	 * 爆炸火花数量限制a
	 */
	@Builder.Default
	protected double a = 0.2;
	/**
	 * 爆炸火花数量限制b
	 */
	@Builder.Default
	protected double b = 0.6;
	/**
	 * 一个很小数
	 */
	@Builder.Default
	protected double eps = Double.MIN_VALUE;

	/**
	 * 随机数
	 */
	@Builder.Default
	protected Random random = new Random();
	/**
	 * 目标函数
	 */
	protected ObjectiveFun objectiveFun;
	/**
	 * 维度
	 */
	protected int dim;
	/**
	 * 最大代数
	 */
	protected int maxGen;
	/**
	 * 最优解
	 */
	protected Firework best;

	@Override
	public void start() {
		// 初始
		initFireworks();
		while (this.iterator < maxGen) {
			calcSparkLight(fireworks);
			generateexplosionSpark(fireworks);
			generateGaussianSpark(fireworks);
			selection(fireworks, explosionSparks, gaussianSparks);
			incrementIter();
			System.out.println("**********第" + iterator + "代最优解：" + best + "**********");
		}
	}

	public void incrementIter() {
		iterator++;
	}

	@Override
	public void selection(List<Firework> fireworks, List<Firework> explosionSparks, List<Firework> gaussianSparks) {
		// TODO Auto-generated method stub
		List<Firework> newFireworks = new ArrayList<Firework>();
		List<Firework> allFireworks = new ArrayList<Firework>();
		allFireworks.addAll(fireworks);
		allFireworks.addAll(explosionSparks);
		allFireworks.addAll(gaussianSparks);
		Collections.sort(allFireworks);
		best = allFireworks.get(allFireworks.size() - 1);
		newFireworks.add(best);
		// 计算选择概率
		double disSum = 0;
		for (Firework fi : allFireworks) {
			double disi = 0;
			for (Firework fj : allFireworks) {
				if (fi != fj) {
					disi += IntStream.range(0, this.dim)
							.mapToDouble(i -> Math
									.abs(fi.getPosition().getPositionCode()[i] - fj.getPosition().getPositionCode()[i]))
							.sum();
				}
			}
			fi.setDis(disi);
			disSum += disi;
		}
		// 选择剩余的烟花
		for (int i = 0; i < this.fireworkNum - 1; i++) {
			double accuProb = 0;
			double rand = random.nextDouble();
			for (Firework f : allFireworks) {
				accuProb += f.getDis() / disSum;
				if (rand <= accuProb) {
					newFireworks.add(f);
					break;
				}
			}
		}
	}

	@Override
	public void generateGaussianSpark(List<Firework> fireworks) {
		// TODO Auto-generated method stub
		gaussianSparks = new ArrayList<Firework>();
		for (int i = 0; i < gaussianSparkNumberM; i++) {
			int index = random.nextInt(fireworks.size());
			// 随机选择烟花
			Firework firework = fireworks.get(index);
			Firework gaussianFirework = new Firework(new Position(this.dim, firework.getPosition().getRange()));
			int dDim = (int) Math.round(random.nextDouble() * this.dim);
			int[] dims = random.ints(dDim, 0, this.dim).toArray();
			double[] newPositionCode = firework.getPosition().getPositionCode();
			for (int d = 0; d < dims.length; d++) {
				newPositionCode[d] *= (random.nextGaussian() + 1);
			}
			gaussianFirework.getPosition().setPositionCode(newPositionCode);
			gaussianFirework.setLight(this.objectiveFun.getObjValue(newPositionCode));
			gaussianSparks.add(gaussianFirework);
		}
	}

	@Override
	public void generateexplosionSpark(List<Firework> fireworks) {
		// TODO Auto-generated method stub
		explosionSparks = new ArrayList<Firework>();
		double maxLight = fireworks.stream().mapToDouble(f -> f.getLight()).max().getAsDouble();
		double minLight = fireworks.stream().mapToDouble(f -> f.getLight()).min().getAsDouble();
		double maxSum = fireworks.stream().mapToDouble(f -> maxLight - f.getLight()).sum();
		double minSum = fireworks.stream().mapToDouble(f -> f.getLight() - minLight).sum();
		// 目标值越优则半径越小，火花数量越多
		for (Firework firework : fireworks) {
			double explosionRadius = this.explosionRadiusA * (maxLight - firework.getLight() + eps) / (maxSum + eps);
			double explosionSparkNumber = this.explosionSparkNumberM * (firework.getLight() - minLight + eps)
					/ (minSum + eps);
			if (explosionSparkNumber < a * explosionSparkNumberM) {
				explosionSparkNumber = Math.round(a * explosionSparkNumberM);
			} else if (explosionSparkNumber < b * explosionSparkNumberM) {
				explosionSparkNumber = Math.round(b * explosionSparkNumberM);
			} else {
				explosionSparkNumber = Math.round(explosionSparkNumber);
			}
			// 产生爆炸火花
			for (int i = 0; i < explosionSparkNumber; i++) {
				Firework explosionFirework = new Firework(new Position(this.dim, firework.getPosition().getRange()));
				int dDim = (int) Math.round(random.nextDouble() * this.dim);
				int[] dims = random.ints(dDim, 0, this.dim).toArray();
				double[] newPositionCode = firework.getPosition().getPositionCode();
				for (int d = 0; d < dims.length; d++) {
					newPositionCode[d] += explosionRadius * (2 * random.nextDouble() - 1);
				}
				explosionFirework.getPosition().setPositionCode(newPositionCode);
				explosionFirework.setLight(this.objectiveFun.getObjValue(newPositionCode));
				explosionSparks.add(explosionFirework);
			}
		}
	}

	@Override
	public void initFireworks() {
		// TODO Auto-generated method stub
		System.out.println("**********种群初始化**********");
		fireworks = new ArrayList<>();
		for (int i = 0; i < fireworkNum; i++) {
			fireworks.add(new Firework(new Position(this.dim, objectiveFun.getRange())));
		}
	}

	@Override
	public void calcSparkLight(List<Firework> fireworks) {
		// TODO Auto-generated method stub
		for (Firework firework : fireworks) {
			firework.setLight(this.objectiveFun.getObjValue(firework.getPosition().getPositionCode()));
		}
	}
}
