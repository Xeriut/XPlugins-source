package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.iutils.LegacyMenuEntry;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class SpecTask extends Task {

    @Override
    public boolean validate() {
        Item mainWeapon = getWeapon(EquipmentInventorySlot.WEAPON.getSlotIdx());
        if (mainWeapon != null) {
            if (atDragons() && mainWeapon.getId() == XRuneDragonsPlugin.taskConfig.specId() && client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) >= XRuneDragonsPlugin.taskConfig.specTreshhold() * 10) {
                if (XRuneDragonsPlugin.currentNPC == XRuneDragonsPlugin.localPlayer.getInteracting()) {
                    return getNpcHealth(XRuneDragonsPlugin.currentNPC, 330) >= XRuneDragonsPlugin.taskConfig.specHp() && client.getVar(VarPlayer.SPECIAL_ATTACK_ENABLED) == 0;
                }
            }
        }

        return false;
    }

    @Override
    public String getTaskDescription() {
        return "Using Spec";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        if (XRuneDragonsPlugin.currentNPC != null) {
            entry = new LegacyMenuEntry("", "", 1, MenuAction.CC_OP.getId(), -1, WidgetInfo.COMBAT_SPECIAL_ATTACK_CLICKBOX.getId(), false);
            utils.doActionMsTime(entry, XRuneDragonsPlugin.currentNPC.getConvexHull().getBounds(), sleepDelay());
        }
        XRuneDragonsPlugin.timeout = tickDelay();
        finished = true;
    }
}