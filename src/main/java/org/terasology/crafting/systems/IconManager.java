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

import org.terasology.engine.rendering.assets.mesh.Mesh;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.rendering.assets.texture.TextureRegion;

import java.util.Set;

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
