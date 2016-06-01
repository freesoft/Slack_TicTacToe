package com.github.freesoft.actor;

import com.github.freesoft.model.EphemeralMessage;
import com.github.freesoft.model.Game;
import com.github.freesoft.model.InChannelMessage;
import com.github.freesoft.model.Message;
import com.github.freesoft.model.SlackCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * process draw command
 *
 * @author wonhee jung
 */
@Service
public class DrawCommandProcessor implements CommandProcessor {

    private static final Logger LOGGER  = LoggerFactory.getLogger(DrawCommandProcessor.class);

    @Override
    public Message process(SlackCommand slackCommand, Map<String, Game> gameInfoList) {
        if (!gameInfoList.containsKey(slackCommand.getChannelId())) {
            return new EphemeralMessage("No game in progress to draw");
        }

        Game game = gameInfoList.get(slackCommand.getChannelId());
        if (!canResetTheGame(slackCommand, game)){
            return new EphemeralMessage("You can't draw as it's not your turn");
        }

        game.draw();

        gameInfoList.remove(slackCommand.getChannelId());
        return new InChannelMessage(
                String.format("%s drew the game. %s won!",
                        slackCommand.getUserName(),
                        game.getPlayer1().equals(game.getCurrentPlayer())? game.getPlayer2() : game.getPlayer1()));
    }

    /**
     * <p>
     * Check if user who calls /ttt draw are eitehr player1 or player2 of current in-progress game so that
     * either one of them can draw the game even if it's not player's turn.
     * Originally designed that only current player can draw, but since current code can't detect if player2's uername is actually
     * exist in the channel, if not using Slack API, so there is a possibility that player1 initiated the game with player2's username, which is not
     * exist and/or valid so that player 2 never able to move its own move and player1 have to wait forever.<br/>
     * Also, due to limitation by given assignment(https://slack-files.com/T024BE7LD-F0NJM55RV-cba031f54a), only one in-progress game can exist in the channel,
     * so if game doesn't allow any game participants can draw the game, there is no way players can initiate new game unless player can restart
     * custom command server daemon.
     * </p>
     *
     * @param slackCommand
     * @param game
     * @return
     */
    private boolean canResetTheGame(SlackCommand slackCommand, Game game) {
        if (!game.getCurrentPlayer().equals(slackCommand.getUserName())){
            LOGGER.info("player1 : {}, player2: {}, currentPlayer : {}, slackCommand.userId : {}", game.getPlayer1(), game.getPlayer2(), game.getCurrentPlayer(), slackCommand.getUserName());
            return false;
        }
        return true;
    }
}
