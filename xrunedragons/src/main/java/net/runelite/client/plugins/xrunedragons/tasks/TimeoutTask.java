package net.runelite.client.plugins.xrunedragons.tasks;

import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.xrunedragons.Task;
import net.runelite.client.plugins.xrunedragons.XRuneDragonsPlugin;

public class TimeoutTask extends Task {
    @Override
    public boolean validate() {
        return XRuneDragonsPlugin.timeout > 0;
    }

    @Override
    public String getTaskDescription() {
        return "Timeout: " + XRuneDragonsPlugin.timeout;
    }

    @Override
    public void onGameTick() {
        started = true;
        XRuneDragonsPlugin.timeout--;
        finished = true;
    }
}