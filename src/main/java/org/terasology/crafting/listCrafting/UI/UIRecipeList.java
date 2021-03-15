// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.crafting.listCrafting.UI;

import org.joml.Vector2i;
import org.terasology.crafting.listCrafting.components.ListRecipe;
import org.terasology.crafting.systems.IconManager;
import org.terasology.crafting.systems.RecipeStore;
import org.terasology.engine.rendering.assets.mesh.Mesh;
import org.terasology.engine.rendering.assets.texture.TextureRegion;
import org.terasology.module.inventory.ui.ItemIcon;
import org.terasology.engine.utilities.Assets;
import org.terasology.joml.geom.Rectanglei;
import org.terasology.nui.BaseInteractionListener;
import org.terasology.nui.Canvas;
import org.terasology.nui.CoreWidget;
import org.terasology.nui.events.NUIMouseClickEvent;
import org.terasology.nui.widgets.ActivateEventListener;
import org.terasology.nui.widgets.TooltipLine;

import java.util.Collections;

/**
 * Displays a list of all the recipes the workstation can craft.
 */
public class UIRecipeList extends CoreWidget {
    private static final int ICON_SIZE = 64;

    private IconManager iconManager;
    private RecipeStore recipeStore;

    private String workstationID = "InHand";

    private ListRecipe[] recipes = new ListRecipe[0];
    private ItemIcon[] icons = new ItemIcon[0];
    private int selectedRecipe = -1;
    private TextureRegion selectedTexture = Assets.getTexture("BasicCrafting:selectedIcon").get();

    private ActivateEventListener listener;

    @Override
    public void onDraw(Canvas canvas) {
        if (recipes != null && icons != null) {
            int i = 0;
            for (int y = 0; i < recipes.length; y++) {
                for (int x = 0; x < 5 && i < recipes.length; x++) {
                    if (i == selectedRecipe) {
                        canvas.drawTexture(selectedTexture, new Rectanglei(ICON_SIZE * x, ICON_SIZE * y, ICON_SIZE, ICON_SIZE));
                    }
                    canvas.drawWidget(icons[i], new Rectanglei(ICON_SIZE * x, ICON_SIZE * y, ICON_SIZE, ICON_SIZE));
                    i++;
                }
            }
        }
        canvas.addInteractionRegion(new BaseInteractionListener() {
            @Override
            public boolean onMouseClick(NUIMouseClickEvent event) {
                return processOnClick(event);
            }
        });

    }

    /**
     * Called when a mouse is clicked on the widget.
     * Calculates the selected recipe and flips a flag boolean
     *
     * @param event The click event.
     * @return True to consume the mouse click event
     */
    private boolean processOnClick(NUIMouseClickEvent event) {
        Vector2i pos = event.getRelativeMousePosition();
        pos.x = pos.x / ICON_SIZE;
        pos.y = pos.y / ICON_SIZE;
        selectedRecipe = pos.y * 5 + pos.x;
        if (selectedRecipe >= recipes.length || selectedRecipe < 0) {
            selectedRecipe = -1;
        }
        if (listener != null) {
            listener.onActivated(this);
        }
        return false;
    }

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        return new Vector2i(ICON_SIZE * 5, (int) (Math.ceil(recipes.length / 5.0) * ICON_SIZE));
    }

    /**
     * Gets the currently selected recipe
     *
     * @return The selected recipe or null if none are selected.
     */
    public ListRecipe getSelectedRecipe() {
        if (selectedRecipe > -1 && selectedRecipe < recipes.length) {
            return recipes[selectedRecipe];
        }
        return null;
    }

    /**
     * Sets the icon manager.
     *
     * @param newIconManager The new icon manager
     */
    public void setManagers(RecipeStore newRecipeStore, IconManager newIconManager) {
        recipeStore = newRecipeStore;
        iconManager = newIconManager;
    }

    /**
     * Set the workstation ID.
     * Triggers a collection of recipes for that workstation.
     *
     * @param newID The new id of the workstation
     */
    public void setWorkstationID(String newID) {
        workstationID = newID;
        collectRecipes();
        selectedRecipe = -1;
        if (listener != null) {
            listener.onActivated(this);
        }
    }

    public void subscribeRecipeView(ActivateEventListener newListener) {
        listener = newListener;
    }

    /**
     * Collect all the recipes for that workstation and set the icons to display
     */
    private void collectRecipes() {
        if (recipeStore.hasCategory(workstationID)) {
            recipes = recipeStore.getRecipes(workstationID, ListRecipe.class).toArray(new ListRecipe[0]);
            setIcons();
        }

    }

    /**
     * Set the icons to display for each recipe
     */

    private void setIcons() {
        icons = new ItemIcon[recipes.length];
        for (int i = 0; i < recipes.length; i++) {
            icons[i] = new ItemIcon();
            if (iconManager.hasIcon(recipes[i].output)) {
                chooseIcon(recipes[i].output, icons[i]);
            } else if (iconManager.hasMesh(recipes[i].output)) {
                chooseMesh(recipes[i].output, icons[i]);
            }

            icons[i].setTooltipLines(Collections.singletonList(new TooltipLine(recipes[i].output)));
        }
    }

    /**
     * Choose the first available mesh for the recipe
     *
     * @param iconID The id of the mesh to look for
     * @param icon   The ItemIcon to set the mesh on
     */
    private void chooseMesh(String iconID, ItemIcon icon) {
        Object[] meshes = iconManager.getMesh(iconID);
        switch (meshes.length) {
            case 0:
                return;
            default:
                icon.setMesh((Mesh) meshes[0]);
                break;
        }
        icon.setMeshTexture(iconManager.getTexture());
        icon.setIcon(null);
    }

    /**
     * Choose the first available TextureRegion for the recipe
     *
     * @param iconID The ID of the icon to use
     * @param icon   The ItemIcon to set it on
     */
    private void chooseIcon(String iconID, ItemIcon icon) {
        Object[] foundIcons = iconManager.getIcon(iconID);
        switch (icons.length) {
            case 0:
                return;
            default:
                icon.setIcon((TextureRegion) foundIcons[0]);
                break;
        }
        icon.setMesh(null);
        icon.setMeshTexture(null);
    }
}
