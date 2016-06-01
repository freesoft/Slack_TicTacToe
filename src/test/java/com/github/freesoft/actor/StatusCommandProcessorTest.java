package com.github.freesoft.actor;

import com.github.freesoft.model.Game;
import com.github.freesoft.model.SlackCommand;
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
public class StatusCommandProcessorTest {
    @InjectMocks
    private StatusCommandProcessor statusCommandProcessor;

    @Mock
    private SlackCommand slackCommand;

    @Mock
    private Map<String,Game> gameInfoList;

    @Before
    public void setUp(){
        when(slackCommand.getText()).thenReturn("status");
    }

    @Test
    public void testProcess(){
        statusCommandProcessor.process(slackCommand, gameInfoList);
    }
}
