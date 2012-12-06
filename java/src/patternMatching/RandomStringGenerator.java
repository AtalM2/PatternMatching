package patternMatching;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomStringGenerator {
	
	private String filePath;
	private int patternLength;
	private int alphabetSize;
	
	public RandomStringGenerator(int patternLength, int alphabetSize, String fileName) {
		this.filePath = System.getProperty("user.dir") + "/"+ fileName;
		this.patternLength = patternLength;
		this.alphabetSize = alphabetSize;
	}
	
	public String generate() {
		String ret = "";
		String text = "";
		Random random = new Random();
		try {
			FileWriter fw = new FileWriter(filePath, false);
			BufferedWriter output = new BufferedWriter(fw);
			for (int i=0 ; i<patternLength ; i++) {
				text = String.valueOf(random.nextInt(alphabetSize));
				text = (i==patternLength-1) ? text : text + " ";
				output.write(text);
			}
			output.close();
			ret = "Success !";
		} catch(IOException ioe) {
			ret = "Error : " + ioe.getMessage();
		}
		return ret;
	}
}
