package com.github.freesoft.actor;

import com.github.freesoft.controller.TicTacToeController;
import com.github.freesoft.model.EphemeralMessage;
import com.github.freesoft.model.Game;
import com.github.freesoft.model.GameStatus;
import com.github.freesoft.model.InChannelMessage;
import com.github.freesoft.model.Message;
import com.github.freesoft.model.SlackCommand;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * process player's mark command
 *
 * @author wonhee jung
 *
 */
@Service
public class MarkCommandProcessor implements CommandProcessor {

    private static final String COMMAND_SEPARATOR = TicTacToeController.COMMAND_SEPARATOR;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MarkCommandProcessor.class);
    private static final String USAGE_TTT_MARK = "[Usage] /ttt mark row(number) col(number)";

    @Override
    public Message process(SlackCommand slackCommand, Map<String, Game> gameInfoList) {

        final String[] tokens = slackCommand.getText().split(COMMAND_SEPARATOR);
        Game game = gameInfoList.get(slackCommand.getChannelId());

        // check if next move position is from the valid channel
        if (slackCommand.getChannelId() == null || !gameInfoList.containsKey(slackCommand.getChannelId())){
            return new EphemeralMessage("Invalid channel information or no game is in progress at this channel");
        }

        // check if next move position is from the valid channel and player.
        if (!isPlayersTurn(slackCommand, game)){
            return new EphemeralMessage("Not your turn.");
        }

        int row, col;

        try {
            row = Integer.valueOf(tokens[1]);
            col = Integer.valueOf(tokens[2]);
        }
        catch(NumberFormatException e){
            return new EphemeralMessage(USAGE_TTT_MARK);
        }

        try {
            game.putMark(row, col);
            if (game.getGameStatus() == GameStatus.GAMEOVER_PLAYER1_WINNER || game.getGameStatus() == GameStatus.GAMEOVER_PLAYER2_WINNER){
                cleanUpFinishedGame(slackCommand, gameInfoList);
                return new InChannelMessage(String.format("%s\n%s won!!!", game.getBoardStatusToString(), game.getCurrentPlayer()));
            }
            else if (game.getGameStatus() == GameStatus.GAMEOVER_TIE){
                cleanUpFinishedGame(slackCommand, gameInfoList);
                return new InChannelMessage(String.format("%s\nTie!!", game.getBoardStatusToString()));
            }
        }
        catch(IllegalArgumentException e){
            return new EphemeralMessage(e.getMessage());
        }

        InChannelMessage inChannelMessage = new InChannelMessage(
                String.format("%s marked at (%s, %s).\n%s\n%s",
                        slackCommand.getUserName(),
                        row,
                        col,
                        game.getBoardStatusToString(),
                        game.getGameStatus() == GameStatus.IN_PROGRESS? game.getWhoseTurnToString() : "")); // print who's next information only when game isn't over yet

        return inChannelMessage;
    }

    public void cleanUpFinishedGame(SlackCommand slackCommand, Map<String, Game> gameInfoList) {
        gameInfoList.remove(slackCommand.getChannelId());
    }

    /**
     * check if player's turn to make a next move in the channel.
     *
     * @param slackCommand
     * @param game
     * @return
     */
    private boolean isPlayersTurn(SlackCommand slackCommand, Game game) {
        if (!game.getCurrentPlayer().equals(slackCommand.getUserName())){
            LOGGER.info("player1 : {}, player2: {}, currentPlayer : {}, slackCommand.userId : {}", game.getPlayer1(), game.getPlayer2(), game.getCurrentPlayer(), slackCommand.getUserName());
            return false;
        }
        return true;
    }
}
