package org.dualwield;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;


public class DualWieldPlugin extends JavaPlugin implements Listener {

    private static final double REACH_DISTANCE = 5.0;

    @Override
    public void onEnable() {
        getServer ().getPluginManager ().registerEvents (this, this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (isWeapon(offHand)) {
            // Get the entity the player is looking at within reach distance
            Entity target = getEntityInSight(player);

            if (target instanceof LivingEntity) {
                // Calculate damage based on sword type and enchantments
                double damage = getWeaponDamage (offHand) + getEnchantmentDamage(offHand);

                // Deal damage to the entity
                ((LivingEntity) target).damage(damage, player);

                // Spawn sweep attack particles
                player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, player.getLocation().add(0, 1, 0).add(player.getLocation().getDirection()), 1);

                // Play sword swing sound
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);

                // Damage the item in the off hand
                if (offHand.getItemMeta() instanceof Damageable) {
                    Damageable damageable = (Damageable) offHand.getItemMeta();
                    damageable.setDamage(damageable.getDamage() + 1);
                    offHand.setItemMeta((ItemMeta) damageable);
                }
            }

            // Swing off-hand
            player.swingOffHand();
        }
    }


    private boolean isWeapon(ItemStack item) {
        if (item == null) {
            return false;
        }
        Material type = item.getType();
        return type == Material.DIAMOND_SWORD || type == Material.IRON_SWORD || type == Material.STONE_SWORD || type == Material.WOODEN_SWORD || type == Material.NETHERITE_SWORD || type == Material.GOLDEN_SWORD ||
                type == Material.DIAMOND_AXE || type == Material.IRON_AXE || type == Material.STONE_AXE || type == Material.WOODEN_AXE || type == Material.NETHERITE_AXE || type == Material.GOLDEN_AXE ||
                type == Material.DIAMOND_HOE || type == Material.IRON_HOE || type == Material.STONE_HOE || type == Material.WOODEN_HOE || type == Material.NETHERITE_HOE || type == Material.GOLDEN_HOE ||
                type == Material.DIAMOND_SHOVEL || type == Material.IRON_SHOVEL || type == Material.STONE_SHOVEL || type == Material.WOODEN_SHOVEL || type == Material.NETHERITE_SHOVEL || type == Material.GOLDEN_SHOVEL ||
                type == Material.DIAMOND_PICKAXE || type == Material.IRON_PICKAXE || type == Material.STONE_PICKAXE || type == Material.WOODEN_PICKAXE || type == Material.NETHERITE_PICKAXE || type == Material.GOLDEN_PICKAXE;
    }

    private double getWeaponDamage(ItemStack item) {
        Material type = item.getType();
        switch (type) {
            case WOODEN_SWORD:
            case WOODEN_AXE:
                return 4.0;
            case WOODEN_HOE:
            case WOODEN_SHOVEL:
            case WOODEN_PICKAXE:
                return 2.0;
            case STONE_SWORD:
            case STONE_AXE:
                return 5.0;
            case STONE_HOE:
            case STONE_SHOVEL:
            case STONE_PICKAXE:
                return 3.0;
            case IRON_SWORD:
            case IRON_AXE:
                return 6.0;
            case IRON_HOE:
            case IRON_SHOVEL:
            case IRON_PICKAXE:
                return 4.0;
            case DIAMOND_SWORD:
            case DIAMOND_AXE:
                return 7.0;
            case DIAMOND_HOE:
            case DIAMOND_SHOVEL:
            case DIAMOND_PICKAXE:
                return 5.0;
            case NETHERITE_SWORD:
            case NETHERITE_AXE:
                return 8.0;
            case NETHERITE_HOE:
            case NETHERITE_SHOVEL:
            case NETHERITE_PICKAXE:
                return 6.0;
            case GOLDEN_SWORD:
            case GOLDEN_AXE:
                return 4.0;
            case GOLDEN_HOE:
            case GOLDEN_SHOVEL:
            case GOLDEN_PICKAXE:
                return 2.0;
            default:
                return 1.0;
        }
    }


    private double getEnchantmentDamage(ItemStack item) {
        if (item.containsEnchantment (Enchantment.DAMAGE_ALL)) {
            int level = item.getEnchantmentLevel (Enchantment.DAMAGE_ALL);
            return 1.0 * level;
        }
        return 0.0;
    }

    private Entity getEntityInSight(Player player) {
        for (Entity entity : player.getNearbyEntities (REACH_DISTANCE, REACH_DISTANCE, REACH_DISTANCE)) {
            Vector toEntity = entity.getLocation ().toVector ().subtract (player.getLocation ().toVector ());
            if (toEntity.normalize ().dot (player.getLocation ().getDirection ()) > 0.9) {
                return entity;
            }
        }
        return null;
    }
}