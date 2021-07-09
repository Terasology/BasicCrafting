// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.listCrafting.components;


import org.terasology.gestalt.entitysystem.component.Component;

public class CraftingWorkstationComponent implements Component<CraftingWorkstationComponent> {
    public String recipeCategory;

    @Override
    public void copy(CraftingWorkstationComponent other) {
        this.recipeCategory = other.recipeCategory;
    }
}
