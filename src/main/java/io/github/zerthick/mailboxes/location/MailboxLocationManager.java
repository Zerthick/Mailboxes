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
