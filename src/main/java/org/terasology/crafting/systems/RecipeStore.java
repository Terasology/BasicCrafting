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

/**
 * Provides a centralised store for all recipes.
 */
public interface RecipeStore {
    /**
     * Get a recipe given it's id
     * @param recipeID The id of the recipe
     * @return The recipe or null if it wasn't found
     */
    Recipe getRecipe(int recipeID);

    /**
     * Adds a recipe to the store.
     *
     * @param recipe The recipe to add
     * @param category The category to add it under
     * @return The id of the recipe.
     */
    int putRecipe(Recipe recipe, String category);
}
