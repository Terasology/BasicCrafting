// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.crafting.listCrafting.UI;

import org.joml.Vector2i;
import org.terasology.crafting.listCrafting.components.ListRecipe;
import org.terasology.crafting.systems.IconManager;
import org.terasology.engine.rendering.assets.mesh.Mesh;
import org.terasology.engine.rendering.assets.texture.TextureRegion;
import org.terasology.rendering.nui.layers.ingame.inventory.ItemIcon;
import org.terasology.engine.utilities.Assets;
import org.terasology.engine.utilities.random.FastRandom;
import org.terasology.joml.geom.Rectanglei;
import org.terasology.nui.Canvas;
import org.terasology.nui.CoreWidget;

/**
 * Display a recipe
 */
public class UIRecipeView extends CoreWidget {
    private static final int ICON_SIZE = 64;

    private IconManager iconManager;
    private FastRandom random = new FastRandom();

    private TextureRegion addTexture = Assets.getTextureRegion("BasicCrafting:AddIcon").get();
    private TextureRegion equalsTexture = Assets.getTextureRegion("BasicCrafting:EqualsIcon").get();

    private ListRecipe recipe;
    private ItemIcon[] inputIcons = null;
    private ItemIcon result = new ItemIcon();

    @Override
    public void onDraw(Canvas canvas) {
        if (inputIcons != null) {
            canvas.drawBackground();
            int i;
            for (i = 0; i < inputIcons.length; i++) {
                if (i > 0) {
                    canvas.drawTexture(addTexture, new Rectanglei((i * 2 - 1) * ICON_SIZE, 0).setSize(ICON_SIZE, ICON_SIZE));
                }
                canvas.drawWidget(inputIcons[i], new Rectanglei(i * 2 * ICON_SIZE, 0).setSize(ICON_SIZE, ICON_SIZE));
            }
            canvas.drawTexture(equalsTexture, new Rectanglei((i * 2 - 1) * ICON_SIZE, 0).setSize(ICON_SIZE, ICON_SIZE));
            canvas.drawWidget(result, new Rectanglei(i * 2 * ICON_SIZE, 0).setSize(ICON_SIZE, ICON_SIZE));
        }
    }

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        return new Vector2i(64 * (inputIcons == null ? 0 : inputIcons.length * 2 + 1), 64);
    }

    /**
     * Set the icon manager to use
     *
     * @param newManager The new icon manager.
     */
    public void setIconManager(IconManager newManager) {
        iconManager = newManager;
    }

    /**
     * Called every tick. Checks if a new recipe is available and updates if there is.
     */
    public void updateRecipe(ListRecipe newRecipe) {
        if (newRecipe != null) {
            inputIcons = new ItemIcon[newRecipe.inputItems.length];
            for (int i = 0; i < inputIcons.length; i++) {
                inputIcons[i] = new ItemIcon();
            }
            updateIcons(newRecipe);
        } else {
            /* Causes the widget to not draw anything */
            inputIcons = null;
        }
        recipe = newRecipe;
    }

    /**
     * Update all the icons for the new recipe;
     *
     * @param newRecipe The new recipe to use.
     */
    private void updateIcons(ListRecipe newRecipe) {
        for (int i = 0; i < inputIcons.length; i++) {
            increment(newRecipe.inputItems[i], inputIcons[i]);
            inputIcons[i].setQuantity(newRecipe.inputCounts[i]);
        }
        increment(newRecipe.output, result);
        result.setQuantity(newRecipe.outputCount);
    }

    public void stepItemIcons() {
        if (recipe != null) {
            for (int i = 0; i < inputIcons.length; i++) {
                increment(recipe.inputItems[i], inputIcons[i]);
            }
            increment(recipe.output, result);
        }
    }

    /**
     * Pick a random icon for the item.
     *
     * @param iconID The item to look for items for
     * @param icon The icon to set the choice on
     */
    private void increment(String iconID, ItemIcon icon) {
        if (iconManager.hasIcon(iconID) && iconManager.hasMesh(iconID)) {
            if (random.nextFloat() < 0.5f) {
                chooseMesh(iconID, icon);
            } else {
                chooseIcon(iconID, icon);
            }
        } else if (iconManager.hasIcon(iconID)) {
            chooseIcon(iconID, icon);
        } else if (iconManager.hasMesh(iconID)) {
            chooseMesh(iconID, icon);
        }
        icon.setTooltip(iconID);
    }

    /**
     * Choose the first available mesh for the recipe
     *
     * @param iconID The id of the mesh to look for
     * @param icon The ItemIcon to set the mesh on
     */
    private void chooseMesh(String iconID, ItemIcon icon) {
        Object[] meshes = iconManager.getMesh(iconID);
        switch (meshes.length) {
            case 0:
                return;
            case 1:
                icon.setMesh((Mesh) meshes[0]);
                break;
            default:
                icon.setMesh((Mesh) meshes[random.nextInt(meshes.length)]);
        }
        icon.setMeshTexture(iconManager.getTexture());
        icon.setIcon(null);
    }

    /**
     * Choose the first available TextureRegion for the recipe
     *
     * @param iconID The ID of the icon to use
     * @param icon The ItemIcon to set it on
     */
    private void chooseIcon(String iconID, ItemIcon icon) {
        TextureRegion[] icons = iconManager.getIcon(iconID);
        switch (icons.length) {
            case 0:
                return;
            case 1:
                icon.setIcon(icons[0]);
                break;
            default:
                icon.setIcon(icons[random.nextInt(icons.length)]);
        }
        icon.setMesh(null);
        icon.setMeshTexture(null);
    }
}
