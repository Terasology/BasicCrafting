// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.basiccrafting.listCrafting.systems;

import org.terasology.basiccrafting.listCrafting.components.ListRecipe;
import org.terasology.engine.entitySystem.entity.EntityRef;

public interface ListCraftingManager {
    EntityRef[] craftRecipe(EntityRef craftingEntity, ListRecipe recipe, boolean giveToCrafter);
}
