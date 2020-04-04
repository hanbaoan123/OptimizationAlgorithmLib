package HS.test;

import Common.ObjectiveFun;
import HS.algorithm.HSContinious;

public class HSContiniousTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 新建目标函数
		ObjectiveFun objectiveFun = new ObjectiveFun();
		HSContinious hsContinious = HSContinious.builder().HMS(100).maxI(2000).numOfInstruments(2).FW(0.01).HMCR(0.7)
				.PAR(0.25).objectiveFun(objectiveFun).build();
		hsContinious.start();
	}
}
