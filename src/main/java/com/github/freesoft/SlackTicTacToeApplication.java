package com.github.freesoft;

import com.github.freesoft.actor.DrawCommandProcessor;
import com.github.freesoft.actor.HelpCommandProcessor;
import com.github.freesoft.actor.MarkCommandProcessor;
import com.github.freesoft.actor.StartCommandProcessor;
import com.github.freesoft.actor.StatusCommandProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class SlackTicTacToeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackTicTacToeApplication.class, args);
	}

	@Bean
	public StartCommandProcessor startCommandProcessor(){
		return new StartCommandProcessor();
	}

	@Bean
	public MarkCommandProcessor markCommandProcessor(){
		return new MarkCommandProcessor();
	}

	@Bean
	public StatusCommandProcessor statusCommandProcessor(){
		return new StatusCommandProcessor();
	}

	@Bean
	public DrawCommandProcessor drawCommandProcessor(){
		return new DrawCommandProcessor();
	}

	@Bean
	public HelpCommandProcessor helpCommandProcessor(){
		return new HelpCommandProcessor();
	}

}
