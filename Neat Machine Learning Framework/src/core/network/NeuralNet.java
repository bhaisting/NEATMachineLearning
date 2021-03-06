package core.network;

import java.util.ArrayList;
import java.util.Comparator;

import core.Neat;
import core.hierarchy.Gene;

public class NeuralNet {

	private Neuron[] neurons;
	
	public NeuralNet (ArrayList<Gene> genes) {
		neurons = new Neuron[Neat.NUMBER_OF_INPUTS + Neat.MAX_HIDDEN_NODES + Neat.NUMBER_OF_OUTPUTS];
		
		for (int i = 0; i <= Neat.NUMBER_OF_INPUTS; i++)
			neurons[i] = (new Neuron());
		
		for (int i = 1; i <= Neat.NUMBER_OF_OUTPUTS; i++)
			neurons[Neat.NUMBER_OF_INPUTS + Neat.MAX_HIDDEN_NODES + i - 1] = new Neuron();
		
		genes.sort(new Comparator<Gene>() {
			@Override
			public int compare(Gene g1, Gene g2) {
				if (g1.out < g2.out) return -1;
				if (g1.out > g2.out) return 1;
				return 0;
			}
		});
		
		for (Gene gene : genes) {
			if (gene.isEnabled) {
				if (neurons[gene.out] == null) {
					neurons[gene.out] = new Neuron();
				}
				
				if (gene.isRecurrent) neurons[gene.out].recurrentConnection = gene;
				else neurons[gene.out].incoming.add(gene);
				
				if (neurons[gene.in] == null) {
					neurons[gene.in] = new Neuron();
				}
			}
		}
	}

	public double[] propagate(double[] inputs) {
		double[] outputs = new double[Neat.NUMBER_OF_OUTPUTS];
		
		neurons[0].value = Neat.BIAS_NODE_VALUE;
		
		for (int i = 1; i <= inputs.length; i++)
			neurons[i].value = inputs[i - 1];
		
		for (int i = Neat.NUMBER_OF_INPUTS; i < Neat.NUMBER_OF_INPUTS + Neat.MAX_HIDDEN_NODES + Neat.NUMBER_OF_OUTPUTS; i++) {
			Neuron neuron = neurons[i];
			if (neuron != null && neuron.incoming.size() > 0) {
				if (i < Neat.NUMBER_OF_INPUTS)
					neuron.value = linear(neuron.sumIncoming(neurons));
				else
					neuron.value = sigmoid(neuron.sumIncoming(neurons));
				
				//if (neuron.incoming.isEmpty()) neuron.value = 0;
			}
		}
		
		
		//for (int i = 1; i <= Neat.NUMBER_OF_OUTPUTS; i++)
			//neurons[Neat.NUMBER_OF_INPUTS + Neat.MAX_HIDDEN_NODES + i - 1].calcValue(neurons);
		
		for (int i = 1; i <= outputs.length; i++)
			outputs[i - 1] = neurons[Neat.NUMBER_OF_INPUTS + Neat.MAX_HIDDEN_NODES + i - 1].value;
		
		for (Neuron neuron : neurons)
			if (neuron != null)
				neuron.history.add(neuron.value);
		
		return outputs;
	}

	private double sigmoid(double sum) {
		//return 2 / (1 + Math.exp(-4.9 * sum)) - 1;
		return 1D / (1D + Math.pow(Math.E, -sum));
	}
	
	private double linear(double sum) {
		return sum;
	}
	
}
