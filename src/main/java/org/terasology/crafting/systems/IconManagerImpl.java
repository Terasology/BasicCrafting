// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.crafting.systems;

import com.google.common.collect.Iterables;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.inventory.ItemComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.engine.rendering.assets.mesh.Mesh;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.rendering.assets.texture.TextureRegion;
import org.terasology.engine.utilities.Assets;
import org.terasology.engine.world.block.BlockExplorer;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.block.BlockUri;
import org.terasology.engine.world.block.family.BlockFamily;
import org.terasology.gestalt.assets.management.AssetManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Share(IconManager.class)
@RegisterSystem
public class IconManagerImpl extends BaseComponentSystem implements IconManager {

    private final List<TextureRegion> iconList = new ArrayList<>();
    private final List<Mesh> meshList = new ArrayList<>();
    private final Map<String, Set<Integer>> iconLookup = new HashMap<>();
    private final Map<String, Set<Integer>> meshLookup = new HashMap<>();
    private Texture texture;

    @In
    private AssetManager assetManager;
    @In
    private EntityManager entityManager;
    @In
    private BlockManager blockManager;
    @In
    private RecipeStore recipeStore;

    private BlockExplorer blockExplorer;


    public void scrapeIcons() {
        blockExplorer = new BlockExplorer(assetManager);
        loadItems();
        loadBlocks();
    }

    public Mesh[] getMesh(String key) {
        key = key.toLowerCase();
        if (meshLookup.containsKey(key)) {
            Set<Integer> indexSet = meshLookup.get(key);
            Mesh[] meshes = new Mesh[indexSet.size()];
            int i = 0;
            for (Integer index : indexSet) {
                meshes[i] = meshList.get(index);
                i++;
            }
            return meshes;
        } else {
            return null;
        }
    }

    public TextureRegion[] getIcon(String key) {
        key = key.toLowerCase();
        if (iconLookup.containsKey(key)) {
            Set<Integer> indexSet = iconLookup.get(key);
            TextureRegion[] textures = new TextureRegion[indexSet.size()];
            int i = 0;
            for (Integer index : indexSet) {
                textures[i] = iconList.get(index);
                i++;
            }
            return textures;
        } else {
            return null;
        }
    }

    public boolean hasIcon(String key) {
        return iconLookup.containsKey(key.toLowerCase());
    }

    public boolean hasMesh(String key) {
        return meshLookup.containsKey(key.toLowerCase());
    }

    public Texture getTexture() {
        return texture;
    }

    /**
     * Searches through all the prefabs for Icons to use. If a prefab has an ItemComponent then the `icon` field of that
     * component is added to the store. It is associated with the prefab name and with the `id` field of a
     * CraftingIngredientComponent if one exists.
     */
    private void loadItems() {
        for (Prefab prefab : assetManager.getLoadedAssets(Prefab.class)) {
            if (prefab.hasComponent(ItemComponent.class)) {
                try {
                    if (prefab.exists() && prefab.hasComponent(ItemComponent.class)) {
                        ItemComponent itemComponent = prefab.getComponent(ItemComponent.class);
                        TextureRegion icon = itemComponent.icon;

                        /* Add link between the ingredient names and icon */
                        String[] otherNames = recipeStore.getIngredientNames(prefab.getName());
                        if (otherNames != null) {
                            for (String otherName : otherNames) {
                                addIconPair(otherName.toLowerCase(), icon);
                            }
                        }
                        /* Add link between full prefab name and icon */
                        addIconPair(prefab.getName().toLowerCase(), icon);

                        /* Add link between short prefab name and icon */
                        addIconPair(prefab.getUrn().getResourceName().toLowerCase(), icon);
                    }
                } catch (Exception ex) {
                    /* Ignore all exceptions, it will prevent bad prefabs from breaking everything. */
                }
            }
        }
    }

    /**
     * Load all the meshes for all blocks. This only uses one mesh per block family and hence all blocks in a family are
     * treated as the same.
     * <p>
     * The mesh is associated with the block family's URI
     */
    private void loadBlocks() {
        Set<BlockUri> blocks = new HashSet<>();

        Iterables.addAll(blocks, blockManager.listRegisteredBlockUris());
        Iterables.addAll(blocks, blockExplorer.getAvailableBlockFamilies());
        Iterables.addAll(blocks, blockExplorer.getFreeformBlockFamilies());

        for (BlockUri block : blocks) {
            if (!block.equals(BlockManager.UNLOADED_ID)) {
                /* Option A */
                BlockFamily blockFamily = blockManager.getBlockFamily(block.getFamilyUri());
                Mesh mesh = blockFamily.getArchetypeBlock().getMeshGenerator().getStandaloneMesh();

                /* Add the full block name */
                addMeshPair(blockFamily.getURI().toString().toLowerCase(), mesh);

                /* Add the short block name */
                addMeshPair(block.getBlockFamilyDefinitionUrn().getResourceName().toLowerCase(), mesh);

                /* Add all the block categories */
                for (String category : blockFamily.getCategories()) {
                    addMeshPair(category, mesh);
                }
            }
        }

        texture = Assets.getTexture("engine:terrain").orElseGet(() -> Assets.getTexture("engine:default").get());
    }

    /**
     * Links an icon to the given key
     *
     * @param key The key to use
     * @param value The icon to use
     */
    private void addIconPair(String key, TextureRegion value) {
        /* Add item to list */
        int index = iconList.indexOf(value);
        if (index == -1) {
            index = iconList.size();
            iconList.add(value);
        }

        /* Add links to item */
        Set<Integer> iconSet = iconLookup.containsKey(key) ? iconLookup.get(key) : new HashSet<>();
        iconSet.add(index);
        iconLookup.put(key, iconSet);
    }

    /**
     * Links a mesh to a given key
     *
     * @param key The key to use
     * @param value The icon to use
     */
    private void addMeshPair(String key, Mesh value) {
        /* Add item to list */
        int index = meshList.indexOf(value);
        if (index == -1) {
            index = meshList.size();
            meshList.add(value);
        }

        /* Add links to item */
        Set<Integer> meshSet = meshLookup.containsKey(key) ? meshLookup.get(key) : new HashSet<>();
        meshSet.add(index);
        meshLookup.put(key, meshSet);
    }


}
