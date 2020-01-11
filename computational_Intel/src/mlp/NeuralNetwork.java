package mlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class NeuralNetwork 
{
	public final int[] NetworkLayers;
	public final int Input;
	public final int Output;
	public final int Layers;
	public final String[] activations;
	
	private double[][] output;
	private double[][][] weights;
	private double[][] bias;
	
	private double[][] errors;
	private double[][][] weightDeltas;
	private double[][] biasDeltas;
	private double[][] derOutput;

	private Utilities util = new Utilities();
	private Random rand = new Random();
	private FileWriter correct, incorrect;
	
	private double[][] AllTrainingInputs;
	private double[][] AllTargetOutputs;
	private double[][] AllRealOutputs;
	private int numOfExamples;
	
	public NeuralNetwork(int[] NetworkLayers, String[] activations) throws FileNotFoundException 
	{
		this.NetworkLayers = NetworkLayers;
		this.Input = NetworkLayers[0];
		this.Output = NetworkLayers[NetworkLayers.length - 1];
		this.Layers = NetworkLayers.length;
		this.activations = activations;
		
		output = new double[Layers][];
		weights = new double[Layers][][];
		bias = new double[Layers][];
		
		errors =  new double[Layers][];
		weightDeltas = new double[Layers][][];
		biasDeltas = new double[Layers][];
		derOutput = new double[Layers][];
		
		util.readExamplesFromFile("training.txt");
		AllTrainingInputs = util.getAllInputs();
		AllTargetOutputs = util.getAllOutputs();
		numOfExamples = AllTrainingInputs.length;
		AllRealOutputs = new double[numOfExamples][Output];
		
		for(int layer = 0; layer < Layers; layer++) 
		{
			output[layer] = new double[NetworkLayers[layer]];
			derOutput[layer] = new double[NetworkLayers[layer]];
			errors[layer] = new double[NetworkLayers[layer]];
			bias[layer] = new double[NetworkLayers[layer]];
			biasDeltas[layer] = new double[NetworkLayers[layer]];
			
			if(layer > 0) 
			{
				int curr = NetworkLayers[layer];
				int prev = NetworkLayers[layer - 1];
				weights[layer] = new double[curr][prev];
				weightDeltas[layer] = new double[curr][prev];
				
				double rangeMin = 0.1, rangeMax = 0.9;
				for(int i = 0; i < curr; i++) 
				{
					for(int j = 0; j < prev; j++) {
						weights[layer][i][j] = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
					}
					bias[layer][i] = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
				}
			}
		}
	}
	
	public double[] forward(double[] input) 
	{
		output[0] = input;
		for(int layer = 1; layer < Layers; layer++) 
		{
			for(int neuron = 0; neuron < NetworkLayers[layer]; neuron++) 
			{
				double sum = 0;
				for(int previous = 0; previous < NetworkLayers[layer - 1]; previous++) {
					sum += output[layer - 1][previous] * weights[layer][neuron][previous];
				}
				sum += bias[layer][neuron];
				output[layer][neuron] = activation(activations[layer], sum);
				derOutput[layer][neuron] = derActivation(activations[layer], sum);
			}
		}
		return output[Layers - 1];
	}
	
	public void batchedTrain(double n, int epochs, int BatchSize) 
	{
		int NumOfBatches = AllTrainingInputs.length / BatchSize;
		for(int e = 0; e < epochs; e++) 
		{
			double errorSum = 0;
			for(int batch = 0; batch < NumOfBatches; batch++) 
			{
				double[][] InputBatch = util.getInBatch(BatchSize, batch);
				double[][] TargetBatch = util.getTarBatch(BatchSize, batch);
				
				for(int i = 0; i < BatchSize; i++) {
					AllRealOutputs[i + batch * BatchSize] = forward(InputBatch[i]);
				}
				backward(TargetBatch);
				updateWeights(n);
			}
			errorSum += util.squareError(AllRealOutputs, AllTargetOutputs);
			System.out.println("Error at epoch " + e + ": " + errorSum / numOfExamples);
		}
	}
	
	public void train(double n, int epochs) 
	{
		for(int e = 0; e < epochs; e++) 
		{
			double errorSum = 0;
			double[][] Target;
			for(int i = 0; i < AllTrainingInputs.length; i++) 
			{
				AllRealOutputs[i] = forward(AllTrainingInputs[i]);
				Target = util.getTarBatch(1, i);
				backward(Target);
			}
			updateWeights(n);
			
			errorSum += util.squareError(AllRealOutputs, AllTargetOutputs);
			System.out.println("Error at epoch " + e + ": " + errorSum/numOfExamples);
		}
	}
	
	public void backward(double[][] target) 
	{
		backPropOutput(target);
		backPropHidden();
	}
	
	private void backPropHidden() 
	{
		for(int layer = Layers - 2; layer > 0; layer--) 
		{
			for(int neuron = 0; neuron < NetworkLayers[layer]; neuron++) 
			{
				double sum = 0;
				for(int next = 0; next < NetworkLayers[layer + 1]; next++) {
					sum += weights[layer + 1][next][neuron] * errors[layer + 1][next];
				}
				errors[layer][neuron] = sum * derOutput[layer][neuron];
				
				for(int previous = 0; previous < NetworkLayers[layer - 1]; previous++) {
					weightDeltas[layer][neuron][previous] = output[layer - 1][previous] * errors[layer][neuron];
				}
				biasDeltas[layer][neuron] = errors[layer][neuron];
			}
		}
	}

	private void backPropOutput(double[][] target) 
	{
		int OutputLayer = Layers - 1;
		int BatchSize = target.length;
		
		for(int neuron = 0; neuron < OutputLayer; neuron++) 
		{
			errors[OutputLayer][neuron] = 0;
			for(int i = 0; i < BatchSize; i++) {
				errors[OutputLayer][neuron] += (output[OutputLayer][neuron] - target[i][neuron]) / BatchSize;	
			}
			errors[OutputLayer][neuron] *= derOutput[OutputLayer][neuron];
			
			weightDeltas[OutputLayer][neuron][OutputLayer - 1] = output[OutputLayer][OutputLayer - 1] * errors[OutputLayer][neuron];
			biasDeltas[OutputLayer][neuron] = errors[OutputLayer][neuron];
		}
	}

	public void updateWeights(double n) 
	{
		for(int layer = 1; layer < Layers; layer++) 
		{
			for(int neuron = 0; neuron < NetworkLayers[layer]; neuron++) 
			{
				for(int previous = 0; previous < NetworkLayers[layer - 1]; previous++) {
					weights[layer][neuron][previous] -= n * weightDeltas[layer][neuron][previous];
				}
				bias[layer][neuron] -= n * biasDeltas[layer][neuron];
			}
		}
	}
	
	public double activation(String config, double x) 
	{
		if(config.equals("sigmoid")) return util.sigmoid(x);
		if(config.equals("tanh")) return util.tanh(x);
		if(config.equals("step")) return util.step(x);
		return x;
	}
	
	public double derActivation(String function, double x) 
	{
		if(function.equals("sigmoid")) return util.derSigmoid(x);
		if(function.equals("tanh")) return util.derTanh(x);
		return 1;
	}
	
	public void test(int i) 
	{
		util.printDoubleArray(forward(AllTrainingInputs[i]));
		util.printDoubleArray(AllTargetOutputs[i]);
	}
	
	public void testAll() throws IOException 
	{
		util.readExamplesFromFile("testing.txt");
		correct = new FileWriter(new File("correct.txt"));
		incorrect = new FileWriter(new File("incorrect.txt"));
		
		double[][] AllTestingInputs = util.getAllInputs();
		double[][] AllTestingTargetOutputs = util.getAllOutputs();
		
		int count = 0;
		for(int i = 0; i < AllTestingInputs.length; i++) 
		{
			if(util.equals(forward(AllTestingInputs[i]), AllTestingTargetOutputs[i])) 
			{
				count++;
				correct.write(AllTestingInputs[i][0] + " " + AllTestingInputs[i][1] + "\n");
			} else {
				incorrect.write(AllTestingInputs[i][0] + " " + AllTestingInputs[i][1] + "\n");
			}
		}
		correct.close();
		incorrect.close();
		System.out.print((double)(count * 100 / AllTestingInputs.length) + "% Accuracy.") ;
	}
		
	public void print()
	{
		System.out.println("Input Layer:\t" + Input + " Neurons.");
		for(int i=1; i<NetworkLayers.length - 1; i++) 
		{
			System.out.print("Hidden Layer " + i + ":\t" + NetworkLayers[i] + " Neurons.");
			System.out.print("Activation:\t" + activations[i] + "\n");
		}
		System.out.println("Output Layer:\t" + Output + " Neurons.");
	}
}