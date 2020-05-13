package GA.test;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

import Common.ObjectiveFun;
import Common.objectives.min.SphereFunction;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static io.jenetics.engine.Limits.bySteadyFitness;

import io.jenetics.DoubleGene;
import io.jenetics.MeanAlterer;
import io.jenetics.Mutator;
import io.jenetics.Optimize;
import io.jenetics.Phenotype;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.DoubleRange;

public class GATest {
	private static ObjectiveFun objectiveFun = new SphereFunction();

	private static double fitness(final double[] x) {
		return objectiveFun.getObjValue(x);
	}

	public static void main(final String[] args) {
		final Engine<DoubleGene, Double> engine = Engine
				.builder(GATest::fitness,
						Codecs.ofVector(DoubleRange.of(objectiveFun.getRange().getLow()[0],
								objectiveFun.getRange().getHigh()[0]), objectiveFun.getRange().getDim()))
				.populationSize(500).optimize(Optimize.MINIMUM).alterers(new Mutator<>(0.03), new MeanAlterer<>(0.6))
				.build();

		final EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();

		final Phenotype<DoubleGene, Double> best = engine.stream().limit(bySteadyFitness(7)).peek(statistics)
				.collect(toBestPhenotype());

		System.out.println(statistics);
		System.out.println(best);
	}
}