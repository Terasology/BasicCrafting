// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.basiccrafting.components;

/**
 * The interface to represent a base component type
 */
public interface RecipeComponent {
    /**
     * Get all the recipes stored in this component
     *
     * @return The array of recipes
     */
    Recipe[] getRecipes();

    /**
     * Get the category to place these recipes into
     *
     * @return The category id
     */
    String[] getCategories();
}
