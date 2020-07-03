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

import org.terasology.crafting.components.CraftingIngredientComponent;
import org.terasology.crafting.events.OnRecipeCrafted;
import org.terasology.crafting.listCrafting.components.ListRecipe;
import org.terasology.crafting.systems.BaseCraftingManager;
import org.terasology.crafting.systems.RecipeStore;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.common.RetainComponentsComponent;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.logic.inventory.InventoryUtils;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.items.BlockItemComponent;
import org.terasology.world.block.items.BlockItemFactory;

@Share(ListCraftingManager.class)
@RegisterSystem
public class ListCraftingManagerImpl extends BaseCraftingManager implements ListCraftingManager {

    @In
    private InventoryManager inventoryManager;
    @In
    private RecipeStore recipeStore;
    @In
    private EntityManager entityManager;
    @In
    private BlockManager blockManager;
    private BlockItemFactory blockItemFactory;

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
                    inventoryManager.giveItem(craftingEntity, craftingEntity, createResultFromName(recipe.output));
                }
                return new EntityRef[0];
            } else {
                EntityRef[] crafted = new EntityRef[recipe.outputCount];
                for (int i = 0; i < recipe.outputCount; i++) {
                    crafted[i] = createResultFromName(recipe.output);
                }
                return crafted;
            }
        }
        return null;
    }

    /**
     * Handles the event sent when a recipe is crafted.
     * If the recipe is successfully crafted then the inputs will be removed and the resultant item returned.
     * The entity doing the crafting must have an inventory.
     *
     * @param event          The OnRecipeCraftedEvent.
     * @param entity         The entity doing the crafting
     * @return The newly crafted item or null if it was unsuccessful
     */
    @ReceiveEvent
    public EntityRef[] OnRecipeCraftedEvent(OnRecipeCrafted event, EntityRef entity){
        EntityRef craftingEntity = entity;
        ListRecipe recipe = event.getRecipe();
        boolean giveToCrafter = event.isGiveToCrafter();

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
                    inventoryManager.giveItem(craftingEntity, craftingEntity, createResultFromName(recipe.output));
                }
                return new EntityRef[0];
            } else {
                EntityRef[] crafted = new EntityRef[recipe.outputCount];
                for (int i = 0; i < recipe.outputCount; i++) {
                    crafted[i] = createResultFromName(recipe.output);
                }
                return crafted;
            }
        }
        return null;
    }

    /**
     * Detects if the result of a recipe is a Block or an Item and creates the correct entity.
     *
     * @param name The name of the result
     * @return The new entity or an air block if creation was not successful.
     */
    private EntityRef createResultFromName(String name) {
        if (entityManager.getPrefabManager().exists(name)) {
            return entityManager.create(name);
        } else {
            BlockFamily block = blockManager.getBlockFamily(name);
            if (block != null) {
                if (blockItemFactory == null) {
                    blockItemFactory = new BlockItemFactory(entityManager);
                }
                return blockItemFactory.newInstance(block);
            }
        }
        return EntityRef.NULL;
    }
}
