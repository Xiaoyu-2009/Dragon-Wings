package net.xiaoyu.dragon_wings;

public enum WingType {
    // 末影龙翅膀
    ENDER_DRAGON("textures/entity/ender_dragon_wings.png", "ender_dragon_wings", "Ender Dragon Wings"),
    // 龙翅膀
    DRAGON("textures/entity/dragon_wings.png", "dragon_wings", "Dragon Wings");

    private final String texturePath;
    private final String configPrefix;
    private final String displayName;
    
    WingType(String texturePath, String configPrefix, String displayName) {
        this.texturePath = texturePath;
        this.configPrefix = configPrefix;
        this.displayName = displayName;
    }
    
    public String getTexturePath() {
        return texturePath;
    }
    
    public String getDisplayName() {
        return displayName;
    }

    public String getEnabledConfigKey() {
        return configPrefix + "_enabled";
    }

    public String getScaleConfigKey() {
        return configPrefix + "_scale";
    }

    public String getFlyingExpandConfigKey() {
        return configPrefix + "_flying_expand";
    }
}