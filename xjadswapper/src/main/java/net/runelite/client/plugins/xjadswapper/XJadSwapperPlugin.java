package net.runelite.client.plugins.xjadswapper;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.iutils.PrayerUtils;
import net.runelite.client.plugins.iutils.iUtils;
import net.runelite.client.util.HotkeyListener;
import org.apache.commons.lang3.ArrayUtils;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Extension
@PluginDependency(iUtils.class)
@PluginDescriptor(name = "XJadSwapper", description = "Auto pray against Jad",
        tags = {"prayer", "swap", "auto", "jad"},
        enabledByDefault = false)
@Slf4j
public class XJadSwapperPlugin extends Plugin {

    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private KeyManager keyManager;
    @Inject
    private PrayerUtils prayer;
    @Inject
    private iUtils utils;
    @Inject
    private XJadSwapperConfig config;
    @Inject
    private ChatMessageManager chatMessageManager;

    private ExecutorService executorService;

    private boolean enabled = false;

    public static final int JALTOK_JAD_MAGE_ATTACK = 7592;
    public static final int JALTOK_JAD_RANGE_ATTACK = 7593;

    private static final int FIGHT_CAVE_REGION = 9551;
    private static final int INFERNO_REGION = 9043;

    private List<JadAttack> prayerQueue = new ArrayList<JadAttack>();

    Player player;

    @Provides
    XJadSwapperConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(XJadSwapperConfig.class);
    }

    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.hotkey()) {
        @Override
        public void hotkeyPressed() {
            executorService.submit(() ->
            {
                if(enabled == true) {
                    sendGameMessage("Jad prayer swapper disabled.", Color.RED);
                    reset();
                } else {
                    sendGameMessage("Jad prayer swapper enabled.", Color.GREEN);
                    enabled = true;
                }
            });
        }
    };

    private void reset() {
        prayerQueue.clear();
        enabled = false;
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        player = client.getLocalPlayer();
        if(enabled) {
            if (client != null && player != null && client.getGameState() == GameState.LOGGED_IN) {
                if (atRegion()) {
                    if (!prayerQueue.isEmpty()) {
                        offensivePrayer();
                        for (JadAttack attack : prayerQueue) {
                            attack.ticks--;
                            if (attack.ticks == 0) {
                                if (!client.isPrayerActive(attack.prayer)) {
                                    prayer.toggle(attack.prayer, 0);
                                }
                                prayerQueue.remove(attack);
                            }
                        }
                    }
                } else {
                    if (!prayerQueue.isEmpty()) prayerQueue.clear();
                }
            }
        }
    }

    protected void startUp() {
        executorService = Executors.newSingleThreadExecutor();
        keyManager.registerKeyListener(hotkeyListener);
        reset();
    }

    protected void shutDown() {
        executorService.shutdown();
        keyManager.unregisterKeyListener(hotkeyListener);
        reset();
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event)
    {
        if(atRegion() && enabled) {
            Actor actor = event.getActor();

            if (actor == null) {
                return;
            }

            switch (actor.getAnimation()) {
                case AnimationID.TZTOK_JAD_MAGIC_ATTACK:
                case JALTOK_JAD_MAGE_ATTACK:
                    log.info("Adding PROTECT FROM MAGIC");
                    prayerQueue.add(new JadAttack(Prayer.PROTECT_FROM_MAGIC, 2));
                    break;
                case AnimationID.TZTOK_JAD_RANGE_ATTACK:
                case JALTOK_JAD_RANGE_ATTACK:
                    log.info("Adding PROTECT FROM RANGE");
                    prayerQueue.add(new JadAttack(Prayer.PROTECT_FROM_MISSILES, 2));
                    break;
                default:
                    break;
            }
        }
    }

    private boolean atRegion() {
        return ArrayUtils.contains(client.getMapRegions(), FIGHT_CAVE_REGION) || ArrayUtils.contains(client.getMapRegions(), INFERNO_REGION);
    }

    private void offensivePrayer() {
        if(config.offensivePray() && !client.isPrayerActive(Prayer.RIGOUR)) {
            prayer.toggle(Prayer.RIGOUR, 0);
        }
    }

    private void sendGameMessage(String message, Color color) {
        String chatMessage = new ChatMessageBuilder()
                .append(color, message)
                .build();

        chatMessageManager
                .queue(QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE)
                        .runeLiteFormattedMessage(chatMessage)
                        .build());
    }
}