package com.goldenfield192.ire.util.graph;

import cam72cam.mod.entity.Player;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.world.World;
import com.goldenfield192.ire.blocks.entity.ConnectorBlockEntity;

import java.util.*;
import java.util.stream.Collectors;

public class DimGraph {
    private final World Dim;
    private final HashSet<ConnectorBlockEntity> dimNetworkSet;
    private final LinkedList<SubGraph> graphInDim;
    static HashSet<UUID> iterated;
    static HashSet<ConnectorBlockEntity> sub;
    static int count;

    public DimGraph(World dim, HashSet<ConnectorBlockEntity> dimNetworkSet) {
        Dim = dim;
        this.dimNetworkSet = dimNetworkSet;
        graphInDim = new LinkedList<>();
    }

    public World getDim() {
        return Dim;
    }

    public void add(ConnectorBlockEntity cbe){
        dimNetworkSet.add(cbe);
    }

    public void remove(ConnectorBlockEntity cbe){
        dimNetworkSet.remove(cbe);
    }

    public void printContain(Player p,ConnectorBlockEntity cbe,boolean isFull){
        p.sendMessage(PlayerMessage.direct(String.valueOf(this.dimNetworkSet.size())));
        p.sendMessage(PlayerMessage.direct(String.valueOf(this.graphInDim.size())));
        if(isFull) {
            p.sendMessage(PlayerMessage.direct(Arrays.toString(this.dimNetworkSet.toArray())));
            p.sendMessage(PlayerMessage.direct(String.valueOf(cbe.subGraphID)));
            p.sendMessage(PlayerMessage.direct("split"));
            p.sendMessage(PlayerMessage.direct(Arrays.toString(this.graphInDim.get(cbe.subGraphID).subGraphSet.toArray())));
        }
    }

    //重建网络
    public void refresh(){
        count = 0;
        graphInDim.clear();
        iterated = new HashSet<>();
        sub = new HashSet<>();
        System.out.println(dimNetworkSet);
        for (ConnectorBlockEntity cbe : dimNetworkSet){
            if(cbe == null) continue;
            if(!iterated.contains(cbe.uuid)) {
                graphInDim.add(new SubGraph(sub));
                dfs(cbe);
                sub = new HashSet<>();
                count++;
            }
        }
    }

    //深度优先搜索
    void dfs(ConnectorBlockEntity cbe){
        sub.add(cbe);
        cbe.subGraphID = count;
        iterated.add(cbe.uuid);
        cbe.getConnection().keySet()
                .forEach(vec3i -> {
                    ConnectorBlockEntity cbe1 = cbe.getWorld().getBlockEntity(cbe.getPos().add(vec3i), ConnectorBlockEntity.class);
                    if(cbe1 != null && iterated.contains(cbe1.uuid)){
                        return;
                    }else if(cbe1 != null){
                        graphInDim.get(count).add(cbe1);
                        dfs(cbe1);
                    }
                });
    }

    public class SubGraph{
        public HashSet<ConnectorBlockEntity> subGraphSet;

        public SubGraph(HashSet<ConnectorBlockEntity> subGraphSet) {
            this.subGraphSet = subGraphSet;
        }

        public void add(ConnectorBlockEntity cbe){
            this.subGraphSet.add(cbe);
        }
    }
}
