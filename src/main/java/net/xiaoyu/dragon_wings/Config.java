package net.xiaoyu.dragon_wings;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod.EventBusSubscriber(modid = DragonWings.MOD_ID)
public class Config {
    private static Configuration config;

    public static boolean[] wingsEnabled = new boolean[WingType.values().length];
    public static int[] wingsScale = new int[WingType.values().length];
    public static boolean[] wingsFlyingExpand = new boolean[WingType.values().length];
    public static boolean[] showOtherPlayersWings = new boolean[WingType.values().length];

    public static void init(File configFile) {
        config = new Configuration(configFile);
        loadConfig();
    }

    private static void loadConfig() {
        WingType[] wingTypes = {WingType.ENDER_DRAGON, WingType.DRAGON};
        
        for (int i = 0; i < wingTypes.length; i++) {
            WingType wingType = wingTypes[i];
            String categoryName = wingType.getDisplayName() + " Settings";
            
            wingsEnabled[wingType.ordinal()] = config.getBoolean(
                wingType.getEnabledConfigKey(), 
                categoryName, 
                i == 0, 
                "Enable or disable the " + wingType.getDisplayName().toLowerCase() + " rendering"
            );
            
            wingsScale[wingType.ordinal()] = config.getInt(
                wingType.getScaleConfigKey(), 
                categoryName, 
                100, 
                1, 
                Integer.MAX_VALUE, 
                "Scale of the " + wingType.getDisplayName().toLowerCase()
            );
            
            wingsFlyingExpand[wingType.ordinal()] = config.getBoolean(
                wingType.getFlyingExpandConfigKey(), 
                categoryName, 
                false, 
                "Whether to expand wings when flying for " + wingType.getDisplayName().toLowerCase()
            );
            
            String otherPlayersKey = wingType.getDisplayName().toLowerCase().replace(" ", "_") + "_show_other_players_wings";
            showOtherPlayersWings[wingType.ordinal()] = config.getBoolean(
                otherPlayersKey, 
                categoryName, 
                false, 
                "Whether to show " + wingType.getDisplayName().toLowerCase() + " on other players"
            );
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static boolean isWingsEnabled(WingType wingType) {
        int index = wingType.ordinal();
        if (index >= 0 && index < wingsEnabled.length) {
            return wingsEnabled[index];
        }
        return true;
    }
    
    public static int getWingsScale(WingType wingType) {
        int index = wingType.ordinal();
        if (index >= 0 && index < wingsScale.length) {
            return wingsScale[index];
        }
        return 100;
    }

    public static boolean isWingsFlyingExpand(WingType wingType) {
        int index = wingType.ordinal();
        if (index >= 0 && index < wingsFlyingExpand.length) {
            return wingsFlyingExpand[index];
        }
        return true;
    }

    public static boolean showOtherPlayersWings(WingType wingType) {
        int index = wingType.ordinal();
        if (index >= 0 && index < showOtherPlayersWings.length) {
            return showOtherPlayersWings[index];
        }
        return true;
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(DragonWings.MOD_ID)) {
            loadConfig();
        }
    }
}