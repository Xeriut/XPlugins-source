package net.runelite.client.plugins.xrunedragons;

import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.ColorUtil;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

@Singleton
class XRuneDragonsOverlay
        extends OverlayPanel {
    private static final Logger logger = LoggerFactory.getLogger(net.runelite.client.plugins.xrunedragons.XRuneDragonsOverlay.class);
    private final XRuneDragonsPlugin plugin;
    private final XRuneDragonsConfig config;
    String a;
    private String status = "Starting...";

    @Inject
    private XRuneDragonsOverlay(Client client, XRuneDragonsPlugin xRuneDragonsPlugin, XRuneDragonsConfig xRuneDragonsConfig) {
        super((Plugin)xRuneDragonsPlugin);
        setPosition(OverlayPosition.BOTTOM_LEFT);
        plugin = xRuneDragonsPlugin;
        config = xRuneDragonsConfig;
        getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "airs overlay"));
    }

    public Dimension render(Graphics2D graphics2D) {
        if (plugin.botTimer == null) {
            logger.debug("Overlay conditions not met, not starting overlay");
            return null;
        }
        TableComponent tableComponent = new TableComponent();
        tableComponent.setColumnAlignments(new TableAlignment[]{TableAlignment.LEFT, TableAlignment.RIGHT});
        Duration duration = Duration.between(plugin.botTimer, Instant.now());
        a = duration.toHours() < 1L ? "mm:ss" : "HH:mm:ss";
        tableComponent.addRow(new String[]{"Time running:", DurationFormatUtils.formatDuration(duration.toMillis(), a)});

        if (plugin.status != null) {
            status = plugin.status;
        } else {
            status = "Waiting...";
        }
        tableComponent.addRow(new String[]{"Status:", status});
        tableComponent.addRow(new String[]{"Total kills:", String.valueOf(plugin.killCount)});


        TableComponent tc = new TableComponent();
        tc.setColumnAlignments(new TableAlignment[]{TableAlignment.LEFT, TableAlignment.RIGHT});
        if (tableComponent.isEmpty()) return super.render(graphics2D);
        panelComponent.setBackgroundColor(ColorUtil.fromHex((String)"#121212"));
        panelComponent.setPreferredSize(new Dimension(200, 200));
        panelComponent.setBorder(new Rectangle(5, 5, 5, 5));
        panelComponent.getChildren().add(TitleComponent.builder().text("Rune Dragon Killer").color(ColorUtil.fromHex((String)"#40C4FF")).build());
        panelComponent.getChildren().add(tableComponent);
        panelComponent.getChildren().add(tc);
        return super.render(graphics2D);
    }
}

