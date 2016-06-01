package com.github.freesoft.controller;

import com.github.freesoft.actor.DrawCommandProcessor;
import com.github.freesoft.actor.HelpCommandProcessor;
import com.github.freesoft.actor.MarkCommandProcessor;
import com.github.freesoft.actor.StartCommandProcessor;
import com.github.freesoft.actor.StatusCommandProcessor;
import com.github.freesoft.model.EphemeralMessage;
import com.github.freesoft.model.Game;
import com.github.freesoft.model.Message;
import com.github.freesoft.model.SlackCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * /start : endpoint of tic-tac-toe
 *
 * @author wonhee jung
 */
@Controller
public class TicTacToeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicTacToeController.class);

    public static final String COMMAND_SEPARATOR = " ";

    @Autowired
    private Environment environment;

    /**
     * key : channel name
     * value : game information that's in progress at given channel.
     */
    private Map<String, Game> gameInfoList = new ConcurrentHashMap<>();

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private StartCommandProcessor startCommandProcessor;

    @Autowired
    private MarkCommandProcessor markCommandProcessor;

    @Autowired
    private StatusCommandProcessor statusCommandProcessor;

    @Autowired
    private DrawCommandProcessor drawCommandProcessor;

    @Autowired
    private HelpCommandProcessor helpCommandProcessor;

    /**
     *
     * slackCommand.getText() will be the command for the game
     *
     * start : start a new game, user name(not user id) will be following to play together. Can't invite the user who is not in the current channel.
     * player can use specify his/her own username so that player can do all by themselves as well.
     * mark : set x and y position for current player
     * draw : draw the game
     * status : display current board status and who's turn to play
     * help : TODO : prints help text
     *
     * @param
     * @return
     */
    @RequestMapping(value="/start", method=RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Message start(HttpServletRequest request){

        final SlackCommand slackCommand = new SlackCommand();
        slackCommand.setToken(request.getParameter("token"));
        slackCommand.setTeamId(request.getParameter("team_id"));
        slackCommand.setTeamDomain(request.getParameter("team_domain"));
        slackCommand.setChannelId(request.getParameter("channel_id"));
        slackCommand.setChannelName(request.getParameter("channel_name"));
        slackCommand.setUserId(request.getParameter("user_id"));
        slackCommand.setUserName(request.getParameter("user_name"));
        slackCommand.setCommand(request.getParameter("command"));
        slackCommand.setText(request.getParameter("text"));
        slackCommand.setResponseUrl(request.getParameter("response_url"));

        LOGGER.info("token : {}, channel_id : {}, user_id : {}, user_name : {}, command : {}, text : {}, response_url : {}",
                slackCommand.getToken(),
                slackCommand.getChannelId(),
                slackCommand.getUserId(),
                slackCommand.getUserName(),
                slackCommand.getCommand(),
                slackCommand.getText(),
                slackCommand.getResponseUrl());

        final String tokenToValidate = slackCommand.getToken();
        final Message response;

        final String token = environment.getProperty("SLACK_TTT_TOKEN");

        if (!StringUtils.hasText(token)){
            return new EphemeralMessage("Failed reading channel's token value from SLACK_TTT_TOKEN environment variable");
        }

        if (!token.equals(tokenToValidate)){
            return new EphemeralMessage("token validation has failed");
        }

        if (!StringUtils.hasText(slackCommand.getText())){
            return new EphemeralMessage("invalid command");
        }

        final String[] tokens = slackCommand.getText().split(COMMAND_SEPARATOR);
        final String cmd = tokens[0];
        switch(cmd){
            case "start":
                response = startCommandProcessor.process(slackCommand, gameInfoList);
                break;
            case "mark":
                return markCommandProcessor.process(slackCommand, gameInfoList);
            case "draw":
                response = drawCommandProcessor.process(slackCommand, gameInfoList);
                break;
            case "status":
                response = statusCommandProcessor.process(slackCommand, gameInfoList);
                break;
            case "help":
                response = helpCommandProcessor.process(slackCommand, gameInfoList);
                break;
            default:
                return new EphemeralMessage("unrecognized command");
        }

        return response;
    }

    /**
     * Send response message to delayed response url
     * @param slackCommand
     * @param responseMessage
     */
    private void sendResponse(SlackCommand slackCommand, String responseMessage) {
        restTemplate = new RestTemplate();
        restTemplate.postForEntity(slackCommand.getResponseUrl(), responseMessage, String.class);
    }
}
