package fr.arcane.spellcast.utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Transformation;

public class DisplayUtils {


    //deprecated but might be useful later
    public static ArmorStand createCircleStand(Location spellLocation) {
        ArmorStand circle = spellLocation.getWorld().spawn(spellLocation.add(0, 50, 0), ArmorStand.class);
        circle.setInvisible(true);
        circle.setGravity(false);
        circle.setBasePlate(false);
        circle.setArms(true);
        circle.setInvulnerable(true);

        circle.setLeftArmPose(new EulerAngle(180, -180, -90));
        circle.setRightArmPose(new EulerAngle(0, -180, -90));

        circle.teleport(spellLocation.subtract(0, 51.5, 0));

        return circle;
    }


    //Item Display Pre-set
    public static ItemDisplay createCircleDisplay(Location spellLocation, ItemStack item) {
        ItemDisplay circle = spellLocation.getWorld().spawn(spellLocation, ItemDisplay.class);
        Transformation transformation = circle.getTransformation();
        transformation.getTranslation().add(0.5f, 0f, 0.5f); //center the display
        circle.setTransformation(transformation);

        circle.setInvulnerable(true);
        circle.setInterpolationDuration(1);

        circle.setViewRange(100f);
        circle.setItemStack(item);
        return circle;
    }

    public static Transformation clone(Transformation transformation) {
        return new Transformation(transformation.getTranslation(), transformation.getLeftRotation(), transformation.getScale(), transformation.getRightRotation());
    }


}
