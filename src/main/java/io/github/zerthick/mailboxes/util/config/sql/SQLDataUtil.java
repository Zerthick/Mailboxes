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

package io.github.zerthick.mailboxes.util.config.sql;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.ImmutableList;
import io.github.zerthick.mailboxes.inventory.MailboxInventory;
import io.github.zerthick.mailboxes.util.DataSerializer;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class SQLDataUtil {

    public static void createMailboxLocationsTable(Logger logger) {

        List<String> columns = ImmutableList.of("WORLD_ID UUID", "LOCATION VARCHAR", "PRIMARY KEY (WORLD_ID, LOCATION)");

        try {
            SQLUtil.createTable("MAILBOX_LOCATIONS", columns);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static Map<UUID, Set<Vector3i>> loadMailboxLocations(Logger logger) {

        Map<UUID, Set<Vector3i>> uuidSetMap = new HashMap<>();

        try {
            SQLUtil.select("MAILBOX_LOCATIONS", resultSet -> {
                try {
                    while (resultSet.next()) {
                        UUID worldID = (UUID) resultSet.getObject("WORLD_ID");
                        Set<Vector3i> worldLocations = uuidSetMap.getOrDefault(worldID, new HashSet<>());
                        worldLocations.add(DataSerializer.deserializeMailboxLocation(resultSet.getString("LOCATION")));
                        uuidSetMap.put(worldID, worldLocations);
                    }
                } catch (SQLException | ObjectMappingException | IOException e) {
                    logger.error(e.getMessage());
                }
            });
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return uuidSetMap;
    }

    public static void saveMailboxLocations(Map<UUID, Set<Vector3i>> locations, Logger logger) {

        try {
            SQLUtil.executeBatch("MERGE INTO MAILBOX_LOCATIONS VALUES (?, ?)", preparedStatement -> {
                for (Map.Entry<UUID, Set<Vector3i>> entry : locations.entrySet()) {
                    try {
                        UUID worldID = entry.getKey();
                        for (Vector3i location : entry.getValue()) {
                            preparedStatement.setObject(1, worldID);
                            preparedStatement.setString(2, DataSerializer.serializeMailboxLocation(location));
                            preparedStatement.addBatch();
                        }
                    } catch (IOException | SQLException | ObjectMappingException e) {
                        logger.error(e.getMessage());
                    }
                }
            });
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void deleteMailboxLocation(UUID worldUUID, Vector3i location, Logger logger) {
        try {
            SQLUtil.executeUpdate("DELETE FROM MAILBOX_LOCATIONS WHERE WORLD_ID = ? AND LOCATION = ?", preparedStatement -> {
                try {
                    preparedStatement.setObject(1, worldUUID);
                    preparedStatement.setObject(2, DataSerializer.serializeMailboxLocation(location));
                } catch (SQLException | IOException | ObjectMappingException e) {
                    logger.error(e.getMessage());
                }
            });
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void createMailboxInventoriesTable(Logger logger) {

        List<String> columns = ImmutableList.of("PLAYER_ID UUID PRIMARY KEY", "INVENTORY VARCHAR");

        try {
            SQLUtil.createTable("MAILBOX_INVENTORIES", columns);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static Optional<MailboxInventory> loadMailboxInventory(UUID playerID, Logger logger) {

        final MailboxInventory[] mailboxInventory = {null};

        try {
            SQLUtil.select("MAILBOX_INVENTORIES", "PLAYER_ID", playerID.toString(), resultSet -> {
                try {
                    if (resultSet.next()) {
                        mailboxInventory[0] = DataSerializer.deserializeMailboxInventory(resultSet.getString("INVENTORY"));
                    }
                } catch (SQLException | ObjectMappingException | IOException e) {
                    logger.error(e.getMessage());
                }
            });
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return Optional.ofNullable(mailboxInventory[0]);
    }

    public static void saveMailboxInventory(UUID playerID, MailboxInventory mailboxInventory, Logger logger) {

        try {
            SQLUtil.executeUpdate("MERGE INTO MAILBOX_INVENTORIES VALUES (?, ?)", preparedStatement -> {
                try {
                    preparedStatement.setObject(1, playerID);
                    preparedStatement.setString(2, DataSerializer.serializeMailboxInventory(mailboxInventory));
                } catch (SQLException | IOException | ObjectMappingException e) {
                    logger.error(e.getMessage());
                }
            });
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void saveMailboxInventories(Map<UUID, MailboxInventory> inventoryMap, Logger logger) {

        try {
            SQLUtil.executeBatch("MERGE INTO MAILBOX_INVENTORIES VALUES (?, ?)", preparedStatement -> {
                for (Map.Entry<UUID, MailboxInventory> entry : inventoryMap.entrySet()) {

                    try {
                        preparedStatement.setObject(1, entry.getKey());
                        preparedStatement.setObject(2, DataSerializer.serializeMailboxInventory(entry.getValue()));
                        preparedStatement.addBatch();
                    } catch (SQLException | IOException | ObjectMappingException e) {
                        logger.error(e.getMessage());
                    }
                }
            });
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
