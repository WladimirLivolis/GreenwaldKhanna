import java.util.ArrayList;

public class GK {

	public static void greenwald_khanna(int n, int v, ArrayList<Tuple> s, double e) {

		// checks whether it is time to perform a compression
		if ((n % (1/(2*e))) == 0) {
			compress(n, s, e);
		}

		insert(n, v, s, e);

	}

	public static ArrayList<Integer> quantile(double phi, int n, ArrayList<Tuple> s, double e) {

		ArrayList<Integer> quantile = new ArrayList<Integer>(1);

		double en = e * n;
		int r = (int) Math.ceil(phi * (n - 1));

		int rmin = 0, rmax;
		for (Tuple t : s) {

			rmin += t.getG();
			rmax = rmin + t.getD();

			if (r - rmin <= en && rmax - r <= en) {
				quantile.add(t.getVal());
			}

		}

		return quantile;
	}

	private static void insert(int n, int v, ArrayList<Tuple> s, double e) {

		// finds the right position to insert v 
		int i = 0;
		while ( i < s.size() && s.get(i).getVal() <= v) {
			i++;
		}

		// calculates the proper value for delta
		int delta;
		if (n < 1/(2*e)) { // if less than 1/(2*e) observations were seen so far then delta = 0 
			delta = 0;
		} else if (i == 0 || i == s.size()) { // if v is the new minimum or maximum value then delta = 0
			delta = 0;
		} else { // else delta = floor(2en) - 1
			int p = (int) Math.floor(2*e*n);
			delta = p - 1;
		}

		// inserts new tuple to summary s
		s.add(i, new Tuple(v,1,delta));

	}

	private static void compress(int n, ArrayList<Tuple> s, double e) {

		// invokes a method that constructs array of bands such that bands[delta] is the band for the provided delta
		int[] bands = bands(n, e);

		for (int i = s.size() - 2; i >= 0; i--) {

			if (i > s.size() - 2) { continue; }

			Tuple t1 = s.get(i);
			Tuple t2 = s.get(i+1);

			if (bands[t1.getD()] <= bands[t2.getD()]) {

				int g_i_star = t1.getG();		

				int j = i;
				while (j >= 1 && bands[s.get(j-1).getD()] < bands[s.get(j).getD()]) {
					g_i_star += s.get(j-1).getG();
					j--;
				}

				if (g_i_star + t2.getG() + t2.getD() < 2*e*n) {
					int new_g = t2.getG() + g_i_star;
					t2.setG(new_g);
					for (int k = j; k <= i; k++) {
						s.remove(k);
					}
				}

			}

		}

	}

	// This method returns an array of bands such that bands[delta] is the band for the provided delta
	private static int[] bands(int n, double e) {

		int p = (int) Math.floor(2*e*n); // p = floor(2eN) by definition

		int[] bands = new int[p+1];

		int alpha_hat; // the last band
		if (p != 0) {
			alpha_hat = (int) Math.ceil(Math.log(p)/Math.log(2)); // alpha_hat is ceil(log2(p)) by definition
		} else {
			alpha_hat = 0;
		}

		bands[0] = alpha_hat; // delta = 0 is band alpha-hat by definition
		bands[p] = 0; // delta = p is band 0 by definition

		for (int alpha = 1; alpha <= alpha_hat; alpha++) { // alpha is a synonym for band

			int two_alpha_minus_1 = (int) Math.pow(2,alpha-1);
			int two_alpha = (int) Math.pow(2,alpha);

			int lower = p - two_alpha - (p % two_alpha);
			if (lower < 0) { lower = 0; }
			int upper = p - two_alpha_minus_1 - (p % two_alpha_minus_1);

			for (int delta = lower + 1; delta <= upper; delta++) {
				bands[delta] = alpha;
			}

		}

		return bands;

	}

}
