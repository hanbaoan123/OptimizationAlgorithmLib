package SA.test;

import Common.ObjectiveFun;
import SA.algorithm.SANormal;

public class SANormalTest {
	public static void main(String[] args) {
		ObjectiveFun objectiveFun = new ObjectiveFun();
		SANormal sa = SANormal.builder().maxGen(400).dim(2).T0(10).beta(200).objectiveFun(objectiveFun).build();
		sa.start();
	}
}
