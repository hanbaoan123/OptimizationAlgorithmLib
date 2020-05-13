package SSA.test;

import Common.ObjectiveFun;
import Common.objectives.min.SphereFunction;
import SSA.algorithm.SSANormal;

public class SSANormalTest {
	public static void main(String[] args) {
		ObjectiveFun objectiveFun = new SphereFunction();
		SSANormal ssa = SSANormal.builder().maxGen(10000).spiderAmount(25).dim(objectiveFun.getRange().getDim()).ra(1)
				.pc(0.7).pm(0.1).objectiveFun(objectiveFun).build();
		ssa.start();
	}
}
