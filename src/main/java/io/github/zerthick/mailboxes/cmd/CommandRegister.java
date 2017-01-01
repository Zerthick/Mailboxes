package io.github.zerthick.mailboxes.cmd;

import io.github.zerthick.mailboxes.Mailboxes;
import io.github.zerthick.mailboxes.cmd.executors.MailboxExecutor;
import io.github.zerthick.mailboxes.cmd.executors.SendPackageExecutor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandRegister {

    public static void registerCommands(Mailboxes plugin) {

        CommandSpec sendPackage = CommandSpec.builder()
                .permission("mailboxes.command.send.package")
                .description(Text.of("Send the item currently held in your hand as a package."))
                .arguments(GenericArguments.user(CommandArgs.RECEIVER), GenericArguments.optional(GenericArguments.remainingJoinedStrings(CommandArgs.NOTE)))
                .executor(new SendPackageExecutor(plugin))
                .build();

        CommandSpec send = CommandSpec.builder()
                .child(sendPackage, "package")
                .build();

        CommandSpec mailbox = CommandSpec.builder()
                .permission("mailboxes.command.info")
                .executor(new MailboxExecutor(plugin))
                .child(send, "send", "wrap")
                .build();

        Sponge.getCommandManager().register(plugin, mailbox, "mailboxes", "mb");
    }

}
