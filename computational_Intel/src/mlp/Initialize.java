package mlp;

import java.io.IOException;

public class Initialize 
{
	public static void main(String[] args) throws IOException
	{
		int input = 2;
		int hidden1 = 8;
		int hidden2 = 5;
		int output = 3;
		
		int[] architecture = {input, hidden1, hidden2, output};
		String[] activations = {"","sigmoid","tanh",""};
		
		NeuralNetwork NN = new NeuralNetwork(architecture, activations);
		//NN.train(0.1, 500);
		NN.batchedTrain(0.1, 500, 1);
		NN.testAll();
	}
}