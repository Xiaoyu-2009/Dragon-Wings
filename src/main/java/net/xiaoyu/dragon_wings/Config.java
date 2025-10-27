package net.xiaoyu.dragon_wings;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;

public class Config {
    public static ModConfigSpec configSpec;

    private static final WingType[] WING_TYPES = WingType.values();

    public static ModConfigSpec.BooleanValue[] WINGS_ENABLED;
    public static ModConfigSpec.IntValue[] WINGS_SCALE;
    public static ModConfigSpec.BooleanValue[] WINGS_FLYING_EXPAND;
    
    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        WINGS_ENABLED = new ModConfigSpec.BooleanValue[WING_TYPES.length];
        WINGS_SCALE = new ModConfigSpec.IntValue[WING_TYPES.length];
        WINGS_FLYING_EXPAND = new ModConfigSpec.BooleanValue[WING_TYPES.length];
        
        for (int i = 0; i < WING_TYPES.length; i++) {
            WingType wingType = WING_TYPES[i];
            builder.push(wingType.getDisplayName() + " Settings");

            WINGS_ENABLED[i] = builder
                .comment("Enable or disable the " + wingType.getDisplayName().toLowerCase() + " rendering")
                .define(wingType.getEnabledConfigKey(), i == 0);

            WINGS_SCALE[i] = builder
                .comment("Scale of the " + wingType.getDisplayName().toLowerCase())
                .defineInRange(wingType.getScaleConfigKey(), 100, 1, Integer.MAX_VALUE);

            WINGS_FLYING_EXPAND[i] = builder
                .comment("Whether to expand wings when flying for " + wingType.getDisplayName().toLowerCase())
                .define(wingType.getFlyingExpandConfigKey(), false);
            
            builder.pop();
        }
        
        configSpec = builder.build();
    }
    
    public static void registerConfig(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, configSpec);
    }

    public static boolean isWingsEnabled(WingType wingType) {
        int index = wingType.ordinal();
        if (index >= 0 && index < WINGS_ENABLED.length) {
            return WINGS_ENABLED[index].get();
        }
        return true;
    }
    
    public static int getWingsScale(WingType wingType) {
        int index = wingType.ordinal();
        if (index >= 0 && index < WINGS_SCALE.length) {
            return WINGS_SCALE[index].get();
        }
        return 100;
    }

    public static boolean isWingsFlyingExpand(WingType wingType) {
        int index = wingType.ordinal();
        if (index >= 0 && index < WINGS_FLYING_EXPAND.length) {
            return WINGS_FLYING_EXPAND[index].get();
        }
        return true;
    }
}