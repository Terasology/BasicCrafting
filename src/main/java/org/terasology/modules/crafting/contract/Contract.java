// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.modules.crafting.contract;

import org.terasology.engine.entitySystem.prefab.Prefab;

import java.util.Collection;

public interface Contract<T> {
    Class<T> contractComponent();

    Collection<Prefab> outputs();
    Collection<Prefab> inputs();
}
