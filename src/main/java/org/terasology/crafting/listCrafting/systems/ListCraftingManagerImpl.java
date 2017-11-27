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
package org.terasology.crafting.listCrafting.systems;

import org.terasology.crafting.components.Recipe;
import org.terasology.crafting.listCrafting.components.CraftingIngredientComponent;
import org.terasology.crafting.listCrafting.components.ListRecipe;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.logic.inventory.InventoryUtils;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.world.block.items.BlockItemComponent;

@Share(ListCraftingManager.class)
@RegisterSystem
public class ListCraftingManagerImpl extends BaseComponentSystem implements ListCraftingManager {

    @In
    private InventoryManager inventoryManager;
    @In
    private EntityManager entityManager;

    /**
     * Attempts to craft the given recipe
     * If the recipe is successfully crafted then the inputs will be removed and the resultant item returned.
     * The entity doing the crafting must have an inventory.
     *
     * @param craftingEntity The entity doing the crafting.
     * @param recipe         The recipe to be crafted
     * @return The newly crafted item or null if it was unsuccessful
     */
    public EntityRef[] craftRecipe(EntityRef craftingEntity, ListRecipe recipe, boolean giveToCrafter) {

        int[] slots = getSlots(craftingEntity, recipe);
        if (slots != null) {
            for (int i = 0; i < slots.length; i++) {
                EntityRef removedItem = inventoryManager.removeItem(craftingEntity, craftingEntity, slots[i], true, recipe.inputCounts[i]);
                if (removedItem == null) {
                    return new EntityRef[0];
                }
            }
            if (giveToCrafter) {
                for (int i = 0; i < recipe.outputCount; i++) {
                    inventoryManager.giveItem(craftingEntity, craftingEntity, entityManager.create(recipe.output));
                }
                return new EntityRef[0];
            } else {
                EntityRef[] crafted = new EntityRef[recipe.outputCount];
                for (int i = 0; i < recipe.outputCount; i++) {
                    crafted[i] = entityManager.create(recipe.output);
                }
                return crafted;
            }
        }
        return null;
    }

    public boolean verifyRecipe(Recipe recipe) {
        return recipe.getClass() == ListRecipe.class;
    }

    /**
     * Locates the position of a given String item name in an inventory.
     * Attempts to match the string against either the prefab name, block name or the CraftingIngredientComponent (if one exists)
     *
     * @param entity The entity to look in for the item
     * @param item   The name of the item being looked for
     * @return The slot containing the item or -1 if it was not found
     */
    private int findInInventory(EntityRef entity, String item, int count) {
        int slotCount = InventoryUtils.getSlotCount(entity);
        for (int i = 0; i < slotCount; i++) {
            EntityRef slotItem = InventoryUtils.getItemAt(entity, i);
            int stackSize = InventoryUtils.getStackCount(slotItem);
            if (stackSize < count) {
                continue;
            }
            if (item.equalsIgnoreCase(slotItem.getParentPrefab().getName())) {
                return i;
            } else if (slotItem.hasComponent(CraftingIngredientComponent.class)) {
                if (item.equalsIgnoreCase(slotItem.getComponent(CraftingIngredientComponent.class).id)) {
                    return i;
                }
            } else if (slotItem.hasComponent(BlockItemComponent.class)) {
                if (item.equalsIgnoreCase(slotItem.getComponent(BlockItemComponent.class).blockFamily.getURI().toString())) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Gets all the slots the items could be in.
     *
     * @param entity The entity to look in
     * @param recipe The recipe to be looking for
     * @return The list of slots the inputs are in. If all inputs could not be found, null is returned
     */
    private int[] getSlots(EntityRef entity, ListRecipe recipe) {
        if (!entity.hasComponent(InventoryComponent.class)) {
            return null;
        }
        int[] slots = new int[recipe.inputItems.length];
        for (int i = 0; i < recipe.inputItems.length; i++) {
            int slot = findInInventory(entity, recipe.inputItems[i], recipe.inputCounts[i]);
            if (slot != -1) {
                slots[i] = slot;
            } else {
                return null;
            }
        }
        return slots;
    }
}
