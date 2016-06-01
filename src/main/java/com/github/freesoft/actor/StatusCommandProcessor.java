package com.github.freesoft.actor;

import com.github.freesoft.model.EphemeralMessage;
import com.github.freesoft.model.Game;
import com.github.freesoft.model.InChannelMessage;
import com.github.freesoft.model.Message;
import com.github.freesoft.model.SlackCommand;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * process status command
 *
 * @author wonhee jung
 */
@Service
public class StatusCommandProcessor implements CommandProcessor {
    @Override
    public Message process(SlackCommand slackCommand, Map<String, Game> gameInfoList) {

        if (!gameInfoList.containsKey(slackCommand.getChannelId())) {
            return new EphemeralMessage("No game in progress to display current board");
        }

        Game game = gameInfoList.get(slackCommand.getChannelId());

        String boardStatus = String.format("%s\n%s", game.getBoardStatusToString(), game.getWhoseTurnToString());
        return new InChannelMessage(boardStatus);
    }
}
