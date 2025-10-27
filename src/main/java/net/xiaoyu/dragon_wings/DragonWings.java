package net.xiaoyu.dragon_wings;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(DragonWings.MOD_ID)
public class DragonWings {
    public static final String MOD_ID = "dragon_wings";

    public DragonWings(ModContainer modContainer) {
        Config.registerConfig(modContainer);
    }
}