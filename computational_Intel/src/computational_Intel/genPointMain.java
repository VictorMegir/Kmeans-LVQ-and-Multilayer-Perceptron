package computational_Intel;

import java.io.IOException;

public class genPointMain {

	public static void main(String[] args) throws IOException 
	{		
        generatePoint genone=new generatePoint();
        genone.writeToFile();
        
        generatePointTwo gentwo=new generatePointTwo();
        gentwo.writeToFile();
        
        //LVQ lvq =new LVQ();
        //lvq.PointsFromFile();
	}

}
