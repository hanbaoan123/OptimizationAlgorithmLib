package FA.test;

import Common.ObjectiveFun;
import FA.algorithm.FANormal;

public class FATest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 新建目标函数
		ObjectiveFun objectiveFun = new ObjectiveFun();
		FANormal faNormal = FANormal.builder().popNum(40).maxGen(200).dim(2).alpha(0.2).initAttraction(1.0).gamma(1.0)
				.isAdaptive(true).objectiveFun(objectiveFun).build();
		faNormal.start();
	}
}
