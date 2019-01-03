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

import java.util.HashMap;

public class AddRoleCommand extends Command
{
    public AddRoleCommand()
    {
        this.name = "addrole";
        this.help = "Adds a role to the bots ping system";
        this.arguments = "<name> <role id>";
    }

    @Override
    protected void execute(CommandEvent event)
    {
        if (!Ranks.hasRank(event.getMember(), Ranks.Rank.ADMIN))
        {
            event.replyError(event.getAuthor().getAsMention() + ", only **Discord Administrators** are allowed to use this command.");
            return;
        }

        String[] args = event.getArgs().split("\\s");

        if (args.length < 2)
        {
            event.replyError(event.getAuthor().getAsMention() + ", Usage: ``!" + name + " " + arguments + "``");
            return;
        }

        Bot.getInstance().getConfig().Roles.computeIfAbsent(event.getGuild().getId(), k -> new HashMap<>());

        if (Bot.getInstance().getConfig().Roles.get(event.getGuild().getId()).containsKey(args[0]))
        {
            event.replyError(event.getAuthor().getAsMention() + ", a role named ``" + args[0] + "`` already exists.");
            return;
        }

        if (event.getGuild().getRoleById(args[1]) == null)
        {
            event.replyError(event.getAuthor().getAsMention() + ", a role with ID ``" + args[0] + "`` doesn't exist or isn't part of this guild.");
            return;
        }

        // Update Config
        Bot.getInstance().getConfig().Roles.get(event.getGuild().getId()).put(args[0], args[1]);
        Bot.getInstance().updateConfig(Bot.getInstance().getConfig());

        event.replySuccess(event.getAuthor().getAsMention() + ", Added ``" + args[0] + "`` to the Bot. You can now use !t " + args[0] + " to make the role pingable.");
    }
}
