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

package io.github.zerthick.mailboxes.cmd.executors;

import io.github.zerthick.mailboxes.Mailboxes;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.plugin.PluginContainer;

public abstract class AbstractCommandExecutor implements CommandExecutor {

    protected PluginContainer container;
    protected Mailboxes plugin;

    public AbstractCommandExecutor(Mailboxes plugin) {
        this.plugin = plugin;
        this.container = plugin.getInstance();
    }
}
