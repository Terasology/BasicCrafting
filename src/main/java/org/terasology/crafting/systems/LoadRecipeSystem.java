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

import org.terasology.assets.management.AssetManager;
import org.terasology.crafting.components.Recipe;
import org.terasology.crafting.components.RecipeComponent;
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;


/**
 * Loads recipes from prefabs.
 */
@RegisterSystem
public class LoadRecipeSystem extends BaseComponentSystem {
    @In
    private RecipeStore recipeStore;
    @In
    private AssetManager assetManager;

    /**
     * Called after the systems have all been setup
     * Searches all prefabs for any components that implement RecipeComponent.
     * If it finds a matching component then it attempts to add all recipes from it to the RecipeStore.
     */
    @Override
    public void postBegin() {
        for (Prefab prefab : assetManager.getLoadedAssets(Prefab.class)) {
            for (Component component : prefab.iterateComponents()) {
                if (RecipeComponent.class.isAssignableFrom(component.getClass())) {
                    loadRecipeComponent(component);
                }
            }
        }

    }

    /**
     * Adds the recipes from a RecipeComponent to the RecipeStore
     * @param genericComponent The component to add from.
     */
    private void loadRecipeComponent(Component genericComponent) {
        RecipeComponent component = (RecipeComponent) genericComponent;
        for (Recipe recipe : component.getRecipes()) {
            recipeStore.putRecipe(recipe, component.getCategories());
        }
    }
}
