package io.github.zerthick.mailboxes.util.config.serializers;

import com.google.common.reflect.TypeToken;
import io.github.zerthick.mailboxes.inventory.MailboxInventory;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class MailboxInventorySerializer implements TypeSerializer<MailboxInventory> {

    public static void register() {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(MailboxInventory.class), new MailboxInventorySerializer());
    }

    @Override
    public MailboxInventory deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        final Map<Integer, ItemStackSnapshot> inventorySlots = value.getNode("inventory").getValue(new TypeToken<Map<Integer, ItemStackSnapshot>>() {
        });

        Inventory mailInventory = Inventory.builder().of(InventoryArchetypes.DOUBLE_CHEST)
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of("Mail")))
                .build(Sponge.getPluginManager().getPlugin("mailboxes").get());

        Iterator<Inventory> it = mailInventory.slots().iterator();
        int i = 0;
        while (it.hasNext()) {
            Slot slot = (Slot) it.next();
            if (inventorySlots.containsKey(i)) {
                slot.offer(inventorySlots.get(i).createStack());
            }
            i++;
        }

        final boolean unread = value.getNode("unread").getBoolean();

        return new MailboxInventory(mailInventory, unread);
    }

    @Override
    public void serialize(TypeToken<?> type, MailboxInventory obj, ConfigurationNode value) throws ObjectMappingException {

        Inventory mailInventory = obj.getMailInventory();

        Map<Integer, ItemStackSnapshot> inventorySlots = new HashMap<>();

        Iterator<Inventory> it = mailInventory.slots().iterator();
        int i = 0;
        while (it.hasNext()) {
            Optional<ItemStack> itemStackOptional = it.next().peek();
            if (itemStackOptional.isPresent()) {
                inventorySlots.put(i, itemStackOptional.get().createSnapshot());
            }
            i++;
        }

        value.getNode("inventory").setValue(new TypeToken<Map<Integer, ItemStackSnapshot>>() {
        }, inventorySlots);
        value.getNode("unread").setValue(TypeToken.of(Boolean.class), obj.isUnread());

    }
}
