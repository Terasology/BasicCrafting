// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.basiccrafting.components;

import org.terasology.engine.entitySystem.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Allows an ingredient name to be set on all prefabs that contain a listed component. Components should be specified in
 * the same format they are in the prefabs, that is "CraftingIngredientComponent" should be "CraftingIngredient"
 */
public class ComponentToIngredientComponent implements Component {
    public Map<String, List<String>> componentMap = new TreeMap<>();
}
