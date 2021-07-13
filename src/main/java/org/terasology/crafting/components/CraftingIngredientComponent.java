// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.components;

import com.google.common.collect.Lists;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Provides another name that an be used to specify this item in a recipe
 * This name can be used for multiple different items.
 */
public class CraftingIngredientComponent implements Component<CraftingIngredientComponent> {
    public List<String> ingredientIds = new LinkedList<>();

    @Override
    public void copy(CraftingIngredientComponent other) {
        this.ingredientIds.clear();
        this.ingredientIds.addAll(other.ingredientIds);
    }
}
