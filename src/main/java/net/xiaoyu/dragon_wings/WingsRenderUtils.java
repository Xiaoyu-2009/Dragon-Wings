package net.xiaoyu.dragon_wings;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class WingsRenderUtils {

    public static boolean shouldRenderWings(EntityPlayer player, boolean enabled, boolean flyingExpand) {
        if (enabled) {
            return true;
        } else {
            return player.capabilities.isFlying && flyingExpand;
        }
    }

    public static void applyWingTransforms(EntityPlayer player, int scale) {
        double scaleValue = scale / 100D;
        
        GL11.glPushMatrix();
        GL11.glScalef((float) -scaleValue, (float) -scaleValue, (float) scaleValue);

        GL11.glRotatef(180 + player.renderYawOffset, 0, 1, 0);

        GL11.glTranslatef(0, -1.25f / (float) scaleValue, 0);
        GL11.glTranslatef(0, 0, 0.2f / (float) scaleValue);

        if (player.isSneaking()) {
            GL11.glTranslatef(0, 0.125f / (float) scaleValue, 0);
        }
    }

    public static void restoreWingTransforms() {
        GL11.glPopMatrix();
    }

    public static boolean isLocalPlayer(EntityPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        return player.equals(mc.player);
    }

    public static WingType getWingTypeToRender(EntityPlayer player) {
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