package net.xiaoyu.dragon_wings;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DragonWings.MOD_ID)
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
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            WingType wingType = WingsRenderUtils.getWingTypeToRender(player);

            if (wingType != null) {
                renderWings(player, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), wingType);
            }
        }
    }
    
    public static void renderWings(Player player, PoseStack poseStack, MultiBufferSource buffer, int packedLight, WingType wingType) {
        WingRenderer renderer = getInstance();

        int scale = Config.getWingsScale(wingType);

        WingsRenderUtils.applyWingTransforms(poseStack, player, scale);

        ResourceLocation texture = new ResourceLocation(DragonWings.MOD_ID, wingType.getTexturePath());
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        
        renderer.updateWingAnimation();
        
        for (int j = 0; j < 2; ++j) {
            renderer.wing.render(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (j == 0) {
                poseStack.scale(-1.0F, 1.0F, 1.0F);
            }
        }

        WingsRenderUtils.restoreWingTransforms(poseStack);
    }
    
    private void updateWingAnimation() {
        float f11 = (System.currentTimeMillis() % 1000) / 1000F * (float) Math.PI * 2.0F;
        this.wing.xRot = (float) Math.toRadians(-80F) - (float) Math.cos((double)f11) * 0.2F;
        this.wing.yRot = (float) Math.toRadians(20F) + (float) Math.sin(f11) * 0.4F;
        this.wing.zRot = (float) Math.toRadians(20F);
        this.wingTip.zRot = -((float)(Math.sin((double)(f11 + 2.0F)) + 0.5D)) * 0.75F;
    }
}