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

    public ResourceCollectionJob(Material m, int amount) {
        material = m;
        this.amount = amount;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Material", material);
        result.put("Amount", amount);
        return result;
    }

    public static ResourceCollectionJob deserialize(Map<String, Object> map) {
        Material material = (Material) map.get("Material");
        int amount = (int) map.get("amount");
        return new ResourceCollectionJob(material, amount);
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemStack getJobIcon() {
        ItemStack icon = new ItemStack(material, 1);
        ItemMeta meta = icon.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(String.valueOf(amount));
        meta.setLore(lore);
        icon.setItemMeta(meta);
        return icon;
    }

}
