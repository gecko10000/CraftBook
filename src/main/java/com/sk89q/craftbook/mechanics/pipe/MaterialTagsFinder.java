package com.sk89q.craftbook.mechanics.pipe;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.MaterialTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MaterialTagsFinder {

    private final Map<String, MaterialSetTag> cachedTags = new HashMap<>();

    @Nullable
    private MaterialSetTag doReflectiveLookup(@NotNull String key) {
        try {
            Field field = MaterialTags.class.getDeclaredField(key);
            return (MaterialSetTag) field.get(null);
        } catch (NoSuchFieldException ex) {
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public MaterialSetTag getTag(@NotNull String name) {
        String key = name.toUpperCase();
        if (cachedTags.containsKey(key)) return cachedTags.get(key);
        MaterialSetTag result = doReflectiveLookup(key);
        cachedTags.put(key, result);
        return result;
    }

}
