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

public class SetVerificationChannel extends Command
{
    public SetVerificationChannel()
    {
        this.name = "setverifychannel";
        this.help = "Sets the verification channel";
        this.arguments = "<channel id>";
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

        if (event.getGuild().getTextChannelById(event.getArgs()) == null)
        {
            event.replyError(event.getAuthor().getAsMention() + ", a channel with ID ``" + event.getArgs() + "`` doesn't exist in this guild.");
            return;
        }

        // Update Config
        Bot.getInstance().getConfig().VerificationChannel.remove(event.getGuild().getId());
        Bot.getInstance().getConfig().VerificationChannel.put(event.getGuild().getId(), event.getArgs());
        Bot.getInstance().updateConfig(Bot.getInstance().getConfig());

        event.replySuccess(event.getAuthor().getAsMention() + ", Changed the verification channel to <#" + event.getArgs() +">.");
    }
}
