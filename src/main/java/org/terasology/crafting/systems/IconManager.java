// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.systems;


import org.terasology.engine.rendering.assets.mesh.Mesh;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.rendering.assets.texture.TextureRegion;

public interface IconManager {
    /**
     * Collect all the icons for all the ingredients
     */
    void scrapeIcons();

    /**
     * Get's all meshes associated with a key
     *
     * @param key The key to use
     * @return All the Meshes found or null if the key doesn't exist.
     */
    Mesh[] getMesh(String key);

    /**
     * Get all icons associated with a key
     *
     * @param key The key to use
     * @return All Icons found or null if the key doesn't exist.
     */
    TextureRegion[] getIcon(String key);

    /**
     * Check if there are any icons associated with the key
     *
     * @param key The key to look for
     * @return True if the key exists, false otherwise
     */
    boolean hasIcon(String key);

    /**
     * Check if there are any Meshes associated with the key
     *
     * @param key The key to look for
     * @return True if the key exists, false otherwise
     */
    boolean hasMesh(String key);

    /**
     * Get the texture to use for the Meshes.
     *
     * @return The Texture to use.
     */
    Texture getTexture();
}
