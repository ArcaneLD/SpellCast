package fr.arcane.spellcast.spells;

import fr.arcane.spellcast.Spellcast;
import fr.arcane.spellcast.utils.DisplayUtils;
import fr.arcane.spellcast.utils.Quaternion;
import fr.arcane.spellcast.utils.SpellUtils;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Partisan {

    // Class Constants
    public static final String NAME = "&ePartisan";
    public static final String DESCRIPTION = "&eDéchaine une pluie d'armes mortelle sur l'adversaire";
    public static final int COLOR = 16772378; // yellow
    public static final String ID = "partisan";
    public static final Elements ELEMENT = Elements.SUN;

    public static final boolean doesConsumeScroll = true;
    public static final boolean canHarmCaster = false;

    private static final float FLY_SPEED = 0.2f;
    private static final float WALK_SPEED = 0.2f;
    private static final long TASK_DELAY = 0L;
    private static final long TASK_INTERVAL = 1L;
    private static final long TASK_DURATION = 200L;

    // Instance variables
    private final Player caster;
    private final ItemStack scroll;


    public Partisan(Player caster, ItemStack scrollUsed) {
        this.caster = caster;
        this.scroll = scrollUsed;
    }

    // Get scroll ItemStack
    public static ItemStack getScroll() {
        return SpellUtils.createScrollItem(COLOR, NAME, DESCRIPTION);
    }

    // Get small magic circle ItemStack
    public ItemStack getSmallCircle() {
        return SpellUtils.createSpellCircleItem(COLOR);
    }

    // Spells effect
    public void effect() {

        // Freeze (more like a big slow down) the player in the air
        freezePlayer();

        // Create and position the magic circles around the player
        List<ItemDisplay> magicCircles = createMagicCircles();

        // Set transformations for the circles
        setCircleTransformations(magicCircles);

        // Start tasks
        setupCirclesTask(magicCircles);
        setupFireballTask(magicCircles);
        cleanupCirclesTask(magicCircles);
    }

    // Freeze the player in the air
    private void freezePlayer() {
        caster.setFlySpeed(FLY_SPEED);
        caster.setWalkSpeed(WALK_SPEED);
    }

    // Create and position magic circles
    private List<ItemDisplay> createMagicCircles() {
        List<ItemDisplay> magicCircles = new ArrayList<>();
        magicCircles.add(DisplayUtils.createCircleDisplay(caster.getLocation().clone().add(0, 3, 0), getSmallCircle()));
        magicCircles.add(DisplayUtils.createCircleDisplay(caster.getLocation().clone().add(0, 2, 0), getSmallCircle()));
        magicCircles.add(DisplayUtils.createCircleDisplay(caster.getLocation().clone().add(0, 3, 0), getSmallCircle()));
        magicCircles.add(DisplayUtils.createCircleDisplay(caster.getLocation().clone().add(0, 2, 0), getSmallCircle()));
        return magicCircles;
    }

    // Set transformations for the circles
    private void setCircleTransformations(List<ItemDisplay> magicCircles) {
        for (ItemDisplay circle : magicCircles) {

            Transformation transformation = initializeTransformation(circle.getTransformation());
            circle.setTransformation(transformation);
        }
        adjustCirclePositions(magicCircles);
    }

    // Initialize transformation
    private Transformation initializeTransformation(Transformation transformation) {
        transformation.getTranslation().sub(0.5f, 0.5f, 0.5f);
        transformation.getLeftRotation().set(Quaternion.rotateZ(transformation.getLeftRotation(), 270));
        transformation.getLeftRotation().set(Quaternion.rotateX(transformation.getLeftRotation(), 90));
        transformation.getScale().set(2);
        return transformation;
    }

    // Adjust circle positions
    private void adjustCirclePositions(List<ItemDisplay> magicCircles) {
        Transformation transformation0 = DisplayUtils.clone(magicCircles.get(0).getTransformation());
        transformation0.getTranslation().add(2.5f, 0, 0);
        Transformation transformation1 = DisplayUtils.clone(magicCircles.get(1).getTransformation());
        transformation1.getTranslation().add(1f, 0, 0);
        Transformation transformation2 = DisplayUtils.clone(magicCircles.get(2).getTransformation());
        transformation2.getTranslation().add(-2.5f, 0, 0);
        Transformation transformation3 = DisplayUtils.clone(magicCircles.get(3).getTransformation());
        transformation3.getTranslation().add(-1f, 0, 0);

        // to be fixed in later bukkit versions cuz ugly

        magicCircles.get(0).setTransformation(transformation0);
        magicCircles.get(1).setTransformation(transformation1);
        magicCircles.get(2).setTransformation(transformation2);
        magicCircles.get(3).setTransformation(transformation3);

    }

    // Setup task for circles' movement
    private void setupCirclesTask(List<ItemDisplay> magicCircles) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (magicCircles.get(0).isDead()) {
                    this.cancel();
                    return;
                }

                updateCirclePositions(magicCircles);

                // Slow down the player
                caster.setVelocity(new Vector(0, 0, 0));
            }
        }.runTaskTimer(Spellcast.INSTANCE, TASK_DELAY, TASK_INTERVAL);
    }

    // Update circle positions around the caster
    private void updateCirclePositions(List<ItemDisplay> magicCircles) {
        Vector direction = caster.getLocation().getDirection();
        Vector right = new Vector(-direction.getZ(), 0, direction.getX()).normalize();
        Vector left = new Vector(direction.getZ(), 0, -direction.getX()).normalize();

        magicCircles.get(0).teleport(caster.getLocation().add(left.multiply(5).normalize()).add(0, 3, 0));
        magicCircles.get(2).teleport(caster.getLocation().add(right.multiply(5).normalize()).add(0, 3, 0));
        magicCircles.get(1).teleport(caster.getLocation().add(left.multiply(1.25).normalize()).add(0, 1, 0));
        magicCircles.get(3).teleport(caster.getLocation().add(right.multiply(1.25).normalize()).add(0, 1, 0));
    }

    // Setup task for throwing Fireballs
    private void setupFireballTask(List<ItemDisplay> magicCircles) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (magicCircles.get(0).isDead()) {
                    this.cancel();
                    return;
                }

                Location ballLocation = getRandomBallLocation(magicCircles);

                Vector direction = caster.getLocation().getDirection().multiply(2);

                Fireball fireball = ballLocation.getWorld().spawn(ballLocation, Fireball.class);
                fireball.setDirection(direction);
                fireball.setIsIncendiary(false);
                fireball.setVisualFire(true);
                fireball.setShooter(caster);
            }
        }.runTaskTimer(Spellcast.INSTANCE, TASK_DELAY, 5);
    }

    // Get a random ball location from the magic circles
    private Location getRandomBallLocation(List<ItemDisplay> magicCircles) {
        Random random = new Random();
        int spawnLoc = random.nextInt(4);
        return magicCircles.get(spawnLoc).getLocation();
    }

    // Cleanup task for removing the circles
    private void cleanupCirclesTask(List<ItemDisplay> magicCircles) {
        new BukkitRunnable() {
            @Override
            public void run() {
                magicCircles.get(0).remove();
                magicCircles.get(1).remove();
                magicCircles.get(2).remove();
                magicCircles.get(3).remove();
            }
        }.runTaskLater(Spellcast.INSTANCE, TASK_DURATION);
    }
}
