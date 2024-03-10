package com.goldenfield192.ire.init;

import cam72cam.mod.item.ItemStack;
import com.goldenfield192.ire.IRE;
import com.goldenfield192.ire.items.ConnectorItem;
import com.goldenfield192.ire.items.ItemBattery;
import com.goldenfield192.ire.items.WireItem;

public class ItemsInit {
    public static final ConnectorItem CONNECTOR_ITEM = new ConnectorItem(IRE.MODID,"connector");
    public static final WireItem WIRE_ITEM = new WireItem(IRE.MODID,"wire");
    public static final ItemBattery BATTERY = new ItemBattery(IRE.MODID,"battery");

    public static ItemStack getIcon(){
        return new ItemStack(CONNECTOR_ITEM,1);
    }

    public static void register() {

    }
}
