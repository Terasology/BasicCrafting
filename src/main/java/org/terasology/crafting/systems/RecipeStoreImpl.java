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
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.registry.Share;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Share(RecipeStore.class)
@RegisterSystem
public class RecipeStoreImpl extends BaseComponentSystem implements RecipeStore {

    private List<Recipe> recipeList = new ArrayList<>();
    private Map<String, List<Integer>> categoryLookup = new HashMap<>();

    public Recipe getRecipe(int recipeID) {
        if (recipeID > -1 && recipeID < recipeList.size()) {
            return recipeList.get(recipeID);
        }
        return null;
    }

    @Override
    public boolean hasCategory(String category) {
        return categoryLookup.containsKey(category.toLowerCase());
    }

    public Recipe[] getRecipes(String category) {
        category = category.toLowerCase();
        if (categoryLookup.containsKey(category)) {
            List<Integer> indices = categoryLookup.get(category);
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

    public int putRecipe(Recipe recipe, String category) {
        int index = recipeList.size();
        recipeList.add(recipe);
        category = category.toLowerCase();
        List<Integer> idList;
        if (categoryLookup.containsKey(category)) {
            idList = categoryLookup.get(category);
        } else {
            idList = new LinkedList<>();
        }
        idList.add(index);
        categoryLookup.put(category, idList);
        return index;
    }

}
