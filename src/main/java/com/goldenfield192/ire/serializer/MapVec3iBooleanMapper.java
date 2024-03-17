package com.goldenfield192.ire.serializer;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

import java.util.Map;
public class MapVec3iBooleanMapper implements TagMapper<Map<Vec3i, Boolean>> {

    public static final String KEY_NAME = "entry";

    @Override
    public TagAccessor<Map<Vec3i, Boolean>> apply(Class<Map<Vec3i, Boolean>> type, String fieldName, TagField tag) {

        return new TagAccessor<>(
                (nbt, map) -> {
                    if (map != null)
                        nbt.setMap(fieldName, map, MapVec3iBooleanMapper::posToString,
                                value -> new TagCompound().setBoolean(KEY_NAME, value));
                },
                nbt -> nbt.getMap(fieldName, MapVec3iBooleanMapper::stringToPos, valueTag -> valueTag.getBoolean(KEY_NAME))
        );
    }

    private static String posToString(Vec3i pos) {
        return pos.x + ";" + pos.y + ";" + pos.z;
    }

    private static Vec3i stringToPos(String pos) {
        String[] xyz = pos.split(";");
        return new Vec3i(Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1]), Integer.parseInt(xyz[2]));
    }
}