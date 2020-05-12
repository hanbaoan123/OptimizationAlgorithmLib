package SSA.test;

import Common.ObjectiveFun;
import SSA.algorithm.SSANormal;

public class SSANormalTest {
	public static void main(String[] args) {
		ObjectiveFun objectiveFun = new ObjectiveFun();
		SSANormal ssa = SSANormal.builder().maxGen(400).spiderAmount(10).dim(2).ra(1).pc(0.7).pm(0.1)
				.objectiveFun(objectiveFun).build();
		ssa.start();
	}
}
