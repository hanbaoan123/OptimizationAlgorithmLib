package ES.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Common.ObjectiveFun;
import ES.domain.Code;
import ES.domain.Individual;
import ES.operation.Recombination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class ESAdaptive implements ESIface {
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
	 * 随机数
	 */
	@Builder.Default
	protected Random random = new Random();
	/**
	 * 交叉因子ρ
	 */
	private int crossNum;
	/**
	 * 重组
	 */
	private Recombination recombination;

	public void incrementIter() {
		iterator++;
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		if (this.random == null) {
			this.random = new Random();
		}
		initPop();
		while (this.iterator < maxGen) {
			List<Individual> newPopulation = this.makeNewIndividuals(population);
			Individual bestIndividual = killBadIndividuals(newPopulation);
			incrementIter();
			System.out.println("**********第" + iterator + "代最优解：" + bestIndividual + "**********");
		}
	}

	@Override
	public void initPop() {
		// TODO Auto-generated method stub
		System.out.println("**********种群初始化**********");
		population = new ArrayList<>();
		for (int i = 0; i < popNum; i++) {
			population.add(new Individual(new Code(this.dim, objectiveFun.getRange(), this.mutStrength)));
		}

	}

	@Override
	public List<Individual> makeNewIndividuals(List<Individual> population) {
		// TODO Auto-generated method stub
		List<Individual> children = new ArrayList<>();
		try {
			for (int i = 0; i < this.childNum; i++) {
				List<Individual> selectedParentsIndividuals = random.ints(crossNum, 0, population.size())
						.mapToObj(ind -> population.get(ind)).collect(Collectors.toList());

				Individual newIndividual = this.recombination.crossover(selectedParentsIndividuals);

				double t0 = Math.pow(2 * this.dim, -0.5);

				double t = Math.pow(2 * Math.pow(this.dim, 0.5), -0.5);

				double random0 = random.nextGaussian();

				double[] newMutStrength = IntStream.range(0, this.dim)
						.mapToDouble(ind -> newIndividual.getCode().getMutStrength()[ind]
								* Math.pow(Math.E, t0 * random0 + t * random.nextGaussian()))
						.toArray();

				double[] newCode = IntStream.range(0, this.dim).mapToDouble(
						ind -> newIndividual.getCode().getCode()[ind] + Math.sqrt(mutStrength) * random.nextGaussian())
						.toArray();
				newIndividual.getCode().setCode(newCode);
				newIndividual.getCode().setMutStrength(newMutStrength);
				children.add(newIndividual);
			}
			// 判断是否合并父代和子代，且如果为(1+1)-ES必须合并
			if (concatParentandChild || (this.childNum == 1 && this.popNum == 1)) {
				children.addAll(population);
			}
		} catch (Exception e) {
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
