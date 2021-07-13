// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.listCrafting.components;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.terasology.crafting.components.Recipe;
import org.terasology.crafting.components.RecipeComponent;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Component that is used to enter the ListRecipe's
 */
public class ListRecipesComponent implements Component<ListRecipesComponent>, RecipeComponent {

    public List<String> categories = Collections.singletonList("InHand");
    public SortedMap<String, ListRecipeContainer> recipes = new TreeMap<>();

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

    @Override
    public void copy(ListRecipesComponent other) {
        this.categories = Lists.newArrayList(other.categories);
        recipes.clear();
        recipes.putAll(other.recipes);
    }
}
