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
package org.terasology.crafting.listCrafting.UI;

import org.terasology.crafting.listCrafting.systems.ListCraftingManager;
import org.terasology.crafting.systems.IconManager;
import org.terasology.crafting.systems.RecipeStore;
import org.terasology.engine.Time;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;

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
