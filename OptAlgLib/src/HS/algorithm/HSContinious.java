package HS.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import ABC.domain.FoodSource;
import Common.ObjectiveFun;
import Common.Position;
import Common.Range;
import HS.domain.Composition;
import HS.domain.Harmony;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class HSContinious implements HSIface {
	/**
	 * 和声记忆库
	 */
	protected List<Harmony> harmonyMemory;
	/**
	 * 和声记忆库大小
	 */
	protected int HMS;
	/**
	 * 和声记忆率
	 */
	@Builder.Default
	protected double HMCR = 0.95;
	/**
	 * 音调调节率
	 */
	@Builder.Default
	protected double PAR = 0.1;
	/**
	 * 调整宽度
	 */
	@Builder.Default
	protected double FW = 0.01;

	/**
	 * 迭代计数器
	 */
	@Builder.Default
	protected int iterator = 0;
	/**
	 * 随机数
	 */
	@Builder.Default
	protected Random random = new Random();
	/**
	 * 目标函数
	 */
	protected ObjectiveFun objectiveFun;
	/**
	 * 最大创作数
	 */
	protected int maxI;

	/**
	 * 乐器数
	 */
	protected int numOfInstruments;

	@Override
	public void start() {
		// TODO Auto-generated method stub
		if (this.random == null) {
			this.random = new Random();
		}
		initHM();
		while (this.iterator < maxI) {
			Harmony improvisedHarmony = improviseHarmony();
			updateHM(improvisedHarmony);
			System.out.println(
					"**********第" + iterator + "代最优解：" + harmonyMemory.get(harmonyMemory.size() - 1) + "**********");
			incrementIter();
		}
	}

	/**
	 * 初始化和声记忆库
	 */
	@Override
	public void initHM() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		System.out.println("**********和声记忆库初始化**********");
		harmonyMemory = new ArrayList<>();
		for (int i = 0; i < HMS; i++) {
			harmonyMemory.add(new Harmony(new Composition(this.numOfInstruments, objectiveFun.getRange())));
		}
		for (Harmony harmony : harmonyMemory) {
			harmony.setPerformence(this.objectiveFun.getObjValue(harmony.getComposition().getPitches()));
		}
		Collections.sort(harmonyMemory);
	}

	/**
	 * 创作和声
	 */
	@Override
	public Harmony improviseHarmony() {
		// TODO Auto-generated method stub
		Range range = objectiveFun.getRange();
		Harmony newHarmony = new Harmony(new Composition(this.numOfInstruments, range));
		double[] newPitches = newHarmony.getComposition().getPitches();
		for (int d = 0; d < numOfInstruments; d++) {
			double rnd = random.nextDouble();
			// 从和声记忆库中取值
			if (rnd <= HMCR) {
				newPitches[d] = harmonyMemory.get(random.nextInt(HMS)).getComposition().getPitches()[d];
				// 音调调节
				double rnd2 = random.nextDouble();
				if (rnd2 <= PAR) {
					newPitches[d] = newPitches[d]
							+ (2 * random.nextDouble() - 1) * FW * (range.getHigh()[d] - range.getLow()[d]);
				}
			}
		}
		newHarmony.getComposition().setPitches(newPitches);
		newHarmony.setPerformence(this.objectiveFun.getObjValue(newHarmony.getComposition().getPitches()));
		return newHarmony;
	}

	/**
	 * 更新和声记忆库
	 * 
	 * @param improvisedHarmony
	 */
	@Override
	public void updateHM(Harmony improvisedHarmony) {
		// TODO Auto-generated method stub
		if (improvisedHarmony.getPerformence() > harmonyMemory.get(0).getPerformence()) {
			harmonyMemory.remove(0);
			harmonyMemory.add(improvisedHarmony);
		}
		Collections.sort(harmonyMemory);
	}

	public void incrementIter() {
		iterator++;
	}
}
