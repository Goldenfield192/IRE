package com.goldenfield192.ire.util.graph;

import cam72cam.mod.world.World;
import com.goldenfield192.ire.blocks.entity.ConnectorBlockEntity;

import java.util.*;
import java.util.stream.Collectors;

public class GraphHandler {
    public static HashSet<DimGraph> wireMap;
    //初始化
    public static void init(){
        wireMap = new HashSet<>();
    }

    //加载已有世界
    public static void loadWorld(World world){
        if(wireMap.stream().noneMatch(g -> g.getDim().equals(world))){
//            HashMap<UUID,ConnectorBlockEntity> map = new HashMap<>();
//            world.getBlockEntities(ConnectorBlockEntity.class).forEach(e -> map.put(e.uuid,e));
            DimGraph d = new DimGraph(world,new HashSet<>(new ArrayList<>(world.getBlockEntities(ConnectorBlockEntity.class))));
            System.out.println(world.getBlockEntities(ConnectorBlockEntity.class).size());
            wireMap.add(d);
            update(world);
        }
    }

    //添加连接器
    public static void addConnector(ConnectorBlockEntity cbe, World world){
        if(wireMap.stream().anyMatch(g -> g.getDim().equals(world))){
            wireMap.stream()
                    .filter(g -> g.getDim().equals(world))
                    .forEach(g -> g.add(cbe));
            update(world);
        }else{
            loadWorld(world);
        }
    }

    //获得当前维度的连接器Map
    public static DimGraph getWorld(World world) throws DuplicateGraphException {
        List<DimGraph> list = wireMap.stream()
                .filter(dimGraph -> dimGraph.getDim().equals(world))
                .collect(Collectors.toList());
        if(list.size() == 0){
            loadWorld(world);
            return wireMap.stream()
                    .filter(dimGraph -> dimGraph.getDim().equals(world))
                    .collect(Collectors.toList()).get(0);
        }
        if(list.size() > 1){
            throw new DuplicateGraphException();
        }
        return list.get(0);
    }

    public static void update(World world){
        try {
            getWorld(world).refresh();
        } catch (DuplicateGraphException e) {
            throw new RuntimeException(e);
        }
    }
}
