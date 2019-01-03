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

package com.nickcontrol.gibot.config;

import java.util.ArrayList;
import java.util.HashMap;

public class BotConfig
{
    public String Token;

    public String Game;

    public String BotOwner;

    public HashMap<String, HashMap<String, String>> Ranks;
    public HashMap<String, HashMap<String, String>> Roles;

    public HashMap<String, String> Archive;

    public String Keyword;
    public HashMap<String, String> VerificationChannel;
    public HashMap<String, String> MemberRoleID;

}
