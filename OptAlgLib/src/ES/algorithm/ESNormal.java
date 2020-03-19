package ES.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import Common.ObjectiveFun;
import ES.domain.Code;
import ES.domain.Individual;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class ESNormal implements ESIface {
	/**
	 * 种群
	 */
	protected List<Individual> population;
	/**
	 * 变异强度
	 */
	@Builder.Default
	protected double mutStrength = 5;
	/**
	 * 最大代数
	 */
	protected int maxGen;
	/**
	 * 种群大小μ
	 */
	protected int popNum;
	/**
	 * 子代个数λ
	 */
	protected int childNum;
	/**
	 * 维度
	 */
	protected int dim;
	/**
	 * 目标函数
	 */
	protected ObjectiveFun objectiveFun;
	/**
	 * 自适应ES
	 */
	@Builder.Default
	protected boolean isAdaptive = false;
	/**
	 * 是否合并父代和子代
	 */
	@Builder.Default
	protected boolean concatParentandChild = false;
	/**
	 * 迭代计数器
	 */
	@Builder.Default
	protected int iterator = 0;
	/**
	 * 成功变异次数
	 */
	@Builder.Default
	protected int successMut = 0;
	/**
	 * 统计范围G
	 */
	@Builder.Default
	protected int G = 20;
	/**
	 * 随机数
	 */
	@Builder.Default
	protected Random random = new Random();

	public ESNormal() {
	}

	@Override
	public void initPop() {
		// TODO Auto-generated method stub
		System.out.println("**********种群初始化**********");
		population = new ArrayList<>();
		for (int i = 0; i < popNum; i++) {
			population.add(new Individual(new Code(this.dim, objectiveFun.getRange())));
		}
	}

	public void incrementIter() {
		iterator++;
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		initPop();
		while (this.iterator < maxGen) {
			List<Individual> newPopulation = this.makeNewIndividuals(population);
			Individual bestIndividual = killBadIndividuals(newPopulation);
			incrementIter();
			System.out.println("**********第" + iterator + "代最优解：" + bestIndividual + "**********");
		}
	}

	@Override
	public List<Individual> makeNewIndividuals(List<Individual> population) {
		// TODO Auto-generated method stub
		List<Individual> children = new ArrayList<>();
		try {
			for (int i = 0; i < this.childNum; i++) {
				int parentInd = random.nextInt(population.size());
				Individual parent = population.get(parentInd);
				Individual child = parent.clone();
				double[] newCode = IntStream.range(0, this.dim)
						.mapToDouble(
								ind -> child.getCode().getCode()[ind] + Math.sqrt(mutStrength) * random.nextGaussian())
						.toArray();
				child.getCode().setCode(newCode);
				children.add(child);
			}
			// 判断是否合并父代和子代，且如果为(1+1)-ES必须合并
			if (concatParentandChild || (this.childNum == 1 && this.popNum == 1)) {
				children.addAll(population);
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return children;
	}

	@Override
	public Individual killBadIndividuals(List<Individual> population) throws Exception {
		Individual bestIndividual = null;
		// 解码
		for (Individual individual : population) {
			individual.setFitness(this.getObjectiveFun().getObjValue((individual.getCode().getCode())));
		}
		// 如果为(1+1)-ES,需要进行自适应调整变异强度
		if (popNum == 1 && childNum == 1) {
			// 最大化目标的化，判断是不是成功变异，要判断子代适应度是否大于父代适应度
			if (this.getObjectiveFun().getDirection() == ObjectiveFun.Max) {
				if (population.get(0).getFitness() > population.get(1).getFitness()) {
					successMut++;
				}
			} else {
				if (population.get(0).getFitness() < population.get(1).getFitness()) {
					successMut++;
				}
			}
		}
		if ((this.iterator + 1) % G == 0) {
			// 大于0.2则增加变异强度
			if (successMut / G > 0.2) {
				this.mutStrength = this.mutStrength / 0.817;
			} else if (successMut / G < 0.2) {
				this.mutStrength = this.mutStrength * 0.817;
			}
			successMut = 0;
		}

		// 首先进行排序
		Collections.sort(population);
		// 在(μ,λ)-ES中必须保证μ<λ
		if (popNum > population.size()) {
			throw new Exception("请重新设置父代种群大小(μ)和子代种群大小(λ)，以保证μ<λ,一般要求5μ=λ !!!");
		}
		// 如果是最大化问题，则取前λ个个体（因为排序是按照适应度值从大到小排列的）
		if (this.getObjectiveFun().getDirection() == ObjectiveFun.Max) {
			this.population = population.subList(0, popNum);
			bestIndividual = population.get(0);
		} else {
			this.population = population.subList(population.size() - 1 - popNum, population.size() - 1);
			bestIndividual = population.get(population.size() - 1);
		}
		return bestIndividual;
	}
}
