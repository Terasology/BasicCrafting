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
package org.terasology.crafting.components;

import org.terasology.engine.entitySystem.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Allows an ingredient name to be set on all prefabs that contain a listed component.
 * Components should be specified in the same format they are in the prefabs,
 * that is "CraftingIngredientComponent" should be "CraftingIngredient"
 */
public class ComponentToIngredientComponent implements Component {
    public Map<String, List<String>> componentMap = new TreeMap<>();
}
