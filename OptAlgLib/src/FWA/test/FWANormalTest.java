package FWA.test;

import Common.ObjectiveFun;
import FWA.algorithm.FWANormal;

public class FWANormalTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ObjectiveFun objectiveFun = new ObjectiveFun();
		FWANormal fwa = FWANormal.builder().fireworkNum(20).explosionRadiusA(10).explosionSparkNumberM(10)
				.gaussianSparkNumberM(20).maxGen(500).dim(2).objectiveFun(objectiveFun).build();
		fwa.start();
	}
}
