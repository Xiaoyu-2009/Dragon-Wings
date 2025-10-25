package net.xiaoyu.dragon_wings;

public enum WingType {
    ENDER_DRAGON("textures/entity/ender_dragon_wings.png"),
    DRAGON("textures/entity/dragon_wings.png");
    
    private final String texturePath;
    
    WingType(String texturePath) {
        this.texturePath = texturePath;
    }
    
    public String getTexturePath() {
        return texturePath;
    }
}