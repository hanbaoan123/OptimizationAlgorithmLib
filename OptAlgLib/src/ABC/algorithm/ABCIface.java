package ABC.algorithm;

import java.util.List;

import ABC.domain.FoodSource;

public interface ABCIface {
	/**
	 * ÖÖÈº³õÊ¼»¯
	 */
	void initPop();

	/**
	 * ¿ªÊ¼
	 * 
	 * @throws Exception
	 */
	void start() throws Exception;

	/**
	 * ¹ÍÓ¶·ä½×¶Î
	 * 
	 * @param foodSources
	 */
	void employeeBeePhase(List<FoodSource> foodSources);

	/**
	 * Õì²ì·ä½×¶Î
	 * 
	 * @param foodSources
	 */
	void scoutBeePhase(List<FoodSource> foodSources);

	/**
	 * ¹Û²ì·ä½×¶Î
	 * 
	 * @param foodSources
	 */
	void onlookerBeePhase(List<FoodSource> foodSources);

}
