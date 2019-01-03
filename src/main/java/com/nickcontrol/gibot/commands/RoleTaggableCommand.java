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
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class RoleTaggableCommand extends Command
{
    public RoleTaggableCommand()
    {
        this.name = "t";
        this.help = "Makes a role temporarily taggable.";
        this.botPermissions = new Permission[] {Permission.MANAGE_ROLES};
    }

    @Override
    protected void execute(CommandEvent event)
    {
        // Perm Check
        if (!(event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) && !event.getAuthor().getId().equals(Bot.getInstance().getConfig().BotOwner))
        {
            event.replyError(event.getAuthor().getAsMention() + ", You aren't allowed to do that!");
            event.getMessage().delete().queue();
            return;
        }

        if (!Bot.getInstance().getConfig().Roles.containsKey(event.getGuild().getId()))
        {
            event.replyError(event.getAuthor().getAsMention() + ", This command isn't configured for this guild yet.");
            return;
        }

        if (event.getArgs().length() == 0)
        {
            StringJoiner joiner = new StringJoiner(", ");
            for (String name : Bot.getInstance().getConfig().Roles.get(event.getGuild().getId()).keySet())
                joiner.add(name);

            event.replyError(event.getAuthor().getAsMention() + ", You didn't specify a role. \n\n**Valid Roles:**\n``" + joiner.toString() + "``");
            return;
        }

        String role = event.getArgs().toLowerCase();
        String roleId = Bot.getInstance().getConfig().Roles.get(event.getGuild().getId()).get(role);

        event.getMessage().delete().queue();

        if (roleId == null)
        {
            StringJoiner joiner = new StringJoiner(", ");
            for (String name : Bot.getInstance().getConfig().Roles.get(event.getGuild().getId()).keySet())
                joiner.add(name);

            event.replyError(event.getAuthor().getAsMention() + ", Found no role matching [" + role + "]. \n\n**Valid Roles:**\n``" + joiner.toString() + "``");
            return;
        }

        final Role r = event.getJDA().getRoleById(roleId);
        if (r == null)
        {
            event.replyError(event.getAuthor().getAsMention() + ", Found no role with ID [" + roleId + "]");
            return;
        }

        r.getManager().setMentionable(true).queue();

        Bot.getInstance().getEventWaiter().waitForEvent(GuildMessageReceivedEvent.class,
                e -> e.getMessage().getMentionedRoles().contains(r),
                e -> { r.getManager().setMentionable(false).queue();}, 30, TimeUnit.SECONDS, () -> {
                    r.getManager().setMentionable(false).queue();
                });
    }

}
