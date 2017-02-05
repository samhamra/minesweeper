

public class Cell {
	private int neighbouringMines = 0; /* Amount of neighbouring mines */
	private boolean isMine = false; /* Whether the cell is a mine or not*/
	private boolean isFlagged = false;
	private boolean isCleared = false; /* Whether the cell has been cleared or not */

	public Cell() {
	
	}
	
	public int getNeighbouringMines(){
		return neighbouringMines;
	}
	public void addNeighbouringMine() {
		neighbouringMines++;
	}
	public boolean isMine(){
		return isMine;
	}
	public void setMine(){
		isMine = true;
	}
	public boolean isCleared(){
		return isCleared;
	}
	public void setIsCleared(boolean b) {
		isCleared = b;
	}
	public boolean isFlagged(){
		return isFlagged;
	}
	public void setIsFlagged(boolean val) {
		isFlagged = val;
	}
}

