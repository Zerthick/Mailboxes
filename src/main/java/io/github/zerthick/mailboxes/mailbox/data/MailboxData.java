package io.github.zerthick.mailboxes.mailbox.data;

import io.github.zerthick.mailboxes.mailbox.data.mailitem.builder.MailItemDataBuilder;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.immutable.ImmutableMailItemData;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.mutable.MailItemData;
import org.spongepowered.api.Sponge;

public class MailboxData {

    public static void registerData() {
        Sponge.getDataManager().register(MailItemData.class, ImmutableMailItemData.class, new MailItemDataBuilder());
    }

}
