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

import org.terasology.crafting.listCrafting.components.CraftingWorkstationComponent;
import org.terasology.crafting.listCrafting.inHand.InHandCraftingButton;
import org.terasology.crafting.systems.IconManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.Priority;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.common.ActivateEvent;
import org.terasology.engine.network.ClientComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.input.ButtonState;

@RegisterSystem(RegisterMode.ALWAYS)
public class ListUISystem extends BaseComponentSystem {
    @In
    private NUIManager nuiManager;
    @In
    private IconManager iconManager;

    /**
     * This event will be triggered whenever one presses the InHandCraftingButton.
     * It will toggle the default crafting screen with the workstation set to 'InHand'
     *
     * @param event           The button event
     * @param entity          The entity sending the event, this should be the one trying to craft.
     * @param clientComponent Flag component for filtering
     */
    @ReceiveEvent
    public void inHandCraftRequested(InHandCraftingButton event, EntityRef entity,
                                     ClientComponent clientComponent) {
        if (event.getState() == ButtonState.DOWN) {
            nuiManager.toggleScreen("BasicCrafting:BaseListCraftingScreen");
            if (nuiManager.isOpen("BasicCrafting:BaseListCraftingScreen")) {
                BaseListCraftingScreen screen = (BaseListCraftingScreen) nuiManager.getScreen("BasicCrafting:BaseListCraftingScreen");
                screen.setWorkstationID("InHand");
            }
        }
    }

    /**
     * This event will be triggered if a workstation block is triggered. It sets the correct workstation id and shows the screen.
     * It has low priority so a separate system can consume the event before this
     *
     * @param event     The activation event
     * @param entity    The entity being activated
     * @param component The flag workstation component
     */
    @Priority(EventPriority.PRIORITY_LOW)
    @ReceiveEvent
    public void workstationCraftRequested(ActivateEvent event, EntityRef entity, CraftingWorkstationComponent component) {
        if (!event.isConsumed()) {
            nuiManager.toggleScreen("BasicCrafting:BaseListCraftingScreen");
            if (nuiManager.isOpen("BasicCrafting:BaseListCraftingScreen")) {
                BaseListCraftingScreen screen = (BaseListCraftingScreen) nuiManager.getScreen("BasicCrafting:BaseListCraftingScreen");
                screen.setWorkstationID(component.recipeCategory);
            }
            event.consume();
        }
    }
}
