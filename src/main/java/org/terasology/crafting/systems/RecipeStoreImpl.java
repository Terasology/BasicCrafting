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
import org.terasology.crafting.listCrafting.components.ListRecipe;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.registry.Share;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Share(RecipeStore.class)
@RegisterSystem
public class RecipeStoreImpl extends BaseComponentSystem implements RecipeStore {

    private List<Recipe> recipeList = new ArrayList<>();
    private Map<String, Set<Integer>> categoryLookup = new HashMap<>();

    public boolean hasCategory(String category) {
        return categoryLookup.containsKey(category.toLowerCase());
    }

    /**
     * Get a list of all recipes in the given category
     *
     * @param category The category to look for
     * @return All recipes in the category
     */
    public Recipe[] getRecipes(String category) {
        return getRecipes(new String[]{category});
    }

    public Recipe[] getRecipes(String[] categories) {
        return getRecipesFromIndices(getIndicesInCategories(categories));
    }

    public <T extends Recipe> List<T> getRecipes(String category, Class<T> filterClass) {
        return getRecipes(new String[]{category}, filterClass);
    }

    public <T extends Recipe> List<T> getRecipes(String[] categories, Class<T> filterClass) {
        Set<Integer> indices = getIndicesInCategories(categories);
        return getRecipesFromIndices(indices, filterClass);
    }

    private <T extends Recipe> List<T> getRecipesFromIndices(Set<Integer> indices, Class<T> filterClass) {
        if (indices.size() > 0) {
            List<T> recipes = new LinkedList<>();
            for (int index : indices) {
                Recipe recipe = recipeList.get(index);
                if (recipe.getClass().equals(filterClass)) {
                    recipes.add(filterClass.cast(recipeList.get(index)));
                }
            }
            return recipes;
        } else {
            return null;
        }
    }

    private Set<Integer> getIndicesInCategories(String[] categories) {
        Set<Integer> indices = new HashSet<>();
        for (String category : categories) {
            category = category.toLowerCase();
            if (categoryLookup.containsKey(category)) {
                indices.addAll(categoryLookup.get(category));
            }
        }
        return indices;
    }


    /**
     * @param indices The indices to use
     * @return The recipes located at the indices.
     */
    private Recipe[] getRecipesFromIndices(Set<Integer> indices) {
        if (indices.size() > 0) {
            Recipe[] recipes = new Recipe[indices.size()];
            int i = 0;
            for (int index : indices) {
                recipes[i] = recipeList.get(index);
                i++;
            }
            return recipes;
        } else {
            return null;
        }
    }

    /**
     * Adds a recipe to the store under set categories.
     *
     * @param recipe     The recipe to add
     * @param categories The categories to add the recipe under
     */

    public void putRecipe(Recipe recipe, String[] categories) {
        int index = addRecipeToStore(recipe);
        for (String category : categories) {
            addLinkToRecipe(index, category.toLowerCase());
        }
    }

    /**
     * Adds a recipe to the list
     *
     * @param recipe The recipe to add
     * @return The index of the recipe
     */
    private int addRecipeToStore(Recipe recipe) {
        recipeList.add(recipe);
        return recipeList.size() - 1;
    }

    /**
     * Adds a link between a recipe ID and a category
     *
     * @param id       The recipe's ID
     * @param category The category to add the recipe to
     */
    private void addLinkToRecipe(int id, String category) {
        Set<Integer> idList = categoryLookup.containsKey(category) ? categoryLookup.get(category) : new HashSet<>();
        idList.add(id);
        categoryLookup.put(category, idList);
    }

}
