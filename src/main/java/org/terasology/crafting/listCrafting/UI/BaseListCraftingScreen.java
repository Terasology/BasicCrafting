// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.listCrafting.UI;

import org.terasology.crafting.listCrafting.systems.ListCraftingManager;
import org.terasology.crafting.systems.IconManager;
import org.terasology.crafting.systems.RecipeStore;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.CoreScreenLayer;


public class BaseListCraftingScreen extends CoreScreenLayer {

    @In
    private IconManager iconManager;
    @In
    private ListCraftingManager craftingManager;
    @In
    private RecipeStore recipeStore;
    @In
    private LocalPlayer localPlayer;
    private UIWorkstationView workstationView;

    @Override
    public void initialise() {
        workstationView = find("workstationView", UIWorkstationView.class);
        if (workstationView != null) {
            workstationView.initialise(craftingManager, recipeStore, iconManager);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    /**
     * Sets the id of the workstation to display
     *
     * @param newID The id to set it to
     */
    public void setWorkstationID(String newID) {
        if (workstationView != null) {
            workstationView.setupView(localPlayer.getCharacterEntity(), newID);
        }
    }

    @Override
    public boolean isModal() {
        return false;
    }
}
