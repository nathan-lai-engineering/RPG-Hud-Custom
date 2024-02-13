package net.spellcraftgaming.rpghud.gui.hud;

import net.minecraft.client.Minecraft;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElement;
import net.spellcraftgaming.rpghud.gui.hud.element.modern.HudElementAirModern;
import net.spellcraftgaming.rpghud.gui.hud.element.modern.HudElementCompassModern;
import net.spellcraftgaming.rpghud.gui.hud.element.modern.HudElementDetailsModern;
import net.spellcraftgaming.rpghud.gui.hud.element.modern.HudElementEntityInspectModern;
import net.spellcraftgaming.rpghud.gui.hud.element.simple.*;

public class HudSimple extends HudVanilla{

	public HudSimple(Minecraft mc, String hudKey, String hudName) {
		super(mc, hudKey, hudName);
	}
	
	@Override
	public HudElement setElementAir() {
		return new HudElementAirModern();
	}
	@Override
	public HudElement setElementDetails() {
		return new HudElementDetailsModern();
	}
	
	@Override
	public HudElement setElementExperience() {
		return new HudElementExperienceSimple();
	}
	
	@Override
	public HudElement setElementArmor() {
		return new HudElementArmorSimple();
	}

	@Override
	public HudElement setElementFood() {
		return new HudElementFoodSimple();
	}
	
	@Override
	public HudElement setElementHealth() {
		return new HudElementHealthSimple();
	}
	
	@Override
	public HudElement setElementHealthMount() {
		return new HudElementHealthMountSimple();
	}
	
	@Override
	protected HudElement setElementCompass() {
		return new HudElementCompassModern();
	}
	
	@Override
	public HudElement setElementLevel() {
		return new HudElementLevelSimple();
	}
	
	@Override
	public HudElement setElementHotbar() {
		return new HudElementHotbarSimple();
	}
	
	@Override
	protected HudElement setElementEntityInspect() {
		return new HudElementEntityInspectModern();
	}

}
