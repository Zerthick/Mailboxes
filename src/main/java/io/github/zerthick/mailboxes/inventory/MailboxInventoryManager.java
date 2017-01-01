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
