package ES.algorithm;

import java.util.List;

import ES.domain.Individual;

public interface ESIface {
	/**
	 * 算法运行
	 * 
	 * @throws Exception
	 * 
	 */
	public void start() throws Exception;

	/**
	 * 初始化种群
	 */
	public void initPop();

	/**
	 * 根据父代创建子代
	 * 
	 * @param population
	 * @return
	 */
	public List<Individual> makeNewIndividuals(List<Individual> population);

	/**
	 * 从种群中移除劣势个体
	 * 
	 * @param population
	 * @return
	 * @throws Exception
	 */
	public Individual killBadIndividuals(List<Individual> population) throws Exception;
}
