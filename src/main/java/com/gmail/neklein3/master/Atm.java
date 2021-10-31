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

public class Atm implements Listener {

    Main main;
    Atm(Main main) { this.main = main; }

    //atm creation.
    @EventHandler
    public void onSignWrite(SignChangeEvent e) {
        Block block = e.getBlock();
        if(e.getLine(0) != null && e.getLine(0).equals("[atm]")) {
            //logs the sign location in atmLocations in the config
            TellerMachine atm = new TellerMachine(block.getLocation());
            atm.setEnabled(true);
            main.addTellerMachineToList(atm);

            e.setLine(0, "* Exp Economy *");
            e.setLine(1, "BANK ATM");
            e.setLine(2, "");
            e.setLine(3, "Status:"  + atm.getStatus());

        }
    }

    //Atm UI
    public void applyAtmUI(Player player) {

        //Beginning
        Inventory mainAtmGui = Bukkit.createInventory(null, 27, main.color("&8Bank ATM"));

        //ItemStacks
        ItemStack withdrawXpItem = new ItemStack(Material.YELLOW_CONCRETE, 1);
        ItemMeta withdrawXpItemMeta = withdrawXpItem.getItemMeta();
        assert withdrawXpItemMeta != null;
        withdrawXpItemMeta.setDisplayName(main.color("&6Withdraw cash."));
        withdrawXpItemMeta.setLore(Arrays.asList(main.color("&eClick this to open the withdraw menu.")));
        withdrawXpItem.setItemMeta(withdrawXpItemMeta);

        ItemStack depositXpItem = new ItemStack(Material.PURPLE_CONCRETE, 1);
        ItemMeta depositXpItemMeta = depositXpItem.getItemMeta();
        assert depositXpItemMeta != null;
        depositXpItemMeta.setDisplayName(main.color("&5Deposit cash."));
        depositXpItemMeta.setLore(Arrays.asList(main.color("&dClick this to open the deposit menu.")));
        depositXpItem.setItemMeta(depositXpItemMeta);

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

        //admin only
        ItemStack disableButtonItem = new ItemStack(Material.RED_CONCRETE, 1);
        ItemMeta disableButtonItemMeta = disableButtonItem.getItemMeta();
        assert disableButtonItemMeta != null;
        disableButtonItemMeta.setDisplayName(main.color("&4Disable Atm."));
        disableButtonItemMeta.setLore(Arrays.asList(main.color("&cThis button disables the Atm until it is enabled again.")));
        disableButtonItem.setItemMeta(disableButtonItemMeta);

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
        /*10*/ mainAtmGui.setItem(10, backgroundWithdrawItem);
        /*11*/ mainAtmGui.setItem(11, withdrawXpItem);
        /*12*/ mainAtmGui.setItem(12, backgroundWithdrawItem);
        /*13*/ mainAtmGui.setItem(13, backgroundLightItem);
        /*14*/ mainAtmGui.setItem(14, backgroundDepositItem);
        /*15*/ mainAtmGui.setItem(15, depositXpItem);
        /*16*/ mainAtmGui.setItem(16, backgroundDepositItem);
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

        //Final
        
        player.openInventory(mainAtmGui);
        

    }

    @EventHandler
    public void onBreakSign(BlockBreakEvent e) {
        Block block = e.getBlock();

        // remove the location of the sign from the atmLocation list if that location was already in the list
        main.removeIfTellerMachine(block);
    }

    @EventHandler
    public void onRightClickSign(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        Action action = e.getAction();

        // executes twice i think cause offhand
        // i have a code snippet to cancel that that ill add later

        if (action.equals(Action.RIGHT_CLICK_BLOCK)) { // if the atm is on, it'll send a message in the console
            if (main.isTellerMachine(block)) {
                if (block != null) {
                    for (TellerMachine tm : main.TellerMachineList) {
                        if (tm.getLocation().equals(block.getLocation())) {
                            if (tm.getEnabled()) {
                                e.getPlayer().sendMessage("teller clicked!");
                                main.getLogger().info("right clicked teller machine");
                                applyAtmUI(e.getPlayer());


                            }
                        }
                    }
                }
            }
        } else if (action.equals(Action.LEFT_CLICK_BLOCK)) {  // this toggles the atms on and off for now
            if (main.isTellerMachine(block)) {
                if (block != null) {
                    for (TellerMachine tm : main.TellerMachineList) {
                        if (tm.getLocation().equals(block.getLocation())) {
                            if(tm.getEnabled()) {
                                e.getPlayer().sendMessage("teller disabled.");
                            } else {
                                e.getPlayer().sendMessage("teller enabled.");
                            }
                            tm.setEnabled(!tm.getEnabled());
                        }
                    }
                }
            }
        }
    }

}
