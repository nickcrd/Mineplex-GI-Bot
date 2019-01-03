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

package com.nickcontrol.gibot.commands.config;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.nickcontrol.gibot.Bot;
import com.nickcontrol.gibot.permissions.Ranks;
import net.dv8tion.jda.core.entities.Game;

public class SetStatusCommand extends Command
{
    public SetStatusCommand()
    {
        this.name = "setstatus";
        this.aliases = new String[]{"setgame"};
        this.help = "Updates the playing status of the bot.";
        this.arguments = "<playing status>";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if (!Ranks.hasRank(event.getMember(), Ranks.Rank.ADMIN))
        {
            event.replyError(event.getAuthor().getAsMention() + ", only **Discord Administrators** are allowed to use this command.");
            return;
        }

        if (event.getArgs().isEmpty())
        {
            event.replyError(event.getAuthor().getAsMention() + ", Usage: ``!" + name + " " + arguments + "``");
            return;
        }

        Bot.getInstance().getJDA().getPresence().setGame(Game.of(Game.GameType.DEFAULT, event.getArgs()));
        event.replySuccess(event.getAuthor().getAsMention() + ", Updated the game status to: " + event.getArgs());

        // Update Config
        Bot.getInstance().getConfig().Game = event.getArgs();
        Bot.getInstance().updateConfig(Bot.getInstance().getConfig());
    }
}
