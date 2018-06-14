import java.util.ArrayList;

public class Block {

	private ArrayList<Tuple> summary;

	private int bstart;
	private int numObs;

	public Block(int bstart) {
		summary = new ArrayList<Tuple>();
		numObs = 0;
		this.bstart = bstart;
	}

	public int bstart() {
		return bstart;
	}

	public int numObs() {
		return numObs;
	}

	public void incrNumObs() {
		numObs++;
	}
	
	public ArrayList<Tuple> summary() {
		return summary;
	}

}
