package com.sk89q.craftbook.mechanics.pipe;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.MaterialTags;
import com.sk89q.craftbook.bukkit.CraftBookPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MaterialSetTagFinder {

    private final Map<String, MaterialSetTag> cachedTags = new HashMap<>();

    @Nullable
    private MaterialSetTag doMaterialTagsLookup(@NotNull String key) {
        try {
            Field field = MaterialTags.class.getDeclaredField(key);
            return (MaterialSetTag) field.get(null);
        } catch (NoSuchFieldException ex) {
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private NamespacedKey randomKey() {
        return new NamespacedKey(CraftBookPlugin.inst(), UUID.randomUUID().toString().replaceAll("-", ""));
    }

    @Nullable
    private MaterialSetTag doTagLookup(@NotNull String key) {
        try {
            Field field = Tag.class.getDeclaredField(key);
            Tag<?> tag = (Tag<?>) field.get(null);
            System.out.println(tag);
            Material[] materials = tag.getValues().toArray(new Material[0]);
            return new MaterialSetTag(randomKey(), materials);
        } catch (NoSuchFieldException | ArrayStoreException ex) {
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public MaterialSetTag getTag(@NotNull String name) {
        String key = name.toUpperCase();
        if (cachedTags.containsKey(key)) return cachedTags.get(key);
        MaterialSetTag result = doMaterialTagsLookup(key);
        result = result == null ? doTagLookup(key) : result;
        cachedTags.put(key, result);
        return result;
    }

}
