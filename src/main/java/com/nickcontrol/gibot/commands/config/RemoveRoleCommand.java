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

import java.util.HashMap;

public class RemoveRoleCommand extends Command
{
    public RemoveRoleCommand()
    {
        this.name = "removerole";
        this.help = "Removes a role from the ping system.";
        this.arguments = "<name>";
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

        Bot.getInstance().getConfig().Roles.computeIfAbsent(event.getGuild().getId(), k -> new HashMap<>());

        if (!(Bot.getInstance().getConfig().Roles.get(event.getGuild().getId()).containsKey(event.getArgs())))
        {
            event.replyError(event.getAuthor().getAsMention() + ", a role named ``" + event.getArgs() + "`` doesn't exist in this guild.");
            return;
        }

        // Update Config
        Bot.getInstance().getConfig().Roles.get(event.getGuild().getId()).remove(event.getArgs());
        Bot.getInstance().updateConfig(Bot.getInstance().getConfig());

        event.replySuccess(event.getAuthor().getAsMention() + ", Removed  ``" + event.getArgs() + "`` from the Bot. You can no longer use !t command to make it pingable.");
    }
}
