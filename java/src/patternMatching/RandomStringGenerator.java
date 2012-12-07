package patternMatching;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class RandomStringGenerator {

	private ArrayList<Integer> alphabet;

	public RandomStringGenerator(int alphabetSize) {
		alphabet = new ArrayList<>();
		for (int token = 0; token < alphabetSize; token++) {
			alphabet.add(token);
		}
	}

	public ArrayList<Integer> generate(int textLength, String filePath) throws IOException {
		int alphabetSize = getAlphabet().size();
		ArrayList<Integer> text = new ArrayList<>();
		Random random = new Random();
		int token;
		if (filePath == null) {
			for (int i = 0; i < textLength; i++) {
				token = random.nextInt(alphabetSize);
				text.add(token);
			}
		} else {
			String stringToken;
			FileWriter fw = new FileWriter(System.getProperty("user.dir") + "/" + filePath, false);
			try (BufferedWriter output = new BufferedWriter(fw)) {
				for (int i = 0; i < textLength; i++) {
					token = random.nextInt(alphabetSize);
					text.add(token);
					stringToken = (i == textLength - 1) ? String.valueOf(token) : token + " ";
					output.write(stringToken);
				}
			}
		}
		return text;
	}

	public ArrayList<Integer> generate(int textLength) {
		ArrayList<Integer> text = new ArrayList<>();
		try {
			text = generate(textLength, null);
		} catch (IOException ex) {
			// L'exception ne peut pas être déclanché si on ne demande pas l'écriture dans un fichier
		}
		return text;
	}

	public ArrayList<Integer> getAlphabet() {
		return alphabet;
	}
}
