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

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

public class Util
{
    public static MessageEmbed embedInfo(String title, String message, String footer, MessageEmbed.Field... fields)
    {
        EmbedBuilder build = new EmbedBuilder();
        build.setColor(new Color(30, 144, 255));
        build.setAuthor(title.toUpperCase() + " »", null, "https://cdn.discordapp.com/emojis/459435852284821515.png?v=1");
        build.setDescription(message);
        build.setFooter(footer, null);

        for (MessageEmbed.Field field : fields)
            build.addField(field);

        return build.build();
    }
}
