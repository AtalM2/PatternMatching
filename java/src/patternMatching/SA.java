package patternMatching;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Noémi Salaün <noemi.salaun@etu.univ-nantes.fr>
 */
public class SA<T> {

	private ArrayList<T> alphabet;

	public SA(ArrayList<T> alphabet) {
		this.alphabet = alphabet;
	}

	public void find(ArrayList<T> text, ArrayList<T> pattern) {
		int n = text.size();
		int m = pattern.size();
		HashMap<T, Integer> d = new HashMap<>();
		int k, j;
		ArrayList<T> x = new ArrayList<>();
		x.add(null);
		x.addAll(text);

		ArrayList<T> p = new ArrayList<>();
		p.add(null);
		p.addAll(pattern);

		// Preprocess
		for (T token : alphabet) {
			d.put(token, m);
		}
		for (j = 1; j < m; j++) {
			d.put(p.get(j), m - j);
		}

		// Search
		for (int i = m; i <= n; i += d.get(x.get(i))) {
			k = i;
			for (j = m; j > 0 && x.get(k).equals(p.get(j)); j--) {
				k--;
			}
			if (j == 0) {
				System.out.println("report match at position : " + (k + 1));
			}
		}
	}
}
