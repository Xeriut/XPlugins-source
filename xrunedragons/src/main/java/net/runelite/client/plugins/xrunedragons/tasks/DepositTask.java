package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class DepositTask extends Task {

    @Override
    public boolean validate() {
        if(bank.isOpen() && !XRuneDragonsPlugin.deposited) {
            return true;
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Deposit items";
    }

    @Override
    public void onGameTick(GameTick event) {
        utils.sendGameMessage("Already deposited? " + XRuneDragonsPlugin.deposited);
        started = true;
        bank.depositAllExcept(XRuneDragonsPlugin.inventorySetup);
        XRuneDragonsPlugin.deposited = true;
    }

    @Override
    public void checkFinished(GameTick event) {
        if (XRuneDragonsPlugin.deposited = true) {
            XRuneDragonsPlugin.timeout = tickDelay();
            finished = true;
        }
    }
}