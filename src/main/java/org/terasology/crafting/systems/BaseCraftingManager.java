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
package org.terasology.crafting.systems;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.crafting.listCrafting.components.ListRecipe;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.module.inventory.components.InventoryComponent;
import org.terasology.module.inventory.systems.InventoryUtils;
import org.terasology.engine.logic.inventory.ItemComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.block.items.BlockItemComponent;

import java.util.HashMap;
import java.util.Map;


/**
 * A base crafting manager that implements a number of helpful methods.
 */
public abstract class BaseCraftingManager extends BaseComponentSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCraftingManager.class);

    @In
    private RecipeStore recipeStore;

    /**
     * Checks if the item is the same one being looked for. Will check if the item matches the full name, short name or
     * any alternative ingredient names.
     *
     * @param item The item to check
     * @param name The item being looked for
     * @return True if item is expectedItem, false otherwise
     */
    private boolean itemMatchesIngredientName(EntityRef item, String name) {
        return matchesExactly(item, name)
                || matchesShortName(item, name)
                || matchesOtherName(item, name);
    }

    /**
     * Checks if the full name (module:item#fragment) is equal to the name
     *
     * @param item The item to check
     * @param name The name to check against
     * @return True if they match, false otherwise.
     */
    private boolean matchesExactly(EntityRef item, String name) {
        /* Check if it's a block */
        if (item.hasComponent(BlockItemComponent.class)) {
            if (name.equalsIgnoreCase(item.getComponent(BlockItemComponent.class).blockFamily.getURI().toString())) {
                return true;
            }
            /* Check the prefab name */
        } else if (name.equalsIgnoreCase(item.getParentPrefab().getName())) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the short name (item part of module:item#fragment) is equal to the name
     *
     * @param item The item to check
     * @param name The name to check against
     * @return True if they match, false otherwise.
     */
    private boolean matchesShortName(EntityRef item, String name) {
        /* Check the short form of a block */
        if (item.hasComponent(BlockItemComponent.class)) {
            String blockName =
                    item.getComponent(BlockItemComponent.class).blockFamily.getURI().getBlockFamilyDefinitionUrn().getResourceName().toString();
            if (name.equalsIgnoreCase(blockName)) {
                return true;
            }
            /* Check short form of prefab */
        } else {
            String prefabName = item.getParentPrefab().getUrn().getResourceName().toString();
            if (name.equalsIgnoreCase(prefabName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the item has another ingredient name that matches the name
     *
     * @param item The item to check
     * @param name The name to check against
     * @return True if they match, false otherwise.
     */
    private boolean matchesOtherName(EntityRef item, String name) {
        if (item.hasComponent(BlockItemComponent.class)) {
            BlockItemComponent component = item.getComponent(BlockItemComponent.class);
            for (String category : component.blockFamily.getCategories()) {
                if (name.equalsIgnoreCase(category)) {
                    return true;
                }
            }

        } else {
            String[] otherNames = recipeStore.getIngredientNames(item.getParentPrefab().getName());
            if (otherNames != null) {
                for (String otherName : otherNames) {
                    if (name.equalsIgnoreCase(otherName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets all the slots the items could be in.
     *
     * @param entity The entity to look in
     * @param recipe The recipe to be looking for
     * @return The list of slots the inputs are in. If all inputs could not be found, null is returned
     */
    protected int[] getSlots(EntityRef entity, ListRecipe recipe) {
        if (!entity.hasComponent(InventoryComponent.class)) {
            return null;
        }

        InventoryComponent inventory = entity.getComponent(InventoryComponent.class);

        int[] slots = new int[recipe.inputItems.length];
        Map<Integer, Integer> slotStacks = new HashMap<>();
        for (int i = 0; i < recipe.inputItems.length; i++) {
            int slot = findInInventory(entity, recipe.inputItems[i], recipe.inputCounts[i], 0);
            ItemComponent item = inventory.itemSlots.get(slot).getComponent(ItemComponent.class);
            if (slotStacks.containsKey(slot)) {
                while (slotStacks.containsKey(slot) && slotStacks.get(slot) == 0) {
                    slot = findInInventory(entity, recipe.inputItems[i], recipe.inputCounts[i], slot + 1);
                }
                slotStacks.put(slot, (int) item.stackCount);
            } else {
                slotStacks.put(slot, (int) item.stackCount);
            }

            slotStacks.put(slot, slotStacks.get(slot) - 1);
            if (slot != -1) {
                slots[i] = slot;
            } else {
                return null;
            }
        }

        return slots;
    }

    /**
     * Locates the position of a given String item name in an inventory. Attempts to match the string against either the
     * prefab name, block name or the CraftingIngredientComponent (if one exists)
     *
     * @param entity The entity to look in for the item
     * @param item The name of the item being looked for
     * @return The slot containing the item or -1 if it was not found
     */
    private int findInInventory(EntityRef entity, String item, int count, int start) {
        int slotCount = InventoryUtils.getSlotCount(entity);
        for (int i = start; i < slotCount; i++) {
            EntityRef slotItem = InventoryUtils.getItemAt(entity, i);
            int stackSize = InventoryUtils.getStackCount(slotItem);
            if (stackSize >= count && itemMatchesIngredientName(slotItem, item)) {
                return i;
            }
        }
        return -1;
    }
}
