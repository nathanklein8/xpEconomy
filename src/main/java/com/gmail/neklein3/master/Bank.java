package com.gmail.neklein3.master;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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

    public void placeNewTerminal(Event e, Block block) {
        if (e instanceof SignChangeEvent) {
            SignChangeEvent event = (SignChangeEvent) e;
            ExchangeTerminal et = new ExchangeTerminal(block.getLocation());
            et.setEnabled(true);
            main.addExchangeTerminalToList(et);

            event.setLine(0, "* Exp Economy *");
            event.setLine(1, "Exchange");
            event.setLine(2, "Terminal");
            event.setLine(3, et.getStatusString());
        }
    }

    //bank sign creation.
    @EventHandler
    public void onSignWrite(SignChangeEvent e) {
        Block block = e.getBlock();
        if(e.getLine(0) != null && e.getLine(0).equals("[bank]")) {
            // only allows bankers to make the sign

            if (main.getUniversalExchangeTerminalCreation() != null) {
                if (main.getUniversalExchangeTerminalCreation()) {
                    //logs the sign location in atmLocations in the config
                    placeNewTerminal(e, block);
                    return;
                }
            }
            if (main.getBankerUUIDString() != null) {
                if (main.getBankerUUIDString().equals(e.getPlayer().getUniqueId().toString())) {
                    //logs the sign location in atmLocations in the config
                    placeNewTerminal(e, block);
                }
            }
        }
    }

    @EventHandler
    public void onBreakSign(BlockBreakEvent e) {
        Block block = e.getBlock();

        if (main.isExchangeTerminal(block)) {
            if (main.getUniversalExchangeTerminalDestruction() != null) {
                if (main.getUniversalExchangeTerminalDestruction()) {
                    main.removeIfExchangeTerminal(block);
                    return;
                }
            }
            if (e.getPlayer().getUniqueId().toString().equals(main.getBankerUUIDString())) {
                // if the player is a banker
                e.getPlayer().sendMessage("Exchange Terminal Destroyed.");
                main.removeIfExchangeTerminal(block);
            } else {
                // if universal is off and player is not a banker
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "Only the Banker can destroy Exchange Terminals.");
            }
        }
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
        Inventory mainBankGui = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Bank Exchange Terminal");

        //ItemStacks
        ItemStack convertToXpItem = new ItemStack(Material.LIME_CONCRETE, 1);
        ItemMeta convertToXpItemMeta = convertToXpItem.getItemMeta();
        assert convertToXpItemMeta != null;
        convertToXpItemMeta.setDisplayName(ChatColor.GREEN + "Convert to Experience");
        convertToXpItem.setItemMeta(convertToXpItemMeta);

        ItemStack convertFromXpItem = new ItemStack(Material.MAGENTA_CONCRETE, 1);
        ItemMeta convertFromXpItemMeta = convertFromXpItem.getItemMeta();
        assert convertFromXpItemMeta != null;
        convertFromXpItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Convert to Physical Currency");
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
        backgroundConvertToXpItemMeta.setDisplayName(main.color("&0_"));
        backgroundConvertToXpItem.setItemMeta(backgroundConvertToXpItemMeta);

        ItemStack backgroundConvertFromXpItem = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE, 1);
        ItemMeta backgroundConvertFromXpItemMeta = backgroundConvertFromXpItem.getItemMeta();
        assert backgroundConvertFromXpItemMeta != null;
        backgroundConvertFromXpItemMeta.setDisplayName(main.color("&0_"));
        backgroundConvertFromXpItem.setItemMeta(backgroundConvertFromXpItemMeta);

        //admin only
        ItemStack disableButtonItem = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta disableButtonItemMeta = disableButtonItem.getItemMeta();
        assert disableButtonItemMeta != null;
        disableButtonItemMeta.setDisplayName(main.color("&4Disable Atm."));
        disableButtonItemMeta.setLore(Arrays.asList(main.color("&cThis button will disable the Atm until you right click the Atm again.")));
        disableButtonItem.setItemMeta(disableButtonItemMeta);

        ItemStack currentMoneyInBankItem = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta currentMoneyInBankItemMeta = currentMoneyInBankItem.getItemMeta();
        currentMoneyInBankItemMeta.setDisplayName(ChatColor.DARK_BLUE + "Money across all bank accounts");
        currentMoneyInBankItemMeta.setLore(Arrays.asList(ChatColor.BLUE + "" + main.getTotalBalance()));
        currentMoneyInBankItem.setItemMeta(currentMoneyInBankItemMeta);

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

        if (main.getExchangeTerminalTwoWayMode() != null) {
            if (main.getExchangeTerminalTwoWayMode()) {
                // two-way mode on
                /*10*/ mainBankGui.setItem(10, backgroundConvertToXpItem);
                /*11*/ mainBankGui.setItem(11, convertToXpItem);
                /*12*/ mainBankGui.setItem(12, backgroundConvertToXpItem);
                /*13*/ mainBankGui.setItem(13, backgroundLightItem);
                /*14*/ mainBankGui.setItem(14, backgroundConvertFromXpItem);
                /*15*/ mainBankGui.setItem(15, convertFromXpItem);
                /*16*/ mainBankGui.setItem(16, backgroundConvertFromXpItem);
            } else {
                // two-way mode off
                /*10*/ mainBankGui.setItem(10, backgroundDarkItem);
                /*11*/ mainBankGui.setItem(11, backgroundLightItem);
                /*12*/ mainBankGui.setItem(12, backgroundConvertToXpItem);
                /*13*/ mainBankGui.setItem(13, convertToXpItem);
                /*14*/ mainBankGui.setItem(14, backgroundConvertToXpItem);
                /*15*/ mainBankGui.setItem(15, backgroundLightItem);
                /*16*/ mainBankGui.setItem(16, backgroundDarkItem);
            }
        } else {
            // two-way mode not set
            /*10*/ mainBankGui.setItem(10, backgroundDarkItem);
            /*11*/ mainBankGui.setItem(11, backgroundLightItem);
            /*12*/ mainBankGui.setItem(12, backgroundConvertToXpItem);
            /*13*/ mainBankGui.setItem(13, convertToXpItem);
            /*14*/ mainBankGui.setItem(14, backgroundConvertToXpItem);
            /*15*/ mainBankGui.setItem(15, backgroundLightItem);
            /*16*/ mainBankGui.setItem(16, backgroundDarkItem);
        }

        /*17*/ mainBankGui.setItem(17, backgroundLightItem);

        /*18*/
        if (main.isBanker(player) != null) {
            if (main.isBanker(player)) {
                mainBankGui.setItem(18, currentMoneyInBankItem);
            } else {
                mainBankGui.setItem(18, backgroundDarkItem);
            }
        } else {
            mainBankGui.setItem(18, backgroundDarkItem);
        }

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