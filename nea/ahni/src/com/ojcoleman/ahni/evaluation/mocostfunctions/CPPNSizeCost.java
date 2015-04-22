package nea.ahni.src.com.ojcoleman.ahni.evaluation.mocostfunctions;

import nea.ahni.src.com.anji.integration.Activator;
import nea.ahni.src.com.ojcoleman.ahni.evaluation.BulkFitnessFunctionMT;
import nea.ahni.src.org.jgapcustomised.Chromosome;

public class CPPNSizeCost extends BulkFitnessFunctionMT {
	@Override
	public boolean fitnessValuesStable() {
		return true;
	}
	
	@Override
	protected double evaluate(Chromosome genotype, Activator substrate, int evalThreadIndex) {
		return 1.0 / (1.0 + (genotype.getAlleles().size() * genotype.getAlleles().size()));
	}
}
