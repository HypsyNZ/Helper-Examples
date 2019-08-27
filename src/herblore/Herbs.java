package net.runelite.client.plugins.herblore;

import net.runelite.api.ItemID;

public enum Herbs {
    RANARR(ItemID.GRIMY_RANARR_WEED, ItemID.RANARR_WEED),
    SNAPDRAGON(ItemID.GRIMY_SNAPDRAGON, ItemID.SNAPDRAGON),
    TOADFLAX(ItemID.GRIMY_TOADFLAX, ItemID.TOADFLAX),
    KWUARM(ItemID.GRIMY_KWUARM, ItemID.KWUARM),
    CADANTINE(ItemID.GRIMY_CADANTINE, ItemID.CADANTINE),
    AVANTOE(ItemID.GRIMY_AVANTOE, ItemID.AVANTOE),
    IRIT(ItemID.GRIMY_IRIT_LEAF, ItemID.IRIT_LEAF),
    TORSTOL(ItemID.GRIMY_TORSTOL, ItemID.TORSTOL),
    LANTADYME(ItemID.GRIMY_LANTADYME, ItemID.LANTADYME),
    DWARF_WEED(ItemID.GRIMY_DWARF_WEED, ItemID.DWARF_WEED),
    HARRALANDER(ItemID.GRIMY_HARRALANDER, ItemID.HARRALANDER);

    private final Integer dirty;
    private final Integer clean;

    Herbs(Integer dirty, Integer clean) {
        this.dirty = dirty;
        this.clean = clean;
    }

    public Integer getDirty() {
        return dirty;
    }

    public Integer getClean() {
        return clean;
    }
}

