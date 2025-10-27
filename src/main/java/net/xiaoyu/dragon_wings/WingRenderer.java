package net.xiaoyu.dragon_wings;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DragonWings.MOD_ID)
public class WingRenderer extends ModelBase {
    private static WingRenderer instance;
    
    private final ModelRenderer wing;
    private final ModelRenderer wingTip;
    
    private WingRenderer() {
        this.textureWidth = 30;
        this.textureHeight = 30;

        this.wing = new ModelRenderer(this, 0, 0);
        this.wing.addBox(-10.0F, -1.0F, -1.0F, 10, 2, 2);
        this.wing.setRotationPoint(-2.0F, 0.0F, 0.0F);

        ModelRenderer wingMembrane = new ModelRenderer(this, -10, 8);
        wingMembrane.addBox(-10.0F, 0.0F, 0.5F, 10, 0, 10);
        wingMembrane.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wing.addChild(wingMembrane);

        this.wingTip = new ModelRenderer(this, 0, 5);
        this.wingTip.addBox(-10.0F, -0.5F, -0.5F, 10, 1, 1);
        this.wingTip.setRotationPoint(-10.0F, 0.0F, 0.0F);
        this.wing.addChild(this.wingTip);

        ModelRenderer wingTipMembrane = new ModelRenderer(this, -10, 18);
        wingTipMembrane.addBox(-10.0F, 0.0F, 0.5F, 10, 0, 10);
        wingTipMembrane.setRotationPoint(0.0F, 0.0F, 0.0F);
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
        EntityPlayer player = event.getEntityPlayer();
        WingType wingType = WingsRenderUtils.getWingTypeToRender(player);

        if (wingType != null) {
            renderWings(player, event.getRenderer(), event.getX(), event.getY(), event.getZ(), event.getPartialRenderTick(), wingType);
        }
    }
    
    public static void renderWings(EntityPlayer player, RenderPlayer renderer, double x, double y, double z, float partialTicks, WingType wingType) {
        WingRenderer model = getInstance();

        int scale = Config.getWingsScale(wingType);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        
        WingsRenderUtils.applyWingTransforms(player, scale);

        ResourceLocation texture = new ResourceLocation(DragonWings.MOD_ID, wingType.getTexturePath());
        renderer.bindTexture(texture);
        
        model.updateWingAnimation();
        
        for (int j = 0; j < 2; ++j) {
            model.wing.render(0.0625F);

            if (j == 0) {
                GlStateManager.scale(-1.0F, 1.0F, 1.0F);
            }
        }

        WingsRenderUtils.restoreWingTransforms();
        GlStateManager.popMatrix();
    }
    
    private void updateWingAnimation() {
        float f11 = (System.currentTimeMillis() % 1000) / 1000F * (float) Math.PI * 2.0F;
        this.wing.rotateAngleX = (float) Math.toRadians(-80F) - (float) Math.cos((double)f11) * 0.2F;
        this.wing.rotateAngleY = (float) Math.toRadians(20F) + (float) Math.sin(f11) * 0.4F;
        this.wing.rotateAngleZ = (float) Math.toRadians(20F);
        this.wingTip.rotateAngleZ = -((float)(Math.sin((double)(f11 + 2.0F)) + 0.5D)) * 0.75F;
    }
}