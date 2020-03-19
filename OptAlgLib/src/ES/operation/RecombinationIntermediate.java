package ES.operation;

import java.util.List;

import ES.domain.Code;
import ES.domain.Individual;

/*
 * 中间交叉，取平均值
 */
public class RecombinationIntermediate extends Recombination {
	public Individual crossover(List<Individual> selectedParents) {
		Code code = new Code(selectedParents.get(0).getCode().getDimension(),
				selectedParents.get(0).getCode().getRange());

		double[] codeD = new double[code.getDimension()];
		double[] mutStrengthD = new double[code.getDimension()];
		for (int i = 0; i < code.getDimension(); i++) {
			double sum1 = 0;
			double sum2 = 0;
			for (int j = 0; j < selectedParents.size(); j++) {
				sum1 += selectedParents.get(j).getCode().getCode()[i];
				sum2 += selectedParents.get(j).getCode().getMutStrength()[i];
			}
			codeD[i] = sum1 / selectedParents.size();
			mutStrengthD[i] = sum2 / selectedParents.size();
		}
		code.setCode(codeD);
		code.setMutStrength(mutStrengthD);
		Individual individual = new Individual(code);
		return individual;
	}
}
