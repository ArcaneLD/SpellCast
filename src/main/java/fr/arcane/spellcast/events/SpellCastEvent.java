package fr.arcane.spellcast.events;

import fr.arcane.spellcast.spells.Aerolithe;
import fr.arcane.spellcast.spells.Partisan;
import fr.arcane.spellcast.spells.Repulsius;
import fr.arcane.spellcast.spells.Spells;
import fr.arcane.spellcast.utils.ColorConverter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class SpellCastEvent implements Listener {

    @EventHandler
    public void onSpellCast(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            handleSpellCast(event);
        }
    }


    private void handleSpellCast(PlayerInteractEvent event) {
        ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemInHand.getItemMeta();

        // Check if the item has a custom model data
        if (itemMeta == null || !itemMeta.hasCustomModelData() || itemInHand.getType() != Material.LEATHER_HORSE_ARMOR) { //Leather Horse Armor is the item used by the ressource pack so I can have spells of almost any color from a single model/texture
            return;
        }

        // Obtain the color of the item
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
        int color = ColorConverter.rgbToDecimal(leatherArmorMeta.getColor().getRed(), leatherArmorMeta.getColor().getGreen(), leatherArmorMeta.getColor().getBlue());

        // Handle the appropriate spell based on action and color
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            handleRightClickBlock(event, color);
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            handleRightClickAir(event, color);
        }
    }


    private void handleRightClickBlock(PlayerInteractEvent event, int color) {
        if (color == Spells.REPULSIUS.getColor()) {
            Repulsius spell = new Repulsius(event.getPlayer(), event.getClickedBlock().getLocation().add(0, 1, 0), event.getItem(), event.getBlockFace());
            spell.effect();
        }
    }


    private void handleRightClickAir(PlayerInteractEvent event, int color) {
        if (color == Spells.AEROLITHE.getColor()) {
            Aerolithe spell = new Aerolithe(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
            spell.effect();
        } else if (color == Spells.PARTISAN.getColor()) {
            Partisan spell = new Partisan(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
            spell.effect();
        }
    }
}