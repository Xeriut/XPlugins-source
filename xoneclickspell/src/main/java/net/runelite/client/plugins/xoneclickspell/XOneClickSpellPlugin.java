package net.runelite.client.plugins.xoneclickspell;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.iutils.LegacyMenuEntry;
import net.runelite.client.plugins.iutils.iUtils;
import net.runelite.client.util.HotkeyListener;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Extension
@PluginDependency(iUtils.class)
@PluginDescriptor(name = "XOneClickSpell", description = "Cast spell on hotkey",
        tags = {"one click", "spell", "auto"},
        enabledByDefault = false)
@Slf4j
public class XOneClickSpellPlugin extends Plugin {

    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private KeyManager keyManager;
    @Inject
    private iUtils utils;
    @Inject
    private XOneClickSpellConfig config;

    LegacyMenuEntry targetMenu = null;
    int timeout = 0;

    private ExecutorService executorService;

    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.hotkey()) {
        @Override
        public void hotkeyPressed() {
            executorService.submit(() ->
            {
                if(timeout == 0) {
                    oneClickCast(config.spellName());
                }
            });
        }
    };
    private final HotkeyListener hotkeyListener2 = new HotkeyListener(() -> config.hotkey2()) {
        @Override
        public void hotkeyPressed() {
            executorService.submit(() ->
            {
                if(timeout == 0) {
                    oneClickCast(config.spellName2());
                }
            });
        }
    };
    private final HotkeyListener hotkeyListener3 = new HotkeyListener(() -> config.hotkey3()) {
        @Override
        public void hotkeyPressed() {
            executorService.submit(() ->
            {
                if(timeout == 0) {
                    oneClickCast(config.spellName3());
                }
            });
        }
    };
    private final HotkeyListener hotkeyListener4 = new HotkeyListener(() -> config.hotkey4()) {
        @Override
        public void hotkeyPressed() {
            executorService.submit(() ->
            {
                log.info("pressed key");
                if(timeout == 0) {
                    oneClickCast(config.spellName4());
                }
            });
        }
    };

    private final void oneClickCast(Spells spell) {
        if(client.getLocalPlayer().getInteracting() != null) {
            NPC npcTarget = (NPC) client.getLocalPlayer().getInteracting();
            if(npcTarget != null) {
                if(npcTarget.getInteracting() == client.getLocalPlayer() || npcTarget.getInteracting() == null) {
                    targetMenu = new LegacyMenuEntry("Cast", "", npcTarget.getIndex(), MenuAction.SPELL_CAST_ON_NPC.getId(),
                            0, 0, false);
                    utils.oneClickCastSpell(spell.getInfo(), targetMenu, npcTarget.getConvexHull().getBounds(), 0);
                    utils.sendGameMessage("Casting: " + spell.getName());
                    timeout = 4;
                }
            }
        }
    }

    @Provides
    XOneClickSpellConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(XOneClickSpellConfig.class);
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if(timeout > 0) timeout--;
    }

    protected void startUp() {
        executorService = Executors.newSingleThreadExecutor();
        keyManager.registerKeyListener(hotkeyListener);
        keyManager.registerKeyListener(hotkeyListener2);
        keyManager.registerKeyListener(hotkeyListener3);
        keyManager.registerKeyListener(hotkeyListener4);
    }

    protected void shutDown() {
        executorService.shutdown();
        keyManager.unregisterKeyListener(hotkeyListener);
        keyManager.unregisterKeyListener(hotkeyListener2);
        keyManager.unregisterKeyListener(hotkeyListener3);
        keyManager.unregisterKeyListener(hotkeyListener4);
    }

}