package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.iutils.LegacyMenuEntry;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class EquipTask extends Task {

    @Override
    public boolean validate() {
        Item mainWeapon = getWeapon(EquipmentInventorySlot.WEAPON.getSlotIdx());
        if(mainWeapon != null) {
            if(atDragons()) {
                if(XRuneDragonsPlugin.currentNPC == (NPC) XRuneDragonsPlugin.localPlayer.getInteracting()) {
                    if (client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) >= XRuneDragonsPlugin.taskConfig.specTreshhold()) {
                        if(getNpcHealth(XRuneDragonsPlugin.currentNPC, 330) >= XRuneDragonsPlugin.taskConfig.specHp()) {
                            if(mainWeapon.getId() != XRuneDragonsPlugin.taskConfig.specId()) {
                                if(inventory.getEmptySlots() >= 2) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                if (client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) < XRuneDragonsPlugin.taskConfig.specTreshhold() * 10) {
                    if (mainWeapon.getId() == XRuneDragonsPlugin.taskConfig.specId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Equiping weapon";
    }

    @Override
    public void onGameTick(GameTick event) {
        Item mainWeapon = getWeapon(EquipmentInventorySlot.WEAPON.getSlotIdx());
        started = true;
        if(mainWeapon != null) {
            if (XRuneDragonsPlugin.currentNPC == (NPC) XRuneDragonsPlugin.localPlayer.getInteracting()) {
                if (client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) >= XRuneDragonsPlugin.taskConfig.specTreshhold() * 10) {
                    if (getNpcHealth(XRuneDragonsPlugin.currentNPC, 330) >= XRuneDragonsPlugin.taskConfig.specHp()) {
                        if (mainWeapon.getId() != XRuneDragonsPlugin.taskConfig.specId()) {
                            WidgetItem weapon = inventory.getWidgetItem(XRuneDragonsPlugin.taskConfig.specId());
                            if (weapon != null) {
                                entry = new LegacyMenuEntry("", "", weapon.getId(), MenuAction.ITEM_SECOND_OPTION.getId(), 1, WidgetInfo.INVENTORY.getId(), false);
                                utils.doActionMsTime(entry, weapon.getCanvasBounds(), sleepDelay());
                            }
                            finished = true;
                        }
                    }
                }
            }
            if (client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) < XRuneDragonsPlugin.taskConfig.specTreshhold() * 10) {
                if (mainWeapon.getId() == XRuneDragonsPlugin.taskConfig.specId()) {
                    WidgetItem weapon = inventory.getWidgetItem(XRuneDragonsPlugin.taskConfig.mainId());
                    if (weapon != null) {
                        entry = new LegacyMenuEntry("", "", weapon.getId(), MenuAction.ITEM_SECOND_OPTION.getId(), weapon.getIndex(), WidgetInfo.INVENTORY.getId(), false);
                        utils.doActionMsTime(entry, weapon.getCanvasBounds(), 0);
                    }
                    WidgetItem shield = inventory.getWidgetItem(XRuneDragonsPlugin.taskConfig.shieldId());
                    if (shield != null) {
                        LegacyMenuEntry shieldEntry = new LegacyMenuEntry("", "", shield.getId(), MenuAction.ITEM_SECOND_OPTION.getId(), shield.getIndex(), WidgetInfo.INVENTORY.getId(), false);
                        utils.doActionMsTime(shieldEntry, shield.getCanvasBounds(), sleepDelay());
                    }
                    finished = true;
                }
            }
        }
        XRuneDragonsPlugin.timeout = tickDelay();
        finished = true;
    }
}