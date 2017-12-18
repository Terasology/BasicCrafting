/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.crafting.systems;


import org.terasology.crafting.components.Recipe;

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
     * @param categories  The categories to search through
     * @param filterClass The class to filter by
     * @param <T>         The type of the Recipe being used
     * @return All recipes found
     */
    <T extends Recipe> List<T> getRecipes(String[] categories, Class<T> filterClass);

    /**
     * Get all recipes in a given category that are of a given type
     *
     * @param category    The category to search through
     * @param filterClass The class to filter by
     * @param <T>         The type of the Recipe being used
     * @return All recipes found
     */
    <T extends Recipe> List<T> getRecipes(String category, Class<T> filterClass);

    /**
     * Adds a recipe to the store.
     *
     * @param recipe     The recipe to add
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
