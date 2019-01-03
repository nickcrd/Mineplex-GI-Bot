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

package com.nickcontrol.gibot.permissions;

import com.nickcontrol.gibot.Bot;
import com.nickcontrol.gibot.config.BotConfig;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nickcontrol.gibot.permissions.Ranks.Rank.ADMIN;

public class Ranks
{
    public static enum Rank
    {
        ADMIN,
        MANAGER,
        STAFF,
        GI,
    }

    public static boolean hasRank(Member user, Rank rank)
    {
        if (Bot.getInstance().getConfig().BotOwner.equals(user.getUser().getId()))
            return true;

        if (Bot.getInstance().getConfig().Ranks == null || Bot.getInstance().getConfig().Ranks.get(user.getGuild().getId()) == null)
            return false;

        Role requirement = Bot.getInstance().getJDA().getRoleById(Bot.getInstance().getConfig().Ranks.get(user.getGuild().getId()).get(rank.name()));
        if (requirement == null)
            return false; // TODO: Log error

        if (rank != ADMIN && hasRank(user, Rank.ADMIN))
            return true;

        return user.getRoles().contains(requirement);
    }
}
