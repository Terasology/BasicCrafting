// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.components;

import com.google.common.collect.Lists;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Allows an ingredient name to be set on all prefabs that contain a listed component.
 * Components should be specified in the same format they are in the prefabs,
 * that is "CraftingIngredientComponent" should be "CraftingIngredient"
 */
public class ComponentToIngredientComponent implements Component<ComponentToIngredientComponent> {
    public Map<String, List<String>> componentMap = new TreeMap<>();

    @Override
    public void copy(ComponentToIngredientComponent other) {
        this.componentMap.clear();
        for (Map.Entry<String, List<String>> entry : other.componentMap.entrySet()) {
            this.componentMap.put(entry.getKey(), Lists.newArrayList(entry.getValue()));
        }
    }
}
