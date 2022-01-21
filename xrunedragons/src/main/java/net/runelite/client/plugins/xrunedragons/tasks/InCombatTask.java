package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class InCombatTask extends Task {

    @Override
    public boolean validate() {
        if (atDragons()) {
            if (XRuneDragonsPlugin.localPlayer.getInteracting() != null) {
                if(XRuneDragonsPlugin.currentNPC == (NPC) XRuneDragonsPlugin.localPlayer.getInteracting()) {
                    return true;
                }
                if (XRuneDragonsPlugin.currentNPC != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "In combat";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        finished = true;
    }
}