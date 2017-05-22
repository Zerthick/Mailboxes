/*
 * Copyright (C) 2017  Zerthick
 *
 * This file is part of Mailboxes.
 *
 * Mailboxes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Mailboxes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mailboxes.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zerthick.mailboxes.location;

import com.flowpowered.math.vector.Vector3i;
import io.github.zerthick.mailboxes.Mailboxes;
import io.github.zerthick.mailboxes.util.config.ConfigManager;
import org.spongepowered.api.Sponge;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MailboxLocationManager {

    private Map<UUID, Set<Vector3i>> locations;
    private Mailboxes plugin;

    public MailboxLocationManager(Map<UUID, Set<Vector3i>> locations, Mailboxes plugin) {
        this.locations = locations;
        this.plugin = plugin;
    }

    public void addLocation(UUID worldUUID, Vector3i location) {
        Set<Vector3i> worldLocations = locations.getOrDefault(worldUUID, new HashSet<>());
        worldLocations.add(location);
        locations.put(worldUUID, worldLocations);
    }

    public void removeLocation(UUID worldUUID, Vector3i location) {
        locations.getOrDefault(worldUUID, new HashSet<>()).remove(location);

        //Remove location from database
        Sponge.getScheduler().createTaskBuilder().async().execute(() -> {
            ConfigManager.deleteMailboxLocation(worldUUID, location, plugin);
        }).submit(plugin.getInstance());
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
