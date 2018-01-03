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

        Sponge.getCommandManager().register(plugin, mailbox, "mailboxes", "mb", "mail");
    }

}
