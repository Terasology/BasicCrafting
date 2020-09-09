// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.listCrafting.systems;

import org.terasology.crafting.listCrafting.components.ListRecipe;
import org.terasology.engine.entitySystem.entity.EntityRef;

public interface ListCraftingManager {
    EntityRef[] craftRecipe(EntityRef craftingEntity, ListRecipe recipe, boolean giveToCrafter);
}
