package fr.arcane.spellcast.commands;

import fr.arcane.spellcast.spells.Aerolithe;
import fr.arcane.spellcast.spells.Partisan;
import fr.arcane.spellcast.spells.Repulsius;
import fr.arcane.spellcast.spells.Spells;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GiveScrollCommand implements CommandExecutor {
    // Map to associate spell IDs with their classes
    private final Map<String, Spells> spellMap = new HashMap<>();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player
        if (sender instanceof Player && sender.isOp()) {
            Player player = (Player) sender;

            spellMap.put(Spells.REPULSIUS.getID(), Spells.REPULSIUS);
            spellMap.put(Spells.AEROLITHE.getID(), Spells.AEROLITHE);
            spellMap.put(Spells.PARTISAN.getID(), Spells.PARTISAN);


            // Check if arguments are provided (to be improved, I want new spells to be added automatically to this string)
            if (args.length == 0) {
                player.sendMessage("Please specify a spell: repulsius, aerolithe, or partisan.");
                return false;
            }

            // Obtain the provided argument
            String spellId = args[0];

            // Check if the spell ID is valid
            Spells spell = spellMap.get(spellId.toLowerCase());
            if (spell == null) {
                player.sendMessage("Unknown spell. Please specify a spell: repulsius, aerolithe, or partisan."); //this one too
                return false;
            }

            // Obtain the corresponding spell scroll
            ItemStack scrollItem = getScrollBySpell(spell);

            // Check if the spell scroll was retrieved correctly
            if (scrollItem == null) {
                player.sendMessage("Error retrieving the spell scroll.");
                return false;
            }

            // Add the item to the player's inventory
            player.getInventory().addItem(scrollItem);
            player.sendMessage("You have received a scroll for the spell: " + spell.getName());
            return true;
        }

        return false;
    }

    // Obtain the corresponding spell scroll
    private ItemStack getScrollBySpell(Spells spell) {
        switch (spell) {
            case REPULSIUS:
                return Repulsius.getScroll();
            case AEROLITHE:
                return Aerolithe.getScroll();
            case PARTISAN:
                return Partisan.getScroll();
            default:
                return null;
        }
    }
}
