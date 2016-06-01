package com.github.freesoft.model;

import org.junit.Test;

/**
 * @author wonhee jung
 * @since 5/31/16
 */
public class GameTest {

    @Test
    public void testGame() {
        Game game = new Game(3, "player1", "player2");
        game.putMark(1,1);          game.putMark(0,2);
        game.putMark(2,2);          game.putMark(0,0);
        game.putMark(0,1);          game.putMark(2,1);
        game.putMark(1,2);          game.putMark(1,0);
        game.putMark(2,0);
        System.out.println(game);
        int winningPlayer = game.winner();
        String[] outcome = {"O wins", "Tie", "X wins"};
        System.out.println(outcome[1 + winningPlayer]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGameWithNegativePosition() {
        Game game = new Game(3, "player1", "player2");
        game.putMark(-1,-1);
    }


    @Test
    public void testGameFor5x5Board() {
        Game game = new Game(5, "player1", "player2");
        game.putMark(0,0);          game.putMark(0,1);
        game.putMark(1,1);          game.putMark(0,2);
        game.putMark(2,2);          game.putMark(0,3);
        game.putMark(3,3);          game.putMark(0,4);
        game.putMark(4,4);
        System.out.println(game);
        int winningPlayer = game.winner();
        String[] outcome = {"O wins", "Tie", "X wins"};
        System.out.println(outcome[1 + winningPlayer]);
    }
}
