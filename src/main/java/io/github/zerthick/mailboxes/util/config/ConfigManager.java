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

package io.github.zerthick.mailboxes.util.config;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.ImmutableList;
import io.github.zerthick.mailboxes.Mailboxes;
import io.github.zerthick.mailboxes.inventory.MailboxInventory;
import io.github.zerthick.mailboxes.location.MailboxLocationManager;
import io.github.zerthick.mailboxes.util.DataSerializer;
import io.github.zerthick.mailboxes.util.SQLUtil;
import io.github.zerthick.mailboxes.util.config.serializers.MailboxInventorySerializer;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class ConfigManager {

    public static void registerSerializers() {
        MailboxInventorySerializer.register();
    }

    public static MailboxLocationManager loadMailboxLocationManager(Mailboxes plugin) {

        final String TABLE_NAME = "MAILBOX_LOCATIONS";
        final String PRIMARY_KEY = "WORLD_ID";
        final String DATA_KEY = "DATA";
        final List<String> COLUMNS = ImmutableList.of(PRIMARY_KEY + " UUID PRIMARY KEY", DATA_KEY + " VARCHAR");

        Map<UUID, Set<Vector3i>> uuidSetMap = new HashMap<>();

        try {

            SQLUtil.createTable(TABLE_NAME, COLUMNS);
            SQLUtil.select(TABLE_NAME, resultSet -> {
                try {
                    while (resultSet.next()) {
                        uuidSetMap.put((UUID) resultSet.getObject(PRIMARY_KEY),
                                DataSerializer.deserializeMailboxLocationManagerList(resultSet.getString(DATA_KEY)));
                    }
                } catch (SQLException | ObjectMappingException | IOException e) {
                    plugin.getLogger().error("Error loading Mailbox locations! Error: " + e.getMessage());
                }
            });

        } catch (SQLException e) {
            plugin.getLogger().error("Error loading Mailbox locations! Error: " + e.getMessage());
        }

        return new MailboxLocationManager(uuidSetMap);
    }

    public static void saveMailboxLocationManager(MailboxLocationManager locationManager, Mailboxes plugin) {

        final String TABLE_NAME = "MAILBOX_LOCATIONS";
        final String PRIMARY_KEY = "WORLD_ID";

        try {

            Map<String, List<String>> serializedLocationManager = new HashMap<>();
            for (Map.Entry<UUID, Set<Vector3i>> entry : locationManager.getLocations().entrySet()) {
                String serializedLocationList = DataSerializer.serializeMailboxLocationManagerList(entry.getValue());
                serializedLocationManager.put(entry.getKey().toString(), ImmutableList.of(serializedLocationList));
            }

            if (!serializedLocationManager.isEmpty()) {
                SQLUtil.mergeMap(TABLE_NAME, PRIMARY_KEY, serializedLocationManager);
            }

        } catch (SQLException | IOException | ObjectMappingException e) {
            plugin.getLogger().error("Error saving Mailbox locations! Error: " + e.getMessage());
        }
    }

    public static MailboxInventory loadMailboxInventory(UUID playerUUID, Mailboxes plugin) {

        final String TABLE_NAME = "MAILBOX_INVENTORIES";
        final String PRIMARY_KEY = "PLAYER_ID";
        final String DATA_KEY = "DATA";
        final List<String> COLUMNS = ImmutableList.of(PRIMARY_KEY + " UUID PRIMARY KEY", DATA_KEY + " VARCHAR");

        MailboxInventory[] mailboxInventory = new MailboxInventory[1];
        mailboxInventory[0] = MailboxInventory.create(plugin.getInstance());

        try {

            SQLUtil.createTable(TABLE_NAME, COLUMNS);

            SQLUtil.select(TABLE_NAME, PRIMARY_KEY, playerUUID.toString(), resultSet -> {
                try {
                    if (resultSet.next()) {
                        mailboxInventory[0] = DataSerializer.deserializeMailboxInventory(resultSet.getString(DATA_KEY));
                    }
                } catch (SQLException | ObjectMappingException | IOException e) {
                    plugin.getLogger().error("Error loading player Mailbox! Error: " + e.getMessage());
                }
            });

        } catch (SQLException e) {
            plugin.getLogger().error("Error loading player Mailbox! Error: " + e.getMessage());
        }

        return mailboxInventory[0];
    }

    public static void saveMailboxInventory(UUID playerUUID, MailboxInventory mailboxInventory, Mailboxes plugin) {

        final String TABLE_NAME = "MAILBOX_INVENTORIES";
        final String PRIMARY_KEY = "PLAYER_ID";

        try {
            SQLUtil.merge(TABLE_NAME, PRIMARY_KEY, playerUUID.toString(), ImmutableList.of(DataSerializer.serializeMailboxInventory(mailboxInventory)));
        } catch (SQLException | IOException | ObjectMappingException e) {
            plugin.getLogger().error("Error saving player Mailbox! Error: " + e.getMessage());
        }
    }

    public static void saveMailboxInventories(Map<UUID, MailboxInventory> mailboxInventoryMap, Mailboxes plugin) {

        final String TABLE_NAME = "MAILBOX_INVENTORIES";
        final String PRIMARY_KEY = "PLAYER_ID";

        try {

            Map<String, List<String>> serializedMailboxInventoryMap = new HashMap<>();
            for (Map.Entry<UUID, MailboxInventory> entry : mailboxInventoryMap.entrySet()) {
                String serializedLocationList = DataSerializer.serializeMailboxInventory(entry.getValue());
                serializedMailboxInventoryMap.put(entry.getKey().toString(), ImmutableList.of(serializedLocationList));
            }

            if (!serializedMailboxInventoryMap.isEmpty()) {
                SQLUtil.mergeMap(TABLE_NAME, PRIMARY_KEY, serializedMailboxInventoryMap);
            }

        } catch (SQLException | IOException | ObjectMappingException e) {
            plugin.getLogger().error("Error saving player Mailboxes! Error: " + e.getMessage());
        }
    }

}
