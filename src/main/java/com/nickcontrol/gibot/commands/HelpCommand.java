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

package com.nickcontrol.gibot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.nickcontrol.gibot.Bot;
import com.nickcontrol.gibot.Util;

import java.util.StringJoiner;

public class HelpCommand extends Command
{
    public HelpCommand()
    {
        this.name = "help";
        this.help = "Shows this lovely help menu!";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        StringJoiner joiner = new StringJoiner("\n");
        for (Command command : Bot.getInstance().getCommandClient().getCommands())
            joiner.add("• ``!" + command.getName() + " " + (command.getArguments() == null ? "" : command.getArguments()) + "``" + (command.getHelp().equals("no help available") ? "" : " - " + command.getHelp()));

        event.reactSuccess();
        event.reply(Util.embedInfo("Bot Help",
                "Command List:\n" + joiner.toString(), "This bot was developed by Nick#0418 to be used within GI Discords"));
    }
}
