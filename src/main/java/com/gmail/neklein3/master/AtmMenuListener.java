package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AtmMenuListener implements Listener {

    private Main main;

    public AtmMenuListener(Main main) {
        this.main = main;
    }

    public void applySubMenu(Player player, String option) {

        //Beginning
        Inventory subAtmGui = Bukkit.createInventory(null, 27, main.color("&7Bank ATM Sub-Menu"));

        //Constant ItemStacks
        //click items
        ItemStack withdrawXpItem = new ItemStack(Material.YELLOW_CONCRETE, 1);
        ItemMeta withdrawXpItemMeta = withdrawXpItem.getItemMeta();
        assert withdrawXpItemMeta != null;
        withdrawXpItemMeta.setDisplayName(main.color("&6Confirm"));
        withdrawXpItemMeta.setLore(Arrays.asList(main.color("&eClick this to confirm transaction.")));
        withdrawXpItem.setItemMeta(withdrawXpItemMeta);

        ItemStack depositXpItem = new ItemStack(Material.PURPLE_CONCRETE, 1);
        ItemMeta depositXpItemMeta = depositXpItem.getItemMeta();
        assert depositXpItemMeta != null;
        depositXpItemMeta.setDisplayName(main.color("&5Confirm."));
        depositXpItemMeta.setLore(Arrays.asList(main.color("&dClick this to confirm transaction.")));
        depositXpItem.setItemMeta(depositXpItemMeta);

        ItemStack backgroundWithdrawItem = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
        ItemMeta backgroundWithdrawItemMeta = backgroundWithdrawItem.getItemMeta();
        assert backgroundWithdrawItemMeta != null;
        backgroundWithdrawItemMeta.setDisplayName(main.color("&6Withdraw"));
        backgroundWithdrawItem.setItemMeta(backgroundWithdrawItemMeta);

        ItemStack backgroundDepositItem = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1);
        ItemMeta backgroundDepositItemMeta = backgroundDepositItem.getItemMeta();
        assert backgroundDepositItemMeta != null;
        backgroundDepositItemMeta.setDisplayName(main.color("&5Deposit"));
        backgroundDepositItem.setItemMeta(backgroundDepositItemMeta);

        //background
        ItemStack backgroundLightItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta backgroundLightItemMeta = backgroundLightItem.getItemMeta();
        assert backgroundLightItemMeta != null;
        backgroundLightItemMeta.setDisplayName(main.color("&0_"));
        backgroundLightItem.setItemMeta(backgroundLightItemMeta);

        ItemStack backgroundDarkItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta backgroundDarkItemMeta = backgroundDarkItem.getItemMeta();
        assert backgroundDarkItemMeta != null;
        backgroundDarkItemMeta.setDisplayName(main.color("&0_"));
        backgroundDarkItem.setItemMeta(backgroundDarkItemMeta);

        //Item Settings
        /*0*/ subAtmGui.setItem(0, backgroundDarkItem);
        /*1*/ subAtmGui.setItem(1, backgroundLightItem);
        /*2*/ subAtmGui.setItem(2, backgroundDarkItem);
        /*3*/ subAtmGui.setItem(3, backgroundLightItem);
        /*4*/ subAtmGui.setItem(4, backgroundDarkItem);
        /*5*/ subAtmGui.setItem(5, backgroundLightItem);
        /*6*/ subAtmGui.setItem(6, backgroundDarkItem);
        /*7*/ subAtmGui.setItem(7, backgroundLightItem);
        /*8*/ subAtmGui.setItem(8, backgroundDarkItem);
        /*9*/ subAtmGui.setItem(9, backgroundLightItem);
        /*10*/ subAtmGui.setItem(10, backgroundDarkItem);
        /*11*/
        /*12*/ subAtmGui.setItem(12, backgroundDarkItem);
        /*13*/
        /*14*/ subAtmGui.setItem(14, backgroundDarkItem);
        /*15*/
        /*16*/ subAtmGui.setItem(16, backgroundDarkItem);
        /*17*/ subAtmGui.setItem(17, backgroundLightItem);
        /*18*/ subAtmGui.setItem(18, backgroundDarkItem);
        /*19*/ subAtmGui.setItem(19, backgroundLightItem);
        /*20*/ subAtmGui.setItem(20, backgroundDarkItem);
        /*21*/ subAtmGui.setItem(21, backgroundLightItem);
        /*22*/ subAtmGui.setItem(22, backgroundDarkItem);
        /*23*/ subAtmGui.setItem(23, backgroundLightItem);
        /*24*/ subAtmGui.setItem(24, backgroundDarkItem);
        /*25*/ subAtmGui.setItem(25, backgroundLightItem);
        /*26*/ subAtmGui.setItem(26, backgroundDarkItem);
        switch (option) {
            case "withdrawal":
                player.sendMessage("withdrawal");
                /*11*/
                subAtmGui.setItem(11, backgroundWithdrawItem);
                /*13*/
                subAtmGui.setItem(13, withdrawXpItem);
                /*15*/
                subAtmGui.setItem(15, backgroundWithdrawItem);
                break;
            case "deposit":
                /*11*/
                subAtmGui.setItem(11, backgroundDepositItem);
                /*13*/
                subAtmGui.setItem(13, depositXpItem);
                /*15*/
                subAtmGui.setItem(15, backgroundDepositItem);
                player.sendMessage("deposit");
                break;
            default:
                player.sendMessage("Something broke. Error-code: SubGuiItemSetting");
                return;
        }

        //Final

        player.openInventory(subAtmGui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.GRAY + "Bank ATM")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);

                switch (e.getCurrentItem().getType()) {
                    case YELLOW_CONCRETE:
                        player.sendMessage("withdrawal");
                        applySubMenu(player, "withdrawal");
                        break;
                    case PURPLE_CONCRETE:
                        player.sendMessage("deposit");
                        applySubMenu(player, "deposit");
                        break;
                    default:
                        return;

                }
            }
        } else if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.GRAY + "Bank ATM Sub-Menu")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);

                switch (e.getCurrentItem().getType()) {
                    case YELLOW_CONCRETE:
                        player.sendMessage("withdrawal of cash");
                        //withdraw cash
                        player.closeInventory();
                        break;
                    case PURPLE_CONCRETE:
                        player.sendMessage("deposit of cash");
                        //deposit cash
                        player.closeInventory();
                        break;
                    default:
                        return;

                }

            }
        }

    }
}

