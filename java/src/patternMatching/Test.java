package patternMatching;

import java.util.ArrayList;

public class Test {

	public static void main(String[] args) {
		RandomStringGenerator randGen = new RandomStringGenerator(3);
		ArrayList<Integer> alphabet = randGen.getAlphabet();
		ArrayList<Integer> text = randGen.generate(10);
		ArrayList<Integer> pattern = randGen.generate(3);
		System.out.println(text);
		System.out.println(pattern);
		FJS fjs = new FJS(alphabet);
		fjs.find(text, pattern);
	}
}
