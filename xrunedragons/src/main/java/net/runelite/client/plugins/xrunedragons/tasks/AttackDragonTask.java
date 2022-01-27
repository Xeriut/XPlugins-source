package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iutils.LegacyMenuEntry;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class AttackDragonTask extends Task {

    @Override
    public boolean validate() {
        if (atDragons()) {
            if (XRuneDragonsPlugin.localPlayer.getInteracting() != null) {
                if (XRuneDragonsPlugin.currentNPC != XRuneDragonsPlugin.localPlayer.getInteracting()) {
                    XRuneDragonsPlugin.currentNPC = (NPC) XRuneDragonsPlugin.localPlayer.getInteracting();
                    if (XRuneDragonsPlugin.currentNPC != null) {
                        return true;
                    }
                }
            }
            if (XRuneDragonsPlugin.currentNPC == null) {
                XRuneDragonsPlugin.currentNPC = findRuneDragon();
                return true;
            }
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Attack dragon";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        if (XRuneDragonsPlugin.currentNPC != null) {
            entry = new LegacyMenuEntry("", "", XRuneDragonsPlugin.currentNPC.getIndex(), MenuAction.NPC_SECOND_OPTION.getId(),
                    0, 0, false);
            utils.doActionMsTime(entry, XRuneDragonsPlugin.currentNPC.getConvexHull().getBounds(), sleepDelay());
        }
        XRuneDragonsPlugin.timeout = tickDelay();
        finished = true;
    }
}