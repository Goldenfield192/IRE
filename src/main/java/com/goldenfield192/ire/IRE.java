package com.goldenfield192.ire;

import cam72cam.mod.ModCore;
import cam72cam.mod.ModEvent;
import cam72cam.mod.render.BlockRender;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.resource.Identifier;
import com.goldenfield192.ire.tiles.TIleBattery;
import com.goldenfield192.ire.tiles.TileConnector;
import com.goldenfield192.ire.init.BlocksInit;
import com.goldenfield192.ire.init.ItemsInit;
import com.goldenfield192.ire.init.TabsInit;
import com.goldenfield192.ire.renderer.ConnectorRenderer;
import com.goldenfield192.ire.renderer.SimpleBlockRenderer;
import com.goldenfield192.ire.util.graph.GraphHandler;

@net.minecraftforge.fml.common.Mod(modid = IRE.MODID, name = "IRE", version = "0.0.1", dependencies = "required-after:immersiverailroading@[1.10, 1.11)", acceptedMinecraftVersions = "[1.12,1.13)")
public class IRE extends ModCore.Mod{
    public static final String MODID = "ir_extension";

    static {
        try {
            ModCore.register(new IRE());
        } catch (Exception e) {
            throw new RuntimeException("Could not load mod " + MODID, e);
        }
    }

    public IRE(){

    }

    @Override
    public String modID() {
        return MODID;
    }


    @Override
    public void commonEvent(ModEvent event) {
        switch (event){
            case CONSTRUCT:
                GraphHandler.init();

                BlocksInit.register();
                TabsInit.register();
                ItemsInit.register();
                break;
            case INITIALIZE:
            case SETUP:
            default:
                break;
        }
    }

    @Override
    public void clientEvent(ModEvent event) {
        switch (event){
            case CONSTRUCT:
                BlockRender.register(BlocksInit.CONNECTOR_BLOCK, ConnectorRenderer::render, TileConnector.class);
                BlockRender.register(BlocksInit.BATTERY_BLOCK, SimpleBlockRenderer::render, TIleBattery.class);

                ItemRender.register(ItemsInit.CONNECTOR_ITEM,new Identifier(MODID,"items/guanmu"));
                ItemRender.register(ItemsInit.WIRE_ITEM,new Identifier(MODID,"items/guanmu"));
                ItemRender.register(ItemsInit.BATTERY,new Identifier(MODID,"items/guanmu"));
                break;
            case INITIALIZE:
            case SETUP:
            default:
                break;
        }
    }

    @Override
    public void serverEvent(ModEvent event) {
        //Do nothing here
    }
}
