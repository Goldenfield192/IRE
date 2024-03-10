package com.goldenfield192.ire.init;

import cam72cam.mod.item.CreativeTab;
import com.goldenfield192.ire.IRE;

import java.util.HashMap;

public class TabsInit {
    public static HashMap<String,CreativeTab> tabs = new HashMap<>();

    public static void register(){
         tabs.put("catenary", new CreativeTab(IRE.MODID,ItemsInit::getIcon));
    }
}
