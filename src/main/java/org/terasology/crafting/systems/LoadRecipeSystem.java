// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.systems;

import org.terasology.crafting.components.Recipe;
import org.terasology.crafting.components.RecipeComponent;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.gestalt.assets.management.AssetManager;
import org.terasology.gestalt.entitysystem.component.Component;


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
