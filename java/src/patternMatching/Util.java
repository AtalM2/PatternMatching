package patternMatching;

import java.util.ArrayList;

public class Util {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<?> border(ArrayList<?> pattern) {
		ArrayList<?> border = new ArrayList();
		ArrayList<?> prefix = new ArrayList();
		ArrayList<?> suffix = new ArrayList();
		int i = 1;
		
		while (i < pattern.size()) {
			prefix = new ArrayList(pattern.subList(0, i));
			suffix = new ArrayList(pattern.subList(pattern.size() - i, pattern.size()));
			System.out.println(prefix + " : " + suffix + " -> " + arrayEquals(prefix, suffix));
			if (arrayEquals(prefix, suffix)) {
				System.out.println("Attribution au bord");
				border = new ArrayList(pattern.subList(0, i));
			}
			i++;
		}
		return border;
	}
	
	private static boolean arrayEquals(ArrayList<?> array1, ArrayList<?> array2) {
		boolean isEquals = true;
		int i = 0;
		int size = array1.size();
		if (size == array2.size()) {
			while(isEquals && i < size) {
				if (array1.get(i).equals(array2.get(i))) {
					i++;
				} else {
					isEquals = false;
				}
			}
		} else {
			return false;
		}
		return isEquals;
	}
}
