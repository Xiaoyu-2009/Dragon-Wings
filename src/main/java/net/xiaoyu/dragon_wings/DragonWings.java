package net.xiaoyu.dragon_wings;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = DragonWings.MOD_ID, name = DragonWings.MOD_NAME, version = DragonWings.VERSION, clientSideOnly = true)
public class DragonWings {
    public static final String MOD_ID = "dragon_wings";
    public static final String MOD_NAME = "Dragon Wings";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.init(event.getSuggestedConfigurationFile());
    }
}