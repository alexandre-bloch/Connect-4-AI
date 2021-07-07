import java.util.Random;

/**            AI Class - Used to play against the computer rather than a friend
               Uses the minimax algorithm with alpha beta pruning */

public class  AI {

    private final int NPC = 2;      /** To avoid magic numbers later on */
    private final int PLAYER = 1;

    //////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Tools //////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    /** The following are all methods that are used to implement the minimax algorithm */

    /** Used to slow ai's move down to help user */
    public void aiDelay(int threads) {

        try
        {
            Thread.sleep(threads); /** Adds the delay */
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    /** Used to count occurrences of an element in an array */
    public static int arrayCount(int[] array, int item) {

        int count = 0;

        for (int i = 0; i < array.length; i++) { /** Loop through all items in array */

            if (array[i] == item) { /** Check if item in array matches item we want */
                count++;            /** If so, add 1 to the count */
            }
        }

        return count;
    }

    /** Returns a column of the board as an array */
    public static int[] aiGetColumn(int[][] array, int index, int nrRows){

        int[] column = new int[nrRows];

        for(int i=0; i<nrRows; i++){
            column[i] = array[i][index];
        }

        return column;
    }

    /** Sorts the array from smallest to largest */
    public int[] sort(int[] array) {

        boolean reversed = true;
        int j = 0;
        int temp;

        while (reversed) {

            reversed = false;
            j++;

            for (int i = 0; i < array.length - j; i++) {

                if (array[i] > array[i + 1]) {
                    temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    reversed = true;

                }
            }
        }
        return array;
    }

    /** Returns an array of all the columns that can still be played in */
    public int[] validLocations(int[][]ourBoard, int nrCols) {

        int [] valid = new int[nrCols];  /** Create an array to store information about each column */

        for (int col = 0 ; col < nrCols ; col ++) { /** loop to get this information */

            if  (ourBoard[0][col] == 0) valid[col] = col; /** If the top row of the column is empty, add the colum number to our array */
            else valid[col] = -1; /** If the column is full, add -1 to the array */

        } /** Now we know which columns are full and which are not */

        int count = 0;

        for (int i = 0 ; i < valid.length ; i++ ) { /** Loop through to count how many columns are full */

            if (valid[i] == -1) count += 1;
        }

        int nrValid = valid.length - count; /** As we know how many columns are full, we can calculate how many are not */

        int[] result = new int[nrValid];

        for (int j = count ; j < valid.length ; j++) { /** Store the non full columns in a new list */
            if (sort(valid)[j] != -1) {
                result[j - count] = valid[j] ;
            }
        }
        return result;
    }

    /** Gets row of move based on the column selected by user */
    public int aiGetRow(int column, int[][] ourBoard, int nrRows) {

        int row;

        for (int i = nrRows - 1 ; i >= 0 ; i-- ) {

            if(ourBoard[i][column] == 0) { /** Keep moving down the column while there are zeros */
                row = i;
                return row;
            }
        }
        row = -1;
        return row;
    }

    /** Checks if game is ending */
    public boolean isTerminalNode(int[][]ourBoard, int connect, int counter, Model model) {
        return ((model.winner(connect,NPC,ourBoard)) || (model.winner(connect,PLAYER,ourBoard))) || model.boardFull(ourBoard);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// MiniMax Algorithm //////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    /** Method to score the board */
    public int score(int counter, int[][] ourBoard, int connect, int nrRows, int nrCols) {

        /** I made a seperate scoring system for traditional connect 4 and for connect x */

        int score = 0;
        int smallest = connect - 1;

        /** Scoring the Centre Column */

        int[] centreArray = new int[nrRows];
        int[] leftCentreArray = new int[nrRows];
        int[] rightCentreArray = new int[nrRows];

        /** Scores middle column in traditional connect 4 */

        if (nrRows == 6 && nrCols == 7 && connect == 4){

            int middle = (nrCols - 1) / 2;

            centreArray = aiGetColumn(ourBoard, middle, nrRows);
            int centreCount = arrayCount(centreArray, counter);

            score += centreCount*3;

        }

        /** Scores middle column in connect x */

        else if (nrCols % 2 != 0) { /** If there is an odd number of columns there is 1 middle column */

            int middle = (nrCols - 1) / 2;

            centreArray = aiGetColumn(ourBoard, middle, nrRows);
            int centreCount = arrayCount(centreArray, counter);

            score += centreCount*6;
        }

        else {  /** If there is an even number of columns, then there are 2 middle columns */

            int middleRight = nrCols / 2;
            rightCentreArray = aiGetColumn(ourBoard, middleRight, nrRows);

            int middleLeft = middleRight - 1;
            leftCentreArray = aiGetColumn(ourBoard, middleLeft, nrRows);

            int leftCentreCount = arrayCount(leftCentreArray, counter);
            int rightCentreCount = arrayCount(rightCentreArray, counter);

            score += leftCentreCount*5;
            score += rightCentreCount*5;
        }

        /** All the following scoring methods find windows of "connect" pieces (how many pieces are needed in a row to win)
         *  in each direction and counts how many of each piece is found in that window.
         *  Then a score is calculated based off of this.
         */

        /** Scoring the horizontals */

        for(int row = 0 ; row < nrRows ; row++ ) {

            for (int col = 0 ; col < nrCols - smallest ; col++) {

                int[] window = new int[connect];

                for (int i = 0; i <= smallest; i++) {

                    window[i] = ourBoard[row][col + i];
                }

                score += scoreWindow(counter,window,connect, nrRows,nrCols);
            }
        }

        /** Scoring the verticals */

        for(int col = 0 ; col < nrCols ; col++ ) {

            for (int row = 0 ; row < nrRows - smallest ; row++) {

                int[] window = new int[connect];

                for (int i = 0; i <= smallest; i++) {

                    window[i] = ourBoard[row+i][col];
                }

                score += scoreWindow(counter,window,connect,nrRows,nrCols);
            }
        }

        /** Scoring the negative diagonals */

        for (int row = smallest; row < nrRows ; row++) {

            for (int col = 0; col < nrCols - smallest; col++) {

                int[] window = new int[connect];

                for (int i = 0; i <= smallest; i++) {

                    window[i] = ourBoard[row - i][col + i];
                }

                score += scoreWindow(counter,window,connect,nrRows,nrCols);

            }
        }

        /** Scoring the positive diagonals */

        for (int row = 0; row < nrRows - smallest; row++) {

            for (int col = 0; col < nrCols - smallest; col++) {

                int[] window = new int[connect];

                for (int i = 0; i <= smallest; i++) {

                    window[i] = ourBoard[row + i][col + i];
                }

                score += scoreWindow(counter,window,connect,nrRows,nrCols);
            }
        }

        return score;
    }

    /** Method used to score all the windows found using the score method */
    public int scoreWindow(int counter, int[] window, int connect, int nrRows, int nrCols) {

        int oppPiece = PLAYER;    /** From the point of view of the ai, the opponent is the user */
        if (counter == PLAYER)  { /** If it's the user's go, then the opponent is the ai */
            oppPiece = NPC;
        }

        int score = 0;

        /** I made a seperate scoring system for traditional connect 4 as I was not confident in the scoring system
         * that i made for connect x.
         */

        if(nrRows == 6 && nrCols == 7 && connect ==4) {
            if (arrayCount(window, counter) == 4) score += Integer.MAX_VALUE;
            if ((arrayCount(window, counter) == 3) && (arrayCount(window, 0) == 1)) score += 5;
            if ((arrayCount(window, counter) == 2) && (arrayCount(window, 0) == 2)) score += 2;
            if ((arrayCount(window,oppPiece) == 3) && (arrayCount(window,0) == 1)) score -= 4;
        }

        else { /** Scoring system for connect x */

            /** This scoring system is explained in report */

            for (int j = 0; j <= connect; j++) {

                if (arrayCount(window,counter) == connect) score += Integer.MAX_VALUE;
                else {

                    if ((arrayCount(window, counter) == connect - j) && (arrayCount(window, 0) == j)) {

                        score += Math.exp(connect - j);
                    }
                }

                if ((arrayCount(window, oppPiece) == connect - 1) && (arrayCount(window, 0) == 1)) {

                    score -= 1500000;
                }
            }
        }

        return score;
    }

    /** Minimax algorithm to decide what the best next move is for the ai */
    public int[] minimax(int[][] ourBoard, int connect, int counter, int depth, boolean maximizingPlayer, Model model, int nrCols, int nrRows, double alpha, double beta) {

        /** This function returns results in the form {column, score}
         * For the terminal and depth = 0 cases, there is no column to return, so we return -1
         */

        int[] validLocations = validLocations(ourBoard,nrCols); /** Find all the columns in which we can still play */
        boolean isTerminal = isTerminalNode(ourBoard,connect,counter,model); /** Check whether game is over or not */

        if (depth <= 0 || isTerminal) { /** If the recursion depth is 0 or if this is the last move before the end of the game */

            if (isTerminal) { /** If this is the final move of the game */

               if (model.winner(connect, NPC, ourBoard)) { /** If ai wins, return a very high score */

                    return new int[] {-1, (int) 100000000000000000L};

                }

                else if (model.winner(connect, PLAYER, ourBoard)) { /** If user wins, return a very low score */

                    return new int[]{-1, (int) -100000000000000000L};

                }
                else { /** If game is drawn return 0 */

                    return new int[] {-1, 0};

                }
            }

            else { /** If recursion depth =0, then return score of move*/

                return new int[] {-1,score(NPC,ourBoard,connect,nrRows,nrCols)};
            }
        }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (maximizingPlayer) { /** If we want to maximise the score of this player - yes for AI */

            Random rnd = new Random();
            int rand = rnd.nextInt(validLocations.length);
            int column = validLocations[rand];

            double value = Double.NEGATIVE_INFINITY;

            for (int i = 0 ; i < validLocations.length ; i++) { /** Try each possible move on the board */

                int col = validLocations[i];
                int row = aiGetRow(col, ourBoard, nrRows); /** Get the row of each possible move */

                ourBoard[row][col] = NPC; /** Place ai piece and then calculate score of move */
                int newScore = minimax(ourBoard,connect,counter,depth-1,false,model,nrCols,nrRows,alpha,beta)[1];
                ourBoard[row][col] = 0; /** Once score has been calculated using score function and recursion, remove the piece */

                if (newScore > value) { /** Keep track of scores of each move */
                                        /** If we find a new high score, remember score and column */
                    value = newScore;
                    column = col;

                }
                alpha = Math.max(alpha,value); /** Alpha beta pruning */
                if (alpha >= beta) {
                    break;
                }
            }
            return new int[]{column, (int) value}; /** return column which produces maximum score */
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        else { /** Noy maximising player - If we want to minimise the score of this player - yes for user */

            Random rnd = new Random();
            int rand = rnd.nextInt(validLocations.length);
            int column = validLocations[rand];

            double value = Double.POSITIVE_INFINITY;

            for (int i = 0 ; i < validLocations.length ; i++) { /** Try each possible move on the board */

                int col = validLocations[i];
                int row = aiGetRow(col, ourBoard, nrRows);  /** Get the row of each possible move */

                ourBoard[row][col] = PLAYER; /** Place user piece and then calculate score of move */
                int newScore = minimax(ourBoard,connect,counter,depth-1,true,model,nrCols,nrRows,alpha,beta)[1];
                ourBoard[row][col] = 0; /** Once score has been calculated using score function and recursion, remove the piece */

                if (newScore < value) { /** Keep track of scores of each move */
                                        /** If we find a new low score, remember score and column */
                    value = newScore;
                    column = col;
                }

                beta = Math.min(beta,value); /** Alpha beta pruning */
                if (alpha>= beta) {
                    break;
                }
            }

            return new int[] {column,(int) value}; /** return column which produces minimum score */
        }

    }


//////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// Unused (Previous AI) /////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////

    /*

    public int pickBestMove(int counter, int[][] ourBoard, int connect, int nrCols, int nrRows, Model model) {

        int[] validLocations = validLocations(ourBoard,nrCols);
        int rnd = new Random().nextInt(validLocations.length);

        int bestScore = -100000000;
        int bestColumn = validLocations[rnd];

        for (int col = 0 ; col < nrCols ; col++) {
            int row = aiGetRow(col,ourBoard, nrRows);
            if (row == -1) continue;
            ourBoard[row][col] = counter;
            int score = score(counter,ourBoard,connect,nrRows,nrCols);
            if (score >= bestScore) {
                bestScore = score;
                bestColumn = col;
            }
            ourBoard[row][col] = 0;

        }
        score(counter,ourBoard,connect,nrRows,nrCols);
        return bestColumn;
    }

    public int score_position(int counter, int[][] ourBoard, int connect, int nrRows, int nrCols) {

        // Score Horizontal

        int smallest = connect - 1;
        int score = 0;

        for(int row = 0 ; row < nrRows ; row++ ) {
            int window = 0;
            for (int col = 0 ; col < nrCols - smallest ; col++) {
                window = 0;
                for (int i = 0; i <= smallest; i++) {
                    if (ourBoard[row][col + i] == counter) window += 1;
                    else window = 0;
                    score += scoreWindow(counter,window,connect);
                }
            }
        }


        for (int col = 0 ; col < nrCols ; col++) {
            int window = 0;
            for (int row = 0 ; row < nrRows - smallest ; row++) {
                window = 0;
                for (int i = 0; i <= smallest; i++) {
                    if (ourBoard[row + i][col] == counter) window += 1;
                    else window = 0;
                    score += scoreWindow(counter,window,connect);
                }
            }
        }

        for (int row = smallest; row < nrRows ; row++) {
            int window = 0;
            for (int col = 0; col < nrCols - smallest; col++) {
                for (int i = 0; i <= smallest; i++)
                    if (ourBoard[row - i][col + i] == counter) window += 1;
                    else window = 0;
                score += scoreWindow(counter,window,connect);
            }
        }


        for(int row = 0; row < nrRows - smallest; row++){
            int window = 0;
            for(int col = 0; col < nrCols - smallest; col++) {
                for (int i = 0; i <= smallest; i++)
                    if (ourBoard[row + i][col + i] == counter) window += 1;
                    else window = 0;
                score += scoreWindow(counter,window,connect);
            }
        }

        return score;
    }

    public int scoreWindow(int counter, int window, int connect) {
        int score = 0;
        for (int j = 0; j <= connect; j++) {
            if (window == connect - j ) score += Math.exp(connect - j);
        }
        return score;
    }

    public static int[] subarray(int[] array,int begin, int end) {


        int[] subarray = new int[end - begin + 1];
        for (int i = 0; i < subarray.length; i++) {
            subarray[i] = array[begin + i];
        }

        return subarray;
    }

    public int aiRandomCol(int nrCols) {
        Random rand = new Random();
        int col = rand.nextInt(nrCols);
        return col;
    }

    public void aiDelay(int threads) {
        try
        {
            Thread.sleep(threads);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    */




}
