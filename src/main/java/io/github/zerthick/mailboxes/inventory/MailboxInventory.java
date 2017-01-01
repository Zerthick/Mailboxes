package io.github.zerthick.mailboxes.inventory;

import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

public class MailboxInventory {

    private Inventory mailInventory;
    private boolean unread;

    public MailboxInventory(Inventory mailInventory, boolean unread) {
        this.mailInventory = mailInventory;
        this.unread = unread;
    }

    public static MailboxInventory create(PluginContainer instance) {
        Inventory defaultInventory = Inventory.builder().of(InventoryArchetypes.DOUBLE_CHEST)
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of("Mail")))
                .build(instance);

        return new MailboxInventory(defaultInventory, false);
    }

    public InventoryTransactionResult addMail(ItemStack mailItem) {
        InventoryTransactionResult result = mailInventory.offer(mailItem);

        if (result.getType() == InventoryTransactionResult.Type.SUCCESS) {
            unread = true;
        }

        return result;
    }

    public Inventory getMailInventory() {
        return mailInventory;
    }

    public void setMailInventory(Inventory mailInventory) {
        this.mailInventory = mailInventory;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}
