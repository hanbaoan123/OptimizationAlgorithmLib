/**
 * 
 */
package AFSA.algorithm;

import AFSA.domain.Fish;

/**
 * @author hba 人工鱼群算法
 */
public interface AFSA {

	void initPop();

	void start() throws Exception;

	Fish AF_Prey(Fish fish);

	Fish AF_Follow(Fish fish);

	Fish AF_Swarm(Fish fish);

}
