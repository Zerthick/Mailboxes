/*
 * Copyright (C) 2018  Zerthick
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

package io.github.zerthick.mailboxes.util.config;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import io.github.zerthick.mailboxes.Mailboxes;
import io.github.zerthick.mailboxes.inventory.MailboxInventory;
import io.github.zerthick.mailboxes.inventory.MailboxInventoryManager;
import io.github.zerthick.mailboxes.location.MailboxLocationManager;
import io.github.zerthick.mailboxes.util.config.serializers.MailboxInventorySerializer;
import io.github.zerthick.mailboxes.util.config.sql.SQLDataUtil;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.asset.Asset;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class ConfigManager {

    public static void registerSerializers() {
        MailboxInventorySerializer.register();
    }

    public static void createTables(Mailboxes plugin) {
        SQLDataUtil.createMailboxLocationsTable(plugin.getLogger());
        SQLDataUtil.createMailboxInventoriesTable(plugin.getLogger());
    }

    public static MailboxLocationManager loadMailboxLocationManager(Mailboxes plugin) {

        return new MailboxLocationManager(SQLDataUtil.loadMailboxLocations(plugin.getLogger()), plugin);
    }

    public static void saveMailboxLocationManager(MailboxLocationManager locationManager, Mailboxes plugin) {
        SQLDataUtil.saveMailboxLocations(locationManager.getLocations(), plugin.getLogger());
    }

    public static void deleteMailboxLocation(UUID worldID, Vector3i location, Mailboxes plugin) {
        SQLDataUtil.deleteMailboxLocation(worldID, location, plugin.getLogger());
    }

    public static MailboxInventory loadMailboxInventory(UUID playerUUID, Mailboxes plugin) {

        return SQLDataUtil.loadMailboxInventory(playerUUID, plugin.getLogger())
                .orElse(MailboxInventory.create(plugin.getInstance()));
    }

    public static void saveMailboxInventory(UUID playerUUID, MailboxInventory mailboxInventory, Mailboxes plugin) {
        SQLDataUtil.saveMailboxInventory(playerUUID, mailboxInventory, plugin.getLogger());
    }

    public static void saveMailboxInventories(MailboxInventoryManager inventoryManager, Mailboxes plugin) {

        SQLDataUtil.saveMailboxInventories(inventoryManager.getInventoryMap(), plugin.getLogger());
    }

    public static ConfigData loadConfigData(Mailboxes plugin) {

        Path path = plugin.getDefaultConfigPath();

        ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(path).build();

        //Generate default config if it doesn't exist
        if (!path.toFile().exists()) {
            Asset defaultConfigAsset = plugin.getInstance().getAsset("DefaultConfig.conf").get();
            plugin.getLogger().info("Generating default config...");
            try {
                defaultConfigAsset.copyToFile(path);
                configLoader.save(configLoader.load());
            } catch (IOException e) {
                plugin.getLogger().error("Error loading default config! Error: " + e.getMessage());
            }
        }

        try {
            CommentedConfigurationNode priceNode = configLoader.load().getNode("prices");
            CommentedConfigurationNode boxItemListNode = configLoader.load().getNode("mailboxBlocks");
            final double packagePrice = priceNode.getNode("package").getDouble(0);
            final Collection<String> mailBoxBlocks = new HashSet<>(boxItemListNode.getList(TypeToken.of(String.class)));
            return new ConfigData(packagePrice, mailBoxBlocks);

        } catch (IOException | ObjectMappingException e) {
            plugin.getLogger().error("Error loading config! Error: " + e.getMessage());
        }
        return new ConfigData(0, ImmutableSet.of("minecraft:chest"));
    }
}
