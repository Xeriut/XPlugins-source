package net.runelite.client.plugins.xrunedragons.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

@Slf4j
public class PrayTask extends Task {

    @Override
    public boolean validate() {
        if (atDragons() && !prayerUtils.isQuickPrayerActive()) {
            return true;
        }
        return !atDragons() && prayerUtils.isQuickPrayerActive();
    }

    @Override
    public String getTaskDescription() {
        return "Quick prayer";
    }

    @Override
    public void onGameTick(GameTick event) {
        started = true;
        if (atDragons()) {
            if (!prayerUtils.isQuickPrayerActive()) {
                prayerUtils.toggleQuickPrayer(true, sleepDelay());
            }
        }
        if (!atDragons()) {
            if (prayerUtils.isQuickPrayerActive()) {
                prayerUtils.toggleQuickPrayer(false, sleepDelay());
            }
        }
        XRuneDragonsPlugin.timeout = tickDelay();
        finished = true;
    }
}