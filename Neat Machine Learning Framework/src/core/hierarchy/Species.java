package core.hierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Species {

	public ArrayList<Genome> genomes;
	public double averageFitness;
	public double previousTopFitness;
	public int staleness;

	public Species() {
		genomes = new ArrayList<>();
		averageFitness = 0;
		staleness = 0;
		previousTopFitness = 0;
	}

	public Species(int staleness, double previousTopFitness, double averageFitness, ArrayList<Genome> genomes) {
		this();
		this.staleness = staleness;
		this.previousTopFitness = previousTopFitness;
		this.averageFitness = averageFitness;
		
		for (int i = 0; i < genomes.size(); i++)
			this.genomes.add(((ArrayList<Genome>) genomes.clone()).get(i));
		
		sort();
	}
	
	public void sort() {
		Collections.sort(genomes, new Comparator<Genome>() {
			@Override
			public int compare(Genome g1, Genome g2) {
				if (g1.fitness > g2.fitness)
					return -1;
				else if (g1.fitness < g2.fitness)
					return 1;
				else
					return 0;
			}
		});
	}
	
	public double averageFitness() {
		double sum = 0;
		
		//if (genomes.isEmpty()) return 0;
		
		for (Genome g:genomes)
			sum += g.fitness;
		
		averageFitness = sum / genomes.size();
		
		return averageFitness;
	}
		
}
