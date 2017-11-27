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
package org.terasology.crafting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.assets.management.AssetManager;
import org.terasology.engine.module.ModuleManager;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.utilities.ReflectionUtil;


@RegisterSystem
public class TestSystem extends BaseComponentSystem {
    @In
    RecipeStore recipeStore;
    @In
    AssetManager assetManager;

    @Override
    public void postBegin() {
        Logger logger = LoggerFactory.getLogger(TestSystem.class);
        for (Prefab prefab : assetManager.getLoadedAssets(Prefab.class)) {
            logger.info(prefab.getName());
            for (Component component : prefab.iterateComponents()) {
                if (RecipeComponent.class.isAssignableFrom(component.getClass())) {
                    loadComponent(prefab, component);
                }
            }
        }

    }

    private void loadComponent(Prefab prefab, Component genericComponent) {
        RecipeComponent component = (RecipeComponent) genericComponent;
        component.getRecipes();
    }


}
