// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.listCrafting.components;


import org.terasology.crafting.components.Recipe;
import org.terasology.crafting.components.RecipeComponent;
import org.terasology.engine.entitySystem.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Component that is used to enter the ListRecipe's
 */
public class ListRecipesComponent implements Component, RecipeComponent {

    public List<String> categories = Collections.singletonList("InHand");
    public Map<String, ListRecipeContainer> recipes = new TreeMap<>();

    public Recipe[] getRecipes() {
        Recipe[] builtRecipes = new Recipe[recipes.size()];
        int i = 0;
        for (Map.Entry<String, ListRecipeContainer> entry : recipes.entrySet()) {
            ListRecipe recipe = new ListRecipe(entry.getValue(), entry.getKey());
            builtRecipes[i] = recipe;
            i++;
        }
        return builtRecipes;
    }

    public String[] getCategories() {
        return categories.toArray(new String[0]);
    }
}
