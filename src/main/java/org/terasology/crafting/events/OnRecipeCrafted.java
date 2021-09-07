// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.crafting.events;


import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.event.Event;

/**
 * This event is sent on an item when it is crafted in a workstation.
 * The event is sent on the produce with the ingredients as parameters.
 * Can be used to modify the produce with some values dictated by the ingredients.
 */
public class OnRecipeCrafted implements Event {
    private EntityRef[] ingredients;

    public OnRecipeCrafted(EntityRef[] ingredients) {
        this.ingredients=ingredients;
    }

    public EntityRef[] getIngredients() {
        return ingredients;
    }
}
