package ABC.test;

import ABC.algorithm.ABCNormal;
import Common.ObjectiveFun;

public class ABCNormalTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ObjectiveFun objectiveFun = new ObjectiveFun();
		ABCNormal abc = ABCNormal.builder().foodSourcesAmout(20).maxGen(500).dim(2).foodLimit(20)
				.objectiveFun(objectiveFun).build();
		abc.start();
	}
}
