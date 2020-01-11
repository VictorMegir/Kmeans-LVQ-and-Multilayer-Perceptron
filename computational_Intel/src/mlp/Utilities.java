package mlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Utilities
{
	private int a = 1;
	private Scanner scanner;
	
	private int length = 3000;
	private double[][] allInputs = new double[length][2];
	private double[][] allOutputs = new double[length][3];
	
	public void readExamplesFromFile(String fileName) throws FileNotFoundException
	{
		File file = new File(fileName);
        scanner = new Scanner(file); 
        int i = 0;
        
        System.out.println("Reading from file: " + fileName);
        while (scanner.hasNext())
        {        	
            String word1 = scanner.next();
            String word2 = scanner.next();
            String team = scanner.next();
            
            double[] input = new double[2];
            input[0] = Double.valueOf(word1);
            input[1] = Double.valueOf(word2);
            
            double[] output = new double[3];
            int teamNumber = Integer.valueOf(team);
            output[teamNumber-1] = 1.0;
            
            allInputs[i] = input;
            allOutputs[i] = output;
            i++;
        }
        System.out.println("Examples read: " + i);
	}
	
	public double squareError(double[][] output, double[][] target)
	{
		double sum = 0;
		for(int i = 0; i < output.length; i++) 
		{
			for(int j = 0; j < output[0].length; j++) {
				sum += Math.pow((target[i][j] - output[i][j]), 2);
			}	
		}
		return sum / 2;
	}
	
	public double[][] getInBatch(int BatchSize, int index)
	{
		int dim = allInputs[0].length;
		double[][] Batch = new double[BatchSize][dim];
		
		for(int i = 0; i < BatchSize; i++) {
			Batch[i] = allInputs[i + BatchSize * index];
		}
		return Batch;
	}
	
	public double[][] getTarBatch(int BatchSize, int index)
	{
		int dim = allOutputs[0].length;
		double[][] Batch = new double[BatchSize][dim];
		
		for(int i = 0; i < BatchSize; i++) {
			Batch[i] = allOutputs[i + BatchSize * index];
		}
		return Batch;
	}
	
	public double[][] getAllInputs() {
		return allInputs;
	}
	
	public double[][] getAllOutputs() {
		return allOutputs;
	}
		
	public void printDoubleArray(double[] array)
	{
		System.out.print("[");
		for(int i=0; i<array.length; i++) {
			System.out.print(array[i] + " ");
		}
		System.out.print("]"+"\n");
	}
	
	public double tanh(double x)
	{
	    double e1 = (double) Math.exp(a * x);
	    double e2 = (double) Math.exp((-a) * x);
	    return (e1 - e2)/(e1 + e2);
	}
	
	public double derTanh(double x) {
	    return (double) (1 - Math.pow(tanh(x),2));
	}
	
	public double sigmoid(double x) {
	    return (double) (1/(1+Math.exp(-a * x)));
	}
	
	public double derSigmoid(double x) {
	    double s = sigmoid(x);
	    return s*(1-s);
	}
	
	public double step(double x) {
		if(x>0)
			return 1.0;
		return -1.0;
	}

	public boolean equals(double[] real, double[] target) 
	{
		for(int i = 0; i < real.length; i++) {
			if((double)Math.round(real[i]) != target[i]) return false;
		}
		return true;
	}
}