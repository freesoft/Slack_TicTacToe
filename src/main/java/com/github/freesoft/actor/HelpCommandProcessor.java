package com.github.freesoft.actor;

import com.github.freesoft.model.EphemeralMessage;
import com.github.freesoft.model.Game;
import com.github.freesoft.model.Message;
import com.github.freesoft.model.SlackCommand;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * process help command
 *
 * @author wonhee jung
 */
@Service
public class HelpCommandProcessor implements CommandProcessor {

    private static final String HELP_MESSAGE =
                    "/ttt start [username] : play new tac-tac-toe game with username, and default board size 3x3\n" +
                    "/ttt start [username] boardSize: play new tac-tac-toe game with username, and with given boardSize x boardSize board. \n" +
                    "/ttt status : display current board status and who's turn to play.\n" +
                    "/ttt mark [row] [col] : make your move on (row,col).\n" +
                    "/ttt draw : draw the game during your turn. Opponent will be a winner.\n" +
                    "/ttt help : get help message for the command";
    @Override
    public Message process(SlackCommand slackCommand, Map<String, Game> gameInfoList) {
        return new EphemeralMessage(HELP_MESSAGE);
    }
}
