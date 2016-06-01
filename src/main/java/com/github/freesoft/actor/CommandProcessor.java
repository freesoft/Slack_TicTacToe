package com.github.freesoft.actor;

import com.github.freesoft.model.Game;
import com.github.freesoft.model.Message;
import com.github.freesoft.model.SlackCommand;

import java.util.Map;

/**
 * interface for {@link com.github.freesoft.model.SlackCommand} processor
 *
 * @author wonhee jung
 */
public interface CommandProcessor {

    Message process(SlackCommand slackCommand, Map<String, Game> gameInfoList);
}
