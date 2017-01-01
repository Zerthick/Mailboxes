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

package io.github.zerthick.mailboxes.util;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;
import io.github.zerthick.mailboxes.inventory.MailboxInventory;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataSerializer {

    public static String serializeMailboxInventory(MailboxInventory mailboxInventory) throws ObjectMappingException, IOException {
        ConfigurationNode node = HoconConfigurationLoader.builder().build().createEmptyNode();
        StringWriter stringWriter = new StringWriter();

        node.setValue(TypeToken.of(MailboxInventory.class), mailboxInventory);
        HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(stringWriter)).build().save(node);

        return stringWriter.toString();
    }

    public static MailboxInventory deserializeMailboxInventory(String mailboxInventory) throws ObjectMappingException, IOException {
        ConfigurationNode node = HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(new StringReader(mailboxInventory))).build().load();
        return node.getValue(TypeToken.of(MailboxInventory.class));
    }

    public static String serializeMailboxLocationManagerList(Set<Vector3i> locationList) throws ObjectMappingException, IOException {
        ConfigurationNode node = HoconConfigurationLoader.builder().build().createEmptyNode();
        StringWriter stringWriter = new StringWriter();

        node.getNode("data").setValue(new TypeToken<List<Vector3i>>() {
        }, new ArrayList<>(locationList));
        HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(stringWriter)).build().save(node);

        return stringWriter.toString();
    }

    public static Set<Vector3i> deserializeMailboxLocationManagerList(String locationList) throws ObjectMappingException, IOException {
        ConfigurationNode node = HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(new StringReader(locationList))).build().load();
        return new HashSet<>(node.getNode("data").getValue(new TypeToken<List<Vector3i>>() {
        }));
    }

}
