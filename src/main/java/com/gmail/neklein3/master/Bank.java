package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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

            if (main.getUniversalBankCreation() != null) {
                if (main.getUniversalBankCreation()) {
                    //logs the sign location in atmLocations in the config
                    ExchangeTerminal et = new ExchangeTerminal(block.getLocation());
                    main.addExchangeTerminalToList(et);

                    e.setLine(0, "* Exp Economy *");
                    e.setLine(1, "Exchange");
                    e.setLine(2, "Terminal");
                    e.setLine(3, et.getStatusString());
                    return;
                }
            }
            if (main.getBankerUUIDString() != null) {
                if (main.getBankerUUIDString().equals(e.getPlayer().getUniqueId().toString())) {
                    //logs the sign location in atmLocations in the config
                    ExchangeTerminal et = new ExchangeTerminal(block.getLocation());
                    main.addExchangeTerminalToList(et);

                    e.setLine(0, "* Exp Economy *");
                    e.setLine(1, "Exchange");
                    e.setLine(2, "Terminal");
                    e.setLine(3, et.getStatusString());
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
                ExchangeTerminal et = main.getExchangeTerminal(block.getLocation());
                if (et.getEnabled()) {
                    if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                        applyAtmUI(e.getPlayer());
                    }
                } else {
                    if (main.getBankerUUIDString() != null) {
                        if (main.getBankerUUIDString().equals(e.getPlayer().getUniqueId().toString())) {
                            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 5, 1);
                            et.setEnabled(true);
                            main.changeSignText(et.getBlock(), 3, et.getStatusString());
                        }
                    }
                }
            }
        }
    }

    //Atm UI
    public void applyAtmUI(Player player) {

        //Beginning
        Inventory mainBankGui = Bukkit.createInventory(null, 27, main.color("&8Bank Exchange Terminal"));

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
        backgroundConvertToXpItemMeta.setDisplayName(main.color(""));
        backgroundConvertToXpItem.setItemMeta(backgroundConvertToXpItemMeta);

        ItemStack backgroundConvertFromXpItem = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE, 1);
        ItemMeta backgroundConvertFromXpItemMeta = backgroundConvertFromXpItem.getItemMeta();
        assert backgroundConvertFromXpItemMeta != null;
        backgroundConvertFromXpItemMeta.setDisplayName(main.color(""));
        backgroundConvertFromXpItem.setItemMeta(backgroundConvertFromXpItemMeta);

        //admin only
        ItemStack disableButtonItem = new ItemStack(Material.RED_CONCRETE, 1);
        ItemMeta disableButtonItemMeta = disableButtonItem.getItemMeta();
        assert disableButtonItemMeta != null;
        disableButtonItemMeta.setDisplayName(main.color("&4Disable Atm."));
        disableButtonItemMeta.setLore(Arrays.asList(main.color("&cThis button will disable the Atm until you right click the Atm again.")));
        disableButtonItem.setItemMeta(disableButtonItemMeta);

        //Item Settings
        /*0*/ mainBankGui.setItem(0, backgroundDarkItem);
        /*1*/ mainBankGui.setItem(1, backgroundLightItem);
        /*2*/ mainBankGui.setItem(2, backgroundDarkItem);
        /*3*/ mainBankGui.setItem(3, backgroundLightItem);
        /*4*/ mainBankGui.setItem(4, backgroundDarkItem);
        /*5*/ mainBankGui.setItem(5, backgroundLightItem);
        /*6*/ mainBankGui.setItem(6, backgroundDarkItem);
        /*7*/ mainBankGui.setItem(7, backgroundLightItem);
        /*8*/ mainBankGui.setItem(8, backgroundDarkItem);
        /*9*/ mainBankGui.setItem(9, backgroundLightItem);

        // these will always be there
        /*10*/ mainBankGui.setItem(10, backgroundConvertToXpItem);
        /*11*/ mainBankGui.setItem(11, convertToXpItem);
        /*12*/ mainBankGui.setItem(12, backgroundConvertToXpItem);


        /*13*/ mainBankGui.setItem(13, backgroundLightItem);

        if (main.getExchangeTerminalTwoWayMode() != null) {
            if (main.getExchangeTerminalTwoWayMode()) {
                // if two-way mode is on
                mainBankGui.setItem(14, backgroundConvertFromXpItem);
                mainBankGui.setItem(15, convertFromXpItem);
                mainBankGui.setItem(16, backgroundConvertFromXpItem);
            } else {
                mainBankGui.setItem(14, backgroundDarkItem);
                mainBankGui.setItem(15, backgroundLightItem);
                mainBankGui.setItem(16, backgroundDarkItem);
            }
        } else {
            mainBankGui.setItem(14, backgroundDarkItem);
            mainBankGui.setItem(15, backgroundLightItem);
            mainBankGui.setItem(16, backgroundDarkItem);
        }

        /*17*/ mainBankGui.setItem(17, backgroundLightItem);
        /*18*/ mainBankGui.setItem(18, backgroundDarkItem);
        /*19*/ mainBankGui.setItem(19, backgroundLightItem);
        /*20*/ mainBankGui.setItem(20, backgroundDarkItem);
        /*21*/ mainBankGui.setItem(21, backgroundLightItem);
        /*22*/ mainBankGui.setItem(22, backgroundDarkItem);
        /*23*/ mainBankGui.setItem(23, backgroundLightItem);
        /*24*/ mainBankGui.setItem(24, backgroundDarkItem);
        /*25*/ mainBankGui.setItem(25, backgroundLightItem);
        /*26*/ // for the bottom right tile (26), check if player is Banker, if so set 26 to disable button
        if (main.isBanker(player) != null) {
            if (main.isBanker(player)) {
                mainBankGui.setItem(26, disableButtonItem);
            } else {
                mainBankGui.setItem(26, backgroundDarkItem);
            }
        } else {
            mainBankGui.setItem(26, backgroundDarkItem);
        }

        player.openInventory(mainBankGui);
    }
}