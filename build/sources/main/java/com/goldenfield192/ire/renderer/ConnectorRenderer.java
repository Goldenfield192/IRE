package com.goldenfield192.ire.renderer;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import com.goldenfield192.ire.IRE;
import com.goldenfield192.ire.blocks.entity.ConnectorBlockEntity;

import static com.goldenfield192.ire.util.MathUtil.*;

//TODO 在做完资源包加载或者线缆渲染之前不要来重构
public class ConnectorRenderer{

    static OBJModel model,
                    wire,
                    linePart;
    static {
        try {
            model = new OBJModel(new Identifier(IRE.MODID,"models/block/jiaxiangan1.obj"),0);
            wire = new OBJModel(new Identifier(IRE.MODID,"models/block/wire.obj"),0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static StandardModel render(ConnectorBlockEntity gbe) {
        return new StandardModel().addCustom((state, partialTicks) -> BlockRenderer(gbe,state));
    }

    //史
    private static void BlockRenderer(ConnectorBlockEntity cbe, RenderState state){
        state.smooth_shading(true);
        state.translate(0.5,0,0.5);
        RenderState baseState = state.clone();
        baseState.rotate(cbe.getRotation(),0,1,0);
        //渲染base
        try (OBJRender.Binding vbo = model.binder().bind(baseState)) {
            vbo.draw();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //渲染网
        Vec3d offset = cbe.inBlockOffset.rotateYaw(cbe.getRotation());

        cbe.getConnection().keySet().stream().forEach(vec3i -> {
            ConnectorBlockEntity cbe2 = cbe.getWorld()
                    .getBlockEntity(cbe.getPos().add(vec3i), ConnectorBlockEntity.class);//目标节点
            if(cbe.getConnection().get(vec3i) && cbe2 != null){
                Vec3d offset2 = cbe2.inBlockOffset.rotateYaw(cbe2.getRotation());
                RenderState storage = state.clone();
                storage.translate(offset);
                Vec3d rotation = toVec3d(vec3i).add(offset2)
                        .subtract(offset);
                storage.scale(vecToLength(rotation), 1, vecToLength(rotation));
//                storage.rotate();
                storage.rotate(vecToDegree(rotation),0,1,0);
                storage.cull_face(false);
                try (OBJRender.Binding vbo = wire.binder().bind(storage)) {
                    vbo.draw();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
