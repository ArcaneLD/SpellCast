package fr.arcane.spellcast.spells;

import fr.arcane.spellcast.Spellcast;
import fr.arcane.spellcast.utils.MagicCircleUtils;
import fr.arcane.spellcast.utils.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Repulsius {

    static String ID = "repulsius";
    public static String NAME = "&aRepulsius";
    public static String DESCRIPTION = "&aRepousse violamment ceux qui osent le déclencher";
    public static int COLOR = 6029061; //light_green
    public static final Elements ELEMENT = Elements.WIND;

    public static final boolean doesConsumeScroll = true;
    public static final boolean canHarmCaster = true;

    Player caster;
    Location spellLocation;
    ItemStack scroll;
    BlockFace blockFace;

    public Repulsius(Player caster, Location spellLocation, ItemStack scroll, BlockFace blockFace) {
        this.caster = caster;
        this.spellLocation = spellLocation;
        this.scroll = scroll;
        this.blockFace = blockFace;

    }

    public static ItemStack getScroll() {
        return SpellUtils.createScrollItem(COLOR, NAME, DESCRIPTION);
    }

    public ItemStack getSmallCircle() {
        return SpellUtils.createSpellCircleItem(COLOR);
    }

    public void effect() {

        Map<BlockFace, Vector> vectorMap = new HashMap<>();
        vectorMap.put(BlockFace.UP, new Vector(0, 2, 0));
        vectorMap.put(BlockFace.DOWN, new Vector(0, -2, 0));
        vectorMap.put(BlockFace.NORTH, new Vector(0, 0.2, -3));
        vectorMap.put(BlockFace.SOUTH, new Vector(0, 0.2, 3));
        vectorMap.put(BlockFace.EAST, new Vector(3, 0.2, 0));
        vectorMap.put(BlockFace.WEST, new Vector(-3, 0.2, 0));

        ItemDisplay circle = MagicCircleUtils.placeCircle(blockFace, getSmallCircle(), spellLocation, 5);

        new BukkitRunnable() {

            @Override
            public void run() {
                if (circle.isDead()) {
                    this.cancel();
                    return;
                }

                for (Entity entity : circle.getNearbyEntities(1, 1, 1)) {
                    // Shoot Entity in the Air
                    entity.setVelocity(vectorMap.get(blockFace));
                    circle.remove();
                    circle.getWorld().spawnParticle(Particle.SPIT, circle.getLocation(), 50);
                }

            }
        }.runTaskTimer(Spellcast.INSTANCE, 0L, 1L);

    }

}
