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

package io.github.zerthick.mailboxes.mailbox.data.mailitem.mutable;

import io.github.zerthick.mailboxes.mailbox.data.mailitem.MailItemKeys;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.immutable.ImmutableMailItemData;
import io.github.zerthick.mailboxes.util.DateFormatter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class MailItemData extends AbstractData<MailItemData, ImmutableMailItemData> {

    private ItemStackSnapshot item;
    private UUID sender;
    private UUID receiver;
    private String sendDate;
    private boolean sent;

    public MailItemData() {
        this(ItemStackSnapshot.NONE, new UUID(0, 0), new UUID(0, 0), DateFormatter.formatDate(ZonedDateTime.now()), false);
    }

    public MailItemData(ItemStackSnapshot item, UUID sender, UUID receiver, String sendDate, boolean sent) {
        this.item = item;
        this.sender = sender;
        this.receiver = receiver;
        this.sendDate = sendDate;
        this.sent = sent;
        registerGettersAndSetters();
    }

    private ItemStackSnapshot getItem() {
        return item;
    }

    private void setItem(ItemStackSnapshot item) {
        this.item = item;
    }

    private UUID getSender() {
        return sender;
    }

    private void setSender(UUID sender) {
        this.sender = sender;
    }

    private UUID getReceiver() {
        return receiver;
    }

    private void setReceiver(UUID receiver) {
        this.receiver = receiver;
    }

    private String getSendDate() {
        return sendDate;
    }

    private void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    private boolean isSent() {
        return sent;
    }

    private void setSent(boolean sent) {
        this.sent = sent;
    }

    protected Value<ItemStackSnapshot> item() {
        return Sponge.getRegistry().getValueFactory().createValue(MailItemKeys.MAIL_ITEM_SNAPSHOT, item);
    }

    protected Value<UUID> sender() {
        return Sponge.getRegistry().getValueFactory().createValue(MailItemKeys.MAIL_ITEM_SENDER, sender);
    }

    protected Value<UUID> receiver() {
        return Sponge.getRegistry().getValueFactory().createValue(MailItemKeys.MAIL_ITEM_RECEIVER, receiver);
    }

    protected Value<String> sendDate() {
        return Sponge.getRegistry().getValueFactory().createValue(MailItemKeys.MAIL_ITEM_DATE, sendDate);
    }

    protected Value<Boolean> sent() {
        return Sponge.getRegistry().getValueFactory().createValue(MailItemKeys.MAIL_ITEM_IS_SENT, sent);
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(MailItemKeys.MAIL_ITEM_SNAPSHOT, this::getItem);
        registerFieldSetter(MailItemKeys.MAIL_ITEM_SNAPSHOT, this::setItem);
        registerKeyValue(MailItemKeys.MAIL_ITEM_SNAPSHOT, this::item);

        registerFieldGetter(MailItemKeys.MAIL_ITEM_SENDER, this::getSender);
        registerFieldSetter(MailItemKeys.MAIL_ITEM_SENDER, this::setSender);
        registerKeyValue(MailItemKeys.MAIL_ITEM_SENDER, this::sender);

        registerFieldGetter(MailItemKeys.MAIL_ITEM_RECEIVER, this::getReceiver);
        registerFieldSetter(MailItemKeys.MAIL_ITEM_RECEIVER, this::setReceiver);
        registerKeyValue(MailItemKeys.MAIL_ITEM_RECEIVER, this::receiver);

        registerFieldGetter(MailItemKeys.MAIL_ITEM_DATE, this::getSendDate);
        registerFieldSetter(MailItemKeys.MAIL_ITEM_DATE, this::setSendDate);
        registerKeyValue(MailItemKeys.MAIL_ITEM_DATE, this::sendDate);

        registerFieldGetter(MailItemKeys.MAIL_ITEM_IS_SENT, this::isSent);
        registerFieldSetter(MailItemKeys.MAIL_ITEM_IS_SENT, this::setSent);
        registerKeyValue(MailItemKeys.MAIL_ITEM_IS_SENT, this::sent);
    }

    @Override
    public Optional<MailItemData> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(MailItemData.class).ifPresent((data) -> {
            MailItemData finalData = overlap.merge(this, data);
            setItem(finalData.getItem());
            setSender(finalData.getSender());
            setReceiver(finalData.getReceiver());
            setSendDate(finalData.getSendDate());
            setSent(finalData.isSent());
        });
        return Optional.of(this);
    }

    @Override
    public Optional<MailItemData> from(DataContainer container) {
        container.getSerializable(MailItemKeys.MAIL_ITEM_SNAPSHOT.getQuery(), ItemStackSnapshot.class)
                .ifPresent(this::setItem);
        container.getObject(MailItemKeys.MAIL_ITEM_SENDER.getQuery(), UUID.class)
                .ifPresent(this::setSender);
        container.getObject(MailItemKeys.MAIL_ITEM_RECEIVER.getQuery(), UUID.class)
                .ifPresent(this::setReceiver);
        container.getString(MailItemKeys.MAIL_ITEM_DATE.getQuery())
                .ifPresent(this::setSendDate);
        container.getBoolean(MailItemKeys.MAIL_ITEM_IS_SENT.getQuery())
                .ifPresent(this::setSent);
        return Optional.of(this);
    }

    @Override
    public MailItemData copy() {
        return new MailItemData(item, sender, receiver, sendDate, sent);
    }

    @Override
    public ImmutableMailItemData asImmutable() {
        return new ImmutableMailItemData(item, sender, receiver, sendDate, sent);
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
