package net.xiaoyu.dragon_wings;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;

public class Config {
    public static ModConfigSpec configSpec;

    public static ModConfigSpec.BooleanValue ENDER_DRAGON_WINGS_ENABLED;
    public static ModConfigSpec.IntValue ENDER_DRAGON_WINGS_SCALE;
    public static ModConfigSpec.BooleanValue DRAGON_WINGS_ENABLED;
    public static ModConfigSpec.IntValue DRAGON_WINGS_SCALE;
    
    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        
        builder.push("Ender Dragon Wings Settings");
        
        ENDER_DRAGON_WINGS_ENABLED = builder
                .comment("Enable or disable the ender dragon wings rendering")
                .define("ender_dragon_wings_enabled", true);
                
        ENDER_DRAGON_WINGS_SCALE = builder
                .comment("Scale of the ender dragon wings")
                .defineInRange("ender_dragon_wings_scale", 100, 60, Integer.MAX_VALUE);

        builder.pop();

        builder.push("Dragon Wings Settings");
        
        DRAGON_WINGS_ENABLED = builder
                .comment("Enable or disable the dragon wings rendering")
                .define("dragon_wings_enabled", false);
                
        DRAGON_WINGS_SCALE = builder
                .comment("Scale of the dragon wings")
                .defineInRange("dragon_wings_scale",  100, 60, Integer.MAX_VALUE);

        builder.pop();
        configSpec = builder.build();
    }
    
    public static void registerConfig(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, configSpec);
    }

    public static boolean isEnderDragonWingsEnabled() {
        return ENDER_DRAGON_WINGS_ENABLED.get();
    }
    
    public static int getEnderDragonWingsScale() {
        return ENDER_DRAGON_WINGS_SCALE.get();
    }

    public static boolean isDragonWingsEnabled() {
        return DRAGON_WINGS_ENABLED.get();
    }
    
    public static int getDragonWingsScale() {
        return DRAGON_WINGS_SCALE.get();
    }
}