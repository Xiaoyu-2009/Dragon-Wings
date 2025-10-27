package net.xiaoyu.dragon_wings;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class WingsRenderUtils {

    public static boolean shouldRenderWings(Player player, boolean enabled, boolean flyingExpand) {
        if (enabled) {
            return true;
        } else {
            return player.getAbilities().flying && flyingExpand;
        }
    }

    public static void applyWingTransforms(PoseStack poseStack, Player player, int scale) {
        double scaleValue = scale / 100D;
        
        poseStack.pushPose();
        poseStack.scale((float) -scaleValue, (float) -scaleValue, (float) scaleValue);

        poseStack.mulPose(Axis.YP.rotationDegrees(180 + player.yBodyRot));

        poseStack.translate(0, -1.25 / scaleValue, 0);
        poseStack.translate(0, 0, 0.2 / scaleValue);

        if (player.isCrouching()) {
            poseStack.translate(0D, 0.125D / scaleValue, 0D);
        }
    }

    public static void restoreWingTransforms(PoseStack poseStack) {
        poseStack.popPose();
    }

    public static boolean isLocalPlayer(Player player) {
        Minecraft mc = Minecraft.getInstance();
        return player.equals(mc.player);
    }

    public static WingType getWingTypeToRender(Player player) {
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