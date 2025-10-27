package net.xiaoyu.dragon_wings;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class WingsRenderUtils {

    public static boolean shouldRenderWings(PlayerEntity player, boolean enabled, boolean flyingExpand) {
        if (enabled) {
            return true;
        } else {
            return player.abilities.flying && flyingExpand;
        }
    }

    public static void applyWingTransforms(MatrixStack matrixStack, PlayerEntity player, int scale) {
        double scaleValue = scale / 100D;
        
        matrixStack.pushPose();
        matrixStack.scale((float) -scaleValue, (float) -scaleValue, (float) scaleValue);

        matrixStack.mulPose(net.minecraft.util.math.vector.Vector3f.YP.rotationDegrees(180 + player.yBodyRot));

        matrixStack.translate(0, -1.25 / scaleValue, 0);
        matrixStack.translate(0, 0, 0.2 / scaleValue);

        if (player.isCrouching()) {
            matrixStack.translate(0D, 0.125D / scaleValue, 0D);
        }
    }

    public static void restoreWingTransforms(MatrixStack matrixStack) {
        matrixStack.popPose();
    }

    public static boolean isLocalPlayer(PlayerEntity player) {
        Minecraft mc = Minecraft.getInstance();
        return player.equals(mc.player);
    }

    public static WingType getWingTypeToRender(PlayerEntity player) {
        if (player.isInvisible()) {
            return null;
        }
        
        boolean isLocalPlayer = isLocalPlayer(player);
        
        for (WingType wingType : WingType.values()) {
            boolean shouldRender;
            if (isLocalPlayer) {
                shouldRender = 
                shouldRenderWings(player, Config.isWingsEnabled(wingType), Config.isWingsFlyingExpand(wingType));
            } else {
                shouldRender = Config.showOtherPlayersWings(wingType) && 
                shouldRenderWings(player, Config.isWingsEnabled(wingType), Config.isWingsFlyingExpand(wingType));
            }
            
            if (shouldRender) {
                return wingType;
            }
        }
        return null;
    }
}