package com.goldenfield192.ire.blocks.entity;

import cam72cam.mod.block.BlockEntityTickable;
import cam72cam.mod.energy.Energy;
import cam72cam.mod.energy.IEnergy;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.entity.sync.TagSync;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.*;
import cam72cam.mod.util.Facing;
import com.goldenfield192.ire.init.ItemsInit;
import com.goldenfield192.ire.serializer.MapVec3iBooleanMapper;
import com.goldenfield192.ire.util.graph.DuplicateGraphException;
import com.goldenfield192.ire.util.graph.GraphHandler;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.*;

import static com.goldenfield192.ire.util.graph.GraphHandler.addConnector;

//邻接表
public class ConnectorBlockEntity extends BlockEntityTickable {
    @TagField("facing")
    private int rotation;

    @TagField("offset")
    public Vec3d inBlockOffset;

    @TagField("this")
    public UUID uuid;

    @TagField("energy")
    @TagSync
    private Energy energy = new Energy(0,5000);

    //连通分量编号
    public int subGraphID;

    public HashMap<Vec3i, Boolean> getConnection() {
        return connection;
    }

    public void setConnection(HashMap<Vec3i, Boolean> connection) {
        this.connection = connection;
    }

    //TODO 如果启用现有图系统就不能正常序列化connection，怎么回事呢
    //Solution 用getter
    @TagField(value = "connect", mapper = MapVec3iBooleanMapper.class)//存相对方块位置
    @TagSync
    private HashMap<Vec3i, Boolean> connection = new HashMap<>();

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    private boolean lastTryBreakByCreativePlayer = false;

    public ConnectorBlockEntity(String facing) {
        super();
        switch (facing){
            case "NORTH":
                rotation = 180;
                break;
            case "WEST":
                rotation = -90;
                break;
            case "EAST":
                rotation = 90;
        }
        inBlockOffset = new Vec3d(0,0.74,1.13);
        uuid = UUID.randomUUID();
        try {
            TagCompound tc = new TagCompound()
                    .setInteger("rotation", rotation)
                    .setVec3d("offset",inBlockOffset)
                    .setUUID("this",uuid);
            save(tc);
        }catch (SerializationException ignore){}
    }

    @Override
    public ItemStack onPick() {
        return new ItemStack(ItemsInit.CONNECTOR_ITEM,1);
    }

    @Override
    public boolean onClick(Player player, Player.Hand hand, Facing facing, Vec3d hit) {
        GraphHandler.loadWorld(getWorld());
        addConnector(this,this.getWorld());
        if(getWorld().isServer && hand == Player.Hand.PRIMARY){
            try {
                GraphHandler.getWorld(getWorld()).printContain(player, this, player.isCrouching());
            } catch (DuplicateGraphException e) {
                throw new RuntimeException(e);
            }
        }
        return super.onClick(player, hand, facing, hit);
    }

    @Override
    public boolean tryBreak(Player player) {
        if(player.isCreative())
            lastTryBreakByCreativePlayer = true;
        return true;
    }

    @Override
    public void onBreak() {
        if (!lastTryBreakByCreativePlayer) {
            getWorld().dropItem(onPick(), getPos());
        }
        removeWire();
        try {
            GraphHandler.getWorld(getWorld()).remove(this);
            GraphHandler.update(getWorld());
        } catch (DuplicateGraphException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ConnectorBlockEntity that = (ConnectorBlockEntity) o;

        return new EqualsBuilder()
                .append(this.getPos().x,that.getPos().x)
                .append(this.getPos().y,that.getPos().y)
                .append(this.getPos().z,that.getPos().z)
                .isEquals();
    }

    public void addWire(boolean isFirst,Vec3i relativeTarget){
        this.connection.put(relativeTarget,isFirst);
        if(isFirst){
            //vecToBlocks(this,getWorld().getBlockEntity(this.getPos().add(relativeTarget), ConnectorBlockEntity.class), BlocksInit.BATTERY_BLOCK);
        }
        GraphHandler.update(getWorld());
        markDirty();
    }

    public void removeWire(){
        this.connection.forEach(((vec3i, bool) -> {
            ConnectorBlockEntity cbe1 = getWorld().getBlockEntity(vec3i.add(this.getPos()),ConnectorBlockEntity.class);
            if(cbe1 != null) {
                cbe1.getConnection().remove(Vec3i.ZERO.subtract(vec3i));
                cbe1.markDirty();
            }
        }));
    }

    @Override
    public double getRenderDistance() {
        return 1024;
    }

    @Override
    public IBoundingBox getRenderBoundingBox() {
        return IBoundingBox.INFINITE;
    }

    @Override
    public IEnergy getEnergy(Facing side) {
        return energy;
    }
}
