
import java.util.*;

// This class handles the Cell matrix
public class Minesweeper{
	public Cell[][] field;
	private int width=8;
	private int height=8;
	private int mines=10;
	private int clearAble = (width*height)-mines;   // the amount of clearable cells, used to check for win
	public GUI gui; // gets set and created from the main class
	private boolean first = true; // used to catch the first click to avoid it being a mine

	public Minesweeper() {
		createField();
	}

	/*
	 * creates a new game and repaints the gui accordingly
	 * gets called from gui class(gui class sets width height mines values before)
	 */
	public void newGame() 
	{
		first = true;
		createField();
		gui.repaint(width, height, mines);
	}



	/*
	 * If you have won, set every button to cleared and make gui changes
	 */
	public void win() {
		for (int i = 0; i<height; i++) {                  
			for (int j= 0; j<width; j++) {
				field[i][j].setIsCleared(true);
			}
		}
		gui.showWin();

	}

	/*
	 * If it is the first click, we genereate the mines and get their neighbouringminecounts fixed
	 * if a cell is to be shown, setIscleared, countdown the wincounter(cleareable) and make gui changes
	 * First time this method gets called, we dont know if the tile is a zero, so the zero will first be shown by this method, then again by the showzero method
	 * 
	 */
	public void show(int x, int y) {
		if(first) {
			first = false;
			generateMines(x, y);
			checkNearby();
			if(field[x][y].getNeighbouringMines() == 0) {
				clearAble++;
			}
		}

		field[x][y].setIsCleared(true);
		clearAble--;
		if (getClearAble() == 0) {
			gui.show(x, y);
			win();
		} else if (field[x][y].isMine()) {
			lose(x, y);
		} else {
			gui.show(x, y);
		}
	}



	/*
	 * If lose set every button to cleared
	 */
	public void lose(int x, int y) {
		for (int i = 0; i<height; i++) {                  
			for (int j= 0; j<width; j++) {
				field[i][j].setIsCleared(true);
			}
		}
		clearAble = -1;
		gui.showLose(x, y);
	}
	/*
	 * Creates the matrix Cell[][] according to height, width, mines
	 * resets clearAble(which is used to see if you have won)
	 */
	private void createField() {
		clearAble = (height*width)-mines;
		field = new Cell[height][width];
		for (int x = 0; x<height; x++) {                 
			for (int y= 0; y<width; y++) {
				field[x][y] = new Cell();
			}
		}

	}
	/* 
	 * Gets called after the first click by the show method
	 *  to ensure that the first click is not a mine
	 * 
	 */

	private void generateMines(int x,int y) {  
		Random random = new Random();
		for (int i = 0; i<mines; i++) {
			int random1 = random.nextInt(height);
			int random2 = random.nextInt(width);
			if ((field[random1][random2].isMine() == false) && !((random1==x) & (random2==y))) {
				field[random1][random2].setMine();
			} else {
				i--;
			}
		}
	}

	/*
	 * For every mine we find, increment neighbouringminecount for all the bordering cells
	 * the bordering cells are different depending on wether the current cell is close to any walls
	 */
	private void checkNearby() {
		for (int x = 0; x<height; x++) {
			for (int y = 0; y<width; y++) {
				if(field[x][y].isMine()) {
					if (y != width-1)  { // East
						field[x][y+1].addNeighbouringMine();
					}
					if (y != 0) {// West 
						field[x][y-1].addNeighbouringMine();
					}
					if (x != 0) {// North
						field[x-1][y].addNeighbouringMine();
					}
					if (x != height-1) {// South
						field[x+1][y].addNeighbouringMine();
					}
					if (y != width-1 && x != 0) {// Northeast
						field[x-1][y+1].addNeighbouringMine();
					}
					if (x != 0 && y != 0) {// Northwest
						field[x-1][y-1].addNeighbouringMine();
					}
					if (x != height-1 && y != 0) {// Southwest
						field[x+1][y-1].addNeighbouringMine();
					}
					if (x != height-1 && y != width-1) {// Southeast
						field[x+1][y+1].addNeighbouringMine();
					}
				}
			}
		}
	}

	/*
	 * If the clicked cell has no bordering mines(neighbouringminecount=0)
	 * show the surrounding mines, if one of the surrounding mines is a 0, use recursion
	 */
	public void clearZeros(int x, int y) {
		show(x, y);
		if (y != width-1) {// East
			if (field[x][y+1].getNeighbouringMines() != 0 && field[x][y+1].isCleared() == false) {
				show(x,y+1);
			} else 
				if (field[x][y+1].getNeighbouringMines() == 0 && field[x][y+1].isCleared() == false) {
					clearZeros(x,y+1);
				}
		}
		if (y != 0) {// West
			if (field[x][y-1].getNeighbouringMines() != 0 && field[x][y-1].isCleared() == false ) {
				show(x,y-1);
			}
			else 
				if (field[x][y-1].getNeighbouringMines() == 0 && field[x][y-1].isCleared() == false) {
					clearZeros(x,y-1);
				}
		}
		if (x != 0) {// North
			if (field[x-1][y].getNeighbouringMines() != 0 && field[x-1][y].isCleared() == false) {
				show(x-1,y);
			}
			else 
				if (field[x-1][y].getNeighbouringMines() == 0 && field[x-1][y].isCleared() == false) {
					clearZeros(x-1,y);
				}
		}
		if (x != height-1) {// South
			if (field[x+1][y].getNeighbouringMines() != 0 && field[x+1][y].isCleared() == false) {
				show(x+1,y);
			}
			else 
				if (field[x+1][y].getNeighbouringMines() == 0 && field[x+1][y].isCleared() == false) {
					clearZeros(x+1,y);
				}
		}
		if (y != width-1 && x != 0) {// Northeast
			if (field[x-1][y+1].getNeighbouringMines() != 0 && field[x-1][y+1].isCleared() == false) {
				show(x-1,y+1);
			}
			else 
				if (field[x-1][y+1].getNeighbouringMines() == 0 && field[x-1][y+1].isCleared() == false) {
					clearZeros(x-1,y+1);
				}
		}
		if (x != 0 && y != 0) {// Northeast
			if (field[x-1][y-1].getNeighbouringMines() != 0 && field[x-1][y-1].isCleared() == false) {
				show(x-1,y-1);
			}
			else 
				if (field[x-1][y-1].getNeighbouringMines() == 0 && field[x-1][y-1].isCleared() == false) {
					clearZeros(x-1,y-1);

				}
		}
		if (x != height-1 && y != 0) {// Southwest
			if (field[x+1][y-1].getNeighbouringMines() != 0 && field[x+1][y-1].isCleared() == false) {
				show(x+1,y-1);
			}
			else 
				if (field[x+1][y-1].getNeighbouringMines() == 0 && field[x+1][y-1].isCleared() == false) {
					clearZeros(x+1,y-1);
				}
		}
		if (x != height-1 && y != width-1) {// Southeast
			if (field[x+1][y+1].getNeighbouringMines() != 0 && field[x+1][y+1].isCleared() == false ) {
				show(x+1,y+1);
			}
			else 
				if (field[x+1][y+1].getNeighbouringMines() == 0 && field[x+1][y+1].isCleared() == false) {
					clearZeros(x+1,y+1);

				}
		}
	}

	public void setMines(int mines) {
		this.mines = mines;
	}

	public void setWidth(int width) {
		this.width = width;

	}
	public void setHeight(int height) {
		this.height = height;
	}

	public int getClearAble() {
		return clearAble;
	}

	public void setClearAble(int clearAble) {
		this.clearAble = clearAble;
	}

	public boolean getFirst() {
		return first;
	}
}
