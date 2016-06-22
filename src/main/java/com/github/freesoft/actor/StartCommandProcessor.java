package com.github.freesoft.actor;

import com.github.freesoft.controller.TicTacToeController;
import com.github.freesoft.model.EphemeralMessage;
import com.github.freesoft.model.Game;
import com.github.freesoft.model.InChannelMessage;
import com.github.freesoft.model.Message;
import com.github.freesoft.model.SlackCommand;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * process start command
 *
 * @author wonhee jung
 */
@Service
public class StartCommandProcessor implements CommandProcessor {
    private static final String COMMAND_SEPARATOR = TicTacToeController.COMMAND_SEPARATOR;

    @Autowired
    private Environment environment;

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

        final String SLACK_BOT_TOKEN = environment.getProperty("SLACK_BOT_TOKEN");
        
        // check if opponent exists in the channel
        SlackSession session = SlackSessionFactory.createWebSocketSlackSession(SLACK_BOT_TOKEN);
        try {
            session.connect();
        } catch (IOException e) {
            return new InChannelMessage("Can't make Slack connection with given Slack bot token value. token : " + SLACK_BOT_TOKEN);
        }
        SlackChannel slackChannel = session.findChannelById(slackCommand.getChannelId());
        if (slackChannel == null){
            return new InChannelMessage("Can't find channel id " + slackCommand.getChannelId());
        }
        Collection<SlackUser> users = slackChannel.getMembers();
        if (users == null || users.isEmpty()){
            return new InChannelMessage("Can't find player 2 " + opponentUserName + " in the channel name "
                    + slackCommand.getChannelName() + ". Available Slack username for game : " + Arrays.toString(users.toArray()));
        }

        Optional<SlackUser> oppUser = users.stream().filter( slackuser -> slackuser.getUserName().equals(opponentUserName) ).findFirst();
        if (!oppUser.isPresent()){
            return new InChannelMessage("User name " + opponentUserName + " doesn't exist in the given channel.");
        }

        Game game = new Game(boardSize, slackCommand.getUserName(), opponentUserName);
        gameInfoList.putIfAbsent(slackCommand.getChannelId(), game);

        return new InChannelMessage(String.format("New game has started. player 1 : %s, player 2 : %s", game.getPlayer1(), game.getPlayer2()));
    }
}
