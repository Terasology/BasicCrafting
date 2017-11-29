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
package org.terasology.crafting.systems;

import com.google.common.collect.Iterables;
import org.terasology.assets.management.AssetManager;
import org.terasology.crafting.components.CraftingIngredientComponent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.inventory.ItemComponent;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.rendering.assets.mesh.Mesh;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.assets.texture.TextureRegion;
import org.terasology.utilities.Assets;
import org.terasology.world.block.BlockExplorer;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Share(IconManager.class)
@RegisterSystem
public class IconManagerImpl extends BaseComponentSystem implements IconManager {

    private Map<String, Set<TextureRegion>> iconMap = new HashMap<>();
    private Map<String, Set<Mesh>> meshMap = new HashMap<>();
    private Texture texture;

    @In
    private AssetManager assetManager;
    @In
    private EntityManager entityManager;
    @In
    private BlockManager blockManager;

    private BlockExplorer blockExplorer;

    /**
     * Called just before the world is loaded.
     * Used to initiate retrieving all the icons
     */
    @Override
    public void postBegin() {
        blockExplorer = new BlockExplorer(assetManager);
        loadItems();
        loadBlocks();
    }

    /**
     * Get's all meshes associated with a key
     *
     * @param key The key to use
     * @return All the Meshes found or null if the key doesn't exist.
     */
    public Set<Mesh> getMesh(String key) {
        key = key.toLowerCase();
        if (meshMap.containsKey(key)) {
            return meshMap.get(key);
        } else {
            return null;
        }
    }

    /**
     * Get all icons associated with a key
     *
     * @param key The key to use
     * @return All Icons found or null if the key doesn't exist.
     */
    public Set<TextureRegion> getIcon(String key) {
        key = key.toLowerCase();
        if (iconMap.containsKey(key)) {
            return iconMap.get(key);
        } else {
            return null;
        }
    }

    /**
     * Check if there are any icons associated with the key
     *
     * @param key The key to look for
     * @return True if the key exists, false otherwise
     */
    public boolean hasIcon(String key) {
        key = key.toLowerCase();
        return iconMap.containsKey(key);
    }

    /**
     * Check if there are any Meshes associated with the key
     *
     * @param key The key to look for
     * @return True if the key exists, false otherwise
     */
    public boolean hasMesh(String key) {
        key = key.toLowerCase();
        return meshMap.containsKey(key);
    }

    /**
     * Get the texture to use for the Meshes.
     *
     * @return The Texture to use.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Searches through all the prefabs for Icons to use.
     * If a prefab has an ItemComponent then the `icon` field of that component is added to the store.
     * It is associated with the prefab name and with the `id` field of a CraftingIngredientComponent if one exists.
     */
    private void loadItems() {
        for (Prefab prefab : assetManager.getLoadedAssets(Prefab.class)) {
            if (prefab.hasComponent(ItemComponent.class)) {
                EntityRef entity = EntityRef.NULL;
                try {
                    entity = entityManager.create(prefab);
                    if (entity.exists() && entity.hasComponent(ItemComponent.class)) {
                        ItemComponent itemComponent = entity.getComponent(ItemComponent.class);
                        TextureRegion icon = itemComponent.icon;

                        /* Add link between the ingredient name and icon */
                        if (entity.hasComponent(CraftingIngredientComponent.class)) {
                            CraftingIngredientComponent ingredient = entity.getComponent(CraftingIngredientComponent.class);
                            for (String id : ingredient.ingredientIds) {
                                addIconPair(id.toLowerCase(), icon);
                            }
                        }
                        /* Add link between prefab name and icon */
                        addIconPair(prefab.getName().toLowerCase(), icon);
                    }
                } catch (Exception ex) {
                    /* Ignore all exceptions, it will prevent bad entities from breaking everything. */
                } finally {
                    entity.destroy();
                }
            }
        }
    }

    /**
     * Load all the meshes for all blocks.
     * This only uses one mesh per block family and hence all blocks in a family are treated as the same.
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

                /* Option B */
                //blockManager.getBlock(block).getMeshGenerator().getStandaloneMesh();

                addMeshPair(blockFamily.getURI().toString().toLowerCase(), mesh);
            }
        }

        Optional<Texture> terrainTex = Assets.getTexture("engine:terrain");
        texture = terrainTex.orElseGet(() -> Assets.getTexture("engine:default").get());
    }

    /**
     * Links an icon to the given key
     *
     * @param key   The key to use
     * @param value The icon to use
     */
    private void addIconPair(String key, TextureRegion value) {
        Set<TextureRegion> iconList;
        if (iconMap.containsKey(key)) {
            iconList = iconMap.get(key);
        } else {
            iconList = new HashSet<>();
        }
        iconList.add(value);
        iconMap.put(key, iconList);
    }

    /**
     * Links a mesh to a given key
     *
     * @param key   The key to use
     * @param value The icon to use
     */
    private void addMeshPair(String key, Mesh value) {
        Set<Mesh> meshList;
        if (iconMap.containsKey(key)) {
            meshList = meshMap.get(key);
        } else {
            meshList = new HashSet<>();
        }
        meshList.add(value);
        meshMap.put(key, meshList);
    }


}
