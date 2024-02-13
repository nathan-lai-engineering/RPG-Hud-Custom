package net.spellcraftgaming.rpghud.gui.hud.element.simple;

import com.google.common.collect.Ordering;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElement;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElementType;
import net.spellcraftgaming.rpghud.settings.Settings;
import org.lwjgl.system.SharedLibrary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Map;

public class HudElementTeldariaSimple extends HudElement{
    public static final String DEFENSE = "defense";
    public static final String DEFENSE_STAT = "[\uD83D\uDEE1] Defense";
    public static final String DEFENSE_TAG = "MMOITEMS_DEFENSE";

    public static final String BLOCK = "block";
    public static final String BLOCK_STAT = "[\uD83D\uDD30] Block";
    public static final String BLOCK_RATE_TAG = "MMOITEMS_BLOCK_RATING";
    public static final String BLOCK_POWER_TAG = "MMOITEMS_BLOCK_POWER";

    public static final String DAMAGE = "damage";
    public static final String DAMAGE_STAT = "[⚔] Damage";
    public static final String DAMAGE_TAG = "MMOITEMS_ATTACK_DAMAGE";

    public static final String CRIT = "crit";
    public static final String CRIT_STAT = "[⚒] Crit";
    public static final String CRIT_CHANCE_TAG = "MMOITEMS_CRITICAL_STRIKE_CHANCE";
    public static final String CRIT_DAMAGE_TAG = "MMOITEMS_CRITICAL_STRIKE_POWER";
    public static final double BASE_CRITICAL_STRIKE_POWER = 105.0;

    protected int tstatOffset = 0;
    private Attribute a;

    public HudElementTeldariaSimple(){
        super(HudElementType.TELDARIA, 0, 0, 0, 0, true);
    }
    public void drawElement(GuiGraphics gg, float zLevel, float partialTicks, int scaledWidth, int scaledHeight){
        if(this.settings.getBoolValue(Settings.teldaria_mode)){
            if(this.settings.getBoolValue(Settings.enable_tstats)){
                gg.pose().translate(this.settings.getPositionValue(Settings.tstats_position)[0],
                        this.settings.getPositionValue(Settings.tstats_position)[1], 0);
                drawTStatBox(gg);
                gg.pose().translate(-this.settings.getPositionValue(Settings.tstats_position)[0],
                        -this.settings.getPositionValue(Settings.tstats_position)[1], 0);
            }
        }
    }

    /**
     * Gets width of largest stat string
     * @return
     */
    private int calculateWidth() {
        int width = 0;
        width = Math.max(width, this.mc.font.width(getStatString(DEFENSE)));
        width = Math.max(width, this.mc.font.width(getStatString(BLOCK)));
        width = Math.max(width, this.mc.font.width(getStatString(DAMAGE)));
        width = Math.max(width, this.mc.font.width(getStatString(CRIT)));
        return width;
    }
    protected void drawTStatBox(GuiGraphics gg){
        tstatOffset = 0;
        boolean reduceSize = this.settings.getBoolValue(Settings.reduce_size);
        if (reduceSize)
            gg.pose().scale(0.5f, 0.5f, 0.5f);
        int width = calculateWidth();

        drawRect(gg, 0, 0, 12 + width, 12 + (20 * 4), 0xA0000000);

        gg.drawCenteredString( this.mc.font, getStatString(DEFENSE), 6 + width / 2, 6 + 5 + tstatOffset, -1);
        tstatOffset += 20;

        gg.drawCenteredString( this.mc.font, getStatString(BLOCK), 6 + width / 2, 6 + 5 + tstatOffset, -1);
        tstatOffset += 20;

        gg.drawCenteredString( this.mc.font, getStatString(DAMAGE), 6 + width / 2, 6 + 5 + tstatOffset, -1);
        tstatOffset += 20;

        gg.drawCenteredString( this.mc.font, getStatString(CRIT), 6 + width / 2, 6 + 5 + tstatOffset, -1);
        tstatOffset += 20;

        if (reduceSize)
            gg.pose().scale(2f, 2f, 2f);
    }

    /**
     * Creates a string for a specific stat
     * @param statName
     * @return
     */
    private String getStatString(String statName){
        return switch(statName){
            case DEFENSE -> String.format("%s: %.1f", DEFENSE_STAT, getStat(DEFENSE_TAG));
            case BLOCK -> String.format("%s: %.1f%% | %.1f%%", BLOCK_STAT, getStat(BLOCK_RATE_TAG), getStat(BLOCK_POWER_TAG));
            case DAMAGE -> String.format("%s: %.1f", DAMAGE_STAT, getStat(DAMAGE_TAG));
            case CRIT -> String.format("%s: %.1f%% | %.1f%%", CRIT_STAT, getStat(CRIT_CHANCE_TAG), getStat(CRIT_DAMAGE_TAG) + BASE_CRITICAL_STRIKE_POWER);
            default -> "";
        };
    }

    /**
     * Summation of a particular stat from all currently equipped stuffs
     * @param statTag
     * @return
     */
    private Double getStat(String statTag){
        double stat = 0;
        if(statTag.equals(DAMAGE_TAG)) {
            stat = 1;
            Collection<MobEffectInstance> collection = this.mc.player.getActiveEffects();
            if(!collection.isEmpty()) {
                int strengthBoost = 0;
                for(MobEffectInstance effectinstance : Ordering.natural().reverse().sortedCopy(collection)) {
                    if(effectinstance.getEffect().getDisplayName().getString().equals("Strength")){
                        strengthBoost = Math.max(strengthBoost, (1 + effectinstance.getAmplifier()) * 3);
                    }
                }
                stat += strengthBoost;
            }
        }
        for (int i = this.mc.player.getInventory().armor.size() - 1; i >= 0; i--) {
            if (this.mc.player.getInventory().getArmor(i) != ItemStack.EMPTY
                    && this.mc.player.getInventory().getArmor(i).getItem().isDamageable(null)) {
                ItemStack item = this.mc.player.getInventory().getArmor(i);
                if(item.getShareTag() != null && item.getShareTag().contains(statTag)){
                    stat += Double.parseDouble(item.getTag().get(statTag).getAsString());
                }
            }
        }
        if (this.mc.player.getMainHandItem() != ItemStack.EMPTY) {
            ItemStack item = this.mc.player.getMainHandItem();
            if(item.getShareTag() != null && item.getShareTag().contains(statTag)){
                stat += Double.parseDouble(item.getTag().get(statTag).getAsString()) - 1;
            }
            else if(statTag.equals(DAMAGE_TAG)){
                double itemAttack = 0;
                for(net.minecraft.world.entity.ai.attributes.Attribute a: item.getAttributeModifiers(EquipmentSlot.MAINHAND).keys()){
                    if(a.getDescriptionId().equals("attribute.name.generic.attack_damage")){
                        for(net.minecraft.world.entity.ai.attributes.AttributeModifier am: item.getAttributeModifiers(EquipmentSlot.MAINHAND).get(a)){
                            itemAttack += am.getAmount();
                        }
                    }
                }
                stat += itemAttack;
            }
        }
        return stat;
    }
}
