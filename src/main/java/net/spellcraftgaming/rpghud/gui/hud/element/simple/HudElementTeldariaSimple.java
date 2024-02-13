package net.spellcraftgaming.rpghud.gui.hud.element.simple;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElement;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElementType;
import net.spellcraftgaming.rpghud.settings.Settings;
public class HudElementTeldariaSimple extends HudElement{
    public static final String DEFENSE_TAG = "MMOITEMS_DEFENSE";
    public static final String BLOCK_RATE_TAG = "MMOITEM_BLOCK_RATING";
    public static final String BLOCK_POWER_TAG = "MMOITEMS_BLOCK_POWER";
    public static final String ATTACK_TAG = "MMOITEMS_ATTACK_DAMAGE";
    public static final String CRIT_CHANCE_TAG = "MMOITEMS_CRITICAL_STRIKE_CHANCE";
    public static final String CRIT_DAMAGE_TAG = "HSTAY_CRITICAL_STRIKE_POWER";
    public static final double BASE_CRITICAL_STRIKE_POWER = 110.0;
    public HudElementTeldariaSimple(){
        super(HudElementType.TELDARIA, 0, 0, 0, 0, true);
    }
    public void drawElement(GuiGraphics gg, float zLevel, float partialTicks, int scaledWidth, int scaledHeight){
        boolean reduceSize = this.settings.getBoolValue(Settings.reduce_size);
        if (reduceSize)
            gg.pose().scale(0.5f, 0.5f, 0.5f);
        if(this.settings.getBoolValue(Settings.teldaria_mode)){
            if(this.settings.getBoolValue(Settings.enable_tstats)){

            }
        }
    }

    private int calculateWidth() {
        int width = 0;
        return width;
    }
    protected void drawTStatBox(GuiGraphics gg, int width){

    }
    private String getStatString(String statName){
        String statString = "";
        return statString;
    }

    /**
     * Summation of a particular stat from all currently equipped stuffs
     * @param statName
     * @return
     */
    private Double getStat(String statName){
        double stat = 0;
        for (int i = this.mc.player.getInventory().armor.size() - 1; i >= 0; i--) {
            if (this.mc.player.getInventory().getArmor(i) != ItemStack.EMPTY
                    && this.mc.player.getInventory().getArmor(i).getItem().isDamageable(null)) {
                ItemStack item = this.mc.player.getInventory().getArmor(i);
                if(item.getShareTag() != null && item.getShareTag().contains(statName)){
                    stat += Double.parseDouble(item.getTag().get(statName).getAsString());
                }
            }

            if (this.mc.player.getMainHandItem() != ItemStack.EMPTY) {
                ItemStack item = this.mc.player.getMainHandItem();
                if(item.getShareTag() != null && item.getShareTag().contains(statName)){
                    stat += Double.parseDouble(item.getTag().get(statName).getAsString());
                }
            }
        }
        return stat;
    }
}
