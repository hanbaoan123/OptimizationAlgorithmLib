package AFSA.test;

import AFSA.algorithm.AFSANormal;
import Common.ObjectiveFun;

public class AFSANormalTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ObjectiveFun objectiveFun = new ObjectiveFun();
		AFSANormal abc = AFSANormal.builder().fishAmount(500).delta(0.2).step(0.1).trials(10).visual(2).maxGen(300)
				.dim(2).objectiveFun(objectiveFun).build();
		abc.start();
	}
}