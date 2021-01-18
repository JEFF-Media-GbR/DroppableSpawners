package de.jeff_media.DroppableSpawners;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerListener implements Listener {
    private final Main main;
    public SpawnerListener(Main main) {
        this.main=main;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if(!event.getBlock().getType().equals(Material.SPAWNER) || event.isCancelled())
        {
            return;
        }

        Player player = event.getPlayer();
        if(!isPickaxeWithSilkTouch(player.getInventory().getItemInMainHand())) return;

        event.setExpToDrop(0);

        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.SPAWNER, 1));
        main.getLogger().info(event.getPlayer().getName() + " broke a spawner succesfully.");

        CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
        EntityType spawnerType = spawner.getSpawnedType();
        if(!spawnerType.equals(EntityType.UNKNOWN) && !spawnerType.equals(EntityType.PIG))
        {
            ItemStack mobEggToSpawn = new ItemStack(Material.valueOf(spawnerType.toString() + "_SPAWN_EGG"), 1);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), mobEggToSpawn);
            main.getLogger().info(event.getPlayer().getName() + " got a spawn egg.");
        }

        event.getBlock().setType(Material.AIR);
        event.setCancelled(true);
    }

    private boolean isPickaxeWithSilkTouch(ItemStack itemInMainHand) {
        if(!itemInMainHand.getType().name().endsWith("_PICKAXE")) return false;
        ItemMeta meta = itemInMainHand.getItemMeta();
        return (meta.hasEnchant(Enchantment.SILK_TOUCH));
    }

    private boolean isPickaxe(Material material)
    {
        return (material.equals(Material.DIAMOND_PICKAXE) || material.equals(Material.IRON_PICKAXE)
                || material.equals(Material.STONE_PICKAXE) || material.equals(Material.WOODEN_PICKAXE));
    }
}
