package fr.arcane.spellcast.spells;

import fr.arcane.spellcast.Spellcast;
import fr.arcane.spellcast.utils.DisplayUtils;
import fr.arcane.spellcast.utils.Quaternion;
import fr.arcane.spellcast.utils.SpellUtils;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;

public class Aerolithe {

    public static String NAME = "Aérolithe";
    public static String DESCRIPTION = "Repousse violamment ceux qui osent le déclencher";
    public static int COLOR = 12181503; //very light blue
    public static String ID = "aerolithe";
    public static final Elements ELEMENT = Elements.WIND;

    boolean doesConsumeScroll = true;
    boolean canHarmCaster = true;


    Player caster;
    ItemStack scroll;

    public Aerolithe(Player caster, ItemStack scroll) {
        this.caster = caster;
        this.scroll = scroll;

    }

    public static ItemStack getScroll() {
        return SpellUtils.createScrollItem(COLOR, NAME, DESCRIPTION);
    }

    public ItemStack getSmallCircle() {
        return SpellUtils.createSpellCircleItem(COLOR);
    }

    public void effect() {

        ItemDisplay flyCircle = DisplayUtils.createCircleDisplay(caster.getLocation(), getSmallCircle());
        flyCircle.setTeleportDuration(1);

        Transformation transformation = flyCircle.getTransformation();
        transformation.getTranslation().sub(0.5f, 0, 0.5f);
        transformation.getLeftRotation().set(Quaternion.rotateZ(transformation.getLeftRotation(), 270));
        transformation.getLeftRotation().set(Quaternion.rotateX(transformation.getLeftRotation(), 90));
        transformation.getScale().set(2.3);

        flyCircle.setTransformation(transformation);

        caster.setAllowFlight(true);


        new BukkitRunnable() {
            @Override
            public void run() {
                if (flyCircle.isDead()) {
                    this.cancel();
                    return;
                }

                Location direction = caster.getLocation().clone();
                direction.setDirection(direction.getDirection().multiply(-0.5));
                direction.add(direction.getDirection());
                Location newLocation = new Location(caster.getWorld(), direction.getX(), caster.getLocation().getY() + 1, direction.getZ(), caster.getLocation().getYaw(), 0);

                int rotateSpeed = getRotateSpeed(caster);

                newLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, newLocation, 10);
                newLocation.getWorld().spawnParticle(Particle.REDSTONE, caster.getLocation().subtract(0, 1, 0), 5, new Particle.DustOptions(Color.WHITE, 1));

                transformation.getLeftRotation().set(Quaternion.rotateY(transformation.getLeftRotation(), rotateSpeed));
                flyCircle.teleport(newLocation);
                flyCircle.setTransformation(transformation);

            }
        }.runTaskTimer(Spellcast.INSTANCE, 0L, 1L);

        new BukkitRunnable() {
            @Override
            public void run() {
                flyCircle.remove();
                caster.setFlying(false);
                if (!caster.getGameMode().equals(GameMode.CREATIVE)) {
                    caster.setAllowFlight(false);
                }
            }
        }.runTaskLater(Spellcast.INSTANCE, 600L);

    }

    private int getRotateSpeed(Player caster) {
        if (caster.isSprinting()) {
            return 8;
        } else if (caster.isFlying()) {
            return 12;
        } else if (caster.isSneaking()) {
            return -3;
        }
        return 2;
    }


}
