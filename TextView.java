/**
 * This file is to be completed by you.
 *
 * @author <Please enter your matriculation number, not your name>
 */
public final class TextView
{
	public TextView()
	{
	
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////// Game Messages ////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////

	/** The following are all messages that the game will show at different stages */



	public final void displaySettingsMessage() {

		System.out.println("  ___         _     _     _                    ");
		System.out.println(" / __|  ___  | |_  | |_  (_)  _ _    __ _   ___");
		System.out.println(" \\__ \\ / -_) |  _| |  _| | | | ' \\  / _` | (_-<");
		System.out.println(" |___/ \\___|  \\__|  \\__| |_| |_||_| \\__, | /__/");
		System.out.println("                                    |___/ ");

		System.out.println();
		System.out.println("You must pick at least 4 rows.");
		System.out.println("You must pick at least 4 columns.");
		System.out.println("You must pick at least 3 pieces to connect.");
		System.out.println("This number must also fit on the board.");



	}

	public final void displayStartMessage() {

		System.out.println();
		System.out.println("  ___   _                  _       _   _ ");
		System.out.println(" / __| | |_   __ _   _ _  | |_    | | | |");
		System.out.println(" \\__ \\ |  _| / _` | | '_| |  _|   |_| |_|");
		System.out.println(" |___/  \\__| \\__,_| |_|    \\__|   (_) (_)");
		System.out.println();
	}

	public final void displayPlayerTurn(int counter) {

		System.out.println("Player " + counter +"'s go ");
	}

	public final void displayInvalidMove() {

		System.out.println("Not a valid move, try again.");
	}

	public final void displayPlayerWins(int counter) {

		System.out.println("Player " + counter +" wins !!");
		System.out.println();
	}

	public final void displayThanks() {

		System.out.println();
		System.out.println("Thanks for playing!");
	}

	public final void displayDraw() {

		System.out.println("Board is full. The game is drawn!");
		System.out.println();
	}

	public final void displayRowError() {

		System.out.println();
		System.out.println("Not a valid number of rows, try again");
	}

	public final void displayConnectError() {

		System.out.println();
		System.out.println("Not a valid number of pieces to connect, try again");
	}

	public final void displayColError() {

		System.out.println();
		System.out.println("Not a valid number of columns, try again");
	}

	public final void displayInvalidOption() {

		System.out.println();
		System.out.println("Sorry this is not a valid option. Try again: ");
		System.out.println();
	}

	public final void displayAICounter(){

		System.out.println();
		System.out.println("The AI will play with the counter '2'.");
		System.out.println();
	}

	 public final void displayTitle() {

		 System.out.println();
		 System.out.println(" a88888b.                                                dP               ");
		 System.out.println("d8'   `88                                                88               ");
		 System.out.println("88        .d8888b. 88d888b. 88d888b. .d8888b. .d8888b. d8888P    dP.  .dP ");
		 System.out.println("88        88'  `88 88'  `88 88'  `88 88ooood8 88'  `\"\"   88       `8bd8'  ");
		 System.out.println("Y8.   .88 88.  .88 88    88 88    88 88.  ... 88.  ...   88       .d88b.  ");
		 System.out.println(" Y88888P' `88888P' dP    dP dP    dP `88888P' `88888P'   dP      dP'  `dP ");
		 System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
	 	 System.out.println();
	}

	public final void displayLogOff() {

		System.out.println();
		System.out.println("Logging off ...");
		System.out.println();
	}

	public final void displayConcede(int counter) {

		System.out.println();
		System.out.println("Player " + counter + " concedes !");
		System.out.println();
	}


	/////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// User Input ///////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////

	/** The following all ask the user to give some input */

	/** Ask if we want to play against the AI or a friend or quit */
	public final int askForGameMode() {

		System.out.println("What would you like to do? ");
		System.out.println();
		System.out.println("1 - Start a new game Player Vs Player ");
		System.out.println("2 - Start a new game Player Vs AI");
		System.out.println("3 - Exit game");
		System.out.println();
		return InputUtil.readIntFromUser();
	}

	/** Ask how we want to start the game */
	public final int askForStart() {

		System.out.println();
		System.out.println("Select which player starts :");
		System.out.println();
		System.out.println("1 - Player 1");
		System.out.println("2 - Player 2");
		System.out.println("3 - Random");

		return InputUtil.readIntFromUser();
	}

	/** Ask which column user wants to play in */
	public final int askForMove()  {

		System.out.print("Select a free column (or -1 to concede): ");
		return InputUtil.readIntFromUser();
	}

	/** Ask for how many pieces needed to connect for a win */
	public final int askForConnect() {

		System.out.println();
		System.out.print("How many would you like to connect: ");
		return InputUtil.readIntFromUser();
	}

	/** Ask for how many rows we want the board */
	public final int askForRows() {

		System.out.println();
		System.out.print("How many Rows: ");
		return InputUtil.readIntFromUser();
	}

	/** Ask for how many columns we want the board */
	public final int askForCols() {

		System.out.println();
		System.out.print("How many Columns: ");
		return InputUtil.readIntFromUser();
	}

	/** Ask the user if they want to play again */
	public final String askForRestart() {

		System.out.print("Would you like to play again? Press 'y' for yes or anywhere else for no. ");
		return InputUtil.readStringFromUser();
	}

	/** Ask the user what counter they want to use on the board */
	public final char askForChar(int counter) {

		System.out.println();
		System.out.print("Player " + counter + " pick a character to place on the board: ");
		return InputUtil.readCharFromUser();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////Display Board//////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////

	/** Print board of any dimensions */
	public final void displayBoard(Model model,char char1, char char2) {

		int nrRows = model.getNrRows();         /** Get the board dimensions */
		int nrCols = model.getNrCols();
		int[][] ourBoard = model.getBoard();

		String axis = "";                        /** Print the number of each column above that column */
		for (int i = 0 ; i < nrCols ; i ++)
			axis = axis + " " + i;
		String str = "-";
		String border = str.repeat(axis.length()); /** Print a boarder around the board */

		System.out.println(axis);
		System.out.println(border);

		for (int row = 0; row < nrRows; row++){    /** Loop to print the board itself */

			System.out.print("|");

			for (int col = 0; col < nrCols; col++){

				if (ourBoard[row][col] == 0) System.out.print("_"); /** All empty positions display "_" */
				else if (ourBoard[row][col] == 1) System.out.print(char1); /** Use counters selected by user */
				else if (ourBoard[row][col] == 2) System.out.print(char2);

				System.out.print("|");
			}

			System.out.println();
		}

		System.out.println(border);     /** Print boarder and column numbers again */
		System.out.println(axis);
		System.out.println();


	}

}
