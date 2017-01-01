package io.github.zerthick.mailboxes.location;

import com.flowpowered.math.vector.Vector3i;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MailboxLocationManager {

    private Map<UUID, Set<Vector3i>> locations;

    public MailboxLocationManager(Map<UUID, Set<Vector3i>> locations) {
        this.locations = locations;
    }

    public void addLocation(UUID worldUUID, Vector3i location) {
        Set<Vector3i> worldLocations = locations.getOrDefault(worldUUID, new HashSet<>());
        worldLocations.add(location);
        locations.put(worldUUID, worldLocations);
    }

    public void removeLocation(UUID worldUUID, Vector3i location) {
        locations.getOrDefault(worldUUID, new HashSet<>()).remove(location);
    }

    public boolean isLocation(UUID worldUUID, Vector3i location) {
        return locations.getOrDefault(worldUUID, new HashSet<>()).contains(location);
    }

    public Map<UUID, Set<Vector3i>> getLocations() {
        return locations;
    }

    public void setLocations(Map<UUID, Set<Vector3i>> locations) {
        this.locations = locations;
    }
}
