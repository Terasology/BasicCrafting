// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.crafting.events;

import org.terasology.crafting.listCrafting.components.ListRecipe;
import org.terasology.crafting.listCrafting.systems.ListCraftingManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

public class OnRecipeCrafted implements Event {
    private ListRecipe recipe;
    private boolean giveToCrafter;
    private ListCraftingManager craftingManager;

    public OnRecipeCrafted(ListRecipe recipe, boolean giveToCrafter, ListCraftingManager craftingManager) {
        this.craftingManager = craftingManager;
        this.recipe = recipe;
        this.giveToCrafter = giveToCrafter;
    }

    public boolean isGiveToCrafter() {
        return giveToCrafter;
    }

    public ListCraftingManager getCraftingManager() {
        return craftingManager;
    }

    public ListRecipe getRecipe() {
        return recipe;
    }
}
