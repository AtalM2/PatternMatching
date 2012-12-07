package patternMatching;

import java.util.ArrayList;
import java.util.HashMap;

public class FJS<T> {

	private ArrayList<T> alphabet;

	public FJS(ArrayList<T> alphabet) {
		this.alphabet = alphabet;
	}

	public int find(ArrayList<T> pattern, ArrayList<T> text) {
		int m = pattern.size();
		int n = text.size();
		int i;
		int i2;
		int j;
		int m2;
		ArrayList<Integer> B2 = defineB2(pattern);
		HashMap<T, Integer> delta = defineDelta(pattern);

		if (m < 1) {
			return 0;
		}

		i = 0;
		i2 = m;
		j = 1;
		m2 = m - 1;

		while (i2 < n) {
			// BM (Sunday) shift if p[m] fails to match
			if (pattern.get(m) != text.get(i2)) {
				do {
					i2 = i2 + delta.get(text.get(i2 + 1));
					if (i2 > n) {
						return 0;
					}
				} while (pattern.get(m) == text.get(i2));
				j = 1;
			}

			// KMP matching if p[m] matches
			if (j <= 1) {
				i = i2 - m2;
				j = 1;
			}
			while (j < m && text.get(i) == pattern.get(j)) {
				i = i + 1;
				j = j + 1;
			}

			// Restore invariant i = i+m−j for next shift
			if (j == m) {
				i = i + 1;
				j = j + 1;
				System.out.println(i - m);
			}
			j = B2.get(j);
			i2 = i + m - j;
		}

		return 0;
	}

	private ArrayList<Integer> defineB(ArrayList<T> pattern) {
		ArrayList<Integer> B = new ArrayList<>(pattern.size() + 1);
		int length;
		ArrayList<?> border;
		B.set(0, 0);
		for (int i = 1; i < B.size(); i++) {
			border = Util.border((ArrayList<T>) (pattern.subList(1, i - 1)));
			length = border.size() + 1;
			B.set(i, length);
		}

		return B;
	}

	private ArrayList<Integer> defineB2(ArrayList<T> pattern) {
		ArrayList<Integer> B = defineB(pattern);
		ArrayList<Integer> B2 = new ArrayList<>(pattern.size() + 1);
		ArrayList<?> border;
		int m = pattern.size();
		int j;

		for (int i = 0; i < B2.size(); i++) {
			if (i == m + 1) {
				B2.set(i, B.get(i));
			} else {
				border = Util.border((ArrayList<T>) (pattern.subList(1, i - 1)));
				j = border.size() + 1;
				if (pattern.get(j) != pattern.get(i)) {
					B2.set(i, 0);
				}
			}
		}

		return B2;
	}

	private HashMap<T, Integer> defineDelta(ArrayList<T> pattern) {
		HashMap<T, Integer> delta = new HashMap<>();
		int alphaSize = alphabet.size();
		int m = pattern.size();
		T a;

		for (int i = 0; i < alphaSize; i++) {
			delta.put(alphabet.get(i), -1);
		}

		for (int i = 0; i < m; i++) {
			a = pattern.get(i);
			delta.put(a, i);
		}

		return delta;
	}
}
