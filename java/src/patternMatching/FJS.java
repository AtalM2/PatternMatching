package patternMatching;

import java.util.ArrayList;

public class FJS<T> {
	/*public int find(ArrayList<T> pattern, ArrayList<T> text) {
		int m = pattern.size();
		int n = text.size();
		int i;
		int i2;
		int j;
		int m2;
		ArrayList<T> B = new ArrayList<T>();
		ArrayList<T> B2 = new ArrayList<T>();
		ArrayList<Integer> delta = new ArrayList<Integer>();
		
		if (m < 1) {
			return 0;
		}
		
		i = 0;
		i2 = m;
		j = 1;
		m2 = m-1;
		
		while (i2 < n) {
			// BM (Sunday) shift if p[m] fails to match
			if (pattern.get(m) != text.get(i2)) {
				do {
					i2 = i2 + delta.get(text.get(i2+1));
					if (i2 > n) {
						return 0;
					}
				} while (pattern.get(m) == text.get(i2));
				j = 1;
			}
			
			// KMP matching if p[m] matches
			if (j<= 1) {
				i = i2 - m2;
				j = 1;
			}
			while (j < m && text.get(i) == pattern.get(j)) {
				i = i+1;
				j = j+1;
			}
			
			// Restore invariant i = i+m−j for next shift
			if (j == m) {
				i = i+1;
				j = j+1;
				System.out.println(i-m);
			}
			j = B2.get(j);
			i2 = i + m - j;
		}
				
		return 0;
	}*/
	
	private ArrayList<Integer> defineB(ArrayList<T> pattern) {
		ArrayList<Integer> B = new ArrayList<Integer>(pattern.size()+1);
		
		B.set(0, 0);
		for (int i=1 ; i<B.size() ; i++) {
			B.set(i, 0);
		}
		
		return B;
	}
}
