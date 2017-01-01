package io.github.zerthick.mailboxes.mailbox.data.mailitem.builder;

import io.github.zerthick.mailboxes.mailbox.data.mailitem.MailItemKeys;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.immutable.ImmutableMailItemData;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.mutable.MailItemData;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.Optional;
import java.util.UUID;

public class MailItemDataBuilder extends AbstractDataBuilder<MailItemData> implements DataManipulatorBuilder<MailItemData, ImmutableMailItemData> {

    public MailItemDataBuilder() {
        super(MailItemData.class, 1);
    }

    @Override
    public MailItemData create() {
        return new MailItemData();
    }

    @Override
    public Optional<MailItemData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    protected Optional<MailItemData> buildContent(DataView container) throws InvalidDataException {

        if (container.contains(MailItemKeys.MAIL_ITEM_SNAPSHOT.getQuery(),
                MailItemKeys.MAIL_ITEM_SENDER.getQuery(),
                MailItemKeys.MAIL_ITEM_RECEIVER.getQuery(),
                MailItemKeys.MAIL_ITEM_DATE.getQuery(),
                MailItemKeys.MAIL_ITEM_IS_SENT.getQuery())) {
            ItemStackSnapshot snapshot = container.getSerializable(MailItemKeys.MAIL_ITEM_SNAPSHOT.getQuery(), ItemStackSnapshot.class).get();
            UUID sender = container.getObject(MailItemKeys.MAIL_ITEM_SENDER.getQuery(), UUID.class).get();
            UUID receiver = container.getObject(MailItemKeys.MAIL_ITEM_RECEIVER.getQuery(), UUID.class).get();
            String date = container.getString(MailItemKeys.MAIL_ITEM_DATE.getQuery()).get();
            //Boolean sent = container.getBoolean(MailItemKeys.MAIL_ITEM_IS_SENT.getQuery()).get();

            //Weird because booleans are not serializing properly
            Boolean sent;
            Object isSent = container.get(MailItemKeys.MAIL_ITEM_IS_SENT.getQuery()).get();
            if (isSent instanceof Boolean) {
                sent = (Boolean) isSent;
            } else {
                sent = (Byte) isSent != 0;
            }

            return Optional.of(new MailItemData(snapshot, sender, receiver, date, sent));
        }

        return Optional.empty();
    }
}
