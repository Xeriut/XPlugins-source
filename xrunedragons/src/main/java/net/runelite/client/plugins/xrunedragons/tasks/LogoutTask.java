package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class LogoutTask extends Task {

    @Override
    public boolean validate() {
        if (shouldRestock(false) && !inventory.containsItem(ItemID.TELEPORT_TO_HOUSE)) {
           return true;
        }
        if (!atDragons() && !atPOH() && !atEdge() && !atLith() && !inventory.containsItem(ItemID.TELEPORT_TO_HOUSE)) {
            return true;
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Logout";
    }

    @Override
    public void onGameTick() {
        started = true;
        utils.sendGameMessage("I can't teleport. Logging out.");
        interfaceUtils.logout();
        finished = true;
    }
}