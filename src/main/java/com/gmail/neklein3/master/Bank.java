package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Bank implements Listener {

    Main main;
    Bank(Main m) {
        this.main = m;
    }

    //bank sign creation.
    @EventHandler
    public void onSignWrite(SignChangeEvent e) {
        Block block = e.getBlock();
        if(e.getLine(0) != null && e.getLine(0).equals("[bank]")) {
            // only allows bankers to make the sign
            if (main.getBankerUUIDString() != null) {
                if (main.getBankerUUIDString().equals(e.getPlayer().getUniqueId().toString())) {
                    //logs the sign location in atmLocations in the config
                    ExchangeTerminal et = new ExchangeTerminal(block.getLocation());
                    main.addExchangeTerminalToList(et);

                    e.setLine(0, "* Exp Economy *");
                    e.setLine(1, "Exchange");
                    e.setLine(2, "Terminal");
                    e.setLine(3, "");
                }
            }
        }
    }

    @EventHandler
    public void onBreakSign(BlockBreakEvent e) {
        Block block = e.getBlock();
        main.removeIfExchangeTerminal(block);
    }

    @EventHandler
    public void onRightClickSign(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        Action action = e.getAction();

        if (block != null) {
            if (main.isExchangeTerminal(block)) {
                if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    applyAtmUI(e.getPlayer());
                }
            }
        }
    }

    //Atm UI
    public void applyAtmUI(Player player) {

        //Beginning
        Inventory mainAtmGui = Bukkit.createInventory(null, 27, main.color("&8Bank Exchange Terminal"));

        //ItemStacks
        ItemStack convertToXpItem = new ItemStack(Material.LIME_CONCRETE, 1);
        ItemMeta convertToXpItemMeta = convertToXpItem.getItemMeta();
        assert convertToXpItemMeta != null;
        convertToXpItemMeta.setDisplayName(main.color("&6Convert to Experience"));
        convertToXpItem.setItemMeta(convertToXpItemMeta);

        ItemStack convertFromXpItem = new ItemStack(Material.MAGENTA_CONCRETE, 1);
        ItemMeta convertFromXpItemMeta = convertFromXpItem.getItemMeta();
        assert convertFromXpItemMeta != null;
        convertFromXpItemMeta.setDisplayName(main.color("&5Convert to Physical Currency"));
        convertFromXpItem.setItemMeta(convertFromXpItemMeta);

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

        ItemStack backgroundConvertToXpItem = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemMeta backgroundConvertToXpItemMeta = backgroundConvertToXpItem.getItemMeta();
        assert backgroundConvertToXpItemMeta != null;
        backgroundConvertToXpItemMeta.setDisplayName(main.color("&6Withdraw"));
        backgroundConvertToXpItem.setItemMeta(backgroundConvertToXpItemMeta);

        ItemStack backgroundConvertFromXpItem = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE, 1);
        ItemMeta backgroundConvertFromXpItemMeta = backgroundConvertFromXpItem.getItemMeta();
        assert backgroundConvertFromXpItemMeta != null;
        backgroundConvertFromXpItemMeta.setDisplayName(main.color("&5Deposit"));
        backgroundConvertFromXpItem.setItemMeta(backgroundConvertFromXpItemMeta);

        //Item Settings
        /*0*/ mainAtmGui.setItem(0, backgroundDarkItem);
        /*1*/ mainAtmGui.setItem(1, backgroundLightItem);
        /*2*/ mainAtmGui.setItem(2, backgroundDarkItem);
        /*3*/ mainAtmGui.setItem(3, backgroundLightItem);
        /*4*/ mainAtmGui.setItem(4, backgroundDarkItem);
        /*5*/ mainAtmGui.setItem(5, backgroundLightItem);
        /*6*/ mainAtmGui.setItem(6, backgroundDarkItem);
        /*7*/ mainAtmGui.setItem(7, backgroundLightItem);
        /*8*/ mainAtmGui.setItem(8, backgroundDarkItem);
        /*9*/ mainAtmGui.setItem(9, backgroundLightItem);

        // these will always be there
        /*10*/ mainAtmGui.setItem(10, backgroundConvertToXpItem);
        /*11*/ mainAtmGui.setItem(11, convertToXpItem);
        /*12*/ mainAtmGui.setItem(12, backgroundConvertToXpItem);


        /*13*/ mainAtmGui.setItem(13, backgroundLightItem);

        if (main.getExchangeTerminalTwoWayMode() != null) {
            if (main.getExchangeTerminalTwoWayMode()) {
                // if two-way mode is on
                mainAtmGui.setItem(14, backgroundConvertFromXpItem);
                mainAtmGui.setItem(15, convertFromXpItem);
                mainAtmGui.setItem(16, backgroundConvertFromXpItem);
            } else {
                mainAtmGui.setItem(14, backgroundDarkItem);
                mainAtmGui.setItem(15, backgroundLightItem);
                mainAtmGui.setItem(16, backgroundDarkItem);
            }
        } else {
            mainAtmGui.setItem(14, backgroundDarkItem);
            mainAtmGui.setItem(15, backgroundLightItem);
            mainAtmGui.setItem(16, backgroundDarkItem);
        }

        /*17*/ mainAtmGui.setItem(17, backgroundLightItem);
        /*18*/ mainAtmGui.setItem(18, backgroundDarkItem);
        /*19*/ mainAtmGui.setItem(19, backgroundLightItem);
        /*20*/ mainAtmGui.setItem(20, backgroundDarkItem);
        /*21*/ mainAtmGui.setItem(21, backgroundLightItem);
        /*22*/ mainAtmGui.setItem(22, backgroundDarkItem);
        /*23*/ mainAtmGui.setItem(23, backgroundLightItem);
        /*24*/ mainAtmGui.setItem(24, backgroundDarkItem);
        /*25*/ mainAtmGui.setItem(25, backgroundLightItem);
        /*26*/ mainAtmGui.setItem(26, backgroundDarkItem);

        player.openInventory(mainAtmGui);
    }
}