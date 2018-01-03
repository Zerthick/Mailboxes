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
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.UUID;

public class MailItemKeys {

    public static final Key<Value<ItemStackSnapshot>> MAIL_ITEM_SNAPSHOT = KeyFactory.makeSingleKey(TypeToken.of(ItemStackSnapshot.class),
            new TypeToken<Value<ItemStackSnapshot>>() {
            }, DataQuery.of("MailItemSnapshot"), "mailboxes:mail_item_snapshot", "Mail Item Snapshot");

    public static final Key<Value<UUID>> MAIL_ITEM_SENDER = KeyFactory.makeSingleKey(TypeToken.of(UUID.class),
            new TypeToken<Value<UUID>>() {
            }, DataQuery.of("MailItemSender"), "mailboxes:mail_item_sender", "Mail Item Sender");

    public static final Key<Value<UUID>> MAIL_ITEM_RECEIVER = KeyFactory.makeSingleKey(TypeToken.of(UUID.class),
            new TypeToken<Value<UUID>>() {
            }, DataQuery.of("MailItemReceiver"), "mailboxes:mail_item_receiver", "Mail Item Receiver");

    public static final Key<Value<String>> MAIL_ITEM_DATE = KeyFactory.makeSingleKey(TypeToken.of(String.class),
            new TypeToken<Value<String>>() {
            }, DataQuery.of("MailItemDate"), "mailboxes:mail_item_date", "Mail Item Date");

    public static final Key<Value<Boolean>> MAIL_ITEM_IS_SENT = KeyFactory.makeSingleKey(TypeToken.of(Boolean.class),
            new TypeToken<Value<Boolean>>() {
            }, DataQuery.of("MailItemIsSent"), "mailboxes:mail_item_is_sent", "Mail Item Is Sent");
}
