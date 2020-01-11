package computational_Intel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class generatePointTwo {
	File training_file= new File("trainingProblem2.txt");
	
	
	public generatePointTwo() throws IOException {
		if (training_file.createNewFile())
		{
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}
	}
	
	public void writeToFile() throws IOException {
		double x,y,theta,distance;
		FileWriter training_writer = new FileWriter(training_file);
		Random rnd=new Random();
		
		for (int i=0; i <100; i++) {
			//  (0,0) radius=0.3
			distance = rnd.nextDouble()*0.3;
			theta= (2*Math.PI)*rnd.nextDouble();  //[0,360] moires to theta
			x=distance *Math.cos(theta);
			y=distance*Math.sin(theta);
			training_writer.write(x + " " + y + " " +"\n");
		}
		
		for (int i=0; i <100; i++) {
			// [-1.1,-0.5] x [0.5,1.1]
			x=-1.1 + (-0.5 +1.1)*rnd.nextDouble();
			y=0.5 + (1.1 - 0.5)*rnd.nextDouble();
			training_writer.write(x + " " + y + " " +"\n");
		}
		
		for (int i=0; i <100; i++) {
			// [-1.1,-0.5] x [-1.1,-0.5]
			x=-1.1 + (-0.5 +1.1)*rnd.nextDouble();
			y=-1.1 + (-0.5 +1.1)*rnd.nextDouble();
			training_writer.write(x + " " + y + " " +"\n");
		}
		
		for (int i=0; i <100; i++) {
			// [0.5,1.1] x [-1.1,-0.5]
			x=0.5 + (1.1 - 0.5)*rnd.nextDouble();
			y=-1.1 + (-0.5 +1.1)*rnd.nextDouble();
			training_writer.write(x + " " + y + " " +"\n");
		}
		
		for (int i=0; i <100; i++) {
			// [0.5,1.1] x [0.5,1.1]
			x=0.5 + (1.1 - 0.5)*rnd.nextDouble();
			y=0.5 + (1.1 - 0.5)*rnd.nextDouble();
			training_writer.write(x + " " + y + " " +"\n");
		}
		
		for (int i=0; i <100; i++) {
			// [-1,1]x[-1,1]
			x=-1 + (1 +1)*rnd.nextDouble();
			y=-1 + (1 +1)*rnd.nextDouble();
			
			training_writer.write(x + " " + y + " " +"\n");
		}
		
		
		//writer.write("Test data");
		training_writer.close();
	}
	
}

