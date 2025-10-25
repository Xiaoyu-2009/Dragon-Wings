package net.xiaoyu.dragon_wings;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(modid = DragonWings.MODID)
public class WingRenderer {
    private static WingRenderer instance;
    
    private final ModelPart wing;
    private final ModelPart wingTip;
    
    private WingRenderer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition wingPart = partdefinition.addOrReplaceChild("wing", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F)
                .texOffs(-10, 8)
                .addBox(-10.0F, 0.0F, 0.5F, 10.0F, 0.0F, 10.0F),
            PartPose.offset(-2.0F, 0.0F, 0.0F)
        );

        wingPart.addOrReplaceChild("wingtip", CubeListBuilder.create()
                .texOffs(0, 5)
                .addBox(-10.0F, -0.5F, -0.5F, 10.0F, 1.0F, 1.0F)
                .texOffs(-10, 18)
                .addBox(-10.0F, 0.0F, 0.5F, 10.0F, 0.0F, 10.0F),
            PartPose.offset(-10.0F, 0.0F, 0.0F)
        );
        
        ModelPart root = partdefinition.bake(30, 30);
        this.wing = root.getChild("wing");
        this.wingTip = wing.getChild("wingtip");
    }
    
    public static WingRenderer getInstance() {
        if (instance == null) {
            instance = new WingRenderer();
        }
        return instance;
    }
    
    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();
        Minecraft mc = Minecraft.getInstance();

        if (player.equals(mc.player) && !player.isInvisible()) {
            // 末影龙翅膀
            if (Config.isEnderDragonWingsEnabled()) {
                renderWings(player, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), WingType.ENDER_DRAGON);
            }
        }
    }
    
    public static void renderWings(Player player, PoseStack poseStack, MultiBufferSource buffer, int packedLight, WingType wingType) {
        WingRenderer renderer = getInstance();
        
        double scale = Config.getEnderDragonWingsScale() / 100D;
        
        poseStack.pushPose();
        poseStack.scale((float) -scale, (float) -scale, (float) scale);

        poseStack.mulPose(Axis.YP.rotationDegrees(180 + player.yBodyRot));

        poseStack.translate(0, -1.25 / scale, 0);
        poseStack.translate(0, 0, 0.2 / scale);

        if (player.isCrouching()) {
            poseStack.translate(0D, 0.125D / scale, 0D);
        }

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(DragonWings.MODID, wingType.getTexturePath());
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        
        for (int j = 0; j < 2; ++j) {
            renderer.updateWingAnimation();
            
            renderer.wing.render(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (j == 0) {
                poseStack.scale(-1.0F, 1.0F, 1.0F);
            }
        }
        
        poseStack.popPose();
    }
    
    private void updateWingAnimation() {
        float f11 = (System.currentTimeMillis() % 1000) / 1000F * (float) Math.PI * 2.0F;
        this.wing.xRot = (float) Math.toRadians(-80F) - (float) Math.cos((double)f11) * 0.2F;
        this.wing.yRot = (float) Math.toRadians(20F) + (float) Math.sin(f11) * 0.4F;
        this.wing.zRot = (float) Math.toRadians(20F);
        this.wingTip.zRot = -((float)(Math.sin((double)(f11 + 2.0F)) + 0.5D)) * 0.75F;
    }
}