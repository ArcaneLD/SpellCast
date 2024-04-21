package fr.arcane.spellcast.utils;

import fr.arcane.spellcast.Spellcast;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Collections;

public class SpellUtils {

    public static ItemStack createScrollItem(int color, String name, String description) {
        ItemStack item = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        meta.setColor(ColorConverter.getBukkitColor(color));
        meta.setCustomModelData(10000);
        meta.setDisplayName(Spellcast.format(name));
        meta.setLore(Collections.singletonList(Spellcast.format(description)));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createSpellCircleItem(int color) {
        ItemStack item = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

        meta.setColor(ColorConverter.getBukkitColor(color));
        meta.setCustomModelData(10001);
        item.setItemMeta(meta);
        return item;
    }
}
