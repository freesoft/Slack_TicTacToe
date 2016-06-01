package com.github.freesoft.actor;

import com.github.freesoft.model.EphemeralMessage;
import com.github.freesoft.model.Game;
import com.github.freesoft.model.Message;
import com.github.freesoft.model.SlackCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * @author Wonhee Jung
 * @since 6/10/16
 */
@RunWith(MockitoJUnitRunner.class)
public class StartCommandProcessorTest {
    @InjectMocks
    private StartCommandProcessor startCommandProcessor;

    @Mock
    private SlackCommand slackCommand;

    @Mock
    private Map<String,Game> gameInfoList;

    @Before
    public void setUp(){
        when(slackCommand.getText()).thenReturn("start");
    }

    @Test
    public void testProcess(){
        startCommandProcessor.process(slackCommand, gameInfoList);

    }

    @Test
    public void testProcessWithEvenBoardSize(){
        when(slackCommand.getText()).thenReturn("start test_account 4");
        Message message = startCommandProcessor.process(slackCommand, gameInfoList);
        Assert.assertNotNull(message);
        Assert.assertTrue(message instanceof EphemeralMessage);
        Assert.assertEquals("Board size should be odd number, not even", message.getText());
    }
}
