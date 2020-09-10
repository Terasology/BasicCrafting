// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.listCrafting.components;

import org.terasology.nui.reflection.MappedContainer;

import java.util.Map;

/**
 * This is the data structure that the user will input into in the prefab.
 */
@MappedContainer
public class ListRecipeContainer {
    public Map<String, Integer> inputs;
    public int outputCount;
    public String output;
}
