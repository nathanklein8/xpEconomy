package com.gmail.neklein3.master;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("ResourceCollectionJob")
public class ResourceCollectionJob  implements ConfigurationSerializable {

    Material material;
    int amount;
    boolean completed;

    public ResourceCollectionJob(Material m, int amount, boolean completed) {
        material = m;
        this.amount = amount;
        this.completed = completed;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Item", new ItemStack(material));
        result.put("Amount", amount);
        result.put("Completed", completed);
        return result;
    }

    public static ResourceCollectionJob deserialize(Map<String, Object> map) {
        ItemStack stack = (ItemStack) map.get("Item");
        Material material = stack.getType();
        int amount = (int) map.get("Amount");
        boolean completed = (boolean) map.get("Completed");

        return new ResourceCollectionJob(material, amount, completed);
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean b) {
        completed = b;
    }

    public ItemStack getJobIcon() {
        ItemStack icon = new ItemStack(material, 1);
        ItemMeta meta = icon.getItemMeta();
        if (meta != null) {
            List<String> lore = new ArrayList<>();
            lore.add("Amount: " + amount);
            meta.setLore(lore);
            icon.setItemMeta(meta);
        }
        return icon;
    }

    public List<ItemStack> getJobBlocks() {

        List<ItemStack> result = new ArrayList<>();

        int numStacks = amount / 64;
        int i;

        for (i = numStacks; i > 0; i--) {
            result.add(new ItemStack(material, 64));
        }

        return result;
    }



}
