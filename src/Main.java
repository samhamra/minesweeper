
/* This is the application class, sets up the Mainsweeper and GUI class*/
public class Main {
	
	
	
	
	public Main() {
		
	}
	public static void main(String[] args) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); // swing bug
		Minesweeper minesweeper = new Minesweeper();
		minesweeper.gui = new GUI(minesweeper);
	}
}