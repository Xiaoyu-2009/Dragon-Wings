package net.xiaoyu.dragon_wings;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DragonWings.MOD_ID)
public class WingRenderer {
    private static WingRenderer instance;
    
    private final ModelRenderer wing;
    private final ModelRenderer wingTip;
    
    private WingRenderer() {
        this.wing = new ModelRenderer(30, 30, 0, 0);
        this.wing.addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F);
        this.wing.setPos(-2.0F, 0.0F, 0.0F);

        ModelRenderer wingMembrane = new ModelRenderer(30, 30, -10, 8);
        wingMembrane.addBox(-10.0F, 0.0F, 0.5F, 10.0F, 0.0F, 10.0F);
        wingMembrane.setPos(0.0F, 0.0F, 0.0F);
        this.wing.addChild(wingMembrane);

        this.wingTip = new ModelRenderer(30, 30, 0, 5);
        this.wingTip.addBox(-10.0F, -0.5F, -0.5F, 10.0F, 1.0F, 1.0F);
        this.wingTip.setPos(-10.0F, 0.0F, 0.0F);
        this.wing.addChild(this.wingTip);

        ModelRenderer wingTipMembrane = new ModelRenderer(30, 30, -10, 18);
        wingTipMembrane.addBox(-10.0F, 0.0F, 0.5F, 10.0F, 0.0F, 10.0F);
        wingTipMembrane.setPos(0.0F, 0.0F, 0.0F);
        this.wingTip.addChild(wingTipMembrane);
    }
    
    public static WingRenderer getInstance() {
        if (instance == null) {
            instance = new WingRenderer();
        }
        return instance;
    }
    
    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        if (event.getPlayer() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getPlayer();
            WingType wingType = WingsRenderUtils.getWingTypeToRender(player);

            if (wingType != null) {
                renderWings(player, event.getMatrixStack(), event.getBuffers(), event.getLight(), wingType);
            }
        }
    }
    
    public static void renderWings(PlayerEntity player, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, WingType wingType) {
        WingRenderer renderer = getInstance();

        int scale = Config.getWingsScale(wingType);

        WingsRenderUtils.applyWingTransforms(matrixStack, player, scale);

        ResourceLocation texture = new ResourceLocation(DragonWings.MOD_ID, wingType.getTexturePath());
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        
        renderer.updateWingAnimation();
        
        for (int j = 0; j < 2; ++j) {
            renderer.wing.render(matrixStack, vertexBuilder, packedLight, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);

            if (j == 0) {
                matrixStack.scale(-1.0F, 1.0F, 1.0F);
            }
        }

        WingsRenderUtils.restoreWingTransforms(matrixStack);
    }
    
    private void updateWingAnimation() {
        float f11 = (System.currentTimeMillis() % 1000) / 1000F * (float) Math.PI * 2.0F;
        this.wing.xRot = (float) Math.toRadians(-80F) - (float) Math.cos((double)f11) * 0.2F;
        this.wing.yRot = (float) Math.toRadians(20F) + (float) Math.sin(f11) * 0.4F;
        this.wing.zRot = (float) Math.toRadians(20F);
        this.wingTip.zRot = -((float)(Math.sin((double)(f11 + 2.0F)) + 0.5D)) * 0.75F;
    }
}