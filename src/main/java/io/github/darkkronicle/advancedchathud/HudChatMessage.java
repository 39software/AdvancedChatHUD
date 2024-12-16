/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchathud;

import io.github.darkkronicle.advancedchatcore.chat.ChatMessage;
import io.github.darkkronicle.advancedchathud.tabs.AbstractChatTab;
import io.github.darkkronicle.advancedchathud.tabs.CustomChatTab;
import java.util.ArrayList;
import java.util.List;

import io.github.darkkronicle.advancedchathud.tabs.MainChatTab;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class HudChatMessage {

    @Getter private List<AbstractChatTab> tabs;

    @Getter private final ChatMessage message;

    public HudChatMessage(ChatMessage message) {
        this(message, new ArrayList<>());

        setupTabs(AdvancedChatHud.MAIN_CHAT_TAB);
        for (AbstractChatTab tab : tabs) {
            tab.addNewUnread();
        }
    }

    public void setupTabs(MainChatTab mainTab) {
        this.tabs.clear();
        boolean forward = true;
        if (!mainTab.getCustomChatTabs().isEmpty()) {
            for (CustomChatTab tab : mainTab.getCustomChatTabs()) {
                if (!tab.shouldAdd(message.getOriginalText())) {
                    continue;
                }
                if (tabs.contains(tab)) {
                    continue;
                }
                tabs.add(tab);
                if (!tab.isForward()) {
                    forward = false;
                    break;
                }
            }
        }
        if (forward) {
            tabs.add(mainTab);
        }
    }

    public HudChatMessage(ChatMessage message, List<AbstractChatTab> tabs) {
        this.message = message;
        this.tabs = tabs;
    }
}
