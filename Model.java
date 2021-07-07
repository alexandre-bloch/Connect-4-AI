import java.util.Random;

/**
 * This file is to be completed by you.
 *
 * @author <Please enter your matriculation number, not your name>
 */
public final class Model
{
	////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// Fields ////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	private final int nrRows = 6;  /** Original Board Dimensions */
	private final int nrCols = 7;

	public int newRows;      /** New custom board dimensions */
	public int newCols;

	/////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// Board Setup ////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////

	public int[][] ourBoard = new int[nrRows][nrCols]; /** Initialise Original Board */

	/** Constructor - Sets original 6 by 7 board */
	public Model() {

		/** Initialise the board size to its default values. */

		for (int row = 0 ; row < nrRows ; row++) { /** Set board equal to zero for every piece */

			for (int col = 0 ; col < nrCols ; col++) {

				ourBoard[row][col] = 0;
			}
		}
	}

	/** Creates new board with input for dimensions and overwrites original board */
	public void newBoard(int nrRows, int nrCols) {

		this.ourBoard = new int[getNrRows()][getNrCols()];

		for (int row = 0 ; row < nrRows ; row++) {  /** Use input dimensions to fill board with zeros */

			for (int col = 0 ; col < nrCols ; col++) {

				ourBoard[row][col] = 0;
			}
		}
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// Input / Move Validation ////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////

	/** Gets how many rows we want the board to be */
	public int getRows(int row) {

		try {

			if (row < 4) return -1;  /** Don't accept values less than 4 */
			else return row;         /** If a value less than 4 (or non integer) is given, return -1 */

		} catch (Exception e) {

			return -1;
		}

	}

	/** Gets how many columns we want the board to be */
	public int getCols(int col) {

		try {

			if (col < 4) return -1; /** Don't accept values less than 4 */
			else return col;        /** If a value less than 4 (or non integer) is given, return -1 */

		} catch (Exception e) {

			return -1;
		}

	}

	/** Gets how many we need to connect in order to win */
	public int getConnect(int con) {

		try {

			if (con >= Math.min(getNrRows(),getNrCols() ) || con < 3) return -1;
			else return con;  /** Don't accept values larger than the number of rows or columns */

		} catch (Exception e) { /** Return -1 if something invalid is entered */

			return -1;
		}
	}

	/** Validates column for move from user input */
	public int getMove(int move) {

		try {

			if (move >= getNrCols() || move < -1) return -2;
			else return move;   /** If move does not fit within the dimensions of the board, return -1 */

		} catch(Exception e) {

			return -2;
		}
	}

	/**  Gets row of move based on the column selected by user */
	public int getRow(int column, int[][] ourBoard) {

		int row;

		for (int i = getNrRows() - 1 ; i >= 0 ; i-- ) {

			if(ourBoard[i][column] == 0) { /** Moves down the column until it finds the first empty slot */

				row = i;
				return row;
			}
		}

		row = -1; /** If there is no empty slot return -1 */
		return row;
	}

	/** Checks if move is valid, ie if the column is full or if the column doesn't exist */
	public boolean isMoveValid(int move) {
		if (move < 0 || move > getNrCols()) return false; /** Check that the move fits within the columns of the board */

		else if (ourBoard[0][move] != 0) return false; /** Check that the column is not full */

		else return true;
	}

	/** Randomly decide who starts */
	public int setPlayer(int input) {

		try {
			if (input == 1) {

				return 1;
			}
			if (input == 2) {

				return 2;
			} else if (input == 3) {

				int counter;
				Random rand = new Random();

				if (rand.nextInt(2) == 0) counter = 1; /** Randomly decide between 1 and 2 */
				else counter = 2;

				return counter;
			}
			else return -1;

		} catch (Exception e) {
			return -1;
		}
	}

	/** Get the characters to display on board for each player */
	public char getChar(char input, char counter) {

		try {

			return input;
		} catch (Exception e) {
			return counter;
		}
	}



	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// Check for Winner ////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

	/** Check for win in horizontal direction */
	public boolean hWinner(int connect, int counter, int[][] ourBoard) {

		int smallest = connect - 1;
		int hCounter = 0;

		for(int row = 0 ; row < getNrRows() ; row++ ) {

			hCounter = 0;   /** Reset counter when looking at new row */

			for (int col = 0 ; col < getNrCols() - smallest ; col++) {

				hCounter = 0; /** Reset counter when starting from a new column */

				for (int i = 0; i <= smallest; i++) {

					if (ourBoard[row][col + i] == counter) hCounter += 1; /** Checking if i in a row are equal */
					else hCounter = 0;
					if (hCounter == connect) return true;
				}
			}
		}
		return false;
	}

	/** Check for win in vertical direction */
	public boolean vWinner(int connect, int counter, int[][] ourBoard) {

		int smallest = connect - 1;
		int vCounter = 0;

		for (int col = 0 ; col < getNrCols() ; col++) {

			vCounter = 0; /** Reset counter when starting from a new column */

			for (int row = 0 ; row < getNrRows() - smallest ; row++) {

				vCounter = 0; /** Reset counter when looking at new row */

				for (int i = 0; i <= smallest; i++) {

					if (ourBoard[row + i][col] == counter) vCounter += 1; /** Checking if i in a row are equal */
					else vCounter = 0;
					if (vCounter == connect) return true;
				}
			}
		}
		return false;
	}

	/** Combine horizontal and vertical check */
	public boolean hvWinner(int connect, int counter, int[][] ourBoard) {

		return (hWinner(connect, counter, ourBoard) || vWinner(connect, counter, ourBoard));
	}

	/** Check for win in diagonal (forward direction) */
	public boolean aDiagonal(int connect, int counter, int[][]ourBoard) {

		int smallest = connect -1;
		int store = 0;

		for (int row = smallest; row < getNrRows(); row++) {

			store = 0;  /** Reset counter when starting from a new row */

			for (int col = 0; col < getNrCols() - smallest; col++) {

				for (int i = 0 ; i <= smallest ; i++)

				if (ourBoard[row - i][col + i] == counter) store += 1; /** Checking if i in a row are equal */
				else store = 0;
				if (store == connect) return true;
				}
			}
		return false;
		}

	/** Check for win in diagonal (backward direction) */
	public boolean bDiagonal(int connect, int counter, int[][] ourBoard) {
		int smallest = connect -1;
		int store = 0;

		for(int row = 0; row < getNrRows() - smallest; row++){

			store = 0; /** Reset counter when starting from a new row */

			for(int col = 0; col < getNrCols() - smallest; col++){

				for (int i = 0 ; i <= smallest ; i++)

					if (ourBoard[row + i][col + i] == counter) store += 1; /** Checking if i in a row are equal */
					if (store == connect) return true;
					else store = 0;
				}
			}
			return false;
		}

	/** Combine both diagonal checks */
	public boolean dWinner(int connect, int counter, int[][] ourBoard) {

		return (aDiagonal(connect, counter, ourBoard) || bDiagonal(connect, counter, ourBoard));
	}

	/** Combine all Checks into 1 method */
	public boolean winner (int connect, int counter, int[][] ourBoard) {

		return (dWinner(connect, counter, ourBoard) || hvWinner(connect, counter, ourBoard));
	}

	/** Check board is full for draw */
	public boolean boardFull (int[][] ourBoard) {

		int count = 0;

		for (int col = 0 ; col < getNrCols() ; col++) { /** Add to count if the top row of each column is non empty (!=0) */

			if (ourBoard[0][col] != 0) count += 1;
		}

		if (count == getNrCols()) return true; /** If each column is non empty at the top row, then the entire board is non-empty */
		return false;
	}

	/** Combine all winning checks and board full check */
	public boolean gameOver(int connect, int counter, int[][] ourBoard) {

		return (winner(connect,counter,ourBoard)) || (boardFull(ourBoard));
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// Restarting Game ////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////

	/** Decide whether to restart game */
	public boolean restartGame(String input, int[][] ourBoard) {

		if (input.equals("y") || input.equals("Y")) return true; /** Deal with capitalisation */

		else return false;
	}

	/** Decide on gamemode. Vs AI or Vs player or exit */
	public int playGameMode(int input){

		try {
			if (input == 3) return 3;
			if (input == 2) return 2;
			else if (input == 1) return 1;

		} catch (Exception e) { /** If invalid input, return -1 */

			return -1;
		}

		return -1;
	}

	/** If game is restarted, clear board */
	public void resetBoard(int a, int b) {

		for (int col = 0; col < a; col ++) {

			for (int row = 0; row < b; row++) {

				ourBoard[col][row] = 0; /** Fill board with zeros */
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// Getters and Setters ////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////

	public int getNrRows()
	{
		return newRows;
	}
	
	public int getNrCols()
	{
		return newCols;
	}

	public int[][] getBoard()
	{
		return ourBoard;
	}

	public void setBoard(int[][] ourBoard)
	{
		this.ourBoard = ourBoard;
	}

	public void setNewRows(int newRows) {
		this.newRows = newRows;
	}

	public int getNewRows() {
		return newRows;
	}

	public void setNewCols(int newCols) {
		this.newCols = newCols;
	}

	public int getNewCols() {
		return newCols;
	}



}
