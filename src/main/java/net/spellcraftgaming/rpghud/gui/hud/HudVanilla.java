package net.spellcraftgaming.rpghud.gui.hud;

import net.minecraft.client.Minecraft;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElement;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElementEmpty;
import net.spellcraftgaming.rpghud.gui.hud.element.simple.HudElementTeldariaSimple;
import net.spellcraftgaming.rpghud.gui.hud.element.vanilla.HudElementClockVanilla;
import net.spellcraftgaming.rpghud.gui.hud.element.vanilla.HudElementCompassVanilla;
import net.spellcraftgaming.rpghud.gui.hud.element.vanilla.HudElementDetailsVanilla;
import net.spellcraftgaming.rpghud.gui.hud.element.vanilla.HudElementEntityInspectVanilla;
import net.spellcraftgaming.rpghud.gui.hud.element.vanilla.HudElementMiscVanilla;
import net.spellcraftgaming.rpghud.gui.hud.element.vanilla.HudElementMobEffectsVanilla;

public class HudVanilla extends Hud {

	public HudVanilla(Minecraft mc, String hudKey, String hudName) {
		super(mc, hudKey, hudName);
	}

	@Override
	public HudElement setElementHotbar() {
		return null;
	}

	@Override
	public HudElement setElementHealth() {
		return null;
	}

	@Override
	public HudElement setElementFood() {
		return null;
	}

	@Override
	public HudElement setElementArmor() {
		return null;
	}

	@Override
	public HudElement setElementAir() {
		return null;
	}

	@Override
	public HudElement setElementExperience() {
		return null;
	}

	@Override
	public HudElement setElementLevel() {
		return null;
	}

	@Override
	public HudElement setElementJumpBar() {
		return null;
	}

	@Override
	public HudElement setElementHealthMount() {
		return null;
	}

	@Override
	public HudElement setElementClock() {
		return new HudElementClockVanilla();
	}

	@Override
	public HudElement setElementDetails() {
		return new HudElementDetailsVanilla();
	}

	@Override
	public HudElement setElementWidget() {
		return new HudElementEmpty();
	}

	@Override
	protected HudElement setElementCompass() {
		return new HudElementCompassVanilla();
	}

	@Override
	protected HudElement setElementEntityInspect() {
		return new HudElementEntityInspectVanilla();
	}

    @Override
    protected HudElement setElementMobEffects() {
        return new HudElementMobEffectsVanilla();
    }
    
	@Override
	protected HudElement setElementMisc() {
		return new HudElementMiscVanilla();
	}
	@Override
	protected HudElement setElementTeldaria() { return new HudElementTeldariaSimple(); }
}
