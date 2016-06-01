package com.github.freesoft.actor;

import com.github.freesoft.controller.TicTacToeController;
import com.github.freesoft.model.EphemeralMessage;
import com.github.freesoft.model.Game;
import com.github.freesoft.model.InChannelMessage;
import com.github.freesoft.model.Message;
import com.github.freesoft.model.SlackCommand;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * process start command
 *
 * @author wonhee jung
 */
@Service
public class StartCommandProcessor implements CommandProcessor {
    private static final String COMMAND_SEPARATOR = TicTacToeController.COMMAND_SEPARATOR;

    @Override
    public Message process(SlackCommand slackCommand, Map<String, Game> gameInfoList) {
        final String[] tokens = slackCommand.getText().split(COMMAND_SEPARATOR);
        if (gameInfoList.containsKey(slackCommand.getChannelId())) {
            return new EphemeralMessage("Another game is in progress. Game can't be started until finishing previous game or draw");
        }
        //expecting 2nd token for opponent player
        if (tokens.length < 2){
            return new EphemeralMessage("need opponent player's name");
        }

        final String opponentUserName = tokens[1];
        // if 3rd token exists, then need to create a game with bigger than 3x3 board.
        int boardSize;
        if (tokens.length != 3){
            boardSize = 3;
        }
        else {
            try {
                if (Integer.valueOf(tokens[2]) < 3){
                    return new EphemeralMessage("Board size should be larger than 3");
                }
                if (Integer.valueOf(tokens[2]) % 2 != 1){
                    return new EphemeralMessage("Board size should be odd number, not even");
                }

                boardSize = Integer.valueOf(tokens[2]);
            }
            catch(NumberFormatException e){
                return new EphemeralMessage("Board size parameter should be given as number format.");
            }
        }

        Game game = new Game(boardSize, slackCommand.getUserName(), opponentUserName);
        gameInfoList.putIfAbsent(slackCommand.getChannelId(), game);

        return new InChannelMessage(String.format("New game has started. player 1 : %s, player 2 : %s", game.getPlayer1(), game.getPlayer2()));
    }
}
