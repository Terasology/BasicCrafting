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

import org.terasology.crafting.listCrafting.components.ListRecipe;
import org.terasology.crafting.systems.IconManager;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.assets.mesh.Mesh;
import org.terasology.rendering.assets.texture.TextureRegion;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.CoreWidget;
import org.terasology.rendering.nui.layers.ingame.inventory.ItemIcon;
import org.terasology.utilities.Assets;
import org.terasology.utilities.random.FastRandom;

/**
 * Display a recipe
 */
public class UIRecipeView extends CoreWidget {
    private static final int ICON_SIZE = 64;

    private IconManager iconManager;
    private FastRandom random = new FastRandom();

    private TextureRegion addTexture = Assets.getTextureRegion("Crafting:AddIcon").get();
    private TextureRegion equalsTexture = Assets.getTextureRegion("Crafting:EqualsIcon").get();

    private ItemIcon[] inputIcons = null;
    private ItemIcon result = new ItemIcon();

    @Override
    public void onDraw(Canvas canvas) {
        if (inputIcons != null) {
            canvas.drawBackground();
            int i;
            for (i = 0; i < inputIcons.length; i++) {
                if (i > 0) {
                    canvas.drawTexture(addTexture, Rect2i.createFromMinAndSize((i * 2 - 1) * ICON_SIZE, 0, ICON_SIZE, ICON_SIZE));
                }
                canvas.drawWidget(inputIcons[i], Rect2i.createFromMinAndSize(i * 2 * ICON_SIZE, 0, ICON_SIZE, ICON_SIZE));
            }
            canvas.drawTexture(equalsTexture, Rect2i.createFromMinAndSize((i * 2 - 1) * ICON_SIZE, 0, ICON_SIZE, ICON_SIZE));
            canvas.drawWidget(result, Rect2i.createFromMinAndSize(i * 2 * ICON_SIZE, 0, ICON_SIZE, ICON_SIZE));
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
     * Called every tick.
     * Checks if a new recipe is available and updates if there is.
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

    /**
     * Pick a random icon for the item.
     *
     * @param iconID The item to look for items for
     * @param icon   The icon to set the choice on
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
     * @param icon   The ItemIcon to set the mesh on
     */
    private void chooseMesh(String iconID, ItemIcon icon) {
        Object[] meshes = iconManager.getMesh(iconID).toArray();
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
     * @param icon   The ItemIcon to set it on
     */
    private void chooseIcon(String iconID, ItemIcon icon) {
        TextureRegion[] icons = iconManager.getIcon(iconID).toArray(new TextureRegion[0]);
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
