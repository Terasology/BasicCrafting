// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.crafting.listCrafting.UI;

import org.joml.Vector2i;
import org.terasology.crafting.listCrafting.components.ListRecipe;
import org.terasology.crafting.listCrafting.systems.ListCraftingManager;
import org.terasology.crafting.systems.IconManager;
import org.terasology.crafting.systems.RecipeStore;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.joml.geom.Rectanglei;
import org.terasology.nui.Canvas;
import org.terasology.nui.CoreWidget;
import org.terasology.nui.layouts.ScrollableArea;
import org.terasology.nui.widgets.UIButton;

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
        canvas.drawWidget(scrollableArea, new Rectanglei(xOffset, 0).setSize(5 * ICON_SIZE + 28, 3 * ICON_SIZE));
        canvas.drawWidget(recipeView, new Rectanglei(0, 3 * ICON_SIZE + ICON_SIZE / 4).setSize(canvas.size().x,
                ICON_SIZE));
        canvas.drawWidget(craftButton,
                new Rectanglei(xOffset + ICON_SIZE, 4 * ICON_SIZE + ICON_SIZE / 2).setSize(3 * ICON_SIZE,
                        ICON_SIZE / 2));
    }

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        Vector2i viewSize = recipeView.getPreferredContentSize(canvas, sizeHint);
        return new Vector2i(Math.max(5 * ICON_SIZE, viewSize.x), (int) (5.5 * ICON_SIZE));
    }

    public void initialise(ListCraftingManager newCraftingManager, RecipeStore newRecipeStore,
                           IconManager newIconManager) {

        recipeList.setManagers(newRecipeStore, newIconManager);
        recipeList.subscribeRecipeView(widget -> recipeView.updateRecipe(((UIRecipeList) widget).getSelectedRecipe()));

        recipeView.setIconManager(newIconManager);

        craftButton.subscribe(widget -> {
            ListRecipe selectedRecipe = recipeList.getSelectedRecipe();
            if (selectedRecipe != null) {
                newCraftingManager.craftRecipe(craftingEntity, selectedRecipe, true);
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
