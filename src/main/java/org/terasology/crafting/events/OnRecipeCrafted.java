// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.crafting.events;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.Event;

public class OnRecipeCrafted implements Event {
    private EntityRef[] ingredients;

    public OnRecipeCrafted(EntityRef[] ingredients) {
        this.ingredients=ingredients;
    }

    public EntityRef[] getIngredients() {
        return ingredients;
    }
}
