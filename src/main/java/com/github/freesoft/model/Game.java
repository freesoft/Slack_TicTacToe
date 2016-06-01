package com.github.freesoft.model;

/**
 * Game class that includes board status, players, etc
 *
 * @author wonhee jung
 */
public class Game {
    private static final int X = 1, O = -1;      // players
    private static final int EMPTY = 0;          // empty cell
    private final int[][] board;
    private final int boardSize;
    private int player;                         // represent current player based on number
    private int playedCnt; // if the value reached at 9 then board has filled out and no more turn is available.
    private String currentPlayer;
    private String player1; // Slack channel user_name mapping for 1st player(X)
    private String player2; // SLack channel user_name mapping for 2nd player(O)
    private GameStatus gameStatus = GameStatus.IN_PROGRESS;

    public Game(final int boardSize, String player1, final String player2) {
        this.player = X;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1; // set player1 as current player
        this.boardSize = boardSize;
        board = new int[boardSize][boardSize];
    }

    /**
     * marking on board with given position row and column.
     *
     * @param row row position for marking. Index starts from 0, not 1.
     * @param col col position for marking. Index starts from 0, not 1.
     * @return true if this marking ends the game and current player become winner, false otherwise.
     * @throws IllegalArgumentException
     */
    public void putMark(final int row, final int col) throws IllegalArgumentException {

        if (row < 0 || row > boardSize - 1 || col < 0 || col > boardSize - 1) {
            throw new IllegalArgumentException("Invalid board position.");
        }

        if (board[row][col] != EMPTY) {
            throw new IllegalArgumentException("Board position has already occupied.");
        }

        board[row][col] = player;
        playedCnt++;

        // check if current player became winner, and return true if it is
        if (hasCompletedLine(player)){
            gameStatus = currentPlayer.equals(player1)? GameStatus.GAMEOVER_PLAYER1_WINNER : GameStatus.GAMEOVER_PLAYER2_WINNER;
            return;
        }

        // tie
        if (playedCnt == boardSize * boardSize) {
            gameStatus = GameStatus.GAMEOVER_TIE;
            return;
        }

        player = - player;
        // change turn
        currentPlayer = currentPlayer.equals(player1)? player2 : player1;
    }

    /**
     * Check if there is a completed line with given mark.
     *
     * @param mark
     * @return
     */
    public boolean hasCompletedLine(int mark) {
        return checkRows(mark) || checkCols(mark) || checkDiagonals(mark) || checkRevDiagonals(mark);
    }

    /**
     * Check if there is completed row
     *
     * @param mark
     * @return
     */
    private boolean checkRows(int mark) {
        final int condition = mark * boardSize;
        int evaluate;
        for ( int i = 0 ; i < boardSize; i++ ) {
            evaluate = 0;
            for ( int j = 0; j < boardSize; j++ ){
                evaluate += board[i][j];
            }
            if (evaluate == condition){
                return true;
            }
        }
        return false;
    }

    /**
     ** Check if there is completed col
     *
     * @param mark
     * @return
     */
    private boolean checkCols(int mark) {
        final int condition = mark * boardSize;
        int evaluate;
        for ( int i = 0 ; i < boardSize; i++ ) {
            evaluate = 0;
            for ( int j = 0; j < boardSize; j++ ){
                evaluate += board[j][i];
            }
            if (evaluate == condition){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if there is completed diagonal
     *
     * @param mark
     * @return
     */
    private boolean checkDiagonals(int mark) {
        final int condition = mark * boardSize;
        int evaluate = 0;
        for ( int i = 0, j = 0 ; i < boardSize && j < boardSize; i++, j++ ) {
            evaluate += board[j][i];
        }
        return (evaluate == condition);
    }

    /**
     * Check if there is completed diagonal
     *
     * @param mark
     * @return
     */
    private boolean checkRevDiagonals(int mark) {
        final int condition = mark * boardSize;
        int evaluate = 0;
        for ( int i = 2, j = 0 ; i >= 0; i--, j++ ) {
            evaluate += board[j][i];
        }
        return (evaluate == condition);
    }

    /**
     * Get winner's code, 1 for X, or -1 for O. If game is tie, then returns 0.
     * @return
     */
    public int winner() {
        if (hasCompletedLine(X)) {
            return (X);
        }
        else if (hasCompletedLine(O)) {
            return (O);
        }
        else {
            return (0);
        }
    }

    /**
     *  display current board status and who's turn to play
     */
    public String getBoardStatusToString() {
        StringBuilder sb = new StringBuilder(64);
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                switch (board[row][col]) {
                    case X:
                        sb.append(" X ");
                        break;
                    case O:
                        sb.append(" O ");
                        break;
                    case EMPTY:
                        sb.append(" * ");
                        break;
                    default:
                        break;
                }
                if (col < boardSize) {
                    sb.append("|");
                }
            }
            if (row < boardSize){
                sb.append("\n-----------\n");
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * generate string that includes who's current player
     *
     * @return
     */
    public String getWhoseTurnToString(){
        StringBuilder sb = new StringBuilder(32);
        if (this.player == X){
            sb.append("It's " + player1 + "'s turn.");
        }
        else if(this.player == O){
            sb.append("It's " + player2 + "'s turn.");
        }
        return sb.toString();
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public int getPlayedCnt() {
        return playedCnt;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * player draw the game
     */
    public void draw() {
        gameStatus = player1.equals(currentPlayer)? GameStatus.GAMEOVER_PLAYER1_DRAW : GameStatus.GAMEOVER_PLAYER2_DRAW;
    }
}