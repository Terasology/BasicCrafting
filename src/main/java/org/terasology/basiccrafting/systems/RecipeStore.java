// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.basiccrafting.systems;


import org.terasology.basiccrafting.components.Recipe;

import java.util.List;

/**
 * Provides a centralised store for all recipes.
 */
public interface RecipeStore {

    /**
     * Get the list of recipes associated with the given category
     *
     * @param category The category to look for
     * @return All recipes associated with it.
     */
    Recipe[] getRecipes(String category);

    /**
     * Get all recipes in a given set of categories
     *
     * @param categories The categories to search through
     * @return All recipes found
     */
    Recipe[] getRecipes(String[] categories);

    /**
     * Get all recipes in the given categories that are of a given type
     *
     * @param categories The categories to search through
     * @param filterClass The class to filter by
     * @param <T> The type of the Recipe being used
     * @return All recipes found
     */
    <T extends Recipe> List<T> getRecipes(String[] categories, Class<T> filterClass);

    /**
     * Get all recipes in a given category that are of a given type
     *
     * @param category The category to search through
     * @param filterClass The class to filter by
     * @param <T> The type of the Recipe being used
     * @return All recipes found
     */
    <T extends Recipe> List<T> getRecipes(String category, Class<T> filterClass);

    /**
     * Adds a recipe to the store.
     *
     * @param recipe The recipe to add
     * @param categories The categories to add it under
     */
    void putRecipe(Recipe recipe, String[] categories);

    /**
     * Check if a given category exists
     *
     * @param category The category to check
     * @return True if it exists, false otherwise
     */
    boolean hasCategory(String category);

    /**
     * Get's all the Other names associated with that prefab name.
     *
     * @param name The Full name of the item to lookup.
     * @return All other possible names for that item or null if none found
     */
    String[] getIngredientNames(String name);

    /**
     * Collect all the ingredient names from the prefabs.
     */
    void scrapeIngredientNames();
}
