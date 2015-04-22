package nea.ahni.src.com.ojcoleman.ahni.evaluation.mocostfunctions;

import nea.ahni.src.com.anji.integration.Activator;
import nea.ahni.src.com.ojcoleman.ahni.evaluation.BulkFitnessFunctionMT;
import nea.ahni.src.com.ojcoleman.ahni.hyperneat.Properties;
import nea.ahni.src.com.ojcoleman.ahni.nn.BainNN;
import nea.ahni.src.org.jgapcustomised.Chromosome;

import com.ojcoleman.bain.base.SynapseCollection;

public class BainNNConnectionLengthCost extends BulkFitnessFunctionMT {
	@Override
	public boolean fitnessValuesStable() {
		return true;
	}
	
	@Override
	protected double evaluate(Chromosome genotype, Activator substrate, int evalThreadIndex) {
		if (substrate instanceof BainNN) {
			double tcl = ((BainNN) substrate).getSumOfSquaredConnectionLengths();
			return 1.0 / (1+tcl);
		}
		return 0;
	}
}
