package fr.arcane.spellcast.utils;

import fr.arcane.spellcast.Spellcast;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;

import java.util.HashMap;
import java.util.Map;

public class MagicCircleUtils {


    public static ItemDisplay placeCircle(BlockFace blockFace, ItemStack itemStack, Location spellLocation, double rotateSpeed) {
        ItemDisplay itemDisplay = null;

        // Create a data structure to store transformation settings for each BlockFace
        Map<BlockFace, Location> offsetMap = new HashMap<>();
        offsetMap.put(BlockFace.UP, spellLocation.clone().add(0, 0.585, 0));
        offsetMap.put(BlockFace.DOWN, spellLocation.clone().subtract(0, 0.585, 0));
        offsetMap.put(BlockFace.NORTH, spellLocation.clone().subtract(0, 0.5, 0.085));
        offsetMap.put(BlockFace.SOUTH, spellLocation.clone().add(0, -0.5, 0.085));
        offsetMap.put(BlockFace.EAST, spellLocation.clone().add(0.085, -0.5, 0));
        offsetMap.put(BlockFace.WEST, spellLocation.clone().add(-0.085, -0.5, 0));

        // Retrieve the location based on the blockFace
        Location itemLocation = offsetMap.get(blockFace);
        if (itemLocation == null) {
            return null;  // Return early if the blockFace is not valid
        }

        // Create the ItemDisplay
        itemDisplay = DisplayUtils.createCircleDisplay(itemLocation, itemStack);
        Transformation transformation = itemDisplay.getTransformation();
        setTransformationForBlockFace(blockFace, transformation);

        // Set up the rotation runnable
        setupRotationRunnable(itemDisplay, transformation, rotateSpeed);

        return itemDisplay;
    }

    private static void setTransformationForBlockFace(BlockFace blockFace, Transformation transformation) {
        // Set initial transformations based on blockFace
        switch (blockFace) {
            case UP:
                // No specific transformation needed
                break;
            case DOWN:
                // No specific transformation needed
                break;
            case NORTH:
                transformation.getLeftRotation().set(Quaternion.rotateX(transformation.getLeftRotation(), 90));
                break;
            case SOUTH:
                transformation.getLeftRotation().set(Quaternion.rotateZ(transformation.getLeftRotation(), 270));
                transformation.getLeftRotation().set(Quaternion.rotateX(transformation.getLeftRotation(), 270));
                break;
            case EAST:
                transformation.getLeftRotation().set(Quaternion.rotateZ(transformation.getLeftRotation(), 270));
                transformation.getLeftRotation().set(Quaternion.rotateX(transformation.getLeftRotation(), 180));
                break;
            case WEST:
                transformation.getLeftRotation().set(Quaternion.rotateZ(transformation.getLeftRotation(), 270));
                transformation.getLeftRotation().set(Quaternion.rotateX(transformation.getLeftRotation(), 0));
                break;
        }
    }

    private static void setupRotationRunnable(ItemDisplay itemDisplay, Transformation transformation, double rotateSpeed) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (itemDisplay.isDead()) {
                    this.cancel();
                    return;
                }
                transformation.getLeftRotation().set(Quaternion.rotateY(transformation.getLeftRotation(), rotateSpeed));
                itemDisplay.setTransformation(transformation);
            }
        }.runTaskTimer(Spellcast.INSTANCE, 0L, 1L);
    }


}
