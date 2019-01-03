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
import com.nickcontrol.gibot.permissions.Ranks;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class ArchiveCommand extends Command
{
    public ArchiveCommand()
    {
        this.name = "archive";
        this.aliases = new String[]{"a"};
        this.help = "Archives a channel";
        this.arguments = "<channel name>";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if (!Ranks.hasRank(event.getMember(), Ranks.Rank.MANAGER))
        {
            event.replyError(event.getAuthor().getAsMention() + ", only **Discord Managers** are allowed to use this command.");
            return;
        }

        if (event.getArgs().isEmpty())
        {
            event.replyError(event.getAuthor().getAsMention() + ", Usage: ``!" + name + " " + arguments + "``");
            return;
        }

        if (!Bot.getInstance().getConfig().Archive.containsKey(event.getGuild().getId()))
        {
            event.replyError(event.getAuthor().getAsMention() + ", This command isn't configured for this guild yet.");
            return;
        }

        List<TextChannel> matches = event.getGuild().getTextChannelsByName(event.getArgs(), false);

        if (matches.size() != 1)
        {
            event.replyError(event.getAuthor().getAsMention() + ", Found " + (matches.size() == 0 ? "no" : "multiple") + " matches for `" + event.getArgs() + "``.");
            return;
        }

        net.dv8tion.jda.core.entities.Category category = event.getGuild().getCategoryById(Bot.getInstance().getConfig().Archive.get(event.getGuild().getId()));

        if (category == null)
        {
            event.replyError(event.getAuthor().getAsMention() + ", The archive specified in the config doesn't seem to exist.");
            return;
        }

        matches.get(0).getManager().setParent(category).clearOverridesRemoved().clearOverridesAdded().queue(success -> {
            event.replySuccess(event.getAuthor().getAsMention() + ", Channel has been archived.");
            matches.get(0).sendMessage("This channel has been archived by " + event.getAuthor().getAsMention() + ".").queue();
        }, err -> event.replyError(event.getAuthor().getAsMention() + ", An error occurred: " + err.getMessage()));

    }
}
