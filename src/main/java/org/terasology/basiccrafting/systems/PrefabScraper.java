// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.basiccrafting.systems;

import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;

@RegisterSystem
public class PrefabScraper extends BaseComponentSystem {
    @In
    private RecipeStore recipeStore;
    @In
    private IconManager iconManager;


    public void postBegin() {
        recipeStore.scrapeIngredientNames();
        iconManager.scrapeIcons();
    }
}
