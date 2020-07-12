package BA.test;

import BA.algorithm.BANormal;
import Common.ObjectiveFun;
import Common.objectives.min.SphereFunction;

public class BANormalTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ObjectiveFun objectiveFun = new SphereFunction();
		BANormal ba = BANormal.builder().batAmount(50).maxGen(2000).loudness(0.5).loudnessAttenuation(0.9)
				.pulseRate(0.5).pulseRateAttenuation(2).frequencyMin(0).frequencyMax(2)
				.dim(objectiveFun.getRange().getDim()).objectiveFun(objectiveFun).build();
		ba.start();
	}

}
