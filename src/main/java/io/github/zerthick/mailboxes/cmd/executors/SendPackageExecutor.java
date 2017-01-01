package io.github.zerthick.mailboxes.cmd.executors;

import com.google.common.collect.ImmutableList;
import io.github.zerthick.mailboxes.Mailboxes;
import io.github.zerthick.mailboxes.cmd.CommandArgs;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.MailItemKeys;
import io.github.zerthick.mailboxes.mailbox.data.mailitem.mutable.MailItemData;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Optional;

public class SendPackageExecutor extends AbstractCommandExecutor {

    public SendPackageExecutor(Mailboxes plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            Player player = (Player) src;

            Optional<ItemStack> itemStackOptional = player.getItemInHand(HandTypes.MAIN_HAND);
            Optional<User> receiver = args.getOne(CommandArgs.RECEIVER);
            Optional<String> note = args.getOne(CommandArgs.NOTE);

            if (receiver.isPresent()) {

                if (itemStackOptional.isPresent()) {
                    ItemStack packageItem = ItemStack.of(ItemTypes.CHEST, 1);
                    packageItem.getOrCreate(MailItemData.class).ifPresent(mailItemData -> {
                        mailItemData.set(MailItemKeys.MAIL_ITEM_SNAPSHOT, itemStackOptional.get().createSnapshot());
                        mailItemData.set(MailItemKeys.MAIL_ITEM_SENDER, player.getUniqueId());
                        mailItemData.set(MailItemKeys.MAIL_ITEM_RECEIVER, receiver.get().getUniqueId());
                        packageItem.offer(mailItemData);
                    });
                    packageItem.offer(Keys.DISPLAY_NAME, Text.of("To: ", receiver.get().getName()));
                    note.ifPresent(s -> {
                        Text noteText = TextSerializers.FORMATTING_CODE.deserialize(s);
                        packageItem.offer(Keys.ITEM_LORE, ImmutableList.of(noteText));
                    });
                    player.setItemInHand(HandTypes.MAIN_HAND, packageItem);
                } else {
                    player.sendMessage(Text.of(TextColors.RED, "Hold the item in your main hand you wish to send!"));
                }
            }
        } else {
            src.sendMessage(Text.of("You can't send packages from the console!"));
        }

        return CommandResult.success();
    }
}
