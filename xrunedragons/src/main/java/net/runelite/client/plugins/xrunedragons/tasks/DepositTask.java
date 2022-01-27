package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class DepositTask extends Task {

    @Override
    public boolean validate() {
        if (bank.isOpen() && (!XRuneDragonsPlugin.deposited || inventory.containsExcept(XRuneDragonsPlugin.inventorySetup))) {
            return true;
        }
        if (bank.isOpen()) {
            if (inventory.getItemCount(XRuneDragonsPlugin.taskConfig.foodID(), false) > XRuneDragonsPlugin.taskConfig.foodAmount()) {
                return true;
            }
            return inventory.getItemCount(ItemID.PRAYER_POTION4, false) > XRuneDragonsPlugin.taskConfig.praypotAmount();
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Deposit items";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        bank.depositAllExcept(XRuneDragonsPlugin.inventorySetup);
        if (inventory.getItemCount(XRuneDragonsPlugin.taskConfig.foodID(), false) > XRuneDragonsPlugin.taskConfig.foodAmount()) {
            bank.depositAllOfItem(XRuneDragonsPlugin.taskConfig.foodID());
        }
        if (inventory.getItemCount(ItemID.PRAYER_POTION4, false) > XRuneDragonsPlugin.taskConfig.praypotAmount()) {
            bank.depositAllOfItem(ItemID.PRAYER_POTION4);
        }
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