
/**
 * This file is to be completed by you.
 *
 * @author <Please enter your matriculation number, not your name>
 */
public final class Controller
{

	private final Model model;
	private final TextView view;
	private final AI aI = new AI();

	private final int PLAYER = 1;   /** Used to eliminate magic numbers later on */
	private final int NPC = 2;
	private final int ERROR = -1;

	public Controller(Model model, TextView view)
	{
		this.model = model;
		this.view = view;
	}

	public void startSession() {
		boolean restart = true;
		breaklabel:
		do {                                   /** Loop means that game will play at least once. The loop will restart if user wants to play again */

			view.displayTitle();

			//////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////// Which Gamemode ////////// //////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////

			/** Either play with another user, agains the AI, or exit game */

			boolean isAgainstAI = false;   /** Loop used to make sure user input is valid. */
			int gameMode = ERROR;

			while (!isAgainstAI) {

				gameMode = model.playGameMode(view.askForGameMode());

				if (gameMode == ERROR) {

					view.displayInvalidOption();
					isAgainstAI = false;

				} else isAgainstAI = true;
			}

			if(gameMode == 3) {        /** Quit game */

				view.displayLogOff();
				aI.aiDelay(1500);
				System.exit(0);
			}

			//////////////////////////////////////////////////////////////////////////////////
			////////////////////////////// Validation of Dimensions //////////////////////////
			//////////////////////////////////////////////////////////////////////////////////

			view.displaySettingsMessage();

			boolean isRowsValid = false;   /** Check that number of rows is valid */
			int rows = ERROR;

			while (!isRowsValid) {

				rows = model.getRows(view.askForRows()); /** These methods ensure that there are at least 4 rows */

				if (rows == ERROR) {

					view.displayRowError(); /** If user input is invalid the question will be asked again */
					isRowsValid = false;

				} else isRowsValid = true;
			}

			boolean isColsValid = false;   /** Check that number of columns is valid */
			int cols = ERROR;

			while (!isColsValid) {

				cols = model.getCols(view.askForCols()); /** These methods ensure that there are at least 4 columns */

				if (cols == ERROR) {

					view.displayColError();  /** If user input is invalid the question will be asked again */
					isColsValid = false;

				} else isColsValid = true;
			}

			model.setNewRows(rows);      /** Set up board with given dimensions */
			model.setNewCols(cols);
			model.newBoard(model.getNewRows(), model.getNewCols()); /** Build board with these new dimensions */

			boolean isConnectValid = false;  /** Check that the number of pieces we want to connect is valid given dimensions */
			int connects = ERROR;

			while (!isConnectValid) {

				connects = model.getConnect(view.askForConnect()); /** These methods ensure that the number to connect is less than the rows and columns */

				if (connects == ERROR) {

					view.displayConnectError();  /** If the user input is invalid, question is asked again */
					isConnectValid = false;

				} else isConnectValid = true;
			}

			int connect = connects;

			///////////////////////////////////////////////////////////////////////////
			//////////////////////////////Pick Counter/////////////////////////////////
			///////////////////////////////////////////////////////////////////////////

			char character1 = (char) PLAYER;
			char character2 = (char) NPC;

			if (gameMode == 1) {

				character1 = model.getChar(view.askForChar(PLAYER), (char)PLAYER);
				character2 = model.getChar(view.askForChar(NPC), (char)NPC);
			}
			else if (gameMode == 2) {

				view.displayAICounter();
				character1 = model.getChar(view.askForChar(PLAYER), (char)PLAYER);
				character2 = '2';

			}

			///////////////////////////////////////////////////////////////////////////
			/////////////////////////Decide who Starts/////////////////////////////////
			///////////////////////////////////////////////////////////////////////////
			boolean isStartValid = false;
			int counter = ERROR;

			while (!isStartValid) {

				counter = model.setPlayer(view.askForStart());

				if (counter == ERROR) {

					view.displayInvalidOption();
					isStartValid = false;

				}else isStartValid = true;
			}

			///////////////////////////////////////////////////////////////////////////
			///////////////////////// Begin Gameloop //////////////////////////////////
			///////////////////////////////////////////////////////////////////////////

			System.out.println();
			view.displayStartMessage();

			boolean gameContinues = true;           /** While this condition is true game continues */
			view.displayBoard(model,character1,character2);               /** Display empty board before the game starts */

			while (gameContinues) {                 /** Make condition false if someone wins or if board is full */

				int[][] ourBoard = model.getBoard();                    /** Set board and begin game */

				view.displayPlayerTurn(counter);



				///////////////////////////////////////////////////////////////////////////
				/////////////////////// Getting player and AI move ////////////////////////
				///////////////////////////////////////////////////////////////////////////

					boolean isColumnValid = false;
					int move = -2;
					while (!isColumnValid) {

						if (gameMode == PLAYER) {    /** If we are playing against a friend */

							move = model.getMove(view.askForMove());

						} else if (gameMode == NPC){  /** If we are playing against the ai */

							if (counter == PLAYER) move = model.getMove(view.askForMove());
							else if (counter == NPC) move = aI.minimax(ourBoard,connect,counter,7,true,model,model.getNrCols(),model.getNrRows(),Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY)[0];

						}
						if (move == -1) {

							model.resetBoard(model.getNrRows(), model.getNrCols());
							view.displayConcede(counter);
							continue breaklabel;

						}
						if (model.isMoveValid(move) == false) {   /** Loop to ensure that if player makes invalid move, they can retry */

							if (counter == PLAYER) view.displayInvalidMove(); /** Error message to player but not to ai */
							isColumnValid = false;

						} else isColumnValid = true;

				}
				// if move.equals # break

				int row = model.getRow(move, ourBoard); /** Once it is certain that the move is valid, we get the row that the piece will drop into */

				ourBoard[row][move] = counter;         /** Place player counter on board using column and row */

				model.setBoard(ourBoard);

				if (counter == NPC && gameMode == NPC) aI.aiDelay(1500); /** Give the ai a time delay to improve user experience */

				view.displayBoard(model,character1,character2);              /** After every valid move, display the board */


				///////////////////////////////////////////////////////////////////////////
				////////////////////////////// Check Winner ////// ////////////////////////
				///////////////////////////////////////////////////////////////////////////

				if (model.gameOver(connect, counter, ourBoard)) {  /** Checks if the game is over */

					gameContinues = false;
					if (model.boardFull(ourBoard)) view.displayDraw(); /** Display different messages based on if a player has won or if the board is full */
					else if (model.winner(connect, counter, ourBoard)) view.displayPlayerWins(counter);

				}

				///////////////////////////////////////////////////////////////////////////
				///////////////////////////////// End Turn ////////////////////////////////
				///////////////////////////////////////////////////////////////////////////

				else {

					if (counter == 1) counter = 2;  /** If there is no winner and the round ends, change player */
					else counter = 1;
				}
			}

			model.resetBoard(model.getNrRows(), model.getNrCols());    /** reset the board by making it empty */

		} while(model.restartGame(view.askForRestart(), model.ourBoard)); /** If the user wants to play again the loop restarts */

		view.displayThanks(); /** Thank you message if the user wants to quit game */
	}
}






