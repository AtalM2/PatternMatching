package patternMatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FJS<T> {

	private ArrayList<T> alphabet;

	public FJS(ArrayList<T> alphabet) {
		this.alphabet = alphabet;
	}

	public int find(ArrayList<T> text, ArrayList<T> pattern) {
		ArrayList<T> x = new ArrayList<>();
		x.add(null);
		x.addAll(text);

		ArrayList<T> p = new ArrayList<>();
		p.add(null);
		p.addAll(pattern);

		ArrayList<Integer> B2 = new ArrayList<>();
		B2.add(null);
		B2.addAll(defineB2(pattern));

		System.out.println(x);
		System.out.println(p);
		System.out.println(B2);

		int m = pattern.size();
		int n = text.size();
		int i;
		int i2;
		int j;
		int m2;
		HashMap<T, Integer> delta = defineDelta(pattern);
		System.out.println(delta);

		if (m < 1) {
			return 0;
		}

		i = 1;
		i2 = m;
		j = 1;
		m2 = m - 1;

		while (i2 <= n) {
			System.out.println("i2 : " + i2 + "  -  n : " + n);
			// BM (Sunday) shift if p[m] fails to match
			if (p.get(m) != x.get(i2)) {
				do {
					i2 = i2 + delta.get(x.get(i2 + 1));
					if (i2 > n) {
						return 0;
					}
					System.out.println("pattern : " + p.get(m) + "  -  text : " + x.get(i2));
				} while (p.get(m) == x.get(i2));
				j = 1;
			}

			// KMP matching if p[m] matches
			if (j <= 1) {
				i = i2 - m2;
				j = 1;
			}
			while (j < m && x.get(i) == p.get(j)) {
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
		ArrayList<Integer> B = new ArrayList<>();
		int m = pattern.size();
		int length;
		List list;
		ArrayList border;
		B.add(0);
		for (int j = 2; j <= m + 1; j++) {
			list = pattern.subList(0, j-1);
			border = Util.border(new ArrayList(list));
			length = border.size() + 1;
			B.add(length);
		}
		System.out.println("B : " + B);
		return B;
	}

	private ArrayList<Integer> defineB2(ArrayList<T> pattern) {
		ArrayList<Integer> B = defineB(pattern);
		ArrayList<Integer> B2 = new ArrayList<>();

		int m = pattern.size();
		int j2;

		for (int j = 0; j < m; j++) {
			j2 = B.get(j);
			if (pattern.get(j2) != pattern.get(j)) {
				j2 = 0;
			}
			B2.add(j2);
		}
		B2.add(B.get(m));
		System.out.println("B2 : " + B2);
		return B2;
	}

	private HashMap<T, Integer> defineDelta(ArrayList<T> pattern) {
		HashMap<T, Integer> delta = new HashMap<>();
		ArrayList<T> p = new ArrayList<>();
		p.add(null);
		p.addAll(pattern);

		int j2;
		int m = pattern.size();
		for (T token : alphabet) {
			j2 = p.lastIndexOf(token);
			if (j2 == -1) {
				delta.put(token, 0);
			} else {
				delta.put(token, m - j2 + 1);
			}
		}
		return delta;
	}
}
