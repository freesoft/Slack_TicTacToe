package com.github.freesoft.actor;

import com.github.freesoft.model.EphemeralMessage;
import com.github.freesoft.model.Game;
import com.github.freesoft.model.Message;
import com.github.freesoft.model.SlackCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Wonhee Jung
 * @since 6/10/16
 */
@RunWith(MockitoJUnitRunner.class)
public class MarkCommandProcessorTest {

    public static final String PLAYER_1 = "PLAYER1";
    @InjectMocks
    private MarkCommandProcessor markCommandProcessor;

    @Mock
    private SlackCommand slackCommand;

    @Mock
    private Map<String,Game> gameInfoList;
    private static int THREE_BY_THREE_BOARD_SIZE = 3;
    private static int FIVE_BY_FIVE_BOARD_SIZE = 5;

    @Before
    public void setUp(){
        when(slackCommand.getText()).thenReturn("mark");
    }

    @Test
    public void testProcess(){
        markCommandProcessor.process(slackCommand, gameInfoList);

    }

    /**
     * set the coordination with (-1, -1) and see if user gets proper error message
     */
    @Test
    public void testProcessWithMinusValue(){
        when(slackCommand.getText()).thenReturn("mark -1 -1");
        when(slackCommand.getChannelId()).thenReturn("TEST CHANNEL");
        when(gameInfoList.containsKey(anyString())).thenReturn(true);
        Game game = new Game(THREE_BY_THREE_BOARD_SIZE, "PLAYER1", "Player2");
        when(slackCommand.getUserName()).thenReturn(PLAYER_1);
        when(gameInfoList.get(anyString())).thenReturn(game);
        Message message = markCommandProcessor.process(slackCommand, gameInfoList);
        Assert.isTrue(message instanceof EphemeralMessage);
        Assert.isTrue(((EphemeralMessage) message).getText().equals("Invalid board position."));
    }

}