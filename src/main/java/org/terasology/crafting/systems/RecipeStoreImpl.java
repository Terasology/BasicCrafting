// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.systems;

import org.terasology.crafting.components.ComponentToIngredientComponent;
import org.terasology.crafting.components.CraftingIngredientComponent;
import org.terasology.crafting.components.Recipe;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.gestalt.assets.management.AssetManager;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Share(RecipeStore.class)
@RegisterSystem
public class RecipeStoreImpl extends BaseComponentSystem implements RecipeStore {

    @In
    private EntityManager entityManager;
    @In
    private AssetManager assetManager;

    private List<Recipe> recipeList = new ArrayList<>();
    private Map<String, Set<Integer>> categoryLookup = new HashMap<>();
    private Map<String, Set<String>> ingredientLookup = new HashMap<>();

    public boolean hasCategory(String category) {
        return categoryLookup.containsKey(category.toLowerCase());
    }

    public Recipe[] getRecipes(String category) {
        return getRecipes(new String[]{category});
    }

    public Recipe[] getRecipes(String[] categories) {
        return getRecipesFromIndices(getIndicesInCategories(categories));
    }

    public <T extends Recipe> List<T> getRecipes(String category, Class<T> filterClass) {
        return getRecipes(new String[]{category}, filterClass);
    }

    public <T extends Recipe> List<T> getRecipes(String[] categories, Class<T> filterClass) {
        Set<Integer> indices = getIndicesInCategories(categories);
        return getRecipesFromIndices(indices, filterClass);
    }

    /**
     * Filteres all the recipes with the given indices by a given type
     * @param indices The indices of the recipes
     * @param filterClass The class to filter by
     * @param <T> The type of the filtering Recipe
     * @return All recipes of that given type
     */
    private <T extends Recipe> List<T> getRecipesFromIndices(Set<Integer> indices, Class<T> filterClass) {
        if (indices.size() > 0) {
            List<T> recipes = new LinkedList<>();
            for (int index : indices) {
                Recipe recipe = recipeList.get(index);
                if (recipe.getClass().equals(filterClass)) {
                    recipes.add(filterClass.cast(recipeList.get(index)));
                }
            }
            return recipes;
        } else {
            return null;
        }
    }

    /**
     * Get all the indices contained within some given categories
     * @param categories The categories to collect from.
     * @return All recipe indices found
     */
    private Set<Integer> getIndicesInCategories(String[] categories) {
        Set<Integer> indices = new HashSet<>();
        for (String category : categories) {
            category = category.toLowerCase();
            if (categoryLookup.containsKey(category)) {
                indices.addAll(categoryLookup.get(category));
            }
        }
        return indices;
    }


    /**
     * @param indices The indices to use
     * @return The recipes located at the indices.
     */
    private Recipe[] getRecipesFromIndices(Set<Integer> indices) {
        if (indices.size() > 0) {
            Recipe[] recipes = new Recipe[indices.size()];
            int i = 0;
            for (int index : indices) {
                recipes[i] = recipeList.get(index);
                i++;
            }
            return recipes;
        } else {
            return null;
        }
    }

    /**
     * Adds a recipe to the store under set categories.
     *
     * @param recipe     The recipe to add
     * @param categories The categories to add the recipe under
     */

    public void putRecipe(Recipe recipe, String[] categories) {
        int index = addRecipeToStore(recipe);
        for (String category : categories) {
            addLinkToRecipe(index, category.toLowerCase());
        }
    }

    public String[] getIngredientNames(String name) {
        name = name.toLowerCase();
        if (ingredientLookup.containsKey(name)) {
            return ingredientLookup.get(name).toArray(new String[0]);
        } else {
            return null;
        }
    }

    public void scrapeIngredientNames() {
        scrapeNames(scrapeComponentMap());
    }

    /**
     * Collect all possible names for all prefabs
     * @param componentMap A mapping from component to names
     */
    private void scrapeNames(Map<Class<? extends Component>, Set<String>> componentMap) {
        for (Prefab prefab : assetManager.getLoadedAssets(Prefab.class)) {
            if (prefab.hasComponent(CraftingIngredientComponent.class)) {
                addLink(prefab.getName().toLowerCase(),
                        prefab.getComponent(CraftingIngredientComponent.class).ingredientIds);
            }
            for (Class<? extends Component> component : componentMap.keySet()) {
                if (prefab.hasComponent(component)) {
                    addLink(prefab.getName().toLowerCase(),
                            componentMap.get(component));
                }
            }
        }
    }

    /**
     * Add a name to the lookup table
     * @param key The key to use
     * @param items The names to add
     */
    private void addLink(String key, Collection<String> items) {
        Set<String> names = ingredientLookup.containsKey(key) ? ingredientLookup.get(key) : new HashSet<>();
        names.addAll(items);
        ingredientLookup.put(key, names);
    }

    /**
     * Collect all the links between components and prefabs
     * @return A map between components and ingredient names
     */
    private Map<Class<? extends Component>, Set<String>> scrapeComponentMap() {
        Map<Class<? extends Component>, Set<String>> result = new HashMap<>();
        for (Prefab prefab : assetManager.getLoadedAssets(Prefab.class)) {
            if (prefab.hasComponent(ComponentToIngredientComponent.class)) {
                ComponentToIngredientComponent component = prefab.getComponent(ComponentToIngredientComponent.class);
                for (Map.Entry<String, List<String>> entry : component.componentMap.entrySet()) {
                    try {
                        Class<? extends Component> componentClass = entityManager.getComponentLibrary().resolve(entry.getKey().toLowerCase()).getType();
                        Set<String> ingredientNames = result.containsKey(componentClass) ? result.get(componentClass) : new HashSet<>();
                        ingredientNames.addAll(entry.getValue());
                        result.put(componentClass, ingredientNames);
                    } catch (NullPointerException ignored) {
                        /* Handle a broken component name */
                    }

                }
            }
        }
        return result;
    }


    /**
     * Adds a recipe to the list
     *
     * @param recipe The recipe to add
     * @return The index of the recipe
     */
    private int addRecipeToStore(Recipe recipe) {
        recipeList.add(recipe);
        return recipeList.size() - 1;
    }

    /**
     * Adds a link between a recipe ID and a category
     *
     * @param id       The recipe's ID
     * @param category The category to add the recipe to
     */
    private void addLinkToRecipe(int id, String category) {
        Set<Integer> idList = categoryLookup.containsKey(category) ? categoryLookup.get(category) : new HashSet<>();
        idList.add(id);
        categoryLookup.put(category, idList);
    }
}
