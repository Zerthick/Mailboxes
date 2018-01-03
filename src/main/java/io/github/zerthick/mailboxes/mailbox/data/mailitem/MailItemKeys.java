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

package io.github.zerthick.mailboxes.mailbox.data.mailitem;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.UUID;

public class MailItemKeys {

    public static Key<Value<ItemStackSnapshot>> MAIL_ITEM_SNAPSHOT;

    public static Key<Value<UUID>> MAIL_ITEM_SENDER;

    public static Key<Value<UUID>> MAIL_ITEM_RECEIVER;

    public static Key<Value<String>> MAIL_ITEM_DATE;

    public static Key<Value<Boolean>> MAIL_ITEM_IS_SENT;

    public static void init() {
        MAIL_ITEM_SNAPSHOT = Key.builder()
                .type(new TypeToken<Value<ItemStackSnapshot>>() {
                })
                .query(DataQuery.of("MailItemSnapshot"))
                .id("mailboxes:mail_item_snapshot")
                .name("Mail Item Snapshot")
                .build();

        MAIL_ITEM_SENDER = Key.builder()
                .type(new TypeToken<Value<UUID>>() {
                })
                .query(DataQuery.of("MailItemSender"))
                .id("mailboxes:mail_item_sender")
                .name("Mail Item Sender")
                .build();

        MAIL_ITEM_RECEIVER = Key.builder()
                .type(new TypeToken<Value<UUID>>() {
                })
                .query(DataQuery.of("MailItemReceiver"))
                .id("mailboxes:mail_item_receiver")
                .name("Mail Item Receiver")
                .build();

        MAIL_ITEM_DATE = Key.builder()
                .type(new TypeToken<Value<String>>() {
                })
                .query(DataQuery.of("MailItemDate"))
                .id("mailboxes:mail_item_date")
                .name("Mail Item Date")
                .build();

        MAIL_ITEM_IS_SENT = Key.builder()
                .type(new TypeToken<Value<Boolean>>() {
                })
                .query(DataQuery.of("MailItemIsSent"))
                .id("mailboxes:mail_item_is_sent")
                .name("Mail Item Is Sent")
                .build();
    }
}
