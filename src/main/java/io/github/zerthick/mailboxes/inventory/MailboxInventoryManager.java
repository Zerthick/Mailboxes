/*
 * Copyright (C) 2017  Zerthick
 *
 * This file is part of MailBoxes.
 *
 * MailBoxes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * MailBoxes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MailBoxes.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zerthick.mailboxes.inventory;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MailboxInventoryManager {

    private Map<UUID, MailboxInventory> inventoryMap;

    public MailboxInventoryManager(Map<UUID, MailboxInventory> inventoryMap) {
        this.inventoryMap = inventoryMap;
    }

    public Optional<MailboxInventory> getInventory(UUID uuid) {
        return Optional.ofNullable(inventoryMap.get(uuid));
    }

    public Optional<MailboxInventory> removeInventory(UUID uuid) {
        return Optional.ofNullable(inventoryMap.remove(uuid));
    }

    public boolean hasInventory(UUID uuid) {
        return getInventory(uuid).isPresent();
    }

    public Optional<MailboxInventory> addInventory(UUID uuid, MailboxInventory inventory) {
        return Optional.ofNullable(inventoryMap.put(uuid, inventory));
    }

    public Map<UUID, MailboxInventory> getInventoryMap() {
        return inventoryMap;
    }

    public void setInventoryMap(Map<UUID, MailboxInventory> inventoryMap) {
        this.inventoryMap = inventoryMap;
    }
}
