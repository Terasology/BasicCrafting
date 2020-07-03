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

import org.terasology.crafting.events.OnRecipeCrafted;
import org.terasology.crafting.listCrafting.components.ListRecipe;
import org.terasology.crafting.listCrafting.systems.ListCraftingManager;
import org.terasology.crafting.systems.IconManager;
import org.terasology.crafting.systems.RecipeStore;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.CoreWidget;
import org.terasology.rendering.nui.layouts.ScrollableArea;
import org.terasology.rendering.nui.widgets.UIButton;

import javax.swing.*;

public class UIWorkstationView extends CoreWidget {

    private static final int ICON_SIZE = 64;
    private static final float UPDATE_ICON_STEP = 1.2f;
    private float counter;

    private ScrollableArea scrollableArea = new ScrollableArea();
    private UIRecipeView recipeView = new UIRecipeView();
    private UIRecipeList recipeList = new UIRecipeList();
    private UIButton craftButton = new UIButton();

    private EntityRef craftingEntity;

    @Override
    public void onDraw(Canvas canvas) {
        int xOffset = (canvas.size().x - 5 * ICON_SIZE) / 2;
        canvas.drawWidget(scrollableArea, Rect2i.createFromMinAndSize(xOffset, 0, 5 * ICON_SIZE + 28, 3 * ICON_SIZE));
        canvas.drawWidget(recipeView, Rect2i.createFromMinAndSize(0, 3 * ICON_SIZE + ICON_SIZE / 4, canvas.size().x, ICON_SIZE));
        canvas.drawWidget(craftButton, Rect2i.createFromMinAndSize(xOffset + ICON_SIZE, 4 * ICON_SIZE + ICON_SIZE / 2, 3 * ICON_SIZE, ICON_SIZE / 2));
    }

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        Vector2i viewSize = recipeView.getPreferredContentSize(canvas, sizeHint);
        return new Vector2i(Math.max(5 * ICON_SIZE, viewSize.x), (int) (5.5 * ICON_SIZE));
    }

    public void initialise(ListCraftingManager newCraftingManager, RecipeStore newRecipeStore, IconManager newIconManager) {

        recipeList.setManagers(newRecipeStore, newIconManager);
        recipeList.subscribeRecipeView(widget -> recipeView.updateRecipe(((UIRecipeList) widget).getSelectedRecipe()));

        recipeView.setIconManager(newIconManager);

        craftButton.subscribe(widget -> {
            ListRecipe selectedRecipe = recipeList.getSelectedRecipe();
            if (selectedRecipe != null) {
                craftingEntity.send(new OnRecipeCrafted(selectedRecipe, true, newCraftingManager));
            }
        });
        craftButton.setText("Craft");

        scrollableArea.setContent(recipeList);
        scrollableArea.setPreferredSize(5 * ICON_SIZE, 3 * ICON_SIZE);

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (counter >= UPDATE_ICON_STEP) {
            recipeView.stepItemIcons();
            counter = 0f;
        } else {
            counter += delta;
        }
    }

    public void setupView(EntityRef newCraftingEntity, String newWorkstationID) {
        craftingEntity = newCraftingEntity;
        recipeList.setWorkstationID(newWorkstationID);
    }
}
