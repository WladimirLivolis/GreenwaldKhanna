import java.util.ArrayList;

public class Main {
	
	public static void main(String[] args) {
		
		/*
		double e = 0.25;
		
		int n = 0;
		
		ArrayList<Tuple> summary = new ArrayList<Tuple>();
		
		int[] observations = { 12, 10, 11, 10, 1, 10, 11, 9 };
		
		for (Integer obs : observations) {
			GK.greenwald_khanna(n, obs, summary, e);
			n++;
		}
		
		for (Tuple t : summary) {
			System.out.println(t.toString());
		}
		
		ArrayList<Integer> quantile = GK.quantile(0.5, n, summary, e);
		
		System.out.println("");
		
		for (Integer q : quantile) {
			System.out.println(q);
		}
		*/
		
		/* ========================================================================= */
		
		/*
		
		System.out.println("");
		
		Tuple t11 = new Tuple(2,1,0);
        Tuple t12 = new Tuple(4,2,1);
        Tuple t13 = new Tuple(8,2,1);
        Tuple t14 = new Tuple(17,3,0);
        
        Tuple t21 = new Tuple(1,1,0);
        Tuple t22 = new Tuple(7,2,0);
        Tuple t23 = new Tuple(12,2,1);
        Tuple t24 = new Tuple(15,3,0);
        
        ArrayList<Tuple> s1 = new ArrayList<Tuple>();
        s1.add(0,t11);
        s1.add(1,t12);
        s1.add(2,t13);
        s1.add(3,t14);
        
        ArrayList<Tuple> s2 = new ArrayList<Tuple>();
        s2.add(0,t21);
        s2.add(1,t22);
        s2.add(2,t23);
        s2.add(3,t24);
        
        ArrayList<Tuple> s = GKWindow.merge(s1, s2);
        
        for (Tuple t : s) {
            System.out.println(t.toString());
        }
        
        */
        
		/* ========================================================================= */

		double e = 0.25;
		int n = 0;
		int w = 32;
		int blocks = (int) (2/e);
		
		ArrayList<Block> blist = new ArrayList<Block>(blocks);
		
		int[] observations = { 12, 10, 11, 10, 1, 10, 11, 9, 6, 7, 8, 11, 4, 5, 2, 3, 13, 19, 14, 15, 12, 16, 18, 17, 11, 1, 7, 13, 9, 10, 4, 8 };
		
		for (Integer obs : observations) {
			GKWindow.greenwald_khanna_window(n, obs, w, e, blist);
			n++;
		}
		
		ArrayList<Tuple> summary = blist.get(0).summary;
		
		for (Tuple t : summary) {
			System.out.println(t.toString());
		}
		
		for (int i = 1; i < blist.size(); i++) {
			
			System.out.println("");
			for (Tuple t : blist.get(i).summary) {
				System.out.println(t.toString());
			}
			
			summary = GKWindow.merge(summary, blist.get(i).summary);
		}

		System.out.println("");
		
		for (Tuple t : summary) {
			System.out.println(t.toString());
		}
		
		System.out.println("");
		
		ArrayList<Integer> quantile = GKWindow.quantile(0.5, w, e, blist);
		
		for (Integer q : quantile) {
			System.out.println(q);
		}
		
		
		// ====
		
		System.out.println("");
		
		ArrayList<Tuple> s = new ArrayList<Tuple>();
		n = 0;
		for (Integer obs : observations) {
			GK.greenwald_khanna(n, obs, s, e);
			n++;
		}
		
		for (Tuple t : s) {
			System.out.println(t.toString());
		}
		
		ArrayList<Integer> quant = GK.quantile(0.5, n, s, e);
		
		System.out.println("");
		
		for (Integer q : quant) {
			System.out.println(q);
		}

	}

}
