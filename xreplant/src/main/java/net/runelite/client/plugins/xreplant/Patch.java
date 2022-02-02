package net.runelite.client.plugins.xreplant;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NpcID;
import net.runelite.api.Varbits;

import java.util.Arrays;

@Getter
@Slf4j
public enum Patch {
    VARROCK_TREE(PatchType.TREE, Varbits.FARMING_4771, PatchGrowthState.TREE, NpcID.TREZNOR, PatchID.VARROCK_TREE.getId()),
    FALADOR_TREE(PatchType.TREE, Varbits.FARMING_4771, PatchGrowthState.TREE, NpcID.HESKEL, PatchID.FALADOR_TREE.getId()),
    TAVERLY_TREE(PatchType.TREE, Varbits.FARMING_4771, PatchGrowthState.TREE, NpcID.ALAIN, PatchID.TAVERLY_TREE.getId()),
    GNOME_STRONGHOLD_TREE(PatchType.TREE, Varbits.FARMING_4771, PatchGrowthState.TREE, NpcID.PRISSY_SCILLA, PatchID.GNOME_STRONGHOLD_TREE.getId()),
    FARMING_GUILD_TREE(PatchType.TREE, Varbits.FARMING_7905, PatchGrowthState.TREE, NpcID.ROSIE, PatchID.FARMING_GUILD_TREE.getId()),
    LUMBRIDGE_TREE(PatchType.TREE, Varbits.FARMING_4771, PatchGrowthState.TREE, NpcID.FAYETH, PatchID.LUMBRIDGE_TREE.getId()),

    CATHERBY_FRUIT_TREEGNOME_STRONGHOLD_FRUIT_TREE(PatchType.FRUIT_TREE, Varbits.FARMING_4771, PatchGrowthState.FRUIT_TREE, NpcID.ELLENA, PatchID.CATHERBY_FRUIT_TREE.getId()),
    TREE_GNOME_VILLAGE_FRUIT_TREE(PatchType.FRUIT_TREE, Varbits.FARMING_4771, PatchGrowthState.FRUIT_TREE, NpcID.GILETH, PatchID.TREE_GNOME_VILLAGE_FRUIT_TREE.getId()),
    BRIMHAVEN_FRUIT_TREE(PatchType.FRUIT_TREE, Varbits.FARMING_4771, PatchGrowthState.FRUIT_TREE, NpcID.GARTH, PatchID.BRIMHAVEN_FRUIT_TREE.getId()),
    FARMING_GUILD_FRUIT_TREE(PatchType.FRUIT_TREE, Varbits.FARMING_7909, PatchGrowthState.FRUIT_TREE, NpcID.NIKKIE, PatchID.FARMING_GUILD_FRUIT_TREE.getId()),
    GNOME_STRONGHOLD_FRUIT_TREE(PatchType.FRUIT_TREE, Varbits.FARMING_4771, PatchGrowthState.FRUIT_TREE, NpcID.BOLONGO, PatchID.GNOME_STRONGHOLD_FRUIT_TREE.getId()),
    PRIFF_FRUIT_TREE(PatchType.FRUIT_TREE, Varbits.FARMING_4771, PatchGrowthState.FRUIT_TREE, NpcID.LILIWEN, PatchID.PRIFF_FRUIT_TREE.getId());

    private final PatchType type;
    private final Varbits varbit;
    private final PatchGrowthState patchGrowthState;
    private final int farmerID;
    private final int patchID;

    private Patch(PatchType type, Varbits varbit, PatchGrowthState patchGrowthState, int farmerID, int patchID) {
        this.type = type;
        this.varbit = varbit;
        this.patchGrowthState = patchGrowthState;
        this.farmerID = farmerID;
        this.patchID = patchID;
    }

    static Patch patchFromObjectID(int objectID) {
        log.info("Checking for object " + objectID);
        return Arrays.stream(Patch.values()).filter(p -> p.getPatchID() == objectID).findFirst().orElse(null);
    }

    static enum PatchType {
        TREE,
        FRUIT_TREE
    }
}