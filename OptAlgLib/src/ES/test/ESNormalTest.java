package ES.test;

import Common.ObjectiveFun;
import ES.algorithm.ESNormal;

public class ESNormalTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// 新建目标函数
		ObjectiveFun objectiveFun = new ObjectiveFun();
		ESNormal es = ESNormal.builder().popNum(20).childNum(100).concatParentandChild(true).maxGen(500).dim(2)
				.objectiveFun(objectiveFun).build();
		es.start();
	}
}
