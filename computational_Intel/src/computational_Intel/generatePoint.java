package computational_Intel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class generatePoint {
	File training_file= new File("training.txt");
	File testing_file= new File("testing.txt");
	
	
	public generatePoint() throws IOException {
		if (training_file.createNewFile() || testing_file.createNewFile())
		{
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}
	}
	
	public void writeToFile() throws IOException {
		double x,y;
		FileWriter training_writer = new FileWriter(training_file);
		FileWriter testing_writer = new FileWriter(testing_file);
		Random rnd=new Random();
		for (int i=0; i <3000; i++) {
			//randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			x=-2 + (rnd.nextDouble() * ((2+2)));  
			y=-2 + (rnd.nextDouble() * ((2+2))); 
			if((x-1)*(x-1) + (y-1)*(y-1)<=0.49) {
				if(rnd.nextInt(10)==0) {  // probability 1/10 to change category to C1
					training_writer.write(x + " " + y + " "+ "1" +"\n");
				}else {
					training_writer.write(x + " " + y + " "+ "2" +"\n");
				}
				
			}else if ((x+1)*(x+1) + (y+1)*(y+1)<=0.49) {
				if(rnd.nextInt(10)==0) {
					training_writer.write(x + " " + y + " "+ "1" +"\n");
				}else {
				    training_writer.write(x + " " + y + " "+ "2"+"\n");
				}
			}else if((x+1)*(x+1) + (y-1)*(y-1)<=0.49) {
				if(rnd.nextInt(10)==0) {
					training_writer.write(x + " " + y + " "+ "1" +"\n");
				}else {
				    training_writer.write(x + " " + y + " "+ "3"+"\n");
				}
			}else if ((x-1)*(x-1) + (y+1)*(y+1)<=0.49) {
				if(rnd.nextInt(10)==0) {
					training_writer.write(x + " " + y + " "+ "1" +"\n");
				}else {
				    training_writer.write(x + " " + y + " "+ "3"+"\n");
				}
			}else {
				training_writer.write(x + " " + y + " "+ "1"+"\n");
			}
		}
		
		for (int i=0; i <3000; i++) {
			x=-2 + (rnd.nextDouble() * ((2+2)));  
			y=-2 + (rnd.nextDouble() * ((2+2))); 
			if((x-1)*(x-1) + (y-1)*(y-1)<=0.49) {
				testing_writer.write(x + " " + y + " "+ "2"+"\n" );
			}else if ((x+1)*(x+1) + (y+1)*(y+1)<=0.49) {
				testing_writer.write(x + " " + y + " "+ "2"+"\n");
			}else if((x+1)*(x+1) + (y-1)*(y-1)<=0.49) {
				testing_writer.write(x + " " + y + " "+ "3"+"\n");
			}else if ((x-1)*(x-1) + (y+1)*(y+1)<=0.49) {
				testing_writer.write(x + " " + y + " "+ "3"+"\n");
			}else {
				testing_writer.write(x + " " + y + " "+ "1"+"\n");
			}
		}
		
		
		//writer.write("Test data");
		training_writer.close();
		testing_writer.close();
	}
	
}
