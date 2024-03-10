package com.goldenfield192.ire.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import com.goldenfield192.ire.blocks.entity.ConnectorBlockEntity;
import com.goldenfield192.ire.init.TabsInit;

import java.util.Collections;
import java.util.List;

public class WireItem extends CustomItem {
    @TagField("connectedTo")
    private ConnectorBlockEntity connection = null;

    private boolean isConnecting;
    private ConnectorBlockEntity cbe;

    public WireItem(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(TabsInit.tabs.get("catenary"));
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        //获得当前对象
        ConnectorBlockEntity storage = world.getBlockEntity(pos, ConnectorBlockEntity.class);
        if(storage == null){
            return ClickResult.REJECTED;
        }
        if (cbe != null && storage != null && !cbe.getPos().equals(storage.getPos())) {
            if (cbe.getPos().y != storage.getPos().y) {
                player.sendMessage(PlayerMessage.direct("Don't support slope for now!"));
                return ClickResult.PASS;
            }
            player.sendMessage(PlayerMessage.direct("Linked"));
            cbe.addWire(true, storage.getPos().subtract(cbe.getPos()));
            storage.addWire(false, cbe.getPos().subtract(storage.getPos()));
            cbe = null;
        } else if(storage != null){
            cbe = storage;
            player.sendMessage(PlayerMessage.direct("First point set: " + cbe.getPos()));
        } else if(cbe.getPos().equals(storage.getPos())){
            player.sendMessage(PlayerMessage.direct("cannot connect to self!"));
        }
        return ClickResult.ACCEPTED;
    }

    @Override
    public void onClickAir(Player player, World world, Player.Hand hand) {
        this.cbe = null;
    }
}
