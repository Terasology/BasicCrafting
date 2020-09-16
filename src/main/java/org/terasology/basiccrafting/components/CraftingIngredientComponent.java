// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.basiccrafting.components;

import org.terasology.engine.entitySystem.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Provides another name that an be used to specify this item in a recipe This name can be used for multiple different
 * items.
 */
public class CraftingIngredientComponent implements Component {
    public List<String> ingredientIds = new LinkedList<>();
}
