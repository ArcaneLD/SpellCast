package fr.arcane.spellcast.utils;

import org.bukkit.Color;

public class ColorConverter {


    public static Color getBukkitColor(int decimalColor) {
        java.awt.Color originalColor = new java.awt.Color(decimalColor);

        int red = originalColor.getRed();
        int green = originalColor.getGreen();
        int blue = originalColor.getBlue();

        return Color.fromRGB(red, green, blue);
    }

    public static int rgbToDecimal(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }
}
