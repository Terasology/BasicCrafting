// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.basiccrafting.listCrafting.components;


import org.terasology.basiccrafting.components.Recipe;

import java.util.Map;

/**
 * Represents a List Recipe. Simply a list of inputs and a single output.
 */
public class ListRecipe implements Recipe {
    public String[] inputItems;
    public int[] inputCounts;
    public int outputCount;
    public String output = "";

    public ListRecipe(ListRecipeContainer copy, String result) {
        if (copy.inputs != null) {
            inputItems = new String[copy.inputs.size()];
            inputCounts = new int[copy.inputs.size()];
            int i = 0;
            for (Map.Entry<String, Integer> entry : copy.inputs.entrySet()) {
                inputItems[i] = entry.getKey();
                inputCounts[i] = entry.getValue();
                i++;
            }
        }
        output = result;
        outputCount = copy.outputCount;
    }


    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < inputItems.length; i++) {
            result += i > 0 ? " + " : "";
            result += inputCounts[i] + "x" + inputItems[i];
        }
        return result + " = " + outputCount + "x" + output;
    }
}
