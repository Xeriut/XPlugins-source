package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.Task;

@Slf4j
public class LogoutTask extends Task {

    @Override
    public boolean validate() {
        if (shouldRestock(false) && !inventory.containsItem(ItemID.TELEPORT_TO_HOUSE)) {
            return true;
        }
        return !atDragons() && !atPOH() && !atEdge() && !atLith() && !inventory.containsItem(ItemID.TELEPORT_TO_HOUSE);
    }

    @Override
    public String getTaskDescription() {
        return "Logout";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        utils.sendGameMessage("I can't teleport. Logging out.");
        interfaceUtils.logout();
        finished = true;
    }
}