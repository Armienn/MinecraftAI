/*
 * Copyright (C) 2004 Derek James and Philip Tucker
 * 
 * This file is part of ANJI (Another NEAT Java Implementation).
 * 
 * ANJI is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 * created by Philip Tucker on Apr 21, 2004
 */
package nea.ahni.src.com.anji.neat;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import nea.ahni.src.com.anji.integration.AnjiRequiredException;
import nea.ahni.src.com.anji.nn.RecurrencyPolicy;
import nea.ahni.src.com.anji.util.Configurable;
import nea.ahni.src.com.anji.util.Properties;
import nea.ahni.src.org.jgapcustomised.Allele;
import nea.ahni.src.org.jgapcustomised.ChromosomeMaterial;
import nea.ahni.src.org.jgapcustomised.Configuration;
import nea.ahni.src.org.jgapcustomised.InvalidConfigurationException;
import nea.ahni.src.org.jgapcustomised.MutationOperator;

/**
 * @author Philip
 */
public class SingleTopologicalMutationOperator extends MutationOperator implements Configurable {

	private final static double DEFAULT_MUTATION_RATE = calculateMutationRate(AddConnectionMutationOperator.DEFAULT_MUTATE_RATE, AddNeuronMutationOperator.DEFAULT_MUTATE_RATE);

	private AddConnectionMutationOperator addConnOp;

	private AddNeuronMutationOperator addNeuronOp;

	private double addConnRatio = calculateAddConnRatio(AddConnectionMutationOperator.DEFAULT_MUTATE_RATE, AddNeuronMutationOperator.DEFAULT_MUTATE_RATE);

	/**
	 * @see com.anji.util.Configurable#init(com.anji.util.Properties)
	 */
	public void init(Properties props) throws Exception {
		addConnOp = (AddConnectionMutationOperator) props.singletonObjectProperty(AddConnectionMutationOperator.class);
		addNeuronOp = (AddNeuronMutationOperator) props.singletonObjectProperty(AddNeuronMutationOperator.class);
		double addConnRate = addConnOp.getMutationRate();
		double addNeuronRate = addNeuronOp.getMutationRate();
		addConnRatio = calculateAddConnRatio(addConnRate, addNeuronRate);
		setMutationRate(calculateMutationRate(addConnRate, addNeuronRate));
	}

	private static double calculateAddConnRatio(double addConnMutationRate, double addNeuronMutationRate) {
		return (addConnMutationRate - (addConnMutationRate * addNeuronMutationRate * 0.5f)) / (addConnMutationRate + addNeuronMutationRate);
	}

	private static double calculateMutationRate(double addConnMutationRate, double addNeuronMutationRate) {
		return addConnMutationRate + addNeuronMutationRate - (addConnMutationRate * addNeuronMutationRate);
	}

	/**
	 * should call <code>init()</code> after this constructor
	 */
	public SingleTopologicalMutationOperator() {
		super(DEFAULT_MUTATION_RATE);
	}

	/**
	 * ctor
	 * 
	 * @param addConnMutationRate
	 * @param addNeuronMutationRate
	 * @param aPolicy
	 */
	public SingleTopologicalMutationOperator(double addConnMutationRate, double addNeuronMutationRate, RecurrencyPolicy aPolicy) {
		super(calculateMutationRate(addConnMutationRate, addNeuronMutationRate));
		addConnRatio = calculateAddConnRatio(addConnMutationRate, addNeuronMutationRate);
		addConnOp = new AddConnectionMutationOperator(addConnMutationRate, aPolicy);
		addNeuronOp = new AddNeuronMutationOperator(addNeuronMutationRate);
	}

	/**
	 * @see org.jgapcustomised.MutationOperator#mutate(org.jgapcustomised.Configuration,
	 *      org.jgapcustomised.ChromosomeMaterial, java.util.Set, java.util.Set)
	 */
	protected void mutate(final Configuration jgapConfig, final ChromosomeMaterial target, Set<Allele> allelesToAdd, Set<Allele> allelesToRemove) throws InvalidConfigurationException {
		if ((jgapConfig instanceof NeatConfiguration) == false)
			throw new AnjiRequiredException("com.anji.neat.NeatConfiguration");

		NeatConfiguration config = (NeatConfiguration) jgapConfig;

		Random rand = config.getRandomGenerator();
		if (doesMutationOccur(rand)) {
			SortedSet<Allele> alleles = target.getAlleles();
			if (rand.nextDouble() < addConnRatio) {
				List<NeuronAllele> neuronList = NeatChromosomeUtility.getNeuronList(alleles);
				SortedMap<Long, ConnectionAllele> conns = NeatChromosomeUtility.getConnectionMap(alleles);
				addConnOp.addConnections(1, config, neuronList, conns, allelesToAdd, allelesToRemove);
			} else {
				List connList = NeatChromosomeUtility.getConnectionList(alleles);
				Collections.shuffle(connList, rand);
				Iterator iter = connList.iterator();
				boolean isAdded = false;
				while (iter.hasNext() && !isAdded) {
					ConnectionAllele oldConnectAllele = (ConnectionAllele) iter.next();
					isAdded = addNeuronOp.addNeuronAtConnection(config, NeatChromosomeUtility.getNeuronMap(alleles), oldConnectAllele, allelesToAdd, allelesToRemove);
				}
			}
		}
	}
}
