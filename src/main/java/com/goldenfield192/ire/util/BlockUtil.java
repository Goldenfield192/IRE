package com.goldenfield192.ire.util;

import cam72cam.mod.block.BlockType;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import com.goldenfield192.ire.blocks.entity.ConnectorBlockEntity;

import java.util.LinkedList;
import java.util.List;

import static com.goldenfield192.ire.util.MathUtil.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class BlockUtil {
    public static void vecToBlocks(ConnectorBlockEntity from, ConnectorBlockEntity to, BlockType block){
        World world = from.getWorld();
        Vec3i startPos = toVec3i(toVec3d(from.getPos()).add(from.inBlockOffset.rotateYaw(from.getRotation())));
        Vec3i endPos = toVec3i(toVec3d(to.getPos()).add(to.inBlockOffset.rotateYaw(to.getRotation())));
        System.out.println(startPos+" "+endPos);

        int minX = Math.min(startPos.x,endPos.x),
                maxX = Math.max(startPos.x,endPos.x),
                minZ = Math.min(startPos.z,endPos.z),
                maxZ = Math.max(startPos.z,endPos.z);
        double radian = Math.toRadians(vecToDegree(toVec3d(endPos.subtract(startPos))));
        System.out.println(radian);
        double limit = vecToLength(startPos.subtract(endPos));
        for(float iteration = 0;iteration < limit;iteration+=0.1){
            Vec3i vec3i = new Vec3i(Math.floor(iteration * sin(radian)),0,Math.floor(iteration * cos(radian)));
            System.out.println(startPos.add(vec3i));
            if(world.isReplaceable(startPos.add(vec3i)))
                world.setBlock(startPos.add(vec3i),block);
        }
    }
}
