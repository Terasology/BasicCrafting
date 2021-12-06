// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.crafting.listCrafting.inHand;

import org.terasology.engine.input.BindButtonEvent;
import org.terasology.engine.input.DefaultBinding;
import org.terasology.engine.input.RegisterBindButton;
import org.terasology.input.InputType;
import org.terasology.input.Keyboard;

@RegisterBindButton(id = "inHandCrafting", description = "In-hand crafting", category = "interaction")
@DefaultBinding(type = InputType.KEY, id = Keyboard.KeyId.F)
public class InHandCraftingButton extends BindButtonEvent {
}
