package com.goldenfield192.ire.util.graph;

import cam72cam.mod.energy.Energy;
import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.world.World;
import com.goldenfield192.ire.blocks.entity.ConnectorBlockEntity;

import java.util.*;

public class DimGraph {
    private final HashSet<Vec3i> dimNetworkSet;

    private final HashMap<UUID,SubGraph> graphInDim;
    public HashSet<Vec3i> getDimNetworkSet() {
        return dimNetworkSet;
    }

    public HashMap<UUID, SubGraph> getGraphInDim() {
        return graphInDim;
    }


    public DimGraph(HashSet<Vec3i> dimNetworkSet) {
        this.dimNetworkSet = dimNetworkSet;
        graphInDim = new HashMap<>();
    }

    public void add(ConnectorBlockEntity cbe){
        dimNetworkSet.add(cbe.getPos());
    }

    public void remove(UUID uuid, Vec3i pos){
        if(graphInDim.get(uuid).subGraphSet.size() == 1){
            graphInDim.remove(uuid);
        }else{
            graphInDim.get(uuid).subGraphSet.remove(pos);
        }
    }

    public void printContain(Player p,ConnectorBlockEntity cbe,boolean isFull){
        p.sendMessage(PlayerMessage.direct(String.valueOf(this.dimNetworkSet.size())));
        p.sendMessage(PlayerMessage.direct(String.valueOf(this.graphInDim.size())));
    }

    public void buildExistedSubGraphs(World world) {
        this.dimNetworkSet.forEach(vec3i -> {
            ConnectorBlockEntity cbe = world.getBlockEntity(vec3i, ConnectorBlockEntity.class);
            if(cbe != null){
                graphInDim.get(cbe.getSubGraphID()).add(cbe);
            }
        });
    }

    public void addSubGraph(Vec3i pos, UUID uuid){
        this.graphInDim.put(uuid, new SubGraph(pos));
    }

    public class SubGraph{
        public HashSet<Vec3i> subGraphSet;

        public Energy energy;

        public SubGraph(Vec3i... vec3is) {
            subGraphSet = new HashSet<>();
            if(vec3is != null){
                subGraphSet.addAll(Arrays.asList(vec3is));
            }
        }

        public void add(ConnectorBlockEntity cbe){
            this.subGraphSet.add(cbe.getPos());
        }
    }
}
