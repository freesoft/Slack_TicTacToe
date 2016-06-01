package com.github.freesoft.controller;

import com.github.freesoft.actor.DrawCommandProcessor;
import com.github.freesoft.actor.HelpCommandProcessor;
import com.github.freesoft.actor.MarkCommandProcessor;
import com.github.freesoft.actor.StartCommandProcessor;
import com.github.freesoft.actor.StatusCommandProcessor;
import com.github.freesoft.controller.TicTacToeController;
import com.github.freesoft.model.EphemeralMessage;
import com.github.freesoft.model.Message;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.mockito.Mockito.when;

/**
 * @author wonhee jung
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TicTacToeControllerTest {

    @InjectMocks
    private TicTacToeController ticTacToeController;

    @Mock
    private StartCommandProcessor startCommandProcessor;

    @Mock
    private MarkCommandProcessor markCommandProcessor;

    @Mock
    private StatusCommandProcessor statusCommandProcessor;

    @Mock
    private DrawCommandProcessor drawCommandProcessor;

    @Mock
    private HelpCommandProcessor helpCommandProcessor;

    @Mock
    private Environment environment;

    private MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

    @Before
    public void setUp(){
        mockHttpServletRequest.addParameter("token", "123445");
        mockHttpServletRequest.addParameter("channel_id", "fake_channel_id");
        mockHttpServletRequest.addParameter("user_id", "fake_user_id");
        mockHttpServletRequest.addParameter("user_name", "fake_user_name");
        mockHttpServletRequest.setMethod("POST");
    }

    @Test
    public void testStart_withoutValidToken(){
        when(environment.getProperty("SLACK_TTT_TOKEN")).thenReturn("ABCD1234");
        mockHttpServletRequest.addParameter("command", "start");
        Message message = ticTacToeController.start(mockHttpServletRequest);
        Assert.assertTrue(message instanceof EphemeralMessage);
        Assert.assertEquals(message.getText(), "token validation has failed");
    }

    @Test
    public void testStart_withNullToken(){
        when(environment.getProperty("SLACK_TTT_TOKEN")).thenReturn(null);
        mockHttpServletRequest.addParameter("command", "start");
        Message message = ticTacToeController.start(mockHttpServletRequest);
        Assert.assertTrue(message instanceof EphemeralMessage);
        Assert.assertEquals(message.getText(), "Failed reading channel's token value from SLACK_TTT_TOKEN environment variable");
    }

}
