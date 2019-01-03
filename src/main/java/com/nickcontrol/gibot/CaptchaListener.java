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

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CaptchaListener extends ListenerAdapter
{
    private ConcurrentHashMap<String, String> userCache;

    public CaptchaListener()
    {
        userCache = new ConcurrentHashMap<>();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getChannel().getId().equals(Bot.getInstance().getConfig().VerificationChannel.get(event.getGuild().getId())) && event.getMessage().getContentRaw().equalsIgnoreCase(Bot.getInstance().getConfig().Keyword))
        {
            generateNewCaptcha(event.getMember(), event.getChannel());
        }
    }

    public void generateNewCaptcha(Member user, TextChannel channel)
    {
        if (userCache.contains(user.getUser().getId()))
            return;

        String code = String.format("%06d", new Random().nextInt(999999));

        channel.sendMessage(Util.embedInfo("Verification Required", "Please type the following code below within the next 30 Seconds to gain access to the server\n\n``" + code + "``", ""))
                .queue(msg -> {
                    userCache.put(user.getUser().getId(), msg.getId());
                    Bot.getInstance().getEventWaiter().waitForEvent(GuildMessageReceivedEvent.class,
                            e -> e.getChannel().getId().equals(channel.getId()) && e.getMessage().getContentRaw().contains(code) && e.getAuthor().getId().equals(user.getUser().getId()),
                            e -> doVerify(e.getMember()), 30, TimeUnit.SECONDS, () -> {
                        String msgId = userCache.remove(user.getUser().getId());
                        if (msgId != null)
                            channel.getMessageById(msgId).queue(message ->
                                    message.editMessage(Util.embedInfo("Verification Required", "Please type the following code below within the next 30 Seconds to gain access to the server\n\n``>> CODE EXPIRED <<``", "")).queue());
                    });
                }
        );

    }

    public void doVerify(Member user)
    {
        user.getGuild().getController().removeSingleRoleFromMember(user, Bot.getInstance().getJDA().getRoleById(Bot.getInstance().getConfig().MemberRoleID.get(user.getGuild().getId()))).queue();
        userCache.remove(user.getUser().getId());
    }

}
