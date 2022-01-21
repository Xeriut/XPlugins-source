package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class WithdrawTask extends Task {

    @Override
    public boolean validate() {
        if(bank.isOpen() && XRuneDragonsPlugin.deposited) {
            if (!inventory.containsItem(ItemID.TELEPORT_TO_HOUSE)) {
                return true;
            }
            if (!inventory.containsItem(SUPER_COMBAT_POTS) && !XRuneDragonsPlugin.taskConfig.supercombats()) {
                return true;
            }
            if (!inventory.containsItem(DIVINE_SUPER_COMBAT_POTS) && XRuneDragonsPlugin.taskConfig.supercombats()) {
                return true;
            }
            if (!inventory.containsItem(EXTENDED_ANTIFIRE_POTS) && !XRuneDragonsPlugin.taskConfig.superantifire()) {
                return true;
            }
            if (!inventory.containsItem(SUPER_EXTENDED_ANTIFIRE_POTS) && XRuneDragonsPlugin.taskConfig.superantifire()) {
                return true;
            }
            if (!inventory.containsItem(PRAYER_POTS)) {
                return true;
            }
            if (!inventory.containsItem(XRuneDragonsPlugin.taskConfig.foodID()) || !inventory.containsItemAmount(XRuneDragonsPlugin.taskConfig.foodID(), XRuneDragonsPlugin.taskConfig.foodAmount(), false, true)) {
                return true;
            }
            if(!inventory.containsItem(DIGSITE_PENDANTS) && !XRuneDragonsPlugin.taskConfig.usePOHdigsite()) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Withdraw items";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        if (!inventory.containsItem(ItemID.TELEPORT_TO_HOUSE)) {
            bank.withdrawItemAmount(ItemID.TELEPORT_TO_HOUSE, 10);
        }
        if (!inventory.containsItem(SUPER_COMBAT_POTS) && !XRuneDragonsPlugin.taskConfig.supercombats()) {
            bank.withdrawItem(ItemID.SUPER_COMBAT_POTION4);
        }
        if (!inventory.containsItem(DIVINE_SUPER_COMBAT_POTS) && XRuneDragonsPlugin.taskConfig.supercombats()) {
            bank.withdrawItem(ItemID.DIVINE_SUPER_COMBAT_POTION4);
        }
        if (!inventory.containsItem(EXTENDED_ANTIFIRE_POTS) && !XRuneDragonsPlugin.taskConfig.superantifire()) {
            bank.withdrawItem(ItemID.EXTENDED_ANTIFIRE4);
        }
        if (!inventory.containsItem(SUPER_EXTENDED_ANTIFIRE_POTS) && XRuneDragonsPlugin.taskConfig.superantifire()) {
            bank.withdrawItem(ItemID.EXTENDED_SUPER_ANTIFIRE4);
        }
        if (!inventory.containsItem(PRAYER_POTS) || !inventory.containsItemAmount(ItemID.PRAYER_POTION4, XRuneDragonsPlugin.taskConfig.praypotAmount(), false, true)) {
            int amountInInvetory = inventory.getItemCount(ItemID.PRAYER_POTION4, false);
            bank.withdrawItemAmount(ItemID.PRAYER_POTION4, XRuneDragonsPlugin.taskConfig.praypotAmount() - amountInInvetory);
        }
        if (!inventory.containsItem(XRuneDragonsPlugin.taskConfig.foodID()) || !inventory.containsItemAmount(XRuneDragonsPlugin.taskConfig.foodID(), XRuneDragonsPlugin.taskConfig.foodAmount(), false, true)) {
            int amountInInvetory = inventory.getItemCount(XRuneDragonsPlugin.taskConfig.foodID(), false);
            bank.withdrawItemAmount(XRuneDragonsPlugin.taskConfig.foodID(), XRuneDragonsPlugin.taskConfig.foodAmount() - amountInInvetory);
        }
        if(!inventory.containsItem(DIGSITE_PENDANTS) && !XRuneDragonsPlugin.taskConfig.usePOHdigsite()) {
            bank.withdrawItem(ItemID.DIGSITE_PENDANT_5);
        }
        XRuneDragonsPlugin.timeout = 2 + tickDelay();
        finished = true;
    }
}