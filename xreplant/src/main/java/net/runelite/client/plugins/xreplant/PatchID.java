package net.runelite.client.plugins.xreplant;

import java.util.HashMap;
import java.util.Map;

public enum PatchID {
    /**
     * Tree patch IDs
     */
    FALADOR_TREE(8389),
    VARROCK_TREE(8390),
    LUMBRIDGE_TREE(8391),
    TAVERLY_TREE(8388),
    GNOME_STRONGHOLD_TREE(19147),
    FARMING_GUILD_TREE(33732),

    /**
     * Menu action triggered when the id is not defined in this class.
     */
    UNKNOWN(-1);

    private static final Map<Integer, PatchID> map = new HashMap<>();

    static
    {
        for (PatchID patchID : values())
        {
            map.put(patchID.getId(), patchID);
        }
    }

    private final int id;

    PatchID(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public static PatchID of(int id)
    {
        return map.getOrDefault(id, UNKNOWN);
    }
}
