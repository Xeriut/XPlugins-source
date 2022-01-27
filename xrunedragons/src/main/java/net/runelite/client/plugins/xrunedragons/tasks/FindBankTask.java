package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameObject;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.iutils.LegacyMenuEntry;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class FindBankTask extends Task {

    @Override
    public boolean validate() {
        if (atEdge()) {
            return shouldRestock(false);
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Find the bank";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        GameObject bankTarget = object.findNearestBank();
        if (bankTarget != null) {
            entry = new LegacyMenuEntry("", "", bankTarget.getId(),
                    bank.getBankMenuOpcode(bankTarget.getId()), bankTarget.getSceneMinLocation().getX(),
                    bankTarget.getSceneMinLocation().getY(), false);
            utils.doActionMsTime(entry, bankTarget.getConvexHull().getBounds(), sleepDelay());
        }
    }

    @Override
    public void checkFinished(GameTick event) {
        if (bank.isOpen()) {
            XRuneDragonsPlugin.timeout = tickDelay();
            finished = true;
        }
    }
}