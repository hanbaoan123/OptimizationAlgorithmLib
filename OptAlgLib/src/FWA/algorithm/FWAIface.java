/**
 * 
 */
package FWA.algorithm;

import java.util.List;

import FWA.domian.Firework;

/**
 * @author hba
 *
 */
public interface FWAIface {

	void start();

	void generateexplosionSpark(List<Firework> fireworks2);

	void initFireworks();

	void calcSparkLight(List<Firework> fireworks);

	void generateGaussianSpark(List<Firework> fireworks2);

	void selection(List<Firework> fireworks, List<Firework> explosionSparks, List<Firework> gaussianSparks);

}
