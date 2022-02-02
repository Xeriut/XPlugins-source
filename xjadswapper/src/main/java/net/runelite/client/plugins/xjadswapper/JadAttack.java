package net.runelite.client.plugins.xjadswapper;

import net.runelite.api.Prayer;

public class JadAttack {
    public Prayer prayer;
    public int ticks;

    public JadAttack(Prayer prayer, int ticks) {
        this.prayer = prayer;
        this.ticks = ticks;
    }
}
