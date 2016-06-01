package com.github.freesoft.actor;

import com.github.freesoft.model.Game;
import com.github.freesoft.model.SlackCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

/**
 * @author wonhee jung
 */
@RunWith(MockitoJUnitRunner.class)
public class DrawCommandProcessorTest {

    @InjectMocks
    private DrawCommandProcessor drawCommandProcessor;

    @Mock
    private SlackCommand slackCommand;

    @Mock
    private Map<String,Game> gameInfoList;


    @Test
    public void testProcess(){
        drawCommandProcessor.process(slackCommand, gameInfoList);

    }
}
