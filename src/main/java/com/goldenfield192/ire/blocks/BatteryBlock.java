package com.goldenfield192.ire.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import com.goldenfield192.ire.blocks.entity.BatteryEntity;

public class BatteryBlock extends BlockTypeEntity{
    public BatteryBlock(String modID, String name) {
        super(modID, name);
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new BatteryEntity();
    }

}
