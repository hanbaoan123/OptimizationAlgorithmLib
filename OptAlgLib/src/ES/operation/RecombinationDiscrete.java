package ES.operation;

import java.util.List;
import java.util.Random;

import ES.domain.Code;
import ES.domain.Individual;

/*
 * 离散交叉，每个维度随机从父代中选取
 */
public class RecombinationDiscrete extends Recombination{
	private Random random = new Random();

	public Individual crossover(List<Individual> selectedParents) {
		Code code = new Code(selectedParents.get(0).getCode().getDimension(),
				selectedParents.get(0).getCode().getRange());
		double[] codeD = new double[code.getDimension()];
		double[] mutStrengthD = new double[code.getDimension()];
		for (int i = 0; i < code.getDimension(); i++) {
			codeD[i] = selectedParents.get(random.nextInt(selectedParents.size())).getCode().getCode()[i];
			mutStrengthD[i] = selectedParents.get(random.nextInt(selectedParents.size())).getCode().getMutStrength()[i];
		}
		code.setCode(codeD);
		code.setMutStrength(mutStrengthD);
		Individual individual = new Individual(code);
		return individual;
	}
}
