package net.spellcraftgaming.rpghud.gui.hud.element.modern;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.spellcraftgaming.rpghud.gui.hud.element.vanilla.HudElementDetailsVanilla;
import net.spellcraftgaming.rpghud.main.ModRPGHud;
import net.spellcraftgaming.rpghud.settings.Settings;

import java.text.DecimalFormat;

public class HudElementDetailsModern extends HudElementDetailsVanilla {

	public HudElementDetailsModern() {
		super();
		this.posX = 0;
		this.posY = 0;
		this.elementWidth = 0;
		this.elementHeight = 0;
		this.moveable = true;
	}

	@Override
	public boolean checkConditions() {
		return !this.mc.options.renderDebug && !this.isChatOpen();
	}

	@Override
	public void drawElement(GuiGraphics gg, float zLevel, float partialTicks, int scaledWidth,
			int scaledHeight) {
		this.offset = (this.settings.getBoolValue(Settings.render_player_face) ? 0 : 16)
				+ ((this.settings.getBoolValue(Settings.show_numbers_health)
						&& this.settings.getBoolValue(Settings.show_numbers_food)) ? 0 : 8);
		int width = calculateWidth();
		if (this.settings.getBoolValue(Settings.show_armor)) {
			gg.pose().translate(this.settings.getPositionValue(Settings.armor_det_position)[0],
					this.settings.getPositionValue(Settings.armor_det_position)[1], 0);
			drawArmorDetails(gg, width);
			gg.pose().translate(-this.settings.getPositionValue(Settings.armor_det_position)[0],
					-this.settings.getPositionValue(Settings.armor_det_position)[1], 0);
		}
		gg.pose().translate(this.settings.getPositionValue(Settings.item_det_position)[0],
				this.settings.getPositionValue(Settings.item_det_position)[1], 0);
		drawItemDetails(gg, InteractionHand.MAIN_HAND, width);
		drawItemDetails(gg, InteractionHand.OFF_HAND, width);
		gg.pose().translate(-this.settings.getPositionValue(Settings.item_det_position)[0],
				-this.settings.getPositionValue(Settings.item_det_position)[1], 0);
		if (this.settings.getBoolValue(Settings.show_arrow_count)) {
			gg.pose().translate(this.settings.getPositionValue(Settings.arrow_det_position)[0],
					this.settings.getPositionValue(Settings.arrow_det_position)[1], 0);
			drawArrowCount(gg, width);
			gg.pose().translate(-this.settings.getPositionValue(Settings.arrow_det_position)[0],
					-this.settings.getPositionValue(Settings.arrow_det_position)[1], 0);
		}
		if (this.settings.getBoolValue(Settings.enable_repair_cost) && this.settings.getBoolValue(Settings.teldaria_mode)) {
			gg.pose().translate(this.settings.getPositionValue(Settings.repair_position)[0],
					this.settings.getPositionValue(Settings.repair_position)[1], 0);
			drawRepairCost(gg, width);
			gg.pose().translate(-this.settings.getPositionValue(Settings.repair_position)[0],
					-this.settings.getPositionValue(Settings.repair_position)[1], 0);
		}
	}

	/**
	 * Gets a displayable durability string from input item, using either Vanilla or Teldaria durability
	 * @param item input itemstack
	 * @return displayable durability string
	 */
	private String getDurabilityString(ItemStack item){
		String durabilityString = (item.getMaxDamage() - item.getDamageValue()) + "/" + item.getMaxDamage();
		if(this.settings.getBoolValue(Settings.teldaria_mode)){
			if(item.getShareTag() != null && item.getShareTag().contains("MMOITEMS_DURABILITY") && item.getShareTag().contains("MMOITEMS_MAX_DURABILITY")){
				durabilityString = item.getTag().get("MMOITEMS_DURABILITY").getAsString() + "/" + item.getTag().get("MMOITEMS_MAX_DURABILITY").getAsString();
			}
		}
		return durabilityString;
	}

	/** Calculates the width for the element background */
	private int calculateWidth() {
		int width = 0;
		for (int i = this.mc.player.getInventory().armor.size() - 1; i >= 0; i--) {
			if (this.mc.player.getInventory().getArmor(i) != ItemStack.EMPTY
					&& this.mc.player.getInventory().getArmor(i).getItem().isDamageable(null)) {
				ItemStack item = this.mc.player.getInventory().getArmor(i);
				if(this.settings.getBoolValue(Settings.show_durability_number)){
					width = Math.max(width, this.mc.font.width(getDurabilityString(item)));
				}
			}
		}
		ItemStack item = this.mc.player.getMainHandItem();
		if (item != ItemStack.EMPTY) {
			if (this.settings.getBoolValue(Settings.show_item_durability) && item.isDamageableItem()) {
				if(this.settings.getBoolValue(Settings.show_durability_number)){
					width = Math.max(width, this.mc.font.width(getDurabilityString(item)));
				}

			} else if (this.settings.getBoolValue(Settings.show_block_count) && item.getItem() instanceof BlockItem) {
				int x = this.mc.player.getInventory().getContainerSize();
				int z = 0;
				if (ModRPGHud.renderDetailsAgain[0] || !ItemStack.matches(this.itemMainHandLast, item)) {
					this.itemMainHandLast = item.copy();
					ModRPGHud.renderDetailsAgain[0] = false;

					for (int y = 0; y < x; y++) {
						item = this.mc.player.getInventory().getItem(y);
						if (item != ItemStack.EMPTY && Item.getId(item.getItem()) == Item
								.getId(this.mc.player.getMainHandItem().getItem())) {
							z += item.getCount();
						}
					}
					this.count1 = z;
				} else {
					z = this.count1;
				}
				if(this.settings.getBoolValue(Settings.show_durability_number)){
					String s = "x " + z;
					width = Math.max(width, this.mc.font.width(s));
				}
			}
		}
		item = this.mc.player.getOffhandItem();
		if (item != ItemStack.EMPTY) {
			if (this.settings.getBoolValue(Settings.show_item_durability) && item.isDamageableItem()) {
				if(this.settings.getBoolValue(Settings.show_durability_number)){
					String s = (item.getMaxDamage() - item.getDamageValue()) + "/" + item.getMaxDamage();
					width = Math.max(width, this.mc.font.width(s));
				}
			} else if (this.settings.getBoolValue(Settings.show_block_count) && item.getItem() instanceof BlockItem) {
				int x = this.mc.player.getInventory().getContainerSize();
				int z = 0;
				if (ModRPGHud.renderDetailsAgain[1] || !ItemStack.matches(this.itemOffhandLast, item)
						|| !ItemStack.matches(this.itemMainHandLast, item)) {
					this.itemOffhandLast = item.copy();
					ModRPGHud.renderDetailsAgain[1] = false;
					for (int y = 0; y < x; y++) {
						item = this.mc.player.getInventory().getItem(y);
						if (item != ItemStack.EMPTY && Item.getId(item.getItem()) == Item
								.getId(this.mc.player.getOffhandItem().getItem())) {
							z += item.getCount();
						}
					}
					this.count2 = z;
				} else {
					z = this.count2;
				}
				if(this.settings.getBoolValue(Settings.show_durability_number)){
					String s = "x " + z;
					width = Math.max(width, this.mc.font.width(s));
				}
			}
		}
		item = this.mc.player.getMainHandItem();
		if (this.settings.getBoolValue(Settings.show_arrow_count) && item != ItemStack.EMPTY
				&& this.mc.player.getMainHandItem().getItem() instanceof BowItem) {
			int x = this.mc.player.getInventory().getContainerSize();
			int z = 0;

			if (ModRPGHud.renderDetailsAgain[2] || !ItemStack.matches(this.itemMainHandLastArrow, item)) {
				ModRPGHud.renderDetailsAgain[2] = false;

				item = findAmmo(this.mc.player);
				if (item != ItemStack.EMPTY) {
					this.itemArrow = item.copy();
					for (int y = 0; y < x; y++) {
						ItemStack item3 = this.mc.player.getInventory().getItem(y);
						if (ItemStack.matches(item, item3)) {
							z += addArrowStackIfCorrect(item, item3);
						}
					}
					this.count3 = z;
				}
				this.count3 = 0;
			} else {
				z = this.count3;
			}
			if(this.settings.getBoolValue(Settings.show_durability_number)){
				String s = "x " + z;
				width = Math.max(width, this.mc.font.width(s));
			}
		}
		if (item == ItemStack.EMPTY || item == null) {
			this.itemMainHandLastArrow = ItemStack.EMPTY;
		} else {
			this.itemMainHandLastArrow = item.copy();
		}
		return width;
	}

	/**
	 * Draws the armor details
	 * 
	 * @param gg   the GUI to draw one
	 * @param width the width of the background
	 */
	protected void drawArmorDetails(GuiGraphics gg, int width) {
		boolean reduceSize = this.settings.getBoolValue(Settings.reduce_size);
		if (reduceSize)
			gg.pose().scale(0.5f, 0.5f, 0.5f);
		for (int i = this.mc.player.getInventory().armor.size() - 1; i >= 0; i--) {
			if (this.mc.player.getInventory().getArmor(i) != ItemStack.EMPTY
					&& this.mc.player.getInventory().getArmor(i).getItem().isDamageable(null)) {
				drawRect(gg, 2, 60 + this.offset, 20 + 12 + width, 20, 0xA0000000);
				ItemStack item = this.mc.player.getInventory().getArmor(i);
				this.renderGuiItemModel(item, 6 + this.settings.getPositionValue(Settings.armor_det_position)[0], 62 + this.settings.getPositionValue(Settings.armor_det_position)[1] + this.offset, reduceSize);
				if (this.settings.getBoolValue(Settings.show_durability_bar))
					this.renderItemDurabilityBar(gg, item, 6, 62 + this.offset, 1f);
				if(this.settings.getBoolValue(Settings.show_durability_number)){
					gg.drawCenteredString( this.mc.font, getDurabilityString(item), 6 + width, 66 + this.offset, -1);
				}
				this.offset += 20;
			}
		}
		if (reduceSize)
			gg.pose().scale(2f, 2f, 2f);
	}

	/**
	 * Draws the held item details
	 * 
	 * @param gg   the GUI to draw on
	 * @param hand  the hand whose item should be detailed
	 * @param width the width of the background
	 */
	protected void drawItemDetails(GuiGraphics gg, InteractionHand hand, int width) {
		boolean reduceSize = this.settings.getBoolValue(Settings.reduce_size);
		if (reduceSize)
			gg.pose().scale(0.5f, 0.5f, 0.5f);
		ItemStack item = this.mc.player.getItemInHand(hand);
		if (item != ItemStack.EMPTY) {
			if (this.settings.getBoolValue(Settings.show_item_durability) && item.isDamageableItem()) {
				drawRect(gg, 2, 60 + this.offset, 20 + 12 + width, 20, 0xA0000000);
				this.renderGuiItemModel(item, 6 + this.settings.getPositionValue(Settings.item_det_position)[0], 62 + this.settings.getPositionValue(Settings.item_det_position)[1] + this.offset, reduceSize);
				if (this.settings.getBoolValue(Settings.show_durability_bar))
					this.renderItemDurabilityBar(gg, item, 6, 62 + this.offset, 1f);
				if(this.settings.getBoolValue(Settings.show_durability_number)){
					gg.drawCenteredString( this.mc.font, getDurabilityString(item), 6 + width, 66 + this.offset, -1);
				}
				this.offset += 20;

			} else if (this.settings.getBoolValue(Settings.show_block_count) && item.getItem() instanceof BlockItem) {
				int x = this.mc.player.getInventory().getContainerSize();
				int z = 0;
				if ((hand == InteractionHand.MAIN_HAND ? ModRPGHud.renderDetailsAgain[0] : ModRPGHud.renderDetailsAgain[1])
						|| !ItemStack.matches(
								(hand == InteractionHand.MAIN_HAND ? this.itemMainHandLast : this.itemOffhandLast), item)
						|| !ItemStack.matches(this.itemMainHandLast, item)) {
					if (hand == InteractionHand.MAIN_HAND) {
						this.itemMainHandLast = item.copy();
						ModRPGHud.renderDetailsAgain[0] = false;
					} else {
						this.itemOffhandLast = item.copy();
						ModRPGHud.renderDetailsAgain[1] = false;
					}
					for (int y = 0; y < x; y++) {
						item = this.mc.player.getInventory().getItem(y);
						if (item != ItemStack.EMPTY && Item.getId(item.getItem()) == Item
								.getId(this.mc.player.getItemInHand(hand).getItem())) {
							z += item.getCount();
						}
					}
					if (hand == InteractionHand.MAIN_HAND)
						this.count1 = z;
					else
						this.count2 = z;
				} else {
					if (hand == InteractionHand.MAIN_HAND)
						z = this.count1;
					else
						z = this.count2;
				}

				item = this.mc.player.getItemInHand(hand);
				drawRect(gg, 2, 60 + this.offset, 20 + 12 + width, 20, 0xA0000000);
				String s = "x " + z;
				this.renderGuiItemModel(item, 6 + this.settings.getPositionValue(Settings.item_det_position)[0], 62 + this.settings.getPositionValue(Settings.item_det_position)[1] + this.offset, reduceSize);
				gg.drawCenteredString( this.mc.font, s, 6 + width, 66 + this.offset, -1);
				this.offset += 20;
			}
		}
		if (reduceSize)
			gg.pose().scale(2f, 2f, 2f);
	}

	/**
	 * Draws the amount of arrows the player has in his inventory on the screen
	 * 
	 * @param gg   the GUI to draw on
	 * @param width the width of the background
	 */
	protected void drawArrowCount(GuiGraphics gg, int width) {
		boolean reduceSize = this.settings.getBoolValue(Settings.reduce_size);
		ItemStack item = this.mc.player.getMainHandItem();
		if (this.settings.getBoolValue(Settings.show_arrow_count) && item != ItemStack.EMPTY
				&& this.mc.player.getMainHandItem().getItem() instanceof BowItem) {
			int x = this.mc.player.getInventory().getContainerSize();
			int z = 0;

			if (ModRPGHud.renderDetailsAgain[2] || !ItemStack.matches(this.itemMainHandLastArrow, item)) {
				ModRPGHud.renderDetailsAgain[2] = false;

				item = findAmmo(this.mc.player);
				if (item != ItemStack.EMPTY) {
					this.itemArrow = item.copy();
					for (int y = 0; y < x; y++) {
						ItemStack item3 = this.mc.player.getInventory().getItem(y);
						if (ItemStack.matches(item, item3)) {
							z += addArrowStackIfCorrect(item, item3);
						}
					}
					this.count3 = z;
				} else {
					this.count3 = 0;
				}
			} else {
				z = this.count3;
			}
			drawRect(gg, 2, 60 + this.offset, 20 + 12 + width, 20, 0xA0000000);
			String s = "x " + z;
			if (this.itemArrow == ItemStack.EMPTY)
				this.itemArrow = new ItemStack(Items.ARROW);
			this.renderGuiItemModel(this.itemArrow, 6 + this.settings.getPositionValue(Settings.item_det_position)[0], 62 + this.settings.getPositionValue(Settings.armor_det_position)[1] + this.offset, reduceSize);
			gg.drawCenteredString( this.mc.font, s, 6 + width, 66 + this.offset, -1);
			this.offset += 20;

		}
		if (item == ItemStack.EMPTY || item == null) {
			this.itemMainHandLastArrow = ItemStack.EMPTY;
		} else {
			this.itemMainHandLastArrow = item.copy();
		}
	}

	/**
	 * Draws the repair cost of the players full loadout on the Teldaria server
	 *
	 * @param gg   the GUI to draw on
	 * @param width the width of the background
	 */
	protected void drawRepairCost(GuiGraphics gg, int width){
		boolean reduceSize = this.settings.getBoolValue(Settings.reduce_size);
		if (reduceSize)
			gg.pose().scale(0.5f, 0.5f, 0.5f);
		String repairCostString = getRepairString();
		if(repairCostString != null){
			drawRect(gg, 2, 60 + this.offset, 20 + 12 + width, 20, 0xA0000000);
			this.renderGuiItemModel(new ItemStack(Blocks.ANVIL), 6 + this.settings.getPositionValue(Settings.repair_position)[0], 62 + this.settings.getPositionValue(Settings.repair_position)[1] + this.offset, reduceSize);
			gg.drawCenteredString( this.mc.font, repairCostString, 6 + width, 66 + this.offset, -1);
			this.offset += 20;
		}
		if (reduceSize)
			gg.pose().scale(2f, 2f, 2f);
	}

	protected String getRepairString(){
		double repairCost = 0;
		for (int i = this.mc.player.getInventory().armor.size() - 1; i >= 0; i--) {
			if (this.mc.player.getInventory().getArmor(i) != ItemStack.EMPTY
					&& this.mc.player.getInventory().getArmor(i).getItem().isDamageable(null)) {
				ItemStack item = this.mc.player.getInventory().getArmor(i);
				repairCost += getTierRepairCost(item);
			}
		}
		ItemStack item = this.mc.player.getMainHandItem();
		if (item != ItemStack.EMPTY) {
			repairCost += getTierRepairCost(item);
		}

		String repairCostString = null;
		if(repairCost > 0){
			DecimalFormat df = new DecimalFormat("#,##0.00");
			repairCostString = "$" + df.format(repairCost);
		}
		return repairCostString;
	}
	protected double getTierRepairCost(ItemStack item){
		if(item.getShareTag() != null && item.getShareTag().contains("MMOITEMS_DURABILITY") && item.getShareTag().contains("MMOITEMS_MAX_DURABILITY") && item.getShareTag().contains("MMOITEMS_TIER")){
			int durabilityDamage = Integer.parseInt(item.getTag().get("MMOITEMS_MAX_DURABILITY").getAsString()) - Integer.parseInt(item.getTag().get("MMOITEMS_DURABILITY").getAsString());
			return durabilityDamage * getTierRepairRate(item.getTag().get("MMOITEMS_TIER").getAsString());
		}
		return 0;
	}
	protected double getTierRepairRate(String tier){
        return switch (tier.toLowerCase()) {
            case "mythic" -> 14.000;
            case "transcendent" -> 7.500;
            case "heavenly" -> 4.666;
            case "legendary" -> 3.333;
            default -> 0.000;
        };
    }
}

