/*
 *
 * Copyright (c) 2019 NICKCONTROL. All rights reserved.
 *
 * This file/repository is proprietary code. You are expressly prohibited from disclosing, publishing,
 * reproducing, or transmitting the content, or substantially similar content, of this repository, in whole or in part,
 * in any form or by any means, verbal or written, electronic or mechanical, for any purpose.
 * By browsing the content of this file/repository, you agree not to disclose, publish, reproduce, or transmit the content,
 * or substantially similar content, of this file/repository, in whole or in part, in any form or by any means, verbal or written,
 * electronic or mechanical, for any purpose.
 *
 */

package com.nickcontrol.gibot;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.nickcontrol.gibot.commands.ArchiveCommand;
import com.nickcontrol.gibot.commands.HelpCommand;
import com.nickcontrol.gibot.commands.RoleTaggableCommand;
import com.nickcontrol.gibot.commands.config.*;
import com.nickcontrol.gibot.config.BotConfig;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Bot
{
    private static Bot _instance;
    public static Bot getInstance()
    {
        return _instance;
    }

    private Gson _gson = new Gson();
    private EventWaiter _eventWaiter;

    private JDA _jda;
    private CommandClient _client;

    private BotConfig _config;
    private String _configFile = "config.txt";

    public static void main(String[] args)
    {
        _instance = new Bot(args);
        _instance.init();
    }

    public Bot(String[] args)
    {
        if (args != null && args.length == 1)
        {
            _configFile = args[0];
        }

        _config = loadConfig();

        if (_config == null) {
            System.err.println("The config isn't valid! Make sure you specified the correct path to config and if the config file exists.");
            System.exit(0);
            return;
        }

        CommandClientBuilder builder = new CommandClientBuilder();

        builder.setPrefix("!");

        builder.setGame(Game.of(Game.GameType.DEFAULT, _config.Game));

        builder.setEmojis(":white_check_mark:", ":warning:", ":x:");

        builder.addCommands(
                new RoleTaggableCommand(),
                new SetStatusCommand(),
                new AddRoleCommand(),
                new RemoveRoleCommand(),
                new SetKeywordCommand(),
                new SetMemberRoleCommand(),
                new SetVerificationChannel(),
                new SetArchiveCommand(),
                new ArchiveCommand(),
                new HelpCommand()
        );

        builder.setOwnerId(_config.BotOwner);

        builder.setHelpWord("nani");
        builder.setHelpConsumer( (event -> {}));

        _eventWaiter = new EventWaiter();

        _client = builder.build();

        try {
            _jda = new JDABuilder(AccountType.BOT).setToken(_config.Token)
                    .setGame(Game.of(Game.GameType.DEFAULT, "Loading...")).addEventListener(_client).addEventListener(_eventWaiter).buildBlocking();

        } catch (LoginException | IllegalArgumentException | InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void init()
    {
        _jda.addEventListener(new CaptchaListener());
    }

    public BotConfig loadConfig()
    {
        try
        {
            if (new File(_configFile) == null)
                return null;

            String json = Files.lines(Paths.get(_configFile)).findFirst().get();

            if (json != null)
                return _gson.fromJson(json, BotConfig.class);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void updateConfig(BotConfig config)
    {
        try
        {
            Files.write(Paths.get(_configFile), Arrays.asList(_gson.toJson(config)), Charset.forName("UTF-8"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public BotConfig getConfig() {
        return _config;
    }

    public EventWaiter getEventWaiter() {
        return _eventWaiter;
    }

    public JDA getJDA()
    {
        return _jda;
    }

    public CommandClient getCommandClient()
    {
        return _client;
    }
}
