import java.util.ArrayList;

public class GKWindow {

	public static void greenwald_khanna_window(int n, int v, int w, double e, ArrayList<Block> blockList) {

		// calculates window start & end positions
		int w_min, w_max = n;
		if (n < w) {
			w_min = 0;
		} else {
			w_min = w_max - w + 1;
		}

		int block_size = (int) (w*e/2);

		// checks whether its time to create another block
		if (n % block_size == 0) {
			blockList.add(new Block(n));
		}

		// checks whether its time to remove the oldest block
		if (blockList.get(0).bstart() < w_min) {
			blockList.remove(0);
		}

		Block under_construction = blockList.get(blockList.size()-1);

		GK.greenwald_khanna(under_construction.numObs(), v, under_construction.summary(), e/2);

		under_construction.incrNumObs();

	}

	public static ArrayList<Integer> quantile(double phi, int w, double e, ArrayList<Block> blist) {

		ArrayList<Tuple> summary = blist.get(0).summary();

		for (int i = 1; i < blist.size(); i++) {

			summary = merge(summary, blist.get(i).summary());

		}

		ArrayList<Integer> quantiles = GK.quantile(phi, w, summary, e);

		return quantiles;
	}

	public static ArrayList<Tuple> merge(ArrayList<Tuple> s1, ArrayList<Tuple> s2) {
		
		int i = 0, j = 0;

		ArrayList<Tuple> s = new ArrayList<Tuple>();

		while (i < s1.size() && j < s2.size()) {
			if (s1.get(i).getVal() < s2.get(j).getVal()) {
				_merge(s, s1, s2, i, j, s.size());
				i++;
			} else {
				_merge(s, s2, s1, j, i, s.size());
				j++;
			}
		}

		while (i < s1.size()) {
			_merge(s, s1, s2, i, j, s.size());
			i++;
		}

		while (j < s2.size()) {
			_merge(s, s2, s1, j, i, s.size());
			j++;
		}

		return s; 

	}

	private static void _merge(ArrayList<Tuple> s, ArrayList<Tuple> s1, ArrayList<Tuple> s2, int i, int j, int k) {

		int rmin, rmax, g, d;

		Tuple xr = s1.get(i), ys, yt;
		updateRanks(i,s1);

		/* ys is the largest element in S2 that is smaller than xr */
		if (j > 0) {
			ys = s2.get(j-1);
			updateRanks(j-1,s2);
		} else {
			ys = null;
		}

		/* yt is the smallest element in S2 that is larger than xr */
		if (j < s2.size()) {
			yt = s2.get(j);
			updateRanks(j,s2);
		} else {
			yt = null;
		}

		if (ys == null) {
			rmin = xr.getRmin();
		} else {
			rmin = xr.getRmin() + ys.getRmin();
		} if (yt == null) {
			rmax = xr.getRmax() + ys.getRmax();
		} else {
			rmax = xr.getRmax() + yt.getRmax() - 1;
		}

		/* Optimization: value merging */
		if (k > 0) {
			if (xr.getVal() == s.get(k-1).getVal()) {
				rmin = s.get(k-1).getRmin();
				k--;
				s.remove(k);
			}
		}

		if (k > 0) {
			g = rmin - s.get(k-1).getRmin();
		} else {
			g = rmin;
		}
		d = rmax - rmin;

		Tuple t = new Tuple(xr.getVal(),g,d);
		t.setRmin(rmin);
		t.setRmax(rmax);
		s.add(k, t);
	}
	
	private static void updateRanks(int pos, ArrayList<Tuple> s) {
		Tuple t = s.get(pos);
		int rmin, rmax;
		if (pos == 0) {
			rmin = t.getG();
		} else {
			rmin = t.getG() + s.get(pos-1).getRmin();
		}
		rmax = t.getD() + rmin;
		t.setRmin(rmin);
		t.setRmax(rmax);
	}

}
