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

package io.github.zerthick.mailboxes.mailbox.data;

import io.github.zerthick.mailboxes.mailbox.data.mailitem.builder.MailItemDataBuilder;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.immutable.ImmutableMailItemData;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.mutable.MailItemData;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.plugin.PluginContainer;

public class MailboxData {

    public static void registerData(PluginContainer container) {
        DataRegistration.<MailItemData, ImmutableMailItemData>builder()
                .dataClass(MailItemData.class)
                .immutableClass(ImmutableMailItemData.class)
                .builder(new MailItemDataBuilder())
                .manipulatorId("mail_item")
                .dataName("Mail Item Data")
                .buildAndRegister(container);
    }

}
