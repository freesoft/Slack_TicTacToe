[![Build Status](https://travis-ci.org/freesoft/Slack_TicTacToe.svg?branch=master)](https://travis-ci.org/freesoft/Slack_TicTacToe)

# Tic-tac-toe custom command program for Slack channel

This project implements tic-tac-toe (3x3 for now) playable in Slack channel using Spring Boot. You only needs built jar file to run it in the server(Heroku, your local PC, wherever) + JVM installed. To build a project, you need Maven 3+, Java 8+ installed in the build machine.


# How to build & run server

if you have maven 3+ and Java 8 installed, then

```
mvn clean install
mvn spring-boot:run
```

will start tic-tac-toe server with 8080 listening port

if not using spring-boot:run with mvn or want to run using built JAR file only using Java( or if you just have JAR file ), then

```
java -jar slack_tictactoe*.jar
```

will also work. Or you can simply click following "deploy to Heroku" button if running on Heroku. The link will ask you to add Slack token when creating new app & runnning on Heroku.
[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

# Set up a Slack channel

This is Slack custom command server side code, and developed base on the documentation at https://api.slack.com/slash-commands.

The code is designed to take HTTP POST only, so you need to set up Method to POST at your Slack team's Slash Command section. Also, you need a public URL that's accessible by Slack side.
You can either use your own PC and use DDNS to have fixed domain, or Heroku/Google App Engine will also work as slong as it's exposed on public internet and accessible.

For more about Slack API and it's custom integration, check https://api.slack.com page.

# Slack token variable set up

Slack and this server recognize each other by Slack token variable in HTTP POST message, which is issued by Slack. Each Slack team has different token variable for their bot or custom command, 
so you need to get your team's Slack app token, and set the value in the server OS ( where you run this program ) as SLACK_TTT_TOKEN environment variable. 
If you are using Heroku, you can simply
```
heroku config:set SLACK_TTT_TOKEN=your token value
```
in Heroku CLI, or through your Heroku web interface.

Also, to be able to check if opponent exist in the Slack channel and eligible to play, you need to create bot and invite it to the channel you'd like to play a game.
What you actually need is API key generated when you set a bot in your team Slack. This server code is going to use Bot API token to access your team Slack and check user information.
Same as SLACK_TTT_TOKEN, but now you need to set SLACK_BOT_TOKEN. If you install this application on Heroku using "deploy to Heroku" button, Heroku will ask you to add SLACK_BOT_TOKEN.
Or you can do it on Heroku config var page or through Heroku CLI.
```
heroku config:set SLACK_BOT_TOKEN=your token value
```

If not, than you can simly define those in environment variable of your OS, or through JVM parameter.

# How to Play

To start new tic-tac-toc, you need to input following command with username on the channel.

```
/ttt start [opponent_username]
```

Above command create default 3x3 board size tic-tac-toe, so if you want bigger board size, you can 

```
/ttt start [opponent_username] boardsize
```

where board size is odd number.

if you registered your Slash command someting different than /ttt, the you can use whatever command you registered.

```
/[your command registered] start [user_name]
```

Current code doesn't check if given username exists in the channel or not. That said, if you choose username that doesn't exist, then game won't be continued even after you finsh your turn.

After started the game, same person who started game need to make first move.

```
/ttt mark row col
```

where row and col means each row and col's index starting with zero. So, if you want to mark position (1,1), then

```
/ttt mark 1 1
```

If you don't want to keep continuing the game, then you can draw the game using following command.

```
/ttt draw
```

You can only draw the game whenever you want for now since current code doesn't know if opponent username exists in the channel when game is created, and starting a game with non-existing username will cause the game to wait forever.


# Copyright and License

Copyright (c) 2016 Wonhee Jung, the code is distrubited under [MIT License](LICENSE.md).
