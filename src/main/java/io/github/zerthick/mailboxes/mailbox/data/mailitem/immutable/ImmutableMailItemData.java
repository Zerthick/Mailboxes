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

package io.github.zerthick.mailboxes.mailbox.data.mailitem.immutable;

import io.github.zerthick.mailboxes.mailbox.data.mailitem.MailItemKeys;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.mutable.MailItemData;
import io.github.zerthick.mailboxes.util.DateFormatter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ImmutableMailItemData extends AbstractImmutableData<ImmutableMailItemData, MailItemData> {

    private final ItemStackSnapshot item;
    private final UUID sender;
    private final UUID receiver;
    private final String sendDate;
    private final boolean sent;

    public ImmutableMailItemData() {
        this(ItemStackSnapshot.NONE, new UUID(0, 0), new UUID(0, 0), DateFormatter.formatDate(ZonedDateTime.now()), false);
    }

    public ImmutableMailItemData(ItemStackSnapshot item, UUID sender, UUID receiver, String sendDate, boolean sent) {
        this.item = item;
        this.sender = sender;
        this.receiver = receiver;
        this.sendDate = sendDate;
        this.sent = sent;
        registerGetters();
    }

    private ItemStackSnapshot getItem() {
        return item;
    }

    private UUID getSender() {
        return sender;
    }

    private UUID getReceiver() {
        return receiver;
    }

    private String getSendDate() {
        return sendDate;
    }

    private boolean isSent() {
        return sent;
    }

    protected ImmutableValue<ItemStackSnapshot> item() {
        return Sponge.getRegistry().getValueFactory().createValue(MailItemKeys.MAIL_ITEM_SNAPSHOT, item).asImmutable();
    }

    protected ImmutableValue<UUID> sender() {
        return Sponge.getRegistry().getValueFactory().createValue(MailItemKeys.MAIL_ITEM_SENDER, sender).asImmutable();
    }

    protected ImmutableValue<UUID> receiver() {
        return Sponge.getRegistry().getValueFactory().createValue(MailItemKeys.MAIL_ITEM_RECEIVER, receiver).asImmutable();
    }

    protected ImmutableValue<String> sendDate() {
        return Sponge.getRegistry().getValueFactory().createValue(MailItemKeys.MAIL_ITEM_DATE, sendDate).asImmutable();
    }

    protected ImmutableValue<Boolean> sent() {
        return Sponge.getRegistry().getValueFactory().createValue(MailItemKeys.MAIL_ITEM_IS_SENT, sent).asImmutable();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(MailItemKeys.MAIL_ITEM_SNAPSHOT, this::getItem);
        registerKeyValue(MailItemKeys.MAIL_ITEM_SNAPSHOT, this::item);

        registerFieldGetter(MailItemKeys.MAIL_ITEM_SENDER, this::getSender);
        registerKeyValue(MailItemKeys.MAIL_ITEM_SENDER, this::sender);

        registerFieldGetter(MailItemKeys.MAIL_ITEM_RECEIVER, this::getReceiver);
        registerKeyValue(MailItemKeys.MAIL_ITEM_RECEIVER, this::receiver);

        registerFieldGetter(MailItemKeys.MAIL_ITEM_DATE, this::getSendDate);
        registerKeyValue(MailItemKeys.MAIL_ITEM_DATE, this::sendDate);

        registerFieldGetter(MailItemKeys.MAIL_ITEM_IS_SENT, this::isSent);
        registerKeyValue(MailItemKeys.MAIL_ITEM_IS_SENT, this::sent);
    }

    @Override
    public MailItemData asMutable() {
        return new MailItemData(item, sender, receiver, sendDate, sent);
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(MailItemKeys.MAIL_ITEM_SNAPSHOT, getItem())
                .set(MailItemKeys.MAIL_ITEM_SENDER, getSender())
                .set(MailItemKeys.MAIL_ITEM_RECEIVER, getReceiver())
                .set(MailItemKeys.MAIL_ITEM_DATE, getSendDate())
                .set(MailItemKeys.MAIL_ITEM_IS_SENT, isSent());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }
}
