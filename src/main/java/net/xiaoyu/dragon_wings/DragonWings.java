package net.xiaoyu.dragon_wings;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(DragonWings.MODID)
public class DragonWings {
    public static final String MODID = "dragon_wings";

    public DragonWings(ModContainer modContainer) {
        Config.registerConfig(modContainer);
    }
}